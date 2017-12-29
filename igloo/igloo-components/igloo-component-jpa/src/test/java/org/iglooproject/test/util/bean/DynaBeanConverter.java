package org.iglooproject.test.util.bean;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;

import com.google.common.base.Function;

/**
 * <p>Convert from a {@link DynaBean} to a POJO</p>.
 * 
 * @author Laurent Almeras
 */
public class DynaBeanConverter<T> implements Function<DynaBean, T> {

	private final Class<T> targetType;

	public DynaBeanConverter(Class<T> targetType) {
		super();
		this.targetType = targetType;
	}

	@Override
	public T apply(DynaBean input) {
		if (input == null) {
			return null;
		}
		
		try {
			T newInstance = targetType.newInstance();
			BeanUtils.copyProperties(newInstance, input);
			return newInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(
					String.format("newInstance must be available on %s", targetType.getSimpleName()), e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(
					String.format("error copying properties on %s", targetType.getSimpleName()), e);
		}
	}

	public DynaBeanConverter<T> as(Class<T> targetType) {
		return new DynaBeanConverter<>(targetType);
	}

}
