package automata_finita;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class AutomataFinita {

    private List<String> states = new ArrayList<>();
    private HashSet<String> alphabet = new HashSet<>();
    private Transition tranzitions = new Transition();
    private HashSet<String> finalStates = new HashSet<>();
    private String initialState;

    public AutomataFinita(String filename) throws FileNotFoundException {
        readFile(filename);
    }

    public AutomataFinita(List<String> states, HashSet<String> alphabet, Transition tranzitions, HashSet<String> finalStates, String initialState) {
        this.states = states;
        this.alphabet = alphabet;
        this.tranzitions = tranzitions;
        this.finalStates = finalStates;
        this.initialState = initialState;
    }

    public List<String> getStates() {
        return states;
    }

    public HashSet<String> getAlphabet() {
        return alphabet;
    }

    public Transition getTranzitions() {
        return tranzitions;
    }

    public HashSet<String> getFinalStates() {
        return finalStates;
    }

    public String getInitialState() {
        return initialState;
    }

    private void readFile(String filename) throws FileNotFoundException {
        Scanner s = new Scanner(new File(filename));
        String[] line;

        // read the list of states
        line = s.nextLine().split(" ");
        Collections.addAll(states, line);

        // read the status of each state (initial state, intermediate or final)
        List<Integer> statuses = new ArrayList<>();
        line = s.nextLine().split(" ");
        for (String status : line) {
            statuses.add(Integer.parseInt(status));
        }

        // set the initial state and final states - 1 for initial and 2 for final
        for (int i = 0; i < states.size(); i++) {
            if (statuses.get(i) == 1) {
                initialState = states.get(i);
            } else if (statuses.get(i) == 2) {
                finalStates.add(states.get(i));
            }
        }

        // read the number of entries (transitions)
        int nrEntries = Integer.parseInt(s.nextLine());
        // read and add the transitions
        for (int i = 0; i < nrEntries; i++) {
            line = s.nextLine().split(" ");
            tranzitions.addTransition(line[0], line[1], line[2]);
            alphabet.add(line[2]);
        }
    }

    boolean isAcceptedSequence(String sequence) throws IOException {
        return verifySequence(sequence);
    }

    private boolean verifySequence(String sequence) {
        // we start from the initial state
        String initialState = this.initialState;
        boolean accepted = true;
        String finalState = "";

        // go through the sequence until the end or until it is not accepted
        for (int i = 0; i < sequence.length() && accepted; ++i) {
            String character = sequence.substring(i, i + 1), next = "";

            List<String> goals = tranzitions.getGoals(initialState, character);

            if (goals.isEmpty()) {
                accepted = false;
                break;
            }

            // what if we have more than 1 goal state in list which can be reached with the value 'character'?
            next = goals.get(0);
            if (finalStates.contains(next) && i == sequence.length() - 1) {
                accepted = true;
                finalState = next;
                break;
            }

            initialState = next;
            finalState = next;
        }

        if(!finalStates.contains(finalState)){
            accepted = false;
        }

        return accepted;

    }

    public List<String> findLongestSequence(){
        return null;
    }


}
