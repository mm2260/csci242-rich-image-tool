package model.utils;

import model.exceptions.InvalidImageSpecificationException;
import model.exceptions.ValueOutOfBoundsException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static void writeDataArrayToFile(String fileName) {

    }

    public static List<Integer> readFileDataToList(String fileName) throws FileNotFoundException {
        //Load file into a scanner and generate a grayscale image from its data.

        //Throws FileNotFoundException
        InputStream fileInput = new FileInputStream( fileName );
        Scanner fileScanner = new Scanner( fileInput );
        InputStream validationInputStream = new FileInputStream( fileName );
        Scanner validationScanner = new Scanner( validationInputStream );

        //Validation parse through input file, calculate size.

        int imageWidth, imageHeight;
        int size = 0;

        //Throws ValueOutOfBoundsException, MalformedDataException
        while( validationScanner.hasNextInt() ) {
            int value = validationScanner.nextInt();
            if(value < 0 || value > 255 ) {
                throw new ValueOutOfBoundsException();
            }
            ++size;
        }

        imageHeight = imageWidth = (int)( Math.sqrt(size) ) ;
        size = imageHeight;

        //Throws InvalidSpecificationException
        if( !isPowerOfTwo(size) ) {
            throw new InvalidImageSpecificationException(size);
        }

        List<Integer> values = new ArrayList<>();
        while (fileScanner.hasNextInt()){
            values.add(fileScanner.nextInt());
        }

        return values;
    }

    public static boolean isPowerOfTwo(int x) {
        return ( x!=0 ) && ( ( x & (x-1) ) == 0 );
    }
}
