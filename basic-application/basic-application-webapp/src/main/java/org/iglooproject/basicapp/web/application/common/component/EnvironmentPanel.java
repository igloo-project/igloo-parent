package org.iglooproject.basicapp.web.application.common.component;

import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.cookies.CookieUtils;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.ajax.AjaxListeners;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.util.model.Detachables;

import com.google.common.collect.ImmutableList;

public class EnvironmentPanel extends Panel {

	private static final long serialVersionUID = -916735857360352450L;

	private static final List<Environment> VISIBLE_ALERTS = ImmutableList.of(Environment.development, Environment.staging);

	private static final CookieUtils COOKIE_UTILS = new CookieUtils();

	private static final String COOKIE_CLOSE_NAME = "environmentAlertClose";

	private final IModel<Environment> environmentModel;

	private final IModel<Boolean> closeModel = Model.of(Boolean.FALSE);

	public EnvironmentPanel(String id) {
		this(id, BasicApplicationSession.get().getEnvironmentModel());
	}

	public EnvironmentPanel(String id, IModel<Environment> environmentModel) {
		super(id);
		setOutputMarkupId(true);
		
		this.environmentModel = environmentModel;
		
		add(
				new AjaxLink<Void>("main") {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target) {
						Cookie cookie = COOKIE_UTILS.getCookie(COOKIE_CLOSE_NAME);
						
						if (cookie == null) {
							COOKIE_UTILS.save(COOKIE_CLOSE_NAME, Boolean.TRUE.toString());
							closeModel.setObject(Boolean.TRUE);
						} else {
							COOKIE_UTILS.remove(COOKIE_CLOSE_NAME);
							closeModel.setObject(Boolean.FALSE);
						}
						
						AjaxListeners.add(target, AjaxListeners.refresh(EnvironmentPanel.this));
					}
				}
						.add(
								new EnclosureContainer("container")
										.condition(Condition.isFalse(closeModel))
										.add(new EnumLabel<>("environment", environmentModel))
						)
		);
		
		add(
				new ClassAttributeAppender(environmentModel),
				new ClassAttributeAppender(Condition.isTrue(closeModel).then("header-alert-section-dismissed").otherwise(""))
		);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		if (COOKIE_UTILS.getCookie(COOKIE_CLOSE_NAME) != null) {
			closeModel.setObject(Boolean.TRUE);
		}
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setVisible(
				VISIBLE_ALERTS.contains(environmentModel.getObject())
			||	BasicApplicationSession.get().getUser() instanceof TechnicalUser
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(environmentModel, closeModel);
	}

}