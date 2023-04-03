import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class ShortTermMemoryBot implements RoShamBot {
    int MEMORY_SIZE = 100;
    ArrayList<Action> memory;
    int count;

    public ShortTermMemoryBot() {
        memory = new ArrayList<Action>();

        // Init with ROCK
        for (int i = 0; i<MEMORY_SIZE; i++) {
            memory.add(Action.ROCK);
        }
    }

    Action lastMove = Action.ROCK;

    public Action getNextMove(Action lastOpponentMove) {
        memory.add(count % MEMORY_SIZE, lastOpponentMove);
        System.out.println(memory.get(count % MEMORY_SIZE));
        count++;

        Action mode = mode(memory);
        return Diagram.beats.get(mode).get((int)Math.random()*Diagram.beats.size());
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
        System.out.println(temp);
        return temp;
    }   


    // Action getMode(ArrayList<Action> memory) {
    //     Integer[] counts = {0,0,0,0,0};

    //     for (Action a: memory) {
    //         switch (a){
    //             case ROCK:
    //                 counts[0]++;
    //             case PAPER:
    //                 counts[1]++;
    //             case SCISSORS:
    //                 counts[2]++;
    //             case LIZARD:
    //                 counts[3]++;
    //             case SPOCK:
    //                 counts[4]++;
    //         }
    //     }

    //     // Return argmax of counts
    //     int re = Integer.MIN_VALUE;
    //     int arg = -1;
    //     for (int i = 0; i < counts.length; i++) {
    //         if (counts[i] > re) {
    //             re = counts[i];
    //             arg = i;
    //         }
    //     }
    //     return null;
    // }
   
}