package fr.openwide.core.basicapp.init;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import de.schlichtherle.truezip.file.TFile;
import fr.openwide.core.basicapp.init.config.spring.BasicApplicationInitConfig;
import fr.openwide.core.basicapp.init.util.SpringContextWrapper;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public final class BasicApplicationInitFromExcelMain {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicApplicationInitFromExcelMain.class);

	public static void main(String[] args) throws ServiceException, SecurityServiceException, IOException {
		ConfigurableApplicationContext context = null;
		try {
			context = new AnnotationConfigApplicationContext(BasicApplicationInitConfig.class);
			
			SpringContextWrapper contextWrapper = context.getBean("springContextWrapper", SpringContextWrapper.class);
			
			contextWrapper.openEntityManager();
			contextWrapper.importDirectory(new TFile( // May be inside a Jar
					BasicApplicationInitFromExcelMain.class.getResource("/init").toURI()
			));
			
			contextWrapper.reindexAll();
			
			LOGGER.info("Initialization complete");
		} catch(Throwable e) { // NOSONAR We just want to log the Exception/Error, no error handling here.
			LOGGER.error("Error during initialization", e);
			throw new RuntimeException(e);
		} finally {
			if (context != null) {
				context.close();
			}
		}
		System.exit(0);
	}
	
	private BasicApplicationInitFromExcelMain() {
	}
}
