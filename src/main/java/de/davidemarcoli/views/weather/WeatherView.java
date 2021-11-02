package de.davidemarcoli.views.weather;

import ch.qos.logback.core.Layout;
import com.google.gson.Gson;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import de.davidemarcoli.WeatherData.WeatherInfo;
import de.davidemarcoli.views.MainLayout;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@PageTitle("Weather")
@Route(value = "", layout = MainLayout.class)
public class WeatherView extends VerticalLayout {

    @Value("${openweathermaps.apikey}")
    String apikey;

    String location = "";
    WeatherInfo weatherInfo = null;

    public WeatherView() {
        setSpacing(false);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);

        getStyle().set("text-align", "center");

        TextField locationField = new TextField();
        Button searchButton = new Button("Search");

        add(new HorizontalLayout(
                locationField,
                searchButton
        ));

        Image img = new Image("", "");
        img.setWidth("200px");
        add(img);

        H2 title = new H2("");
        Paragraph description = new Paragraph("");

        IFrame googleMaps = new IFrame("");

        add(
                title,
                description
        );

        add(googleMaps);

        searchButton.addClickListener(click -> {
            location = locationField.getValue();
            getWeatherInfo();

            /*Image img = new Image("images/empty-plant.png", "placeholder plant");
            img.setWidth("200px");
            add(img);*/


            img.setSrc("https://openweathermap.org/img/wn/" + weatherInfo.weather.get(0).icon + "@2x.png");
            img.setAlt(weatherInfo.weather.get(0).description);
            title.setText("In " + location + " ist die Temperatur " + weatherInfo.main.temp + "°C");
            description.setText("Das Wetter ist: " + weatherInfo.weather.get(0).description);

            googleMaps.setSrc("https://maps.google.com/maps?q=" + weatherInfo.coord.lat + "%20" + weatherInfo.coord.lon + "&t=&z=9&ie=UTF8&iwloc=&output=embed");

            setSizeFull();
            setJustifyContentMode(JustifyContentMode.CENTER);
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            getStyle().set("text-align", "center");
        });
        searchButton.addClickShortcut(Key.ENTER);
    }

    public void getWeatherInfo() {
        StringBuilder result = new StringBuilder();
        try {

            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apikey + "&mode=json&units=metric&lang=de";
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            System.out.println(result);

            Gson gson = new Gson();
            weatherInfo = gson.fromJson(result.toString(), WeatherInfo.class);

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
