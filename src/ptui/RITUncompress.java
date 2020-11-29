package ptui;

import model.RITQTCodec;
import model.exceptions.InvalidImageSpecificationException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//TODO: add ptui uncompress documentation
public class RITUncompress {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }
        try {

            //Loading files and such may throw IO exceptions which are handled below.
            InputStream inputFile = new FileInputStream(args[0]);
            Scanner scanner = new Scanner(new File(args[0]));
            ArrayList<Integer> input = new ArrayList<>();
            while(scanner.hasNextInt()){
                input.add(scanner.nextInt());
            }

            System.out.println("Uncompressing: " + args[0]);

            RITQTCodec.Decoder decoder = new RITQTCodec.Decoder(scanner);

            BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));




            //Codec takes InputStream as parameter and returns raw pixel data as a 2D-Array.
            int[][] dataArray = decoder.decodeToArray(  //this function throws an exception if the image size is
                    // not a power of two.
                    inputFile);

            System.out.print("QTree:");
            for(int i=1; i<input.size();i++){
                System.out.print(" " + input.get(i));
            }
            for (int[] row : dataArray) {
                for (int value : row) {
                    writer.append(String.valueOf(value));
                    writer.newLine();
                }
            }
            System.out.println("\nOutput file: " + args[1]);
            writer.close();


        } catch (IOException e) {
            System.out.println("Please make sure that desired input and output files / their directories exist.");
            System.out.println(e.toString());
            e.printStackTrace();
        } catch (InvalidImageSpecificationException e) {
            System.out.println("The image specification is invalid -- image size is not a power of 2.");
            e.printStackTrace();
        }

    }
}
