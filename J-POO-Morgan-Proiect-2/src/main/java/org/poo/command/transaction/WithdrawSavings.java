package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.fileio.CommandInput;
import org.poo.services.BankMethods;
import org.poo.services.payment.PaymentMethod;
import org.poo.services.payment.PaymentStrategy;
import org.poo.services.withdraw.WithdrawSavingsAction;

public class WithdrawSavings implements Command {
    /**
     * Scoatem bani de pa contul de savings pe unul clasic
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        PaymentStrategy withdrawSavings = new WithdrawSavingsAction();
        if (withdrawSavings.checkForErrors(input)) {
            return;
        }
        BankMethods withdraw = new PaymentMethod(withdrawSavings);
        BANKING_SERVICES.acceptVisitor(withdraw);
    }
}
