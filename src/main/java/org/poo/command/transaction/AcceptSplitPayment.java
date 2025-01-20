package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;
import org.poo.services.splitPayment.WaitingSplitPayment;

public class AcceptSplitPayment implements Command {
    /**
     * Se accepta contribuirea la plata distribuita
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        WaitingSplitPayment waitingSplitPayment =
                BANKING_SERVICES.acceptOrRejectSplitPayment(input);
        if (waitingSplitPayment == null) {
            return;
        }
        waitingSplitPayment.getRemainedPayments().remove(input.getEmail());
        if (waitingSplitPayment.check()) {
            waitingSplitPayment.processPayment();
            Bank.getInstance().getWaitingSplitPayments().get(input.getSplitPaymentType())
                    .remove(waitingSplitPayment);
        }
    }
}
