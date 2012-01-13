package test.geocoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderResultType;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;
import com.google.code.geocoder.model.LatLngBounds;

/**
 * Cette classe a été créée à des fins de tests / documentation
 */
@Ignore
public class TestGeocoder {
	
	private static Geocoder geocoder;
	
	@BeforeClass
	public static void setUp() {
		geocoder = new Geocoder();
	}
	
	@Test
	public void testURL() {
		//Obtenir une réponse
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress("Lyon, France").setLanguage("fr").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		assertEquals(geocoderResponse.getStatus(), GeocoderStatus.OK);
	}
	
	@Test
	public void reverseGeocoding() {
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(new LatLng("45.772216", "4.859242")).setLanguage("fr").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		assertNotNull(geocoderResponse);
		assertEquals(GeocoderStatus.OK, geocoderResponse.getStatus());
		assertFalse(geocoderResponse.getResults().isEmpty());
		final GeocoderResult geocoderResult = geocoderResponse.getResults().iterator().next();
		assertTrue(geocoderResult.getFormattedAddress().contains("Rue Jean Novel"));
	}
	
	@Test
	public void testAccurateSearch() {
		//My Home :-)
		String myHome = "237 Avenue Jean Jaurès 69007 Lyon France";
		
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(myHome)
				.setRegion("fr").setLanguage("fr").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		assertFalse(geocoderResponse.getResults().isEmpty());
		
		GeocoderResult geocoderResult = geocoderResponse.getResults().iterator().next();
		LatLng location = geocoderResult.getGeometry().getLocation();
		
		geocoderRequest = new GeocoderRequestBuilder().setLocation(location).setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertTrue(geocoderResult.getFormattedAddress().contains(myHome));
	}
	
	@Test 
	public void testBoundingCity() {
		LatLng limit_southwest = new LatLng("45.434616", "4.479726");
		LatLng limit_northeast = new LatLng("45.53778", "4.889763");
		
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress("st martin").setRegion("fr").setLanguage("fr").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		assertTrue(geocoderResponse.getResults().size() > 1 );
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress("st martin")
				.setRegion("fr")
				.setLanguage("fr")
				.setBounds(new LatLngBounds(limit_southwest, limit_northeast)).getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		for(GeocoderResult result : geocoderResponse.getResults()) {
			List<GeocoderAddressComponent> addressComponents = result.getAddressComponents();
			for(GeocoderAddressComponent addressComponent : addressComponents) {
				if (addressComponent.getTypes().contains(GeocoderResultType.ADMINISTRATIVE_AREA_LEVEL_1.value())) {
					assertTrue(addressComponent.getLongName().equals("Rhône-Alpes"));
				}
			}
		}
	}
	
	/*
	 * Gecoder API ne permet pas de rechercher des objets touristiques
	 */
	@Test
	public void testSitraObject(){
		String obt_domaine_skiable = "Domaines skiables";
		String obt_patrimoine_naturel = "Patrimoine Naturel";
		String obt_fetes_manifestations = "Fêtes et Manisfestations";
		
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(obt_domaine_skiable).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		assertEquals(geocoderResponse.getStatus(), GeocoderStatus.ZERO_RESULTS);
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress(obt_patrimoine_naturel).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertEquals(geocoderResponse.getStatus(), GeocoderStatus.ZERO_RESULTS);
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress(obt_fetes_manifestations).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertEquals(geocoderResponse.getStatus(), GeocoderStatus.ZERO_RESULTS);
	}
	
	/*
	 * Geocoder API ne permet pas de rechercher des points d'interets ormis les parcs, aéroport, réserve naturel (natural_feature)
	 */
	@Test
	public void testTourismName() {
		String domaine_skiable = "La croix fry"; //Domain skiable
		String restaurant = "Mc Donalds";
		String fete = "Fêtes des lumières";
		String parc = "Parc de la tête d'or";
		
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(domaine_skiable).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		assertFalse(geocoderResponse.getResults().iterator().next().getTypes().contains(GeocoderResultType.POINT_OF_INTEREST.value()));
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress(restaurant).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertFalse(geocoderResponse.getResults().iterator().next().getTypes().contains(GeocoderResultType.POINT_OF_INTEREST.value()));
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress(fete).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertEquals(geocoderResponse.getStatus(), GeocoderStatus.ZERO_RESULTS);
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress("restaurant Lyon").setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertFalse(geocoderResponse.getResults().iterator().next().getTypes().contains(GeocoderResultType.POINT_OF_INTEREST.value()));
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress(parc).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertTrue(geocoderResponse.getResults().iterator().next().getTypes().contains(GeocoderResultType.PARK.value()));
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress("Aéroport").setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertTrue(geocoderResponse.getResults().iterator().next().getTypes().contains(GeocoderResultType.AIRPORT.value()));
		
		geocoderRequest = new GeocoderRequestBuilder().setAddress("L'Yon, Vendée, Pays de la Loire").setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertTrue(geocoderResponse.getResults().iterator().next().getTypes().contains(GeocoderResultType.NATURAL_FEATURE.value()));
	}
	
	/*
	 * La recherche sur des noms de rue, avenue, etc se limite à 10
	 */
	@Test
	public void testAutocompleteAdress() {
		String adress = "place foch";
		
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(adress).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		assertTrue(geocoderResponse.getResults().size() == 10);
		
		adress = "rue  Victor Hugo";
		geocoderRequest = new GeocoderRequestBuilder().setAddress(adress).setRegion("fr").setLanguage("fr").getGeocoderRequest();
		geocoderResponse = geocoder.geocode(geocoderRequest);
		assertTrue(geocoderResponse.getResults().size() == 10);
	}
}
