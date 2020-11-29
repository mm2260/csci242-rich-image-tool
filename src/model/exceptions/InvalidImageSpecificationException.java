package model.exceptions;

public class InvalidImageSpecificationException extends RuntimeException {
    public InvalidImageSpecificationException(int size) {
        double side = Math.sqrt(size);
        if(side != (int) side ) {
            System.out.println("[-] The image is not a square -- image size(" + size + ") is not a power of 2.");
        }
    }
}
