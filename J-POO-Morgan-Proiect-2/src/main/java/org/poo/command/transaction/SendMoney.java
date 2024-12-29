package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.fileio.CommandInput;
import org.poo.services.BankMethods;
import org.poo.services.payment.BankTransferStrategy;
import org.poo.services.payment.PaymentMethod;
import org.poo.services.payment.PaymentStrategy;

public class SendMoney implements Command {
    /**
     * Se realizeaza un transfer bancar
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        PaymentStrategy bankTransfer = new BankTransferStrategy();
        if (bankTransfer.checkForErrors(input)) {
            return;
        }
        BankMethods transfer = new PaymentMethod(bankTransfer);
        BANKING_SERVICES.acceptVisitor(transfer);
    }
}
