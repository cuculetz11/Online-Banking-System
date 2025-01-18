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
    private LinkedHashSet<String> managers;
    @JsonIgnore
    private LinkedHashSet<String> employees;
    @JsonIgnore
    private TreeMap<String, List<UserBusinessInfo>> transactionHistoryWorkers;
    @JsonIgnore
    private ArrayList<CommerciantBusinessInfo> transactionHistoryCommerciants;
    @JsonIgnore
    @Setter
    private double payLimit;
    @JsonIgnore
    @Setter
    private double depositLimit;

    public BusinessAccount(CommandInput input) {
        super(0, input.getCurrency(), input.getAccountType());
        managers = new LinkedHashSet<>();
        employees = new LinkedHashSet<>();
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        payLimit = currencyExchangeService.exchangeCurrency(new CurrencyPair("RON", input.getCurrency()),500);
        depositLimit = payLimit;
        transactionHistoryWorkers = new TreeMap<>();
        transactionHistoryCommerciants = new ArrayList<>();
    }

    @Override
    public String isTransferPossible(double amount) {

        if(employees.contains(Bank.getInstance().getCurrentInput().getEmail()) && amount + this.getUser().getPlan().getCommissionPlan().commission(amount, getCurrency()) >= payLimit) {
            return Constants.LIMIT_EXCEEDED;
        }
        if(this.getBalance() < (amount + this.getUser().getPlan().getCommissionPlan().commission(amount, getCurrency()))) {
            return Constants.TRANSACTION_IMPOSSIBLE;
        }
        return "";
    }

    @Override
    public void addInterest(CommandInput input) {
        ErrorManager.notFound(Constants.NOT_SAVINGS_ACCOUNT, input.getCommand(),
                input.getTimestamp());
    }

    @Override
    public void changeInterestRate(CommandInput input) {
        ErrorManager.notFound(Constants.NOT_SAVINGS_ACCOUNT, input.getCommand(),
                input.getTimestamp());
    }

    @Override
    public void spendingReport(CommandInput input) {
        if(input.getType().equals(Constants.TRANSACTION)) {
            ArrayList<PaymentPerWorkerDTO> manager = new ArrayList<>();
            ArrayList<PaymentPerWorkerDTO> employee = new ArrayList<>();
            double total_spent = 0;
            double total_deposit = 0;
            for(String managerEmail : managers) {
                List<UserBusinessInfo> managerHistoryTransaction = transactionHistoryWorkers.get(managerEmail);

                User managerUser = Bank.getInstance().getUsers().get(managerEmail);
                String name = managerUser.getLastName() + " " + managerUser.getFirstName();
                PaymentPerWorkerDTO paymentPerWorkerDTO = new PaymentPerWorkerDTO(name);
                if(managerHistoryTransaction != null) {
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
                manager.add(paymentPerWorkerDTO);
                total_spent += paymentPerWorkerDTO.getSpent();
                total_deposit += paymentPerWorkerDTO.getDeposited();
            }
            for(String employeeEmail : employees) {
                List<UserBusinessInfo> employeeHistoryTransaction = transactionHistoryWorkers.get(employeeEmail);

                User employeeUser = Bank.getInstance().getUsers().get(employeeEmail);
                String name = employeeUser.getLastName() + " " + employeeUser.getFirstName();
                PaymentPerWorkerDTO paymentPerWorkerDTO = new PaymentPerWorkerDTO(name);
                if(employeeHistoryTransaction != null) {
                    for (UserBusinessInfo userBusinessInfo : employeeHistoryTransaction) {
                        if (userBusinessInfo.getTimestamp() > input.getEndTimestamp()) {
                            break;
                        }
                        if (userBusinessInfo.getTimestamp() >= input.getStartTimestamp()) {
                            paymentPerWorkerDTO.addDeposited(userBusinessInfo.getDeposit());
                            paymentPerWorkerDTO.addSpent(userBusinessInfo.getSpent());
                        }
                    }
                }
                employee.add(paymentPerWorkerDTO);
                total_spent += paymentPerWorkerDTO.getSpent();
                total_deposit += paymentPerWorkerDTO.getDeposited();
            }
            BusinessReportTransaction businessReportTransaction =
                    new BusinessReportTransaction(this.getIban(),input.getType(),this.getBalance(),this.getCurrency(),this.payLimit, this.depositLimit,manager,employee,total_spent,total_deposit);
            DebugActionsDTO<BusinessReportTransaction> debugActionsDTO = new DebugActionsDTO<>(input.getCommand(),businessReportTransaction, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(debugActionsDTO);
        } else if(input.getType().equals(Constants.COMMERCIANT)) {
            TreeMap<String, CommerciantDTO> totalReceivedPerCommerciant = new TreeMap<>();
            Set<String> employee = new TreeSet<>();
            Set<String> manager = new TreeSet<>();

            for(CommerciantBusinessInfo c : transactionHistoryCommerciants) {
                if(c.getTimestamp() > input.getEndTimestamp()) {
                    break;
                }
                if(c.getTimestamp() >= input.getStartTimestamp()) {
                    if(getUser().getEmail().equals(c.getUserEmail())) {
                        continue;
                    }
                    totalReceivedPerCommerciant.putIfAbsent(c.getCommerciantName(), new CommerciantDTO(c.getCommerciantName(),0,new ArrayList<>(),new ArrayList<>()));
                    totalReceivedPerCommerciant.get(c.getCommerciantName()).addMoney(c.getAmount());

                    if(employees.contains(c.getUserEmail())) {
                        User user = Bank.getInstance().getUsers().get(c.getUserEmail());
                        String name = user.getLastName() + " " + user.getFirstName();
                        totalReceivedPerCommerciant.get(c.getCommerciantName()).addEmployer(name);
                    }
                    if(managers.contains(c.getUserEmail())) {
                        User user = Bank.getInstance().getUsers().get(c.getUserEmail());
                        String name = user.getLastName() + " " + user.getFirstName();
                        totalReceivedPerCommerciant.get(c.getCommerciantName()).addManger(name);
                    }
                }
            }
            ArrayList<CommerciantDTO> commerciantDTOS = new ArrayList<>();
            for(CommerciantDTO info : totalReceivedPerCommerciant.values()) {
               commerciantDTOS.add(info);
            }
            BusinessReportCommerciant businessReportCommerciant = new BusinessReportCommerciant(this.getIban(), getBalance(), this.getCurrency(), this.payLimit, this.depositLimit, commerciantDTOS, "commerciant");
            DebugActionsDTO<BusinessReportCommerciant> debugActionsDTO = new DebugActionsDTO<>(input.getCommand(),businessReportCommerciant, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(debugActionsDTO);
        }
    }

    @Override
    public void deposit(CommandInput input) {
        if(!checkPropriety(input.getEmail()))
            return;
        if(employees.contains(input.getEmail()) && depositLimit < input.getAmount()) {
            return;
        }
        transactionHistoryWorkers.computeIfAbsent(input.getEmail(), k -> new ArrayList<>());
        transactionHistoryWorkers.get(input.getEmail()).add(new UserBusinessInfo(input.getTimestamp(), 0, input.getAmount()));
        setBalance(getBalance() + input.getAmount());
    }

    @Override
    public void setMinBalance(CommandInput input) {
        if(!getUser().getEmail().equals(input.getEmail())) {
            System.out.println("nu e popritarul");
            return;
        }
        setMinimumBalance(input.getAmount());

    }

    @Override
    public void deleteCard(CommandInput input) {
        Card card = getCards().get(input.getCardNumber());
        if (!this.getUser().getEmail().equals(input.getEmail())) {
            if(employees.contains(input.getEmail())) {
                if(!this.getCards().get(input.getCardNumber()).getCardOwnerEmail().equals(input.getEmail())) {
                    System.out.println("nu e popritarul cardului business");
                    return;
                }
            }
        }
        String iban = Bank.getInstance().getCards().get(input.getCardNumber()).getAccount()
                .getIban();
        BankingServices bankingServices = new BankingServices();
        if(this.getBalance() != 0)
            return;
        bankingServices.deleteCard(this, input.getCardNumber());


        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.THE_CARD_HAS_BEEN_DESTROYED,
                        input.getTimestamp())
                        .transactionName(Constants.DELETE_CARD_TRANSACTION)
                        .cardNumber(input.getCardNumber())
                        .userEmail(input.getEmail())
                        .iban(iban)
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
    }

    @Override
    public void deleteAccount(CommandInput input) {
        if(!input.getEmail().equals(this.getUser().getEmail())) {
            System.out.println("nu e popritarul");
            return;
        }
        AccountDeleteInfo data = null;
        if(this.getBalance() > 0) {
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
    @Override
    public void pay(double amount) {
        super.pay(amount);
        CommandInput input = Bank.getInstance().getCurrentInput();
        transactionHistoryWorkers.computeIfAbsent(input.getEmail(), k -> new ArrayList<>());
        transactionHistoryWorkers.get(input.getEmail()).add(new UserBusinessInfo(input.getTimestamp(), amount, 0));
        if(input.getCommand().equals(Constants.SEND_MONEY)) {
            if(Bank.getInstance().getCommerciants().containsKey(input.getReceiver())) {
                transactionHistoryCommerciants.add(new CommerciantBusinessInfo(input.getTimestamp(), input.getEmail(), amount, Bank.getInstance().getCommerciants().get(input.getReceiver()).getCommerciant()));
            }
        }
        if(input.getCommand().equals(Constants.PAY_ONLINE)) {
            transactionHistoryCommerciants.add(new CommerciantBusinessInfo(input.getTimestamp(), input.getEmail(), amount,  Bank.getInstance().getCommerciants().get(input.getCommerciant()).getCommerciant()));
        }
    }
    @Override
    public boolean checkPropriety(String email) {
        if(super.checkPropriety(email)) {
            return true;
        }
        if(employees.contains(email)) {
            return true;
        }
        if(managers.contains(email)) {
            return true;
        }
        return false;
    }
}
