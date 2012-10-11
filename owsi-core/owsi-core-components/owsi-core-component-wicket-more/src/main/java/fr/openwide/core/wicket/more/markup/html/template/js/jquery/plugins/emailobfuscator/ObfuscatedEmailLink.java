package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.emailobfuscator;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

import fr.openwide.core.spring.util.StringUtils;

public class ObfuscatedEmailLink extends MarkupContainer {
	private static final long serialVersionUID = 3120329766081035111L;
	
	private boolean generateLabel;

	private final IModel<String> ccModel;

	private final IModel<String> subjectModel;

	private final IModel<String> bodyModel;

	public ObfuscatedEmailLink(String id, IModel<String> emailModel) {
		this(id, emailModel, false);
	}

	public ObfuscatedEmailLink(String id, IModel<String> emailModel, boolean generateLabel) {
		this(id, emailModel, null, generateLabel);
	}

	public ObfuscatedEmailLink(String id, IModel<String> emailModel, IModel<String> subjectModel, boolean generateLabel) {
		this(id, emailModel, subjectModel, null, generateLabel);
	}

	public ObfuscatedEmailLink(String id, IModel<String> emailModel, IModel<String> subjectModel,
			IModel<String> bodyModel, boolean generateLabel) {
		this(id, emailModel, null, subjectModel, bodyModel, generateLabel);
	}

	public ObfuscatedEmailLink(String id, IModel<String> emailModel, IModel<String> ccModel, IModel<String> subjectModel,
			IModel<String> bodyModel, boolean generateLabel) {
		super(id, emailModel);
		this.ccModel = ccModel;
		this.generateLabel = generateLabel;
		this.subjectModel = wrap(subjectModel);
		this.bodyModel = wrap(bodyModel);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		if (ccModel != null) {
			ccModel.detach();
		}
		if (subjectModel != null) {
			subjectModel.detach();
		}
		if (bodyModel != null) {
			bodyModel.detach();
		}
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		checkComponentTag(tag, "a");
	}

	public JsStatement statement() {
		String[] emailParts = StringUtils.split(getDefaultModelObjectAsString(), "@");
		String cc;
		String subject;
		String body;
		
		if (ccModel != null && ccModel.getObject() != null) {
			cc = ccModel.getObject().replaceAll("@","µ");
		} else {
			cc = "";
		}
		if (subjectModel != null && subjectModel.getObject() != null) {
			subject = subjectModel.getObject();
		} else {
			subject = "";
		}
		if (bodyModel != null && bodyModel.getObject() != null) {
			body = bodyModel.getObject();
		} else {
			body = "";
		}
		if (emailParts != null) {
			return new JsStatement().$(this).chain("obfuscateEmail",
					JsUtils.quotes(emailParts[0], true),
					JsUtils.quotes(emailParts[1], true),
					JsUtils.quotes(cc, true),
					JsUtils.quotes(subject, true),
					JsUtils.quotes(body, true),
					Boolean.valueOf(generateLabel).toString() // generateLabel with email ?
			);
		}
		return null;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(EmailObfuscatorJavascriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}
}
