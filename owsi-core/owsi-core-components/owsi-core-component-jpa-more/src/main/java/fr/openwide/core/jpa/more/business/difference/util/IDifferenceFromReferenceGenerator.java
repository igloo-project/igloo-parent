package fr.openwide.core.jpa.more.business.difference.util;

import java.util.concurrent.Callable;

import fr.openwide.core.jpa.more.business.difference.model.Difference;

public interface IDifferenceFromReferenceGenerator<T> extends IDifferenceGenerator<T> {
	
	Difference<T> diffFromReference(T value);
	
	/**
	 * Lorsqu'on doit calculer des différences sur de nombreux objets, ceci permet de factoriser la récupération des
	 * références dans une seule et même transaction, au lieu d'ouvrir une transaction pour chaque calcul de différence.
	 * <p>Afin d'être utilisable dans la méthode {@link #diff(Object, Object)}, la référence retournée doit passer
	 * par la méthode {@link #initializeReference(Object)}
	 * @see #initializeReference(Object)
	 * @see #diff(Object, Object)
	 */
	Callable<T> getReferenceProvider(T value);

	/**
	 * Doit être appelé sur un objet retourné par {@link #retrieveReference(Object)} afin de pouvoir le passer à la
	 * méthode {@link #diff(Object, Object)}.
	 * @see #retrieveReference(Object)
	 * @see #diff(Object, Object)
	 */
	void initializeReference(T reference);

}
