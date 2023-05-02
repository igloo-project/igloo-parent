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

package org.iglooproject.jpa.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.sql.DataSource;

import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jakarta.annotation.PostConstruct;

/**
 * <p>Initialise le schéma de la base.</p>
 *
 * @author Open Wide
 */
public class DatabaseInitializationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializationService.class);

	@Autowired
	private IPropertyService propertyService;

	private JdbcTemplate jdbcTemplate;

	/**
	 * Méthode utilisée par l'initialisation spring pour la mise en place
	 * du {@link DataSource}
	 */
	public void setDataSource(DataSource dataSource) {
		/*
		 * Il faut utiliser une DelegatingDataSource car le transaction manager d'hibernate
		 * n'accepte pas de partager la gestion d'un data-source avec un autre transaction
		 * manager.
		 * On wrappe donc la data-source pour qu'hibernate accepte de déléguer la gestion.
		 */
		DelegatingDataSource myDataSource = new DelegatingDataSource(dataSource);
		jdbcTemplate = new JdbcTemplate(myDataSource);
	}

	/**
	 * Méthode permettant de mettre en place le schéma de la base.
	 */
	@PostConstruct
	protected void initDatabaseSchema() throws IOException {
		if (propertyService.isConfigurationTypeDevelopment()) {
			LOGGER.warn("Initialisation de la base de données");

			InputStream sqlDropFile = new ClassPathResource("sql/drop_tables.sql").getInputStream();
			InputStream sqlInitFile = new ClassPathResource("sql/create_tables.sql").getInputStream();
			
			/*
			 * Si on ne gère pas la connexion à la main en utilisant les méthodes
			 * TransactionSynchronizationManager.initSychronization/clear, alors
			 * les appels consécutifs de jdbcTemplate.execute :
			 *  - ne se feront pas sur la même connexion du pool
			 *  - de ce fait, il sera impossible d'avoir une quelconque influence sur les transactions
			 */
			TransactionSynchronizationManager.initSynchronization();
			try {
				executeQueriesFromFile(sqlDropFile, true);
				jdbcTemplate.execute("BEGIN");
				executeQueriesFromFile(sqlInitFile, false);
				jdbcTemplate.execute("COMMIT");
			} catch (RuntimeException e) {
				LOGGER.error("Initialization problem", e);
				jdbcTemplate.execute("ROLLBACK");
			} finally {
				TransactionSynchronizationManager.clear();
			}
			LOGGER.warn("Initialisation terminée");
		}
	}

	/**
	 * Exécute les requêtes SQL d'un fichier. Une erreur sur une requête n'interrompt pas le processing mais
	 * affiche un message d'erreur.
	 * 
	 * @param sqlFile le fichier sql
	 * @param doCommit indique si chaque opération du fichier doit être encadrée d'un BEGIN/COMMIT
	 */
	private void executeQueriesFromFile(InputStream sqlFile, boolean doCommit) {
		StringBuilder sql = new StringBuilder();

		InputStreamReader isr = null;
		BufferedReader in = null;
		try {
			isr = new InputStreamReader(sqlFile);
			in = new BufferedReader(isr);

			boolean inFunction = false;
			String line;
			while ((line = in.readLine()) != null) {
				String lineTrimed = line.trim();
				if (lineTrimed.startsWith("--") || lineTrimed.length() == 0) {
					continue;
				}

				sql.append(line);
				sql.append('\n');

				if (lineTrimed.contains("$func$")) {
					inFunction = !inFunction;
				}

				if (!inFunction && lineTrimed.endsWith(";")) {
					String sqlQuery = sql.toString();

					// exécution après un point-virgule ou à la fin d'une
					// fonction
					try {
						if (doCommit) {
							jdbcTemplate.execute("BEGIN");
						}
						jdbcTemplate.execute(sqlQuery);
					} catch (RuntimeException err) {
						if (doCommit) {
							jdbcTemplate.execute("ROLLBACK");
						}
						LOGGER.error(err.getMessage(), err);
					} finally {
						if (doCommit) {
							jdbcTemplate.execute("COMMIT");
						}
					}
					sql.delete(0, sql.length());
				}
			}
		} catch (RuntimeException | IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				if (sqlFile != null) {
					sqlFile.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

}
