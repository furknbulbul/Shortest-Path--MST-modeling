import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;


public class project3main  {
	public static void main(String[] args) throws IOException  { 
		String fileName = args[0];
	     ArrayList<String> inputLines = new ArrayList<String>();
	     try (FileReader reader = new FileReader(fileName);
	          BufferedReader bufferedReader = new BufferedReader((reader))) {
	         String line;
	         while ((line = bufferedReader.readLine()) != null) {
	             inputLines.add(line);
	         }
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
	     Iterator<String> itr= inputLines.iterator();
	     
	     int timeLimit=Integer.parseInt(itr.next());
	     int nCity=Integer.parseInt(itr.next());
	     
	     
	     String[] tmp=itr.next().split(" ");
	     
	     int mecnunCity=Integer.parseInt(tmp[0].substring(1));
	     int leylaCity=Integer.parseInt(tmp[1].substring(1));
	     
	     int numberC=leylaCity;//number of cities type c
	     int numberD=nCity-leylaCity;//number of cities type d
    	 
    	 DirectedGraph dGraph= new DirectedGraph(numberC);//directed part is c part
	     UndirectedGraph undGraph=new UndirectedGraph(numberD+1);//leyla's city + d part of country
	     
	     for(int i=0;i<leylaCity-1;i++) {
	    	 String s=itr.next();
	    	 String[] arr=s.split(" ");
	    	 int start=Integer.parseInt(arr[0].substring(1));
	    	 
	    	 for(int j=1;j<arr.length-1;j+=2) {
	    		 int to=Integer.parseInt(arr[j].substring(1));
	    		 
	    		 int weight=Integer.parseInt(arr[j+1]);
	    		 
	    		 dGraph.addEdge(dGraph.nodes.get(to-1),dGraph.nodes.get(start-1),weight);
	    	 }	    	 
	     }
	     //list of shortest path returned by Dijkstra 
	     ArrayList<Node> lst= Dijkstra(dGraph,dGraph.nodes.get(mecnunCity-1),dGraph.nodes.get(leylaCity-1));
	     lst.add(lst.size(),dGraph.nodes.get(leylaCity-1));
	     
	     //output
	     
	     String[] leylaSourceArr=itr.next().split(" ");
	     //from c to d, c[last]=nodes[0]
	     for(int j=1; j<leylaSourceArr.length-1;j+=2) {
	    	 if(leylaSourceArr[j].charAt(0)=='c') {
	    		 continue;
	    	 }
	    	 int to=Integer.parseInt(leylaSourceArr[j].substring(1));
	    	 int weight=Integer.parseInt(leylaSourceArr[j+1]);
	    	 undGraph.addEdge(undGraph.nodes.get(to),undGraph.nodes.get(0),weight);
	     }
	     
	     
	     for(int i=0;i<numberD;i++) {
	    	 String s=itr.next();
	    	 String[] arr=s.split(" ");
			 int start=Integer.parseInt(arr[0].substring(1));
	    	 
	    	 for(int j=1;j<arr.length-1;j+=2) {
	    		 int to=Integer.parseInt(arr[j].substring(1));
	    		 
	    		 int weight=Integer.parseInt(arr[j+1]);
	    		 
	    		 undGraph.addEdge(undGraph.nodes.get(to),undGraph.nodes.get(start),weight);
	    	 }	    	 
	    	  
	     }
	     //assume leyla reaches mecnun
	     boolean reaches=true;
	    //int returned by prim for mst
	     int mst=prim(undGraph,undGraph.nodes.get(0));
	     try { 
	        File outputFile=new File(args[1]);
	        if(!outputFile.exists()) {
	        	outputFile.createNewFile();
	        }
	        PrintStream output = new PrintStream(args[1]);
	        //mecnun cannot reach leyla
	        if(lst.size()==1){
	        	reaches=false;
	        	output.println(-1);
	        	output.print(-1);
	      	
	        }
	        if(reaches==true) {
	        	for(Node n: lst) {
			    	 output.print(n.label+" ");
			    }
			    output.println();
			    //cannot marry
			    if(dGraph.dijkstraTime>timeLimit) {
	        		output.print(-1);
	        	}
			    //can marry, mst is -1 when honeymoon is impossible
			    else if(dGraph.dijkstraTime<=timeLimit) {
	        		output.print(mst*2);	
	        	}
	        }
  
         output.close();
	     }
	     catch(Exception e) {
	    	 e.getStackTrace();
	     }
	}
	//Dijkstra algorithm
	public static ArrayList<Node> Dijkstra(DirectedGraph graph, Node start, Node to) {
		HashMap<Node,Node> prevNodes=new HashMap<Node,Node>();//first node destination
		HashMap<Node,Integer> totalWeight=new HashMap<Node,Integer>();
		HashSet<Node> visited=new HashSet<Node>();
		PriorityQueue<Node> pq=new PriorityQueue<Node>(new compareNodes());//orders with respect to weight
		ArrayList<Node> result=new ArrayList<Node>();
		
		
		totalWeight.put(start,0);
		start.pathWeight=0;
		pq.add(start);
		
		for(Node node:graph.nodes) {
			if(!node.equals(start)) {
				totalWeight.put(node,Integer.MAX_VALUE);
				node.pathWeight=Integer.MAX_VALUE;
				pq.add(node);
				
			}
		}
		while(!pq.isEmpty()) {
			Node smallestNode= pq.poll();
			
			for(Edge edge: smallestNode.edges) {
				
				if(!visited.contains(edge.destination)) {
					int newPath=totalWeight.get(smallestNode)+edge.weight;
					
					if(newPath<totalWeight.get(edge.destination)) {
						totalWeight.put(edge.destination,newPath);
						prevNodes.put(edge.destination,smallestNode);
						pq.remove(edge.destination);
						edge.destination.pathWeight=totalWeight.get(edge.destination);
						pq.add(edge.destination);
						
					}	
				}	
			} 
			
			visited.add(smallestNode);
			
		}

		if(!visited.contains(to)) {
			return result;
		}
		
		graph.dijkstraTime=totalWeight.get(to);
		while(true) {
			if(Math.abs(totalWeight.get(to))==Integer.MAX_VALUE) {
				result.clear();
				return result;
			}
			
			result.add(0,prevNodes.get(to));
			to=prevNodes.get(to);
			if (to.equals(start)) {
				break;	
			}
			
		}
		
		
		return result;
	}
	
	
	//Prim's algorithm
	public static int prim(UndirectedGraph graph, Node source) {
		Set<Node> mst=new HashSet<Node>();
		HashMap<Node,Integer> nodeWeight=new HashMap<Node,Integer>();
		
		
		
		PriorityQueue<Node> pq=new PriorityQueue<Node>(new compareNodes());//orders with respect to weight
		
		for(Node node:graph.nodes) {
			if(!node.equals(source)) {
				nodeWeight.put(node,Integer.MAX_VALUE);
				node.pathWeight=Integer.MAX_VALUE;
				
			}
		}
		nodeWeight.put(source,0);
		source.pathWeight=0;
		pq.add(source);
		
		
		int totalWeight=0;
		while(!pq.isEmpty()) {
			Node smallestNode=pq.poll();
			
			
			if(!mst.contains(smallestNode)) {
				mst.add(smallestNode);
			
				for(Edge edge: smallestNode.edges) {
					if(!mst.contains(edge.destination)&&nodeWeight.get(edge.destination)>edge.weight) {
						pq.remove(edge.destination);
						nodeWeight.put(edge.destination,edge.weight);	
						edge.destination.pathWeight=edge.weight;
						pq.add(edge.destination);
							
						
					}
					
				}
			}
			
		}
		
		
		if(!mst.containsAll(graph.nodes)) {
			return -1;
		}
		
		for(Node node:graph.nodes) {
			totalWeight+=nodeWeight.get(node);
		}
		
		return totalWeight;
	}
	

}
