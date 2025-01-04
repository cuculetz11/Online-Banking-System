package org.poo.entities.users;

import lombok.Setter;
import org.poo.entities.CurrencyPair;
import org.poo.services.CurrencyExchangeService;
import org.poo.services.cashbackService.CashBackContext;
import org.poo.services.commmissionService.CommissionPlan;
import org.poo.services.commmissionService.CommissionFactory;


public class UserPlan {
    private String planType;
    private int nrOfTransactions;
    private CommissionPlan commissionPlan;
    @Setter
    private CashBackContext cashBackContext;


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
    public void checkUpdate(double amount, String currency) {
        if(planType.equals("silver")) {
             if (nrOfTransactions >= 5) {
                 update("gold");
             } else {
                 CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
                 double amountRON = currencyExchangeService.exchangeCurrency(new CurrencyPair(currency, "RON"), amount);
                 if(amountRON >= 300) {
                     nrOfTransactions++;
                 }
             }
        }
    }

    public CommissionPlan getCommissionPlan() {
        return commissionPlan;
    }
    public String getPlanType() {
        return planType;
    }

}
