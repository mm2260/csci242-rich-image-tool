package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

//TODO: Add codec documentation.
public class RITQTCodec {

    private static class Encoder {

    }

    public int[][] decodeToArray( InputStream resource ) {
        Scanner fileScanner = new Scanner(resource);

        //TODO: Move data loading responsibility to separate class.
        int size = fileScanner.nextInt();
        int sideLength = (int) Math.sqrt(size);
        int[][] dataArray = new int[sideLength][sideLength];

        new Decoder(fileScanner).decode(dataArray, size, 0, 0);
        return dataArray;
    }

    public static class Decoder {

        private final Scanner fileScanner;

        public Decoder(Scanner fileScanner) {
            this.fileScanner = fileScanner;
        }

        /**
         * Parse Quad Tree object from a list of tokens.
         * @param tokens integer value tokens
         * @return Constructed quad-tree.
         */
        public static RITQTNode parse(List<Integer> tokens) {
            int token = tokens.remove(0);
            switch (token) {
                case -1:
                    RITQTNode ul = parse(tokens);
                    RITQTNode ur = parse(tokens);
                    RITQTNode ll = parse(tokens);
                    RITQTNode lr = parse(tokens);
                    return new RITQTNode( -1, ul, ur, ll, lr );
                default:
                    return new RITQTNode( token );
            }
        }

        /**
         * Decode raw encoded data to 2D data-array.
         * @param dataArray data array for information to be stored in.
         * @param size size of sub-section.
         * @param startRow starting row coordinate of sub-section.
         * @param startCol starting column coordinate of sub-section.
         */
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
                    //TODO: Create singleton thread pool class and add fill task to its queue for improved performance.
                    fillDataArraySlice(dataArray, startRow, startCol, val, (int) sqrtSizeDividedByTwo);
                }
            }
        }

        private void fillDataArraySlice(int[][] dataArray, int startRow, int startCol, int val, int sqrtSizeDividedByTwo) {
            IntStream.range(startRow, startRow + sqrtSizeDividedByTwo *2 ).forEach(row -> {
                IntStream.range(startCol, startCol + sqrtSizeDividedByTwo *2 ).forEach(col -> {
                    dataArray[row][col] = val;
                });
            });
        }
    }
}