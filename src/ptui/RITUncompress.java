package ptui;

import model.RITQTCodec;
import model.RITQuadTree;
import model.util.ExceptionHandler;

import java.io.FileNotFoundException;

public class RITUncompress {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }

        String inputFilePath, outputFilePath;
        inputFilePath=args[0];
        outputFilePath=args[1];

        try {
            System.out.println("Uncompressing: "+ inputFilePath);
            RITQuadTree quadTree = RITQTCodec.openFile(inputFilePath);
            System.out.printf("QTree: %s%n", quadTree.getImageData());
            RITQTCodec.exportUncompressed(outputFilePath, quadTree);

        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
    }
}
