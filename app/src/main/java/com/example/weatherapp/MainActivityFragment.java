package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

public class MainActivityFragment extends Fragment {

    public int position;
    private JSONObject weatherInfo;
    private String place="";
    private String TAG="MainActivityFragment";
    String ats ="Clear day ";
    String MyPREFERENCES = "WeatherApp2";
    String favKey = "fPlaces";
    SharedPreferences sharedPreferences;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        mainActivityFragment.setArguments(bundle);
        return mainActivityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.main_activity_fragment, container, false);

        Bundle b= this.getArguments();
        if(b!= null) {

            try {
                weatherInfo= new JSONObject(b.getString("weather"));
                place = b.getString("place");
                position= b.getInt("position");

                Log.d(TAG, weatherInfo.toString());

                Log.d(TAG, place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("Fragment", "no bundle");
        }

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        FloatingActionButton mapButton = rootView.findViewById(R.id.map_button);
        if(position==0){
            mapButton.hide();
        }

        final Intent intent = new Intent(getContext(), DetailsActivity.class);
        fillInfo(rootView);

        CardView card1= rootView.findViewById(R.id.card1);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call Details Activity and send all weather info in intent
                Intent intent = new Intent( getContext(), DetailsActivity.class);
                intent.putExtra("place", place);
                intent.putExtra("weather", weatherInfo.toString());

                startActivity(intent);
            }
        });

        final MainActivityFragment frag= this;
        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //remove that fragment

                //also remove the place from shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = sharedPreferences.getString(favKey, "");

                ArrayList<String> places = new ArrayList<>();
                if(!json.isEmpty()){
                    Type type= new TypeToken<ArrayList<String>>() {}.getType();
                    places= gson.fromJson(json, type);
                }
                places.remove(place);

                String jsonnew= gson.toJson(places);
                editor.putString(favKey, jsonnew);
                editor.commit();

                Log.d("places", places.toString());
                Toast.makeText(getContext(),place + " was removed from favorites", Toast.LENGTH_SHORT).show();

                ((MainActivity)getActivity()).removeFragment2(position);
            }
        });

        return rootView;
    }
  public int   get_weather(int d) {

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

    public String getDate(long timestamp)
    {
        Calendar cal= Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date= DateFormat.format("MM/dd/yyyy", cal).toString();
        return date;
    }

    public void colorImage(ImageView imageView, int resource, int color)
    {
        Drawable unwrappedDrawable = ContextCompat.getDrawable(getContext(), resource);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        imageView.setImageDrawable(wrappedDrawable);
    }

    public double roundOff(double x)
    {
        //round off to 2 decimals
        double value= Math.round(x* 100.0) /100.0;
        return value;
    }

    private void fillInfo(View rootView){

        //create 8 layouts and add to the scroll view for min and max temp and so on

        if(weatherInfo !=null)
        {
            Log.d(TAG, "in fill info");
            TextView locationTextView= rootView.findViewById(R.id.location);
            locationTextView.setText(place);

            String icon, summary;
            double temperature;
            double humidity, pressure, visibility, windSpeed;

            try {
                JSONArray array  = weatherInfo.getJSONArray("obj");
                JSONObject v = array.getJSONObject(0);
                JSONObject obj = v.getJSONObject("values");
                Log.d("ats",obj.toString());

                icon ="cloudy";
                summary= obj.getString("weatherCode");

                temperature= obj.getDouble("temperature");
                int temp= (int) temperature;


                humidity= obj.getDouble("humidity");
                humidity= roundOff(humidity);

                pressure= obj.getDouble("pressureSeaLevel");
                pressure= roundOff(pressure);

                visibility= obj.getDouble("visibility");
                visibility= roundOff(visibility);

                windSpeed= obj.getDouble("windSpeed");
                windSpeed= roundOff(windSpeed);

                //card1
                ImageView iconImage= rootView.findViewById(R.id.icon);
                int iconResource = get_weather(Integer.valueOf(summary));
                iconImage.setImageResource(iconResource);
                //iconImage.setImageResource(iconResource);

//                if (icon.equals("clear-day")) {
//                    //color icon yellow
//                    colorImage(iconImage, iconResource, Color.rgb(255, 167, 39));
//                } else {
//                    //color image white;
//                    colorImage(iconImage, iconResource, Color.WHITE);
//                }

                TextView tempTextView= rootView.findViewById(R.id.temperature);
                tempTextView.setText(temp + "°F");

                TextView summ= rootView.findViewById(R.id.summary);
                summ.setText(ats);

                //card 2
                TextView t= rootView.findViewById(R.id.rain_value);
                t.setText(humidity + "%");

                TextView t2= rootView.findViewById(R.id.pressure_value);
                t2.setText(pressure + " mb");

                TextView t3= rootView.findViewById(R.id.wind_speed_value);
                t3.setText(windSpeed + " mph");

                TextView t4= rootView.findViewById(R.id.visibility_value);
                t4.setText(visibility+ " km");

                ImageView i1= rootView.findViewById(R.id.humidity_image);
                ImageView i2= rootView.findViewById(R.id.pressure_image);
                ImageView i3= rootView.findViewById(R.id.wind_image);
                ImageView i4= rootView.findViewById(R.id.visibility_image);

                int color= Color.rgb(0, 0, 0);
                colorImage(i1, R.drawable.water_percent, color);
                colorImage(i2, R.drawable.gauge, color);
                colorImage(i3, R.drawable.weather_windy, color);
                colorImage(i4, R.drawable.eye_outline, color);

                LinearLayout scrollViewLL= rootView.findViewById(R.id.scroll_view_linear_layout);



                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                JSONArray data= weatherInfo.getJSONArray("obj");
                int n= data.length();


                for(int i=0;i<n ;i++)
                {

                    JSONObject d = (JSONObject) data.get(i);
                    Log.d(TAG, d.toString());
                    Log.d("ME",d.toString());
                    JSONObject a = d.getJSONObject("values");
                    //long timestamp= d.getInt("startTime");
                    //String date= getDate(d.get("StartTime").toString());
                    double th= a.getDouble("temperatureMax");
                    double tl= a.getDouble("temperatureMin");
                    String table_icon= a.getString("weatherCode");//d.getString("icon");

                    String s1= d.get("startTime").toString();
                    String s2= String.valueOf((int)tl);
                    String s3= String.valueOf((int)th);

                    LayoutInflater inflater= LayoutInflater.from(getContext());
                    View view= inflater.inflate(R.layout.scroll_item, null);
                    LinearLayout linearLayout1= (LinearLayout) view;

                    TextView tv1= linearLayout1.findViewById(R.id.date);
                    tv1.setText(s1);

                    TextView tv2= linearLayout1.findViewById(R.id.maxt);
                    tv2.setText(s2);
                    TextView tv3= linearLayout1.findViewById(R.id.mint);
                    tv3.setText(s3);

                    ImageView iv= linearLayout1.findViewById(R.id.table_icon);
                    int tableiconresource= get_weather(Integer.valueOf(table_icon));
                    iv.setImageResource(tableiconresource);
                    scrollViewLL.addView(linearLayout1);
                }

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
