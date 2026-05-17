package org.velohaven.kaidoku.model;

public class Validation {

    private Validation() {
    }

    public static  <T> T requireNonNull(T obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " must not be null");
        } else  {
            return obj;
        }
    }

    public static int checkValue(int value, int minValue, int maxValue, String valueName) {
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException(
                    valueName + " " + value + " is out of bounds. Must be >=" + minValue + " and <= " + maxValue
            );
        }
        return value;
    }

}
