package effect.java.generics.item28;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ChooserWithArray<T> {
	
	private final T[] choiceArray;
	
	public Object choose() {
		Random rnd = ThreadLocalRandom.current();
		return choiceArray[rnd.nextInt(choiceArray.length)];
	}
	
	@SuppressWarnings("unchecked")
	public ChooserWithArray(Collection<T> choices) {
		choiceArray = (T[]) choices.toArray();
	}

}
