package LLParser;

import lftc.edu.util.Tuple;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private List<String> nonterminals = new ArrayList<>();
    private List<String> terminals = new ArrayList<>();
    private Map<String, List<String>> grammar = new HashMap<>();
    private List<Tuple<String, String>> listOfRules = new ArrayList<>();
    private Map<String, List<String>> firstOne = new HashMap<>();
    private Map<String, List<String>> followOne = new HashMap<>();
    private Map<Tuple<String, String>, String> parsingTable = new HashMap<>();
    private TreeNode root;
    private String filename;

    public Parser(String filename) throws FileNotFoundException {
        this.filename = filename;
        readGrammar();
    }

    private void readGrammar() throws FileNotFoundException {
        Scanner s = new Scanner(new File(filename));
        String[] line;

        // read the list of nonterminals
        line = s.nextLine().split(" ");
        Collections.addAll(nonterminals, line);

        // read the list pf terminals
        line = s.nextLine().split(" ");
        Collections.addAll(terminals, line);

        while(s.hasNextLine()){
            line = s.nextLine().split("->");
            if(grammar.get(line[0]) == null){
                List<String> rule = new ArrayList<>();
                rule.add(line[1]);
                grammar.put(line[0], rule);
                listOfRules.add(new Tuple<>(line[0], line[1]));
            } else {
                grammar.get(line[0]).add(line[1]);
                listOfRules.add(new Tuple<>(line[0], line[1]));
            }
        }

//        System.out.println("nonterminals:" + nonterminals);
//        System.out.println("terminals:" + terminals);
//        grammar.keySet().forEach(key -> System.out.println("key: " + key + " values "+ grammar.get(key)));
//        listOfRules.forEach(System.out::println);

    }

    public void findFirstAndFollow(){
//        System.out.println("First 1");

        for (String nonterminal : nonterminals) {
            firstOne.put(nonterminal, findFirst(nonterminal));
        }

//        for (String key: firstOne.keySet()) {
//            System.out.println("key: " + key + " values: " + firstOne.get(key));
//        }
//
//        System.out.println("Follow 1");

        for (String nonterminal : nonterminals) {
            followOne.put(nonterminal, findFollow(nonterminal));
        }

//        for (String key: followOne.keySet()) {
//            System.out.println("key: " + key + " values: " + followOne.get(key));
//        }

        constructParsingTable();

//        parsingTable.forEach((tuple, s) -> System.out.println(tuple + " value: " + s));

        System.out.println(parseSequence("acd"));

        System.out.println(root.toString());

    }

    public List<String> findFirst(String nonterminal){
        int found = 0;
        String string = "";
        List<String> temp = new ArrayList<>();
        for (String rule : grammar.get(nonterminal)) {
//            System.out.println("nonterminal: " + nonterminal);
//            System.out.println("rule: " + rule);
            for (int k=0;k<rule.length(); k++, found = 0){ // length of the rule
                if (nonterminals.contains(String.valueOf(rule.charAt(k)))){
                    List<String> returnedResult = findFirst(String.valueOf(rule.charAt(k)));
//                    System.out.println("returned: " + returnedResult);
//                    System.out.println("for");
                    for (String str: returnedResult) {
                        if(!(str.charAt(0)=='?')) { //when epsilon production is the only nonterminal production
                            temp.add(str);
//                            System.out.println("temp1: " + temp);
                            string = str;
                            found = 1;
                        } else {
                            int index = k+1;
                            returnedResult = findFirst(String.valueOf(rule.charAt(index)));
                            while(returnedResult.contains("?")){
                                index++;
                                temp.addAll(returnedResult);
                                temp.remove("?");
                                returnedResult = findFirst(String.valueOf(rule.charAt(index)));
                            }
                            temp.addAll(returnedResult);
                        }
                    }
                }
                if(found==1) {
                    if(string.contains("?")) //here epsilon will lead to next nonterminal’s first set
                        continue;
                }
                else {
                    //if first set includes terminal
                    temp.add(String.valueOf(rule.charAt(k)));
//                    System.out.println("temp2: " + temp);
                }
                break;
            }
        }
        return temp;
    }

    private List<String> findFollow(String nonterminal){
        int found = 0;
        List<String> temp = new ArrayList<>();
        if(nonterminal.equals("S")){
            temp.add("$");
        }
        for (String ntm : nonterminals){
            for (String rule : grammar.get(ntm)) {
                for (int k=0;k<rule.length(); k++){
                    if(String.valueOf(rule.charAt(k)).equals(nonterminal)){
                        if(k == rule.length() -1){//if it is the last terminal/non-terminal then follow of current non-terminal
                            if(followOne.get(ntm) != null){
                                temp.addAll(followOne.get(ntm));
                            }
                        } else {
                            for (String ntm2 : nonterminals) {
                                if (String.valueOf(rule.charAt(k+1)).equals(ntm2)){
                                    List<String> firsts = firstOne.get(ntm2);
                                    for (String first : firsts) {
                                        if (first.equals("?")){
                                            if(k+1==rule.charAt(rule.length()-1))
                                                temp.addAll(findFollow(ntm));
                                            else
                                                temp.addAll(findFollow(ntm2));
                                        }

                                        if(!first.equals("?")){
                                            temp.add(first);
                                        }
                                    }
                                    found = 1;
                                }
                            }

                            if(found!=1)
                                temp.add(String.valueOf(rule.charAt(k+1)));
                        }
                    }
                }

            }
        }
        return temp.stream().distinct().collect(Collectors.toList());
    }

    public void constructParsingTable(){
        for (String nonterminal : nonterminals) {
            for (String first: firstOne.get(nonterminal)) {
//                System.out.println("first hehe: " + first);
                if (first.charAt(0) == '?'){
//                    System.out.println("HEHHEE");
                    for (String follow : followOne.get(nonterminal)) {
//                        System.out.println(follow);
                        List<String> r = listOfRules.stream()
                                .filter(tuple -> tuple.getVal1().equals(nonterminal) && tuple.getVal2().equals("?"))
                                .map(Tuple::getVal2)
                                .collect(Collectors.toList());
//                        System.out.println("HOHOHO");
//                        System.out.println(r);
                        parsingTable.put(new Tuple<>(nonterminal, "$"), r.get(0));
                        parsingTable.put(new Tuple<>(nonterminal, follow), r.get(0));
                    }
                } else {
                    List<String> rules = listOfRules.stream()
                            .filter(stringTuple -> stringTuple.getVal1().equals(nonterminal) && !stringTuple.getVal2().equals("?"))
                            .map(Tuple::getVal2)
                            .collect(Collectors.toList());
                    if (rules.size() > 1) {
                        rules = rules.stream()
                                .filter(rule -> rule.charAt(0) == first.charAt(0))
                                .collect(Collectors.toList());
                        if (!rules.isEmpty()) {
                            parsingTable.put(new Tuple<>(nonterminal, first), rules.get(0));
                        }
                    } else {
                        parsingTable.put(new Tuple<>(nonterminal, first), rules.get(0));
                    }
                }
            }
        }
//        for (String terminal : terminals) {
//            parsingTable.put(new Tuple<>(terminal, terminal), "pop");
//        }
//        parsingTable.put(new Tuple<>("$", "$"), "acc");
    }

    public boolean parseSequence(String input){
        int index = 0;
        input.concat("$");
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push("S");
        String head = stack.pop();
        root = new TreeNode();
        root.setValue(head);
        List<TreeNode> currentLeaves = new ArrayList<>();
        currentLeaves.add(root);
        List<TreeNode> nodeToAddTo = new ArrayList<>();
        while(true){
//            System.out.println("stack");
//            System.out.println(stack);
//            System.out.println("head " + head);
            if(head.equals("$") && stack.empty()){
                return true; // successful, return true, it's accepted
            }

            String rule = null;

            if(nonterminals.contains(head)){
                if( index < input.length()) {
                    rule = parsingTable.get(new Tuple<>(head, String.valueOf(input.charAt(index))));
                }
//                System.out.println(rule);
                if(rule == null){
                    rule = parsingTable.get(new Tuple<>(head, "$"));
                }
//                System.out.println("second try");
//                System.out.println(rule);
                if(rule == null){
                    throw new IllegalArgumentException("wrong string");
                }
                if(!rule.equals("?")){
//                    System.out.println("hey");
                    for (int i = rule.length()-1; i>=0; i--){ // push the rule in the inverse order on the stack
                        String value = String.valueOf(rule.charAt(i));
                        stack.push(value);
//                        System.out.println("stack: " + stack);
                        TreeNode node = new TreeNode();
                        node.setValue(value);

                        String finalHead = head;

                        nodeToAddTo = currentLeaves.stream()
                                .filter(treeNode -> treeNode.getValue().equals(finalHead))
                                .collect(Collectors.toList());
//                        System.out.println("Node");

                        if (!nodeToAddTo.isEmpty()){
                            nodeToAddTo.get(0).getLeaves().add(node);
//                            System.out.println(nodeToAddTo.get(0));
                            node.setRoot(nodeToAddTo.get(0));
                        }

                        if(nonterminals.contains(value)){
                            currentLeaves.add(node);
                        }
                    }
                    if(!nodeToAddTo.isEmpty()) {
                        currentLeaves.remove(nodeToAddTo.get(0));
                    }
                }
            }
            if( terminals.contains(head)){
                index++;
            }

            if(stack.empty()){
                break;
            }
            head = stack.pop();
        }
        return false;
    }

}
