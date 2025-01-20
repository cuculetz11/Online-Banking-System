package org.poo.services.commmissionService;

public final class CommissionFactory {
    private CommissionFactory() { }

    /**\
     * In functie de ce tip de plan are se creeaza un obiect de tip "CommisionPlan"
     * Acest obiect aplica comisionul specific pe tranzactie
     * @param userPlan planul userului
     * @return un obiect ce aplica foarte usor comisionul
     */
    public static CommissionPlan createCommissionPlan(final String userPlan) {
        return switch (userPlan) {
            case "student", "gold" -> new NoCommission();
            case "silver" -> new SilverCommission();
            case "standard" -> new StandardCommission();
            default -> throw new IllegalArgumentException("Invalid user plan: " + userPlan);
        };
    }
}
