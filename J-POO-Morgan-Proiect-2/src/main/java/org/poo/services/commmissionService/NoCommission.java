package org.poo.services.commmissionService;

/**
 * Comision specific pentru panul de studenti si cel gold
 */
public class NoCommission implements CommissionPlan {
    /**
     * {@inheritDoc}
     */
    @Override
    public double commission(final double amount, final String currency) {
        return 0;
    }
}
