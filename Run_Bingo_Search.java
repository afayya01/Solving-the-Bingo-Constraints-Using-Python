import java.util.*;

public class Run_Bingo_Search{
 public static void main(String[] args){
   Bingo_Search searcher = new Bingo_Search(args[0]);
   Search_State init_state = (Search_State) new Bingo_State();
   String res = searcher.run_Search(init_state, "depth_first");
   System.out.println(res);
 }
}