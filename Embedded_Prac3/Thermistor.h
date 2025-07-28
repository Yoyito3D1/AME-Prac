#ifndef THERMISTOR_H
#define THERMISTOR_H

#include "mbed.h"

class Thermistor {
private:
    AnalogIn adcPin;
    static constexpr float Rb = 100000.0;
    static constexpr float T0 = 298.15;
    static constexpr float B = 4274.0;
    
public:
    Thermistor(PinName pin);
    float readTemperature();
};

#endif // THERMISTOR_H