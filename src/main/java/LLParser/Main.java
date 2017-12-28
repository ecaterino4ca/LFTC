package LLParser;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Parser parser = new Parser("src/main/resources/gramatica.txt");
        parser.findFirstAndFollow();
    }
}
