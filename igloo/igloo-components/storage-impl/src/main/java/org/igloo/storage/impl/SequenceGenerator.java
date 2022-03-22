package org.igloo.storage.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.persistence.EntityManager;

import org.hibernate.Session;

public class SequenceGenerator {

	private final String sequenceName;

	public SequenceGenerator(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public long generate(EntityManager entityManager) {
		return entityManager.unwrap(Session.class).doReturningWork(c -> {
			PreparedStatement statement = c.prepareStatement("SELECT nextval(?)");
			statement.setString(1, sequenceName);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getLong(1);
		});
	}

}
