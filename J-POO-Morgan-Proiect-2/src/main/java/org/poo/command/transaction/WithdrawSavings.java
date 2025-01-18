package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;
import org.poo.services.BankMethods;
import org.poo.services.payment.PaymentMethod;
import org.poo.services.payment.PaymentStrategy;
import org.poo.services.withdraw.WithdrawSavingsAction;

public class WithdrawSavings implements Command {
    @Override
    public void execute(CommandInput input) {
        PaymentStrategy withdrawSavings = new WithdrawSavingsAction();
        if(withdrawSavings.checkForErrors(input)) {
            //System.out.println("erroare pentru utilizatorul: " + Bank.getInstance().getAccounts().get(input.getAccount()).getUser().getEmail() + " contul: " + Bank.getInstance().getAccounts().get(input.getAccount()).getIban());
            return;
        }
        BankMethods withdraw = new PaymentMethod(withdrawSavings);
        BANKING_SERVICES.acceptVisitor(withdraw);
    }
}
