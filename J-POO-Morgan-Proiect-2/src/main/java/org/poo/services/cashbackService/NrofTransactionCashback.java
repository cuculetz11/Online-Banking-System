package org.poo.services.cashbackService;

import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;
import org.poo.services.CurrencyExchangeService;

import java.util.Map;

public class NrofTransactionCashback implements CashbackStrategy{
    private final Map<String, Integer> commerciantType = Map.of(
            "Food", 2,
            "Clothes", 5,
            "Tech", 10
    );

    private final String cashbackType = "nrOfTransactions";
    private String typeProduce;
    private final double transactionAmount;
    private String accountCurrency;

    public NrofTransactionCashback(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    @Override
    public boolean check(Commerciant commerciant, Account account) {
        accountCurrency = account.getCurrency();
        if(account.getUsedCashback().contains(commerciant.getType())) {
            return true;
        }
        if(!account.getCashback().containsKey(commerciant.getType())) {
            return true;
        }
        if(account.getCashback().get(cashbackType) < commerciantType.get(commerciant.getType())) {
            return true;
        }
        account.getUsedCashback().add(commerciant.getType());
        typeProduce = commerciant.getType();
        return false;
    }

    @Override
    public double getCashback() {

        double cashback = commerciantType.get(typeProduce) * transactionAmount / 100;
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        cashback = currencyExchangeService.exchangeCurrency(new CurrencyPair("RON", accountCurrency), cashback);
        return cashback;
    }

    @Override
    public void updateCashback(Account account) {
        if(!account.getCashback().containsKey(cashbackType)) {
            account.getCashback().put(cashbackType, 1.0);
        }
        account.getCashback().put(cashbackType, account.getCashback().get(cashbackType) + 1);
    }
}
