package fr.openwide.core.commons.util.functional.converter;

import java.util.Collection;

import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

public class StringCollectionConverter<T, C extends Collection<T>> extends Converter<String, C> {

	private final Converter<String, T> converter;

	private final Supplier<? extends C> supplier;

	private Splitter splitter;

	private Joiner joiner;

	public StringCollectionConverter(Converter<String, T> converter, Supplier<? extends C> supplier) {
		this.converter = converter;
		this.supplier = supplier;
		separator(" ");
	}

	public StringCollectionConverter<T, C> joiner(Joiner joiner) {
		Preconditions.checkNotNull(joiner);
		this.joiner = joiner;
		return this;
	}

	public StringCollectionConverter<T, C> splitter(Splitter splitter) {
		Preconditions.checkNotNull(splitter);
		this.splitter = splitter;
		return this;
	}

	public StringCollectionConverter<T, C> separator(String separator) {
		Preconditions.checkNotNull(separator);
		this.splitter = Splitter.on(separator).omitEmptyStrings().trimResults();
		this.joiner = Joiner.on(separator).skipNulls();
		return this;
	}

	@Override
	protected C doForward(String a) {
		C collection = supplier.get();
		for (String valueAsString : splitter.split(a)) {
			T value = converter.convert(valueAsString);
			if (value != null) {
				collection.add(value);
			}
		}
		return collection;
	}
	
	@Override
	protected String doBackward(C b) {
		return joiner.join(Iterables.transform(b, converter.reverse()));
	}

}
