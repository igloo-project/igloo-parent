package org.iglooproject.jpa.querydsl.group;

import java.util.Comparator;

import org.javatuples.Triplet;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.Table;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.group.AbstractGroupExpression;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.types.Expression;

public final class GroupBy2 {

	private GroupBy2() {
	}
	
	public static <T> ResultTransformer<T> transformer(GroupExpression<?, T> groupExpression) {
		return new GroupExpressionResultTransformer<>(groupExpression);
	}

	/**
	 * @see #table(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C, V> AbstractGroupExpression<Triplet<R, C, V>, Table<R, C, V>> table(
			Expression<R> row, Expression<C> column, Expression<V> value) {
		return GTable.createLinked(QTriplet.create(row, column, value));
	}

	/**
	 * @see #table(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C, V, R2> AbstractGroupExpression<Triplet<R, C, V>, Table<R2, C, V>> table(
			GroupExpression<R, R2> row, Expression<C> column, Expression<V> value) {
		return table(row, new GOne<C>(column), new GOne<V>(value));
	}

	/**
	 * @see #table(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C, V, C2> AbstractGroupExpression<Triplet<R, C, V>, Table<R, C2, V>> table(
			Expression<R> row, GroupExpression<C, C2> column, Expression<V> value) {
		return table(new GOne<R>(row), column, new GOne<V>(value));
	}

	/**
	 * @see #table(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C, V, R2, C2> AbstractGroupExpression<Triplet<R, C, V>, Table<R2, C2, V>> table(
			GroupExpression<R, R2> row, GroupExpression<C, C2> column, Expression<V> value) {
		return table(row, column, new GOne<V>(value));
	}

	/**
	 * @see #table(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C, V, V2> AbstractGroupExpression<Triplet<R, C, V>, Table<R, C, V2>> table(
			Expression<R> row, Expression<C> column, GroupExpression<V, V2> value) {
		return table(new GOne<R>(row), new GOne<C>(column), value);
	}

	/**
	 * @see #table(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C, V, R2, V2> AbstractGroupExpression<Triplet<R, C, V>, Table<R2, C, V2>> table(
			GroupExpression<R, R2> row, Expression<C> column, GroupExpression<V, V2> value) {
		return table(row, new GOne<C>(column), value);
	}

	/**
	 * @see #table(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C, V, C2, V2> AbstractGroupExpression<Triplet<R, C, V>, Table<R, C2, V2>> table(
			Expression<R> row, GroupExpression<C, C2> column, GroupExpression<V, V2> value) {
		return table(new GOne<R>(row), column, value);
	}

	/**
	 * Create a new aggregating table expression using a backing HashBasedTable
	 *
	 * @param row
	 *            row for the table entries
	 * @param column
	 *            column for the table entries
	 * @param value
	 *            value for the table entries
	 * @return wrapper expression
	 */
	public static <R, C, V, R2, C2, V2> AbstractGroupExpression<Triplet<R, C, V>, Table<R2, C2, V2>> table(
			GroupExpression<R, R2> row, GroupExpression<C, C2> column, GroupExpression<V, V2> value) {
		return new GTable.Mixin<>(
				row, column, value,
				GTable.createLinked(QTriplet.create(row, column, value))
		);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R extends Comparable<? super R>, C extends Comparable<? super C>, V>
			AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R, C, V>> sortedTable(
			Expression<R> row, Expression<C> column, Expression<V> value) {
		return GTable.createSorted(QTriplet.create(row, column, value));
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C extends Comparable<? super C>, V, R2 extends Comparable<? super R2>>
			AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R2, C, V>> sortedTable(
			GroupExpression<R, R2> row, Expression<C> column, Expression<V> value) {
		return sortedTable(row, new GOne<C>(column), new GOne<V>(value));
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R extends Comparable<? super R>, C, V, C2 extends Comparable<? super C2>>
			AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R, C2, V>> sortedTable(
			Expression<R> row, GroupExpression<C, C2> column, Expression<V> value) {
		return sortedTable(new GOne<R>(row), column, new GOne<V>(value));
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C, V, R2 extends Comparable<? super R2>, C2 extends Comparable<? super C2>>
			AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R2, C2, V>> sortedTable(
			GroupExpression<R, R2> row, GroupExpression<C, C2> column, Expression<V> value) {
		return sortedTable(row, column, new GOne<V>(value));
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R extends Comparable<? super R>, C extends Comparable<? super C>, V, V2>
			AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R, C, V2>> sortedTable(
			Expression<R> row, Expression<C> column, GroupExpression<V, V2> value) {
		return sortedTable(new GOne<R>(row), new GOne<C>(column), value);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R, C extends Comparable<? super C>, V, R2 extends Comparable<? super R2>, V2>
			AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R2, C, V2>> sortedTable(
			GroupExpression<R, R2> row, Expression<C> column, GroupExpression<V, V2> value) {
		return sortedTable(row, new GOne<C>(column), value);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression)
	 */
	public static <R extends Comparable<? super R>, C, V, C2 extends Comparable<? super C2>, V2>
			AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R, C2, V2>> sortedTable(
			Expression<R> row, GroupExpression<C, C2> column, GroupExpression<V, V2> value) {
		return sortedTable(new GOne<R>(row), column, value);
	}

	/**
	 * Create a new aggregating table expression using a backing HashBasedRowSortedTable
	 *
	 * @param row
	 *            row for the sortedTable entries
	 * @param column
	 *            column for the sortedTable entries
	 * @param value
	 *            value for the sortedTable entries
	 * @return wrapper expression
	 */
	public static <R, C, V, R2 extends Comparable<? super R2>, C2 extends Comparable<? super C2>, V2>
			AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R2, C2, V2>> sortedTable(
			GroupExpression<R, R2> row, GroupExpression<C, C2> column, GroupExpression<V, V2> value) {
		return new GTable.Mixin<>(
				row, column, value,
				GTable.createSorted(QTriplet.create(row, column, value))
		);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression, Comparator, Comparator)
	 */
	public static <R, C, V> AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R, C, V>> sortedTable(
			Expression<R> row, Expression<C> column, Expression<V> value,
			Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
		return GTable.createSorted(QTriplet.create(row, column, value), rowComparator, columnComparator);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression, Comparator, Comparator)
	 */
	public static <R, C, V, R2> AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R2, C, V>> sortedTable(
			GroupExpression<R, R2> row, Expression<C> column, Expression<V> value,
			Comparator<? super R2> rowComparator, Comparator<? super C> columnComparator) {
		return sortedTable(row, new GOne<C>(column), new GOne<V>(value),
				rowComparator, columnComparator);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression, Comparator, Comparator)
	 */
	public static <R, C, V, C2> AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R, C2, V>> sortedTable(
			Expression<R> row, GroupExpression<C, C2> column, Expression<V> value,
			Comparator<? super R> rowComparator, Comparator<? super C2> columnComparator) {
		return sortedTable(new GOne<R>(row), column, new GOne<V>(value),
				rowComparator, columnComparator);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression, Comparator, Comparator)
	 */
	public static <R, C, V, R2, C2> AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R2, C2, V>> sortedTable(
			GroupExpression<R, R2> row, GroupExpression<C, C2> column, Expression<V> value,
			Comparator<? super R2> rowComparator, Comparator<? super C2> columnComparator) {
		return sortedTable(row, column, new GOne<V>(value),
				rowComparator, columnComparator);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression, Comparator, Comparator)
	 */
	public static <R, C, V, V2> AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R, C, V2>> sortedTable(
			Expression<R> row, Expression<C> column, GroupExpression<V, V2> value,
			Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
		return sortedTable(new GOne<R>(row), new GOne<C>(column), value,
				rowComparator, columnComparator);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression, Comparator, Comparator)
	 */
	public static <R, C, V, R2, V2> AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R2, C, V2>> sortedTable(
			GroupExpression<R, R2> row, Expression<C> column, GroupExpression<V, V2> value,
			Comparator<? super R2> rowComparator, Comparator<? super C> columnComparator) {
		return sortedTable(row, new GOne<C>(column), value,
				rowComparator, columnComparator);
	}

	/**
	 * @see #sortedTable(GroupExpression, GroupExpression, GroupExpression, Comparator, Comparator)
	 */
	public static <R, C, V, C2, V2> AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R, C2, V2>> sortedTable(
			Expression<R> row, GroupExpression<C, C2> column, GroupExpression<V, V2> value,
			Comparator<? super R> rowComparator, Comparator<? super C2> columnComparator) {
		return sortedTable(new GOne<R>(row), column, value,
				rowComparator, columnComparator);
	}

	/**
	 * Create a new aggregating table expression using a backing HashBasedRowSortedTable
	 *
	 * @param row
	 *            row for the sortedTable entries
	 * @param column
	 *            column for the sortedTable entries
	 * @param value
	 *            value for the sortedTable entries
	 * @return wrapper expression
	 */
	public static <R, C, V, R2, C2, V2> AbstractGroupExpression<Triplet<R, C, V>, RowSortedTable<R2, C2, V2>> sortedTable(
			GroupExpression<R, R2> row, GroupExpression<C, C2> column, GroupExpression<V, V2> value,
			Comparator<? super R2> rowComparator, Comparator<? super C2> columnComparator) {
		return new GTable.Mixin<>(
				row, column, value,
				GTable.createSorted(QTriplet.create(row, column, value), rowComparator, columnComparator)
		);
	}

}
