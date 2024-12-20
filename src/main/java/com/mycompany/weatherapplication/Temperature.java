package com.mycompany.weatherapplication;

public class Temperature extends WeatherData {

    private Temperature(Builder builder) {
        super(builder);
    }

    // Static Builder class for Temperature
    public static class Builder extends WeatherData.Builder {
        
        @Override
        public Temperature build() {
            return new Temperature(this);
        }
    }
}
