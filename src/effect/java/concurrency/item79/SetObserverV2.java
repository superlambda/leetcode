package effect.java.concurrency.item79;

@FunctionalInterface
public interface SetObserverV2<E>{
	
	void added(ObservableSetBasedOnCopyOnWrite<E> set, E element);

}
