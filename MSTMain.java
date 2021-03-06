import java.util.*;
import java.io.*;
/**
* Max Peterson, Minerva Chen
* 11/22/2013
* MSTMain
*
* A program to read in a graph and print a minimum spanning tree.
* Copied from FindPaths except for the MST activities in the lower half of main.
*/

public class MSTMain {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("USAGE: java MSTMain <vertex_file> <edge_file>");
			System.exit(1);
		}

		MyGraph g = readGraph(args[0], args[1]);

		Scanner console = new Scanner(System.in);
		Collection<Vertex> v = g.vertices();
      Collection<Edge> e = g.edges();
		System.out.println("Vertices are " + v);
		System.out.println("Edges are " + e);
		System.out.println();
		Set<Edge> mst = g.getMST();
		if (mst != null) {
			System.out.println("One possible MST for the graph is " + mst);
			int sum = 0;
			for (Edge edge : mst)
				sum += edge.getWeight();
			System.out.println("That would cost " + sum + '.');
			if (sum > 4500)
				System.out.println("could you pay my tuition instead?");
		} else
			System.out.println("No MST exists.");
	}
	
	/**
	* reads a graph
	* @param f1 file name of vertices
	* @param f2 file name of edges
	* @return new MyGraph made of vertices and edges
	*/
	public static MyGraph readGraph(String f1, String f2) {
		Scanner s = null;
		try {
			s = new Scanner(new File(f1));
		} catch(FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: " + f1);
			System.exit(2);
		}
		Collection<Vertex> v = new ArrayList<Vertex>();
		while(s.hasNext())
			v.add(new Vertex(s.next()));
		try {
			s = new Scanner(new File(f2));
		} catch(FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: " + f2);
			System.exit(2);
      }
		Collection<Edge> e = new ArrayList<Edge>();
		while(s.hasNext()) {
			try {
				Vertex a = new Vertex(s.next());
				Vertex b = new Vertex(s.next());
				int w = s.nextInt();
				e.add(new Edge(a, b, w));
			} catch (NoSuchElementException e2) {
				System.err.println("EDGE FILE FORMAT INCORRECT");
				System.exit(3);
			}
		}
		return new MyGraph(v, e);
	}
}
