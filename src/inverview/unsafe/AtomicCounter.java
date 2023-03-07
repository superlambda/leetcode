package inverview.unsafe;

import java.lang.reflect.Field;

import org.w3c.dom.css.Counter;

public final class AtomicCounter implements Counter {
    private static final Unsafe unsafe;
    private static final long valueOffset;

    private volatile int value = 0;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            valueOffset = unsafe.objectFieldOffset
                (AtomicCounter.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    @Override
    public int increment() {
        return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
    }

    @Override
    public int get() {
        return value;
    }
}
