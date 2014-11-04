package fr.openwide.core.basicapp.core.config.scheduling.service;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.commons.util.FileUtils;

@Service("schedulingService")
public class SchedulingServiceImpl implements ISchedulingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingServiceImpl.class);

	@Autowired
	private BasicApplicationConfigurer configurer;

	@Override
	public void temporaryFilesCleaning() {
		//Par défaut, on ne vide aucun répertoire.
		//Exemple :
		//cleanDirectory(configurer.getTmpDirectory());
	}
	
	@SuppressWarnings("unused")
	private void cleanDirectory(File file) {
		try {
			// on ne supprime que les fichiers qui sont plus vieux que 3 jours
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			
			FileUtils.cleanDirectory(file, calendar.getTime());
		} catch (IOException e) {
			LOGGER.error("Erreur lors du nettoyage du répertoire " + file, e);
		}
	}
	
}
