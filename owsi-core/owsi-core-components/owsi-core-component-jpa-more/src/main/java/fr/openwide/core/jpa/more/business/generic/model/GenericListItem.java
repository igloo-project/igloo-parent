/*
 * Copyright (C) 2009-2011 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.openwide.core.jpa.more.business.generic.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.SortableField;

import com.google.common.collect.Ordering;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

@MappedSuperclass
@Bindable
public abstract class GenericListItem<E extends GenericListItem<?>> extends GenericEntity<Long, E>
		implements IGenericListItemBindingInterface {
	
	private static final long serialVersionUID = -6270832991786371463L;
	
	@SuppressWarnings("rawtypes")
	private static final Ordering<Comparable> DEFAULT_KEY_ORDERING = Ordering.natural().nullsLast();
	
	public static final String LABEL_SORT_FIELD_NAME = "labelSort";
	
	public static final String SHORT_LABEL_SORT_FIELD_NAME = "shortLabelSort";
	
	public static final String CODE_FIELD_NAME = "code";

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	@Fields({
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
		@Field(name = LABEL_SORT_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	@SortableField(forField = LABEL_SORT_FIELD_NAME)
	private String label;
	
	@Column
	@Fields({
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
		@Field(name = SHORT_LABEL_SORT_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	@SortableField(forField = SHORT_LABEL_SORT_FIELD_NAME)
	private String shortLabel;
	
	@Column(nullable = false)
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Integer position = 0;

	@Field
	@Column(nullable = false)
	private boolean enabled = true;
	
	@Column(nullable = false)
	private boolean editable = true;
	
	@Column(nullable = false)
	private boolean disableable = true;
	
	@Column(nullable = false)
	private boolean deleteable = false;

	protected GenericListItem() {
	}

	public GenericListItem(String label) {
		this(label, 0);
	}

	public GenericListItem(String label, Integer position) {
		this.label = label;
		this.position = position;
	}

	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setShortLabel(String shortLabel) {
		this.shortLabel = shortLabel;
	}

	@Override
	public String getShortLabel() {
		return shortLabel;
	}

	@Override
	@Fields({
		@Field(name = CODE_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
		// A priori, pas besoin d'un champ spécifique pour le tri ici... ?
	})
	@SortableField(forField = CODE_FIELD_NAME)
	public String getCode() {
		return null;
	}

	public void setPosition(Integer order) {
		this.position = order;
	}

	@Override
	public Integer getPosition() {
		return position;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean isDisableable() {
		return disableable;
	}

	public void setDisableable(boolean disableable) {
		this.disableable = disableable;
	}

	@Override
	public boolean isDeleteable() {
		return deleteable;
	}

	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}

	@Override
	public int compareTo(E right) {
		if (this == right) {
			return 0;
		}
		
		int result = 0;
		if (this.getPosition() != null) {
			if (right.getPosition() != null) {
				result = this.getPosition().compareTo(right.getPosition());
			} else {
				result = 1;
			}
		} else if (right.getPosition() != null) {
			result = -1;
		} else {
			result = 0;
		}
		
		if (result == 0 && this.getLabel() != null && right.getLabel() != null) {
			result = DEFAULT_STRING_COLLATOR.compare(this.getLabel(), right.getLabel());
		}
		
		if (result == 0) {
			Long leftId = getId();
			Long rightId = right.getId();
			// Pas d'exception si les deux IDs sont null, pour conserver un comportement relativement proche
			// de ce qu'on avait quand on ne comparait pas les IDs
			if (leftId != null || rightId != null) {
				result = DEFAULT_KEY_ORDERING.compare(leftId, rightId);
			}
		}
		
		return result;
	}

	@Override
	public String getDisplayName() {
		return getLabel();
	}

	@Override
	public String getNameForToString() {
		return getLabel();
	}
}
