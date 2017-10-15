package lftc.edu.util;

import java.io.IOException;
import java.io.Writer;

public interface SymTable {

    Node getRoot();

    void put(String value);

    int find(String value);

    String getByIndex(int index);

    int size();

    void display(Writer writer, Node root) throws IOException;

}
