package ptui;

import model.RITQTCodec;

public class RITUncompress {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }

        RITQTCodec codec = new RITQTCodec();
        int[][] dataArray = codec.decodeToArray(
                RITUncompress.class.getResourceAsStream("/compressed/simple4x4.rit") );

        for( int row[] : dataArray ) {
            for( int value : row ) {
                System.out.println(value);
            }
        }
    }

}