package w4.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import w4.dto.CityWeather;

@Service
public class OpenWeatherService {

    @Value("${open-weather.api-key}")
    private String apiKey;

    private static final String OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={appid}";

    private RestTemplate restTemplate = new RestTemplate();

    public CityWeather getWeather(Double latitude, Double longitude){
        JsonNode response;
        try {
            response = restTemplate.getForObject(OPEN_WEATHER_URL, JsonNode.class, latitude, longitude, apiKey);
        } catch (HttpClientErrorException e) {
            return null;
        }
        JsonNode weather = response.get("weather").get(0);
        String main = weather.get("main").asText();
        String description = weather.get("description").asText();
        return CityWeather.builder()
                .main(main)
                .description(description)
                .build();
    }
}
