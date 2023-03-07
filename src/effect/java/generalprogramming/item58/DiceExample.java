package effect.java.generalprogramming.item58;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

public class DiceExample {
	
	enum Face {
		ONE, TWO, THREE, FOUR, FIVE, SIX
	}
	public static void main(String[] args) {
		Collection<Face> faces = EnumSet.allOf(Face.class);
		for(Iterator<Face> i = faces.iterator(); i.hasNext();) {
			Face suit = i.next();
			for(Iterator<Face> j = faces.iterator(); j.hasNext();) {
//				System.out.println(i.next() + " " + j.next());
				System.out.println(suit + " " + j.next());
			}	
		}
	}

}
