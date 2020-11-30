package ptui;

import model.RITQTCodec;
//import model.exceptions.InvalidImageSpecificationException;

import java.io.*;
import java.util.ArrayList;

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
            writer.close();
            System.out.println("Output written to: " + args[1]);
        } catch (IOException e) {
            System.out.println("Please make sure that desired input and output files / their directories exist.");
            System.out.println(e.toString());
            e.printStackTrace();
        } /*catch (InvalidImageSpecificationException e) {
            System.out.println("The image specification is invalid -- image size is not a power of 2.");
            e.printStackTrace();
        }*/

//        new RITQTCodec().encodeToSystemOut();
    }

}
