import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.util.*;

public class Graph {
    int vertexCt;  // Number of vertices in the graph.
    GraphNode[] G;  // Adjacency list for graph.
    String graphName;  //The file from which the graph was created.
    int [][] residual;
    int fCheck = 0;

    public Graph() {
        this.vertexCt = 0;
        this.graphName = "";
    }
    public void go(){
        System.out.println();
        System.out.println( graphName );
        System.out.println("--- Max Flow For ---");
        while (findPath()){
            findPath();
        }
        System.out.println(" Total Flow Produced: " + fCheck + "\n");
        System.out.println("---Used Edges---");
        usedEdges();
        System.out.println();
        System.out.println("--- Min Cuts ---");
        minCut();
    }

    // find all paths with current data
    public boolean findPath(){
        LinkedList<Integer> q = new LinkedList<>();
        for( int i = 0; i< vertexCt; ++i){
            G[i].visited = false;
        }
        q.add(G[0].nodeID);
        G[0].visited = true;
        G[0].parent = -1;
        while (q.size() != 0){
            int u = q.remove();
            for( int v = 0; v < vertexCt; v++){
                if(!G[v].visited && residual[v][u] > 0){
                    q.add(v);
                    G[v].parent = u;
                    G[v].visited = true;
                }
            }
        }
        maxFlow();
        return G[vertexCt - 1].visited;
    }

    // Find the maxFlow of one path
    public void maxFlow(){
        ArrayList<Integer> list  = new ArrayList<>();
        GraphNode end = G[vertexCt -1];
        int check = 10;
        while (end != G[0]){
            GraphNode temp = G[end.parent];
            for (GraphNode.EdgeInfo i : temp.succ){
                  if(G[i.to] == end ){
                      if(residual[i.to][i.from] <= check){
                        check = residual[i.to][i.from];
                      }
                      list.add(end.nodeID);
                      end = temp;
                  }
            }
        }
        if(check == 0){
            return;
        }
        list.add(G[0].nodeID);
        Collections.reverse(list);
        System.out.println(" Max Flow " + check + " : " + list.toString());
        fCheck += check;
        updateFlow(check);
    }

    // Subtract maxFlow from capacity
    public void updateFlow(int flow) {
        GraphNode end = G[vertexCt - 1];
        while (end != G[0]) {
            GraphNode temp = G[end.parent];
            for (GraphNode.EdgeInfo i : temp.succ) {
                if (G[i.to] == end) {
                    residual[i.to][i.from] -= flow;
                    residual[i.from][i.to] += flow;
                    end = temp;
                }
            }
        }
    }

    // Find Capacity 0 paths and add them to list for printing
    public void minCut(){
        ArrayList<GraphNode.EdgeInfo> list = new ArrayList<>();
        Queue<GraphNode> q = new LinkedList<>();
        q.add(G[0]);
        while(!q.isEmpty()){
            GraphNode temp = q.remove();
            for (GraphNode.EdgeInfo i : temp.succ){
                if(residual[i.to][i.from] > 0 ){
                    q.add(G[i.to]);

                }else if(residual[i.to][i.from] == 0) {
                    if(!list.contains(i)){
                        list.add(i);
                    }
                }
            }
        }
        for (GraphNode.EdgeInfo i: list){
            System.out.println(" " + i);
        }
    }
    // Print all Edges that are used along with how many cases where used on that path
    public void usedEdges(){
        int check;
        for (GraphNode i : G){
            for (GraphNode.EdgeInfo j : i.succ){
                if (residual[j.to][j.from] != j.capacity){
                    check = j.capacity - residual[j.to][j.from];
                    System.out.println(j + " Transported " + check + " Cases");

                }
            }
        }
    }
    public int getVertexCt() {
        return vertexCt;
    }

    public boolean addEdge(int source, int destination, int cap) {
        //System.out.println("addEdge " + source + "->" + destination + "(" + cap + ")");
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        //add edge
        G[source].addEdge(source, destination, cap);
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The Graph " + graphName + " \n");

        for (int i = 0; i < vertexCt; i++) {
            sb.append(G[i].toString());
        }
        return sb.toString();
    }
    // Make Graph
    public void makeGraph(String filename) {
        try {
            graphName = filename;
            Scanner reader = new Scanner(new File(filename));
            vertexCt = reader.nextInt();
            G = new GraphNode[vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                G[i] = new GraphNode(i);
            }
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                if (!addEdge(v1, v2, cap))
                    throw new Exception();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        makeMatrix();
    }

    // Build Matrix
    public void makeMatrix() {
        int[][] rGraph = new int[G.length][G.length];
        for (GraphNode i : G){
            for(GraphNode.EdgeInfo j : i.succ){
                rGraph[j.to][j.from] = j.capacity;
                rGraph[j.from][j.to] = 0;

            }
        }
        residual = rGraph;
    }
}
