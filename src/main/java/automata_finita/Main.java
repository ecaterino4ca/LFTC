package automata_finita;

import java.io.*;
import java.util.*;

public class Main {

    static AutomataFinita automataFinita;

    public static void main(String[] args) {

        int option=-1;
        while(option!=0) {
            System.out.println("Options: ");
            System.out.println("   1. Insert the AF");
            System.out.println("   2. Read from file AF");
            System.out.println("   3. Print the list of states");
            System.out.println("   4. Print the alphabet ");
            System.out.println("   5. Print the list of final states ");
            System.out.println("   6. Print the transitions ");
            System.out.println("   7. Verify a sequence ");
            System.out.println("   8. Generate longest sequence ");
            System.out.println("   0. Exit ");
            System.out.println("Give the desired option:");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            while(true){
                try {
                    option = Integer.parseInt(bufferedReader.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Wrong option");
                }
            }

            switch (option) {
                case 1:
                    readFromKeyboard();
                    break;
                case 2:
                    readFromFile();
                    break;
                case 3:
                    printStates();
                    break;
                case 4:
                    printAlphabet();
                    break;
                case 5:
                    printFinalStates();
                    break;
                case 6:
                    printTransitions();
                    break;
                case 7:
                    verifySequence();
                    break;
                case 8:
                    generateSequence();
                    break;
                default:
                    System.out.println("Wrong Option!");
            }
        }

    }

    private static void readFromKeyboard(){
        List<String> states = new ArrayList<>();
        HashSet<String> alphabet = new HashSet<>();
        Transition tranzitions = new Transition();
        HashSet<String> finalStates = new HashSet<>();
        String initialState = "";

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Please give the states all in a row with space between:");
            String line = br.readLine();
            Collections.addAll(states, line.split(" "));

            System.out.println("Please give the status of the inserted states (1 for initial, 2 for final:");
            line = br.readLine();
            List<Integer> statuses = new ArrayList<>();
            String[] row = line.split(" ");
            for (String status : row) {
                statuses.add(Integer.parseInt(status));
            }
            //set the initial and final states
            for (int i = 0; i < states.size(); i++) {
                if (statuses.get(i) == 1) {
                    initialState = states.get(i);
                } else if (statuses.get(i) == 2) {
                    finalStates.add(states.get(i));
                }
            }

            System.out.println("Please give the transitions (press -1 to stop)");
            line = br.readLine();
            while(!line.equals("-1")){
                row = line.split(" ");
                tranzitions.addTransition(row[0], row[1], row[2]);
                alphabet.add(row[2]);
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        automataFinita = new AutomataFinita(states, alphabet, tranzitions, finalStates, initialState);
    }

    private static void readFromFile(){
        try {
            automataFinita = new AutomataFinita("src/main/resources/automata.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void printStates(){
        System.out.println("All the states of the AF:");
        for (String state : automataFinita.getStates()) {
            System.out.println(state);
        }
    }

    private static void printAlphabet(){
        System.out.println("All the alphabet of the AF:");
        for (String alphabet: automataFinita.getAlphabet()) {
            System.out.println(alphabet);
        }
    }

    private static void printFinalStates(){
        System.out.println("All final states of AF:");
        for (String state : automataFinita.getFinalStates()) {
            System.out.println(state);
        }
    }

    private static void printTransitions(){
        System.out.println("All transitions of the AF");
        Transition transition = automataFinita.getTranzitions();
        Map<String, List<Tuple<String, String>>> allTransitions = transition.getTransitions();

        for (String startState : allTransitions.keySet()) {
            for (Tuple<String, String> tuple: allTransitions.get(startState)) {
                System.out.println(startState + " --> " + tuple.getVal2() + " with " + tuple.getVal1());
            }
        }
    }

    private static void verifySequence(){
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please give the sequence to verify:");
        try {
            System.out.println(automataFinita.isAcceptedSequence(br.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateSequence(){
        //ask: what if the AF is cyclic? the generated sequence will be infinit
        //what we need this for later?
        //TODO:implement this functionality
        automataFinita.findLongestSequence();
    }
}
