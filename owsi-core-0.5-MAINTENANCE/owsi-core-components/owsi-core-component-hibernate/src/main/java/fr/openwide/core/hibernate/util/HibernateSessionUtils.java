/*
 * Copyright (C) 2009-2010 Open Wide
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

package fr.openwide.core.hibernate.util;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * <p>Classe permettant de manipuler la session Hibernate.</p>
 * 
 * <p>Utilisée dans les tests pour maintenir une session unique ouverte. Dans le
 * flot normal de l'application, un filter gère l'ouverture et la fermeture de
 * la session Hibernate.</p>
 * 
 * @author Open Wide
 */
@Component
public class HibernateSessionUtils {
	
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Démarre la session Hibernate.
	 * 
	 * @return session Hibernate ouverte
	 */
	public Session initSession() {
		Session session = null;
		if (!TransactionSynchronizationManager.hasResource(sessionFactory)) {
			session = getSession(sessionFactory);
			TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		}
		return session;
	}
	
	/**
	 * Ferme la session Hibernate.
	 */
	public void closeSession() {
		SessionHolder sessionHolder = 
			(SessionHolder) TransactionSynchronizationManager.unbindResourceIfPossible(sessionFactory);
		if (sessionHolder != null) {
			SessionFactoryUtils.closeSession(sessionHolder.getSession());
		}
	}
	
	/**
	 * Retourne une session Hibernate gérée par le sessionFactory passé en
	 * paramètre.
	 * 
	 * @return session Hibernate
	 * @throws DataAccessResourceFailureException
	 */
	private Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.MANUAL);
		return session;
	}
}
