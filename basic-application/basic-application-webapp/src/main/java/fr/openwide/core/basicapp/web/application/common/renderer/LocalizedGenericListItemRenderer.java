package fr.openwide.core.basicapp.web.application.common.renderer;

import java.util.Locale;

import fr.openwide.core.basicapp.core.business.common.model.IHierarchicalListItem;
import fr.openwide.core.basicapp.core.business.common.model.LocalizedGenericListItem;
import fr.openwide.core.wicket.more.rendering.LocalizedTextRenderer;
import fr.openwide.core.wicket.more.rendering.Renderer;

public abstract class LocalizedGenericListItemRenderer extends Renderer<LocalizedGenericListItem<?>> {

	private static final long serialVersionUID = -3042035624376063917L;

	private static final Renderer<LocalizedGenericListItem<?>> INSTANCE = new LocalizedGenericListItemRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(LocalizedGenericListItem<?> value, Locale locale) {
			return LocalizedTextRenderer.get().render(value.getLabel(), locale);
		}
	}.nullsAsNull();

	private static final Renderer<LocalizedGenericListItem<?>> CODE_LABEL = new LocalizedGenericListItemRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(LocalizedGenericListItem<?> value, Locale locale) {
			String code = value.getCode();
			return (code != null ? code + " - " : "") + LocalizedTextRenderer.get().render(value.getLabel(), locale);
		}
	}.nullsAsNull();


	private static final Renderer<LocalizedGenericListItem<?>> WITH_PARENT_LABEL = new LocalizedGenericListItemRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(LocalizedGenericListItem<?> value, Locale locale) {
			StringBuilder sb = new StringBuilder();
			if (value instanceof IHierarchicalListItem<?>) {
				LocalizedGenericListItem<?> parent = ((IHierarchicalListItem<?>) value).getParent();
				if (parent != null) {
					sb.append(LocalizedTextRenderer.get().render(parent.getLabel(), locale))
						.append(" - ");
				}
			}
			
			sb.append(LocalizedTextRenderer.get().render(value.getLabel(), locale));
			return sb.toString();
		}
	}.nullsAsNull();
	
	public static final Renderer<LocalizedGenericListItem<?>> get() {
		return INSTANCE;
	}

	public static final Renderer<LocalizedGenericListItem<?>> codeLabel() {
		return CODE_LABEL;
	}

	/**
	 * Should be used only with {@link IHierarchicalListItem} objects. Fallback to a simple label display.
	 * @return
	 */
	public static final Renderer<LocalizedGenericListItem<?>> withParentLabel() {
		return WITH_PARENT_LABEL;
	}
	
	/**
	 * @deprecated Use {@link #get()} instead.
	 */
	@Deprecated
	protected LocalizedGenericListItemRenderer() {
	}

}
