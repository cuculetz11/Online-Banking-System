package org.poo.entities.bankAccount;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.command.debug.dto.AccountDeleteInfo;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.command.debug.dto.DeleteAccountDTO;
import org.poo.command.debug.dto.ErrorPrint;
import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.card.Card;
import org.poo.entities.transaction.Transaction;
import org.poo.entities.users.User;
import org.poo.fileio.CommandInput;
import org.poo.services.BankingServices;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.*;

import java.util.*;

@Setter
public abstract class Account {
    private String iban;
    @Getter
    private double balance;
    @Getter
    private String currency;
    @Getter
    private String type;
    private LinkedHashMap<String, Card> cards;
    @Getter
    @JsonIgnore
    private User user;
    @Getter
    @JsonIgnore
    private double minimumBalance;
    @Getter
    @JsonIgnore
    private ArrayList<Transaction> transactionsHistory;
    @Getter
    @JsonIgnore
    private double cashbackSpending;
    @Getter
    @JsonIgnore
    private HashMap<String, Integer> cashbackNrOfTransactions;
    @Getter
    @JsonIgnore
    private HashSet<String> usedCashback;

    public Account(final double balance, final String currency, final String type) {
        this.iban = Utils.generateIBAN();
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new LinkedHashMap<>();
        this.minimumBalance = 0;
        this.transactionsHistory = new ArrayList<>();
        this.usedCashback = new HashSet<>();
        this.cashbackNrOfTransactions = new HashMap<>();
        this.cashbackSpending = 0;
    }

    /**
     * Realizeaza transferul unei sume din contul curent in contul receiveru-ului
     * @param receiver contul celui ce primeste suma de bani
     * @param amount suma trasferata
     */
    public void transfer(final Account receiver, final double amount) {
        pay(amount);
        if (receiver != null) {
            CurrencyPair currencyPair = new CurrencyPair(this.currency, receiver.getCurrency());
            CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
            double exchangedAmount = currencyExchangeService
                    .exchangeCurrency(currencyPair, amount);
            receiver.setBalance(receiver.getBalance() + exchangedAmount);
        } else {
            this.getUser().getPlan().checkUpdate(amount, this.getCurrency(), this);
        }
    }

    /**
     * Realizeaza o plata
     * @param amount suma platii
     */
    public void pay(final double amount) {
        this.setBalance(this.getBalance() - amount
                - this.getUser().getPlan().getCommissionPlan().commission(amount, currency));
    }

    /**
     * Verifica daca balanta contului e mai mare decat minimul pus
     * @return adevarat daca balanta e mai mica sau egala cu minimul, fals altfel
     */
    public boolean verifyBalance() {
        return minimumBalance >= balance;
    }

    /**
     * Se verifica daca se poate efectua o anumita operatie
     * @param amount suma pentru operatia respectiva
     * @return un sir de caractere ce reprezinta rezultatul
     */
    public String isTransferPossible(final double amount) {
        if (!(this.getBalance() >= (amount + this.getUser()
                .getPlan().getCommissionPlan().commission(amount, getCurrency())))) {
            return Constants.TRANSACTION_IMPOSSIBLE;
        }
        return "";
    }

    /**
     * Adauga dobanda obtinuta la suma din contul dat(trebuie sa fie de economii contul)
     * @param input datale necesare
     */
    public abstract void addInterest(CommandInput input);

    /**
     * Schimba dobanda contutlui de economii
     * @param input datele necesare
     */
    public abstract void changeInterestRate(CommandInput input);

    /**
     *  Raport de cheltuilei pentru un cont classic
     * @param input datale necesare
     */
    public abstract void spendingReport(CommandInput input);

    /**
     * Adugam bani in cont
     * @param input comanda de input
     */
    public void deposit(final CommandInput input) {
        if (!input.getEmail().equals(this.getUser().getEmail())) {
            return;
        }
        setBalance(getBalance() + input.getAmount());
    }

    /**
     * Setam o limita a balantei
     * @param input comada de input
     */
    public void setMinBalance(final CommandInput input) {
        setMinimumBalance(input.getAmount());
    }

    /**
     * Stergem un card
     * @param input comanda de input
     */
    public void deleteCard(final CommandInput input) {
        String ibanInput = Bank.getInstance().getCards().get(input.getCardNumber()).getAccount()
                .getIban();
        BankingServices bankingServices = new BankingServices();
        if (this.getBalance() != 0) {
            return;
        }
        bankingServices.deleteCard(this, input.getCardNumber());


        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.THE_CARD_HAS_BEEN_DESTROYED,
                        input.getTimestamp())
                        .transactionName(Constants.DELETE_CARD_TRANSACTION)
                        .cardNumber(input.getCardNumber())
                        .userEmail(input.getEmail())
                        .iban(ibanInput)
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
    }

    /**
     * Stergem contul dat in comanda
     * @param input comanda de input
     */
    public void deleteAccount(final CommandInput input) {
        AccountDeleteInfo data = null;
        if (this.getBalance() > 0) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.ACCOUNT_CANT_BE_DELETED_FUNDS,
                            input.getTimestamp())
                            .transactionName(Constants.DELETE_ACCOUNT_FAIL_TRANSACTION)
                            .userEmail(input.getEmail())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            data = new ErrorPrint(Constants.ACCOUNT_CANT_BE_DELETED, input.getTimestamp());
            DebugActionsDTO<AccountDeleteInfo> wasAccountDeleted =
                    new DebugActionsDTO<>(input.getCommand(), data, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(wasAccountDeleted);
            return;
        }

        for (Card c : this.getCards().values()) {
            Bank.getInstance().getCards().remove(c.getCardNumber());
        }
        this.getCards().clear();
        Bank.getInstance().getUsers().get(input.getEmail()).getAccounts().remove(this.getIban());
        Bank.getInstance().getAccounts().remove(this.getIban());

        data = new DeleteAccountDTO(Constants.ACCOUNT_DELETED, input.getTimestamp());
        DebugActionsDTO<AccountDeleteInfo> wasAccountDeleted =
                new DebugActionsDTO<>(input.getCommand(), data, input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(wasAccountDeleted);
    }

    /**
     * Obstine cardurile ca o lista
     * @return cardurile ca o lista
     */
    @JsonGetter("cards")
    public List<Card> getCardsAsList() {
        return new ArrayList<>(cards.values());
    }

    /**
     * returneaza ibanul contului
     * @return ibanul contului
     */
    @JsonGetter("IBAN")
    public String getIban() {
        return iban;
    }

    /**
     *  retruneaza map-ul ce contine cardurile
     * @return map-ul cu carduri
     */
    public Map<String, Card> getCards() {
        return cards;
    }

    /**
     * Adugam bani in cont
     * @param amount suma pe care vrem sa o daugam
     * @param currencyInput currency-ul in care vine suma data
     */
    public void addMoney(final double amount, final String currencyInput) {
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        double amountToAdd = currencyExchangeService.exchangeCurrency(
                new CurrencyPair(currencyInput, this.getCurrency()), amount);
        this.setBalance(this.getBalance() + amountToAdd);
    }

    /**
     * Scoatem bani de pe cont
     * @param amount suma pe care vrem sa o scoatem
     */
    public void withdrawMoney(final double amount) {
        this.setBalance(this.getBalance() - amount
                - this.getUser().getPlan().getCommissionPlan().commission(amount, currency));
    }

    /**
     * Platim fara comision(pentru Split)
     * @param amount suma pe care o platim
     */
    public void paySplit(final double amount) {
        this.setBalance(this.getBalance() - amount);
    }

    /**
     * Verificam daca operatia e posibila fara comision
     * @param amount suma pentru care verificam
     * @return adevarat daca nu putem, fals altfel
     */
    public boolean isTransferPossibleWhCommision(final double amount) {
        if (this.getBalance() >= amount) {
            return false;
        }
        return true;
    }

    /**
     * Verificam daca emailul dat e si cel ce detine contul
     * @param email emailul userului pentru care verificam
     * @return adevarat daca userul nu e proprietarul, fals altfel
     */
    public boolean checkPropriety(final String email) {
        if (!this.getUser().getEmail().equals(email)) {
            return true;
        }
        return false;
    }

}
