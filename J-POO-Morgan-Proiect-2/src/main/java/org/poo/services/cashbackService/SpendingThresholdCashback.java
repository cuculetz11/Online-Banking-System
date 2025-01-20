package org.poo.services.cashbackService;

import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Constants;

import java.util.Map;

public class SpendingThresholdCashback implements CashbackStrategy {
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

    public SpendingThresholdCashback(final String userPlanType, final double transactionAmount) {
        this.userPlanType = userPlanType;
        this.transactionAmount = transactionAmount;
    }

    /**
     * {@inheritDoc}
     * @param commerciant comerciantul pentru care se face tranzactia
     * @param account contul clientului
     * @return fals daca se poate, adevarat altfel
     */
    @Override
    public boolean check(final Commerciant commerciant, final Account account) {
        accountCurrency = account.getCurrency();
        double amount = account.getCashbackSpending();
        if (amount < Constants.FIRST_THRESHOLD) {
            return true;
        }
        if (amount >= Constants.FIRST_THRESHOLD && amount < Constants.SECOND_THRESHOLD) {
            threshold = Constants.FIRST_THRESHOLD;
        } else if (amount >= Constants.SECOND_THRESHOLD && amount < Constants.THIRD_THRESHOLD) {
            threshold = Constants.SECOND_THRESHOLD;
        } else if (amount >= Constants.THIRD_THRESHOLD) {
            threshold = Constants.THIRD_THRESHOLD;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * @return cashback-ul
     */
    @Override
    public double getCashback() {
        double cashback = cashbackRates.get(threshold).get(userPlanType)
                * transactionAmount / Constants.PROCENT;
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        cashback = currencyExchangeService.exchangeCurrency(
                new CurrencyPair("RON", accountCurrency), cashback);
        return cashback;
    }

    /**
     * {@inheritDoc}
     * @param account contul ce a efectuat tranzactia
     */
    @Override
    public void updateCashback(final Account account) {
        account.setCashbackSpending(account.getCashbackSpending() + transactionAmount);
    }
}
