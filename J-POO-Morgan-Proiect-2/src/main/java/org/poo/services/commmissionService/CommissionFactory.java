package org.poo.services.commmissionService;

public class CommissionFactory {
    private CommissionFactory() {}

    public static CommissionPlan createCommissionPlan(String userPlan) {
        return switch (userPlan) {
            case "student", "gold" -> new NoCommission();
            case "silver" -> new SilverCommission();
            case "standard" -> new StandardCommission();
            default -> throw new IllegalArgumentException("Invalid user plan: " + userPlan);
        };
    }
}
