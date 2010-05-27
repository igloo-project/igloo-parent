package fr.openwide.springmvc.web.resolver.bind;

import java.beans.PropertyEditor;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import fr.openwide.springmvc.web.business.common.editor.DateEditor;
import fr.openwide.springmvc.web.util.DateFormatUtils;

/**
 * 
 * @author Open Wide
 */
public class MyWebBindingInitializer implements WebBindingInitializer {
	/**
     * Enregistrement des {@link PropertyEditor} qui seront systematiquement 
     * utilisés pour peupler les objets.
     * 
     * @param binder le {@link WebDataBinder} utilisé
     * @param request la requête
     */
    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
    	binder.registerCustomEditor(java.util.Date.class, new DateEditor(DateFormatUtils.DATE_FORMAT));
    }
}
