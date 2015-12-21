package fr.openwide.core.jpa.more.business.history.util;

import java.util.Date;

import fr.openwide.core.jpa.more.business.difference.model.Difference;

/**
 * Met à jour un objet en fonction des différences constatées sur cet objet.
 * <p>Se résume pour l'instant à simplement rafraîchir certains {@link HistoryEvent}.
 */
public interface IDifferenceHandler<T> {
	
	void handle(T entity, Difference<T> difference, Date date);

}
