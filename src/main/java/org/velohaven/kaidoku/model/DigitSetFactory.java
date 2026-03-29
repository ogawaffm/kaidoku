package org.velohaven.kaidoku.model;

public final class DigitSetFactory {

    private final int maxSize;
    private final int usableMask;

    private final DigitSet allDigits;
    private final DigitSet[] singleDigits;

    private DigitSetFactory(int maxSize) {
        validateMaxSize(maxSize);

        this.maxSize = maxSize;
        this.usableMask = createUsableMask(maxSize);

        this.allDigits = new DigitSet(this, usableMask);

        this.singleDigits = new DigitSet[maxSize + 1];
        for (int digit = 1; digit <= maxSize; digit++) {
            this.singleDigits[digit] = new DigitSet(this, maskOf(digit));
        }
    }

    public static DigitSetFactory forMaxSize(int maxSize) {
        return new DigitSetFactory(maxSize);
    }

    public int maxSize() {
        return maxSize;
    }

    public DigitSet allDigits() {
        return allDigits;
    }

    public DigitSet singleDigit(int digit) {
        validateDigit(digit);
        return singleDigits[digit];
    }

    int usableMask() {
        return usableMask;
    }

    int maskOf(int digit) {
        return 1 << (digit - 1);
    }

    void validateDigit(int digit) {
        if (digit < 1 || digit > maxSize) {
            throw new IllegalArgumentException(
                "digit must be between 1 and " + maxSize + ": " + digit
            );
        }
    }

    DigitSet create(int requestedDigits, DigitSet current) {
        int normalized = requestedDigits & usableMask;

        if (normalized == current.toInt()) {
            return current;
        }
        if (normalized == usableMask) {
            return allDigits;
        }
        if (normalized != 0 && (normalized & (normalized - 1)) == 0) {
            return singleDigits[Integer.numberOfTrailingZeros(normalized) + 1];
        }

        return new DigitSet(this, normalized);
    }

    private static void validateMaxSize(int maxSize) {
        if (maxSize < 1 || maxSize > 32) {
            throw new IllegalArgumentException(
                "maxSize must be between 1 and 32: " + maxSize
            );
        }
    }

    private static int createUsableMask(int maxSize) {
        return maxSize == 32 ? -1 : (1 << maxSize) - 1;
    }
}