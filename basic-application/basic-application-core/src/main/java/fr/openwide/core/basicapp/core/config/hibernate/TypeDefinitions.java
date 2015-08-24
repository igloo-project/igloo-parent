package fr.openwide.core.basicapp.core.config.hibernate;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import fr.openwide.core.basicapp.core.business.common.model.AdresseEmail;
import fr.openwide.core.basicapp.core.business.common.model.CodePostal;
import fr.openwide.core.basicapp.core.business.common.model.NumeroTelephone;
import fr.openwide.core.basicapp.core.config.hibernate.type.AdresseEmailType;
import fr.openwide.core.basicapp.core.config.hibernate.type.CodePostalType;
import fr.openwide.core.basicapp.core.config.hibernate.type.NumeroTelephoneType;
import fr.openwide.core.jpa.hibernate.usertype.StringClobType;

/**
 * Classe utilisée pour définir des types via des annotations. Aucun autre intérêt.
 */
@SuppressWarnings("deprecation")
@TypeDefs({
	// On utilise "text" plutôt que "varchar" pour les colonnes de type String
	@TypeDef(defaultForType = String.class, typeClass = StringClobType.class),
	// On stocke certains types comme des chaines de caractères, sinon ils sont stockés en format binaire
	@TypeDef(defaultForType = AdresseEmail.class, typeClass = AdresseEmailType.class),
	@TypeDef(defaultForType = NumeroTelephone.class, typeClass = NumeroTelephoneType.class),
	@TypeDef(defaultForType = CodePostal.class, typeClass = CodePostalType.class)
})
@MappedSuperclass
public final class TypeDefinitions {
	
	/**
	 * Utilisation:
	 * <pre>
	 *	&#064;Column(length = 4)
	 *	&#064;Type(type = TypeDefinitions.STRING_VARCHAR)
	 *	private String myStringVarcharField;
	 * </pre>
	 */
	public static final String STRING_VARCHAR = "org.hibernate.type.StringType";
	
	private TypeDefinitions() { }
	
}
