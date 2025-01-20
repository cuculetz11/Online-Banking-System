package org.poo.services.cashbackService;

import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;

public interface CashbackStrategy {
    /**
     * Verifica daca putem aduga cashback ul specific acelei tranzactii
     * @param commerciant comerciantul pentru care se face tranzactia
     * @param account contul clientului
     * @return adevarat daca nu se poate face, fals altfel
     */
    boolean check(Commerciant commerciant, Account account);

    /**
     * Obtinem cashback-ul
     * @return cashback-ul
     */
    double getCashback();

    /**
     * Se actualizeaza evidenta pentru a obtine cashbeck-ul
     * @param account contul ce a efectuat tranzactia
     */
    void updateCashback(Account account);
}
