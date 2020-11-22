package model;

import java.io.InputStream;
import java.util.Scanner;
import java.util.stream.IntStream;

public class RITQTCodec {

    public RITQTNode encode(  ) {
        //TODO
        return null;
    }

    public int[] decodeToArray( InputStream resource ) {
        Scanner fileScanner = new Scanner(resource);

        int size = fileScanner.nextInt();
        int[] dataArray = new int[size];
        int sideLength = (int) Math.sqrt(size);

        new Decoder(sideLength, sideLength, fileScanner).decode(dataArray, size, 0, 0);
        return dataArray;
    }

    private static class Decoder {

        private final Scanner fileScanner;
        private final int imageWidth;
        private final int imageHeight;

        public Decoder(int imageWidth, int imageHeight, Scanner fileScanner) {
            this.fileScanner = fileScanner;
            this.imageHeight = imageHeight;
            this.imageWidth = imageWidth;
        }

        private void decode(int[] dataArray, int size, int startRow, int startCol) {
            int val = this.fileScanner.nextInt();

            if(size==1) {
                dataArray[ castToIndex(startRow, startCol) ] = val;
            } else {
                float sqrtSizeDividedByTwo = (float) (Math.sqrt(size) / 2);

                //Split node:
                if (val == -1) {
                    //ul:
                    decode(dataArray, size/4, startRow, startCol);
                    //ur:
                    decode(dataArray, size/4, startRow,
                            (int) (startCol + sqrtSizeDividedByTwo));
                    //ll:
                    decode(dataArray, size/4,
                            (int) (startRow + sqrtSizeDividedByTwo), startCol);
                    //lr:
                    decode(dataArray, size/4,
                            (int) (startRow + sqrtSizeDividedByTwo),
                            (int) (startCol + sqrtSizeDividedByTwo));
                } else {
                    //Fill in pixel values.
                    IntStream.range(startRow, startRow+ (int)sqrtSizeDividedByTwo *2 ).forEach(row -> {
                        IntStream.range(startCol, startCol+(int)sqrtSizeDividedByTwo *2 ).forEach( col -> {
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
}
