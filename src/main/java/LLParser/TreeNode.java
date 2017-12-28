package LLParser;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String value;
    private List<TreeNode> leaves = new ArrayList<>();
    private TreeNode root = null;

    public TreeNode(String value, List<TreeNode> leaves, TreeNode root) {
        this.value = value;
        this.leaves = leaves;
        this.root = root;
    }

    public TreeNode() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<TreeNode> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<TreeNode> leaves) {
        this.leaves = leaves;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return
                 value + " " + leaves ;
    }
}
