package org.velohaven.kaidoku.model;

public class DigitConverter {

    private static final String SYMBOLS = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private DigitConverter() {
    }

    public static boolean isValidSymbol(char symbol) {
        return  (symbol >= '1' && symbol <= '9')
                || (symbol >= 'a' && symbol <= 'z') ||  (symbol >= 'A' && symbol <= 'Z');
    }

    public static boolean isValidSymbol(char symbol, int maxValue) {
        return isValidSymbol(symbol) && symbolToDigitValue(symbol) <= maxValue;
    }

    private static void throwInvalidSymbol(char symbol, int maxValue) {
        throw new IllegalArgumentException("'" + symbol + "' is no valid symbol for a digit with max value "
                + maxValue + ". Valid symbols are: " + SYMBOLS.substring(0, maxValue + 1));
    }

    public static int symbolToDigitValue(char symbol) {
        int value = 0;
        if (symbol >= '1' && symbol <= '9') {
            value = symbol - '0';
        } else if (symbol >= 'a' && symbol <= 'z') {
            value = symbol - 'a' + 10;
        } else if (symbol >= 'A' && symbol <= 'F') {
            value = symbol - 'A' + 10;
        } else {
            throwInvalidSymbol(symbol, SYMBOLS.length());
        }
        return value;
    }

    public static int symbolToDigitValue(char symbol, int maxValue) {
        int value = symbolToDigitValue(symbol);
        if (value > maxValue) {
            throwInvalidSymbol(symbol, maxValue);
        }
        return value;
    }

    public static char digitValueToSymbol(int digitValue) {
        if (digitValue < 1 || digitValue > SYMBOLS.length()) {
            throw new IllegalArgumentException("Digit value " + digitValue +
                    " is out of bounds. Valid digit values are >= 1 and <= " + SYMBOLS.length()
            );
        }
        return SYMBOLS.charAt(digitValue - 1);
    }

}
