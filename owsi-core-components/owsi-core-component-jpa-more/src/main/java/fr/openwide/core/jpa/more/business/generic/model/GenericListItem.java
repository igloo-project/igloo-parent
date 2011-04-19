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

package fr.openwide.core.hibernate.more.business.generic.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@MappedSuperclass
@Bindable
public abstract class GenericListItem<E extends GenericListItem<?>> extends GenericEntity<Integer, E>
		implements GenericListItemBindingInterface {
	
	private static final long serialVersionUID = -6270832991786371463L;

	@Id
	@GeneratedValue
	private Integer id;
	
	private String label;
	
	private String shortLabel;
	
	@Column(nullable = false)
	private Integer position;

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
	
	public GenericListItem(String label, Integer position) {
		super();
		
		this.label= label;
		this.position = position;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
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
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isDisableable() {
		return disableable;
	}

	public void setDisableable(boolean disableable) {
		this.disableable = disableable;
	}

	public boolean isDeleteable() {
		return deleteable;
	}

	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}

	@Override
	public int compareTo(E o) {
		if (this == o) {
			return 0;
		}
		
		int result = 0;
		if (this.getPosition() != null) {
			if (o.getPosition() != null) {
				result = this.getPosition().compareTo(o.getPosition());
			} else {
				result = 1;
			}
		} else if (o.getPosition() != null) {
			result = -1;
		} else {
			result = 0;
		}
		
		if (result == 0 && this.getLabel() != null && o.getLabel() != null) {
			result = DEFAULT_STRING_COLLATOR.compare(this.getLabel(), o.getLabel());
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
