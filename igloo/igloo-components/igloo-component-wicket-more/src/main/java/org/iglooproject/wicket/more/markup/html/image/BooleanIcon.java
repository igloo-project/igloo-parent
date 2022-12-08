package org.iglooproject.wicket.more.markup.html.image;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import igloo.wicket.behavior.ClassAttributeAppender;

/**
 * Container affichant une icône selon que le model soit true ou false.
 * Par défaut un carré coché ou non avec possibilité de personnaliser.
 */
public class BooleanIcon extends WebMarkupContainer {

	private static final long serialVersionUID = 6413843086031282399L;
	
	private static final String DEFAULT_TRUE_ICON_CLASS = "fa fa-fw fa-check fa-check-square-o";
	private static final String DEFAULT_FALSE_ICON_CLASS = "fa fa-fw fa-square fa-square-o";
	
	private static final String DEFAULT_TRUE_HIDE_IF_NULL_OR_FALSE_ICON_CLASS = "fa fa-fw fa-check";
	
	private static final String BOOLEAN_ICON_BASE_CLASS = "boolean-icon";
	
	private IModel<Boolean> booleanModel;
	
	private IModel<String> trueIconClassModel;
	
	private IModel<String> falseIconClassModel;
	
	private boolean hideIfNullOrFalse = false;
	
	public BooleanIcon(String id, IModel<Boolean> booleanModel) {
		this(id, booleanModel, null, null);
	}
	
	public BooleanIcon(String id, IModel<Boolean> booleanModel,
			IModel<String> trueIconClassModel, IModel<String> falseIconClassModel) {
		super(id, booleanModel);
		this.booleanModel = booleanModel;
		this.trueIconClassModel = trueIconClassModel;
		this.falseIconClassModel = falseIconClassModel;
		
		add(new ClassAttributeAppender(BOOLEAN_ICON_BASE_CLASS));
		add(new ClassAttributeAppender(new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String load() {
				return Boolean.TRUE.equals(getBooleanModelObject()) ? getTrueIconClass() : getFalseIconClass();
			}
		}));
	}
	
	private Boolean getBooleanModelObject() {
		return booleanModel != null ? booleanModel.getObject() : null;
	}
	
	private String getTrueIconClass() {
		if (trueIconClassModel == null) {
			if (hideIfNullOrFalse) {
				trueIconClassModel = new Model<>(DEFAULT_TRUE_HIDE_IF_NULL_OR_FALSE_ICON_CLASS);
			} else {
				trueIconClassModel = new Model<>(DEFAULT_TRUE_ICON_CLASS);
			}
		}
		return trueIconClassModel.getObject();
	}
	
	private String getFalseIconClass() {
		if (falseIconClassModel == null) {
			falseIconClassModel = new Model<>(DEFAULT_FALSE_ICON_CLASS);
		}
		return falseIconClassModel.getObject();
	}
	
	public BooleanIcon hideIfNullOrFalse() {
		this.hideIfNullOrFalse = true;
		return this;
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		setVisible(Boolean.TRUE.equals(getBooleanModelObject()) || !hideIfNullOrFalse);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		if (trueIconClassModel != null) {
			trueIconClassModel.detach();
		}
		if (falseIconClassModel != null) {
			falseIconClassModel.detach();
		}
	}
}
