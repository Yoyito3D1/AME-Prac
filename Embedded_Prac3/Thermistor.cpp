// Thermistor.cpp
#include "Thermistor.h"
#include <cmath>

Thermistor::Thermistor(PinName pin) : adcPin(pin) {
}

float Thermistor::readTemperature() {
    float adcValue = adcPin.read();
    float Rtherm = Rb * ((1.0 / adcValue) - 1.0);
    float temperatureK = 1.0 / (log(Rtherm / Rb) / B + 1.0 / T0);
    float temperatureC = temperatureK - 273.15;
    return temperatureC / 2;
}
