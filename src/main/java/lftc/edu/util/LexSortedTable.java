package lftc.edu.util;

import lombok.Data;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * We use lexicographically sorted binary tree as implementation
 */
@Data
public class LexSortedTable implements SymTable {

    private static Node root;
    private int nextIndex = 0;

    @Override
    public Node getRoot() {
        return root;
    }

    /**
     * Insert the element into the tree preserving lexicographical order of elements
     *
     * @param value value to be inserted
     */
    @Override
    public void put(String value) {
        Node newNode = new Node(value, nextIndex);
        nextIndex++;
        //in case the  tree is empty
        if (root == null) {
            root = newNode;
            return;
        }
        Node current = root;
        Node parent = null;
        while (true) {
            parent = current;
            if (value.compareTo(current.getValue()) < 0) {
                current = current.getLeft();
                //insert the node to the left if it is smaller than the current node and the left node is null
                if (current == null) {
                    parent.setLeft(newNode);
                    return;
                }
            } else {
                //insert the node to the right if value is greater
                current = current.getRight();
                if (current == null) {
                    parent.setRight(newNode);
                    return;
                }
            }
        }
    }

    /**
     * Find the element in the tree, based on lexicographical order of elements
     *
     * @param value element to lookup
     * @return index of node if present, otherwise -1
     */
    @Override
    public int find(String value) {
        Node current = root;
        while (current != null) {
            String currentNodeValue = current.getValue();
            if (currentNodeValue.equals(value)) {
                return current.getIndex();
            } else if (currentNodeValue.compareTo(value) > 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return -1;
    }

    /**
     * Bfs algorithm for parsing the tree and finding the value
     *
     * @param index to find
     * @return value at corresponding index
     */
    @Override
    public String getByIndex(int index) {
        Queue<Node> q = new LinkedList<>();
        if (root == null)
            return "";
        q.add(root);
        while (!q.isEmpty()) {
            Node n = q.remove();
            if (n.getIndex() == index) {
                return n.getValue();
            }
            if (n.getLeft() != null) {
                q.add(n.getLeft());
            }
            if (n.getRight() != null)
                q.add(n.getRight());
        }
        return "";
    }

    @Override
    public int size() {
        return nextIndex;
    }

    @Override
    public void display(Writer writer, Node root) throws IOException {
        if (root != null) {
            writer.write(root.getIndex() + " " + root.getValue() + "\n");
            display(writer, root.getLeft());
            display(writer, root.getRight());
        }
    }

}

