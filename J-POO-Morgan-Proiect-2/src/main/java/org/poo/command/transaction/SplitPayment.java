package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.fileio.CommandInput;
import org.poo.services.BankMethods;
import org.poo.services.payment.PaymentMethod;
import org.poo.services.payment.PaymentStrategy;
import org.poo.services.payment.SplitPaymentStrategy;

public class SplitPayment implements Command {
    /**
     * Se imparte plata la conturile date
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        PaymentStrategy split = new SplitPaymentStrategy();
        if (split.checkForErrors(input)) {
            return;
        }
        BankMethods pay = new PaymentMethod(split);
        BANKING_SERVICES.acceptVisitor(pay);

    }
}
