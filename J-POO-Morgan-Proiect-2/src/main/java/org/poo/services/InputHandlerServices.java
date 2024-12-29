package org.poo.services;

import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.services.initialize.Initializator;

public class InputHandlerServices {
    private final ObjectInput input;

    public InputHandlerServices(final ObjectInput input) {
        this.input = input;
    }

    /**
     * Ia toate datele de intrare si incepe executia comenzilor
     */
    public void handle() {
        Initializator initializator = new Initializator(input.getUsers(), input.getExchangeRates());
        initializator.initialize();
        BankingServices bankingServices = new BankingServices();
        for (CommandInput commandInput : input.getCommands()) {
            bankingServices.performCommand(commandInput);
        }

    }
}
