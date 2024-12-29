package org.poo.command.debug;

import org.poo.command.Command;
import org.poo.command.debug.dto.DebugDTO;
import org.poo.entities.Bank;
import org.poo.entities.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutManager;

import java.util.ArrayList;

public class PrintUsers implements Command {
    /**
     * Printeaza toti userii cu toate conturile si cardurile lor
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        ArrayList<User> users = new ArrayList<>(Bank.getInstance().getUsers().values());
        DebugDTO<User> printUsers = new DebugDTO<User>(input.getCommand(),
                users, input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(printUsers);
    }
}
