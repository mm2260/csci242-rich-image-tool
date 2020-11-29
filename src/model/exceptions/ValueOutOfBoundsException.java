package model.exceptions;

import java.util.List;

public class ValueOutOfBoundsException extends RuntimeException {
    public ValueOutOfBoundsException(List<Integer> values){
        for(int value: values){
            if(value < 0 || value > 255 ) {
                System.out.println("[-] Pixel value not between 0 and 255.");
                System.exit(-1);
            }
        }
    }
}
