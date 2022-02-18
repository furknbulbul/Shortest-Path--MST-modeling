import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


//node class
class Node{
		int pathWeight;//holds weight
		int v;
		String label;
		List<Edge> edges;
		public Node(int v, String label) {
			edges=new ArrayList<Edge>();
			this.v=v;
			this.label=label;
		}
		
	}
//compare nodes for dijkstra and prim
class compareNodes implements Comparator<Node>{
	public int compare(Node n1, Node n2) {
		return n1.pathWeight-n2.pathWeight;
	}
}
//edge class 
class Edge{
		Node destination;
		int weight;
		public Edge(Node v,int weight) {
			destination=v;
			this.weight=weight;
		}

}
//directed graph implementation
public class DirectedGraph{
	int dijkstraTime;
	int nVertices;
	List<Node> nodes;
	public DirectedGraph(int nVertices) {
		dijkstraTime=0;
		nodes=new ArrayList<Node>();
		for(int i=0;i<nVertices;i++) {
			int tmp=i+1;
			String s="c"+tmp;
			nodes.add(new Node(i+1, s));
		}
			
	}
	//add edges between given nodes
	public void addEdge(Node toVertex,Node source, int weight) {
		Edge edge= new Edge(toVertex, weight);
		source.edges.add(edge);
		
	}

	
	
	
	
}
