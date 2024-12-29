package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.fileio.CommandInput;

public class AddFounds implements Command {
    /**
     * Adauga bani pe cont
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        BANKING_SERVICES.addFounds(input.getAccount(), input.getAmount());
    }
}
