package fr.openwide.core.wicket.more.config.spring;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.wicket.more.lesscss.service.ILessCssService;
import fr.openwide.core.wicket.more.lesscss.service.LessCssServiceImpl;

@Configuration
public class LessCssConfig {

	public ILessCssService lessCssService() {
		return new LessCssServiceImpl();
	}

}
