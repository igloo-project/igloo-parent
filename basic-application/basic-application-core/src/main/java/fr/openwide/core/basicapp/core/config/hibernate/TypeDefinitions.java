package fr.openwide.core.basicapp.core.config.hibernate;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import fr.openwide.core.basicapp.core.business.common.model.AdresseEmail;
import fr.openwide.core.basicapp.core.business.common.model.NumeroTelephone;
import fr.openwide.core.basicapp.core.business.common.model.PostalCode;
import fr.openwide.core.basicapp.core.config.hibernate.type.AdresseEmailType;
import fr.openwide.core.basicapp.core.config.hibernate.type.NumeroTelephoneType;
import fr.openwide.core.basicapp.core.config.hibernate.type.PostalCodeType;
import fr.openwide.core.jpa.hibernate.usertype.StringClobType;

/**
 * Class used to define types with annotations.
 */
@SuppressWarnings("deprecation")
@TypeDefs({
	// We use "text" instead of "varchar" for String columns
	@TypeDef(defaultForType = String.class, typeClass = StringClobType.class),
	// We declare here the types we want to store as String instead of binary
	@TypeDef(defaultForType = AdresseEmail.class, typeClass = AdresseEmailType.class),
	@TypeDef(defaultForType = NumeroTelephone.class, typeClass = NumeroTelephoneType.class),
	@TypeDef(defaultForType = PostalCode.class, typeClass = PostalCodeType.class)
})
@MappedSuperclass
public final class TypeDefinitions {
	
	/**
	 * Usage:
	 * <pre>
	 *	&#064;Column
	 *	&#064;Type(type = TypeDefinitions.STRING_CLOB)
	 *	private String myStringClobField;
	 * </pre>
	 */
	public static final String STRING_CLOB = "fr.openwide.core.jpa.hibernate.usertype.StringClobType";
	
	/**
	 * Usage:
	 * <pre>
	 *	&#064;Column(length = 4)
	 *	&#064;Type(type = TypeDefinitions.STRING_VARCHAR)
	 *	private String myStringVarcharField;
	 * </pre>
	 */
	public static final String STRING_VARCHAR = "org.hibernate.type.StringType";
	
	private TypeDefinitions() { }
	
}
