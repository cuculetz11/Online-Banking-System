package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.command.debug.dto.AccountDeleteInfo;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.command.debug.dto.DeleteAccountDTO;
import org.poo.command.debug.dto.ErrorPrint;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.JsonOutManager;
import org.poo.utils.TransactionManager;

public class DeleteAccount implements Command {
    /**
     * Sterge contul bancar daca e gol
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    public void execute(final CommandInput input) {
        AccountDeleteInfo data = null;

        if (BANKING_SERVICES.removeAccount(input.getEmail(), input.getAccount())) {
            data = new DeleteAccountDTO(Constants.ACCOUNT_DELETED, input.getTimestamp());
        } else {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.ACCOUNT_CANT_BE_DELETED_FUNDS,
                            input.getTimestamp())
                            .transactionName(Constants.DELETE_ACCOUNT_FAIL_TRANSACTION)
                            .userEmail(input.getEmail())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            data = new ErrorPrint(Constants.ACCOUNT_CANT_BE_DELETED, input.getTimestamp());
        }
        DebugActionsDTO<AccountDeleteInfo> wasAccountDeleted =
                new DebugActionsDTO<>(input.getCommand(), data, input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(wasAccountDeleted);
    }
}
