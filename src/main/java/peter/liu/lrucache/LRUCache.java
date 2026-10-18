package peter.liu.lrucache;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

	final Node head = new Node();
	final Node tail = new Node();

	private Map<Integer, Node> cache;
	private int capacity;

	public LRUCache(int capacity) {
		this.cache = new HashMap<>(capacity);
		this.capacity = capacity;
		head.next = tail;
		tail.prev=head;

	}
	public int get(int key) {
		int result = -1;
		if (cache.containsKey(key)) {
			Node current = cache.get(key);
			remove(current);
			add(current);
			result = current.value;
		}
		return result;

	}
	public void put(int key, int value) {
		Node node = cache.get(key);
		if (node != null) {
			remove(node);
			node.value = value;
			add(node);
		} else {
			if (cache.size() == this.capacity) {
				cache.remove(tail.prev.key);
				remove(tail.prev);
			}
			Node newNode = new Node();
			newNode.key = key;
			newNode.value = value;
			cache.put(key, newNode);
			add(newNode);
		}
	}

	public void add(Node node) {
		Node headNext = head.next;
		head.next = node;
		node.prev = head;
		node.next = headNext;
		headNext.prev = node;
	}

	public void remove(Node node) {
		Node nextNode = node.next;
		Node prevNode = node.prev;
		nextNode.prev = prevNode;
		prevNode.next = nextNode;
	}

}

class Node {
	int key;
	int value;
	Node prev;
	Node next;
}
