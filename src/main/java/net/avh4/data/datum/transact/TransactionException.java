package net.avh4.data.datum.transact;

public class TransactionException extends Exception {
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
