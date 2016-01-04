package fr.openwide.core.jpa.more.business.history.util;

import java.util.Date;

import fr.openwide.core.jpa.more.business.difference.model.Difference;

/**
 * Update an object depending on the differences found on it.
 * Met à jour un objet en fonction des différences constatées sur cet objet.
 * <p>At the moment, only refreshes {@link HistoryEvent}.
 */
public interface IDifferenceHandler<T> {
	
	void handle(T entity, Difference<T> difference, Date date);

}
