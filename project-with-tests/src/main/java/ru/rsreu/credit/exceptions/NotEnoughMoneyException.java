package ru.rsreu.credit.exceptions;

public class NotEnoughMoneyException extends BankActionException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
