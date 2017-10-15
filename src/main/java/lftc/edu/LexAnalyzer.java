package lftc.edu;

import lftc.edu.util.*;
import lombok.Data;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class LexAnalyzer {

    private static final String SPECIAL_SYMBOLS = new String(new char[]{':', ';', '=', '+', '-', '*', '/', '<', '>', ' ',
            '%', '!', '(', ')', '{', '}', '[', ']', '~'});

    private int lineCount; // used for displaying line at which possible error might occur

    private Map<String, Integer> codificationTable; //initialized at the start with values from codificationTable file
    private List<Tuple<Integer, Integer>> pif;
    private SymTable symTable;

    public LexAnalyzer(String codificationFileName) {
        symTable = new LexSortedTable();
        pif = new ArrayList<>();
        lineCount = 0;
        initCodificationTable(codificationFileName);
    }

    private void initCodificationTable(String codificationFileName) {
        try {
            codificationTable = FileUtil.readFileInMap(codificationFileName);
        } catch (IOException e) {
            System.out.println("Error reading file: " + codificationFileName);
        }
    }

    public void parseFile(String fileName) throws IOException, CustomException {
        File file = FileUtil.getFile(fileName);
        StringTokenizer tokenizer;
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                ++lineCount;
                //StringTokenizer is used to split the list by SPECIAL_SYMBOLS
                //thus preserving them into the result
                tokenizer = new StringTokenizer(line, SPECIAL_SYMBOLS, true);
                iterateTokenizer(tokenizer);
            }
        }
    }

    private void iterateTokenizer(StringTokenizer tokenizer) throws CustomException {
        while (tokenizer.hasMoreElements()) {
            String word = (String) tokenizer.nextElement();
            if (word.trim().equals("")) continue;
            Integer posInCodificationTable = codificationTable.get(word);
            if (posInCodificationTable == null) {
                processIdentifiersAndConstants(word);
            } else {
                pif.add(new Tuple<>(posInCodificationTable, -1));
            }
        }
    }

    private void processIdentifiersAndConstants(String word) throws CustomException {
        if (word.length() > 255) {
            throw new CustomException("Error: Input is too large for " + word + " at line " + lineCount);
        }

        if (validateIdentifier(word)) {
            processToken(word, 0);
            return;
        }

        if (validateIntConstant(word) || validateCharConst(word) || validateStringConst(word) || validateRealConst(word)) {
            processToken(word, 1);
            return;
        }
        throw new CustomException("Error: Invalid token " + word + " at line " + lineCount);
    }


    private void processToken(String word, int codification) {
        int i = symTable.find(word);
        if (i == -1) {
            pif.add(new Tuple<>(codification, symTable.size()));
            symTable.put(word);
        } else {
            pif.add(new Tuple<>(codification, i));
        }
    }

    private boolean validateIntConstant(String word) {
        return Pattern.matches("^-?\\d+$", word);
    }

    private boolean validateIdentifier(String word) {
        return Pattern.matches("^[a-zA-Z]([a-zA-Z0-9])*", word);
    }

    private boolean validateStringConst(String word) {
        return Pattern.matches("^\'[a-zA-Z0-9]\'$", word);
    }

    private boolean validateCharConst(String word) {
        return Pattern.matches("^\"[a-zA-Z0-9]*\"$", word);
    }

    private boolean validateRealConst(String word) {
        return Pattern.matches("^-?\\d+,\\d+$", word);
    }

    public void reconstructProgramToFile(String fileName) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))))) {
            for (Tuple<Integer, Integer> tuple : pif) {
                Integer codificationTablePos = tuple.getVal1();
                Integer symTablePos = tuple.getVal2();
                if (symTablePos == -1) {
                    writeWordToFile(writer, getKeyByValue(codificationTablePos).get(0));
                } else {
                    writeWordToFile(writer, symTable.getByIndex(symTablePos));
                }
            }

            writer.write("\n\nSymbol table: " + "\n");
            symTable.display(writer, symTable.getRoot());
            writer.write("\n\nPIF: " + pif.toString());
        }
    }

    private void writeWordToFile(Writer writer, String value) throws IOException {
        if (value.equals(";") || value.equals("{") || value.equals("}") || value.equals("begin") || value.equals("end") || value.equals("var")) {
            writer.write(value + "\n");
        } else {
            writer.write(value + " ");
        }
    }

    private List<String> getKeyByValue(Integer value) {
        return codificationTable.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
