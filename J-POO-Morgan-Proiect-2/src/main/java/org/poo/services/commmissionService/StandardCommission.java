package org.poo.services.commmissionService;

import org.poo.utils.Constants;

public class StandardCommission implements CommissionPlan {

    @Override
    public double commission(double amount, String currency) {
        return (Constants.STANDARD_COMMISSION * amount / 100);
    }
}
