package ptui;

import model.RITQTCodec;
import model.RITQuadTree;
import model.util.ExceptionHandler;

import java.io.PrintStream;

public class RITCompress {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITCompress uncompressed-file.txt compressed-file.rit");
            return;
        }

        String inputFilePath, outputFilePath;
        inputFilePath = args[0];
        outputFilePath = args[1];

        try {
            System.out.println("Compressing: "+inputFilePath);
            RITQuadTree quadTree = RITQTCodec.importFile(inputFilePath);
            System.out.println("QTree: "+quadTree.getRepresentation());
            RITQTCodec.exportCompressed(outputFilePath, quadTree);
            System.out.printf("Compressed image size: %d%n", quadTree.getRepresentation().size());
            double rawImageSize = quadTree.getImageData().getLength();
            double compressedSize = quadTree.getRepresentation().size();
            System.out.println("Raw image size: "+rawImageSize);
            System.out.println("Compressed image size: "+compressedSize);
            double compressionRatio = 1 - compressedSize/rawImageSize;
            System.out.printf("Compression %%%.1f",compressionRatio*100);

        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
    }
}