package model.util.exception;

public class IllegalImageSpecification extends RuntimeException {

    int invalidLength;
    String source;

    public IllegalImageSpecification(int length, String source) {
        this.invalidLength = length;
        this.source = source;
    }

    @Override
    public String getMessage() {
        return String.format("Image must be square. Image file \"%s\" has length (size*size) = %d, which is invalid.",
                source, invalidLength);
    }
}
