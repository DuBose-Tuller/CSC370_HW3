import java.util.ArrayList;
import java.lang.Math;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;

public class SuperStratBot implements RoShamBot {
    int NUM_ROUNDS = 10000;
    int MEMORY_SIZE = 25;

    ArrayList<Action> opp_memory;
    ArrayList<Action> own_memory;
    int[] scores = new int[MEMORY_SIZE];
    int count = 0;
    int game_diff = 0;

    HashMap<Action, List<Action>> beats = new HashMap<Action, List<Action>>();
    HashMap<Action, List<Action>> beaten_by = new HashMap<Action, List<Action>>();
    ArrayList<Action> actions = new ArrayList<Action>();

    // RPSLS ordering CUMULATIVE
    double[] levers = {0.2, 0.4, 0.6, 0.8, 1.0};
    int levers_countdown = 0;

    public SuperStratBot() {
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
        Action move;
        Action lastMove = own_memory.get(count % MEMORY_SIZE);
        opp_memory.set(count % MEMORY_SIZE, lastOpponentMove);
        int lastScore = getScore(lastMove, lastOpponentMove);
        scores[count % MEMORY_SIZE] = lastScore;
        game_diff += lastScore; // Update the total score

        if (levers_countdown > 0) {
            //System.out.println("Pull The Lever Kronk!");
            levers_countdown--;
            count++;
            move = useLevers(levers);
            own_memory.set(count % MEMORY_SIZE, move);
            return move;
        }


        /* Main strategy */
                
        //Lock in the win if we are sufficiently ahead
        if (game_diff > 300) {
            System.out.println("SuperStratBot is superior! No more need to try...");
            reset_levers(); //Nash
            levers_countdown = NUM_ROUNDS;
            return useLevers(levers);
        }

        double flip = Math.random();
        if (flip > 0.1) { //
            move = nash();
        } else {
            move = new ShortTermMemoryBot().getNextMove(lastOpponentMove);
        }


        // Retern the selected move
        count++;
        own_memory.set(count % MEMORY_SIZE, move); 
        return move;
    }

    int getScore(Action p1, Action p2) {
        if (beats.get(p1).contains(p2)) {
            return 1;
        } else if (beats.get(p2).contains(p1)) {
            return -1;
        }

        return 0;
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

    void reset_levers() {
        levers[0] = 0.2;
        levers[1] = 0.4;
        levers[2] = 0.6;
        levers[3] = 0.8;
        levers[4] = 1.0;
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
