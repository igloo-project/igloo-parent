package igloo.bootstrap4.markup.html.template.js.bootstrap;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.options.DefaultOptionsRenderer;
import org.wicketstuff.wiquery.core.options.ICollectionItemOptions;
import org.wicketstuff.wiquery.core.options.IComplexOption;
import org.wicketstuff.wiquery.core.options.IModelOption;
import org.wicketstuff.wiquery.core.options.IOptionsRenderer;
import org.wicketstuff.wiquery.core.options.ITypedOption;

public abstract class SimpleOptions implements IDetachable {

	private static final long serialVersionUID = 1L;

	private Component owner;

	private IOptionsRenderer optionsRenderer;

	public SimpleOptions() {
		this(null);
	}

	public SimpleOptions(Component owner) {
		this.owner = owner;
		this.optionsRenderer = DefaultOptionsRenderer.get();
	}

	public void setOwner(Component owner) {
		if (this.owner != null && this.owner != owner)
			throw new IllegalArgumentException("Cannot use the same Options for multiple components");
		this.owner = owner;
	}

	/**
	 * Returns the JavaScript statement corresponding to options.
	 */
	protected CharSequence getJavaScriptOptions(Map<String, Object> options) {
		StringBuilder sb = new StringBuilder();
		this.optionsRenderer.renderBefore(sb);
		int count = 0;
		for (Entry<String, Object> entry : options.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof IModelOption<?>)
				value = ((IModelOption<?>) value).wrapOnAssignment(owner);
			boolean isLast = !(count < options.size() - 1);
			if (value instanceof JsScope) {
				// Case of a JsScope
				sb.append(this.optionsRenderer.renderOption(key, ((JsScope) value).render(), isLast));
			} else if (value instanceof ICollectionItemOptions) {
				// Case of an ICollectionItemOptions
				sb.append(this.optionsRenderer.renderOption(key,
						((ICollectionItemOptions) value).getJavascriptOption(), isLast));
			} else if (value instanceof IComplexOption) {
				// Case of an IComplexOption
				sb.append(this.optionsRenderer.renderOption(key, ((IComplexOption) value).getJavascriptOption(), isLast));
			} else if (value instanceof ITypedOption<?>) {
				// Case of an ITypedOption
				sb.append(this.optionsRenderer.renderOption(key, ((ITypedOption<?>) value).getJavascriptOption(),
						isLast));
			} else {
				// Other cases
				sb.append(this.optionsRenderer.renderOption(key, value, isLast));
			}
			count++;
		}
		this.optionsRenderer.renderAfter(sb);
		return sb.toString();
	}

	
	/**
	 * Sets the renderer to use.
	 */
	public void setRenderer(IOptionsRenderer optionsRenderer) {
		this.optionsRenderer = optionsRenderer;
	}

}