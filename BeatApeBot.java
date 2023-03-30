public class BeatApeBot implements RoShamBot {
    Action lastMove = Action.ROCK;
   
    public Action getNextMove(Action lastOpponentMove) {
        Action next_action = Diagram.beaten_by.get(lastMove).get(0);
        lastMove = next_action;
        return next_action;
    }
}
