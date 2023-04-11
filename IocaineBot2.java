import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class IocaineBot2 implements RoShamBot {
    int NUM_VARIATIONS = 10;
    int MEMORY_SIZE = 10;   
    HashMap<String, Integer> strats = new HashMap<String, Integer>();
    HashMap<Action, List<Action>> beaten_by = new HashMap<Action, List<Action>>();
    HashMap<Action, List<Action>> beats = new HashMap<Action, List<Action>>();
    ArrayList<Action> opp_memory;
    ArrayList<Action> own_memory;
    int count = 0;
    
    public IocaineBot2() {
        beats.put(Action.ROCK, Arrays.asList(Action.SCISSORS, Action.LIZARD));
        beats.put(Action.PAPER, Arrays.asList(Action.ROCK, Action.SPOCK));
        beats.put(Action.SCISSORS, Arrays.asList(Action.PAPER, Action.LIZARD));
        beats.put(Action.LIZARD, Arrays.asList(Action.SPOCK, Action.PAPER));
        beats.put(Action.SPOCK, Arrays.asList(Action.SCISSORS, Action.ROCK));

        beaten_by.put(Action.ROCK, Arrays.asList(Action.PAPER, Action.SPOCK));
        beaten_by.put(Action.PAPER, Arrays.asList(Action.SCISSORS, Action.LIZARD));
        beaten_by.put(Action.SCISSORS, Arrays.asList(Action.ROCK, Action.SPOCK));
        beaten_by.put(Action.LIZARD, Arrays.asList(Action.ROCK, Action.SCISSORS));
        beaten_by.put(Action.SPOCK, Arrays.asList(Action.PAPER, Action.LIZARD));

        strats.put("p0", 0);
        strats.put("p1", 0);
        strats.put("p2", 0);
        strats.put("q0", 0);
        strats.put("q1", 0);
        strats.put("q2", 0);


        opp_memory = new ArrayList<Action>();
        own_memory = new ArrayList<Action>();

        // Init with ROCK
        for (int i = 0; i<MEMORY_SIZE; i++) {
            opp_memory.add(Action.ROCK);
            own_memory.add(Action.ROCK);
        }
    }

    Action p0(Action a) { // Beat opponents move
        return beaten_by.get(a).get((int)Math.random()*beaten_by.size());
    }

    Action p1(Action a) { // Beat p0
        return beaten_by.get(p0(a)).get((int)Math.random()*beaten_by.size());
    }

    Action p2(Action a) { // Beat p1
        return beaten_by.get(p1(a)).get((int)Math.random()*beaten_by.size());
    }

    
    public void updateScores(String strat, Action a, Action b) {
        if (beats.get(a).contains(b)) {
            strats.put(strat, strats.get(strat) + 1);
        } else if (beaten_by.get(a).contains(b)) {
            strats.put(strat, strats.get(strat) - 1);
        } 
        // If tie no change necessary
    }

    public Action getNextMove(Action lastOpponentMove) {
        Action opp_mode = mode(opp_memory);
        Action own_mode = mode(own_memory);

        // Update memory and return
        opp_memory.set(count % MEMORY_SIZE, lastOpponentMove);

        //Get action for each sub-strat and update
        Action p0 = p0(lastOpponentMove);
        Action p1 = p1(lastOpponentMove);
        Action p2 = p2(lastOpponentMove);
        Action q0 = p0(own_memory.get(own_memory.size()-1));
        Action q1 = p1(own_memory.get(own_memory.size()-1));
        Action q2 = p2(own_memory.get(own_memory.size()-1));
        updateScores("p0", p0, lastOpponentMove);
        updateScores("p1", p1, lastOpponentMove);
        updateScores("p2", p2, lastOpponentMove);
        updateScores("q0", q0, own_memory.get(own_memory.size()-1));
        updateScores("q1", q1, own_memory.get(own_memory.size()-1));
        updateScores("q2", q2, own_memory.get(own_memory.size()-1));
        

        // Use strat with highest score
        int maxScore = 0;
        String maxStrat = "p0";
        for (Map.Entry<String, Integer> e: strats.entrySet()) {
            if (e.getValue() > maxScore) {
                maxStrat = e.getKey();
                maxScore = e.getValue();
            }
            //System.out.print(e.getKey() + " " + e.getValue() + "\t");
        }
        //System.out.println();


        // Now execute the best sub-strat against the mode
        Action a = null;

        switch (maxStrat) {
            case "p0": 
                a = p0(opp_mode);
            case "p1":
                a = p1(opp_mode);
            case "p2":
                a = p2(opp_mode);
            case "q0":
                a = p0(own_mode);
            case "q1":
                a = p1(own_mode);
            case "q2":
                a = p2(own_mode);
        }

        opp_memory.set(count % MEMORY_SIZE, lastOpponentMove);
        count++;

        return a;
    }

    // Modified from https://stackoverflow.com/questions/15725370/write-a-mode-method-in-java-to-find-the-most-frequently-occurring-element-in-anhttps://stackoverflow.com/questions/15725370/write-a-mode-method-in-java-to-find-the-most-frequently-occurring-element-in-an
    public Action mode(ArrayList<Action> array) {
        HashMap<Action,Integer> hm = new HashMap<Action,Integer>();
        int max  = 1;
        Action temp = null;

        for(int i = 0; i < array.size(); i++) {

            if (hm.get(array.get(i)) != null) {

                int count = hm.get(array.get(i));
                count++;
                hm.put(array.get(i), count);

                if(count > max) {
                    max  = count;
                    temp = array.get(i);
                }
            }

            else 
                hm.put(array.get(i),1);
        }
        return temp;
    }
}

