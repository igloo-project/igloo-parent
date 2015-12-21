package fr.openwide.core.wicket.more.markup.html.form;

import static fr.openwide.core.spring.property.SpringPropertyIds.AUTOCOMPLETE_LIMIT;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public abstract class AbstractQuickSearchComponent<T> extends AutocompleteAjaxComponent<T> {
	
	private static final long serialVersionUID = -2026928290852503475L;
	
	@SpringBean
	private IPropertyService propertyService;
	
	private final IPageLinkGenerator ficheLinkGenerator;
	
	public AbstractQuickSearchComponent(String id, final IModel<T> quickSearchModel, IChoiceRenderer<? super T> choiceRenderer,
			final IPageLinkGenerator ficheLinkGenerator) {
		super(id, quickSearchModel, choiceRenderer);
		
		this.ficheLinkGenerator = ficheLinkGenerator;
		
		setAutoUpdate(true);
		getAutocompleteField()
				.setLabel(new ResourceModel("common.quickAccess"))
				.add(new LabelPlaceholderBehavior());
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		ficheLinkGenerator.detach();
	}
	
	@Override
	public boolean isAutoUpdate() {
		return true;
	}
	
	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		super.onUpdate(target);
		if (getModelObject() != null) {
			try {
				ficheLinkGenerator.setResponsePage();
			} finally {
				setModelObject(null);
			}
		}
	}
	
	@Override
	public List<T> getValues(String term) {
		return searchAutocomplete(term, propertyService.get(AUTOCOMPLETE_LIMIT), 0);
	}
	
	protected abstract List<T> searchAutocomplete(String term, int limit, int offset);
	
	@Override
	public T getValueOnSearchFail(String input) {
		return null;
	}
	
	public AbstractQuickSearchComponent<T> labelModel(IModel<String> labelModel) {
		getAutocompleteField().setLabel(labelModel);
		return this;
	}
}
