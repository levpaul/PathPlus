package examples;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.Config;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import org.pathplus.algorithms.implementations.BiMaxAlgorithm;
import org.pathplus.utils.state.implementations.FifteenPuzzleState;

public class Neo4JExample {
	public static void main(String[] args) {

		DoNeo4JExample doExample = new DoNeo4JExample();
		try {
			doExample.init();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

// Class that actually does stuff.
class DoNeo4JExample {

	private BiMaxAlgorithm<FifteenPuzzleState> bimax;
	private EmbeddedGraphDatabase graphDb;

	public void init() throws IOException {

		System.out.println("doing search...");
		doSearch();

		System.out.println("making database...");
		makeDB();

		System.out.println("populating database...");
		populateDB();

	}

	private void populateDB() {

		Collection<FifteenPuzzleState> allNodes = bimax.getNodes();

		Transaction tx = graphDb.beginTx();

		try {
			// Mutating operations go here.
			Index<Node> nodeIndex = graphDb.index().forNodes("id");
			// Add the nodes into the database. {DONE}
			for (FifteenPuzzleState s : allNodes) {

				Node node = graphDb.createNode();

				node.setProperty("state", s.stateToString());
				node.setProperty("neighbours", s.neighboursToString());
				node.setProperty("parent", s.parentToString());

				nodeIndex.add(node, (String) node.getProperty("state"),
						(String) node.getProperty("state"));

			}

			Iterable<Node> iterator = graphDb.getAllNodes();

			for (Node n : iterator) {

				String parent = (String) n.getProperty("parent");

				System.out.println(parent);

				n.createRelationshipTo(
						nodeIndex.get((String) n.getProperty("state"),
								(String) n.getProperty("state")).getSingle(),
						RelTypes.PARENT);
			}

			tx.success();
		} finally {
			tx.finish();
		}

		System.out.println("all done... total nodes: " + allNodes.size());

	}

	private void doSearch() {

		bimax = new BiMaxAlgorithm<FifteenPuzzleState>();

		// The easiest of Korf's 100 test problem set. (ref)
		int[][] startStateArray = { { 2, 12, 14, 15 }, { 1, 13, 5, 11 },
				{ 3, 10, 8, 6 }, { 0, 9, 7, 4 } };

		int[][] goalStateArray = { { 13, 9, 5, 1 }, { 14, 10, 6, 2 },
				{ 15, 11, 7, 3 }, { 0, 12, 8, 4 } };

		FifteenPuzzleState start = new FifteenPuzzleState(startStateArray,
				goalStateArray);
		FifteenPuzzleState goal = new FifteenPuzzleState(goalStateArray,
				goalStateArray);

		// Execute the search.
		bimax.search(start, goal);

	}

	private void makeDB() throws IOException {

		Properties properties = new Properties();

		URL url = Config.class.getClassLoader().getResource("neo4j.properties");

		// Load the file
		properties.load(url.openStream());

		System.out.println(properties.getProperty("dbpath"));

		graphDb = new EmbeddedGraphDatabase(properties.getProperty("dbpath"));
		registerShutdownHook(graphDb);

	}

	private static enum RelTypes implements RelationshipType {
		PARENT, NEIGHBOUR, NOT_CONNECTED
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
}
