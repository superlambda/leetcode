package inverview.spliterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

public class SpliteratorTest {

	public static void main(String[] args) {
		// Create an object of array list
		ArrayList<Integer> list = new ArrayList<>();
		// Add values to the array list.
		list.add(101);
		list.add(201);
		list.add(301);
		list.add(401);
		list.add(501);
		// Get Spliterator object on list
		Spliterator<Integer> splitr = list.stream().spliterator();
		// Get estimateSize method
		System.out.println("Estimate size: " + splitr.estimateSize());
		// Print getExactSizeIfKnown method
		System.out.println("Exact size: " + splitr.getExactSizeIfKnown());
		// Check for hasCharacteristics and characteristics method
//		System.out.println("Characteristics: " + splitr.characteristics());
		
		if (splitr.hasCharacteristics(Spliterator.ORDERED)) {
		    System.out.println("ORDERED");
		}
		 
		if (splitr.hasCharacteristics(Spliterator.DISTINCT)) {
		    System.out.println("DISTINCT");
		}
		 
		if (splitr.hasCharacteristics(Spliterator.SORTED)) {
		    System.out.println("SORTED");
		}
		 
		if (splitr.hasCharacteristics(Spliterator.SIZED)) {
		    System.out.println("SIZED");
		}
		 
		if (splitr.hasCharacteristics(Spliterator.CONCURRENT)) {
		    System.out.println("CONCURRENT");
		}
		 
		if (splitr.hasCharacteristics(Spliterator.IMMUTABLE)) {
		    System.out.println("IMMUTABLE");
		}
		 
		if (splitr.hasCharacteristics(Spliterator.NONNULL)) {
		    System.out.println("NONNULL");
		}
		 
		if (splitr.hasCharacteristics(Spliterator.SUBSIZED)) {
		    System.out.println("SUBSIZED");
		}
		int expected = Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
		System.out.println("expected: " + expected + " Spliterator.ORDERED: " + Spliterator.ORDERED);
		
		
		
		System.out.println("Boolean Result: " + splitr.hasCharacteristics(splitr.characteristics()));
		System.out.println("Elements of ArrayList :");
		// Obtain result forEachRemaining method
		splitr.forEachRemaining(System.out::println);
		// Obtaining another Stream to the array list.
		Stream<Integer> str1 = list.stream();
		splitr = str1.spliterator();
		// Obtain result from trySplit() method
		Spliterator<Integer> splitr2 = splitr.trySplit();
		// If splitr could be split, use splitr2 first.
		if (splitr2 != null) {
			System.out.println("Output from splitr2: ");
			splitr2.forEachRemaining(System.out::println);
		}
		// Now, use the splitr
		System.out.println("Output from splitr1: ");
		splitr.forEachRemaining(System.out::println);
		
		List<String> fruitList = Arrays.asList("Mango", "Banana", "Apple");  
        Spliterator<String> splitr3 = fruitList.spliterator();  
        System.out.println("List of Fruit name-");  
        while(splitr3.tryAdvance((num) -> System.out.println("" +num)));  

	}

}
