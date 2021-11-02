package de.davidemarcoli.WeatherData;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@RequestMapping(value = "weather")
@RestController
public class WeatherDataController {

    @Value("${openweathermaps.apikey}")
    private String apikey;


    @GetMapping("/{location}")
    public WeatherInfo getWeatherData(@PathVariable String location) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid= " + apikey + "&mode=json");
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            Gson gson = new Gson();
            WeatherInfo weatherInfo = gson.fromJson(result.toString(), WeatherInfo.class);
            return weatherInfo;

        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
