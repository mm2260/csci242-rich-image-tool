package model;

import model.exceptions.InvalidImageSpecificationException;
import model.utils.Utils;

import java.io.InputStream;
import java.util.Scanner;
import java.util.stream.IntStream;

public class RITQTCodec {

    public RITQTNode encode(  ) {
        //TODO
        return null;
    }

    public int[][] decodeToArray( InputStream resource ) throws InvalidImageSpecificationException {
        Scanner fileScanner = new Scanner(resource);

        int size = fileScanner.nextInt();

        int sideLength = (int) Math.sqrt(size);
        int[][] dataArray = new int[sideLength][sideLength];

        //Pass dataArray reference to recursive decoder function
        new Decoder(fileScanner).decode(dataArray, size, 0, 0);
        return dataArray;
    }

    private static class Decoder {

        private final Scanner fileScanner;

        public Decoder(Scanner fileScanner) {
            this.fileScanner = fileScanner;
        }

        private void decode(int[][] dataArray, int size, int startRow, int startCol) {
            int val = this.fileScanner.nextInt();

            if(size==1) {
                dataArray[startRow][startCol] = val;
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
                            dataArray[row][col] = val;
                        });
                    });
                }
            }
        }

    }
}
