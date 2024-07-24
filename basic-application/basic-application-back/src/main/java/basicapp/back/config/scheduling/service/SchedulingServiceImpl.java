package basicapp.back.config.scheduling.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.iglooproject.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SchedulingServiceImpl implements ISchedulingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingServiceImpl.class);

  @Override
  public void temporaryFilesCleaning() {
    // Par défaut, on ne vide aucun répertoire.
    // Exemple :
    // cleanDirectory(configurer.getTmpDirectory());
  }

  @SuppressWarnings("unused")
  private void cleanDirectory(File file) {
    try {
      // on ne supprime que les fichiers qui sont plus vieux que 3 jours
      Instant instant = Instant.now().minus(3, ChronoUnit.DAYS);
      FileUtils.cleanDirectory(file, instant);
    } catch (IOException e) {
      LOGGER.error("Erreur lors du nettoyage du répertoire " + file, e);
    }
  }
}
