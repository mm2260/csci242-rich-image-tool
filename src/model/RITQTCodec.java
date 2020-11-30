package model;

import model.RITQuadTree.DataArray;

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

    /**
     * Open a .txt file.
     * @param fileName File name (XYZ.txt)
     * @return Quad Tree representation of file data.
     * @throws FileNotFoundException
     */
    public static RITQuadTree importFile( String fileName ) throws FileNotFoundException {
        DataArray imageData = FileHandler.getDataArray(fileName);
        Decoder decoder = new Decoder(imageData);
        return decoder.decode();
    }

    /**
     * Open a .rit file.
     * @param fileName File Name (XYZ.rit)
     * @return Quad Tree representation of uncompressed file data.
     * @throws FileNotFoundException
     */
    public static RITQuadTree openFile( String fileName ) throws FileNotFoundException {
        Decoder decoder = new Decoder(FileHandler.getInputStream(fileName));
        return decoder.decode();
    }

    /*
    *   Decoder: [ File       ==>  QuadTree ] : [ un-compress ]
    *   Encoder: [ QuadTree   ==>  File     ] : [  compress   ]
    */

    public static class Encoder {

    }

    public static class Decoder {

        private final Scanner fileScanner;
        private final DataArray dataArray;
        private final List<Integer> accumulator;
        private final int initialSize;

        /**
         * Constructor for the decoder. Un-compresses dataArray from a compressed inputStream (.rit file).
         * @param resource input stream to pull data from.
         */
        public Decoder(InputStream resource) {
            this.fileScanner = new Scanner(resource);
            this.accumulator = new ArrayList<>();
            this.dataArray = uncompress();
            this.initialSize = dataArray.getLength();
        }

        /**
         * Constructor for the decoder.
         * @param rawData
         */
        public Decoder(DataArray rawData ) {
            this.fileScanner = null;
            this.accumulator = new ArrayList<>();
            this.dataArray = rawData;
            this.initialSize = dataArray.getLength();
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
         * Un-compress input file into pixel data.
         * @return raw image data.
         */
        private DataArray uncompress() {

            int size = nextValue();
            int sideLength = (int) Math.sqrt(size);

            DataArray dataArray = new DataArray( Arrays.asList( new Integer[sideLength*sideLength] ) );
            // equivalent to int[][] dataArray = new int[sideLength][sideLength]

            dataArray.unlock();
            decode(dataArray, size, 0,0);
            dataArray.lock();
            return dataArray;
        }

        /**
         * Succeeding uncompress. Convert the raw image data into its corresponding quad-tree representation.
         * @return final Quad Tree representation of a compressed file.
         */
        public RITQuadTree decode() {
            RITQTNode root = decode(initialSize, 0,0);
            return new RITQuadTree(root, this.dataArray, accumulator);
        }

        /**
         * Construct the Quad Tree using a recursive divide-and-conquer algorithm.
         * @param size size of image slice.
         * @param startRow starting row of image sub-section.
         * @param startCol starting column of image sub-section.
         * @return Quad Tree root node.
         */
        private RITQTNode decode(int size, int startRow, int startCol) {

            if(size==1) {
                return new RITQTNode( dataArray.get(startRow, startCol) );
            }

            int sqrtSizeDividedByTwo = (int) Math.sqrt(size)/2;
            //ul:
            RITQTNode ul = decode(size/4, startRow, startCol);
            //ur:
            RITQTNode ur = decode(size/4, startRow, startCol + sqrtSizeDividedByTwo);
            //ll:
            RITQTNode ll = decode(size/4, startRow + sqrtSizeDividedByTwo, startCol);
            //lr:
            RITQTNode lr = decode(size/4, startRow + sqrtSizeDividedByTwo, startCol + sqrtSizeDividedByTwo);

            if( ul.getVal() == ur.getVal() && ur.getVal()==ll.getVal() && ll.getVal()==lr.getVal() && ul.getVal()!=-1 ) {
                return new RITQTNode(ll.getVal());
            } else {
                return new RITQTNode(-1, ul, ur, ll, lr);
            }
        }

        /**
         * Decode raw encoded data to 2D data-array.
         * @param dataArray data array for information to be stored in.
         * @param size size of sub-section.
         * @param startRow starting row coordinate of sub-section.
         * @param startCol starting column coordinate of sub-section.
         */
        private void decode(DataArray dataArray, int size, int startRow, int startCol) {
            int val = nextValue();

            if(size==1) {
                dataArray.set(startRow,startCol, val);
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

        /**
         * Fill value in image sub-section.
         * @param dataArray raw image data.
         * @param startRow starting row coordinate of sub-section
         * @param startCol starting column coordinate of sub-section.
         * @param val value to be filled into sub-section.
         * @param sqrtSizeDividedByTwo side-length of sub-section.
         */
        private void fillDataArraySlice(DataArray dataArray, int startRow, int startCol, int val, int sqrtSizeDividedByTwo) {
            IntStream.range(startRow, startRow + sqrtSizeDividedByTwo *2 ).forEach(row -> {
                IntStream.range(startCol, startCol + sqrtSizeDividedByTwo *2 ).forEach(col -> {
                    dataArray.set(row, col, val);
                });
            });
        }

        /**
         * Get next value from the scanner.
         * @return next value in file.
         */
        private int nextValue() {
            return Integer.parseInt( this.fileScanner.nextLine() );
        }
    }

    private static class FileHandler {

        public static DataArray getDataArray(String filename) throws FileNotFoundException {
            Scanner fileScanner = new Scanner( getInputStream(filename) );
            List<Integer> rawData = new ArrayList<>();
            while(fileScanner.hasNext()) { rawData.add( nextValue(fileScanner) ); }
            return new DataArray(rawData);
        }

        private static int nextValue(Scanner fileScanner) {
            return Integer.parseInt( fileScanner.nextLine() );
        }

        public static InputStream getInputStream(String filename) throws FileNotFoundException {
            return new FileInputStream(filename);
        }

    }

}