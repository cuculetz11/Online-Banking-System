package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.fileio.CommandInput;
import org.poo.services.BankMethods;
import org.poo.services.payment.PaymentMethod;
import org.poo.services.payment.PaymentStrategy;
import org.poo.services.withdraw.WithdrawCashAction;

public class CashWithdrawal implements Command {

    @Override
    public void execute(CommandInput input) {
        PaymentStrategy cashWithdrawal = new WithdrawCashAction();
        if(cashWithdrawal.checkForErrors(input)){
            return;
        }
        BankMethods withdraw = new PaymentMethod(cashWithdrawal);
        BANKING_SERVICES.acceptVisitor(withdraw);
    }
}
