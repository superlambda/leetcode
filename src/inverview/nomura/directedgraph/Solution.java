package inverview.nomura.directedgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution {
	public boolean solution(int[] A, int[] B) {
		
		/* Traverse all the edges, filter that has more than 1
		 * edges from one vertice to another. 
		 */
		Map<Integer, Integer> pairMap = new HashMap<>();
		for(int i=0; i<A.length; i++) {
			if(!pairMap.containsKey(A[i])) {
				pairMap.put(A[i], B[i]);
			}else {
				return false;
			}
		}
		
		/* Connect the edges and verify the last node(node)
		 * is them same with A[0] and all the nodes 
		 * are connected(nodeSet.size()==A.length).
		 */
		Set<Integer> nodeSet = new HashSet<>();
		int node = A[0];
		int nextNode = 0;
		while(! pairMap.isEmpty()) {
			if(!nodeSet.contains(node)) {
				nodeSet.add(node);
				nextNode= pairMap.get(node);
				pairMap.remove(node);
				node = nextNode;
			}else {
				if(nodeSet.size()==A.length&&node==A[0]) {
					return true;
				} else {
					return false;
				}
			}
		}
		if(nodeSet.size()==A.length&&node==A[0]) {
			return true;
		} else {
			return false;
		}
	}

}
