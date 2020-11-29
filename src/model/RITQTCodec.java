package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

//TODO: Add codec documentation.
public class RITQTCodec {

    public int[][] decodeToArray( InputStream resource ) {
        Scanner fileScanner = new Scanner(resource);

        //TODO: Move data loading responsibility to separate class.
        int size = fileScanner.nextInt();
        int sideLength = (int) Math.sqrt(size);
        int[][] dataArray = new int[sideLength][sideLength];

        new Decoder(fileScanner).decode(dataArray, size, 0, 0);
        return dataArray;
    }

    public void encodeToSystemOut( ) {
                /* 2x2 base-case:

               TEST-1           TEST-2             TEST-3
               _______________  _________________    ____________
               | 0   |   33  |  | 255   |   255 |   | 0   |  0  |
               |-----|-------|  |------|--------|   |-----|-----|
               | 66  |   255 |  | 255  |   255  |   | 0  |   0  |
               --------------   -----------------   ------------
               (-1,0,33,66,255)     (255)               (0)

         */

//        try {
//            Scanner fileScanner = new Scanner(new FileInputStream(""));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        new Encoder(new ArrayList<>(Arrays.asList(
                255, 255, 255, 255,
                120, 120, 120, 120,
                60, 60, 60, 60,
                0, 0, 0, 0)));
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

    public static class Encoder {

        private DataArray dataArray;
        private List<Integer> accumulator;
        private int initialSize;

        public Encoder(List<Integer> rawData) {
            this.dataArray = new DataArray(rawData);
            this.accumulator = new ArrayList<>();
            this.initialSize = rawData.size();
        }

        public RITQTNode encode() {
            RITQTNode root = encode(initialSize, 0,0);
            System.out.println(accumulator);
            return root;
        }

        private RITQTNode encode(int size, int startRow, int startCol) {

            if(size==1) {
                return new RITQTNode( dataArray.get(startRow, startCol) );
            }

            int sqrtSizeDividedByTwo = (int) Math.sqrt(size)/2;
            //ul:
            RITQTNode ul = encode(size/4, startRow, startCol);
            //ur:
            RITQTNode ur = encode(size/4, startRow, startCol + sqrtSizeDividedByTwo);
            //ll:
            RITQTNode ll = encode(size/4, startRow + sqrtSizeDividedByTwo, startCol);
            //lr:
            RITQTNode lr = encode(size/4, startRow + sqrtSizeDividedByTwo, startCol + sqrtSizeDividedByTwo);

            if( ul.getVal() == ur.getVal() && ur.getVal()==ll.getVal() && ll.getVal()==lr.getVal() ) {
                return new RITQTNode(ll.getVal());
            } else {
                return new RITQTNode(-1, ul, ur, ll, lr);
            }
        }

        class DataArray {
            List<Integer> data;
            private int size;

            public DataArray(List<Integer> data) {
                this.data = data;
                this.size = (int) Math.sqrt(data.size());
            }

            public int get(int row, int col) {
                return data.get( row*size+col );
            }
        }
    }
}