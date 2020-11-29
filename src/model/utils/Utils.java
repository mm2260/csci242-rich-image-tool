package model.utils;

import model.exceptions.InvalidImageSpecificationException;
import model.exceptions.ValueOutOfBoundsException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static void writeDataArrayToFile(String fileName) {

    }

    public static List<Integer> readFileDataToList(String fileName) throws FileNotFoundException {

        InputStream fileInput = new FileInputStream( fileName );
        InputStream validationInputStream = new FileInputStream( fileName );
        Scanner validationScanner = new Scanner( validationInputStream );
        boolean check = false;
        int size = 0;
        List<Integer> values = new LinkedList<>();


        while( validationScanner.hasNextInt() ) {
            int value = validationScanner.nextInt();
            values.add(value);
            if(value>255 || value<0){
                check = true;
            }
            ++size;
        }

        double side = Math.sqrt(size);
        if(side != (int) side ) {
            throw new InvalidImageSpecificationException(size);
        }

        if(check){
            throw new ValueOutOfBoundsException(values);
        }



        return values;




    }
}
