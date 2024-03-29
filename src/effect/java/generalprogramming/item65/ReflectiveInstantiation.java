package effect.java.generalprogramming.item65;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class ReflectiveInstantiation {

	public static void main(String[] args) {
		Class<? extends Set<String>> cl = null;

		try {
			cl = (Class<? extends Set<String>>) Class.forName(args[0]);
		} catch (ClassNotFoundException e) {
			fatalError("Class not found.");
		}
		Constructor<? extends Set<String>> cons = null;

		try {
			cons = cl.getDeclaredConstructor();
		} catch (NoSuchMethodException e) {
			fatalError("No parameterless constructor");
		}

		Set<String> s = null;
		try {
			s = cons.newInstance();
		} catch (IllegalAccessException e) {
			fatalError("Constructor not accessible");
		} catch (InstantiationException e) {
			fatalError("Class not instantiable");
		} catch (IllegalArgumentException e) {
			fatalError("Illegal argument");
		} catch (InvocationTargetException e) {
			fatalError("Constructor threw " + e.getCause());
		}
		s.addAll(Arrays.asList(args).subList(1, args.length));
		System.out.println(s);

	}

	private static void fatalError(String msg) {
		System.err.println(msg);
		System.exit(1);
	}

}
