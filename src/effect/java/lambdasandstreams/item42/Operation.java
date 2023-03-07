package effect.java.lambdasandstreams.item42;

import java.util.Map;
import java.util.Optional;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Operation {

	PLUS("+", (x, y) -> x + y),
	NINUS("-", (x, y) -> x - y),
	TIMES("*", (x, y) -> x * y),
	DIVIDE("/", (x, y) -> x / y);

	private final String symbol;
	private final DoubleBinaryOperator op;

	Operation(String symbol, DoubleBinaryOperator op) {
		this.symbol = symbol;
		this.op = op;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public double apply(double x, double y) {
		return op.applyAsDouble(x, y);
	}
	
	/*
	 * The simplest map collector is toMap(keyMapper, valueMapper) which takes two functions,
	 * one of which maps a stream element to a key, the other, to a value.
	 * 
	 * We used this collector to make a map from the string form of an enum to the enum itself.
	 * 
	 * The simple form of toMap is perfect if each element in the stream maps to a unique key. If multiple stream elements
	 * map to the same key, the pipeline will terminate with an IllegalStateException.
	 */
	private static final Map<String, Operation> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Object::toString, e -> e));
	
	
	public static Optional<Operation> fromString (String symbol) {
		return Optional.ofNullable(stringToEnum.get(symbol));
	}

}
