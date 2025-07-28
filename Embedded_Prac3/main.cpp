#include <cstdio>
#include <cstring>
#include <mbed.h>
#include "BufferedSerial.h"
#include <ByteBuffer.h>
#include "Thermistor.h"
#include "LightSensor.h"

BufferedSerial bs(D1, D0, 9600);  // D1: TX, D0: RX
Thermistor termistor(A0);  // A0: Pin analógico para el sensor de temperatura
LightSensor luzSensor(A1);  // A1: Pin analógico para el sensor de luz

// main() runs in its own thread in the OS
int main() {
    bs.set_format(8, BufferedSerial::None, 1);

    while (true) {
        // Leer temperatura y luz
        float temperatura = termistor.readTemperature();
        float luz = luzSensor.readLux();

        // Crear una cadena con los datos separados por coma
        char dataBuffer[50];
        sprintf(dataBuffer, " %.2f,%.2f\n",luz,  temperatura);
        
        printf("%.2f,%.2f\n", temperatura, luz);
        // Enviar datos a través de Bluetooth
        bs.write(dataBuffer, strlen(dataBuffer));

        // Esperar antes de la próxima lectura
        ThisThread::sleep_for(1000);  // Puedes ajustar este tiempo según sea necesario
    }
}
