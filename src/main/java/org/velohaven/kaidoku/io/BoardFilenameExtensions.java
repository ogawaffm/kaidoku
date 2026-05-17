package org.velohaven.kaidoku.io;

public final class BoardFilenameExtensions {

    private BoardFilenameExtensions() {
    }

    public static String getExtension(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }

        int lastSeparator = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        String name = path.substring(lastSeparator + 1);

        int lastDot = name.lastIndexOf('.');

        if (lastDot <= 0 || lastDot == name.length() - 1) {
            return "";
        }

        return name.substring(lastDot + 1);
    }

    public static boolean hasBoxSizeExtension(String path) {
        String extension = getExtension(path);
        int separator = getDimensionSeparatorIndex(extension);

        if (separator <= 0 || separator == extension.length() - 1) {
            return false;
        }

        String xPart = extension.substring(0, separator);
        String yPart = extension.substring(separator + 1);

        try {
            int x = Integer.parseInt(xPart);
            int y = Integer.parseInt(yPart);
            return x > 0 && y > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int getBoxColumnCountFromFilename(String path) {
        return getBoxSizeParts(path)[0];
    }

    public static int getBoxRowCountFromFilename(String path) {
        return getBoxSizeParts(path)[1];
    }

    private static final String EXTENSION_DOES_NOT_MATCH_FORMAT_MESSAGE = "Extension does not match <x>x<y> format: %s";
    private static final String X_AND_Y_MUST_BE_GREATER_0 = "X and Y must be > 0:  %s";

    private static int[] getBoxSizeParts(String path) {
        String extension = getExtension(path);
        int separator = getDimensionSeparatorIndex(extension);

        if (separator <= 0 || separator == extension.length() - 1) {
            throw new IllegalArgumentException(String.format(EXTENSION_DOES_NOT_MATCH_FORMAT_MESSAGE, path));
        }

        String xPart = extension.substring(0, separator);
        String yPart = extension.substring(separator + 1);

        try {
            int x = Integer.parseInt(xPart);
            int y = Integer.parseInt(yPart);

            if (x <= 0 || y <= 0) {
                throw new IllegalArgumentException(String.format(X_AND_Y_MUST_BE_GREATER_0, path));
            }

            return new int[] { x, y };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(EXTENSION_DOES_NOT_MATCH_FORMAT_MESSAGE, path), e);
        }
    }

    private static int getDimensionSeparatorIndex(String extension) {
        int lowerX = extension.indexOf('x');
        int upperX = extension.indexOf('X');

        if (lowerX >= 0 && upperX >= 0) {
            return -1;
        }

        return Math.max(lowerX, upperX);
    }
}