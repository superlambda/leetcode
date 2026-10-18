package peter.liu.clonegraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clone an undirected graph. Each node in the graph contains a label and a list
 * of its neighbors.
 * 
 * 
 * OJ's undirected graph serialization: Nodes are labeled uniquely.
 * 
 * We use # as a separator for each node, and , as a separator for node label
 * and each neighbor of the node. As an example, consider the serialized graph
 * {0,1,2#1,2#2,2}.
 * 
 * The graph has a total of three nodes, and therefore contains three parts as
 * separated by #.
 * 
 * First node is labeled as 0. Connect node 0 to both nodes 1 and 2. Second node
 * is labeled as 1. Connect node 1 to node 2. Third node is labeled as 2.
 * Connect node 2 to node 2 (itself), thus forming a self-cycle. Visually, the
 * graph looks like the following:
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	private Map<Integer,UndirectedGraphNode> map=new HashMap<>();
	private Map<Integer,UndirectedGraphNode> labelMap=new HashMap<>();
	public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
		if(node==null){
			return null;
		}
		if(!map.containsKey(node.label)){
			UndirectedGraphNode newNode=null;
			if(!labelMap.containsKey(node.label)){
				newNode=new UndirectedGraphNode(node.label);
				labelMap.put(newNode.label, newNode);
			}else{
				newNode=labelMap.get(node.label);
			}
			map.put(newNode.label, newNode);
			for(UndirectedGraphNode neighbor: node.neighbors){
				if(!labelMap.containsKey(neighbor.label)){
					UndirectedGraphNode newNeighborNode=new UndirectedGraphNode(neighbor.label);
					labelMap.put(neighbor.label, newNeighborNode);
					newNode.neighbors.add(newNeighborNode);
				}else{
					newNode.neighbors.add(labelMap.get(neighbor.label));
				}
				
			}
			for(UndirectedGraphNode neighbor: node.neighbors){
				cloneGraph(neighbor);
				
			}
			
			return newNode;
		}
		return null;
	}
}

// Definition for undirected graph.
class UndirectedGraphNode {
	int label;
	List<UndirectedGraphNode> neighbors;

	UndirectedGraphNode(int x) {
		label = x;
		neighbors = new ArrayList<UndirectedGraphNode>();
	}
}
