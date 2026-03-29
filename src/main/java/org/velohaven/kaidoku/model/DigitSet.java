package org.velohaven.kaidoku.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class DigitSet implements Iterable<Integer>, Comparable<DigitSet> {

    private static final char FIRST_SYMBOL = '\u2460'; // ①

    private final DigitSetFactory factory;
    private final int digits;

    DigitSet(DigitSetFactory factory, int digits) {
        this.factory = Objects.requireNonNull(factory, "factory must not be null");
        this.digits = digits & factory.usableMask();
    }

    public int maxSize() {
        return factory.maxSize();
    }

    public int size() {
        return Integer.bitCount(digits);
    }

    public boolean isEmpty() {
        return digits == 0;
    }

    public boolean isSingle() {
        return digits != 0 && (digits & (digits - 1)) == 0;
    }

    public boolean contains(int digit) {
        factory.validateDigit(digit);
        return (digits & factory.maskOf(digit)) != 0;
    }

    public boolean containsAll(DigitSet other) {
        requireCompatible(other);
        return (this.digits & other.digits) == other.digits;
    }

    public DigitSet add(int digit) {
        factory.validateDigit(digit);
        return factory.create(digits | factory.maskOf(digit), this);
    }

    public DigitSet remove(int digit) {
        factory.validateDigit(digit);
        return factory.create(digits & ~factory.maskOf(digit), this);
    }

    public DigitSet retain(int digit) {
        factory.validateDigit(digit);
        return factory.create(digits & factory.maskOf(digit), this);
    }

    public DigitSet addAll(DigitSet other) {
        requireCompatible(other);
        return factory.create(this.digits | other.digits, this);
    }

    public DigitSet removeAll(DigitSet other) {
        requireCompatible(other);
        return factory.create(this.digits & ~other.digits, this);
    }

    public DigitSet retainAll(DigitSet other) {
        requireCompatible(other);
        return factory.create(this.digits & other.digits, this);
    }

    public DigitSet intersect(DigitSet other) {
        return retainAll(other);
    }

    public int singleDigit() {
        if (!isSingle()) {
            throw new IllegalStateException(
                "DigitSet must contain exactly one digit, but contains " + size()
            );
        }
        return Integer.numberOfTrailingZeros(digits) + 1;
    }

    public int toInt() {
        return digits;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int remaining = digits;

            @Override
            public boolean hasNext() {
                return remaining != 0;
            }

            @Override
            public Integer next() {
                if (remaining == 0) {
                    throw new NoSuchElementException();
                }

                int lowestBit = remaining & -remaining;
                remaining ^= lowestBit;
                return Integer.numberOfTrailingZeros(lowestBit) + 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(
                    "DigitSet is immutable; iterator.remove() is not supported."
                );
            }
        };
    }

    @Override
    public int compareTo(DigitSet other) {
        Objects.requireNonNull(other, "other must not be null");

        int sizeCompare = Integer.compare(this.size(), other.size());
        if (sizeCompare != 0) {
            return sizeCompare;
        }

        int left = this.digits;
        int right = other.digits;

        while (left != 0 || right != 0) {
            int leftDigit = left != 0
                ? Integer.numberOfTrailingZeros(left) + 1
                : Integer.MAX_VALUE;
            int rightDigit = right != 0
                ? Integer.numberOfTrailingZeros(right) + 1
                : Integer.MAX_VALUE;

            if (leftDigit != rightDigit) {
                return Integer.compare(leftDigit, rightDigit);
            }

            if (left != 0) {
                left &= left - 1;
            }
            if (right != 0) {
                right &= right - 1;
            }
        }

        return Integer.compare(this.maxSize(), other.maxSize());
    }

    @Override
    public String toString() {
        char[] out = new char[maxSize()];
        Arrays.fill(out, ' ');

        int remaining = digits;
        while (remaining != 0) {
            int lowestBit = remaining & -remaining;
            int digit = Integer.numberOfTrailingZeros(lowestBit) + 1;
            out[digit - 1] = symbolOf(digit);
            remaining ^= lowestBit;
        }

        return new String(out);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DigitSet)) {
            return false;
        }

        DigitSet other = (DigitSet) obj;
        return this.maxSize() == other.maxSize()
            && this.digits == other.digits;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(maxSize());
        result = 31 * result + Integer.hashCode(digits);
        return result;
    }

    private void requireCompatible(DigitSet other) {
        Objects.requireNonNull(other, "other must not be null");

        if (this.maxSize() != other.maxSize()) {
            throw new IllegalArgumentException(
                "Incompatible DigitSet: maxSize differs (" +
                this.maxSize() + " vs " + other.maxSize() + ")"
            );
        }
    }

    private static char symbolOf(int digit) {
        return (char) (FIRST_SYMBOL + (digit - 1));
    }
}