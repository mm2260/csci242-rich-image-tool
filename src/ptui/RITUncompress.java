package ptui;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RITUncompress {

    private Scanner fileScanner;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }

        RITUncompress ritUncompress = new RITUncompress();
        ritUncompress.fileScanner = new Scanner(
                RITUncompress.class.getResourceAsStream("/compressed/simple4x4.rit") );

        int size = ritUncompress.fileScanner.nextInt();
        int dataArray[] = new int[size];

        ritUncompress.uncompress(dataArray, size, 0, 0);

        for( int value : dataArray ) {
            System.out.println(value);
        }
    }

    private void uncompress(int dataArray[], int size, int startRow, int startCol) {

        for( int value : dataArray ) {
            System.out.print(value + " ");
        }
        System.out.println();
        
        System.out.println("RECURSIVE CALL: row " + startRow + " col: " + startCol ); //[DEBUG]

        //cases: size is 1 -->
        //       size > 1 and a power of 2.
        int val = this.fileScanner.nextInt();

        if(size==1) {
            System.out.println("\tFILLING IN PIXEL VALUE " + val + " FOR " + size +
                            " PIXELS AT INDEX " + castToIndex(size, startRow, startCol)); //[DEBUG]
            dataArray[ castToIndex(size, startRow, startCol) ] = val;
            return;
        } else {
            float sqrtSizeDividedByTwo = (float) (Math.sqrt(size) / 2);
            System.out.println("\tSQRT SIZE / 2 = " + sqrtSizeDividedByTwo); //[DEBUG]
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
                //Fill in pixel values.
                System.out.println("\tFILLING IN PIXEL VALUE " + val + " FOR " + size + " PIXELS"); //[DEBUG]
                int start = castToIndex(size, startRow, startCol);
                int end = start + size;
                System.out.println("\tEND " + end); //[DEBUG]
                //Fill in the values.
                IntStream.range(start, end).forEach(i -> {
                    dataArray[i] = val;
                });
            }
        }
    }

    private int castToIndex(int size, int row, int col) {
        return row*(size*2) + col%(size*2);
    }
}