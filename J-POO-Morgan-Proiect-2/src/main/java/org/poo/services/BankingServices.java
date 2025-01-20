package org.poo.services;

import org.poo.command.Command;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.command.debug.dto.PaymentPerWorkerDTO;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.bankAccount.BusinessAccount;
import org.poo.entities.card.Card;
import org.poo.entities.transaction.Transaction;
import org.poo.entities.users.User;
import org.poo.entities.users.UserBusinessInfo;
import org.poo.fileio.CommandInput;
import org.poo.services.splitPayment.WaitingSplitPayment;
import org.poo.utils.CommandManager;
import org.poo.utils.Constants;
import org.poo.utils.ErrorManager;
import org.poo.utils.JsonOutManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

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
        Bank.getInstance().setCurrentInput(input);
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
     *
     */
    public void addFounds(final CommandInput input) {
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if (account == null) {
            System.err.println("contul nu a fost gasit: " + input.getAccount());
            return;
        }
        account.deposit(input);
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
     * Se ocupa de logica de stergere a unui card
     * @param account contul cardului
     * @param cardNumber numarul cardului
     */
    public void deleteCard(final Account account, final String cardNumber) {
        Bank.getInstance().getCardDeletedHistory().put(cardNumber, account.getCards()
                .get(cardNumber));
        account.getCards().remove(cardNumber);
        Bank.getInstance().getCards().remove(cardNumber);
    }

    /**
     * Face clasa DTO ce contine numele muncitorului si cat a cheltui si depus el in contul business
     * @param workers muncitorii din cont
     * @param transactionHistoryWorkers contine istoricul de depuneri sau plati a fiecarui muncitor
     * @param input comanda de input
     * @param spendingListPerWorker pentru fiecare muncitor cat a cheluit si depus
     * @param total reprezinta un vector de 2 elemente ce are total[0] = totalSpent
     *              total[1] = totalDeposited
     */
    public void makeTransactionListPerWorker(
            final  LinkedHashSet<String> workers, final TreeMap<String,
            List<UserBusinessInfo>> transactionHistoryWorkers,
            final CommandInput input, final ArrayList<PaymentPerWorkerDTO> spendingListPerWorker,
            final double[] total) {
        for (String workerEmail : workers) {
            List<UserBusinessInfo> managerHistoryTransaction =
                    transactionHistoryWorkers.get(workerEmail);

            User managerUser = Bank.getInstance().getUsers().get(workerEmail);
            String name = managerUser.getLastName() + " " + managerUser.getFirstName();
            PaymentPerWorkerDTO paymentPerWorkerDTO = new PaymentPerWorkerDTO(name);
            if (managerHistoryTransaction != null) {
                for (UserBusinessInfo userBusinessInfo : managerHistoryTransaction) {
                    if (userBusinessInfo.getTimestamp() > input.getEndTimestamp()) {
                        break;
                    }
                    if (userBusinessInfo.getTimestamp() >= input.getStartTimestamp()) {
                        paymentPerWorkerDTO.addDeposited(userBusinessInfo.getDeposit());
                        paymentPerWorkerDTO.addSpent(userBusinessInfo.getSpent());
                    }
                }
            }
            spendingListPerWorker.add(paymentPerWorkerDTO);
            total[0] += paymentPerWorkerDTO.getSpent();
            total[1] += paymentPerWorkerDTO.getDeposited();
        }
    }

    /**
     * Verifica daca se poate pune limita respectiva contului business dat
     * @param description descrierea erorii in caz ca nu e owner-ul
     * @return adevarat daca se poate pune limita, fals altfel
     */
    public boolean checkSetBusinessLimits(final String description) {
        CommandInput input = Bank.getInstance().getCurrentInput();
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if (account == null) {
            return false;
        }
        if (!account.getType().equals(Constants.BUSINESS)) {
            ErrorManager.notFound(Constants.THIS_IS_NOT_A_BUSINESS_ACCOUNT,
                    input.getCommand(), input.getTimestamp());
            return false;
        }
        BusinessAccount businessAccount = (BusinessAccount) account;
        if (!businessAccount.getUser().getEmail().equals(input.getEmail())) {
            Transaction err = new Transaction(input.getTimestamp(),
                    description);
            DebugActionsDTO<Transaction> debugActionsDTO = new DebugActionsDTO<>(input.getCommand(),
                    err, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(debugActionsDTO);
            return false;
        }
        return true;
    }

    /**
     * Cauta Split Payment-ul prin toate ce se desfasoara in acelasi timp
     * @param input comanda din input
     * @return un obiect ce contine info depre acel split daca exista pentru userul dat
     */
    public WaitingSplitPayment acceptOrRejectSplitPayment(final CommandInput input) {
        if (!Bank.getInstance().getUsers().containsKey(input.getEmail())) {
            ErrorManager.notFound(Constants.USER_NOT_FOUND, input.getCommand(),
                    input.getTimestamp());
            return null;
        }
        WaitingSplitPayment waitingSplitPayment = null;
        if (Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType()) == null) {
            return null;
        }
        for (WaitingSplitPayment w : Bank.getInstance().getWaitingSplitPayments().get(input
                .getSplitPaymentType())) {
            if (w.getRemainedPayments().contains(input.getEmail())) {
                waitingSplitPayment = w;
                break;
            }
        }
        return waitingSplitPayment;
    }
}
