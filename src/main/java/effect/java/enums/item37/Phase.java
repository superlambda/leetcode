package effect.java.enums.item37;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Phase {

	SOLID, LIQUID, GAS, PLASMA;

	public enum Transition {

		MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID), BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID), SUBLIME(SOLID, GAS),
		DEPOSIT(GAS, SOLID), IONIZE(GAS, PLASMA), DEIONIZE(PLASMA, GAS);

		private final Phase from;
		private final Phase to;

		Transition(Phase from, Phase to) {
			this.from = from;
			this.to = to;
		}

		private static final Map<Phase, Map<Phase, Transition>> m = Stream.of(values())
				.collect(Collectors.groupingBy(
						t -> t.from,
						() -> new EnumMap<>(Phase.class),
						Collectors.toMap(t -> t.to, t -> t, (x, y) -> y, () -> new EnumMap<>(Phase.class))
						)
				);

		public static Transition from(Phase from, Phase to) {
			return m.get(from).get(to);		
			}
	}
	public static void main(String[] args) {
		System.out.println(Transition.m);
//		System.out.println(Transition.from(SOLID, LIQUID));
//		System.out.println(Transition.from(LIQUID, SOLID));
//		System.out.println(Transition.from(LIQUID, GAS));
//		System.out.println(Transition.from(GAS, LIQUID));
//		System.out.println(Transition.from(SOLID, GAS));
//		System.out.println(Transition.from(GAS, SOLID));
//		System.out.println(Transition.from(GAS, PLASMA));
//		System.out.println(Transition.from(PLASMA, GAS));
	}
}
