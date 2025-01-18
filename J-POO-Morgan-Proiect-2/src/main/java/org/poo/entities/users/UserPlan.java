package org.poo.entities.users;

import lombok.Setter;
import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.services.CurrencyExchangeService;
import org.poo.services.cashbackService.CashBackContext;
import org.poo.services.commmissionService.CommissionPlan;
import org.poo.services.commmissionService.CommissionFactory;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;


public class UserPlan {
    private String planType;
    private int nrOfTransactions;
    private CommissionPlan commissionPlan;


    public UserPlan(String planType) {
        this.planType = planType;
        this.commissionPlan = CommissionFactory.createCommissionPlan(planType);
        this.nrOfTransactions = 0;
    }
    public void update(String planType) {
        this.planType = planType;
        this.commissionPlan = CommissionFactory.createCommissionPlan(planType);
        this.nrOfTransactions = 0;
    }
    public void checkUpdate(double amount, String currency, Account account) {
        if(planType.equals("silver")) {
            CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
            double amountRON = currencyExchangeService.exchangeCurrency(new CurrencyPair(currency, "RON"), amount);
            if(amountRON >= 300) {
                nrOfTransactions++;
            }
             if (nrOfTransactions >= 5) {
                 update("gold");
                 CommandInput input = Bank.getInstance().getCurrentInput();
                 DatesForTransaction datesForTransaction =
                         new DatesForTransaction.Builder(Constants.UPGRADE_PLAN_SENTENCE,
                                 input.getTimestamp())
                                 .transactionName(Constants.UPGRADE_PLAN_TRANSACTION)
                                 .userEmail(account.getUser().getEmail())
                                 .newPlanType("gold")
                                 .iban(account.getIban())
                                 .build();
                 TransactionManager.generateAndAddTransaction(datesForTransaction);
             }
        }
    }


    public CommissionPlan getCommissionPlan() {
        return commissionPlan;
    }
    public String getPlanType() {
        return planType;
    }
    public void setCashBackContext(CashBackContext cashBackContext) {
    }
}
