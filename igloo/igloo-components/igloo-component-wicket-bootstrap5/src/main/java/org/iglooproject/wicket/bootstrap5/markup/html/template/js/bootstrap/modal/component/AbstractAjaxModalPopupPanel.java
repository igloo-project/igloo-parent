package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.iglooproject.bootstrap.api.IAjaxModalPopupPanel;
import org.iglooproject.bootstrap.api.IAjaxModalShowListener;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;

public abstract class AbstractAjaxModalPopupPanel<O> extends AbstractModalPopupPanel<O> implements IAjaxModalPopupPanel {

	private static final long serialVersionUID = 2483564542384270295L;

	private final boolean resetInputOnShow;

	private boolean shown = false;

	/**
	 * Le fonctionnement par défaut provoque le vidage des saisies utilisateur lors du show. Utiliser le constructeur
	 * avec le paramètre {@link AbstractAjaxModalPopupPanel#resetInputOnShow} si vous désirez conserver les saisies
	 * utilisateur après ouverture / fermeture du popup.
	 */
	public AbstractAjaxModalPopupPanel(String id, IModel<? extends O> model) {
		this(id, model, true);
	}

	/**
	 * 
	 * 
	 * @param id identifiant wicket du composant
	 * @param model modèle du composant
	 * @param resetInputOnShow indique si le ou les formulaires doivent être réinitialisée lors du show (ouverture). La
	 *                         réinitialisation consiste à ne pas prendre en compte les saisies utilisateur précédentes
	 *                         et à se baser intégralement sur le modèle. Ce fonctionnement implique un parcours du
	 *                         composant pour chercher les éléments de type {@link Form}
	 */
	public AbstractAjaxModalPopupPanel(String id, IModel<? extends O> model, boolean resetInputOnShow) {
		super(id, model);
		this.resetInputOnShow = resetInputOnShow;
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		getBody().setVisible(shown);
		getHeader().setVisible(shown);
		getFooter().setVisible(shown);
	}

	@Override
	public final void show(AjaxRequestTarget target) {
		shown = true;
		getBody().setVisible(shown);
		getHeader().setVisible(shown);
		getFooter().setVisible(shown);
		target.add(getContainer());
		onShow(target);
		if (resetInputOnShow) {
			visitChildren(Form.class, new IVisitor<Form<?>, Void>() {
				@Override
				public void component(Form<?> component, IVisit<Void> visit) {
					component.clearInput();
				}
			});
		}
		visitChildren(IAjaxModalShowListener.class, new OnShowVisitor<>(target));
		FeedbackUtils.refreshFeedback(target, getPage());
	}
	
	private class OnShowVisitor<T extends Component & IAjaxModalShowListener> implements IVisitor<T, Void> {
		
		private final AjaxRequestTarget target;
		
		public OnShowVisitor(AjaxRequestTarget target) {
			super();
			this.target = target;
		}

		@Override
		public void component(T object, IVisit<Void> visit) {
			object.onShow(AbstractAjaxModalPopupPanel.this, target);
		}
	}

	protected final void closePopup(AjaxRequestTarget target) {
		target.prependJavaScript(closeStatement().render(true));
	}

	protected void onShow(AjaxRequestTarget target) {
	}

}
