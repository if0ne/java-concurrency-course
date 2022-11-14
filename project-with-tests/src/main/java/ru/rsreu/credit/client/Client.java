package ru.rsreu.credit.client;

import java.util.Objects;
import java.util.UUID;

public class Client {
    private final UUID id;

    public Client() {
        id = UUID.randomUUID();
    }

    public UUID id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
