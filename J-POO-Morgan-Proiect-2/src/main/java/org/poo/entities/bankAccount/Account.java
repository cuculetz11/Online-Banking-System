package org.poo.entities.bankAccount;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.entities.CurrencyPair;
import org.poo.entities.card.Card;
import org.poo.entities.transaction.Transaction;
import org.poo.entities.User;
import org.poo.fileio.CommandInput;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @JsonIgnore
    private double minimumBalance;
    @Getter
    @JsonIgnore
    private ArrayList<Transaction> transactionsHistory;

    public Account(final double balance, final String currency, final String type) {
        this.iban = Utils.generateIBAN();
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new LinkedHashMap<>();
        this.minimumBalance = 0;
        this.transactionsHistory = new ArrayList<>();
    }

    /**
     * Realizeaza transferul unei sume din contul curent in contul receiveru-ului
     * @param receiver contul celui ce primeste suma de bani
     * @param amount suma trasferata
     */
    public void transfer(final Account receiver, final double amount) {
        this.setBalance(this.getBalance() - amount);
        CurrencyPair currencyPair = new CurrencyPair(this.currency, receiver.getCurrency());
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        double exchangedAmount = currencyExchangeService
                .exchangeCurrency(currencyPair, amount);
        receiver.setBalance(receiver.getBalance() + exchangedAmount);
    }

    /**
     * Realizeaza o plata
     * @param amount suma platii
     */
    public void pay(final double amount) {
        this.setBalance(this.getBalance() - amount);
    }

    /**
     * Verifica daca balanta contului e mai mare decat minimul pus
     * @return adevarat daca balanta e mai mica sau egala cu minimul, fals altfel
     */
    public boolean verifyBalance() {
        return minimumBalance >= balance;
    }

    /**
     * Verifica daca se poate realiza un transfer
     * @param amount suma ce trebuire transferata
     * @return adevarat daca transferul nu e posibil, fals altfel
     */
    public boolean isTransferPossible(final double amount) {
        return !(this.getBalance() > amount);
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

}
