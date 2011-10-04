package fr.openwide.core.jpa.security.runas;
/**
 * Interface wrapper permettant l'exécution d'une tâche sous une identité tierce.
 */
public interface IRunAsTask<T> {
	
	T execute();
	
}
