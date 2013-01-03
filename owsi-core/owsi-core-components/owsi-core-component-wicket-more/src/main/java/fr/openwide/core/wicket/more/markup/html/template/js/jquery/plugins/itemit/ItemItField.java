package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemit;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.codehaus.jackson.map.ObjectMapper;
import org.odlabs.wiquery.core.javascript.JsStatement;

import com.google.common.collect.Lists;

public abstract class ItemItField<F, J> extends FormComponentPanel<List<F>> {

	private static final long serialVersionUID = -9155020419420538737L;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private AjaxItemItBehavior<F, J> completeBehavior;

	private ItemIt itemIt;

	public abstract List<F> getValues(String term);

	public abstract J newAutocompleteJson(Integer index, F item, Locale locale);

	public abstract F convertValue(J value);

	public abstract Class<J> getJsonType();

	public ItemItField(String id, IModel<List<F>> selectedItemListModel) {
		this(id, selectedItemListModel, null);
	}

	public ItemItField(String id, IModel<List<F>> selectedItemListModel, ItemIt itemIt) {
		super(id, selectedItemListModel);
		if (itemIt == null) {
			this.itemIt = new ItemIt();
		} else {
			this.itemIt = itemIt;
		}
		add(new ListView<F>("itemList", selectedItemListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<F> item) {
				WebMarkupContainer field = new WebMarkupContainer("item");
				field.add(new AttributeAppender("value", new JsonModel(item.getModel())));
				field.add(new AttributeAppender("name", new InputNameModel()));
				item.add(field);
			}
		});
		
		completeBehavior = new AjaxItemItBehavior<F, J>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<F> getValues(String term) {
				return ItemItField.this.getValues(term);
			}

			@Override
			public J newAutocompleteJson(Integer index, F item, Locale locale) {
				return ItemItField.this.newAutocompleteJson(index, item, locale);
			}

			@Override
			public Class<?> getJsonType() {
				return ItemItField.this.getJsonType();
			}
		};
		add(completeBehavior);
	}

	@Override
	protected List<F> convertValue(String[] values) throws ConversionException {
		if (values == null) {
			return Lists.newArrayList();
		} else {
			List<F> convertedValues = Lists.newArrayList();
			for (String value : values) {
				try {
					J convertedValue = OBJECT_MAPPER.readValue(value, getJsonType());
					convertedValues.add(convertValue(convertedValue));
				} catch (Exception e) {
					throw new ConversionException(e);
				}
			}
			return convertedValues;
		}
	}

	@Override
	public void onBeforeRender() {
		itemIt.setInternalJsonSource(completeBehavior.getCallbackUrl().toString());
		itemIt.setFieldName(getInputName());
		super.onBeforeRender();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(ItemItJavascriptResourceReference.get()));
		
		JsStatement statement = statement();
		if (statement != null) {
			response.render(OnDomReadyHeaderItem.forScript(statement().render()));
		}
	}
	
	protected JsStatement statement() {
		return new JsStatement().$(this).chain(itemIt);
	}

	public class InputNameModel implements IModel<String> {

		private static final long serialVersionUID = -3856892968691396356L;

		@Override
		public void detach() {
			// nothing
		}

		@Override
		public String getObject() {
			return getInputName();
		}

		@Override
		public void setObject(String value) {
			throw new IllegalStateException("Not supported");
		}
		
	}

	public class JsonModel implements IModel<String> {

		private static final long serialVersionUID = 6384538050831419296L;

		private IModel<F> objectModel;

		public JsonModel(IModel<F> objectModel) {
			super();
			
			this.objectModel = objectModel;
		}

		@Override
		public void detach() {
			if (this.objectModel != null) {
				this.objectModel.detach();
			}
		}

		@Override
		public String getObject() {
			if (objectModel != null) {
				try {
					return OBJECT_MAPPER.writeValueAsString(newAutocompleteJson(null, objectModel.getObject(), getLocale()));
				} catch (Exception e) {
					throw new IllegalStateException("Unable to serialize the model object", e);
				}
			} else {
				return "";
			}
		}

		@Override
		public void setObject(String object) {
		}

	}

	protected ItemIt getItemItOptions() {
		return itemIt;
	}

}
