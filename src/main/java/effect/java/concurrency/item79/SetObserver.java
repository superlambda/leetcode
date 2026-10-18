package effect.java.concurrency.item79;

@FunctionalInterface
public interface SetObserver<E>{
	
	void added(ObservableSet<E> set, E element);

}
