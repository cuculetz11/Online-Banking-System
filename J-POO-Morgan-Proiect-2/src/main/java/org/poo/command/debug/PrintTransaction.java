package org.poo.command.debug;

import org.poo.command.Command;
import org.poo.command.debug.dto.DebugDTO;
import org.poo.entities.Bank;
import org.poo.entities.transaction.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutManager;

import java.util.ArrayList;

public class PrintTransaction implements Command {
    /**
     * Printeaza toate tranzactiile userului dat la input
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        ArrayList<Transaction> transactions = Bank.getInstance().getTransactionHistory()
                .get(input.getEmail());
        DebugDTO<Transaction> printTransaction = new DebugDTO<Transaction>(input.getCommand(),
                transactions, input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(printTransaction);
    }
}
