package fr.openwide.core.commons.util.ordering;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Ordering;

/**
 * A wrapper class providing {@link Collator}-like functionnality in a {@link Serializable} object.
 * <p>This class extends {@link Ordering}.
 * <p><em>Note:</em> Collators themselves are <strong>not</strong> Serializable
 */
public final class SerializableCollator extends Ordering<String> implements Serializable {
	
	private static final long serialVersionUID = -7694744450385630563L;

	private final Locale locale;
	
	private transient Collator collator;

	/**
	 * @see Collator#getInstance(Locale)
	 */
	public SerializableCollator(Locale locale) {
		this.locale = locale;
		initCollator();
	}
	
	private void initCollator() {
		this.collator = Collator.getInstance(locale);
	}
	
	/**
	 * @see Collator#getStrength()
	 */
	public int getStrength() {
		return collator.getStrength();
	}

	/**
	 * @see Collator#setStrength(int)
	 */
	public void setStrength(int strength) {
		collator.setStrength(strength);
	}

	/**
	 * @see Collator#getDecomposition()
	 */
	public int getDecomposition() {
		return collator.getDecomposition();
	}

	/**
	 * @see Collator#setDecomposition(int)
	 */
	public void setDecomposition(int decomposition) {
		collator.setDecomposition(decomposition);
	}

	/**
	 * @see Collator#compare(String, String)
	 */
	@Override
	public int compare(final String left, final String right) {
		return collator.compare(left, right);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SerializableCollator)) {
			return false;
		}
		SerializableCollator other = (SerializableCollator) obj;
		return new EqualsBuilder()
				.append(locale, other.locale)
				.append(collator, other.collator)
				.build();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(locale)
				.append(collator)
				.build();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append(locale)
				.append(collator)
				.build();
	}

	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeInt(collator.getStrength());
		out.writeInt(collator.getDecomposition());
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		initCollator();
		setStrength(in.readInt());
		setDecomposition(in.readInt());
	}

}
