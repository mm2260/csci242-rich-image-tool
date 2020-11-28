package model.exceptions;

public class ValueOutOfBoundsException extends RuntimeException {
    public ValueOutOfBoundsException(){
        System.out.println("[-] Pixel value not between 0 and 255.");
    }
}
