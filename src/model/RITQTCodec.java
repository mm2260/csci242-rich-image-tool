package model;

import model.RITQuadTree.DataArray;
import model.util.exception.IllegalPixelValueException;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

/**
 *   Decoder: [ Raw Compressed Data ==>       QuadTree       ] : [ un-compress ]
 *   Encoder: [      QuadTree       ==>  Raw Compressed Data ] : [  compress   ]
 */
public class RITQTCodec {

    /**
     * Open a .txt file.
     * @param fileName File name (XYZ.txt)
     * @return Quad Tree representation of file data.
     * @throws FileNotFoundException
     */
    public static RITQuadTree importFile( String fileName ) throws FileNotFoundException {
        DataArray imageData = FileHandler.getDataArray(fileName);
        Decoder decoder = new Decoder(imageData, fileName);
        return Encoder.encode(decoder.decode());
    }

    /**
     * Open a .rit file.
     * @param fileName File Name (XYZ.rit)
     * @return Quad Tree representation of uncompressed file data.
     * @throws FileNotFoundException
     */
    public static RITQuadTree openFile( String fileName ) throws FileNotFoundException {
        Decoder decoder = new Decoder(FileHandler.getInputStream(fileName), fileName);
        return Encoder.encode(decoder.decode());
    }

    public static void exportCompressed( String fileName, RITQuadTree quadTree ) throws IOException {
        export(fileName, quadTree.representation);
    }

    public static void exportUncompressed( String fileName, RITQuadTree quadTree ) throws IOException {
        export(fileName, quadTree.imageData.data);
        System.out.println("Output file: "+fileName);
    }

    private static void export(String fileName, List<Integer> data) throws IOException {
        FileHandler.validateWritePath(fileName);
        BufferedWriter bufferedWriter = FileHandler.getFileWriter(fileName);
        FileHandler.writeLines(bufferedWriter, data);
        bufferedWriter.close();
    }

    public static class Encoder {

        public static RITQuadTree encode(RITQuadTree quadTree) {
            quadTree.getRepresentation().add((int) Math.pow(quadTree.imageData.getSize(),2));
            generateRepresenation(quadTree.root, quadTree.getRepresentation());
            return quadTree;
        }

        private static void generateRepresenation(RITQTNode root, List<Integer> accumulator) {
            if(root.getLowerLeft()==root.getLowerRight() &&
                    root.getLowerRight()==root.getUpperLeft() &&
                    root.getUpperLeft()==root.getUpperRight() &&
                    root.getUpperRight()== null) {

                accumulator.add(root.getVal());
            }
            else {
                accumulator.add(-1);
                generateRepresenation( root.getUpperLeft(),accumulator  );
                generateRepresenation( root.getUpperRight(),accumulator );
                generateRepresenation( root.getLowerLeft(),accumulator  );
                generateRepresenation( root.getLowerRight(),accumulator );
            }
        }
    }

    public static class Decoder {

        private final Scanner fileScanner;
        private final DataArray dataArray;
        private final int initialSize;
        private final String source;

        /**
         * Constructor for the decoder. Un-compresses dataArray from a compressed inputStream (.rit file).
         * @param resource input stream to pull data from.
         */
        public Decoder(InputStream resource, String source) {
            this.fileScanner = new Scanner(resource);
            this.dataArray = uncompress();
            this.initialSize = dataArray.getLength();
            this.source = source;
        }

        /**
         * Constructor for the decoder.
         * @param rawData
         */
        public Decoder(DataArray rawData, String source ) {
            this.fileScanner = null;
            this.dataArray = rawData;
            this.initialSize = dataArray.getLength();
            this.source = source;
        }

        /**
         * Parse Quad Tree object from a list of tokens.
         * @param tokens integer value tokens
         * @return Constructed quad-tree.
         */
        public static RITQTNode parse(List<Integer> tokens) {
            int token = tokens.remove(0);
            if (token == -1) {
                RITQTNode ul = parse(tokens);
                RITQTNode ur = parse(tokens);
                RITQTNode ll = parse(tokens);
                RITQTNode lr = parse(tokens);
                return new RITQTNode(-1, ul, ur, ll, lr);
            }
            return new RITQTNode(token);
        }

        /**
         * Un-compress input file into pixel data.
         * @return raw image data.
         */
        private DataArray uncompress() {

            int size = FileHandler.nextValue(this.fileScanner, source);
            int sideLength = (int) Math.sqrt(size);

            DataArray dataArray = new DataArray( Arrays.asList( new Integer[sideLength*sideLength] ), source );
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
            return new RITQuadTree(root, this.dataArray);
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
            int val = FileHandler.nextValue(this.fileScanner, source);

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

//        /**
//         * Get next value from the scanner.
//         * @return next value in file.
//         */

    }

    private static class FileHandler {

        public static DataArray getDataArray(String filename) throws FileNotFoundException {
            Scanner fileScanner = new Scanner( getInputStream(filename) );
            List<Integer> rawData = new ArrayList<>();
            while(fileScanner.hasNext()) { rawData.add( nextValue(fileScanner, filename) ); }
            return new DataArray(rawData, filename);
        }

        public static InputStream getInputStream(String filename) throws FileNotFoundException {
            return new FileInputStream(filename);
        }

        public static BufferedWriter getFileWriter(String filename) throws IOException {
            return new BufferedWriter( new FileWriter(filename) );
        }

        public static void writeLines(BufferedWriter bufferedWriter, List<Integer> representation) throws IOException {
            for(int i = 0; i<representation.size()-1;++i ) {
                bufferedWriter.write( String.valueOf(representation.get(i)) );
                bufferedWriter.newLine();
            }
            bufferedWriter.write( String.valueOf( representation.get( representation.size()-1 ) ) );
        }

        private static int nextValue(Scanner fileScanner, String source) {

            String rawValue = fileScanner.nextLine();
            boolean isNumeric = rawValue.chars().allMatch( Character::isDigit );
            if (!isNumeric) {
                throw new IllegalPixelValueException(rawValue, source);
            }

            int integerValue = Integer.parseInt( rawValue );
            boolean outOfBounds = ( integerValue < 0 ) || ( 255 < integerValue );

            if (outOfBounds) {
                throw new IllegalPixelValueException(rawValue, source);
            }

            return Integer.parseInt( rawValue );
        }

        public static void validateWritePath(String fileName) throws IOException {
            File file = new File(fileName);
            if (file.exists()) {
                return;
            } else {
                File parentDirectory = new File(file.getParent());
                if(parentDirectory.exists()) {
                    file.createNewFile();
                }
            }
        }
    }

}