package fr.openwide.core.showcase.web.application.widgets.component;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel.CarouFredSel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel.CarouFredSelBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.Modal;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.ModalDiaporamaBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.ModalHeaderFooterBehavior;

public class ImageCarouselPanel extends Panel {

	private static final long serialVersionUID = 4562012379117930236L;

	private static final int CAROUSEL_WIDTH = 212;
	private static final int CAROUSEL_HEIGHT = 212;

	private final String diaporamaId;

	public ImageCarouselPanel(String id, String diaporamaId) {
		super(id);
		
		this.diaporamaId = diaporamaId;
		
		final MarkupContainer diaporamaContainer = new WebMarkupContainer("container");
		diaporamaContainer.setOutputMarkupId(true);
		add(diaporamaContainer);
		
		List<DummyImageBean> imageBeans = Lists.newArrayList(
				new DummyImageBean("logo_openwide.png", "widgets.carousel.openwide.header", "widgets.carousel.openwide.footer"),
				new DummyImageBean("logo_sitra.jpg", "widgets.carousel.sitra.header", "widgets.carousel.sitra.footer"),
				new DummyImageBean("logo_sytral.jpg", "widgets.carousel.sytral.header", "widgets.carousel.sytral.footer"),
				new DummyImageBean("logo_vuitton.jpg", "widgets.carousel.vuitton.header", "widgets.carousel.vuitton.footer")
		);
		
		diaporamaContainer.add(new ListView<DummyImageBean>("imageItem", imageBeans) {
			private static final long serialVersionUID = 4148116695921332880L;
			@Override
			protected void populateItem(ListItem<DummyImageBean> item) {
				DummyImageBean imageBean = item.getModelObject();
				
				Component header = new Label("header", new ResourceModel(imageBean.getHeaderLabelKey()));
				header.setOutputMarkupId(true);
				item.add(header);
				
				WebMarkupContainer footer = new WebMarkupContainer("footer");
				footer.add(new Label("footerLabel", new ResourceModel(imageBean.getFooterLabelKey())));
				footer.setOutputMarkupId(true);
				item.add(footer);
				
				WebComponent image = new ContextImage("image", "static/application/images/" + imageBean.getImagePath());
				
				AbstractLink imageLink = new ResourceLink<Void>("imageLink",
						new ContextRelativeResource("static/application/util/template/images/" + imageBean.getImagePath()));
				imageLink.add(new AttributeModifier("rel", ImageCarouselPanel.this.diaporamaId));
				imageLink.add(new ModalHeaderFooterBehavior(header, footer));
				imageLink.add(image);
				item.add(imageLink);
			}
		});
		
		final Component nextButton = new WebMarkupContainer("nextButton");
		nextButton.setOutputMarkupId(true);
		add(nextButton);
		
		final Component prevButton = new WebMarkupContainer("prevButton");
		prevButton.setOutputMarkupId(true);
		add(prevButton);
		
		diaporamaContainer.add(new CarouFredSelBehavior(getCarouFredSel(prevButton, nextButton)));
		diaporamaContainer.add(new ModalDiaporamaBehavior("a[rel=" + ImageCarouselPanel.this.diaporamaId +"]", new Modal()));
	}

	protected CarouFredSel getCarouFredSel(Component prevButton, Component nextButton) {
		CarouFredSel carouFredSel = new CarouFredSel();
		carouFredSel.setAutoPlay(false);
		carouFredSel.setNextButton(nextButton);
		carouFredSel.setPreviousButton(prevButton);
		carouFredSel.setItemsVisible(1);
		carouFredSel.setInfinite(false);
		carouFredSel.setCircular(false);
		carouFredSel.setWidth(CAROUSEL_WIDTH);
		carouFredSel.setHeight(CAROUSEL_HEIGHT);
		return carouFredSel;
	}

	private class DummyImageBean implements Serializable {
		private static final long serialVersionUID = 1950186970223043671L;
		
		private String imagePath;
		
		private String headerLabelKey;
		
		private String footerLabelKey;
		
		public DummyImageBean(String imagePath, String headerLabelKey, String footerLabelKey) {
			super();
			this.imagePath = imagePath;
			this.headerLabelKey = headerLabelKey;
			this.footerLabelKey = footerLabelKey;
		}
		
		public String getImagePath() {
			return imagePath;
		}
		
		public String getHeaderLabelKey() {
			return headerLabelKey;
		}
		
		public String getFooterLabelKey() {
			return footerLabelKey;
		}
	}
}
