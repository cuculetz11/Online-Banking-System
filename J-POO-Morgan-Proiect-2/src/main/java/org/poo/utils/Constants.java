package org.poo.utils;

public final  class Constants {
    private Constants() { }
    public static final String PRINT_USERS = "printUsers";
    public static final String ADD_ACCOUNT = "addAccount";
    public static final String CREATE_CARD = "createCard";
    public static final String CREATE_ONE_TIME_CARD = "createOneTimeCard";
    public static final String ADD_FUNDS = "addFunds";
    public static final String DELETE_ACCOUNT = "deleteAccount";
    public static final String DELETE_CARD = "deleteCard";
    public static final String SET_MINIMUM_BALANCE = "setMinimumBalance";
    public static final String PAY_ONLINE = "payOnline";
    public static final String SEND_MONEY = "sendMoney";
    public static final String SET_ALIAS = "setAlias";
    public static final String PRINT_TRANSACTIONS = "printTransactions";
    public static final String CHECK_CARD_STATUS = "checkCardStatus";
    public static final String CHANGE_INTEREST_RATE = "changeInterestRate";
    public static final String SPLIT_PAYMENT = "splitPayment";
    public static final String REPORT = "report";
    public static final String SPENDINGS_REPORT = "spendingsReport";
    public static final String ADD_INTEREST = "addInterest";
    //------------------------------
    public static final String COMMAND_NOT_FOUND_ERROR = "Command not found";
    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String NOT_CLASSIC_ACCOUNT =
            "This kind of report is not supported for a saving account";
    public static final String NOT_SAVINGS_ACCOUNT = "This is not a savings account";
    public static final String CARD_PAYMENT = "Card payment";
    public static final String NEW_ACCOUNT_CREATED = "New account created";
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds";
    public static final String SENT = "sent";
    public static final String RECEIVED = "received";
    public static final String THE_CARD_HAS_BEEN_DESTROYED = "The card has been destroyed";
    public static final String CARD_NOT_FOUND = "Card not found";
    public static final String ACCOUNT_DELETED = "Account deleted";
    public static final String ACCOUNT_CANT_BE_DELETED =
            "Account couldn't be deleted - see org.poo.transactions for details";
    public static final String ACCOUNT_CANT_BE_DELETED_FUNDS =
            "Account couldn't be deleted - there are funds remaining";
    public static final String FROZEN = "frozen";
    public static final String THE_CARD_WILL_BE_FROZEN =
            "You have reached the minimum amount of funds, the card will be frozen";
    public static final String NEW_CARD_CREATED = "New card created";
    public static final String THE_CARD_IS_FROZEN    = "The card is frozen";
    //---------------------------
    public static final String ADD_ACCOUNT_TRANSACTION =
            "AddAccountTransaction";
    public static final String CHANGE_INTEREST_RATE_TRANSACTION =
            "ChangeInterestRateTransaction";
    public static final String ADD_CARD_TRANSACTION =
            "AddCardTransaction";
    public static final String DELETE_ACCOUNT_FAIL_TRANSACTION =
            "DeleteAccountTransaction";
    public static final String DELETE_CARD_TRANSACTION =
            "DeleteCardTransaction";
    public static final String FROZEN_CARD_TRANSACTION =
            "FrozenCardTransaction";
    public static final String INSUFFICIENT_FUNDS_PAY_ONLINE_TRANSACTION =
            "InsufficientFundsTransaction";
    public static final String CARD_PAYMENT_TRANSACTION =
            "CardPaymentTransaction";
    public static final String INSUFFICIENT_FUNDS_TRANSFER_TRANSACTION =
            "InsufficientFundsTransferTransactionT";
    public static final String TRANSFER_TRANSACTION =
            "TransferTransaction";
    public static final String SPLIT_PAYMENT_FAILED_TRANSACTION =
            "SplitPaymentFailedTransaction";
    public static final String SPLIT_PAYMENT_TRANSFER =
            "SplitPaymentTransfer";
    public static final String CHECK_CARD_TRANSACTION =
            "CheckCardTransaction";
}
