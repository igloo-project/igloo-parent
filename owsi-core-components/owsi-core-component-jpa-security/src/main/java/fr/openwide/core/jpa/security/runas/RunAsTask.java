package fr.openwide.core.hibernate.security.runas;
/**
 * Interface wrapper permettant l'exécution d'une tâche sous une identité tierce.
 */
public interface RunAsTask<T> {
	
	T execute();
	
}
