package w4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import w4.dto.CityData;
import w4.dto.CityWeather;
import w4.services.GeocodingService;
import w4.services.GettyImagesService;
import w4.services.OpenWeatherService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class CityDataControllerTest {

    private static final String URL = "/location?latitude={latitude}&longitude={longitude}";

    private final Double macamicLatitude = 48.713;

    private final Double macamicLongitude = -79.066;

    private final String macamicAddress = "address";

    private final String macamicImageUrl = "https://macamic.com";

    private final String mainWeatherMacamic = "main weather";

    private final String descriptionWeatherMacamic = "description weather";

    private final CityWeather macamicCityWeather = CityWeather.builder()
            .main(mainWeatherMacamic)
            .description(descriptionWeatherMacamic)
            .build();

    private final CityData macamicCityData = CityData.builder()
            .address(macamicAddress)
            .image(macamicImageUrl)
            .cityWeather(macamicCityWeather)
            .build();


    private final Double antarcticaLatitude = -69.034;

    private final Double antarcticaLongitude = 75.2227334;

    private final String antarcticaAddress = "Antarctica";

    private final String mainWeatherAntarctica = "main";

    private final String descriptionWeatherAntarctica = "description";

    private final String antarcticaImageUrl = "https://antarctica.com";

    private final CityWeather antarcticaCityWeather = CityWeather.builder()
            .main(mainWeatherAntarctica)
            .description(descriptionWeatherAntarctica)
            .build();

    private final CityData antarcticaCityData = CityData.builder()
            .address(antarcticaAddress)
            .image(antarcticaImageUrl)
            .cityWeather(antarcticaCityWeather)
            .build();

    private final Double noGeocodingLatitude = -99.034;

    private final Double noGeocodingLongitude = 71.2227334;

    private final String noGeocodingAddress = null;

    private final String mainWeatherNoGeocoding = "main";

    private final String descriptionWeatherNoGeocoding = "description";

    private final String noGeocodingImageUrl = null;

    private final CityWeather noGeocodingCityWeather = CityWeather.builder()
            .main(mainWeatherNoGeocoding)
            .description(descriptionWeatherNoGeocoding)
            .build();

    private final CityData noGeocodingCityData = CityData.builder()
            .address(noGeocodingAddress)
            .image(noGeocodingImageUrl)
            .cityWeather(noGeocodingCityWeather)
            .build();


    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private GeocodingService geocodingService;

    @MockBean
    private GettyImagesService gettyImagesService;

    @MockBean
    private OpenWeatherService openWeatherService;

    @Before
    public void setup() {
        Mockito.when(geocodingService.getFullAddress(macamicLatitude, macamicLongitude)).thenReturn(macamicAddress);
        Mockito.when(gettyImagesService.getImageUrl(macamicAddress)).thenReturn(macamicImageUrl);
        Mockito.when(openWeatherService.getWeather(macamicLatitude, macamicLongitude)).thenReturn(macamicCityWeather);

        Mockito.when(geocodingService.getFullAddress(antarcticaLatitude, antarcticaLongitude)).thenReturn(antarcticaAddress);
        Mockito.when(gettyImagesService.getImageUrl(antarcticaAddress)).thenReturn(antarcticaImageUrl);
        Mockito.when(openWeatherService.getWeather(antarcticaLatitude, antarcticaLongitude)).thenReturn(antarcticaCityWeather);

        Mockito.when(geocodingService.getFullAddress(noGeocodingLatitude, noGeocodingLongitude)).thenReturn(noGeocodingAddress);
        Mockito.when(gettyImagesService.getImageUrl(noGeocodingAddress)).thenReturn(noGeocodingImageUrl);
        Mockito.when(openWeatherService.getWeather(noGeocodingLatitude, noGeocodingLongitude)).thenReturn(noGeocodingCityWeather);
    }


    @Test
    public void testCityDataForMacamic() {
        CityData macamic = testRestTemplate.getForObject(URL, CityData.class, macamicLatitude, macamicLongitude);
        Assert.assertEquals("Macamic object is not the expected", macamic, macamicCityData);
    }

    @Test
    public void testCityDataNoImage() {
        CityData antarctica = testRestTemplate.getForObject(URL, CityData.class, antarcticaLatitude, antarcticaLongitude);
        Assert.assertEquals("Antarctica object is not the expected", antarctica, antarcticaCityData);
    }

    @Test
    public void testCityDataNoAddress() {
        CityData noGeocoding = testRestTemplate.getForObject(URL, CityData.class, noGeocodingLatitude, noGeocodingLongitude);
        Assert.assertEquals("No Address object is not the expected", noGeocoding, noGeocodingCityData);
    }


}
