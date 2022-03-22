package org.igloo.storage.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.persistence.EntityManager;

import org.hibernate.Session;

public class SequenceGenerator {

	private final String fichierSequenceName;

	private final String storageUnitSequenceName;

	public SequenceGenerator(String sequenceName) {
		this.fichierSequenceName = sequenceName;
		this.storageUnitSequenceName = sequenceName;
	}

	public long generateStorageUnit(EntityManager entityManager) {
		return entityManager.unwrap(Session.class).doReturningWork(c -> {
			PreparedStatement statement = c.prepareStatement("SELECT nextval(?)");
			statement.setString(1, storageUnitSequenceName);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getLong(1);
		});
	}

	public long generateFichier(EntityManager entityManager) {
		return entityManager.unwrap(Session.class).doReturningWork(c -> {
			PreparedStatement statement = c.prepareStatement("SELECT nextval(?)");
			statement.setString(1, fichierSequenceName);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getLong(1);
		});
	}

}
