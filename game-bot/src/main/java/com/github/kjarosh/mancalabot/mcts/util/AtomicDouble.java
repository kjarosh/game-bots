package com.github.kjarosh.mancalabot.mcts.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Kamil Jarosz
 */
public class AtomicDouble extends Number implements Serializable {
    private final AtomicLong delegate = new AtomicLong();

    public AtomicDouble(double value) {
        set(value);
    }

    public final double get() {
        return Double.longBitsToDouble(delegate.get());
    }

    public final void set(double value) {
        delegate.set(Double.doubleToLongBits(value));
    }

    public final void addAndGet(double value) {
        boolean success;
        do {
            long expected = delegate.get();
            long newValue = Double.doubleToLongBits(Double.longBitsToDouble(expected) + value);
            success = delegate.compareAndSet(expected, newValue);
        } while (!success);
    }

    @Override
    public int intValue() {
        return (int) get();
    }

    @Override
    public long longValue() {
        return (long) get();
    }

    @Override
    public float floatValue() {
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    @Override
    public String toString() {
        return Double.toString(get());
    }
}
