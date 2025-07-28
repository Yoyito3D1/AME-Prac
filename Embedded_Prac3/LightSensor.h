// LightSensor.h
#ifndef LIGHT_SENSOR_H
#define LIGHT_SENSOR_H

#include "mbed.h"

class LightSensor {
private:
    AnalogIn lightSensor;

public:
    LightSensor(PinName pin);
    float readLux();
};

#endif  // LIGHT_SENSOR_H
