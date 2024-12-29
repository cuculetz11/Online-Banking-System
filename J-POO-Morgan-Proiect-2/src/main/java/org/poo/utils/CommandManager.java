package org.poo.utils;

import org.poo.command.CheckCardStatus;
import org.poo.command.Command;
import org.poo.command.SetMinBalance;
import org.poo.command.debug.PrintTransaction;
import org.poo.command.debug.PrintUsers;
import org.poo.command.report.Report;
import org.poo.command.report.SpendingReport;
import org.poo.command.transaction.*;

/*
Aceasta calsa e un fel de factory pentu comenzi
aceeasti aborfare am avut o si in tema trevute mi se pare o abordare eleganat si usoara
 */
public final class CommandManager {
    private CommandManager() {
    }

    /**
     *
     * @param commandName numele comenzii
     * @return retureneaza o clasa ce va executa o comanda specifica
     */
    public static Command getConcreteCommand(final String commandName) {
        return switch (commandName) {
            case Constants.PRINT_USERS -> new PrintUsers();
            case Constants.ADD_ACCOUNT -> new AddAccount();
            case Constants.CREATE_CARD, Constants.CREATE_ONE_TIME_CARD -> new CreateCard();
            case Constants.ADD_FUNDS -> new AddFounds();
            case Constants.DELETE_ACCOUNT -> new DeleteAccount();
            case Constants.DELETE_CARD -> new DeleteCard();
            case Constants.SET_MINIMUM_BALANCE -> new SetMinBalance();
            case Constants.PAY_ONLINE -> new PayOnline();
            case Constants.SEND_MONEY -> new SendMoney();
            case Constants.SET_ALIAS -> new SetAlias();
            case Constants.PRINT_TRANSACTIONS -> new PrintTransaction();
            case Constants.CHECK_CARD_STATUS -> new CheckCardStatus();
            case Constants.CHANGE_INTEREST_RATE -> new ChangeInterestRate();
            case Constants.SPLIT_PAYMENT -> new SplitPayment();
            case Constants.REPORT -> new Report();
            case Constants.SPENDINGS_REPORT -> new SpendingReport();
            case Constants.ADD_INTEREST -> new AddInterest();
            default -> throw new IllegalArgumentException(Constants.COMMAND_NOT_FOUND_ERROR);
        };
    }

}
