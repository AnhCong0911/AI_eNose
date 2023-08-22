package com.example.ainose;

public class RawDataSample {
    private float temperature;
    private float pressure;
    private float humidity;
    private float resistance; // Gas resistance

    // Constructor with default values
    public RawDataSample() {
        this.temperature = 0;
        this.pressure = 0;
        this.humidity = 0;
        this.resistance = 0;
    }

    public RawDataSample(float temperature, float pressure, float humidity, float resistance) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.resistance = resistance;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getResistance() {
        return resistance;
    }

    public void setResistance(float resistance) {
        this.resistance = resistance;
    }

    /** This function determines if the data sample has been filled with all parameters (T,P,H,G) or not.
     * Otherwise it may contain default values of 0 */
    boolean checkZeroDefault() {
        if (this.getTemperature() == 0f) {
            return true;
        }
        else if (this.getPressure() == 0f) {
            return true;
        }
        else if (this.getHumidity() == 0f) {
            return true;
        }
        else if (this.getResistance() == 0f) {
            return true;
        }
        return false;
    }

    public RawDataSample copy(){
        return new RawDataSample(this.getTemperature(), this.getPressure(), this.getHumidity(), this.getResistance());
    }
}
