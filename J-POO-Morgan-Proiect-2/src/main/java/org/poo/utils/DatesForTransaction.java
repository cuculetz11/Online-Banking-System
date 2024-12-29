package org.poo.utils;

import lombok.Getter;
import org.poo.entities.bankAccount.Account;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DatesForTransaction {
    private final String transactionName;
    private final String description;
    private final int timestamp;
    private final String cardNumber;
    private final String userEmail;
    private final String iban;
    private final double amount;
    private final String commerciant;
    private final String senderIban;
    private final String receiverIban;
    private final String transferType;
    private final String money;
    private final String currency;
    private final List<String> accounts;
    private final ArrayList<Account> accountsList;
    private final String errorMessage;

    public DatesForTransaction(final Builder builder) {
        this.transactionName = builder.transactionName;
        this.description = builder.description;
        this.timestamp = builder.timestamp;
        this.cardNumber = builder.cardNumber;
        this.userEmail = builder.userEmail;
        this.iban = builder.iban;
        this.amount = builder.amount;
        this.commerciant = builder.commerciant;
        this.senderIban = builder.senderIban;
        this.receiverIban = builder.receiverIban;
        this.transferType = builder.transferType;
        this.money = builder.money;
        this.currency = builder.currency;
        this.accounts = builder.accounts;
        this.accountsList = builder.accountsList;
        this.errorMessage = builder.errorMessage;

    }
    public static class Builder {
        private String transactionName;
        private final String description;
        private final int timestamp;
        private String cardNumber;
        private String userEmail;
        private String iban;
        private double amount;
        private String commerciant;
        private String senderIban;
        private String receiverIban;
        private String transferType;
        private String money;
        private String currency;
        private List<String> accounts;
        private ArrayList<Account> accountsList;
        private String errorMessage;


        public Builder(final String description, final int timestamp) {
            this.description = description;
            this.timestamp = timestamp;
        }

        /**
         * Seteaza numele tranzactiei
         * @param transactionName1 numele tranzactiei
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder transactionName(final String transactionName1) {
            this.transactionName = transactionName1;
            return this;
        }

        /**
         * Seteaza numarul cardului
         * @param cardNumber1 numarul cardului
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder cardNumber(final String cardNumber1) {
            this.cardNumber = cardNumber1;
            return this;
        }

        /**
         * Seteaza email-ul user-ului
         * @param userEmail1 emailul user-ului
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder userEmail(final String userEmail1) {
            this.userEmail = userEmail1;
            return this;
        }

        /**
         * Seteaza IBAN-ul contului
         * @param iban1 IBAN-ul contului
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder iban(final String iban1) {
            this.iban = iban1;
            return this;
        }

        /**
         * Seteaza suma de bani
         * @param amount1 suma de bani
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder amount(final double amount1) {
            this.amount = amount1;
            return this;
        }

        /**
         * Seteaza numele comerciantului
         * @param commerciant1 numele comerciantului
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder commerciant(final String commerciant1) {
            this.commerciant = commerciant1;
            return this;
        }

        /**
         * Seteaza IBAN-ul celui ce a trimis bani
         * @param senderIban1 IBAN-ul celui ce a trimis bani
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder senderIban(final String senderIban1) {
            this.senderIban = senderIban1;
            return this;
        }

        /**
         * Seteaza IBAN-ul celui ce a primit bani
         * @param receiverIban1 IBAN-ul ceului ce a primit bani
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder receiverIban(final String receiverIban1) {
            this.receiverIban = receiverIban1;
            return this;
        }

        /**
         *
         * @param transferType1 tipul transferului
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder transferType(final String transferType1) {
            this.transferType = transferType1;
            return this;
        }

        /**
         *
         * @param money1 suma de bani
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder money(final String money1) {
            this.money = money1;
            return this;
        }

        /**
         *
         * @param currency1 valuta
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder currency(final String currency1) {
            this.currency = currency1;
            return this;
        }

        /**
         *
         * @param accounts1 lista de conturi
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder accounts(final List<String> accounts1) {
            this.accounts = accounts1;
            return this;
        }

        /**
         *
         * @return o instanta complet construita a clasei {@link DatesForTransaction}
         */
        public DatesForTransaction build() {
            return new DatesForTransaction(this);
        }

        /**
         *
         * @param errorMessage1 mesaj de eroare
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder errorMessage(final String errorMessage1) {
            this.errorMessage = errorMessage1;
            return this;
        }

        /**
         *
         * @param accountsList1 lista de conturi
         * @return instanta curenta a builder-ului pentru a face acel lant
         */
        public Builder accountsList(final ArrayList<Account> accountsList1) {
            this.accountsList = accountsList1;
            return this;
        }
    }

}
