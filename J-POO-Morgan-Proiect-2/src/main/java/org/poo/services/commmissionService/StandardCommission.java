package org.poo.services.commmissionService;

import org.poo.utils.Constants;

public class StandardCommission implements CommissionPlan {
    /**
     * {@inheritDoc}
     */
    @Override
    public double commission(final double amount, final String currency) {
        return (Constants.STANDARD_COMMISSION * amount / Constants.PROCENT);
    }
}
