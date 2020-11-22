package ptui;

import model.RITQTCodec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class RITUncompress {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }

        String inputFilename = args[0];
        String outputFilename = args[1];

        RITQTCodec codec = new RITQTCodec();
        int[] dataArray = codec.decodeToArray(
                RITUncompress.class.getResourceAsStream(inputFilename) );

        FileWriter fileWriter = new FileWriter(outputFilename);//Error in writing file.
        for(int value : dataArray){
            fileWriter.write(value);
        }
        fileWriter.close();

        //Output
        System.out.println("Input file: " + inputFilename);
        for( int value : dataArray ) {
            System.out.print(value + " ");
        }

        System.out.println("\nOutput File: " + outputFilename);
    }

}