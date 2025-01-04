package org.poo.entities.users;

import org.poo.services.commmissionService.CommissionPlan;
import org.poo.services.commmissionService.CommissionFactory;


public class UserPlan {
    private String planType;
    private int nrOfTransactions;
    private CommissionPlan commissionPlan;


    public UserPlan(String planType) {
        this.planType = planType;
        this.commissionPlan = CommissionFactory.createCommissionPlan(planType);
        this.nrOfTransactions = 0;
    }
    public void update(String planType) {
        this.planType = planType;
        this.commissionPlan = CommissionFactory.createCommissionPlan(planType);
        this.nrOfTransactions = 0;
    }
    public void checkUpdate() {
        if(planType.equals("silver") && nrOfTransactions >= 5) {
            update("gold");
        }
    }

    public CommissionPlan getCommissionPlan() {
        return commissionPlan;
    }
    public String getPlanType() {
        return planType;
    }

}
