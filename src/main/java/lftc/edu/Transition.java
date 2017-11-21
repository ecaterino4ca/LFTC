package lftc.edu;

import automata_finita.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transition {

    private Map<String, List<Tuple<String, String>>> transitions = new HashMap<>();

    public void addTransition(String stateFrom,
                              String goalState,
                              String value) {
        transitions.putIfAbsent(stateFrom, new ArrayList<>());
        transitions.get(stateFrom).add(new Tuple<>(value, goalState));
    }

    public List<String> getGoals(String startState, String value) {
        if (!transitions.containsKey(startState)) {
            return null;
        }

        List<Tuple<String, String>> listOfTuples = transitions.get(startState);
        List<String> goals = new ArrayList<>();

        for (Tuple<String, String> tuple: listOfTuples) {
            if(tuple.getVal1().equals(value)){
                goals.add(tuple.getVal2());
            }
        }
        return goals;
    }

    public Map<String, List<Tuple<String, String>>> getTransitions() {
        return transitions;
    }
}
