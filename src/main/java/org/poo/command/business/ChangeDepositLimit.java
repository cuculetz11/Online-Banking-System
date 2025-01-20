package org.poo.command.business;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.BusinessAccount;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;

public class ChangeDepositLimit implements Command {
    /**
     * Se schimba limita de deposit(doar owner-ul face asta)
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        if (!BANKING_SERVICES.checkSetBusinessLimits(Constants.YOU_MUST_BE_OWNER_DEPOSIT)) {
            return;
        }
        BusinessAccount businessAccount = (BusinessAccount) Bank.getInstance().getAccounts()
                .get(input.getAccount());
        businessAccount.setDepositLimit(input.getAmount());
    }
}
