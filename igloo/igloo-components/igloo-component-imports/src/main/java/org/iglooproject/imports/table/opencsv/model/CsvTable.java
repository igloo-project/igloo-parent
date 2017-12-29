package org.iglooproject.imports.table.opencsv.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.Lists;

import org.iglooproject.commons.util.functional.SerializableFunction;

public class CsvTable implements Serializable, Iterable<CsvRow> {
	
	private static final long serialVersionUID = -4479121548776416896L;
	
	private final List<List<String>> rows;

	public CsvTable(List<String[]> rows) {
		super();
		this.rows = Lists.transform(rows, new SerializableFunction<String[], List<String>>() {
			private static final long serialVersionUID = 4706021485040817605L;
			@Override
			public List<String> apply(String[] input) {
				return Arrays.asList(input);
			}
		});
	}
	
	@Override
	public Iterator<CsvRow> iterator() {
		return new AbstractSequentialIterator<CsvRow>(getRow(0)) {
			@Override
			protected CsvRow computeNext(CsvRow previous) {
				return getRow(previous.getIndex() + 1);
			}
		};
	}

	public List<List<String>> getContent() {
		return rows;
	}
	
	public CsvRow getRow(int index) {
		if (0 <= index && index < rows.size()) {
			return new CsvRow(this, index);
		} else {
			return null;
		}
	}
	
	// equals and hashcode defined as identity (==)

}
