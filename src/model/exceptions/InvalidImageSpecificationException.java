package model.exceptions;

public class InvalidImageSpecificationException extends RuntimeException {
    public InvalidImageSpecificationException(int size) {
        System.out.println("[-] The image is not a square -- image size(" + size*size+ ") is not a power of 2.");
    }
}
