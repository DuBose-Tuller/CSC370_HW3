import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class IocaineBot3 implements RoShamBot {
    int NUM_VARIATIONS = 10;
    int MEMORY_SIZE = 10;   
    HashMap<String, Integer> strats = new HashMap<String, List<Integer>>();
    HashMap<Action, List<Action>> beaten_by = new HashMap<Action, List<Action>>();
    HashMap<Action, List<Action>> beats = new HashMap<Action, List<Action>>();
    ArrayList<Action> opp_memory;
    ArrayList<Action> own_memory;
    int count = 0;
    
    public IocaineBot3() {
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

        int[] empty = {0,0,0,0,0,0};

        strats.put("p0", Arrays.asList(empty));
        strats.put("p1", 0);
        strats.put("p2", 0);
        strats.put("p0 own", 0);
        strats.put("p1 own", 0);
        strats.put("p2 own", 0);


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
        return beats.get(p1(a)).get((int)Math.random()*beaten_by.size());
    }

    
    
    public Action getNextMove(Action lastOpponentMove) {
        Action opp_mode = mode(opp_memory);
        Action own_mode = mode(own_memory);
        Class<?> c = this.getClass();
        Class[] types = new Class[] {Action.class};
        Action a = null;

        // Update memory and return
        count++;
        opp_memory.set(count % MEMORY_SIZE, lastOpponentMove);

        for (String f: strats.keySet()) {
            // Simulate each hypothetical round
            Method s;
            try {
                if (f.contains("own")) {
                    s = c.getDeclaredMethod(f.split(" ")[0], types);
                    a = (Action)s.invoke(this, own_mode);
                } else {
                    s = c.getDeclaredMethod(f, types);
                    a = (Action)s.invoke(this, opp_mode);
                }

            // Make Java Happy
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }   

            // Calculate scores
            if (beats.get(a).contains(lastOpponentMove)) {
                strats.put(f, strats.get(f) + 1);
            } else if (beaten_by.get(a).contains(lastOpponentMove)) {
                strats.put(f, strats.get(f) - 1);
            } 
            // If tie no change necessary
        }

        // Use strat with highest score
        int maxScore = Integer.MIN_VALUE;
        String maxStrat = "p0";
        for (Map.Entry<String, Integer> e: strats.entrySet()) {
            if (e.getValue() > maxScore) {
                maxStrat = e.getKey();
                maxScore = e.getValue();
            } else if (e.getValue() == maxScore) {// Handle ties sortof randomly
                double flip = Math.random();
                if (flip > 0.5) {
                    maxStrat = e.getKey();
                    maxScore = e.getValue();
                }
            }

            System.out.print(e.getKey() + " " + e.getValue() + "\t");
        }
        System.out.println(" Pick: " + maxStrat);


        // Now execute the best sub-strat against the mode
        try {
            Method s;
            if (maxStrat.contains("own")) {
                s = c.getDeclaredMethod(maxStrat.split(" ")[0], types);
                a = (Action)s.invoke(this, own_mode);
            } else {
                s = c.getDeclaredMethod(maxStrat, types);
                a = (Action)s.invoke(this, opp_mode);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace(); 
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }



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

