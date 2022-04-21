/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.httpfrk.cores.HttpHandler;
import io.github.kubesys.tools.annotations.ServiceDefinition;

/**
 * @author xuyuanjia2017@otcaix.iscas.ac.cn
 * @since 2019.10.29
 *
 */

@ServiceDefinition
public class McmfService extends HttpHandler {

	@GetMapping("/mcmf/solveBase")
	public Object solveBase(JsonNode json) {
		NetworkFlowSolverBase nfsb = NetworkUtil.initializeSolver(json);
		NetworkUtil.constructBellmanFordGraph(nfsb, json);
		nfsb.solve();
		System.out.printf("Max flow: %d, Min cost: %d\n", nfsb.getMaxFlow(), nfsb.getMinCost());
		for(DisplayModel.Animation temp : nfsb.dm.animations){
			System.out.println(temp);
		}
		return nfsb;
	}

	public static class NetworkUtil {
		public static String bfString = "";
		public static final String TASKS = "tasks";
		public static final String APPLICATIONS = "applications";
		public static final String MACHINES = "machines";
		public static final int augmentLength = 4;

		public static final String[] colors = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C",
				"D", "E", "F" };
		public static final String[] columnColors = new String[] { "#ff9999", "#67b55b", "SandyBrown",
				"MediumTurquoise", "#0399d3" };

		public static String getRandomColor() {
			StringBuffer sb = new StringBuffer("#");
			for (int i = 0; i < 6; ++i) {
				Random random = new Random();
				sb.append(colors[random.nextInt(colors.length - 1)]);
			}
			return sb.toString();
		}

		public static NetworkFlowSolverBase initializeSolver(JsonNode jo) {
			int n = jo.get(NetworkUtil.TASKS).size() + jo.get(NetworkUtil.APPLICATIONS).size()
					+ jo.get(NetworkUtil.MACHINES).size() + 2;
			return new MinCostMaxFlowWithBellmanFord(n, 0, n - 1);
		}

		public static void constructBellmanFordGraph(NetworkFlowSolverBase solver, JsonNode jo) {
			solver.dm = new DisplayModel();

			solver.dm.data.add(new DisplayModel.Data("0", 0, 0, 1, columnColors[0]));
			solver.dm.data.add(new DisplayModel.Data(solver.getT() + "", 4, 0, 1, columnColors[4]));
			JsonNode tasks = jo.get(NetworkUtil.TASKS);
			JsonNode applications = jo.get(NetworkUtil.APPLICATIONS);
			JsonNode machines = jo.get(NetworkUtil.MACHINES);

			for (int i = 0; i < applications.size(); i++) {
				JsonNode application = applications.get(i);
				solver.dm.data.add(new DisplayModel.Data(application.get("index").asInt() + "", 2, i,
						applications.size(), columnColors[2]));
			}

			for (int i = 0; i < machines.size(); i++) {
				JsonNode machine = machines.get(i);
				solver.dm.data.add(new DisplayModel.Data(machine.get("index").asInt() + "", 3, i, machines.size(),
						columnColors[3]));
				solver.dm.links.add(new DisplayModel.Link(machine.get("index").asInt() + "", solver.getT() + "",
						"<" + machine.get("cpu").asInt() + "," + machine.get("cost").asInt() + ">", "#000"));
				solver.addEdge(machine.get("index").asInt(), solver.getT(), machine.get("cpu").asInt());
			}

			for (int i = 0; i < tasks.size(); i++) {
				JsonNode task = tasks.get(i);
				solver.dm.data.add(
						new DisplayModel.Data(task.get("index").asInt() + "", 1, i, tasks.size(), columnColors[1]));
				solver.dm.links.add(new DisplayModel.Link(0 + "", task.get("index").asInt() + "",
						"<" + task.get("cpu").asInt() + "," + task.get("cost").asInt() + ">", "#000"));
				solver.addEdge(0, task.get("index").asInt(), task.get("cpu").asInt(), task.get("cost").asInt());
				solver.dm.links
						.add(new DisplayModel.Link(task.get("index").asInt() + "", task.get("application").asInt() + "",
								"<" + task.get("cpu").asInt() + "," + task.get("cost").asInt() + ">", "#000"));
				solver.addEdge(task.get("index").asInt(), task.get("application").asInt(), task.get("cpu").asInt(),
						task.get("cost").asInt());
			}

			for (int i = 0; i < applications.size(); i++) {
				JsonNode application = applications.get(i);
				for (int j = 0; j < machines.size(); j++) {
					JsonNode machine = machines.get(j);
					int applicationFlow = 0;
					for (int k = 0; k < tasks.size(); k++) {
						if (tasks.get(k).get("application").asInt() - application.get("index").asInt() == 0) {
							applicationFlow += tasks.get(k).get("cpu").asInt();
						}
					}
					solver.dm.links.add(new DisplayModel.Link(application.get("index").asInt() + "",
							machine.get("index").asInt() + "",
							"<" + applicationFlow + "," + application.get("cost").asInt() + ">", "#000"));
					solver.addEdge(application.get("index").asInt(), machine.get("index").asInt(), applicationFlow,
							application.get("cost").asInt());
				}
			}
		}

		public static void main(String[] args) {
			System.out.printf(getRandomColor());
			System.out.printf(getRandomColor());
			System.out.printf(getRandomColor());
		}
	}

	public static abstract class NetworkFlowSolverBase {
		// To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
		protected static final long INF = Long.MAX_VALUE / 2;

		public DisplayModel dm;

		public static class Edge {
			public int from, to;
			public Edge residual;
			public long flow, cost;
			public final long capacity, originalCost;

			public Edge(int from, int to, long capacity) {
				this(from, to, capacity, 0 /* unused */);
			}

			public Edge(int from, int to, long capacity, long cost) {
				this.from = from;
				this.to = to;
				this.capacity = capacity;
				this.originalCost = this.cost = cost;
			}

			public boolean isResidual() {
				return capacity == 0;
			}

			public long remainingCapacity() {
				return capacity - flow;
			}

			public void augment(long bottleNeck) {
				flow += bottleNeck;
				residual.flow -= bottleNeck;
			}

			public String toString(int s, int t) {
				String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
				String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
				return String.format("Edge %s -> %s | flow = %d | capacity = %d | is residual: %s", u, v, flow,
						capacity, isResidual());
			}
		}

		// Inputs: n = number of nodes, s = source, t = sink
		protected final int n, s, t;

		protected long maxFlow;
		protected long minCost;

		protected boolean[] minCut;
		protected List<Edge>[] graph;

		// 'visited' and 'visitedToken' are variables used for graph sub-routines to
		// track whether a node has been visited or not. In particular, node 'i' was
		// recently visited if visited[i] == visitedToken is true. This is handy
		// because to mark all nodes as unvisited simply increment the visitedToken.
		private int visitedToken = 1;
		private int[] visited;

		// Indicates whether the network flow algorithm has ran. We should not need to
		// run the solver multiple times, because it always yields the same result.
		private boolean solved;

		/**
		 * Creates an instance of a flow network solver. Use the {@link #addEdge} method
		 * to add edges to the graph.
		 *
		 * @param n - The number of nodes in the graph including source and sink nodes.
		 * @param s - The index of the source node, 0 <= s < n
		 * @param t - The index of the sink node, 0 <= t < n, t != s
		 */
		public NetworkFlowSolverBase(int n, int s, int t) {
			this.n = n;
			this.s = s;
			this.t = t;
			initializeGraph();
			minCut = new boolean[n];
			visited = new int[n];
		}

		// Construct an empty graph with n nodes including the source and sink nodes.
		@SuppressWarnings("unchecked")
		private void initializeGraph() {
			graph = new List[n];
			for (int i = 0; i < n; i++)
				graph[i] = new ArrayList<Edge>();
		}

		/**
		 * Adds a directed edge (and residual edge) to the flow graph.
		 *
		 * @param from     - The index of the node the directed edge starts at.
		 * @param to       - The index of the node the directed edge ends at.
		 * @param capacity - The capacity of the edge.
		 */
		public void addEdge(int from, int to, long capacity) {
			if (capacity < 0)
				throw new IllegalArgumentException("Capacity < 0");
			Edge e1 = new Edge(from, to, capacity);
			Edge e2 = new Edge(to, from, 0);
			e1.residual = e2;
			e2.residual = e1;
			graph[from].add(e1);
			graph[to].add(e2);
		}

		/** Cost variant of {@link (int, int, int)} for min-cost max-flow */
		public void addEdge(int from, int to, long capacity, long cost) {
			Edge e1 = new Edge(from, to, capacity, cost);
			Edge e2 = new Edge(to, from, 0, -cost);
			e1.residual = e2;
			e2.residual = e1;
			graph[from].add(e1);
			graph[to].add(e2);
		}

		// Marks node 'i' as visited.
		public void visit(int i) {
			visited[i] = visitedToken;
		}

		// Returns whether or not node 'i' has been visited.
		public boolean visited(int i) {
			return visited[i] == visitedToken;
		}

		// Resets all nodes as unvisited. This is especially useful to do
		// between iterations of finding augmenting paths, O(1)
		public void markAllNodesAsUnvisited() {
			visitedToken++;
		}

		/**
		 * Returns the graph after the solver has been executed. This allow you to
		 * inspect the {@link Edge#flow} compared to the {@link Edge#capacity} in each
		 * edge. This is useful if you want to figure out which edges were used during
		 * the max flow.
		 */
		public List<Edge>[] getGraph() {
			execute();
			return graph;
		}

		// Returns the maximum flow from the source to the sink.
		public long getMaxFlow() {
			execute();
			return maxFlow;
		}

		// Returns the min cost from the source to the sink.
		// NOTE: This method only applies to min-cost max-flow algorithms.
		public long getMinCost() {
			execute();
			return minCost;
		}

		// Returns the min-cut of this flow network in which the nodes on the "left
		// side"
		// of the cut with the source are marked as true and those on the "right side"
		// of the cut with the sink are marked as false.
		public boolean[] getMinCut() {
			execute();
			return minCut;
		}

		// Wrapper method that ensures we only call solve() once
		private void execute() {
			if (solved)
				return;
			solved = true;
			solve();
		}

		// Method to implement which solves the network flow problem.
		public abstract void solve();

		public static long getINF() {
			return INF;
		}

		public DisplayModel getDm() {
			return dm;
		}

		public void setDm(DisplayModel dm) {
			this.dm = dm;
		}

		public int getN() {
			return n;
		}

		public int getS() {
			return s;
		}

		public int getT() {
			return t;
		}

		public void setMaxFlow(long maxFlow) {
			this.maxFlow = maxFlow;
		}

		public void setMinCost(long minCost) {
			this.minCost = minCost;
		}

		public void setMinCut(boolean[] minCut) {
			this.minCut = minCut;
		}

		public void setGraph(List<Edge>[] graph) {
			this.graph = graph;
		}

		public int getVisitedToken() {
			return visitedToken;
		}

		public void setVisitedToken(int visitedToken) {
			this.visitedToken = visitedToken;
		}

		public int[] getVisited() {
			return visited;
		}

		public void setVisited(int[] visited) {
			this.visited = visited;
		}

		public boolean isSolved() {
			return solved;
		}

		public void setSolved(boolean solved) {
			this.solved = solved;
		}
	}

	public static class MinCostMaxFlowWithBellmanFord extends NetworkFlowSolverBase {
		/**
		 * Creates a min-cost maximum flow network solver. To construct the flow network
		 * use the {@link NetworkFlowSolverBase#addEdge} method to add edges to the
		 * graph.
		 *
		 * @param n - The number of nodes in the graph including source and sink nodes.
		 * @param s - The index of the source node, 0 <= s < n
		 * @param t - The index of the sink node, 0 <= t < n, t != s
		 */
		public MinCostMaxFlowWithBellmanFord(int n, int s, int t) {
			super(n, s, t);
		}

		@Override
		public void solve() {

			// Sum up the bottlenecks on each augmenting path to find the max flow and min
			// cost.
			List<Edge> path;
			int count = 0;
			while ((path = getAugmentingPath()).size() != 0) {

				// Find bottle neck edge value along path.
				long bottleNeck = Long.MAX_VALUE;
				for (Edge edge : path)
					bottleNeck = min(bottleNeck, edge.remainingCapacity());

				String color = NetworkUtil.getRandomColor();
				for (Edge edge : path) {
					edge.augment(bottleNeck);
					minCost += bottleNeck * edge.originalCost;
					System.out.println(edge.from + "----" + edge.to + "'s cost:" + edge.cost + "  flow:" + edge.flow);
					preRecord(edge, color);
				}
				System.out.println("adding flow:" + bottleNeck);
				afterRecord(path, count);
				maxFlow += bottleNeck;
				count++;
			}
		}

		private void preRecord(Edge edge, String color) {
			if (edge.to == this.t) {
				dm.animations.add(new DisplayModel.Animation("reset", "link", new DisplayModel.Link(edge.from + "",
						edge.to + "", "<" + edge.remainingCapacity() + "," + edge.cost + ">", "#FFF"), null));
				dm.animations.add(new DisplayModel.Animation("reset", "link", new DisplayModel.Link(edge.from + "",
						edge.to + "", "<" + edge.remainingCapacity() + "," + edge.cost + ">", color), null));
			} else {
				dm.animations.add(new DisplayModel.Animation("reset", "link", new DisplayModel.Link(edge.from + "",
						edge.to + "", "<" + edge.remainingCapacity() + "," + edge.cost + ">", "#FFF"), null));
				dm.animations.add(new DisplayModel.Animation("reset", "link", new DisplayModel.Link(edge.from + "",
						edge.to + "", "<" + edge.capacity + "," + edge.cost + ">", color), null));
			}
		}

		private void afterRecord(List<Edge> path, int count) {
			if (path.size() != NetworkUtil.augmentLength) {
				return;
			}
			Edge edge = path.get(0);

			dm.animations
					.add(new DisplayModel.Animation("reset", "data", null, new DisplayModel.Data(edge.to + "", 70)));

			dm.animations
					.add(new DisplayModel.Animation("reset", "data", null, new DisplayModel.Data(edge.to + "", 50)));

			edge = path.get(2);
			dm.animations
					.add(new DisplayModel.Animation("reset", "data", null, new DisplayModel.Data(edge.to + "", 70)));

			dm.animations
					.add(new DisplayModel.Animation("reset", "data", null, new DisplayModel.Data(edge.to + "", 50)));

			edge = path.get(0);
			dm.animations.add(new DisplayModel.Animation("reset", "link", new DisplayModel.Link(edge.from + "",
					edge.to + "", "<" + edge.remainingCapacity() + "," + edge.cost + ">", "#FFF"), null));

			edge = path.get(1);
			dm.animations.add(new DisplayModel.Animation("reset", "link", new DisplayModel.Link(edge.from + "",
					edge.to + "", "<" + edge.remainingCapacity() + "," + edge.cost + ">", "#FFF"), null));

			dm.animations.add(new DisplayModel.Animation("add", "data", null,
					new DisplayModel.Data("Task" + edge.from, 5, count, 5, NetworkUtil.columnColors[1])));

			edge = path.get(2);
			boolean ifExist = false;
			for (int i = 0; i < dm.animations.size(); i++) {
				if (dm.animations.get(i).data != null && dm.animations.get(i).data.name.equals("Mach" + edge.to)) {
					ifExist = true;
					break;
				}
			}
			if (!ifExist) {
				dm.animations.add(new DisplayModel.Animation("add", "data", null,
						new DisplayModel.Data("Mach" + edge.to, 6, count, 5, NetworkUtil.columnColors[3])));
			}

			dm.animations.add(new DisplayModel.Animation("add", "link",
					new DisplayModel.Link("Task" + path.get(1).from, "Mach" + edge.to, "deploy on", "#000"), null));

		}

		/**
		 * Use the Bellman-Ford algorithm (which work with negative edge weights) to
		 * find an augmenting path through the flow network.
		 */
		private List<Edge> getAugmentingPath() {
			long[] dist = new long[n];
			Arrays.fill(dist, INF);
			dist[s] = 0;

			Edge[] prev = new Edge[n];

			// For each vertex, relax all the edges in the graph, O(VE)
			for (int i = 0; i < n - 1; i++) {
				for (int from = 0; from < n; from++) {
					for (Edge edge : graph[from]) {
						if (edge.remainingCapacity() > 0 && dist[from] + edge.cost < dist[edge.to]) {
							dist[edge.to] = dist[from] + edge.cost;
							prev[edge.to] = edge;
						}
					}
				}
			}

			// Retrace augmenting path from sink back to the source.
			LinkedList<Edge> path = new LinkedList<>();
			for (Edge edge = prev[t]; edge != null; edge = prev[edge.from])
				path.addFirst(edge);
			return path;
		}

		/* Example usage. */

		public static void main(String[] args) {
			testSmallNetwork();
		}

		private static void testSmallNetwork() {
			int n = 6;
			int s = n - 1;
			int t = n - 2;
			MinCostMaxFlowWithBellmanFord solver;
			solver = new MinCostMaxFlowWithBellmanFord(n, s, t);

			solver.addEdge(s, 1, 4, 10);
			solver.addEdge(s, 2, 2, 30);
			solver.addEdge(1, 2, 2, 10);
			solver.addEdge(1, t, 0, 9999);
			solver.addEdge(2, t, 4, 10);

			// Prints: Max flow: 4, Min cost: 140
			System.out.printf("Max flow: %d, Min cost: %d\n", solver.getMaxFlow(), solver.getMinCost());
		}
	}

	public static class DisplayModel {
		public static final int height = 400;
		public static final int columnSegment = 100;

		public List<Data> data;
		public List<Link> links;

		public List<Animation> animations;

		public String[] colors = NetworkUtil.columnColors;
		public List<Map<String, String>> categories;

		@SuppressWarnings("serial")
		public DisplayModel() {
			this.data = new ArrayList<>();
			this.links = new ArrayList<>();
			this.animations = new ArrayList<>();
			categories = new ArrayList<>();
			categories.add(new HashMap<String, String>() {
				{
					put("name", "起点");
				}
			});
			categories.add(new HashMap<String, String>() {
				{
					put("name", "任务(Task)");
				}
			});
			categories.add(new HashMap<String, String>() {
				{
					put("name", "应用");
				}
			});
			categories.add(new HashMap<String, String>() {
				{
					put("name", "主机(Mach)");
				}
			});
			categories.add(new HashMap<String, String>() {
				{
					put("name", "终点");
				}
			});
		}

		public static class Normal {
			public boolean show = true;
			public int width = 4;
			public String formatter = "";
			public int curveness = 0;
			public String color = "#AAA";
		}

		public static class Label {
			public Normal normal;

			public Label() {
				this.normal = new Normal();
			}
		}

		public static class LineStyle {
			public Normal normal;

			public LineStyle() {
				this.normal = new Normal();
			}
		}

		public static class Data {
			public String name;
			public Map<String, String> itemStyle = new HashMap<>();
			public int x;
			public int y;
			public int symbolSize;

			public Data(String index, int columnIndex, int rowIndex, int rowSize, String color) {
				x = columnSegment + columnIndex * columnSegment;
				y = (height / (rowSize + 1)) * (rowIndex + 1);
				name = index + "";
				itemStyle.put("color", color);
				this.symbolSize = 50;
			}

			public Data(String index, int symbolSize) {
				name = index + "";
				this.symbolSize = symbolSize;
			}

			@Override
			public String toString() {
				return "点名称："+this.name+"；文字尺寸："+this.symbolSize+"；附属信息："+itemStyle.toString();
			}
		}

		public static class Link {
			public String source;
			public String target;
			public Label label;
			public LineStyle lineStyle;
			public int[] symbolSize = new int[] { 5, 10 };

			public Link(String source, String target, String content, String color) {
				this.source = source;
				this.target = target;
				this.label = new Label();
				this.label.normal.formatter = content;
				this.lineStyle = new LineStyle();
				this.lineStyle.normal.color = color;
				this.label.normal.color = color;
			}

			public void update(String content, String color) {
				this.label.normal.formatter = content;
				this.lineStyle.normal.color = color;
				this.label.normal.color = color;
			}

			@Override
			public String toString() {
				return "源点："+source+"；终点："+target;
			}
		}

		public static class Animation {
			public String type;
			public String action;
			public Link link;
			public Data data;

			public Animation() {

			}

			public Animation(String action, String type, Link link, Data data) {
				this.type = type;
				this.link = link;
				this.data = data;
				this.action = action;
			}

			@Override
			public String toString() {
				if(link != null)
					return "重放类型："+type+"；重放事件："+action+"；对应操作边："+link;
				if(data != null)
					return "重放类型："+type+"；重放事件："+action+"；对应操作点："+data;

				return "";
			}
		}
	}
}