package org.iglooproject.wicket.more.markup.html.form;

import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.select2.util.Select2Utils;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.wicketstuff.select2.Settings;

import com.google.common.collect.Lists;

public class BooleanDropDownSingleChoice extends GenericSelect2DropDownSingleChoice<Boolean> {

	private static final long serialVersionUID = 6244269987751271782L;
	
	public BooleanDropDownSingleChoice(String id, IModel<Boolean> model) {
		super(id, model, new BooleanChoicesModel(), new ResourceKeyWithParameterBooleanChoiceRenderer());
	}
	
	public BooleanDropDownSingleChoice(String id, IModel<Boolean> model, String prefix) {
		super(id, model, new BooleanChoicesModel(), new ResourceKeyWithParameterBooleanChoiceRenderer(prefix));
	}
	
	public BooleanDropDownSingleChoice(String id, IModel<Boolean> model, IChoiceRenderer<Boolean> renderer) {
		super(id, model, new BooleanChoicesModel(), renderer);
	}
	
	@Override
	protected void fillSelect2Settings(Settings settings) {
		super.fillSelect2Settings(settings);
		Select2Utils.disableSearch(settings);
	}

	private static class BooleanChoicesModel extends LoadableDetachableModel<List<Boolean>> {
		
		private static final long serialVersionUID = 1L;
		
		public BooleanChoicesModel() {
		}
		
		@Override
		protected List<Boolean> load() {
			return Lists.newArrayList(Boolean.TRUE, Boolean.FALSE);
		}
	}
	
	private static class ResourceKeyWithParameterBooleanChoiceRenderer extends ChoiceRenderer<Boolean> {
		private static final long serialVersionUID = -5914319140045008140L;
		String prefix = null;
		
		public ResourceKeyWithParameterBooleanChoiceRenderer(String prefix) {
			super();
			this.prefix = prefix;
		}

		public ResourceKeyWithParameterBooleanChoiceRenderer() {
			super();
		}

		@Override
		public Object getDisplayValue(Boolean object) {
			if (StringUtils.hasText(prefix)) {
				return BooleanRenderer.withPrefix(prefix).render(object, Session.get().getLocale());
			} else {
				return BooleanRenderer.get().render(object, Session.get().getLocale());
			}
		}
	}

}
