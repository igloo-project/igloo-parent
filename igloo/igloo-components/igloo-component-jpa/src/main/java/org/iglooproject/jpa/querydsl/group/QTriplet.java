package org.iglooproject.jpa.querydsl.group;

import org.javatuples.Triplet;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;

/**
 * A triplet of (Table) row, column and value
 *
 * @param <R> Table row type
 * @param <C> Table column type
 * @param <V> Table value type
 */
 public final class QTriplet<R, C, V> extends ConstructorExpression<Triplet<R, C, V>> {

    private static final long serialVersionUID = -1943990903548916056L;

    public static <R, C, V> QTriplet<R, C, V> create(Expression<R> row, Expression<C> column, Expression<V> value) {
        return new QTriplet<>(row, column, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    public QTriplet(Expression<R> row, Expression<C> column, Expression<V> value) {
        super((Class) Triplet.class, new Class<?>[]{Object.class, Object.class, Object.class}, row, column, value);
    }

    public boolean equals(Expression<?> rowExpr, Expression<?> columnExpr, Expression<?> valueExpr) {
        return getArgs().get(0).equals(rowExpr) && getArgs().get(1).equals(columnExpr)
        		&& getArgs().get(2).equals(valueExpr) ;
    }

    public boolean equals(Expression<?> rowExpr, Expression<?> columnExpr, Class<?> valueType) {
        return getArgs().get(0).equals(rowExpr) && getArgs().get(1).equals(columnExpr)
        		&& valueType.isAssignableFrom(getArgs().get(2).getType());
    }

}