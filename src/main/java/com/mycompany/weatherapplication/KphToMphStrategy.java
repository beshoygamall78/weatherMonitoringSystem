/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.weatherapplication;
class KphToMphStrategy implements WindSpeedConversionStrategy {
    public double convert(double windSpeed) {
        return windSpeed * 0.621371; // التحويل من كيلومتر في الساعة إلى ميل في الساعة
    }
}

// Kph to Kph strategy (no conversion)
class KphToKphStrategy implements WindSpeedConversionStrategy {
    public double convert(double windSpeed) {
        return windSpeed; // لا تغيير إذا كانت الوحدة كيلومتر في الساعة
    }
}