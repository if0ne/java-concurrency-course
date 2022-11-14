package ru.rsreu.credit.bank;

import java.math.BigDecimal;

public class Account {
    private BigDecimal value;
    private Status status;

    public Account(BigDecimal value) {
        this.status = Status.Unblocked;
        this.value = value;
    }

    public BigDecimal value() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Status status() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "value=" + value +
                ", status=" + status +
                '}';
    }
}
