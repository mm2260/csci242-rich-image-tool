package ptui;

import model.RITQTCodec;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RITUncompress {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }

        RITQTCodec codec = new RITQTCodec();
        int[] dataArray = codec.decodeToArray(
                RITUncompress.class.getResourceAsStream("/compressed/simple8x8-2.rit") );

        for( int value : dataArray ) {
            System.out.println(value);
        }
    }

}