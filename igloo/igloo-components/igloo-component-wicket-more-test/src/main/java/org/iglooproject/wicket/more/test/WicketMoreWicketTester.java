package org.iglooproject.wicket.more.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.tester.TagTester;
import org.iglooproject.test.wicket.core.CoreWicketTester;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.basic.CountLabel;
import org.iglooproject.wicket.more.link.descriptor.impl.DynamicBookmarkablePageLink;
import org.iglooproject.wicket.more.markup.html.template.component.BreadCrumbListView;
import org.iglooproject.wicket.more.markup.html.template.component.LinkGeneratorBreadCrumbElementPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractModalPopupPanel;

public class WicketMoreWicketTester extends CoreWicketTester {

	public WicketMoreWicketTester(WebApplication application) {
		super(application);
	}

	/**
	 * Assert the escape text of a Label Component
	 */
	public void assertEscapeLabel(String path, String expectedValue) {
		assertLabel(path, Strings.escapeMarkup(expectedValue, false, false).toString());
	}

	/**
	 * Assert the not escape text of a Label Component
	 */
	public void assertNotEscapeLabel(String path, String actualValue) {
		assertLabel(path, actualValue);
	}

	/**
	 * Assert that a Component is a BreadCrumbLinkElement and displays expectedValue
	 */
	public void assertBreadcrumbLinkElement(String path, int element, String expectedValue) {
		assertVisible(path);
		assertComponent(path, BreadCrumbListView.class);
		
		String elementPath = path + ":" + element + ":breadCrumbElement";
		assertVisible(elementPath);
		assertComponent(elementPath, LinkGeneratorBreadCrumbElementPanel.class);
		String elementLinkPath = elementPath + ":breadCrumbElementLink";
		assertUsability(elementLinkPath);
		assertComponent(elementLinkPath, Link.class);
		@SuppressWarnings("unchecked")
		Link<Void> link = (Link<Void>) getComponentFromLastRenderedPage(elementLinkPath);
		String label = (String) link.getBody().getObject();
		assertEquals(label, expectedValue);
	}

	/**
	 * Assert a Component is a CountLabel and displays expectedValue
	 */
	public void assertCountLabel(String path, String expectedValue) {
		assertVisible(path, CountLabel.class);
		assertNotEscapeLabel(path, expectedValue);
	}

	/**
	 * Assert a Component is a usabled DynamicBookmarkablePageLink and points to the page
	 */
	public <P extends Page>void assertDynamicBookmarkablePageLinkUsability(String path, Class<P> pageClass) {
		DynamicBookmarkablePageLink linkComponent = assertUsability(path, DynamicBookmarkablePageLink.class);
		assertTrue(linkComponent.isPageAccessible(pageClass));
	}

	/**
	 * Assert a Component is a disabled DynamicBookmarkablePageLink and points to the page
	 */
	public <P extends Page>void assertDynamicBookmarkablePageLinkDisabled(String path, Class<P> pageClass) {
		DynamicBookmarkablePageLink linkComponent = assertDisabled(path, DynamicBookmarkablePageLink.class);
		assertTrue(linkComponent.isPageAccessible(pageClass));
	}

	/**
	 * Assert a Component is visible, of type popupClass (extends AbstractModalPopupPanel)
	 * and that the internal component are invisible
	 */
	public <T extends AbstractModalPopupPanel<?>> void assertModalBeforeOpening(String modalPath, String modalTitle, Class<T> popupClass,
		List<String> bodyFirstChildElementsId, List<String> footerFirstChildElementsId) {
		assertVisible(modalPath, popupClass);
		
		assertVisible(modalPath + ":container");
		
		// The elements present in AbstractModalPopupPanel.html should be visible (such as the header)
		// Header
		assertVisible(modalPath + ":container:dialog:header", CoreLabel.class);
		assertEscapeLabel(modalPath + ":container:dialog:header", modalTitle);
		// Body elements should be invisible
		bodyFirstChildElementsId.stream().forEach(id -> assertInvisible(modalPath + ":container:dialog:body:" + id));
		// Footer elements should be invisible
		footerFirstChildElementsId.stream().forEach(id -> assertInvisible(modalPath + ":container:dialog:footer:" + id));
	}

	/**
	 * Assert that a Component has a tooltip that displays expectedValue
	 */
	public void assertTooltip(String path, String expectedValue) {
		Component component = getComponentFromLastRenderedPage(path);
		TagTester tagTester = TagTester.createTagByAttribute(getLastResponse().getDocument(), "id", component.getMarkupId());
		assertNotNull(tagTester);
		assertEquals(tagTester.getAttribute("title"), expectedValue);
	}
}
