package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.wicketstuff.wiquery.core.javascript.JsQuery;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeContext;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.springframework.context.ApplicationContext;

import org.iglooproject.wicket.more.application.CoreWicketApplication;

public abstract class AutocompleteAjaxComponent<T> extends org.wicketstuff.wiquery.ui.autocomplete.AutocompleteAjaxComponent<T> {

	private static final long serialVersionUID = 2543997784221712556L;

	private WebMarkupContainer cleanLink;

	public AutocompleteAjaxComponent(String id, IModel<T> model, IChoiceRenderer<? super T> choiceRenderer) {
		super(id, model, choiceRenderer);
		
		cleanLink = new WebMarkupContainer("cleanLink");
		cleanLink.setOutputMarkupId(true);
		
		add(cleanLink);
	}

	public AutocompleteAjaxComponent(String id, IModel<T> model) {
		this(id, model, null);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(OnDomReadyHeaderItem.forScript(getAutocompleteStatement()));
	}

	public CharSequence getAutocompleteStatement() {
		JsScope jsScope = new JsScope() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(JsScopeContext scopeContext) {
				// cette suite d'événement permet de simuler de manière correcte le vidage du champ, et permet
				// en particulier de déclencher le autoUpdate (appel Ajax) vers Wicket.
				// focus du champ -> on vide -> on quitte le champ
				scopeContext.append(new JsQuery(getAutocompleteField()).$().chain("trigger", "'focus'").render());
				scopeContext.append(new JsStatement().$(getAutocompleteField()).chain("val", "''").render());
				scopeContext.append(new JsStatement().$(getAutocompleteHidden()).chain("val", "''").render());
				scopeContext.append(new JsQuery(getAutocompleteField()).$().chain("trigger", "'blur'").render());
			}
		};
		JsStatement jsStatement = new JsStatement().$(cleanLink).chain("bind", "'click'", jsScope.render());
		
		// Sur les modifications du champ autocomplete, on vérifie si le champ est vide de manière à vider aussi
		// le champ caché
		JsScope clearHiddenField = JsScope.quickScope(new JsStatement()
				.append("if (")
				.append(new JsStatement().$(getAutocompleteField()).chain("val").render(false))
				.append(" == '') { ") // si le champ autocomplete est vide
				.append(new JsStatement().$(getAutocompleteHidden()).chain("val", "''").render(true)) // alors on vide le champ identifiant
				.append(" }"));
		
		jsStatement.append(";").$(getAutocompleteField()).chain("bind", "'change'", clearHiddenField.render());
		return jsStatement.render();
	}

	protected <SQ> SQ getBean(Class<SQ> clazz) {
		return getContext().getBean(clazz);
	}

	protected <SQ> SQ getBean(Class<SQ> clazz, Object... args) {
		return getContext().getBean(clazz, args);
	}

	private ApplicationContext getContext() {
		return CoreWicketApplication.get().getApplicationContext();
	}

}
