package org.poo.entities.bankAccount;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.entities.CurrencyPair;
import org.poo.entities.card.Card;
import org.poo.entities.transaction.Transaction;
import org.poo.entities.users.User;
import org.poo.fileio.CommandInput;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Constants;
import org.poo.utils.Utils;
import org.poo.utils.observer.BalanceObserverPrecision;

import java.util.*;

@Setter
public abstract class Account implements BalanceObserverPrecision {
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
        this.setBalance(this.getBalance() - amount - this.getUser().getPlan().getCommissionPlan().commission(amount, currency));
    }

    /**
     * Verifica daca balanta contului e mai mare decat minimul pus
     * @return adevarat daca balanta e mai mica sau egala cu minimul, fals altfel
     */
    public boolean verifyBalance() {
        return minimumBalance >= balance;
    }

    /**
     */
    public String isTransferPossible(final double amount) {
        if(!(this.getBalance() >= (amount + this.getUser().getPlan().getCommissionPlan().commission(amount, getCurrency())))) {
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

    public abstract void deposit(CommandInput input);

    public abstract void setMinBalance(CommandInput input);

    public abstract void deleteCard(CommandInput input);

    public abstract void deleteAccount(CommandInput input);

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

    public void addMoney(double amount, String currency) {
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        amount = currencyExchangeService.exchangeCurrency(new CurrencyPair(currency, this.getCurrency()), amount);
        this.setBalance(this.getBalance() + amount);
    }
    public void withdrawMoney(double amount) {
        this.setBalance(this.getBalance() - amount - this.getUser().getPlan().getCommissionPlan().commission(amount, currency));
    }
    public void paySplit(double amount) {
        this.setBalance(this.getBalance() - amount);
    }
    public boolean isTransferPossibleWhCommision(final double amount) {
        if(this.getBalance() >= amount ) {
            return true;
        }
        return false;
    }
    public boolean checkPropriety(String email) {
        if(!this.getUser().getEmail().equals(email)) {
            return false;
        }
        return true;
    }
//    @Override
//    public void checkBalancePrecision() {
//        this.balance = Math.round(this.balance * 100000.0) / 100000.0;
//    }
}
