package seedu.address.model;

import java.util.function.Predicate;

public abstract class StateAwarePredicate <T, U> implements Predicate<T> {
    public void notifyStateChanged(U oldState, U newState) {};
}
