package fr.openwide.core.jpa.more.business.difference.util;

/**
 * Une interface pour les objets chargés d'initialiser les entités chargées depuis la base de données avant la
 * fermeture de la transaction. Permet d'utiliser par la suite des champs non chargés automatiquement, tels que
 * des collections LAZY.
 */
public interface IProxyInitializer<T> {
	void initialize(T value);
}