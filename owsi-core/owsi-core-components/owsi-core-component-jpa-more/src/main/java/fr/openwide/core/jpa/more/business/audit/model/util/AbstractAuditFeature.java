/*
 * Copyright (C) 2008-2011 Open Wide
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
package fr.openwide.core.jpa.more.business.audit.model.util;

import javax.persistence.MappedSuperclass;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;

/**
 * <p>Les fonctions référencées dans le journal.</p>
 * 
 * <p>ATTENTION, ces éléments sont utilisés pour de l'affichage : quand on ajoute une clé ici, il faut ajouter l'élément
 * ad hoc dans le fichier Global.properties.</p>
 */
@MappedSuperclass
public abstract class AbstractAuditFeature extends GenericListItem<AbstractAuditFeature> {
	private static final long serialVersionUID = -188108294900406595L;
	
	public AbstractAuditFeature(String label, Integer position) {
		super(label, position);
	}
}