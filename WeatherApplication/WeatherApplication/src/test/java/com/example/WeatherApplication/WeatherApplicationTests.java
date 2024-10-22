package com.example.WeatherApplication;

import com.example.WeatherApplication.entity.WeatherData;
import com.example.WeatherApplication.repository.WeatherRepository;
import com.example.WeatherApplication.service.WeatherService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class WeatherApplicationTests {

	@Mock
	private WeatherRepository weatherRepository;

	@InjectMocks
	private WeatherService weatherService;

	private MockWebServer mockWebServer;

	@BeforeEach
	void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
		mockWebServer = new MockWebServer();
		mockWebServer.start();
		// Set mock URLs for Google Maps and OpenWeather
		weatherService.setGoogleMapsApiUrl(mockWebServer.url("/geocode").toString());
		weatherService.setOpenWeatherApiUrl(mockWebServer.url("/weather").toString());
	}

	@Test
	void testGetWeather_Successful() throws IOException {
		// Mock response for Google Maps Geocoding API
		String geocodingResponse = "{ 'results': [{ 'geometry': { 'location': { 'lat': 12.9715987, 'lng': 77.594566 } } }] }";
		mockWebServer.enqueue(new MockResponse().setBody(geocodingResponse).setResponseCode(200));

		// Mock response for OpenWeather API
		String weatherResponse = "{ 'main': { 'temp': 298.15 }, 'weather': [{ 'description': 'clear sky' }] }";
		mockWebServer.enqueue(new MockResponse().setBody(weatherResponse).setResponseCode(200));

		// Mock repository save behavior
		when(weatherRepository.save(any(WeatherData.class))).thenReturn(null);

		WeatherData weatherData = weatherService.getWeather("636003", "2024-10-21");

		assertNotNull(weatherData);
		assertEquals(298.15, weatherData.getTemperature());
		assertEquals("clear sky", weatherData.getDescription());
	}

	@Test
	void testGetWeather_NoResultsFromGeocoding() throws IOException {
		// Mock empty response from Google Maps Geocoding API
		String geocodingResponse = "{ 'results': [] }";
		mockWebServer.enqueue(new MockResponse().setBody(geocodingResponse).setResponseCode(200));

		WeatherData weatherData = weatherService.getWeather("invalid_pincode", "2024-10-21");

		// Expect null because no coordinates were found
		assertNull(weatherData);
	}

	@Test
	void testGetWeather_ErrorResponseFromGeocoding() throws IOException {
		// Mock 404 error from Google Maps Geocoding API
		mockWebServer.enqueue(new MockResponse().setResponseCode(404));

		WeatherData weatherData = weatherService.getWeather("636003", "2024-10-21");

		// Expect null because the geocoding API failed
		assertNull(weatherData);
	}

	@Test
	void testGetWeather_ErrorResponseFromWeatherApi() throws IOException {
		// Mock successful response from Google Maps Geocoding API
		String geocodingResponse = "{ 'results': [{ 'geometry': { 'location': { 'lat': 12.9715987, 'lng': 77.594566 } } }] }";
		mockWebServer.enqueue(new MockResponse().setBody(geocodingResponse).setResponseCode(200));

		// Mock 500 error from OpenWeather API
		mockWebServer.enqueue(new MockResponse().setResponseCode(500));

		WeatherData weatherData = weatherService.getWeather("636003", "2024-10-21");

		// Expect null because the weather API failed
		assertNull(weatherData);
	}

	@Test
	void testGetWeather_CachedData() {
		// Mock existing weather data in the database
		WeatherData cachedWeather = new WeatherData("636003", "2024-10-21", 298.15, "clear sky");
		when(weatherRepository.findByPincodeAndDate("636003", "2024-10-21")).thenReturn(cachedWeather);

		WeatherData weatherData = weatherService.getWeather("636003", "2024-10-21");

		// Check that the cached data is returned
		assertNotNull(weatherData);
		assertEquals(298.15, weatherData.getTemperature());
		assertEquals("clear sky", weatherData.getDescription());

		// Verify that the APIs were not called due to cache hit
		verify(weatherRepository, never()).save(any(WeatherData.class));
	}
}
