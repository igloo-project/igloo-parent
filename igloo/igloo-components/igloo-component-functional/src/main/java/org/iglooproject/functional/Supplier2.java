package org.iglooproject.functional;

import java.util.function.Supplier;

@FunctionalInterface
public interface Supplier2<T> extends Supplier<T>, com.google.common.base.Supplier<T> {

}
