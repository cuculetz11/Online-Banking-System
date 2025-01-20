package org.poo.services.commmissionService;

public interface CommissionPlan {
    /**
     * Se calculeaza comisionul pentru o anumita suma
     * @param amount suma pentru care trebuie calculat comisionul
     * @param currency valuta
     * @return comisionul specific
     */
    double commission(double amount, String currency);
}
