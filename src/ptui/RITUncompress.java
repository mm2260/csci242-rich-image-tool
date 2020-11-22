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
        int[][] dataArray = codec.decodeToArray(
                RITUncompress.class.getResourceAsStream("/compressed/simple4x4.rit") );

        for( int row[] : dataArray ) {
            for( int value : row ) {
                System.out.println(value);
            }
        }

        System.out.println("\nOutput File: " + outputFilename);
    }

}