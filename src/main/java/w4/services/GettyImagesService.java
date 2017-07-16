package w4.services;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GettyImagesService {

    @Value("${getty-images.api-key}")
    private String apiKey;

    private static final String IMAGES_URL = "https://api.gettyimages.com/v3/search/images?phrase={phrase}";

    private RestTemplate restTemplate = new RestTemplate();

    public String getImageUrl(String formattedAddress) {
        if (formattedAddress == null) {
            return null;
        }
        ResponseEntity<JsonNode> response;
        try {
            response = restTemplate.exchange(
                IMAGES_URL,
                HttpMethod.GET,
                getObjectEntity(),
                JsonNode.class,
                formattedAddress);
        } catch (HttpClientErrorException e) {
            return null;
        }
        return getUrl(response);
    }

    private String getUrl(ResponseEntity<JsonNode> response) {
        if (response.getBody().get("images").get(0) == null) {
            return null;
        } else {
            return response.getBody().get("images").get(0).get("display_sizes").get(0).get("uri").asText();
        }
    }

    private HttpEntity<Object> getObjectEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        return new HttpEntity<>(null, headers);
    }
}
