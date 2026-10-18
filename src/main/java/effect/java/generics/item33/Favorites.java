package effect.java.generics.item33;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Favorites {

	private Map<Class<?>, Object> favorites = new HashMap<>();

	public <T> void putFavorite(Class<T> type, T instance) {
//		favorites.put(Objects.requireNonNull(type), instance);
		favorites.put(Objects.requireNonNull(type), type.cast(instance));
	}

	public <T> T getFavorite(Class<T> type) {
		return type.cast(favorites.get(type));
	}

	public static void main(String[] args) {
		Favorites f = new Favorites();
		f.putFavorite(String.class, "java");
		f.putFavorite(Integer.class, 0xcafebabe);
		f.putFavorite(Class.class, Favorites.class);

		String favoriteString = f.getFavorite(String.class);
		int favoriteInteger = f.getFavorite(Integer.class);
		Class<?> favoriteClass = f.getFavorite(Class.class);
		System.out.printf("%s %x %s%n", favoriteString, favoriteInteger, favoriteClass.getName());

	}
	
	static Annotation getAnnotation(AnnotatedElement element, String annotationTypeName) {
		Class<?> annotationType = null;
		
		try {
			annotationType = Class.forName(annotationTypeName);
		} catch(Exception ex) {
			throw new IllegalArgumentException(ex);
		}
//		return element.getAnnotation(annotationType.asSubclass(Annotation.class));
		return element.getAnnotation(annotationType.asSubclass(Annotation.class));
	}

}
