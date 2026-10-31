package seedu.address.model;

public class ListAllPredicate<T, U> extends StateAwarePredicate<T, U> {
    @Override
    public boolean test(Object o) {
        return true;
    }

    @Override
    public String toString() {
        return "All";
    }
}
