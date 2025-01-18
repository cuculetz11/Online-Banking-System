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
    private String commerciantName;

    public NrofTransactionCashback(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    @Override
    public boolean check(Commerciant commerciant, Account account) {
        commerciantName = commerciant.getCommerciant();
        accountCurrency = account.getCurrency();
        if(!commerciantType.containsKey(commerciant.getType())){
            return true;
        }
        if(account.getUsedCashback().contains(commerciant.getType())) {
            return true;
        }

       int maxNrOfTransactions = 0;
        for(int i : account.getCashbackNrOfTransactions().values())
            maxNrOfTransactions = Math.max(maxNrOfTransactions, i);
        if(maxNrOfTransactions < commerciantType.get(commerciant.getType())) {
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
        if(!account.getCashbackNrOfTransactions().containsKey(commerciantName)) {
            account.getCashbackNrOfTransactions().put(commerciantName, 0);
        }
        account.getCashbackNrOfTransactions().put(commerciantName, account.getCashbackNrOfTransactions().get(commerciantName) + 1);
    }
}
