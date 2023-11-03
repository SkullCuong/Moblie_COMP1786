package com.example.hiking_app;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
    private Main main;
    private Weather[] weather;

    private Wind wind;

    private Sys sys;

    private String name;

    public String getName() {
        return name;
    }

    public class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;

        public double getTemp() {
            return temp;
        }

        public double getFeelsLike() {
            return feels_like;
        }

        public double getTempMin() {
            return temp_min;
        }

        public double getTempMax() {
            return temp_max;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
    public class Wind {
        private double speed;
        private int deg;

        public double getSpeed() {
            return speed;
        }

        public int getDeg() {
            return deg;
        }
    }
    public class Sys {
        private String country;

        public String getCountry() {
            return country;
        }
    }


    public Main getMain() {
        return main;
    }
    public Wind getWind() {
        return wind;
    }

    public Sys getSys(){return  sys;}

    public Weather[] getWeather() {
        return weather;
    }
}