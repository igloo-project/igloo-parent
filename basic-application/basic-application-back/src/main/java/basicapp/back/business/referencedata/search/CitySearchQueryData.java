package basicapp.back.business.referencedata.search;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import org.bindgen.Bindable;

@Bindable
public class CitySearchQueryData extends AbstractReferenceDataSearchQueryData<City> {

  private PostalCode postalCode;

  public PostalCode getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(PostalCode postalCode) {
    this.postalCode = postalCode;
  }
}
