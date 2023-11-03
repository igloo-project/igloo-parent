package igloo.wicket.spring;

import org.apache.wicket.model.IDetachable;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

public class SpringBeanLookupCache<T> implements IDetachable {

	private static final long serialVersionUID = 1L;

	private final SerializableFunction2<ApplicationContext, T> supplier;

	private final SerializableSupplier2<ApplicationContext> contextSupplier;

	private transient ApplicationContext context;
	private transient T cache;

	public SpringBeanLookupCache(SerializableSupplier2<ApplicationContext> contextSupplier, SerializableFunction2<ApplicationContext, T> supplier) {
		this.supplier = supplier;
		this.contextSupplier = contextSupplier;
	}

	public T get() {
		if (context == null) {
			context = contextSupplier.get();
		}
		if (cache == null) {
			cache = supplier.apply(context);
		}
		return cache;
	}

	public static <T> SpringBeanLookupCache<T> of(SerializableSupplier2<ApplicationContext> contextSupplier, Class<?> type, Class<?>... generics) {
		return new SpringBeanLookupCache<>(contextSupplier, c -> c.<T>getBeanProvider(ResolvableType.forClassWithGenerics(type, generics)).getIfUnique());
	}

	@Override
	public void detach() {
		cache = null;
		context = null;
	}

}
