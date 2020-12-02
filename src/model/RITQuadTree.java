package model;

import model.util.exception.IllegalImageSpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class around the quad tree nodes, raw image data, and pre-order representation.
 */
public class RITQuadTree {

    /**
     * Quadtree root node.
     */
    RITQTNode root;
    /**
     * Raw image data represented by quadtree.
     */
    DataArray imageData;
    /**
     * Preorder traversal of the quadtree.
     */
    List<Integer> representation;

    /**
     * QuadTree constructor
     * @param root root node of quadtree
     * @param imageData raw image data
     */
    public RITQuadTree(RITQTNode root, DataArray imageData) {
        this.root = root;
        this.imageData = imageData;
        this.representation = new ArrayList<>();
    }

    /**
     * get root node of quadtree.
     * @return quadtree root.
     */
    public RITQTNode getRoot() { return this.root; }

    /**
     * Get the raw image data represented by quadtree.
     * @return image data.
     */
    public DataArray getImageData() { return this.imageData; }

    /**
     * Get the Pre-Order Representation of QuadTree.
     * @return preorder traversal of quadtree.
     */
    public List<Integer> getRepresentation() { return this.representation; }

    @Override
    public String toString() {
        return String.format( "SIZE: %d, REPR: %s",this.imageData.size, this.representation );
    }

    /**
     * Wrapper class around list of integers. Allows for 2D access.
     */
    public static class DataArray {
        /**
         * internal data representation.
         */
        List<Integer> data;
        /**
         * Side-length of image represented by data-array.
         */
        private final int size;
        /**
         * Length of the 1D repr.
         */
        private final int length;

        /**
         * Prevent unwanted writes.
         */
        private boolean locked = true;

        /**
         * Construct a Data Array from raw image data.
         * @param data raw image data.
         * @param source source of the raaw image data.
         */
        public DataArray(List<Integer> data, String source) {
            this.data = data;
            this.length = data.size();

            //
            boolean isSquare = ( ( length!=0 ) && ( ( length & (length-1) ) == 0 ) );
            if(!isSquare) {
                throw new IllegalImageSpecification(this.length, source);
            }

            this.size = (int) Math.sqrt(data.size());
        }

        /**
         * Get length of internal data representation {One Dimensional List}.
         * @return
         */
        public int getLength() { return this.length; }

        /**
         * get the side-length of image represented by data array.
         * @return side-length of image.
         */
        public int getSize() {return this.size;}

        /**
         * Get a value from the data array's internal data representation.
         * @param row row index.
         * @param col column index.
         * @return  get value at row and column indices.
         */
        public int get(int row, int col) {
            return data.get( row*size+col );
        }

        /**
         * Set value inside of the data array's internal data representation.
         * @param row   row index.
         * @param col   column index.
         * @param value value to be set.
         */
        public void set(int row, int col, int value) {
            if(this.locked) {
                System.err.println("Data Array Locked. Illegal Access.");
                return;
            }
            data.set( row*size+col, value );
        }

        /**
         * Lock DataArray against changes
         */
        public void lock() {this.locked=true;}

        /**
         *  Unlock DataArray for changes.
         */
        public void unlock() {this.locked=false;}

        @Override
        public String toString() { return String.format("%s", this.data); }
    }
}