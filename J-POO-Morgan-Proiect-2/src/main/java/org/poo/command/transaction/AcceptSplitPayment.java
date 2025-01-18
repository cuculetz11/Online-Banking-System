package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;
import org.poo.services.splitPayment.WaitingSplitPayment;
import org.poo.utils.Constants;
import org.poo.utils.ErrorManager;

public class AcceptSplitPayment implements Command {

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
            //ErrorManager.notFound(Constants.USER_NOT_FOUND,input.getCommand(), input.getTimestamp());
            return;
        }
        waitingSplitPayment.getRemainedPayments().remove(input.getEmail());
        if(waitingSplitPayment.check()) {
            waitingSplitPayment.processPayment();
            Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType()).remove(waitingSplitPayment);
        }
    }
}
