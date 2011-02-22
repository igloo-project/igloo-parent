package fr.openwide.core.wicket.more.markup.html.basic;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.AbstractConverter;

import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;

public class GenericListItemListLabel extends Label {

	private static final long serialVersionUID = -6830982860837635819L;
	
	private static final String DEFAULT_SEPARATOR = ", ";
	
	private final String separator;

	public GenericListItemListLabel(String id, IModel<? extends List<? extends GenericListItem<?>>> model, String separator) {
		super(id, model);
		this.separator = separator;
	}
	
	public GenericListItemListLabel(String id, IModel<? extends List<? extends GenericListItem<?>>> model) {
		this(id, model, DEFAULT_SEPARATOR);
	}
	
	@Override
	public IConverter getConverter(Class<?> type) {
		return new GenericListItemListConverter(separator);
	}
	
	private class GenericListItemListConverter extends AbstractConverter {
		private static final long serialVersionUID = 1L;
		
		private final String separator;
		
		private GenericListItemListConverter(String separator) {
			this.separator = separator;
		}

		@Override
		public Object convertToObject(String value, Locale locale) {
			throw new IllegalAccessError();
		}
		
		@Override
		public String convertToString(Object value, Locale locale) {
			if (value instanceof List) {
				List<?> items = (List<?>) value;
				StringBuilder sb = new StringBuilder();
				for (Object item : items) {
					if (item instanceof GenericListItem) {
						if (sb.length() > 0) {
							sb.append(this.separator);
						}
						sb.append(((GenericListItem<?>) item).getLabel());
					}
				}
				return sb.toString();
			}
			return "";
		}

		@Override
		protected Class<?> getTargetType() {
			return BigDecimal.class;
		}
	}

}
