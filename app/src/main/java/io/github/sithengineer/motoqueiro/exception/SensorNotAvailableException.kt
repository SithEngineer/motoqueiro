package io.github.sithengineer.motoqueiro.exception

class SensorNotAvailableException(sensorType: String) :
    Throwable("Sensor $sensorType not available")
