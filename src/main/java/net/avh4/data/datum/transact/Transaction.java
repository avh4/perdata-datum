package net.avh4.data.datum.transact;

import fj.data.List;
import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.transact.commands.*;

public class Transaction {
    private final List<Command> commands;

    public Transaction() {
        this(List.<Command>nil());
    }

    protected Transaction(List<Command> commands) {
        this.commands = commands;
    }

    public Transaction and(Command command) {
        return new Transaction(commands.cons(command));
    }

    public Iterable<Command> commands() {
        return commands;
    }

    //
    // All the following methods are simply convenience methods to create various commonly-used Command objects
    //

    public Transaction set(Id entity, String action, String value) {
        return and(new Set(entity, action, value));
    }

    public Transaction set(Id entity, String attribute, Id ref) {
        return and(new SetRef(entity, attribute, ref));
    }

    public Transaction add(Id entity, String action, String value) {
        return and(new Add(entity, action, value));
    }

    public Transaction add(Id entity, String attribute, Id ref) {
        return and(new AddRef(entity, attribute, ref));
    }

    public Transaction remove(Id entity, String action, String value) {
        return and(new Remove(entity, action, value));
    }

    public Transaction remove(Id entity, String attribute, Id ref) {
        return and(new RemoveRef(entity, attribute, ref));
    }

    public Transaction inc(Id entity, String attribute) {
        return and(new Increment(entity, attribute));
    }
}
