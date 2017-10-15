package lftc.edu;

import lftc.edu.util.CustomException;

import java.io.IOException;

public class Main {

    private static final String CODIFICATION_FILE = "codificationTable.txt";
    private static final String FILE_TO_PARSE = "sourceCode.txt";
    private static final String OUTPUT_FILE = "src/main/resources/out.txt";

    public static void main(String[] args) {
        try {
            LexAnalyzer lexAnalyzer = new LexAnalyzer(CODIFICATION_FILE);
            // read the source code and parste it
            // constructing pif and st
            lexAnalyzer.parseFile(FILE_TO_PARSE);
            // reconstruct the code from obtained pif
            // to ensure validity of parsing algorithm
            lexAnalyzer.reconstructProgramToFile(OUTPUT_FILE);
        } catch (CustomException e) {
            System.out.println(e.getMsg());
        } catch (IOException e) {
            System.out.println("Error while reading files");
        }
    }
}
