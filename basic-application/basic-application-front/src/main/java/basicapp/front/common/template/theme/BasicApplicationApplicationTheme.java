package basicapp.front.common.template.theme;

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;

import basicapp.front.common.component.ApplicationEnvironmentPanel;
import basicapp.front.common.template.MainTemplate;
import igloo.wicket.condition.Condition;

public enum BasicApplicationApplicationTheme {

	BASIC {
		@Override
		public String getMarkupVariation() {
			return "basic";
		}
		
		@Override
		public void renderHead(IHeaderResponse response) {
			response.render(CssHeaderItem.forReference(basicapp.front.common.template.resources.styles.application.application.applicationbasic.StylesScssResourceReference.get()));
		}
		
		@Override
		public void specificContent(
			MainTemplate mainTemplate,
			Supplier<List<NavigationMenuItem>> mainNavSupplier,
			Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
			Supplier<Class<? extends WebPage>> secondMenuPageSupplier
		) {
			mainTemplate.add(
				new basicapp.front.common.template.theme.basic.NavbarPanel(
					"navbar",
					mainNavSupplier,
					firstMenuPageSupplier,
					secondMenuPageSupplier
				),
				
				new ApplicationEnvironmentPanel("environment"),
				
				new basicapp.front.common.template.theme.basic.FooterPanel("footer")
			);
		}
		
		@Override
		public BasicApplicationApplicationTheme next() {
			return BasicApplicationApplicationTheme.ADVANCED;
		}
	},
	ADVANCED {
		@Override
		public String getMarkupVariation() {
			return "advanced";
		}
		
		@Override
		public void renderHead(IHeaderResponse response) {
			response.render(CssHeaderItem.forReference(basicapp.front.common.template.resources.styles.application.application.applicationadvanced.StylesScssResourceReference.get()));
		}
		
		@Override
		public void specificContent(
			MainTemplate mainTemplate,
			Supplier<List<NavigationMenuItem>> mainNavSupplier,
			Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
			Supplier<Class<? extends WebPage>> secondMenuPageSupplier
		) {
			mainTemplate.add(
				new basicapp.front.common.template.theme.advanced.NavbarPanel(
					"navbar",
					firstMenuPageSupplier
				),
				
				new basicapp.front.common.template.theme.advanced.SidebarPanel(
					"sidebar",
					mainNavSupplier,
					firstMenuPageSupplier,
					secondMenuPageSupplier
				)
			);
		}
		
		@Override
		public BasicApplicationApplicationTheme next() {
			return BasicApplicationApplicationTheme.BASIC;
		}
	};

	public abstract String getMarkupVariation();

	public abstract void renderHead(IHeaderResponse response);

	public abstract void specificContent(
		MainTemplate mainTemplate,
		Supplier<List<NavigationMenuItem>> mainNavSupplier,
		Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
		Supplier<Class<? extends WebPage>> secondMenuPageSupplier
	);

	public abstract BasicApplicationApplicationTheme next();

	public Condition isEqual(BasicApplicationApplicationTheme applicationTheme) {
		if (applicationTheme == null) {
			return Condition.alwaysFalse();
		}
		return Condition.isEqual(Model.of(this), Model.of(applicationTheme));
	}

	public Condition isBasic() {
		return isEqual(BasicApplicationApplicationTheme.BASIC);
	}

	public Condition isAdvanced() {
		return isEqual(BasicApplicationApplicationTheme.ADVANCED);
	}

}
