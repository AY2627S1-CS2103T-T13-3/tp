package seedu.address.model;

public abstract class BaseLinkedPredicate <T, U extends Titled> extends StateAwarePredicate<T, U> {

    private static final String LABEL = "Filtered by %s: \"%s\"";

    private U key;

    public BaseLinkedPredicate(U key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return String.format(LABEL, key.getTypeTitle(), key.getInstanceTitle());
    }

    @Override
    public boolean test(T item) {
        return isLinked(item, key);
    }

    @Override
    public void notifyStateChanged(U oldState, U newState) {
        if (key.equals(oldState)) {
            key = newState;
        }
    }

    public abstract boolean isLinked(T item, U other);
}
