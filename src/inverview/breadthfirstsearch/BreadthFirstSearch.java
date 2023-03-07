package inverview.breadthfirstsearch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class BreadthFirstSearch {

	public List<Node> bfs(Node root) {
		Queue<Node> queue = new LinkedBlockingQueue<Node>();
		List<Node> result = new ArrayList<Node>();
		queue.add(root);
		while (!queue.isEmpty()) {
			Node first = queue.poll();
			result.add(first);
			List<Node> children = first.getChildren();
			if (children != null && children.size() > 0) {
				queue.addAll(children);
			}
		}
		return result;

	}

	public static void main(String[] args) {
		List<Node> children= new ArrayList<>();
		children.add(new Node(4,null));
		children.add(new Node(5,null));
		children.add(new Node(6,null));
		Node two = new Node(2,children);
		
		children= new ArrayList<>();
		children.add(new Node(7,null));
		children.add(new Node(8,null));
		children.add(new Node(9,null));
		Node three = new Node(3,children);
		
		children= new ArrayList<>();
		children.add(two);
		children.add(three);
		Node one = new Node(1,children);
		
		BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();
		List<Node> result = breadthFirstSearch.bfs(one);
		result.forEach(System.out::println);
	}

}

class Node {
	private int value;
	private List<Node> children = null;

	Node(int value, List<Node> children) {
		this.value = value;
		this.children = new LinkedList<>();
		if (children != null && children.size() > 0) {
			this.children.addAll(children);
		}
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(this.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return value == other.value;
	}

	@Override
	public String toString() {
		return "Node [value=" + value + "]";
	}

}
