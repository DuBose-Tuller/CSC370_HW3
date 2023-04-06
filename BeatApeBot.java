import java.util.List;
import java.util.HashMap;
import java.util.Arrays;

public class BeatApeBot implements RoShamBot {
    Action lastMove = Action.ROCK;
    HashMap<Action, List<Action>> beaten_by = new HashMap<Action, List<Action>>();

    public BeatApeBot() {
        beaten_by.put(Action.ROCK, Arrays.asList(Action.PAPER, Action.SPOCK));
        beaten_by.put(Action.PAPER, Arrays.asList(Action.SCISSORS, Action.LIZARD));
        beaten_by.put(Action.SCISSORS, Arrays.asList(Action.ROCK, Action.SPOCK));
        beaten_by.put(Action.LIZARD, Arrays.asList(Action.ROCK, Action.SCISSORS));
        beaten_by.put(Action.SPOCK, Arrays.asList(Action.PAPER, Action.LIZARD));
    }
   
    public Action getNextMove(Action lastOpponentMove) {
        Action next_action = beaten_by.get(lastMove).get(0);
        lastMove = next_action;
        return next_action;
    }
}
