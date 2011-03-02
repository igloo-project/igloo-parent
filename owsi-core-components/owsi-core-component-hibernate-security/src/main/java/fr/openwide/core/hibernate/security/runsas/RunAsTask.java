package fr.openwide.core.hibernate.security.runsas;
/**
 * Interface wrapper permettant l'exécution d'une tâche sous une identité tierce.
 */
public interface RunAsTask<T> {
	
	T execute();
	
}
