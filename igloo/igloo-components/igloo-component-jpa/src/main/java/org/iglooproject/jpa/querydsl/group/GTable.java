/*
 * Adapted from com.querydsl.core.group.GMap<K, V, M>, whose copyright was:
 * 
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iglooproject.jpa.querydsl.group;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.javatuples.Triplet;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.RowSortedTable;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.querydsl.core.group.AbstractGroupExpression;
import com.querydsl.core.group.GroupCollector;
import com.querydsl.core.group.GroupExpression;

abstract class GTable<R, C, V, T extends Table<R, C, V>> extends AbstractGroupExpression<Triplet<R, C, V>, T> {

	private static final long serialVersionUID = 7106389414200843920L;

	public GTable(QTriplet<R, C, V> qtriplet) {
		super(Table.class, qtriplet);
	}

	protected abstract T createTable();

	public static <R, C, V> GTable<R, C, V, Table<R, C, V>> createLinked(QTriplet<R, C, V> expr) {
		return new GTable<R, C, V, Table<R, C, V>>(expr) {
			private static final long serialVersionUID = 1L;
			@Override
			protected Table<R, C, V> createTable() {
				return HashBasedTable.create();
			}
		};
	}

	@SuppressWarnings("rawtypes")
	public static <R extends Comparable, C extends Comparable, V> GTable<R, C, V, RowSortedTable<R, C, V>>
			createSorted(QTriplet<R, C, V> expr) {
		return new GTable<R, C, V, RowSortedTable<R, C, V>>(expr) {
			private static final long serialVersionUID = 1L;
			@Override
			protected RowSortedTable<R, C, V> createTable() {
				return TreeBasedTable.<R, C, V>create();
			}
		};
	}

	public static <R, C, V> GTable<R, C, V, RowSortedTable<R, C, V>>
			createSorted(QTriplet<R, C, V> expr, final Comparator<? super R> rowComparator,
					final Comparator<? super C> columnComparator) {
		return new GTable<R, C, V, RowSortedTable<R, C, V>>(expr) {
			private static final long serialVersionUID = 1L;
			@Override
			protected RowSortedTable<R, C, V> createTable() {
				return TreeBasedTable.<R, C, V>create(rowComparator, columnComparator);
			}
		};
	}

	@Override
	public GroupCollector<Triplet<R, C, V>, T> createGroupCollector() {
		return new GroupCollector<Triplet<R, C, V>, T>() {

			private final T table = createTable();

			@Override
			public void add(Triplet<R, C, V> triplet) {
				table.put(triplet.getValue0(), triplet.getValue1(), triplet.getValue2());
			}

			@Override
			public T get() {
				return table;
			}

		};
	}

	static class Mixin<R, C, V, R2, C2, V2, T extends Table<? super R2, ? super C2, ? super V2>>
			extends AbstractGroupExpression<Triplet<R, C, V>, T> {

		private static final long serialVersionUID = 1939989270493531116L;

		private class GroupCollectorImpl implements GroupCollector<Triplet<R, C, V>, T> {

			private final GroupCollector<Triplet<R2, C2, V2>, T> groupCollector;

			private final Map<R, GroupCollector<R, R2>> rowCollectors =
					new LinkedHashMap<>();

			private final Map<C, GroupCollector<C, C2>> columnCollectors =
					new LinkedHashMap<>();

			private final Table<GroupCollector<R, R2>, GroupCollector<C, C2>, GroupCollector<V, V2>> valueCollectors =
					HashBasedTable.create();

			public GroupCollectorImpl() {
				this.groupCollector = mixin.createGroupCollector();
			}

			@Override
			public void add(Triplet<R, C, V> triplet) {
				R row = triplet.getValue0();
				GroupCollector<R, R2> rowCollector = rowCollectors.get(row);
				if (rowCollector == null) {
					rowCollector = rowExpression.createGroupCollector();
					rowCollectors.put(row, rowCollector);
				}
				rowCollector.add(row);
				
				C column = triplet.getValue1();
				GroupCollector<C, C2> columnCollector = columnCollectors.get(column);
				if (columnCollector == null) {
					columnCollector = columnExpression.createGroupCollector();
					columnCollectors.put(column, columnCollector);
				}
				columnCollector.add(column);
				
				GroupCollector<V, V2> valueCollector = valueCollectors.get(rowCollector, columnCollector);
				if (valueCollector == null) {
					valueCollector = valueExpression.createGroupCollector();
					valueCollectors.put(rowCollector, columnCollector, valueCollector);
				}
				V value = triplet.getValue2();
				valueCollector.add(value);
			}

			@Override
			public T get() {
				for (Map.Entry<GroupCollector<R, R2>, Map<GroupCollector<C, C2>, GroupCollector<V, V2>>> rowEntry
						: valueCollectors.rowMap().entrySet()) {
					GroupCollector<R, R2> rowCollector = rowEntry.getKey();
					R2 row = rowCollector.get();
					Map<GroupCollector<C, C2>, GroupCollector<V, V2>> columnMap = rowEntry.getValue();
					for (Map.Entry<GroupCollector<C, C2>, GroupCollector<V, V2>> columnEntry : columnMap.entrySet()) {
						GroupCollector<C, C2> columnCollector = columnEntry.getKey();
						C2 column = columnCollector.get();
						GroupCollector<V, V2> valueCollector = columnEntry.getValue();
						V2 value = valueCollector.get();
						groupCollector.add(Triplet.with(row, column, value));
					}
				}
				rowCollectors.clear();
				columnCollectors.clear();
				valueCollectors.clear();
				return groupCollector.get();
			}

		}

		private final GroupExpression<Triplet<R2, C2, V2>, T> mixin;

		private final GroupExpression<R, R2> rowExpression;
		private final GroupExpression<C, C2> columnExpression;
		private final GroupExpression<V, V2> valueExpression;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Mixin(GroupExpression<R, R2> rowExpression, GroupExpression<C, C2> columnExpression, GroupExpression<V, V2> valueExpression,
				AbstractGroupExpression<Triplet<R2, C2, V2>, T> mixin) {
			super((Class) mixin.getType(),
					QTriplet.create(rowExpression.getExpression(), columnExpression.getExpression(), valueExpression.getExpression()));
			this.rowExpression = rowExpression;
			this.columnExpression = columnExpression;
			this.valueExpression = valueExpression;
			this.mixin = mixin;
		}

		@Override
		public GroupCollector<Triplet<R, C, V>, T> createGroupCollector() {
			return new GroupCollectorImpl();
		}

	}

}