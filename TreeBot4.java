import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;public class TreeBot4 implements RoShamBot {    // number of non-prediction layers.
    private int LAYERS = 4;    /*
     * data capacity needs to be manually updated with layers... some basic capacities we tested though:
     * LAYERS = 2: 156
     * LAYERS = 3: 781
     * LAYERS = 4: 3906
     *
     * essentially for some layer value x, we need to update to 5^(x+1) + 5^(x) + ... + 5^0
     */
    private double[] data = new double[3906];    // queue to store previous opponent actions
    private Queue<Action> previousActions = new LinkedList<>();    // constant array that helps "heap" traversal.
    private HashMap<Action, Integer> JUMP_VALUES = new HashMap<>();    /**
     * Constructor for the TreeBot class, fills the data array with 1s and sets up the JUMP_VALUES map.
     */
    public TreeBot4() {
        Arrays.fill(data, 1);
        for (int i = 1; i <= Action.values().length; i++) {
            JUMP_VALUES.put(Action.values()[i - 1], i);
        }
    }    /**
     * Updates the queue
     *
     * Removes head if long enough, adds last move always.
     *
     * @param lastOpponentMove - action representing the opponent’s last move
     */
    private void updateQueue(Action lastOpponentMove) {
        if (previousActions.size() >= LAYERS) {
            previousActions.poll();
        }
        previousActions.add(lastOpponentMove);
    }    /**
     * Updates the probability tree
     *
     * Takes in the opponent’s last move, traverses via the order specified in the
     * queue and
     * adds one to each. Looks at prediction layer and adds on to corresponding
     * action index.
     *
     * @param lastOpponentMove
     */
    private void updateTree(Action lastOpponentMove) {
        int index = 0;
        for (Action prevAction : previousActions) {
            data[index] += 1;
            int jumpValue = JUMP_VALUES.get(prevAction);
            index = 5 * index + jumpValue;
        }
        index = 5 * index + JUMP_VALUES.get(lastOpponentMove);
        data[index] += 1;    }    /**
     * Generates a prediction based on current queue and the current probability
     * tree
     *
     * Calculates a probability based on the opponent’s previous moves, and the
     * moves they have
     * taken after those moves. We take the move that the opponent picked the most,
     * and then randomly
     * pick a move that beats it based on their selection.
     *
     * @return predicted winning action
     */
    private Action predictNextMove() {
        int index = 0;
        for (Action prevAction : previousActions) {
            int jumpValue = JUMP_VALUES.get(prevAction);
            index = 5 * index + jumpValue;
        }        Action maxAction = Action.ROCK;
        double maxCount = -1;
        for (int i = 1; i <= Action.values().length; i++) {
            if (data[5 * index + i] > maxCount) {
                maxCount = data[5 * index + i];
                maxAction = Action.values()[i - 1];
            }
        }
        Action play = getWinningAction(maxAction);
        return play;
    }    /**
     * Helper method that returns a winning action for a specified action
     *
     * @param action - any valid action
     * @return an action that beats the specified action
     */
    private Action getWinningAction(Action action) {
        double coinFlip = Math.random();
        if (action.equals(Action.ROCK)) {
            if (coinFlip <= .5) {
                return Action.PAPER;
            } else {
                return Action.SPOCK;
            }
        } else if (action.equals(Action.SCISSORS)) {
            if (coinFlip <= .5) {
                return Action.ROCK;
            } else {
                return Action.SPOCK;
            }
        } else if (action.equals(Action.PAPER)) {
            if (coinFlip <= .5) {
                return Action.SCISSORS;
            } else {
                return Action.LIZARD;
            }
        } else if (action.equals(Action.LIZARD)) {
            if (coinFlip <= .5) {
                return Action.SCISSORS;
            } else {
                return Action.ROCK;
            }
        } else if (action.equals(Action.SPOCK)) {
            if (coinFlip <= .5) {
                return Action.LIZARD;
            } else {
                return Action.PAPER;
            }
        }        throw new IllegalArgumentException(action + " not a valid action.");
    }    @Override
    /**
     * Implements RoShamBot’s getNextMove method.
     *
     * @return the move (that we hope beats whatever the opponent selects...)
     */
    public Action getNextMove(Action lastOpponentMove) {
        updateTree(lastOpponentMove);
        updateQueue(lastOpponentMove);
        if (previousActions.size() < LAYERS) {
            double coinFlip = Math.random();
            if (coinFlip <= 1.0 / 5.0)
                return Action.ROCK;
            else if (coinFlip <= 2.0 / 5.0)
                return Action.PAPER;
            else if (coinFlip <= 3.0 / 5.0)
                return Action.SCISSORS;
            else if (coinFlip <= 4.0 / 5.0)
                return Action.LIZARD;
            else
                return Action.SPOCK;
        } else {
            Action prediction = predictNextMove();
            return prediction;
        }    }
}
