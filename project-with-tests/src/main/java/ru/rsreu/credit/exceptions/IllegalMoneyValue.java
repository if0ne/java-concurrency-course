package ru.rsreu.credit.exceptions;

public class IllegalMoneyValue extends BankActionException {
    public IllegalMoneyValue(String message) {
        super(message);
    }
}
