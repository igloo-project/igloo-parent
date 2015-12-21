package fr.openwide.core.jpa.more.business.difference.service;

import fr.openwide.core.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import fr.openwide.core.jpa.more.business.difference.util.IHistoryDifferenceGenerator;

public interface IDifferenceService<T> extends IHistoryDifferenceGenerator<T> {
	
	/**
	 * @return Un générateur de différence à utiliser dans la plupart des cas.
	 */
	IDifferenceFromReferenceGenerator<T> getMainDifferenceGenerator();

	/**
	 * @return Un générateur de différence à utiliser lorsque le diff doit être réalisé sur un sous-ensemble
	 * restreint de propriétés (par exemple à la création d'un objet).
	 */
	IDifferenceFromReferenceGenerator<T> getMinimalDifferenceGenerator();

}
