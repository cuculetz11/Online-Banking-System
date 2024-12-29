package org.poo.services.initialize;

import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;
import org.poo.services.BankMethods;
import org.poo.services.BankingServices;

import java.util.ArrayList;

public class Initializator {
    private final UserInput[] userInputs;
    private final ExchangeInput[] exchangeInputs;

    public Initializator(final UserInput[] userInputs, final ExchangeInput[] exchangeInputs) {
        this.userInputs = userInputs;
        this.exchangeInputs = exchangeInputs;
    }

    /**
     * functia ce initalizeaza toate datele din banca date la input
     */
    public void initialize() {
        BankingServices bankingServices = new BankingServices();
        ArrayList<BankMethods> bankMethods = new ArrayList<>();
        bankMethods.add(new UserInitialize(userInputs));
        bankMethods.add(new ExchangeRatesInitialize(exchangeInputs));
        for (BankMethods method : bankMethods) {
            bankingServices.acceptVisitor(method);
        }
    }
}
