// LightSensor.cpp
#include "LightSensor.h"

LightSensor::LightSensor(PinName pin) : lightSensor(pin) {
}

float LightSensor::readLux() {
    float counts = 0;
    for (int i = 0; i < 100; i++) {
        counts += lightSensor.read();
    }
    counts /= 100;

    float vout = counts * 5;
    counts = (((5 * 500) * vout) - 500) / 10;
    counts /= 1200;

    return counts;
}
