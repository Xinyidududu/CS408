import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Stack;
import org.objectweb.asm.commons.*;
import org.objectweb.asm.tree.*;

public class CFG {
    Set<Node> nodes = new HashSet<Node>();
    Map<Node, Set<Node>> edges = new HashMap<Node, Set<Node>>();

    static class Node {
	int position;
	MethodNode method;
	ClassNode clazz;

	Node(int p, MethodNode m, ClassNode c) {
	    position = p; method = m; clazz = c;
	}

	public boolean equals(Object o) {
	    if (!(o instanceof Node)) return false;
	    Node n = (Node)o;
	    return (position == n.position) &&
		method.equals(n.method) && clazz.equals(n.clazz);
	}

	public int hashCode() {
	    return position + method.hashCode() + clazz.hashCode();
	}

	public String toString() {
	    return clazz.name + "." +
		method.name + method.signature + ": " + position;
	}
    }

    public void addNode(int p, MethodNode m, ClassNode c) {
		Node newNode = new Node(p, m, c);
		if (!nodes.contains(newNode)) {
			nodes.add(newNode);
			edges.put(newNode, new HashSet<>());
		}
    }

    public void addEdge(int p1, MethodNode m1, ClassNode c1,
			int p2, MethodNode m2, ClassNode c2) {
		Node node1 = new Node(p1, m1, c1);
		Node node2 = new Node(p2, m2, c2);

		// Add the nodes if they do not exist
		addNode(p1, m1, c1);
		addNode(p2, m2, c2);

		// Add the edge from node1 to node2
		edges.get(node1).add(node2);
    }
	
	public void deleteNode(int p, MethodNode m, ClassNode c) {
		Node nodeToRemove = new Node(p, m, c);

		// If the node exists in the graph, remove it
		if (nodes.contains(nodeToRemove)) {
			nodes.remove(nodeToRemove);    // Remove the node from nodes
			edges.remove(nodeToRemove);    // Remove all outgoing edges

			// Remove all incoming edges from other nodes
			for (Set<Node> edgeSet : edges.values()) {
				edgeSet.remove(nodeToRemove);
			}
		}
    }
	
    public void deleteEdge(int p1, MethodNode m1, ClassNode c1,
						int p2, MethodNode m2, ClassNode c2) {
		Node node1 = new Node(p1, m1, c1);
		Node node2 = new Node(p2, m2, c2);

		// Ensure node1 exists and has edges
		if (edges.containsKey(node1) && edges.containsKey(node2)) {
			edges.get(node1).remove(node2); // Remove the edge from node1 to node2
		}
    }


	public boolean isReachable(int p1, MethodNode m1, ClassNode c1,
							   int p2, MethodNode m2, ClassNode c2) {
		Node start = new Node(p1, m1, c1);
		Node end = new Node(p2, m2, c2);

		// If the start or end node doesn't exist, return false
		if (!nodes.contains(start) || !nodes.contains(end)) {
			return false;
		}

		// Stack for DFS
		Stack<Node> stack = new Stack<>();
		Set<Node> visited = new HashSet<>();  // To avoid cycles

		// Start with the start node
		stack.push(start);

		while (!stack.isEmpty()) {
			Node current = stack.pop();

			// If we reached the end node
			if (current.equals(end)) {
				return true;
			}

			// If the node is already visited, skip it
			if (visited.contains(current)) {
				continue;
			}

			// Mark the current node as visited
			visited.add(current);

			// Add all unvisited neighbors to the stack
			for (Node neighbor : edges.get(current)) {
				if (!visited.contains(neighbor)) {
					stack.push(neighbor);
				}
			}
		}
		// If no path was found
		return false;
	}

}
