import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

class Link{
	public int cost;
	private String fromNode;
	public String toNode;
	public Link(String fromNode,String toNode,int cost){
		this.cost = cost;
		this.fromNode = fromNode;
		this.toNode = toNode;
	}

	@Override
	public String toString() {
		return fromNode + " -["+cost+"]-> " +toNode;
	}
}

class SearchTree{
	public ArrayList<String> nodes = new ArrayList<String>();
	public Map<String,ArrayList<Link>> link = new HashMap<String,ArrayList<Link>>();
	public void addNode(String from, String to, int cost){
		if (!nodes.contains(to)){
			nodes.add(to);
			link.put(to,new ArrayList<Link>());
		}
		if (!nodes.contains(from)){
			nodes.add(from);
			ArrayList<Link> newLink = new ArrayList<Link>();
			newLink.add(new Link(from,to,cost));
			link.put(from,newLink);
		}
		else{
			link.get(from).add(new Link(from,to,cost));
		}
	}

	@Override
	public String toString() {
		return link.entrySet().stream().map(a -> a.toString()).reduce("", (a, b)-> a==""?b:a+"\n"+b);
	}
}

class Path implements Cloneable{
	private int cost=0;
	private LinkedList<String> dStore;

	private Path(){
		dStore = new LinkedList<String>();
	}

	Path(String firstNode){
		dStore = new LinkedList<String>();
		this.add(firstNode,0);
	}

	Path add(String s,int cost) {
		this.cost += cost;
		this.dStore.addLast(s);
		return this;
	}

	boolean contains(String nodeString){
		return this.dStore.contains(nodeString);
	}

	int getCost(){
		return cost;
	}

	String  getLast(){
		return this.dStore.getLast();
	}

	public Path clone(){
		Path newPath = new Path();
		newPath.cost = this.cost;
		newPath.dStore = (LinkedList<String>) this.dStore.clone();
		return newPath;
	}

	@Override
	public String toString() {
		return "["+cost+"]"+dStore.stream().reduce("",(a,b)-> a!=""? a+">"+b: b);
	}
}

public class BFS {

	public static ArrayList<Path> breatheFirstSearch(int minCost,SearchTree graph,String from, String to){
		LinkedList<Path> q = new LinkedList<Path>();
		ArrayList<Path> minPaths = new ArrayList<>();
		q.add(new Path(from));
		while(!q.isEmpty()){
			System.out.println("Before IN Q: "+q);
			Path fifo = q.pollLast();
			System.out.println("Start Round > Pop Last: ["+fifo+"]");
			System.out.println("Left IN Q: "+q+"\n");
			if(fifo.getLast().equals(to) && fifo.getCost() == minCost ){
				minPaths.add(fifo);
			}else{
				graph.link.get(fifo.getLast()).stream()
						.filter(a->fifo.getCost()<=minCost && !fifo.contains(a.toNode))
						.forEach(a->q.addFirst(fifo.clone().add(a.toNode,a.cost)));
			}
		}
		return minPaths;
	}

	public static void testBFS() throws FileNotFoundException {

		Scanner sc = new Scanner(new FileInputStream(new File("test_input.txt")));
		SearchTree st = new SearchTree();
		while(sc.hasNext()){
			String from = sc.next();
			String to = sc.next();
			int cost = sc.nextInt();
			st.addNode(from,to,cost);
		}
		System.out.println(st);
		System.out.println(breatheFirstSearch(6,st,"a","e"));
	}
	public static void main(String[] args) throws FileNotFoundException {
		testBFS();
	}
}
