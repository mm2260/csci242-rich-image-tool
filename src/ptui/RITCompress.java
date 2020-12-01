package ptui;

import model.RITQTCodec;
import model.RITQuadTree;
import model.util.ExceptionHandler;

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
            RITQuadTree quadTree = RITQTCodec.openFile(inputFilePath);
            RITQTCodec.exportCompressed(outputFilePath, quadTree);

        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
    }
}