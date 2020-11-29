package model;

import model.exceptions.InvalidImageSpecificationException;
import model.utils.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class RITQTCodec {

    public ArrayList<Integer> encodeToArray(InputStream resource ) {
        //TODO
        Scanner fileScanner = new Scanner(resource);
        ArrayList<Integer> dataArray = new ArrayList<>();

        while (fileScanner.hasNext()){
            dataArray.add(fileScanner.nextInt());
        }

        int size = dataArray.size();
        int side = (int) Math.sqrt(size);


        int[][] twoDArray = new int[side][side];

        for(int i=0; i<side; i++){
            for(int j=0; j<side;j++){
                twoDArray[i][j] = dataArray.get(i*side+j);
            }
        }

        RITQTNode root = compress(side,0,0,twoDArray);
        ArrayList<Integer> compressArray = new ArrayList<>();
        compressArray.add(size);
        preorder(root,compressArray);
        return compressArray;
    }

    private RITQTNode parse(List<Integer> tokens) {
        int val = tokens.remove(0);
        if (val != -1) {
            return new RITQTNode(val);
        } else {
            RITQTNode ul = parse(tokens);
            RITQTNode ur = parse(tokens);
            RITQTNode ll = parse(tokens);
            RITQTNode lr = parse(tokens);
            return new RITQTNode(val, ul, ur, ll, lr);
        }
    }



    private ArrayList<Integer> preorder(RITQTNode ritqtNode, ArrayList<Integer> data){
        if(ritqtNode!=null){
            data.add(ritqtNode.getVal());
            preorder(ritqtNode.getUpperLeft(),data);
            preorder(ritqtNode.getUpperRight(),data);
            preorder(ritqtNode.getLowerLeft(),data);
            preorder(ritqtNode.getLowerRight(),data);
        }
        return data;
    }

    public RITQTNode compress(int side, int startRow, int startCol, int[][] dataArray){
        if(side==1){
            return new RITQTNode(dataArray[startRow][startCol]);
        }else{
            int fixedOffset=side/2;
            RITQTNode ul;
            RITQTNode ur;
            RITQTNode ll;
            RITQTNode lr;
            if(checkIfEqual(fixedOffset,startRow,startCol,dataArray)){
                 ul = new RITQTNode(dataArray[startRow][startCol]);
            }else {
                int offset=fixedOffset/2;
                ul = new RITQTNode(-1,
                        compress(offset,startRow,startCol,dataArray),
                        compress(offset,startRow,startCol+offset,dataArray),
                        compress(offset,startRow+offset,startCol,dataArray),
                        compress(offset,startRow+offset,startCol+offset,dataArray));
            }
            if(checkIfEqual(fixedOffset,startRow,startCol+fixedOffset,dataArray)) {
                ur = new RITQTNode(dataArray[startRow][startCol+fixedOffset]);
            }else {
                int offset=fixedOffset/2;
                ur = new RITQTNode(-1,
                        compress(offset,startRow,startCol+fixedOffset,dataArray),
                        compress(offset,startRow,startCol+fixedOffset+offset,dataArray),
                        compress(offset,startRow+offset,startCol+fixedOffset,dataArray),
                        compress(offset,startRow+offset,startCol+offset+fixedOffset,dataArray));
            }
            if(checkIfEqual(fixedOffset,startRow+fixedOffset,startCol,dataArray)){
                ll = new RITQTNode(dataArray[startRow+fixedOffset][startCol]);
            }else {
                int offset=fixedOffset/2;
                ll = new RITQTNode(-1,
                        compress(offset,startRow+fixedOffset,startCol,dataArray),
                        compress(offset,startRow+fixedOffset,startCol+offset,dataArray),
                        compress(offset,startRow+offset+fixedOffset,startCol,dataArray),
                        compress(offset,startRow+offset+fixedOffset,startCol+offset,dataArray));
            }
            if(checkIfEqual(fixedOffset,startRow+fixedOffset,startCol+fixedOffset,dataArray)){
                lr = new RITQTNode(dataArray[startRow+fixedOffset][startCol+fixedOffset]);
            }else {
                int offset=fixedOffset/2;
                lr = new RITQTNode(-1,
                        compress(offset,startRow+fixedOffset,startCol+fixedOffset,dataArray),
                        compress(offset,startRow+fixedOffset,startCol+offset+fixedOffset,dataArray),
                        compress(offset,startRow+offset+fixedOffset,startCol+fixedOffset,dataArray),
                        compress(offset,startRow+offset+fixedOffset,startCol+offset+fixedOffset,dataArray));
            }

            return new RITQTNode(-1,ul,ur,ll,lr);

        }
    }

    public boolean checkIfEqual(int side, int startRow, int startCol,int[][] dataArray){
        for(int i=0;i<side;i++){
            for(int j=0;j<side;j++){
                if(dataArray[startRow][startCol]!=dataArray[startRow+i][startCol+j]){
                    return false;
                }
            }
        }
        return true;
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
