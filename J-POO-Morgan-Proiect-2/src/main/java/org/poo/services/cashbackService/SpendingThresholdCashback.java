package org.poo.services.cashbackService;

import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;
import org.poo.services.CurrencyExchangeService;

import java.util.Map;

public class SpendingThresholdCashback implements CashbackStrategy{
    private final  Map<Integer, Map<String, Double>> cashbackRates = Map.of(
            100, Map.of(
                    "standard", 0.1,
                    "student", 0.1,
                    "silver", 0.3,
                    "gold", 0.5
            ),
            300, Map.of(
                    "standard", 0.2,
                    "student", 0.2,
                    "silver", 0.4,
                    "gold", 0.55
            ),
            500, Map.of(
                    "standard", 0.25,
                    "student", 0.25,
                    "silver", 0.5,
                    "gold", 0.7
            )

    );
    private final  String userPlanType;
    private final String cashbackType = "spendingThreshold";
    private int threshold;
    private final double transactionAmount;
    private String accountCurrency;

    public SpendingThresholdCashback(String userPlanType, double transactionAmount) {
        this.userPlanType = userPlanType;
        this.transactionAmount = transactionAmount;
    }
    @Override
    public boolean check(Commerciant commerciant, Account account) {
        accountCurrency = account.getCurrency();
        double amount = account.getCashback().get(cashbackType);
        if(amount < 100) {
            return true;
        }
        if(amount >= 100 && amount < 300) {
            threshold = 100;
        } else if(amount >= 300 && amount < 500) {
            threshold = 300;
        } else if(amount >= 500) {
            threshold = 500;
        }
//        if(account.getUsedCashback().contains(String.valueOf(threshold))) {
//            return true;
//        }
        account.getUsedCashback().add(String.valueOf(threshold));
        return false;
    }

    @Override
    public double getCashback() {
        double cashback = cashbackRates.get(threshold).get(userPlanType) * transactionAmount / 100;
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        cashback = currencyExchangeService.exchangeCurrency(new CurrencyPair("RON", accountCurrency), cashback);
        return cashback;
    }

    @Override
    public void updateCashback(Account account) {
        if(!account.getCashback().containsKey(cashbackType)) {
            account.getCashback().put(cashbackType,transactionAmount);
            return;
        }
        account.getCashback().put(cashbackType,account.getCashback().get(cashbackType) + transactionAmount);
    }
}
