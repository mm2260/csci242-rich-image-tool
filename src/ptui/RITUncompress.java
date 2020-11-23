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

        InputStream inputFile = new FileInputStream( args[0] );
        BufferedWriter writer = new BufferedWriter( new FileWriter( args[1] ) );

        RITQTCodec codec = new RITQTCodec();
        //Codec takes InputStream as parameter and returns raw pixel data as a 2D-Array.
        int[][] dataArray = codec.decodeToArray(
                inputFile );

        for( int row[] : dataArray ) {
            for( int value : row ) {
                System.out.println(value);
                writer.append( String.valueOf(value) );
                writer.newLine();
            }
        }

        writer.close();
        System.out.println("Output written to: " + args[1]);
    }

}