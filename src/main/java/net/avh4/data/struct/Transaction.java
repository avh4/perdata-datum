package net.avh4.data.struct;

import javax.transaction.TransactionRequiredException;

public interface Transaction {
    public void run() throws TransactionRequiredException;
}
