package effect.java.enums.item37;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Plant {

	enum LifeCycle {
		ANNUAL, PERENNIAL, BIENNIAL
	}

	final String name;
	final LifeCycle lifeCycle;

	Plant(String name, LifeCycle lifeCycle) {
		this.name = name;
		this.lifeCycle = lifeCycle;
	}

	@Override
	public String toString() {
		return name;
	}

	public void organize(List<Plant> garden) {
		Set<Plant>[] plantsByLifeCycle = (Set<Plant>[]) new Set[Plant.LifeCycle.values().length];
		for (int i = 0; i < plantsByLifeCycle.length; i++) {
			plantsByLifeCycle[i] = new HashSet<>();
		}

		for (Plant p : garden) {
			plantsByLifeCycle[p.lifeCycle.ordinal()].add(p);
		}

		for (int i = 0; i < plantsByLifeCycle.length; i++) {
			System.out.printf("%S: %s%n", Plant.LifeCycle.values()[i], plantsByLifeCycle[i]);
		}
	}

	public void organizeUsingEnumMap(List<Plant> garden) {
		
		Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle = new EnumMap<>(Plant.LifeCycle.class);
		for (Plant.LifeCycle lc : Plant.LifeCycle.values()) {
			plantsByLifeCycle.put(lc, new HashSet<>());
		}

		for (Plant p : garden) {
			plantsByLifeCycle.get(p.lifeCycle).add(p);
		}
		System.out.println(plantsByLifeCycle);
	}

}
