package model.util.exception;

public class IllegalImageSpecification extends RuntimeException {

    String message;

    public IllegalImageSpecification(String malformedSize, String source) {
        this.message = String.format("Image size must be a square number. Image file \"%s\" has malformed size data: %s.",
                source, malformedSize);
    }

    public IllegalImageSpecification(int length, String source) {
        this.message = String.format("Image must be square. Image file \"%s\" has length (size*size) = %d, which is invalid.",
                source, length);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
