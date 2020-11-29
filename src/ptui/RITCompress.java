package ptui;

import model.RITQTCodec;
import model.RITQTNode;
import model.exceptions.InvalidImageSpecificationException;

import java.io.*;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TODO: add ptui compress documentation
public class RITCompress {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITCompress uncompressed-file.txt compressed-file.rit");
            //TODO
            return;
        }
        try {

            //Loading files and such may throw IO exceptions which are handled below.
            InputStream inputFile = new FileInputStream(args[0]);
            BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
            Scanner sc = new Scanner(new File(args[0]));
            System.out.println("Compressing: " + args[0]);

            RITQTCodec codec = new RITQTCodec();
            ArrayList<Integer> dataArray = new ArrayList<>();
            //Codec takes InputStream as parameter and returns raw pixel data as a 2D-Array.
            while (sc.hasNextInt()) {
                dataArray.add(sc.nextInt());
            }
            RITQTCodec.Encoder encoder = new RITQTCodec.Encoder(dataArray);
            RITQTNode root = encoder.encode();

            ArrayList<Integer> compressData = new ArrayList<>();
            preorder(root, compressData);

            System.out.print("QTree:");


            for (int data : compressData) {
                System.out.print(" " + data);
                writer.append(String.valueOf(data));
                writer.newLine();
            }
            compressData.add(0,dataArray.size());
            System.out.println("\nOutput file: " + args[1]);
            System.out.println("Raw image size: " + dataArray.size());
            System.out.println("Compressed Image size: " + compressData.size());
            System.out.println("Compression%: " + ((double)( dataArray.size()-compressData.size())/dataArray.size())*100);

        } catch (IOException e) {
            System.out.println("Please make sure that desired input and output files / their directories exist.");
            System.out.println(e.toString());
            e.printStackTrace();
        } catch (InvalidImageSpecificationException e) {
            System.out.println("The image specification is invalid -- image size is not a power of 2.");
            e.printStackTrace();
        }

        new RITQTCodec().encodeToSystemOut();
    }


    private static ArrayList<Integer> preorder(RITQTNode ritqtNode, ArrayList<Integer> data){
        if(ritqtNode!=null){
            data.add(ritqtNode.getVal());
            preorder(ritqtNode.getUpperLeft(),data);
            preorder(ritqtNode.getUpperRight(),data);
            preorder(ritqtNode.getLowerLeft(),data);
            preorder(ritqtNode.getLowerRight(),data);
        }
        return data;
    }

}
