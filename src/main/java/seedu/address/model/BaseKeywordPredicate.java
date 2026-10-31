package seedu.address.model;

import java.util.List;

public abstract class BaseKeywordPredicate <T, U> extends StateAwarePredicate<T, U> {
    private static final String LABEL = "Filtered: \"%s\"";

    private final List<String> keywords;

    public BaseKeywordPredicate(List<String> keywords) {
        this.keywords = List.copyOf(keywords);
    }

    @Override
    public boolean test(T item) {
        return keywords.stream()
                .anyMatch(kw -> matchesKeyword(item, kw));
    }

    @SuppressWarnings("rawtypes") // raw casting is valid for equivalence checking at this level
    @Override
    public boolean equals(Object other) {
        return other instanceof BaseKeywordPredicate && keywords.equals(((BaseKeywordPredicate)other).keywords);
    }

    public abstract boolean matchesKeyword(T item, String keyword);

    @Override
    public String toString() {
        return String.format(LABEL, String.join(" ", keywords));
    }
}
