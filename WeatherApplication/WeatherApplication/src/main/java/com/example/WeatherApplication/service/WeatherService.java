package com.example.WeatherApplication.service;

import com.example.WeatherApplication.entity.WeatherData;
import com.example.WeatherApplication.repository.WeatherRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    private String googleMapsApiUrl = "https://maps.googleapis.com/maps/api/geocode/json";
    private String openWeatherApiUrl = "https://api.openweathermap.org/data/2.5/weather";

    // Setter methods for dynamic API URLs (for testing)
    public void setGoogleMapsApiUrl(String googleMapsApiUrl) {
        this.googleMapsApiUrl = googleMapsApiUrl;
    }

    public void setOpenWeatherApiUrl(String openWeatherApiUrl) {
        this.openWeatherApiUrl = openWeatherApiUrl;
    }

    private final OkHttpClient client = new OkHttpClient();

    @Cacheable("weather")
    public WeatherData getWeather(String pincode, String date) {
        WeatherData weatherData = weatherRepository.findByPincodeAndDate(pincode, date);
        if (weatherData != null) {
            return weatherData;
        }

        String coordinates = getCoordinatesFromPincode(pincode);
        if (coordinates != null) {
            String weatherJson = getWeatherFromCoordinates(coordinates, date);
            if (weatherJson != null) {
                JSONObject weatherObject = new JSONObject(weatherJson);
                double temp = weatherObject.getJSONObject("main").getDouble("temp");
                String description = weatherObject.getJSONArray("weather").getJSONObject(0).getString("description");

                weatherData = new WeatherData(pincode, date, temp, description);
                weatherRepository.save(weatherData);
                return weatherData;
            }
        }
        return null;
    }

    private String getCoordinatesFromPincode(String pincode) {
        String url = googleMapsApiUrl + "?address=" + pincode + "&key=YOUR_GOOGLE_MAPS_API_KEY";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);

                // Check if 'results' array exists and has at least one result
                JSONArray results = json.optJSONArray("results");
                if (results != null && results.length() > 0) {
                    JSONObject location = results.getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location");
                    return location.getDouble("lat") + "," + location.getDouble("lng");
                } else {
                    // Log an error or handle the case where no results were found
                    System.out.println("No results found for the provided pincode: " + pincode);
                }
            } else {
                // Handle unsuccessful responses (non-200 status codes)
                System.out.println("Error: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no coordinates could be fetched
    }

    private String getWeatherFromCoordinates(String coordinates, String date) {
        String url = openWeatherApiUrl + "?lat=" + coordinates.split(",")[0] + "&lon=" + coordinates.split(",")[1] + "&appid=YOUR_OPENWEATHER_API_KEY";
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
