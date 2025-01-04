package org.poo.services.commmissionService;

import org.poo.utils.Constants;

public class StandardCommission implements CommissionPlan {

    @Override
    public double commission(double amount) {
        double commission = (Constants.STANDARD_COMMISSION * amount) / 100;
        return Math.round(commission * 1000) / 1000.0;
    }
}
