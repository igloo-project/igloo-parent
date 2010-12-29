package fr.openwide.core.wicket.markup.html.util.css3pie;

import java.util.Map;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.collections.MiniMap;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;
import org.springframework.util.StringUtils;

public class Css3PieHeaderContributor extends TextTemplateHeaderContributor {

	private static final long serialVersionUID = -4091910399987963790L;
	
	private static final String CSS_TEMPLATE_FILENAME = "include-pie-htc.css";
	
	private static final String PIE_HTC_FILENAME = "PIE.htc";
	
	private static final String STYLES_VARIABLE = "styles";
	
	private static final String PIE_HTC_URL_VARIABLE = "pieHtcUrl";
	
	public static final Css3PieHeaderContributor forStyles(final String[] stylesWithCss3Properties) {
		return new Css3PieHeaderContributor(getTemplate(), getVariablesModel(stylesWithCss3Properties));
	}

	protected Css3PieHeaderContributor(final TextTemplate template, IModel<Map<String, Object>> variablesModel) {
		super(template, variablesModel);
	}
	
	private static final TextTemplate getTemplate() {
		return new PackagedTextTemplate(Css3PieHeaderContributor.class, CSS_TEMPLATE_FILENAME);
	}
	
	private static final IModel<Map<String, Object>> getVariablesModel(String[] stylesWithCss3Properties) {
		Map<String, Object> variables = new MiniMap<String, Object>(2);
		variables.put(STYLES_VARIABLE, StringUtils.arrayToCommaDelimitedString(stylesWithCss3Properties));
		variables.put(PIE_HTC_URL_VARIABLE, RequestCycle.get().urlFor(new ResourceReference(Css3PieHeaderContributor.class, PIE_HTC_FILENAME)).toString());
		
		return Model.ofMap(variables);
	}

}
