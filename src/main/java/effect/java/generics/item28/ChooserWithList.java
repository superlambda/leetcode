package effect.java.generics.item28;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ChooserWithList<T> {

	private final List<T> choiceList;

	public Object choose() {
		Random rnd = ThreadLocalRandom.current();
		return choiceList.get(rnd.nextInt(choiceList.size()));
	}

	public ChooserWithList(Collection<T> choices) {
		choiceList = new ArrayList<>(choices);
	}

}
