package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;
import org.poo.services.splitPayment.WaitingSplitPayment;

public class AcceptSplitPayment implements Command {

    @Override
    public void execute(CommandInput input) {
        WaitingSplitPayment waitingSplitPayment = null;
        for(WaitingSplitPayment w : Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType())) {
            if(w.getRemainedPayments().contains(input.getEmail())) {
                waitingSplitPayment = w;
                break;
            }
        }
        if(waitingSplitPayment == null) {
            System.out.println("problema la cautarea in lista de split");
            return;
        }
        waitingSplitPayment.getRemainedPayments().remove(input.getEmail());
        if(waitingSplitPayment.check()) {
            waitingSplitPayment.processPayment();
            Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType()).remove(waitingSplitPayment);
        }
    }
}
