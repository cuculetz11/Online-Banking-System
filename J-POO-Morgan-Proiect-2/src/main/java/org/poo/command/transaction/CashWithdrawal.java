package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.fileio.CommandInput;
import org.poo.services.BankMethods;
import org.poo.services.payment.PaymentMethod;
import org.poo.services.payment.PaymentStrategy;
import org.poo.services.withdraw.WithdrawCashAction;

public class CashWithdrawal implements Command {
    /**
     * Se scot bani de pe card
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        PaymentStrategy cashWithdrawal = new WithdrawCashAction();
        if (cashWithdrawal.checkForErrors(input)) {
            return;
        }
        BankMethods withdraw = new PaymentMethod(cashWithdrawal);
        BANKING_SERVICES.acceptVisitor(withdraw);
    }
}
