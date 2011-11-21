package fr.openwide.core.wicket.more.util;

import java.io.Serializable;

public interface NumberAdapter<T extends Number> extends Serializable {

	T convert(Number number);

	Class<T> getNumberClass();
}
