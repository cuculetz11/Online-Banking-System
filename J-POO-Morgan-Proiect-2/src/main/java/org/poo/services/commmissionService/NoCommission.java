package org.poo.services.commmissionService;

public class NoCommission implements CommissionPlan {

    @Override
    public double commission(double amount, String currency) {
        return 0;
    }
}
