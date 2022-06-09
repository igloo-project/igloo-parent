package igloo.bootstrap4.markup.html.template.js.bootstrap.scrollspy;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.Options;

import igloo.wicket.model.Models;

/**
 * @deprecated old code not reviewed for a long time. If needed, ask for re-inclusion.
 */
@Deprecated(since = "4.3.0")
public class BootstrapScrollSpy implements IBootstrapScrollSpy {

	private static final long serialVersionUID = 5736038438319652356L;

	private IModel<String> targetModel;

	private Component targetComponent;

	private IModel<Integer> offsetModel;

	public BootstrapScrollSpy() {
		super();
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		
		String target = Models.getObject(targetModel);
		if (target != null) {
			options.put("target", JsUtils.quotes(target, true));
		}
		
		if (targetComponent != null) {
			options.put("target", JsUtils.quotes("#" + targetComponent.getMarkupId(), true));
		}
		
		Integer offset = Models.getObject(offsetModel);
		if (offset != null) {
			options.put("offset", offset);
		}
		
		return new CharSequence[] { options.getJavaScriptOptions() };
	}

	public IModel<String> getTargetModel() {
		return targetModel;
	}

	public BootstrapScrollSpy target(IModel<String> targetModel) {
		this.targetModel = targetModel;
		return this;
	}

	public BootstrapScrollSpy target(String target) {
		return target(Model.of(target));
	}

	public Component getTargetComponent() {
		return targetComponent;
	}

	public BootstrapScrollSpy targetComponent(Component targetComponent) {
		this.targetComponent = targetComponent;
		return this;
	}

	public IModel<Integer> getOffsetModel() {
		return offsetModel;
	}

	public BootstrapScrollSpy offset(IModel<Integer> offsetModel) {
		this.offsetModel = offsetModel;
		return this;
	}

	public BootstrapScrollSpy offset(Integer offset) {
		return offset(Model.of(offset));
	}

}
