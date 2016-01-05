package fr.openwide.core.jpa.more.business.difference.util;

import fr.openwide.core.jpa.more.business.difference.model.Difference;

public interface IDifferenceGenerator<T> {

	Difference<T> diff(T modifie, T reference);

}
