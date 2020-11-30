package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RITQuadTree {

    RITQTNode root;
    DataArray imageData;
    List<Integer> representation;

    RITQuadTree(RITQTNode root, DataArray imageData, List<Integer> representation) {
        this.root = root;
        this.imageData = imageData;
        this.representation = representation;
    }

    public RITQTNode getRoot() {
        return this.root;
    }

    @Override
    public String toString() {
        return String.format( "SIZE: %d, REPR: %s",this.imageData.size, this.representation );
    }

    public static class DataArray {
        List<Integer> data;
        private final int size;
        private final int length;

        //TODO: implement locking mechanism.
        private boolean locked = true;

        public DataArray(List<Integer> data) {
            this.data = data;
            this.length = data.size();
            this.size = (int) Math.sqrt(data.size());
        }

        public int getLength() { return this.length; }

        public int get(int row, int col) {
            return data.get( row*size+col );
        }

        public void set(int row, int col, int value) {
            data.set( row*size+col, value );
        }

        public List<Integer> copyData() {
            return new ArrayList<>( this.data );
        }

        public void lock() {this.locked=true;}
        public void unlock() {this.locked=false;}
    }
}