package org.poo.services.commmissionService;

import org.poo.entities.CurrencyPair;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Constants;

public class SilverCommission implements CommissionPlan {
    /**
     * {@inheritDoc}
     */
    @Override
    public double commission(final double amount, final String currency) {
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        double amountRON = currencyExchangeService.exchangeCurrency(
                new CurrencyPair(currency, "RON"), amount);
        if (amountRON < Constants.SILVER_COMMISSION_LIMIT) {
            return 0;
        }
        return (amount * Constants.SILVER_COMMISSION / Constants.PROCENT);
    }
}
