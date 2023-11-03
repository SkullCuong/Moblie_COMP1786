package com.example.hiking_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hiking_app.databinding.FragmentWeatherForecastBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherForecast#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherForecast extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OpenWeatherMapService openWeatherMapService;

    TextView tempCondition ;
    TextView temperature;
    TextView humidityValue;
    TextView windValue;
    private String apiKey = "c2099e1cd83613a813c6dcfb96399183";

    // TODO: Rename and change types of parameterse
    private String mParam1;
    private String mParam2;

    public WeatherForecast() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherForecast.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherForecast newInstance(String param1, String param2) {
        WeatherForecast fragment = new WeatherForecast();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        openWeatherMapService = ApiClient.getInstance().create(OpenWeatherMapService.class);
        loadWeatherData("London");
    }

    private void loadWeatherData(String location) {

        openWeatherMapService.getCurrentWeatherData(location, apiKey).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    WeatherData weatherData = response.body();
                    updateUI(weatherData);
                }
                System.out.println("Fail");
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }

        });
    }
    private void updateUI(WeatherData weatherData) {
        tempCondition.setText((String.valueOf((int) weatherData.getMain().getTemp() - 273)).toString() +"°C") ;
        temperature.setText((String.valueOf((int) weatherData.getMain().getTempMax() - 273)).toString() +"°C");
        humidityValue.setText(String.valueOf((int) weatherData.getMain().getHumidity()).toString() +"%");
        windValue.setText(String.valueOf( weatherData.getWind().getSpeed()).toString() +" m/s");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        tempCondition = view.findViewById(R.id.tempCondition);
        temperature = view.findViewById(R.id.temperature);
        humidityValue = view.findViewById(R.id.humidityValue);
        windValue = view.findViewById(R.id.windValue);


        return view;
    }
}