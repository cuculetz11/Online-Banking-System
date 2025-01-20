package org.poo.services;

import org.poo.entities.Bank;

public interface BankMethods {
    /**
     * Sunt clase ce vor implementa metode pentru banca pentru a nu polua entitatea banca
     * @param bank banca ca parametru
     */
    void visit(Bank bank);
}
