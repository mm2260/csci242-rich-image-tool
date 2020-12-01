package model.util.exception;

public class IllegalPixelValueException extends RuntimeException {

    private String rawValue;
    private String source;
    public IllegalPixelValueException(String rawValue, String source) {
        this.rawValue = rawValue;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format(
                "Raw Value '%s' in file \"%s\" is not a valid pixel value. {legal values are always integers between 0 and 255 (inclusive)}",
                this.rawValue, this.source);
    }
}
