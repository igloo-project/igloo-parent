package fr.openwide.core.jpa.querydsl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang3.EnumUtils;

import com.google.common.base.Function;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;

public final class SqlExpressions {
	
	private SqlExpressions() {
	}
	
	public static <E extends Enum<E>> Expression<E> enumFromName(final Class<E> clazz, Expression<String> expression) {
		return Expressions2.fromFunction(String.class, expression, clazz, new Function<String, E>() {
			@Override
			public E apply(String input) {
				return input != null ? EnumUtils.getEnum(clazz, input) : null;
			}
		});
	}
	
	/**
	 * Allows Hibernate to guess the parameter types, especially inside a CASE statement.
	 */
	public static <E extends Enum<E>> Expression<E> enumLiteral(E value) {
		checkNotNull(value);
		return Expressions.template(value.getDeclaringClass(), value.getDeclaringClass().getName() + "." + value.name());
	}

}
