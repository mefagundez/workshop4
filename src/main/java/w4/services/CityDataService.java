package w4.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import w4.dto.CityData;

@Service
public class CityDataService {

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private GettyImagesService gettyImagesService;

    @Autowired
    private OpenWeatherService openWeatherService;

    public CityData getCityData(Double latitude, Double longitude) {
        String fullAddress = geocodingService.getFullAddress(latitude, longitude);
        return CityData.builder()
                .address(fullAddress)
                .image(fullAddress != null ? gettyImagesService.getImageUrl(fullAddress) : null)
                .cityWeather(openWeatherService.getWeather(latitude, longitude))
                .build();
    }
}
