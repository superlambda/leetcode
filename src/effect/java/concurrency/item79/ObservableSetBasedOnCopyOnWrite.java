package effect.java.concurrency.item79;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import effect.java.classandinterfaces.ForwardingSet;

public class ObservableSetBasedOnCopyOnWrite<E> extends ForwardingSet<E> {

	public ObservableSetBasedOnCopyOnWrite(Set<E> set) {
		super(set);
	}

	private final List<SetObserverV2<E>> observers = new CopyOnWriteArrayList<>();

	public void addObserver(SetObserverV2<E> observer) {
		observers.add(observer);
	}

	public void removeObserver(SetObserverV2<E> observer) {
		observers.remove(observer);
	}

	private void notifyElementAdded(E element) {
		
		for(SetObserverV2<E> observer: observers) {
			observer.added(this, element);
		}
	}

	@Override
	public boolean add(E element) {
		boolean added = super.add(element);
		if (added) {
			notifyElementAdded(element);
		}
		return added;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = false;
		for (E element : c) {
			result |= add(element);
		}
		return result;

	}

	public static void main(String[] args) {

		ObservableSetBasedOnCopyOnWrite<Integer> set = new ObservableSetBasedOnCopyOnWrite<>(new HashSet<>());
//	    works fine
//		set.addObserver((s, e)-> System.out.println(e));
//		a bit fancier
//		set.addObserver(new SetObserverV2<>() {
//			public void added(ObservableSetBasedOnCopyOnWrite<Integer> s, Integer e) {
//				System.out.println(e);
//				if(e == 23) {
//					s.removeObserver(this);
//				}
//			}
//		});
//		something odd
		set.addObserver(new SetObserverV2<>() {
			public void added(ObservableSetBasedOnCopyOnWrite<Integer> s, Integer e) {
				System.out.println(e);
				if (e == 23) {
					ExecutorService exec = Executors.newSingleThreadExecutor();
					try {
						exec.submit(() -> s.removeObserver(this)).get();
					} catch (InterruptedException | ExecutionException ex) {
						throw new AssertionError(ex);
					} finally {
						exec.shutdown();
					}
				}

			}
		});
		for (int i = 0; i < 100; i++) {
			set.add(i);
		}

	}

}
