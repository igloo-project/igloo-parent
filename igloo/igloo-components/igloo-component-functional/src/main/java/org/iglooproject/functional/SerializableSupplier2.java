package org.iglooproject.functional;

import org.danekja.java.util.function.serializable.SerializableSupplier;

@FunctionalInterface
public interface SerializableSupplier2<T> extends Supplier2<T>, SerializableSupplier<T> {

}
