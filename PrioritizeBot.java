import java.util.ArrayList;
import java.lang.Math;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;

public class PrioritizeBot implements RoShamBot {
    int MEMORY_SIZE = 10;
    double HIGH_PRIORITY = 0.3;
    double LOW_PRIORITY = 0.05;

    ArrayList<Action> opp_memory;
    int count = 0;

    HashMap<Action, List<Action>> beats = new HashMap<Action, List<Action>>();
    HashMap<Action, List<Action>> beaten_by = new HashMap<Action, List<Action>>();
    HashMap<Action, Integer> order = new HashMap<Action, Integer>();

    // RPSLS ordering CUMULATIVE
    double[] levers = {0.2, 0.4, 0.6, 0.8, 0.1}; // Nash
    int levers_countdown = 0;

    public PrioritizeBot() {
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

        order.put(Action.ROCK, 0);
        order.put(Action.PAPER, 1);
        order.put(Action.SCISSORS, 2);
        order.put(Action.LIZARD, 3);
        order.put(Action.SPOCK, 4);

        opp_memory = new ArrayList<Action>();

        // Init
        for (int i = 0; i<MEMORY_SIZE; i++) {
            opp_memory.add(Action.LIZARD);
        }
    } 



    public Action getNextMove(Action lastOpponentMove) {
        ShortTermMemoryBot stm = new ShortTermMemoryBot();
        Action move;
        opp_memory.set(count % MEMORY_SIZE, lastOpponentMove);

        /* Main strategy */
        Action target = stm.getNextMove(lastOpponentMove);

        // Set the levers to optimize beating `target`
        double[] dist = new double[5];
        dist[order.get(target)] = HIGH_PRIORITY;
        for (Action a: beaten_by.get(target)) {
            dist[order.get(a)] = HIGH_PRIORITY;
        }

        for (Action a: beats.get(target)) {
            dist[order.get(a)] = LOW_PRIORITY;
        }

        setLevers(dist);
        move = useLevers(levers);

        // Retern the selected move
        count++;
        return move;
    }

    void setLevers(double[] dist) {
        assert dist.length == 5;
        
        double sum = 0;
        for (int i=0; i<dist.length; i++) {
            sum += dist[i];
            levers[i] = sum;
        }
    }

    Action useLevers(double[] levers) {
        double flip = Math.random();

        if (flip <= levers[0])
            return Action.ROCK;
        else if (flip <= levers[1])
            return Action.PAPER;
        else if (flip <= levers[2])
            return Action.SCISSORS;
        else if (flip <= levers[3])
            return Action.LIZARD;
        else 
            return Action.SPOCK;
    }
}
