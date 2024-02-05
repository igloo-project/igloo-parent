package igloo.wicket.convert;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.util.convert.IConverter;

/**
 * Fix enum class determination for enum constants with constant-specific class bodies.
 * 
 * <p>(Example: <code>getClass()</code> returns <code>EnumType$1</code> if enum does have a class body, but we want to perform
 * convertor lookup based on <code>EnumType</code>).
 * 
 * @see Enum#getDeclaringClass()
 */
public class EnumClassAwareConverterLocator implements IConverterLocator {

	private static final long serialVersionUID = 1L;

	private final IConverterLocator delegate;

	public EnumClassAwareConverterLocator(IConverterLocator delegate) {
		this.delegate = delegate;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (Enum.class.isAssignableFrom(type) && type.getSuperclass() != Enum.class) {
			return (IConverter<C>) delegate.getConverter(type.getSuperclass()); // NOSONAR
		} else {
			return delegate.getConverter(type);
		}
	}

}
