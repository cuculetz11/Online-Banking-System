package org.poo.services.cashbackService;

import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Constants;

/**
 *  Facand aceasta clasa nu mai poluez codul la PayOnline si sendMoney deorece acum o sa adug doar o simpla linie ce face si factoty pt stategie si apoi o si aplica
 */
public class CashBackContext {
    private CashbackStrategy cashbackStrategy;
    public CashBackContext(Commerciant commerciant, Account account, double amount) {
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        amount = currencyExchangeService.exchangeCurrency(new CurrencyPair(account.getCurrency(),"RON"), amount);
        if(commerciant.getCashbackStrategy().equals(Constants.NR_OF_TRANSACTIONS)) {
            cashbackStrategy = new NrofTransactionCashback(amount);
            if(!cashbackStrategy.check(commerciant, account)) {
                double cashback = cashbackStrategy.getCashback();
                account.setBalance(account.getBalance() + cashback);
            }
            cashbackStrategy.updateCashback(account);
        }
        if(commerciant.getCashbackStrategy().equals(Constants.SPENDING_THRESHOLD)) {
            cashbackStrategy = new SpendingThresholdCashback(account.getUser().getPlan().getPlanType(), amount);
            cashbackStrategy.updateCashback(account);
            if(!cashbackStrategy.check(commerciant, account)) {
                double cashback = cashbackStrategy.getCashback();
                account.setBalance(account.getBalance() + cashback);
            }
        }
    }
}
