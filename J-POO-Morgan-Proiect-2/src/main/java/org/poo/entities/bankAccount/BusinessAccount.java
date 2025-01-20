package org.poo.entities.bankAccount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.command.debug.dto.*;
import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.card.Card;
import org.poo.entities.commerciant.CommerciantBusinessInfo;
import org.poo.entities.users.User;
import org.poo.entities.users.UserBusinessInfo;
import org.poo.fileio.CommandInput;
import org.poo.services.BankingServices;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.*;

import java.util.*;

/**
 * Userul este ownerul acestui cont
 */
@Getter
public class BusinessAccount extends Account {
    @JsonIgnore
    private final LinkedHashSet<String> managers;
    @JsonIgnore
    private final LinkedHashSet<String> employees;
    @JsonIgnore
    private final TreeMap<String, List<UserBusinessInfo>> transactionHistoryWorkers;
    @JsonIgnore
    private final ArrayList<CommerciantBusinessInfo> transactionHistoryCommerciants;
    @JsonIgnore
    @Setter
    private double payLimit;
    @JsonIgnore
    @Setter
    private double depositLimit;

    public BusinessAccount(final CommandInput input) {
        super(0, input.getCurrency(), input.getAccountType());
        managers = new LinkedHashSet<>();
        employees = new LinkedHashSet<>();
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        payLimit = currencyExchangeService.exchangeCurrency(new CurrencyPair("RON",
                input.getCurrency()), Constants.INITIAL_LIMIT);
        depositLimit = payLimit;
        transactionHistoryWorkers = new TreeMap<>();
        transactionHistoryCommerciants = new ArrayList<>();
    }

    /**
     * Daca e posibila o anumita operatie cu tot cu comision
     * @param amount suma pentru operatia respectiva
     * @return un sir de caractere ce contine rezultatul obtinut
     */
    @Override
    public String isTransferPossible(final double amount) {

        if (employees.contains(Bank.getInstance().getCurrentInput().getEmail()) && amount
                + this.getUser().getPlan().getCommissionPlan().commission(amount, getCurrency())
                >= payLimit) {
            return Constants.LIMIT_EXCEEDED;
        }
        if (this.getBalance() < (amount + this.getUser().getPlan().getCommissionPlan().commission(
                amount, getCurrency()))) {
            return Constants.TRANSACTION_IMPOSSIBLE;
        }
        return "";
    }

    /**
     * Eroare pentru acest tip de cont
     * @param input datale necesare
     */
    @Override
    public void addInterest(final CommandInput input) {
        ErrorManager.notFound(Constants.NOT_SAVINGS_ACCOUNT, input.getCommand(),
                input.getTimestamp());
    }

    /**
     * Eorare pentru acest tip de cont
     * @param input datele necesare
     */
    @Override
    public void changeInterestRate(final CommandInput input) {
        ErrorManager.notFound(Constants.NOT_SAVINGS_ACCOUNT, input.getCommand(),
                input.getTimestamp());
    }

    /**
     * Se fac ambele raporate caracteristice acestui tip de cont
     * @param input datale necesare
     */
    @Override
    public void spendingReport(final CommandInput input) {
        if (input.getType().equals(Constants.TRANSACTION)) {
            ArrayList<PaymentPerWorkerDTO> manager = new ArrayList<>();
            ArrayList<PaymentPerWorkerDTO> employee = new ArrayList<>();
            double[] total = new double[2];
            BankingServices bankingServices = new BankingServices();
            bankingServices.makeTransactionListPerWorker(managers, transactionHistoryWorkers,
                    input, manager, total);
            bankingServices.makeTransactionListPerWorker(employees, transactionHistoryWorkers,
                    input, employee, total);
            BusinessReportTransaction businessReportTransaction =
                    new BusinessReportTransaction(this.getIban(), input.getType(),
                            this.getBalance(), this.getCurrency(), this.payLimit,
                            this.depositLimit, manager, employee, total[0], total[1]);
            DebugActionsDTO<BusinessReportTransaction> debugActionsDTO = new DebugActionsDTO<>(
                    input.getCommand(), businessReportTransaction, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(debugActionsDTO);

        } else if (input.getType().equals(Constants.COMMERCIANT)) {
            TreeMap<String, CommerciantDTO> totalReceivedPerCommerciant = new TreeMap<>();

            for (CommerciantBusinessInfo c : transactionHistoryCommerciants) {
                if (c.getTimestamp() > input.getEndTimestamp()) {
                    break;
                }
                if (c.getTimestamp() >= input.getStartTimestamp()) {
                    if (getUser().getEmail().equals(c.getUserEmail())) {
                        continue;
                    }
                    totalReceivedPerCommerciant.putIfAbsent(c.getCommerciantName(),
                            new CommerciantDTO(c.getCommerciantName(), 0,
                                    new ArrayList<>(), new ArrayList<>()));
                    totalReceivedPerCommerciant.get(c.getCommerciantName()).addMoney(c.getAmount());

                    if (employees.contains(c.getUserEmail())) {
                        User user = Bank.getInstance().getUsers().get(c.getUserEmail());
                        String name = user.getLastName() + " " + user.getFirstName();
                        totalReceivedPerCommerciant.get(c.getCommerciantName()).addEmployer(name);
                    }
                    if (managers.contains(c.getUserEmail())) {
                        User user = Bank.getInstance().getUsers().get(c.getUserEmail());
                        String name = user.getLastName() + " " + user.getFirstName();
                        totalReceivedPerCommerciant.get(c.getCommerciantName()).addManger(name);
                    }
                }
            }
            DebugActionsDTO<BusinessReportCommerciant> debugActionsDTO =
                    getBusinessReportCommerciantDebugActionsDTO(input,
                            totalReceivedPerCommerciant);
            JsonOutManager.getInstance().addToOutput(debugActionsDTO);
        }
    }

    private DebugActionsDTO<BusinessReportCommerciant> getBusinessReportCommerciantDebugActionsDTO(
            final CommandInput input, final TreeMap<String,
            CommerciantDTO> totalReceivedPerCommerciant) {
        ArrayList<CommerciantDTO> commerciantDTOS =
                new ArrayList<>(totalReceivedPerCommerciant.values());

        BusinessReportCommerciant businessReportCommerciant =
                new BusinessReportCommerciant(this.getIban(), getBalance(), this.getCurrency(),
                        this.payLimit, this.depositLimit, commerciantDTOS,
                        Constants.COMMERCIANT);

        return new DebugActionsDTO<>(
                input.getCommand(), businessReportCommerciant, input.getTimestamp());
    }

    /**
     * Adugam bani in contul de business
     * Verific daca e employee si daca se trece de limita impusa
     * @param input comanda de input
     */
    @Override
    public void deposit(final CommandInput input) {
        if (checkPropriety(input.getEmail())) {
            return;
            }
        if (employees.contains(input.getEmail()) && depositLimit < input.getAmount()) {
            return;
        }
        transactionHistoryWorkers.computeIfAbsent(input.getEmail(), k -> new ArrayList<>());
        transactionHistoryWorkers.get(input.getEmail()).add(new UserBusinessInfo(
                input.getTimestamp(), 0, input.getAmount()));
        setBalance(getBalance() + input.getAmount());
    }

    /**
     * Doar owner-ul pune balnta minima
     * @param input comada de input
     */
    @Override
    public void setMinBalance(final CommandInput input) {
        if (!getUser().getEmail().equals(input.getEmail())) {
            return;
        }
        setMinimumBalance(input.getAmount());

    }

    /**
     * Stergem cardul
     * Tratez situatia in care un employee poate incearca sa stearga un card ce nu ii apartine
     * @param input comanda de input
     */
    @Override
    public void deleteCard(final CommandInput input) {
        Card card = getCards().get(input.getCardNumber());
        if (!this.getUser().getEmail().equals(input.getEmail())) {
            if (employees.contains(input.getEmail())) {
                if (!this.getCards().get(input.getCardNumber()).getCardOwnerEmail()
                        .equals(input.getEmail())) {
                    return;
                }
            }
        }
        super.deleteCard(input);
    }

    /**
     * Stergem contul(doar ownerul)
     * @param input comanda de input
     */
    @Override
    public void deleteAccount(final CommandInput input) {
        if (!input.getEmail().equals(this.getUser().getEmail())) {
            return;
        }
        super.deleteAccount(input);
    }

    /**
     * Metoda de pay suprascrisa pentru contul de business deoarce trebuie sa retin anumite date
     * @param amount suma platii
     */
    @Override
    public void pay(final double amount) {
        super.pay(amount);

        CommandInput input = Bank.getInstance().getCurrentInput();
        transactionHistoryWorkers.computeIfAbsent(input.getEmail(), k -> new ArrayList<>());
        transactionHistoryWorkers.get(input.getEmail()).add(new
                UserBusinessInfo(input.getTimestamp(), amount, 0));

        if (input.getCommand().equals(Constants.SEND_MONEY)) {
            if (Bank.getInstance().getCommerciants().containsKey(input.getReceiver())) {
                transactionHistoryCommerciants.add(new CommerciantBusinessInfo(
                        input.getTimestamp(), input.getEmail(), amount, Bank.getInstance()
                        .getCommerciants().get(input.getReceiver()).getCommerciant()));
            }
        }
        if (input.getCommand().equals(Constants.PAY_ONLINE)) {
            transactionHistoryCommerciants.add(new CommerciantBusinessInfo(input.getTimestamp(),
                    input.getEmail(), amount,  Bank.getInstance().getCommerciants().get(
                            input.getCommerciant()).getCommerciant()));
        }
    }

    /**
     * Se verifica daca emailul dat ca parametru este vreo persoana ce face parte din acest
     * cont de business
     * @param email emailul ce ar trebui sa fie prezent in acest cont
     * @return adevarat daca nu exista detinatorul aici, altfel fals
     */
    @Override
    public boolean checkPropriety(final String email) {
        if (!super.checkPropriety(email)) {
            return false;
        }
        if (employees.contains(email)) {
            return false;
        }
        if (managers.contains(email)) {
            return false;
        }
        return true;
    }
}
