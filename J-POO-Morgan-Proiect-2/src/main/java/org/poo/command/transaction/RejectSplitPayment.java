package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.services.splitPayment.WaitingSplitPayment;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.ErrorManager;
import org.poo.utils.TransactionManager;

public class RejectSplitPayment implements Command {
    @Override
    public void execute(CommandInput input) {
        if(!Bank.getInstance().getUsers().containsKey(input.getEmail())) {
            ErrorManager.notFound(Constants.USER_NOT_FOUND,input.getCommand(), input.getTimestamp());
            return;
        }
        WaitingSplitPayment waitingSplitPayment = null;
        if(Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType()) == null) {
            System.out.println("nu exitsa inca tipul asta de split");
            return;
        }
        for(WaitingSplitPayment w : Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType())) {
            if(w.getRemainedPayments().contains(input.getEmail())) {
                waitingSplitPayment = w;
                break;
            }
        }
        if(waitingSplitPayment == null) {
//            System.out.println("nu e userul aici");
            //ErrorManager.notFound(Constants.USER_NOT_FOUND,input.getCommand(), input.getTimestamp());
            return;
        }
        for(int i = 0 ; i < waitingSplitPayment.getCommandInput().getAccounts().size(); i++) {
            Account account = Bank.getInstance().getAccounts().get(waitingSplitPayment.getCommandInput().getAccounts().get(i));
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder("Split payment of " + String.format("%.2f", waitingSplitPayment.getCommandInput().getAmount()) + " " + waitingSplitPayment.getCommandInput().getCurrency(), waitingSplitPayment.getCommandInput().getTimestamp())
                            .transactionName(Constants.SPLIT_PAYMENT_FAILED_TRANSACTION)
                            .currency(waitingSplitPayment.getCommandInput().getCurrency())
                            .accounts(waitingSplitPayment.getCommandInput().getAccounts())
                            .amountForUsers(waitingSplitPayment.getCommandInput().getAmountForUsers())
                            .splitPaymentType(waitingSplitPayment.getCommandInput().getSplitPaymentType())
                            .userEmail(account.getUser().getEmail())
                            .iban(account.getIban())
                            .errorMessage("One user rejected the payment.")
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
        }
        Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType()).remove(waitingSplitPayment);
    }
}
