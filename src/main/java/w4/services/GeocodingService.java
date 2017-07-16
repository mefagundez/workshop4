package w4.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class GeocodingService {

    @Value("${google-maps.api-key}")
    private String apiKey;

    private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng={lat},{long}&key={apiKey}";

    private static final String ADDRESS_TYPE = "political";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;
    public String getFullAddress(Double latitude, Double longitude){
        JsonNode response;
        try {
            response = restTemplate.getForObject(GEOCODING_URL, JsonNode.class, latitude, longitude, apiKey);
        } catch (HttpClientErrorException e) {
            return null;
        }
        JsonNode results = response.get("results").get(0);

        if (results == null) {
            return null;
        }

        return getAddressComponentOfType(
                (ArrayNode) results.get("address_components"), ADDRESS_TYPE)
                .orElse(results.get("formatted_address").asText());
    }

    private Optional<String> getAddressComponentOfType(ArrayNode addressComponents, String type) {
        return StreamSupport.stream(addressComponents.spliterator(), false)
                .filter(jsonNode -> {
                    ArrayList<String> types = objectMapper.convertValue(jsonNode.get("types"), ArrayList.class);
                    return types.contains(type);
                })
                .findAny()
                .flatMap(jsonNode -> Optional.of(jsonNode.get("long_name").asText()));
    }
}
