package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;
import org.poo.services.BankMethods;
import org.poo.services.payment.CardPaymentStrategy;
import org.poo.services.payment.PaymentMethod;
import org.poo.services.payment.PaymentStrategy;

public class PayOnline implements Command {
    /**
     * Se efectueaza o plata online cu cardul
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        Bank.getInstance().setCurrentTimestamp(input.getTimestamp());

        PaymentStrategy cardPay = new CardPaymentStrategy();
        if (cardPay.checkForErrors(input)) {
            return;
        }
        BankMethods pay = new PaymentMethod(cardPay);

        BANKING_SERVICES.acceptVisitor(pay);

    }
}
