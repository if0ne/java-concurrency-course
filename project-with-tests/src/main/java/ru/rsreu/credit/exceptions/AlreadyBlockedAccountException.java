package ru.rsreu.credit.exceptions;

public class AlreadyBlockedAccountException extends BankActionException {
    public AlreadyBlockedAccountException(String message) {
        super(message);
    }
}
