
package com.mycompany.weatherapplication;

class CelsiusToFahrenheitStrategy implements TemperatureConversionStrategy {
    public double convert(double temperature) {
        return (temperature * 9 / 5) + 32; 
    }
}

// Celsius to Celsius strategy (no conversion)
class CelsiusToCelsiusStrategy implements TemperatureConversionStrategy {
    public double convert(double temperature) {
        return temperature; 
    }
}