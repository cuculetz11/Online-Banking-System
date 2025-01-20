package org.poo.entities.users;

import lombok.Getter;
import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;
import org.poo.fileio.CommandInput;
import org.poo.services.CurrencyExchangeService;
import org.poo.services.cashbackService.CashBackContext;
import org.poo.services.commmissionService.CommissionFactory;
import org.poo.services.commmissionService.CommissionPlan;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;


public class UserPlan {
    @Getter
    private String planType;
    private int nrOfTransactions;
    @Getter
    private CommissionPlan commissionPlan;
    private CashBackContext cashBackContext;


    public UserPlan(final String planType) {
        this.planType = planType;
        this.commissionPlan = CommissionFactory.createCommissionPlan(planType);
        this.nrOfTransactions = 0;
    }

    /**
     * Actualizam planul
     * @param planTypeUser noul tip de plan
     */
    public void update(final String planTypeUser) {
        this.planType = planTypeUser;
        this.commissionPlan = CommissionFactory.createCommissionPlan(planType);
        this.nrOfTransactions = 0;
    }

    /**
     * In cazul in care se face auto-upgrade
     * @param amount supa unei tranzactii
     * @param currency valuta
     * @param account contul respectiv
     */
    public void checkUpdate(final double amount, final String currency, final Account account) {
        if (planType.equals(Constants.SILVER)) {
            CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
            double amountRON = currencyExchangeService.exchangeCurrency(
                    new CurrencyPair(currency, "RON"), amount);
            if (amountRON >= Constants.AUTO_UPGRADE_LIMIT) {
                nrOfTransactions++;
            }
             if (nrOfTransactions >= Constants.AUTO_UPGRADE_NR_TRANSACTION) {
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

    /**
     * Cream contextul de cashback ca apoi sa l aplicam
     * @param commerciant comerciantul caruia i s-a facut trnzactia
     * @param account contul actual
     * @param amount suma tranzactiei
     */
    public void createCashBackContext(final Commerciant commerciant, final Account account,
                                   final double amount) {
        cashBackContext = new CashBackContext(commerciant, account, amount);
    }

    /**
     * Efectuam toata logica pentru verificare, adugare cashback
     */
    public void performCashback() {
        cashBackContext.performCashBack();
    }
}
