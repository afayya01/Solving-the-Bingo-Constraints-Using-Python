import java.util.*;

public abstract class Search_State {
   abstract boolean goalP(Search searcher);
   abstract ArrayList<Search_State> get_Successors(Search searcher);
   abstract boolean same_State(Search_State n2);
   abstract int cost_from (Search_State from);
   abstract int difference (Search_State goal);
}
