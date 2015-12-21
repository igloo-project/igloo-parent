package fr.openwide.core.jpa.more.business.difference.access;

import de.danielbechler.diff.access.Accessor;

public interface IKeyAwareAccessor<K> extends Accessor {
	
	K getKey();

}
