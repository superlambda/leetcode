package effect.java.concurrency.item79;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import effect.java.classandinterfaces.ForwardingSet;

public class ObservableSet<E> extends ForwardingSet<E> {

	public ObservableSet(Set<E> set) {
		super(set);
	}

	private final List<SetObserver<E>> observers = new ArrayList<>();

	public void addObserver(SetObserver<E> observer) {
		synchronized (observers) {
			observers.add(observer);
		}
	}

	public void removeObserver(SetObserver<E> observer) {
		synchronized (observers) {
			observers.remove(observer);
		}
	}

	private void notifyElementAdded(E element) {
		List<SetObserver<E>> snapshot = null;
		
		synchronized (observers) {
			snapshot = new ArrayList<>(observers); 
		}
		
		for(SetObserver<E> observer: snapshot) {
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

		ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
//	    works fine
//		set.addObserver((s, e)-> System.out.println(e));
//		a bit fancier
		set.addObserver(new SetObserver<>() {
			public void added(ObservableSet<Integer> s, Integer e) {
				System.out.println(e);
				if(e == 23) {
					s.removeObserver(this);
				}
			}
		});
//		something odd
//		set.addObserver(new SetObserver<>() {
//			public void added(ObservableSet<Integer> s, Integer e) {
//				System.out.println(e);
//				if (e == 23) {
//					ExecutorService exec = Executors.newSingleThreadExecutor();
//					try {
//						exec.submit(() -> s.removeObserver(this)).get();
//					} catch (InterruptedException | ExecutionException ex) {
//						throw new AssertionError(ex);
//					} finally {
//						exec.shutdown();
//					}
//				}
//
//			}
//		});
		for (int i = 0; i < 100; i++) {
			set.add(i);
		}

	}

}
