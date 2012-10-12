package fr.openwide.core.wicket.markup.html.util.css3pie;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.collections.MiniMap;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.springframework.util.StringUtils;

import fr.openwide.core.commons.util.CloneUtils;

public class Css3PieHeadBehavior extends Behavior {

	private static final long serialVersionUID = -4091910399987963790L;
	
	private static final String CSS_TEMPLATE_FILENAME = "include-pie-htc.css";
	
	private static final String PIE_HTC_FILENAME = "PIE.htc";
	
	private static final String STYLES_VARIABLE = "styles";
	
	private static final String PIE_HTC_URL_VARIABLE = "pieHtcUrl";
	
	private final String[] stylesWithCss3Properties;

	public Css3PieHeadBehavior(String[] stylesWithCss3Properties) {
		super();
		
		this.stylesWithCss3Properties = CloneUtils.clone(stylesWithCss3Properties);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(StringHeaderItem.forString(getTemplate().asString(getVariables(stylesWithCss3Properties))));
	}
	
	private static TextTemplate getTemplate() {
		return new PackageTextTemplate(Css3PieHeadBehavior.class, CSS_TEMPLATE_FILENAME);
	}
	
	private static Map<String, Object> getVariables(final String[] stylesWithCss3Properties) {
		Map<String, Object> variables = new MiniMap<String, Object>(2);
		variables.put(STYLES_VARIABLE, StringUtils.arrayToCommaDelimitedString(stylesWithCss3Properties));
		variables.put(PIE_HTC_URL_VARIABLE,
				RequestCycle.get().urlFor(new PackageResourceReference(Css3PieHeadBehavior.class, PIE_HTC_FILENAME), null).toString());
		
		return variables;
	}

}
