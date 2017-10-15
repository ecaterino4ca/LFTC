package lftc.edu.util;

import lombok.Data;

@Data
public class Node {

    private String value;
    private Node left;
    private Node right;
    private int index;

    public Node(String value, int index) {
        this.value = value;
        left = null;
        right = null;
        this.index = index;
    }

}
