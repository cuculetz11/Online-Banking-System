package org.poo.services.cashbackService;

import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Constants;

/**
 *  Facand aceasta clasa nu mai poluez codul la PayOnline si sendMoney deorece
 *  acum o sa adug doar o simpla linie ce face si factoty pt stategie si apoi o si aplica
 */
public class CashBackContext {
    private Commerciant commerciant;
    private Account account;
    private double amount;
    public CashBackContext(final Commerciant commerciant, final Account account,
                           final double amount) {
        this.commerciant = commerciant;
        this.account = account;
        this.amount = amount;
    }

    /**
     * Aici e logica cashback-ului, aici se creaza strategia si se aplica in functie de care e
     */
    public void performCashBack() {
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        amount = currencyExchangeService.exchangeCurrency(
                new CurrencyPair(account.getCurrency(), "RON"), amount);
        CashbackStrategy nrOfTransaction = new NrofTransactionCashback(amount);
        if (!nrOfTransaction.check(commerciant, account)) {
            double cashback = nrOfTransaction.getCashback();
            account.setBalance(account.getBalance() + cashback);
        }
        if (commerciant.getCashbackStrategy().equals(Constants.NR_OF_TRANSACTIONS)) {
            nrOfTransaction.updateCashback(account);
        }
        if (commerciant.getCashbackStrategy().equals(Constants.SPENDING_THRESHOLD)) {
            CashbackStrategy cashbackStrategy = new SpendingThresholdCashback(
                    account.getUser().getPlan().getPlanType(), amount);
            cashbackStrategy.updateCashback(account);
            if (!cashbackStrategy.check(commerciant, account)) {
                double cashback = cashbackStrategy.getCashback();
                account.setBalance(account.getBalance() + cashback);
            }
        }
    }
}
