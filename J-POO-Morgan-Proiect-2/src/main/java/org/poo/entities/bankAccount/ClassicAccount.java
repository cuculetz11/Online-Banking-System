package org.poo.entities.bankAccount;

import org.poo.command.debug.dto.*;
import org.poo.entities.Bank;
import org.poo.entities.Merchant;
import org.poo.entities.card.Card;
import org.poo.entities.transaction.CardPayment;
import org.poo.entities.transaction.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.services.BankingServices;
import org.poo.utils.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ClassicAccount extends Account {
    public ClassicAccount(final CommandInput input) {
        super(0, input.getCurrency(), "classic");
    }


    /**
     * {@inheritDoc}
     * @param input datale necesare
     */
    @Override
    public void addInterest(final CommandInput input) {
        ErrorManager.notFound(Constants.NOT_SAVINGS_ACCOUNT, input.getCommand(),
                input.getTimestamp());
    }

    /**
     * {@inheritDoc}
     * @param input datele necesare
     */
    @Override
    public void changeInterestRate(final CommandInput input) {
        ErrorManager.notFound(Constants.NOT_SAVINGS_ACCOUNT, input.getCommand(),
                input.getTimestamp());
    }

    /**
     * {@inheritDoc}
     * @param input datale necesare
     */
    @Override
    public void spendingReport(final CommandInput input) {
        if(input.getCommand().equals(Constants.BUSINESS_REPORT)) {
            System.out.println("nu e const business");
            return;
        }
        ArrayList<Transaction> transactions = new ArrayList<>();
        LinkedHashMap<String, Merchant> merchants = new LinkedHashMap<>();


        for (Transaction transaction : this.getTransactionsHistory()) {
            if (transaction.getTimestamp() > input.getEndTimestamp()) {
                break;
            }
            if (transaction.getTimestamp() >= input.getStartTimestamp()
                    && transaction.getDescription().equals(Constants.CARD_PAYMENT)) {
                transactions.add(transaction);
                CardPayment cardPayment = (CardPayment) transaction;
                if (!merchants.containsKey(cardPayment.getCommerciant())) {
                    Merchant merchant = new Merchant(cardPayment.getCommerciant(),
                            cardPayment.getAmount());
                    merchants.put(cardPayment.getCommerciant(), merchant);
                } else {
                    merchants.get(cardPayment.getCommerciant()).setTotal(merchants.get(cardPayment
                            .getCommerciant()).getTotal() + cardPayment.getAmount());
                }
            }
        }
        ArrayList<Merchant> merchantsList = new ArrayList<>(merchants.values());
        merchantsList.sort((c2, c1) -> c2.getCommerciant().compareTo(c1.getCommerciant()));
        AccountMerchantsDTO accountCommerciantsDTO = new AccountMerchantsDTO(input.getAccount(),
                this.getBalance(), this.getCurrency(), transactions, merchantsList);
        DebugActionsDTO<AccountMerchantsDTO> spendingReports =
                new DebugActionsDTO<>(input.getCommand(), accountCommerciantsDTO,
                        input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(spendingReports);
    }

    @Override
    public void deposit(CommandInput input) {
        if(!input.getEmail().equals(this.getUser().getEmail()))
            return;
        setBalance(getBalance() + input.getAmount());
    }

    @Override
    public void setMinBalance(CommandInput input) {
        setMinimumBalance(input.getAmount());
    }

    @Override
    public void deleteCard(CommandInput input) {
        if (!this.getUser().getEmail().equals(input.getEmail())) {
            System.out.println("acest card nu e a userului dat in comanda");
        }
        BankingServices bankingServices = new BankingServices();

        String iban = Bank.getInstance().getCards().get(input.getCardNumber()).getAccount()
                .getIban();
        if(this.getBalance() != 0)
            return;
        bankingServices.deleteCard(this, input.getCardNumber());

        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.THE_CARD_HAS_BEEN_DESTROYED,
                        input.getTimestamp())
                        .transactionName(Constants.DELETE_CARD_TRANSACTION)
                        .cardNumber(input.getCardNumber())
                        .userEmail(input.getEmail())
                        .iban(iban)
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
    }

    @Override
    public void deleteAccount(CommandInput input) {
        AccountDeleteInfo data = null;
        if(this.getBalance() > 0) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.ACCOUNT_CANT_BE_DELETED_FUNDS,
                            input.getTimestamp())
                            .transactionName(Constants.DELETE_ACCOUNT_FAIL_TRANSACTION)
                            .userEmail(input.getEmail())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            data = new ErrorPrint(Constants.ACCOUNT_CANT_BE_DELETED, input.getTimestamp());
            DebugActionsDTO<AccountDeleteInfo> wasAccountDeleted =
                    new DebugActionsDTO<>(input.getCommand(), data, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(wasAccountDeleted);
            return;
        }

        for (Card c : this.getCards().values()) {
            Bank.getInstance().getCards().remove(c.getCardNumber());
        }
        this.getCards().clear();
        Bank.getInstance().getUsers().get(input.getEmail()).getAccounts().remove(this.getIban());
        Bank.getInstance().getAccounts().remove(this.getIban());

        data = new DeleteAccountDTO(Constants.ACCOUNT_DELETED, input.getTimestamp());
        DebugActionsDTO<AccountDeleteInfo> wasAccountDeleted =
                new DebugActionsDTO<>(input.getCommand(), data, input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(wasAccountDeleted);
    }
}
