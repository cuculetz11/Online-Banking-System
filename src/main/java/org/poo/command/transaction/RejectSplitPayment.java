package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.services.splitPayment.WaitingSplitPayment;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;

public class RejectSplitPayment implements Command {
    /**
     * Se respinge contributia la plata distribuita si se sterg si
     * toate informatiile desprea aceea plata
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        WaitingSplitPayment waitingSplitPayment =
                BANKING_SERVICES.acceptOrRejectSplitPayment(input);
        if (waitingSplitPayment == null) {
            return;
        }
        for (int i = 0; i < waitingSplitPayment.getCommandInput().getAccounts().size(); i++) {
            Account account = Bank.getInstance().getAccounts()
                    .get(waitingSplitPayment.getCommandInput().getAccounts().get(i));
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder("Split payment of "
                            + String.format("%.2f", waitingSplitPayment.getCommandInput()
                            .getAmount()) + " " + waitingSplitPayment.getCommandInput()
                            .getCurrency(), waitingSplitPayment.getCommandInput().getTimestamp())
                            .transactionName(Constants.SPLIT_PAYMENT_FAILED_TRANSACTION)
                            .currency(waitingSplitPayment.getCommandInput().getCurrency())
                            .accounts(waitingSplitPayment.getCommandInput().getAccounts())
                            .amountForUsers(waitingSplitPayment.getCommandInput()
                                    .getAmountForUsers())
                            .splitPaymentType(waitingSplitPayment.getCommandInput()
                                    .getSplitPaymentType())
                            .userEmail(account.getUser().getEmail())
                            .iban(account.getIban())
                            .errorMessage("One user rejected the payment.")
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
        }
        Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType())
                .remove(waitingSplitPayment);
    }
}
