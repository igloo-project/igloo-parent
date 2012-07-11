package fr.openwide.core.jpa.config.spring.listener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import fr.openwide.core.jpa.hibernate.tool.ExtendedMultipleLinesSqlCommandExtractor;
import fr.openwide.core.spring.util.StringUtils;

public class EntityManagerListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityManagerListener.class);

	private static final String FAIL_ON_ERROR = "failOnError";

	private static final String SUPERUSER = "superuser";

	private static final String WRAP_IN_TRANSACTION = "wrapInTransaction";

	@Autowired
	private ApplicationContext applicationContext;

	@Value("${hibernate.entityManagerListener.enabled}")
	private boolean enabled;

	@Value("${hibernate.entityManagerListener.filesLocations}")
	private String filesLocations;

	@Value("${hibernate.entityManagerListener.superuser.userName}")
	private String superuserUserName;

	@Value("${hibernate.entityManagerListener.superuser.password}")
	private String superuserPassword;

	private boolean listenerProcessed = false;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!listenerProcessed && enabled) {
			try {
				LocalContainerEntityManagerFactoryBean bean = applicationContext.getBean(LocalContainerEntityManagerFactoryBean.class);
				DataSource dataSource = bean.getPersistenceUnitInfo().getNonJtaDataSource();
				if (dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
					((org.apache.tomcat.jdbc.pool.DataSource) dataSource).setAlternateUsernameAllowed(true);
				}
				List<String> files = StringUtils.splitAsList(filesLocations, ",");
				for (String file : files) {
					boolean superuser = false;
					boolean wrapInTransaction = false;
					boolean failOnError = false;
					if (file.contains("|")) {
						List<String> fileDescription = StringUtils.splitAsList(file, "|");
						file = fileDescription.get(fileDescription.size() - 1);
						superuser = fileDescription.contains(SUPERUSER);
						wrapInTransaction = fileDescription.contains(WRAP_IN_TRANSACTION);
						failOnError = fileDescription.contains(FAIL_ON_ERROR);
					}
					if (applicationContext.getResource(file).exists()) {
						boolean hasError = false;
						
						Resource resource = applicationContext.getResource(file);
						ExtendedMultipleLinesSqlCommandExtractor extractor = new ExtendedMultipleLinesSqlCommandExtractor();
						String[] statements;
						try {
							statements = extractor.extractCommands(new InputStreamReader(resource.getInputStream()));
						} catch (IOException e) {
							throw new RuntimeException("Erreur de lecture d'un fichier d'import SQL " + file, e);
						}
						Connection connection;
						try {
							if (superuser) {
								connection = dataSource.getConnection(superuserUserName, superuserPassword);
							} else {
								connection = dataSource.getConnection();
							}
						} catch (SQLException e) {
							throw new RuntimeException("Erreur de récupération de la connection lors de l'initialisation", e);
						}
						try {
							if (wrapInTransaction) {
								connection.setAutoCommit(false);
							} else {
								connection.setAutoCommit(true);
							}
						} catch (SQLException e) {
							throw new RuntimeException("Erreur de préparation de la connexion", e);
						}
						for (String statement : statements) {
							try {
								System.out.println(statement);
								connection.prepareCall(statement).execute();
							} catch (SQLException e) {
								hasError = true;
								LOGGER.error("Erreur initialisation SQL : " + e.getMessage());
								LOGGER.info("Stacktrace de l'erreur SQL", e);
								try {
									if (wrapInTransaction) {
										connection.rollback();
										break;
									}
								} catch (SQLException e1) {
									LOGGER.error("Erreur de fermeture de connexion sur une erreur : " + e1.getMessage());
									LOGGER.info("Stacktrace de l'erreur SQL", e1);
								}
								if (failOnError) {
									break;
								}
							}
						}
						if (connection != null && wrapInTransaction) {
							try {
								connection.commit();
							} catch (SQLException e) {
								if (failOnError) {
									throw new RuntimeException("Erreur au commit d'une transaction : " + e.getMessage());
								} else {
									LOGGER.error("Erreur au commit d'une transaction : " + e.getMessage());
									LOGGER.info("Stacktrace de l'erreur SQL", e);
								}
							}
						}
						if (connection != null) {
							try {
								connection.close();
							} catch (SQLException e) {
								if (failOnError) {
									throw new RuntimeException("Erreur au close d'une transaction : " + e.getMessage());
								} else {
									LOGGER.error("Erreur au close d'une transaction : " + e.getMessage());
									LOGGER.info("Stacktrace de l'erreur SQL", e);
								}
							} finally {
								connection = null;
							}
						}
						if (hasError && failOnError) {
							throw new RuntimeException("Erreur lors de l'initialisation du fichier " + file);
						}
					}
				}
			} finally {
				listenerProcessed = true;
			}
		}
	}

}
