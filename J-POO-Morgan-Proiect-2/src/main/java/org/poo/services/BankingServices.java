package org.poo.services;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.utils.CommandManager;

import java.util.Map;

public class BankingServices {
    /**
     * Realizeaza design-ul visitor pentru banca
     * @param visitor reprezinta un visitor
     */
    public void acceptVisitor(final BankMethods visitor) {
        visitor.visit(Bank.getInstance());
    }

    /**
     * Executa comanda data ca input
     * @param input comanda si toate datele pentru a putea fi executata
     */
    public void performCommand(final CommandInput input) {
        Command c = CommandManager.getConcreteCommand(input.getCommand());
        c.execute(input);
    }

    /**
     * Adauga un cont nou
     * @param account cont
     * @param accountNameorIBAN iban-ul sau alias-ul contului
     * @param userEmail email-ul user-ului
     */
    public void addAccount(final Account account, final String accountNameorIBAN,
                           final String userEmail) {
        account.setUser(Bank.getInstance().getUsers().get(userEmail));
        Bank.getInstance().getUsers().get(userEmail).getAccounts().put(accountNameorIBAN, account);
        Bank.getInstance().getAccounts().put(accountNameorIBAN, account);
    }

    /**
     * Adauga un card nou
     * @param card cardul
     * @param account contul ce detine cardul
     */
    public void addCard(final Card card, final Account account) {
        card.setAccount(account);
        account.getCards().put(card.getCardNumber(), card);
        Bank.getInstance().getCards().put(card.getCardNumber(), card);
    }

    /**
     * Adauga bani pe un cont dat
     * @param accountNameorIBAN contul dat
     * @param amount suma
     */
    public void addFounds(final String accountNameorIBAN, final double amount) {
        Account account = Bank.getInstance().getAccounts().get(accountNameorIBAN);
        if (account == null) {
            System.err.println("contul nu a fost gasit: " + accountNameorIBAN);
            return;
        }
        account.setBalance(account.getBalance() + amount);
    }

    /**
     * Sterge contul si cardurile lui daca balanta sa este 0
     *
     * @param emailUser         emailul user-ului
     * @param accountNameorIBAN IBAN-ul sau alias-ul contului
     * @return advarat daca s-a sters, fals altfel
     */
    public boolean removeAccount(final String emailUser, final String accountNameorIBAN) {
        Account account = Bank.getInstance().getUsers().get(emailUser).getAccounts()
                .get(accountNameorIBAN);
        if (account.getBalance() != 0) {
            return false;
        }
        for (Card c : account.getCards().values()) {
            Bank.getInstance().getCards().remove(c.getCardNumber());
        }
        account.getCards().clear();
        Bank.getInstance().getUsers().get(emailUser).getAccounts().remove(accountNameorIBAN);
        Bank.getInstance().getAccounts().remove(accountNameorIBAN);
        return true;
    }

    /**
     * Stergera unui card
     * @param emailUser email-ul user-ului
     * @param cardNumber numarul cardului
     */
    public void deleteCard(final String emailUser, final String cardNumber) {
        Map<String, Account> userAccounts = Bank.getInstance().getUsers().get(emailUser)
                .getAccounts();
        for (Account account : userAccounts.values()) {
            if (account.getCards().containsKey(cardNumber)) {
                Bank.getInstance().getCardDeletedHistory().put(cardNumber, account.getCards()
                        .get(cardNumber));
                account.getCards().remove(cardNumber);
                Bank.getInstance().getCards().remove(cardNumber);
                break;
            }
        }
    }
}
