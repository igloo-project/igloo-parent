package fr.openwide.core.jpa.hibernate.usertype;

/**
 * @deprecated Extend {@link AbstractMaterializedPrimitiveValue} instead.
 */
public abstract class AbstractMaterializedStringValue<T extends AbstractMaterializedStringValue<T>>
		extends AbstractMaterializedPrimitiveValue<String, T> {
	
	private static final long serialVersionUID = -798806400623732408L;
	
	protected AbstractMaterializedStringValue(String value) {
		super(value);
	}

}
