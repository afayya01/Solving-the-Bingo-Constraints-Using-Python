import java.util.*;

public class Search_Node{

  private Search_State state;
  private Search_Node parent;
  private int cost;
  public Search_State get_State(){return state;}
  public Search_Node get_parent(){return parent;}
  public void put_parent(Search_Node p){parent = p;}
  public int get_cost(){return cost;}
  public void put_cost(int c){cost = c;}
  
  // Constructors
  public Search_Node(Search_State s, Search_Node p){
    state = s; parent = p; cost = 1;
  } 

  public Search_Node(Search_State s, Search_Node p, int c){
    state = s; parent = p; cost = c;
  } 

  public boolean goalP(Search searcher){
      return state.goalP(searcher);
  }

  public int difference (Search_State goal){
      return state.difference(goal);
  }

  public int best_path_cost(){
    int cos = 0;
    Search_Node n = this;
    while (n.parent != null){
      cos += n.cost;
      n = n.parent;
    }
    return cos;
   }

  public int evaluation_fn(Search_State goal){
    return difference(goal) + best_path_cost();
  }

  public ArrayList<Search_Node> get_Successors(Search searcher){
    ArrayList<Search_State> slis = state.get_Successors(searcher);
    ArrayList<Search_Node> nlis = new ArrayList<Search_Node>();
    for (Search_State suc_state: slis){
            Search_Node n = new Search_Node(suc_state, searcher.get_current_node(), 
                                            suc_state.cost_from(searcher.get_current_node().get_State()));
            nlis.add(n);
    }
    return nlis;
  } 

 public boolean same_State(Search_Node n2){
    return state.same_State(n2.get_State());
 }

 public String toString(){
   return "Node with state " + state.toString();
 }

}