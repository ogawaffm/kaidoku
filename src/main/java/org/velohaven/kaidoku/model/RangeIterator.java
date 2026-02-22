package org.velohaven.kaidoku.model;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Generic iterable for iterating over elements of a Range.
 * This class can be used multiple times in for-each loops without issues.
 *
 * @param <T> the type of elements returned by this iterator
 */
public class RangeIterator<T> implements Iterable<T> {

    private final Function<Integer, T> mapper;
    private final int maxIndex;

    /**
     * Creates a new RangeIterator.
     *
     * @param mapper   function that maps an index to an element
     * @param maxIndex the number of elements to iterate over
     */
    public RangeIterator(Function<Integer, T> mapper, int maxIndex) {
        this.mapper = mapper;
        this.maxIndex = maxIndex;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < maxIndex;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return mapper.apply(currentIndex++);
            }
        };
    }
}

