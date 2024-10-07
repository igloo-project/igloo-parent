package basicapp.front.role.model;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.search.RoleSort;
import basicapp.back.business.role.service.IRoleService;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.List;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class RoleDataProvider extends LoadableDetachableDataProvider<Role> {

  private static final long serialVersionUID = -1L;

  private final CompositeSortModel<RoleSort> sortModel =
      new CompositeSortModel<>(
          CompositeSortModel.CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              RoleSort.TITLE, RoleSort.TITLE.getDefaultOrder(),
              RoleSort.ID, RoleSort.ID.getDefaultOrder()),
          ImmutableMap.of(RoleSort.ID, RoleSort.ID.getDefaultOrder()));

  @SpringBean private IRoleService roleService;

  public RoleDataProvider() {
    Injector.get().inject(this);
  }

  @Override
  public IModel<Role> model(Role object) {
    return GenericEntityModel.of(object);
  }

  @Override
  protected List<Role> loadList(long first, long count) {
    if (first > loadSize()) {
      return List.of();
    }
    return roleService.list().subList((int) Math.max(0, first), (int) Math.min(loadSize(), count));
  }

  @Override
  protected long loadSize() {
    return roleService.list().size();
  }

  public Collection<Role> list() {
    return loadList(0, Long.MAX_VALUE);
  }

  public CompositeSortModel<RoleSort> getSortModel() {
    return sortModel;
  }
}
