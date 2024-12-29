package org.poo.command;

import org.poo.fileio.CommandInput;
import org.poo.services.BankingServices;

public interface Command {
    BankingServices BANKING_SERVICES = new BankingServices();

    /**
     * Executa o comanda specifica bazata pe input-ul primit
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    void execute(CommandInput input);
}
