package org.iglooproject.wicket.more.rendering;

import static org.iglooproject.spring.util.StringUtils.emptyTextToNull;

import java.util.Locale;

import com.google.common.base.Optional;

import org.iglooproject.commons.util.functional.Joiners;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.rendering.Renderer;

public abstract class GenericListItemRenderer extends Renderer<GenericListItem<?>> {

	private static final long serialVersionUID = -3042035624376063917L;

	private static final Renderer<GenericListItem<?>> INSTANCE = new GenericListItemRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(GenericListItem<?> value, Locale locale) {
			return value.getLabel();
		}
	}.nullsAsNull();

	private static final Renderer<GenericListItem<?>> CODE = new GenericListItemRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(GenericListItem<?> value, Locale locale) {
			return value.getCode();
		}
	}.nullsAsNull();

	private static final Renderer<GenericListItem<?>> CODE_LABEL = new GenericListItemRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(GenericListItem<?> value, Locale locale) {
			return Joiners.onHyphenSpace()
					.join(
							emptyTextToNull(value.getCode()),
							emptyTextToNull(value.getLabel())
					);
		}
	}.nullsAsNull();

	private static final Renderer<GenericListItem<?>> SHORT_LABEL = new GenericListItemRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(GenericListItem<?> value, Locale locale) {
			return Optional.fromNullable(StringUtils.emptyTextToNull(value.getShortLabel()))
					.or(Optional.fromNullable(StringUtils.emptyTextToNull(value.getLabel())))
					.orNull();
		}
	}.nullsAsNull();

	public static final Renderer<GenericListItem<?>> get() {
		return INSTANCE;
	}

	public static final Renderer<GenericListItem<?>> code() {
		return CODE;
	}
	
	public static final Renderer<GenericListItem<?>> codeLabel() {
		return CODE_LABEL;
	}

	public static final Renderer<GenericListItem<?>> shortLabel() {
		return SHORT_LABEL;
	}

	/**
	 * @deprecated Use {@link #get()} instead.
	 */
	@Deprecated
	protected GenericListItemRenderer() {
	}

}

