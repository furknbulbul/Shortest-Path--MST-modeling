

import java.util.ArrayList;
import java.util.List;
//undirected graph implementation
public class UndirectedGraph{
	int nVertices;
	List<Node> nodes;
	public UndirectedGraph(int nVertices) {
		nodes=new ArrayList<Node>();
		
		
		for(int i=0;i<nVertices;i++) {
			String s="d"+i;//c last is d0 
			nodes.add(new Node(i+1,s));
		}
			
			
	}
	// add undirected edge between given nodes 
	public void addEdge(Node toVertex,Node source, int weight) {
		source.edges.add(new Edge(toVertex, weight));
		toVertex.edges.add(new Edge(source, weight));
		
	}

	
	
	
}
