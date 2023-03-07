package effect.java.enums.item38;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BasicOperation implements Operation {
	PLUS("+") {
		public double apply(double x, double y) {
			return x + y;
		}
	},
	MINUS("-") {
		public double apply(double x, double y) {
			return x - y;
		}
	},
	TIMES("*") {
		public double apply(double x, double y) {
			return x * y;
		}
	},
	DIVIDE("/") {
		public double apply(double x, double y) {
			return x / y;
		}
	};

	private final String symbol;

	BasicOperation(String symbol) {
		this.symbol = symbol;

	}

	@Override
	public String toString() {
		return symbol;
	}
	
	private static final Map<String, BasicOperation> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Object::toString, e -> e));
	
	
	public static Optional<BasicOperation> fromString(String symbol) {
		return Optional.ofNullable(stringToEnum.get(symbol));
	}
	
	public static void main(String[] args) {
		System.out.println(stringToEnum);
	}

}
