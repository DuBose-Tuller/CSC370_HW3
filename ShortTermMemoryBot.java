import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.util.List;
import java.util.Arrays;

public class ShortTermMemoryBot implements RoShamBot {
    int MEMORY_SIZE = 10;
    ArrayList<Action> memory;
    int count;
    HashMap<Action, List<Action>> beaten_by = new HashMap<Action, List<Action>>();

    public ShortTermMemoryBot() {
        beaten_by.put(Action.ROCK, Arrays.asList(Action.PAPER, Action.SPOCK));
        beaten_by.put(Action.PAPER, Arrays.asList(Action.SCISSORS, Action.LIZARD));
        beaten_by.put(Action.SCISSORS, Arrays.asList(Action.ROCK, Action.SPOCK));
        beaten_by.put(Action.LIZARD, Arrays.asList(Action.ROCK, Action.SCISSORS));
        beaten_by.put(Action.SPOCK, Arrays.asList(Action.PAPER, Action.LIZARD));


        memory = new ArrayList<Action>();

        // Init with ROCK
        for (int i = 0; i<MEMORY_SIZE; i++) {
            memory.add(Action.LIZARD);
        }
    }

    Action lastMove = Action.ROCK;

    public Action getNextMove(Action lastOpponentMove) {
        memory.set(count % MEMORY_SIZE, lastOpponentMove);
        count++;

        Action mode = mode(memory);
        return beaten_by.get(mode).get((int)Math.random()*beaten_by.size());
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