package app.lab34;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileContentReader {

    public static final boolean RECURSIVE = false;

    private static void printContentOfFilesInDirectory(File directory) throws IOException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if(file.isFile()){
                printFile(file);
            } else if(file.isDirectory() && RECURSIVE){
                printContentOfFilesInDirectory(file);
            }
        }
    }

    private static void printFile(File file) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            printReaderContent(reader);
        }finally{
            reader.close();
        }
    }

    private static void printReaderContent(BufferedReader reader) throws IOException {
        String line = null;
        while( (line = reader.readLine()) != null ){
            System.out.println(line);
        }
    }
}
