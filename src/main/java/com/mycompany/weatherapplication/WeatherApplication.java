package com.mycompany.weatherapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyManagementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class WeatherApplication {
    private static WeatherDataFetcher fetcher = WeatherDataFetcher.getInstance();

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Weather Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new GridLayout(12, 1));
        frame.getContentPane().setBackground(new Color(240, 248, 255)); 

     
        JLabel welcomeLabel = new JLabel("Welcome to the Weather Application!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(0, 0, 128)); 
        frame.add(welcomeLabel);

   
        JPanel namePanel = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Enter your name:");
        JTextField nameField = new JTextField(20);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        frame.add(namePanel);

      
        JPanel cityPanel = new JPanel(new FlowLayout());
        JLabel cityLabel = new JLabel("Enter city name:");
        JTextField cityField = new JTextField(20);
        cityPanel.add(cityLabel);
        cityPanel.add(cityField);
        frame.add(cityPanel);

      
        JPanel tempPanel = new JPanel(new FlowLayout());
        JLabel tempLabel = new JLabel("Choose temperature unit:");
        String[] tempOptions = {"Celsius", "Fahrenheit"};
        JComboBox<String> tempComboBox = new JComboBox<>(tempOptions);
        tempPanel.add(tempLabel);
        tempPanel.add(tempComboBox);
        frame.add(tempPanel);

      
        JPanel windPanel = new JPanel(new FlowLayout());
        JLabel windLabel = new JLabel("Choose wind speed unit:");
        String[] windOptions = {"km/h", "mph"};
        JComboBox<String> windComboBox = new JComboBox<>(windOptions);
        windPanel.add(windLabel);
        windPanel.add(windComboBox);
        frame.add(windPanel);

     
        JButton submitButton = new JButton("Fetch Weather");
        submitButton.setBackground(new Color(30, 144, 255)); 
        submitButton.setForeground(Color.WHITE); 
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(submitButton);

       
        JLabel tempOutputLabel = new JLabel("Temperature: Loading...", JLabel.CENTER);
        tempOutputLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        tempOutputLabel.setForeground(new Color(34, 139, 34)); // Green color for temperature
        JLabel windOutputLabel = new JLabel("Wind Speed: Loading...", JLabel.CENTER);
        windOutputLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        windOutputLabel.setForeground(new Color(255, 99, 71)); // Tomato color for wind speed
        JLabel humidityOutputLabel = new JLabel("Humidity: Loading...", JLabel.CENTER);
        humidityOutputLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        humidityOutputLabel.setForeground(new Color(255, 69, 0)); // Red-Orange color for humidity
        JLabel conditionOutputLabel = new JLabel("Condition: Loading...", JLabel.CENTER);
        conditionOutputLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        conditionOutputLabel.setForeground(new Color(255, 165, 0)); // Orange color for condition

        // Adding output labels to the frame
        frame.add(tempOutputLabel);
        frame.add(windOutputLabel);
        frame.add(humidityOutputLabel);
        frame.add(conditionOutputLabel);

        // Action listener for the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = nameField.getText().trim();
                String city = cityField.getText().trim();
                String tempUnit = (String) tempComboBox.getSelectedItem();
                String windUnit = (String) windComboBox.getSelectedItem();

                if (userName.isEmpty() || city.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter all the details.");
                    return;
                }

                // Send welcome message
                JOptionPane.showMessageDialog(frame, "Welcome, " + userName + "!");

                // Fetch weather data
                fetcher.setUnitTemp(tempUnit);
                fetcher.setUnitWind(windUnit);
                String jsonData = null;
                try {
                    jsonData = fetcher.fetchWeatherData(city);
                } catch (KeyManagementException ex) {
                    Logger.getLogger(WeatherApplication.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (jsonData != null) {
                    JSONObject weatherObject = new JSONObject(jsonData);
                    JSONObject current = weatherObject.getJSONObject("current");
                    double temperatureCelsius = current.getDouble("temp_c");
                    double windSpeedKph = current.getDouble("wind_kph");
                    int humidity = current.getInt("humidity");
                    String condition = current.getJSONObject("condition").getString("text");

                    TemperatureConversionStrategy tempStrategy = tempUnit.equalsIgnoreCase("Fahrenheit") ?
                            new CelsiusToFahrenheitStrategy() : new CelsiusToCelsiusStrategy();

                    WindSpeedConversionStrategy windStrategy = windUnit.equalsIgnoreCase("mph") ?
                            new KphToMphStrategy() : new KphToKphStrategy();

                    double temperature = tempStrategy.convert(temperatureCelsius);
                    double windSpeed = windStrategy.convert(windSpeedKph);

                    // Update the output labels with weather data
                    tempOutputLabel.setText("Temperature: " + String.format("%.2f", temperature) + " " + (tempUnit.equalsIgnoreCase("Fahrenheit") ? "°F" : "°C"));
                    windOutputLabel.setText("Wind Speed: " + String.format("%.2f", windSpeed) + " " + (windUnit.equalsIgnoreCase("mph") ? "mph" : "km/h"));
                    humidityOutputLabel.setText("Humidity: " + humidity + "%");
                    conditionOutputLabel.setText("Condition: " + condition);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to fetch weather data for city: " + city, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Set the frame visible
        frame.setVisible(true);
    }
}
