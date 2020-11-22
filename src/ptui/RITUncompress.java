package ptui;

import model.RITQTCodec;

import java.io.*;
import java.nio.file.Path;

public class RITUncompress {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }

        //TODO: Add error handling.

        InputStream inputFile = new FileInputStream( args[0] );
//        FileOutputStream outputFile = new FileOutputStream( args[1] );

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