package org.poo.services.payment;

import org.poo.fileio.CommandInput;
import org.poo.services.CurrencyExchangeService;

public interface PaymentStrategy {
    CurrencyExchangeService CURRENCY_EXCHANGE_SERVICE = new CurrencyExchangeService();

    /**
     * Plata efectiva printr o stategie aleasa
     */
    void pay();

    /**
     * Verifica daca se poate efectua plata
     * @param input data necesare pentru verificare
     * @return adevarat daca sunt erori, fals altfel
     */
    boolean checkForErrors(CommandInput input);
}
