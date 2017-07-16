package w4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import w4.dto.CityData;
import w4.services.CityDataService;

@RestController
public class CityDataController {


    @Autowired
    private CityDataService cityDataService;

    @RequestMapping(method = RequestMethod.GET, value = "location")
    public CityData getLocationData(@RequestParam("latitude") Double latitude,
                                    @RequestParam("longitude") Double longitude) {
       return cityDataService.getCityData(latitude, longitude);
    }

}