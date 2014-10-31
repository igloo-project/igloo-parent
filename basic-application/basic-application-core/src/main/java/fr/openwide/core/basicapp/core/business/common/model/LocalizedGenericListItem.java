package fr.openwide.core.basicapp.core.business.common.model;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.mysema.query.annotations.QueryInit;

import fr.openwide.core.basicapp.core.business.common.model.embeddable.LocalizedText;
import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem;

@MappedSuperclass
@Bindable
public class LocalizedGenericListItem<E extends LocalizedGenericListItem<?>> extends
		GenericLocalizedGenericListItem<E, LocalizedText> {
	
	private static final long serialVersionUID = 3133040383005390191L;
	
	@Embedded
	@IndexedEmbedded
	@QueryInit("*")
	private LocalizedText label;

	public LocalizedGenericListItem() {
		this(new LocalizedText());
	}

	public LocalizedGenericListItem(LocalizedText localizedLabel) {
		setLabel(localizedLabel);
	}

	@Override
	public LocalizedText getLabel() {
		if (label == null) {
			label = new LocalizedText();
		}
		return label;
	}
	
	@Override
	public void setLabel(LocalizedText label) {
		this.label = label == null ? null : new LocalizedText(label);
	}

}
