import java.util.*;

public abstract class Search {

  protected Search_Node init_node;
  protected Search_Node current_Node;
  protected ArrayList<Search_Node> open; 
  protected ArrayList<Search_Node> closed;
  protected ArrayList<Search_Node> successor_nodes;
  protected Search_State goal_state;

  public Search_State get_Goal(){return goal_state;}
  public void put_Goal(Search_State goal){goal_state = goal;}


  public Search_Node get_current_node(){return current_Node;}


  public String run_Search(Search_State init_state, Search_State g_state, String search_method){
      goal_state = g_state;
      return run_Search(init_state, search_method);
  }

  public String run_Search(Search_State init_state, String search_method){
    init_node = new Search_Node(init_state, null);
    System.out.println("\nStarting Search");
    open = new ArrayList<Search_Node>();
    open.add(init_node);
    closed = new ArrayList<Search_Node>();
    int cnum = 1;
    while(!open.isEmpty()){
       select_Node(search_method); // puts value to current_Node
       if (current_Node.goalP(this)) return report_Success();//"Search Succeeds";
       expand(search_method); // successor_nodes of current_Node
       closed.add(current_Node);
       cnum++;
     }
     return "Search Fails";
   }

   private void expand(String search_method){
      successor_nodes = current_Node.get_Successors(this);
      vet_Successors(search_method);
      switch (search_method){
       case "depth_first":
        for (Search_Node i : successor_nodes){open.add(0,i);}
        break;
       default:
        for (Search_Node i : successor_nodes){open.add(i);}
      }
   }

   private void vet_Successors(String search_method){ 
      ArrayList<Search_Node> vslis = new ArrayList<Search_Node>();
      switch (search_method){
        case "depth_first": case "breadth_first": case "best_first":
           for (Search_Node snode : successor_nodes){
              if (!(on_Closed(snode)) && !(on_Open(snode))) vslis.add(snode);
           };
           break;
        case "branch_and_bound": 
           for (Search_Node snode : successor_nodes){
              if (!(on_Closed(snode)) && !(on_Open(snode))) vslis.add(snode);
              else if (on_Open(snode)){
                        int i = pos_Open(snode);
                        if (snode.best_path_cost() < open.get(i).best_path_cost()){
                               open.remove(i);
                               open.add(snode);
                               vslis.add(snode);
                         }
                     }
               else if (on_Closed(snode)){
                         int i = pos_Closed(snode);
                         if (snode.best_path_cost() < closed.get(i).best_path_cost()){
                               closed.remove(i);
                               open.add(snode);
                               vslis.add(snode);
                          }
                     }
                        
             };
           break;
        case "A_star":
           for (Search_Node snode : successor_nodes){
              if (!(on_Closed(snode)) && !(on_Open(snode))) vslis.add(snode);
              else if (on_Open(snode)){
                        int i = pos_Open(snode);
                        if (snode.evaluation_fn(goal_state) < open.get(i).evaluation_fn(goal_state)){
                               open.remove(i);
                               open.add(snode);
                               vslis.add(snode);
                         }
                     }
               else if (on_Closed(snode)){
                         int i = pos_Closed(snode);
                         if (snode.evaluation_fn(goal_state) < closed.get(i).evaluation_fn(goal_state)){
                               closed.remove(i);
                               open.add(snode);
                               vslis.add(snode);
                          }
                     }
                        
             };
            
      }
      successor_nodes = vslis;
   }

   private boolean on_Closed(Search_Node new_node){
      boolean ans = false;
      for (Search_Node closed_node : closed){
         if (new_node.same_State(closed_node)) ans = true;
      }
      return ans;
   }

 
   private boolean on_Open(Search_Node new_node){
      boolean ans = false; 
      for (Search_Node open_node : open){
         if (new_node.same_State(open_node)) ans = true;
      }
      return ans;
   }

   private void select_Node(String search_method){
     int i = 0, min = 0;
     switch (search_method){
       case "depth_first": 
          int osize = open.size();
          current_Node = open.get(0);//open.get(osize-1);
          open.remove(0); System.out.println(current_Node);
          break;
      case "breadth_first":
          current_Node = open.get(0);
          open.remove(0);
          break;
      case "branch_and_bound":
          i = 0; min = open.get(0).best_path_cost();
          for (int j = 1; j < open.size(); j++){
              if (open.get(j).best_path_cost() < min){i = j; min = open.get(j).best_path_cost();}
          }
          current_Node = open.get(i);
          open.remove(i);
          break;
       case "best_first":
          i = 0; min = open.get(0).difference(goal_state);
          for (int j = 1; j < open.size(); j++){
              if (open.get(j).difference(goal_state) < min){i = j; min = open.get(j).difference(goal_state);}
          }
          current_Node = open.get(i);  
          open.remove(i);
          break;
       case "A_star":
          i = 0; min = open.get(0).evaluation_fn(goal_state);
          for (int j = 1; j < open.size(); j++){
              if (open.get(j).evaluation_fn(goal_state) < min){i = j; min = open.get(j).evaluation_fn(goal_state);}
          }
          current_Node = open.get(i);  
          open.remove(i);
     } 
   }

   private String report_Success(){
     Search_Node n = current_Node;
     StringBuffer buf = new StringBuffer(n.toString());
     int plen = 1;
     while (n.get_parent() != null){
       buf.insert(0,"\n");
       n = n.get_parent();
       buf.insert(0, n.toString());
       plen++;
     }
     System.out.println("===============================");
     System.out.println("Search Succeeds");
     System.out.println("Efficiency " + ((float)plen/(closed.size()+1)));
     System.out.println("Nodes visited: " + (closed.size()+1));
     System.out.println("Solution Path");
     return buf.toString();
   }

   private int pos_Open(Search_Node new_node){
     for (int i = 0; i < open.size(); i++)
           if (new_node.same_State(open.get(i))) return i;
     return -1;
   }

   private int pos_Closed(Search_Node new_node){
     for (int i = 0; i < closed.size(); i++)
           if (new_node.same_State(closed.get(i))) return i;
     return -1;
   }
}