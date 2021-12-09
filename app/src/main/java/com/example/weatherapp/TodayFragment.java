package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TodayFragment extends Fragment {

    private String TAG="TodayFragmentTag";
    private JSONObject weatherInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.today_tab, container, false);
        Bundle b= this.getArguments();
        if(b!= null) {
            try {
                weatherInfo= new JSONObject(b.getString("weather"));
                Log.d(TAG, weatherInfo.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d(TAG, "no bundle");
        }

        fillInfo(rootView);
        return rootView;
    }


    public void colorImage(ImageView imageView, int resource, int color)
    {
        Drawable unwrappedDrawable = ContextCompat.getDrawable(getContext(), resource);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        imageView.setImageDrawable(wrappedDrawable);
    }

    private int getIconDrawable(String summary)
    {
        int icon= R.drawable.weather_sunny;
        Log.d(TAG, summary);

        if(summary.equals("clear-day")) {
            icon= R.drawable.weather_sunny;
        }
        else if(summary.equals("clear-night")){
            icon= R.drawable.weather_night;
        }
        else if(summary.equals("rain")){
            icon= R.drawable.weather_rainy;
        }
        else if(summary.equals("sleet")){
            icon= R.drawable.weather_snowy_rainy;
        }else if(summary.equals("snow")){
            icon= R.drawable.weather_snowy;
        }else if(summary.equals("wind")){
            icon= R.drawable.weather_windy_variant;
        }else if(summary.equals("fog")){
            icon= R.drawable.weather_fog;
        }
        else if(summary.equals("cloudy")){
            icon= R.drawable.weather_cloudy;
        }
        else if(summary.equals("partly-cloudy-night")){
            icon= R.drawable.weather_night_partly_cloudy;
        }
        else if(summary.equals("partly-cloudy-day")){
            icon= R.drawable.weather_partly_cloudy;
        }
        return icon;
    }

    public double roundOff(double x)
    {
        //round off to 2 decimals
        double value= Math.round(x* 100.0) /100.0;
        return value;
    }
    public int get_weather(int d) {

        int icon= R.drawable.weather_sunny;
        if (d == 1000) {
            icon = R.drawable.clear_day;
            ats= "Clear Day";
        } else if (d == 1100) {
            icon = R.drawable.mostly_clear_day; //night
            ats= "Mostly Clear Day";
        } else if (d == 1101) {
            icon = R.drawable.partly_cloudy_day;
            ats= "Partly Cloudy Day";
        } else if (d == 1102) {
            icon = R.drawable.mostly_cloudy;
            ats= "Mostly Cloudy";
        } else if (d == 1001) {
            icon = R.drawable.cloudy;
            ats= "Cloudy";
        } else if (d == 2000) {
            icon = R.drawable.fog;
            ats= "Fog";
        } else if (d == 2100) {
            icon = R.drawable.fog_light;
            ats= "Fog Light";
        } else if (d == 8000) {
            icon = R.drawable.tstorm;
            ats= "Thunderstorm";
        } else if (d == 5001) {
            icon = R.drawable.flurries;
            ats= "Flurries";
        } else if (d == 5100) {
            icon = R.drawable.snow_light;
            ats= "Snow Light";
        } else if (d == 5000) {
            icon = R.drawable.snow;
            ats= "Snow";
        } else if (d == 5101) {
            icon = R.drawable.snow_heavy;
            ats= "Snow Heavy";
        } else if (d == 7102) {
            icon = R.drawable.ice_pellets_light;
            ats= "Ice Pellets Light";
        } else if (d == 7000) {
            icon = R.drawable.ice_pellets;
            ats= "Ice Pellets";
        } else if (d == 7101) {
            icon = R.drawable.ice_pellets_heavy;
            ats= "Ice Pellets Heavy";
        } else if (d == 4000) {
            icon = R.drawable.drizzle;
            ats= "Drizzle";
        } else if (d == 6000) {
            icon = R.drawable.freezing_drizzle;
            ats= "Freezing Drizzle";
        } else if (d == 6200) {
            icon = R.drawable.freezing_rain_light;
            ats= "Freezing Rain Light";
        } else if (d == 6001) {
            icon = R.drawable.freezing_rain;
            ats= "Freezing Rain";
        } else if (d == 6201) {
            icon = R.drawable.freezing_rain_heavy;
            ats= "Freezing Rain Heavy";
        } else if (d == 4200) {
            icon = R.drawable.freezing_rain;
            ats= "Freezing Rain";
        } else if (d == 4001) {
            icon = R.drawable.rain;
            ats= "Rain";
        } else if (d == 4201) {
            icon = R.drawable.rain_heavy;
            ats= "Rain Heavy";
        }
        return icon;

    }
    String ats = "Clear Day";
    private void fillInfo(View rootView){

        //create 8 layouts and add to the scroll view for min and max temp and so on

        if(weatherInfo !=null)
        {
            Log.d(TAG, "in fill info");

            String icon, summary,ats;
            double temperature;

            double humidity, pressure, visibility, windSpeed;
            double ozone, cloudCover, precipitation;

            try {
                //JSONObject ats = weatherInfo.getJSONObject("obj");
                JSONArray array  = weatherInfo.getJSONArray("obj");
                JSONObject v = array.getJSONObject(0);
                JSONObject obj = v.getJSONObject("values");
                Log.d("ats",obj.toString());
                icon= obj.getString("weatherCode");
                temperature= obj.getDouble("temperature");
                int temp= (int) temperature;
                ats ="cloudy";
                summary= obj.getString("weatherCode");

                precipitation = obj.getDouble("precipitationProbability");
                //precipitation= roundOff(precipitation);

                humidity= obj.getDouble("humidity");
                //humidity= humidity*100;
                //humidity= roundOff(humidity);

                pressure= obj.getDouble("pressureSeaLevel");
                //pressure= roundOff(pressure);

                visibility= obj.getDouble("visibility");
                //visibility= roundOff(visibility);

                windSpeed= obj.getDouble("windSpeed");
                //windSpeed= roundOff(windSpeed);

                ozone= obj.getDouble("uvIndex");
                //ozone= roundOff(ozone);

                cloudCover= obj.getDouble("cloudCover");
                //cloudCover= cloudCover*100;
                //cloudCover= roundOff(cloudCover);

                //card1
                ImageView iconImage= rootView.findViewById(R.id.icon);
                int iconResource = getIconDrawable(ats);
                //iconImage.setImageResource(iconResource);
                iconImage.setImageResource(get_weather(Integer.valueOf(summary)));

                TextView tempTextView= rootView.findViewById(R.id.temperature_value);
                tempTextView.setText(String.valueOf(temp) + "°F");

                TextView summ= rootView.findViewById(R.id.summary);
                summ.setText(ats);

                TextView t= rootView.findViewById(R.id.precipitation_value);
                t.setText(Double.toString(precipitation) + " mmph");

                TextView t2= rootView.findViewById(R.id.pressure_value);
                t2.setText(pressure + " mb");

                TextView t3= rootView.findViewById(R.id.wind_speed_value);
                t3.setText(windSpeed + " mph");

                TextView t4= rootView.findViewById(R.id.visibility_value);
                t4.setText(visibility+ " km");

                TextView t5= rootView.findViewById(R.id.ozone_value);
                t5.setText(ozone + " DU");

                TextView t6= rootView.findViewById(R.id.cloudcover_value);
                t6.setText(cloudCover+ "%");

                TextView t7= rootView.findViewById(R.id.humidity_value);
                t7.setText(humidity+ "%");

                ImageView i1= rootView.findViewById(R.id.wind_image);
                ImageView i2= rootView.findViewById(R.id.pressure_image);
                ImageView i3= rootView.findViewById(R.id.precipitation_image);
                ImageView i4= rootView.findViewById(R.id.temperature_image);
                ImageView i5= rootView.findViewById(R.id.humidity_image);
                ImageView i6= rootView.findViewById(R.id.visibility_image);
                ImageView i7= rootView.findViewById(R.id.cloudcover_image);
                ImageView i8= rootView.findViewById(R.id.ozone_image);

                int color= Color.rgb(0, 0, 0);
                colorImage(i1, R.drawable.weather_windy, color);
                colorImage(i2, R.drawable.gauge, color);
                colorImage(i3, R.drawable.weather_pouring, color);

                colorImage(i4, R.drawable.thermometer, color);
                colorImage(i5, R.drawable.water_percent, color);

                colorImage(i6, R.drawable.eye_outline, color);
                colorImage(i7, R.drawable.weather_fog, color);
                colorImage(i8, R.drawable.earth, color);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
