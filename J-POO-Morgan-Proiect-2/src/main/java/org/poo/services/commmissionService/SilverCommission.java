package org.poo.services.commmissionService;

import org.poo.entities.CurrencyPair;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Constants;

public class SilverCommission implements CommissionPlan {

    @Override
    public double commission(double amount, String currency) {
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        double amountRON = currencyExchangeService.exchangeCurrency(new CurrencyPair(currency, "RON"), amount);
        if(amountRON < 500) {
            return 0;
        }
        return (amount * Constants.SILVER_COMMISSION / 100);
    }
}
