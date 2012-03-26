package examples.neo4j;

import java.io.IOException;

import com.google.common.collect.Iterables;

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
	public static void theMain(String[] args) {

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

	// To be used in search.
	private FifteenPuzzleState start, goal;

	// Graph Key fields.
	private static String STATE_KEY = "state";
	private static String NEIGHBOURS_KEY = "neighbours";
	private static String PARENT_KEY = "parent";

	// Graph indexer.
	private static Index<Node> nodeIndex;

	/**
	 * First method.
	 */
	public void init() throws IOException {

		// Set up and execute the search.
		doSearch();

		// Setup and initialise the Database.
		makeDB();

		// Populate the database with the data.
		populateDB();

		createRelationships();

	}

	private void createRelationships() {

		// Get all of the nodes and place them into an array, we could stack
		// all of this into one line, but it may be a bit too hard to read.
		Iterable<Node> nodes = Iterables.skip(graphDb.getAllNodes(), 1);

		Node[] nodeArray = Iterables.toArray(nodes, Node.class);

		nodes = null; // An attempt to free up space.

		createNeighbourRelationships(nodeArray);

		System.out.println("allnodes size: " + nodeArray.length);

	}

	/**
	 * Method to create relationships between each node and its neighbours.
	 * 
	 * @param nodeArray
	 */
	private void createNeighbourRelationships(Node[] nodeArray) {

		// TODO FML comment this out.
		Transaction tx = graphDb.beginTx();
		try {

			for (Node n : nodeArray) {

				String neighboursString = n.getProperty(NEIGHBOURS_KEY)
						.toString();

				// Create a string array which is the size of how many
				// neighbours
				// there will be (it is the first thing in the neighbours
				// property).
				String[] neighbours = new String[Integer
						.parseInt(neighboursString.substring(0,
								neighboursString.indexOf("\n")))];

				neighboursString = neighboursString.substring(neighboursString
						.indexOf("\n") + 1);

				// Find the fifth occurrence of a character.
				for (int i = 0; i < neighbours.length; i++) {
					neighbours[i] = neighboursString.substring(0,
							neighboursString.indexOf("\n\n") + 1);
					neighboursString = neighboursString
							.substring(neighboursString.indexOf("\n\n") + 2);
				}

				for (String s : neighbours) {

					Node neighbour = nodeIndex.get(STATE_KEY, s).getSingle();

					if (neighbour != null)
						n.createRelationshipTo(neighbour, RelTypes.NEIGHBOUR);

				}

			}
			tx.success();

		} finally {
			tx.finish();
		}

	}

	private static enum RelTypes implements RelationshipType {
		PARENT, NEIGHBOUR, NOT_CONNECTED, USERS_REFERENCE
	}

	/**
	 * Fourth Method TODO Make these better (Titles).
	 */
	private void populateDB() {

		Collection<FifteenPuzzleState> allNodes = bimax.getNodes();

		Transaction tx = graphDb.beginTx();
		try {
			// Mutations etc go in here.

			// Add the nodes into the database.
			for (FifteenPuzzleState s : allNodes) {

				Node node = graphDb.createNode();

				// Check if the node is the start state. TODO Maybe take this
				// out.
				if (s.equals(start)) {
					graphDb.getReferenceNode().createRelationshipTo(node,
							RelTypes.USERS_REFERENCE);
				}

				// Set the properties of each node and index it based on each
				// index. TODO maybe we dont need EACH property to be indexed.

				node.setProperty(STATE_KEY, s.stateToString());
				nodeIndex.add(node, STATE_KEY, s.stateToString());

				// TODO make something to split these easier.
				node.setProperty(NEIGHBOURS_KEY, s.neighboursToString());
				nodeIndex.add(node, NEIGHBOURS_KEY, s.neighboursToString());

				node.setProperty(PARENT_KEY, s.parentToString());
				nodeIndex.add(node, PARENT_KEY, s.parentToString());
			}

			tx.success();

		} finally {
			tx.finish();
		}

	}

	private void makeDB() throws IOException {

		Properties properties = new Properties();

		URL url = Config.class.getClassLoader().getResource("neo4j.properties");

		// Load the file
		properties.load(url.openStream());

		graphDb = new EmbeddedGraphDatabase(properties.getProperty("dbpath"));

		// Populate the nodeIndex variable.
		nodeIndex = graphDb.index().forNodes("node");

		registerShutdownHook(graphDb);

		// clearDb(); TODO this could involve file deletion for swiftness.

	}

	/**
	 * Second Method.
	 */
	private void doSearch() {

		bimax = new BiMaxAlgorithm<FifteenPuzzleState>();

		// The easiest of Korf's 100 test problem set. (ref)
		int[][] startStateArray = { { 2, 12, 14, 15 }, { 1, 13, 5, 11 },
				{ 3, 10, 8, 6 }, { 0, 9, 7, 4 } };

		int[][] goalStateArray = { { 13, 9, 5, 1 }, { 14, 10, 6, 2 },
				{ 15, 11, 7, 3 }, { 0, 12, 8, 4 } };

		start = new FifteenPuzzleState(startStateArray, goalStateArray);

		goal = new FifteenPuzzleState(goalStateArray, goalStateArray);

		// Execute the search.
		bimax.search(start, goal);

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
