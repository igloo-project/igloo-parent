package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemit;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.util.string.Strings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.collect.Lists;

import fr.openwide.core.jpa.util.HibernateUtils;

public abstract class AjaxItemItBehavior<I, J> extends AbstractAjaxBehavior {

	private static final long serialVersionUID = -7329322381118427479L;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public abstract List<I> getValues(String term);

	public abstract J newAutocompleteJson(Integer index, I item, Locale locale);

	public abstract Class<?> getJsonType();

	public AjaxItemItBehavior() {
	}

	@Override
	public void onRequest() {
		String term = this.getComponent().getRequest().getQueryParameters().getParameterValue("term").toString();
		
		if (!Strings.isEmpty(term)) {
			StringWriter sw = new StringWriter();
			try {
				J value = null;
				Integer index = 0;
				List<J> toSerializeObjects = Lists.newArrayList();
				
				for (I item : getValues(term)) {
					item = HibernateUtils.unwrap(item);
					index++;
					value = newAutocompleteJson(index, item, getComponent().getLocale());
					toSerializeObjects.add(value);
				}
				
				// il est important d'utiliser un typeReference sinon les paramètres de sérialisation
				// polymorphiques de J ne seront pas appliqués.
				CollectionType collectionType = OBJECT_MAPPER.getTypeFactory()
						.constructCollectionType(List.class, getJsonType());
				OBJECT_MAPPER.writerWithType(collectionType).writeValue(sw, toSerializeObjects);
			} catch (IOException e) {
				throw new WicketRuntimeException(e);
			}
			
			RequestCycle.get().scheduleRequestHandlerAfterCurrent(
				new TextRequestHandler("application/json", "utf-8", sw.toString()));
		}
	}

}
