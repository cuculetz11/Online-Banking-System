package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;
import org.poo.services.splitPayment.WaitingSplitPayment;

import java.util.ArrayList;
import java.util.HashSet;

public class SplitPayment implements Command {
    /**
     * Cand se da comanda doar verific daca exista vreun cont invalid, apoi atibui in fiecare
     * user pentru fiecare cont clasa de waitingSplitPayment ce asteapta acceptul sau
     * rejectul fiecaruia
     *
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        HashSet<String> remainedAccounts = new HashSet<>();
        for (String accountNumber : input.getAccounts()) {
            if (Bank.getInstance().getAccounts().get(accountNumber) == null) {
                return;
            }
            remainedAccounts.add(Bank.getInstance().getAccounts()
                    .get(accountNumber).getUser().getEmail());
        }
        WaitingSplitPayment waitingSplitPayment =
                new WaitingSplitPayment(input, remainedAccounts);
        Bank.getInstance().getWaitingSplitPayments()
                .putIfAbsent(input.getSplitPaymentType(), new ArrayList<>());
        Bank.getInstance().getWaitingSplitPayments()
                .get(input.getSplitPaymentType()).add(waitingSplitPayment);
    }
}
