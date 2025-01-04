package org.poo.services.commmissionService;

import org.poo.utils.Constants;

public class SilverCommission implements CommissionPlan {

    @Override
    public double commission(double amount) {
        if(amount < 500) {
            return 0;
        }
        double commission = (amount * Constants.SILVER_COMMISSION) / 100;
        return Math.round(commission * 1000) / 1000.0;
    }
}
