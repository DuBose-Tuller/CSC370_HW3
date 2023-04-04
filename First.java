import java.util.ArrayList;
import java.lang.Math;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;

public class First implements RoShamBot {

    int MEMORY_SIZE = 25;
    ArrayList<Action> opp_memory;
    ArrayList<Action> own_memory;
    int[] scores = new int[MEMORY_SIZE];
    int count;
    HashMap<Action, List<Action>> beats = new HashMap<Action, List<Action>>();
    HashMap<Action, List<Action>> beaten_by = new HashMap<Action, List<Action>>();
    ArrayList<Action> actions = new ArrayList<Action>();

    public First() {
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

        actions.add(Action.ROCK);
        actions.add(Action.PAPER);
        actions.add(Action.SCISSORS);
        actions.add(Action.LIZARD);
        actions.add(Action.SPOCK);


        opp_memory = new ArrayList<Action>();
        own_memory = new ArrayList<Action>();

        // Init
        for (int i = 0; i<MEMORY_SIZE; i++) {
            opp_memory.add(Action.LIZARD);
            own_memory.add(Action.LIZARD);
            scores[i] = 0;
        }
    } 



    public Action getNextMove(Action lastOpponentMove) {
        Action lastMove = own_memory.get(count);
        opp_memory.set(count % MEMORY_SIZE, lastOpponentMove);
        Action move = null;
        
        //Calculate result of last round        
        double flip = Math.random();
        if (flip > 0.5) {
            move = nash();
        } else {
            move = new ShortTermMemoryBot().getNextMove(lastOpponentMove);
        }

        own_memory.set(count % (MEMORY_SIZE+1), move); 
        return move;
    }

    Action nash() {
        double coinFlip = Math.random();
        
        if (coinFlip <= 1.0/5.0)
            return Action.ROCK;
        else if (coinFlip <= 2.0/5.0)
            return Action.PAPER;
        else if (coinFlip <= 3.0/5.0)
            return Action.SCISSORS;
        else if (coinFlip <= 4.0/5.0)
            return Action.LIZARD;
        else 
            return Action.SPOCK;
    } 
}
