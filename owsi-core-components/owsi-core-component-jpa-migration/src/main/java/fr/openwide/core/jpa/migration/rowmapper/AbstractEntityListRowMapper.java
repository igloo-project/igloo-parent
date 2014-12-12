package fr.openwide.core.jpa.migration.rowmapper;

import java.util.List;

public abstract class AbstractEntityListRowMapper<E> extends AbstractEntityRowMapper<List<E>> {

	protected AbstractEntityListRowMapper(List<E> results) {
		super(results);
	}
}
