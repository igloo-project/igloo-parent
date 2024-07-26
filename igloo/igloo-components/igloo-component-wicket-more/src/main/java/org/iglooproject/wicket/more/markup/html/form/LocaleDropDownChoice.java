package org.iglooproject.wicket.more.markup.html.form;

import static org.iglooproject.spring.property.SpringPropertyIds.AVAILABLE_LOCALES;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Locale;
import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.select2.util.Select2Utils;
import org.wicketstuff.select2.Settings;

public class LocaleDropDownChoice extends GenericSelect2DropDownSingleChoice<Locale> {

  private static final long serialVersionUID = -6782229493391720861L;

  private static final LocaleChoiceRenderer CHOICE_RENDERER = new LocaleChoiceRenderer();

  public LocaleDropDownChoice(String id, IModel<Locale> model) {
    super(id, model, new LocaleChoiceList(), CHOICE_RENDERER);
    setNullValid(false);
  }

  @Override
  protected void fillSelect2Settings(Settings settings) {
    super.fillSelect2Settings(settings);
    Select2Utils.disableSearch(settings);
  }

  private static class LocaleChoiceRenderer extends ChoiceRenderer<Locale> {

    private static final long serialVersionUID = 2534709458895245968L;

    @Override
    public Object getDisplayValue(Locale object) {
      return object.getDisplayName(Session.get().getLocale());
    }

    @Override
    public String getIdValue(Locale object, int index) {
      return object.getLanguage();
    }
  }

  private static class LocaleChoiceList extends LoadableDetachableModel<List<Locale>> {

    private static final long serialVersionUID = 4991853466150310164L;

    @SpringBean private IPropertyService propertyService;

    public LocaleChoiceList() {
      Injector.get().inject(this);
    }

    @Override
    protected List<Locale> load() {
      return ImmutableList.copyOf(propertyService.get(AVAILABLE_LOCALES));
    }
  }
}
