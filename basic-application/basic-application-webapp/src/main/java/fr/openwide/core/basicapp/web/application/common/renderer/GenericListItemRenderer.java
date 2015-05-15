package fr.openwide.core.basicapp.web.application.common.renderer;

import java.util.Locale;

import com.google.common.base.Optional;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.rendering.Renderer;

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

