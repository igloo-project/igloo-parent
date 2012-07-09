package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemit;

import org.odlabs.wiquery.core.options.ArrayItemOptions;
import org.odlabs.wiquery.core.options.IListItemOption;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.core.options.StringOption;

public class ItemItGroupOptions implements IListItemOption {

	private static final long serialVersionUID = -3899417829688422634L;

	private String name;

	private ArrayItemOptions<LiteralOption> types = new ArrayItemOptions<LiteralOption>();

	private StringOption titleModel;

	private String className;

	public ItemItGroupOptions(String name, String[] types, StringOption titleModel, String className) {
		super();
		this.name = name;
		for (String type : types) {
			this.types.add(new LiteralOption(type));
		}
		this.titleModel = titleModel;
		this.className = className;
	}

	@Override
	public CharSequence getJavascriptOption() {
		Options options = new Options();
		if (name != null) {
			options.putLiteral("name", name);
		}
		if (types != null) {
			options.put("types", types);
		}
		if (titleModel != null) {
			options.putLiteral("title", titleModel.getModel());
		}
		if (className != null) {
			options.putLiteral("className", className);
		}
		return options.getJavaScriptOptions();
	}

}
