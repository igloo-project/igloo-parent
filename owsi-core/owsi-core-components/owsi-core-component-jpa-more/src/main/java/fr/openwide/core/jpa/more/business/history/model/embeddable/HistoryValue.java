package fr.openwide.core.jpa.more.business.history.model.embeddable;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;

import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.search.bridge.NullEncodingGenericEntityReferenceFieldBridge;

@Embeddable
@Bindable
@Access(AccessType.FIELD)
public class HistoryValue implements Serializable {
	
	private static final long serialVersionUID = 1251495816635000683L;

	public static final String ENTITY_REFERENCE = "entityReference";

	/**
	 * Human-readable string
	 */
	@Basic
	private String label;

	/**
	 * Machine-readable string (for instance MyEnum.VALUE.name())
	 */
	@Basic
	private String serialized;
	
	@Embedded
	@Field(name = ENTITY_REFERENCE, bridge = @FieldBridge(impl = NullEncodingGenericEntityReferenceFieldBridge.class), analyze = Analyze.NO)
	private HistoryEntityReference entityReference;

	public HistoryValue() {
		// nothing to do
	}

	public HistoryValue(String label) {
		this(label, null, null);
	}

	public HistoryValue(String label, String serialized) {
		this(label, serialized, null);
	}

	public HistoryValue(String label, GenericEntityReference<Long, ?> entityValueReference) {
		this(label, null, entityValueReference);
	}

	private HistoryValue(String label, String serialized, GenericEntityReference<Long, ?> entityValueReference) {
		super();
		this.label = label;
		this.serialized = serialized;
		this.entityReference = HistoryEntityReference.from(entityValueReference);
	}

	public String getLabel() {
		return label;
	}
	
	public String getSerialized() {
		return serialized;
	}

	public HistoryEntityReference getEntityReference() {
		return entityReference;
	}
	
	@Override
	public String toString() {
		if (entityReference != null) {
			return entityReference.toString();
		} else {
			return label;
		}
	}

}
