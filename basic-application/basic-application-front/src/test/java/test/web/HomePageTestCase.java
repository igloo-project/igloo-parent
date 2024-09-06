package test.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import basicapp.front.common.template.theme.advanced.SidebarMenuPanel;
import basicapp.front.navigation.page.HomePage;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.visit.IVisitor;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import org.junit.jupiter.api.Test;
import test.web.config.spring.SpringBootTestBasicApplicationWebapp;

@SpringBootTestBasicApplicationWebapp
class HomePageTestCase extends AbstractBasicApplicationWebappTestCase {

  @Test
  void initPage() {
    tester.startPage(HomePage.class);
    tester.assertRenderedPage(HomePage.class);
  }

  @Test
  void switchLocale() {
    tester.getSession().setLocale(Locale.ENGLISH);
    tester.startPage(HomePage.class);
    tester.assertRenderedPage(HomePage.class);
  }

  @Test
  void initPageUserAuthenticated() throws ServiceException, SecurityServiceException {
    authenticateUser(basicUser);

    tester.startPage(HomePage.class);
    tester.assertRenderedPage(HomePage.class);

    tester.assertEnabled("profile");

    tester.assertInvisible("users");
    tester.assertInvisible("referenceData");
  }

  @Test
  void checkImageAlt() throws ServiceException, SecurityServiceException {
    authenticateUser(basicUser);

    tester.startPage(HomePage.class);
    tester.assertRenderedPage(HomePage.class);

    TagTester tagTester =
        TagTester.createTagByAttribute(
            tester.getLastResponse().getDocument(), "class", "sidebar-footer");
    assertEquals("Logo Igloo", tagTester.getChild("img").getAttribute("alt"));
  }

  @Test
  void sidebarMenuUserAuthenticated() throws ServiceException, SecurityServiceException {
    authenticateUser(basicUser);

    tester.startPage(HomePage.class);
    tester.assertRenderedPage(HomePage.class);

    sidebarMenu(1);
  }

  @Test
  void sidebarMenuUserAdmin() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(HomePage.class);
    tester.assertRenderedPage(HomePage.class);

    sidebarMenu(8);
  }

  private void sidebarMenu(int nbExpectedItems) {
    tester.assertVisible("sidebar:menu", SidebarMenuPanel.class);

    final MutableInt countVisibleItems = new MutableInt(0);
    tester.assertVisible("sidebar:menu:sidenavContainer:sidenavItems", ListView.class);
    @SuppressWarnings("unchecked")
    ListView<NavigationMenuItem> menuItems =
        (ListView<NavigationMenuItem>)
            tester.getComponentFromLastRenderedPage("sidebar:menu:sidenavContainer:sidenavItems");

    menuItems.visitChildren(
        ListItem.class,
        (IVisitor<ListItem<NavigationMenuItem>, Void>)
            (object, visit) -> {
              if (!(object.getModelObject() instanceof NavigationMenuItem)) {
                return;
              }

              if (object.determineVisibility()) {
                countVisibleItems.increment();
              }

              if (object.getModelObject().getSubMenuItems().isEmpty()) {
                visit.dontGoDeeper();
              }
            });

    int countAccessibleItems =
        sidebarMenuComponents(NavbarItem.menu(), "sidebar:menu:sidenavContainer:sidenavItems");
    assertEquals(nbExpectedItems, countAccessibleItems);
    assertEquals(nbExpectedItems, countVisibleItems.getValue().intValue());
  }

  private int sidebarMenuComponents(List<NavbarItem> menu, String pathToMenu) {
    int nbItems = 0;
    int itemIndex = 0;

    for (NavbarItem menuItem : menu) {
      boolean hasRoleMenu = false;

      for (String authority : menuItem.getAuthorities()) {
        if (authenticationService.hasRole(authority)) {
          hasRoleMenu = true;
        }
      }

      if (hasRoleMenu) {
        tester.assertVisible(pathToMenu);
        tester.assertEnabled(pathToMenu + ":" + itemIndex + ":navLink");
        tester.assertEscapeLabel(
            pathToMenu + ":" + itemIndex + ":navLink:label", menuItem.getLabel());
        ++nbItems;

        // SubMenu
        String subMenuPath = pathToMenu + ":" + itemIndex + ":sidenavSubContainer:sidenavSubItems";
        nbItems += sidebarMenuComponents(NavbarItem.submenu(menuItem), subMenuPath);

        ++itemIndex;
      }
    }

    return nbItems;
  }

  private enum NavbarItem {
    // Menu
    HOME(null, localize("navigation.home"), 0, CoreAuthorityConstants.ROLE_AUTHENTICATED),
    REFERENCE_DATA(
        null, localize("navigation.referenceData"), 1, CoreAuthorityConstants.ROLE_ADMIN),
    ADMINISTRATION(
        null, localize("navigation.administration"), 2, CoreAuthorityConstants.ROLE_ADMIN),
    CONSOLE(null, localize("navigation.console"), 3, CoreAuthorityConstants.ROLE_ADMIN),

    // SubMenu
    ADMINISTRATION_BASIC_USER(
        ADMINISTRATION,
        localize("navigation.administration.basicUser"),
        0,
        CoreAuthorityConstants.ROLE_ADMIN),
    ADMINISTRATION_TECHNICAL_USER(
        ADMINISTRATION,
        localize("navigation.administration.technicalUser"),
        1,
        CoreAuthorityConstants.ROLE_ADMIN),
    ADMINISTRATION_USERGROUP(
        ADMINISTRATION,
        localize("navigation.administration.userGroup"),
        2,
        CoreAuthorityConstants.ROLE_ADMIN),
    ADMINISTRATION_ANNOUNCEMENT(
        ADMINISTRATION,
        localize("navigation.administration.announcement"),
        3,
        CoreAuthorityConstants.ROLE_ADMIN);

    private String label;

    private String[] authorities;

    private int order;

    private NavbarItem parent;

    private NavbarItem(NavbarItem parent, String label, int order, String... authorities) {
      this.parent = parent;
      this.label = label;
      this.authorities = authorities;
      this.order = order;
    }

    public NavbarItem getParent() {
      return parent;
    }

    public String getLabel() {
      return label;
    }

    public String[] getAuthorities() {
      return authorities;
    }

    public int getOrder() {
      return order;
    }

    public static List<NavbarItem> menu() {
      return Arrays.stream(values())
          .filter(Predicates2.compose(Predicates2.isNull(), NavbarItem::getParent))
          .sorted(Comparator.comparing(NavbarItem::getOrder))
          .collect(ImmutableList.toImmutableList());
    }

    public static List<NavbarItem> submenu(NavbarItem parent) {
      if (parent == null) {
        return ImmutableList.of();
      }

      return Arrays.stream(values())
          .filter(Predicates2.compose(Predicates2.equalTo(parent), NavbarItem::getParent))
          .sorted(Comparator.comparing(NavbarItem::getOrder))
          .collect(ImmutableList.toImmutableList());
    }
  }
}
