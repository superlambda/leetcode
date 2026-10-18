package effect.java.enums.item34;

public class WeightTable {

	public static void main(String[] args) {
		double mass = 185/Plant.EARTH.surfaceGravity();
		for (Plant p: Plant.values()) {
			System.out.printf("Weight on %s is %f%n",p, p.surfaceWeight(mass));
		}

	}

}
