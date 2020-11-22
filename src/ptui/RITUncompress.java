package ptui;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RITUncompress {

    private Scanner fileScanner;
    private int imageWidth;
    private int imageHeight;

    public RITUncompress(int imageWidth, int imageHeight, Scanner fileScanner) {
        this.fileScanner = fileScanner;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }

        Scanner fileScanner = new Scanner(
                RITUncompress.class.getResourceAsStream("/compressed/simple8x8.rit") );
        int size = fileScanner.nextInt();
        int sideLength = (int) Math.sqrt(size);
        RITUncompress ritUncompress = new RITUncompress( sideLength, sideLength, fileScanner );

        int dataArray[] = new int[size];
        ritUncompress.uncompress(dataArray, size, 0, 0);

        for( int value : dataArray ) {
            System.out.println(value);
        }
    }

    private void uncompress(int dataArray[], int size, int startRow, int startCol) {

        //[DEBUG]
        //System.out.println("RECURSIVE CALL: row " + startRow + " col: " + startCol ); //[DEBUG]

        //cases: size is 1 -->
        //       size > 1 and a power of 2.
        int val = this.fileScanner.nextInt();

        if(size==1) {
            //[DEBUG]
            //System.out.println("\tFILLING IN PIXEL VALUE " + val + " FOR " + size +
            //                " PIXEL AT INDEX " + castToIndex(startRow, startCol));
            int index = castToIndex(startRow, startCol);
            //[DEBUG]
            //System.out.println("\t\tROW, COL "+ startRow+","+startCol);
            //System.out.println("\t\tVALUE " + val + " AT INDEX "+ index);
            dataArray[ index ] = val;
            return;
        } else {
            float sqrtSizeDividedByTwo = (float) (Math.sqrt(size) / 2);

            //[DEBUG]
            //System.out.println("\tSQRT SIZE / 2 = " + sqrtSizeDividedByTwo);

            //Split node:
            if (val == -1) {
                //ul:
                uncompress(dataArray, size/4, startRow, startCol);
                //ur:
                uncompress(dataArray, size/4, startRow,
                        (int) (startCol + sqrtSizeDividedByTwo));
                //ll:
                uncompress(dataArray, size/4,
                        (int) (startRow + sqrtSizeDividedByTwo), startCol);
                //lr:
                uncompress(dataArray, size/4,
                        (int) (startRow + sqrtSizeDividedByTwo),
                        (int) (startCol + sqrtSizeDividedByTwo));
            } else {
                //[DEBUG]
                //System.out.println("\tFILLING IN PIXEL VALUE " + val + " FOR " + size + " PIXELS");

                //Fill in pixel values.
                IntStream.range(startRow, startRow+ (int)sqrtSizeDividedByTwo *2 ).forEach( row -> {
                    IntStream.range(startCol, startCol+(int)sqrtSizeDividedByTwo *2 ).forEach( col -> {
                        //[DEBUG]
                        //System.out.println("\t\tROW, COL "+row +","+col);
                        int index = castToIndex(row, col);
                        //[DEBUG]
                        //System.out.println("\t\tVALUE " + val + " AT INDEX " + index);
                        dataArray[ castToIndex(row, col) ] = val;
                    });
                });
            }
        }
    }

    private int castToIndex(int row, int col) {
        return row*(imageWidth) + col%(imageHeight);
    }
}