package com.example.weatherapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class SearchableActivity extends AppCompatActivity {

    private LinearLayout mProgressView;
    private Toolbar mToolbar;
    RequestQueue mRequestQueue;
    private JSONObject mWeatherInfo;
    private String query;

    private Boolean isFavorite;

    String TAG = "SearchableActivityTag";
    String ats ="Clear Dya";
    String MyPREFERENCES = "WeatherApp2";
    String favKey = "fPlaces";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Log.d(TAG, "activity started");

        mProgressView = findViewById(R.id.progressbar_view);
        mProgressView.setVisibility(View.VISIBLE);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        checkFavorite();

        Log.d(TAG, "requesting request queue");
        mRequestQueue = Volley.newRequestQueue(SearchableActivity.this);

        Log.d(TAG, "got request queue");
        Intent intent = getIntent();

        query = intent.getStringExtra("query");
        Log.d(TAG, "got query" + query);

        TextView topView = findViewById(R.id.place_searched);
        topView.setText(query);

        doMySearch(query);

        CardView card1 = findViewById(R.id.card1);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call Details Activity and send all weather info in intent
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("weather", mWeatherInfo.toString());
                intent.putExtra("place", query);
                startActivity(intent);
            }
        });

        final FloatingActionButton mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //add the place/query to favourites
                if (isFavorite) {

                    //remove from shared preference
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString(favKey, "");

                    ArrayList<String> places = new ArrayList<>();
                    if(!json.isEmpty()){
                        Type type= new TypeToken<ArrayList<String>>() {}.getType();
                        places= gson.fromJson(json, type);
                    }
                    places.remove(query);

                    String jsonnew= gson.toJson(places);
                    editor.putString(favKey, jsonnew);
                    editor.commit();

                    isFavorite= false;
                    mapButton.setImageResource(R.drawable.map_marker_plus);
                    Toast.makeText(getApplicationContext(),query + " was removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    //add to shared preference
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString(favKey, "");

                    ArrayList<String> places = new ArrayList<>();
                    if(!json.isEmpty()){
                        Type type= new TypeToken<ArrayList<String>>() {}.getType();
                        places= gson.fromJson(json, type);
                    }
                    places.add(query);

                    String jsonnew= gson.toJson(places);
                    editor.putString(favKey, jsonnew);
                    editor.commit();

                    Log.d(TAG, "City added ");
                    isFavorite= true;
                    mapButton.setImageResource(R.drawable.map_marker_minus);
                    Toast.makeText(getApplicationContext(),query + " was added to favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getDate(long timestamp)
    {
        Calendar cal= Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date= DateFormat.format("MM/dd/yyyy", cal).toString();
        return date;
    }


    @Override
    public void finish() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.finish();

    }

    public void checkFavorite() {
        isFavorite= false;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(favKey, "");

        ArrayList<String> places = new ArrayList<>();
        if(!json.isEmpty()){
            Type type= new TypeToken<ArrayList<String>>() {}.getType();
            places= gson.fromJson(json, type);
        }

        Log.d(TAG, "favs: "+ places.toString());
        FloatingActionButton mapbutton = findViewById(R.id.map_button);
        if(places.contains(query)){
            isFavorite = true;
            mapbutton.setImageResource(R.drawable.map_marker_minus);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doMySearch(String query) {
        Log.d(TAG, "domysearch:" + query);
        String ats = query.replace(" ","+");
        Log.d(TAG, "domysearchnew:" + ats);
        getLatLong(query);
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

    private int getIconDrawable(String summary) {
        int icon = R.drawable.weather_sunny;
        Log.d(TAG, summary);

        if (summary.equals("clear-day")) {
            icon = R.drawable.weather_sunny;
        } else if (summary.equals("clear-night")) {
            icon = R.drawable.weather_night;
        } else if (summary.equals("rain")) {
            icon = R.drawable.weather_rainy;
        } else if (summary.equals("sleet")) {
            icon = R.drawable.weather_snowy_rainy;
        } else if (summary.equals("snow")) {
            icon = R.drawable.weather_snowy;
        } else if (summary.equals("wind")) {
            icon = R.drawable.weather_windy_variant;
        } else if (summary.equals("fog")) {
            icon = R.drawable.weather_fog;
        } else if (summary.equals("cloudy")) {
            icon = R.drawable.weather_cloudy;
        } else if (summary.equals("partly-cloudy-night")) {
            icon = R.drawable.weather_night_partly_cloudy;
        } else if (summary.equals("partly-cloudy-day")) {
            icon = R.drawable.weather_partly_cloudy;
        }
        return icon;
    }

    public void colorImage(ImageView imageView, int resource, int color)
    {
        Drawable unwrappedDrawable = ContextCompat.getDrawable(getApplicationContext(), resource);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        imageView.setImageDrawable(wrappedDrawable);
    }

    public double roundOff(double x)
    {
        double value= Math.round(x* 100.0) /100.0;
        return value;
    }

    private void fillInfo(){

        //create 8 layouts and add to the scroll view for min and max temp and so on

        if(mWeatherInfo !=null)
        {
            Log.d(TAG, "in fill info");
            TextView locationTextView= findViewById(R.id.location);
            locationTextView.setText(query);

            String  summary;
            double temperature;
            double humidity, pressure, visibility, windSpeed;
            int icon;
            try {
                JSONArray array  = mWeatherInfo.getJSONArray("obj");
                JSONObject v = array.getJSONObject(0);
                JSONObject obj = v.getJSONObject("values");
                Log.d("ats",obj.toString());
                summary= obj.getString("weatherCode");
                icon = Integer.valueOf(summary);
                temperature= obj.getDouble("temperature");
                int temp= (int) temperature;



                humidity= obj.getDouble("humidity");
                humidity= humidity*100;
                humidity= roundOff(humidity);

                pressure= obj.getDouble("pressureSeaLevel");
                pressure= roundOff(pressure);

                visibility= obj.getDouble("visibility");
                visibility= roundOff(visibility);

                windSpeed= obj.getDouble("windSpeed");
                windSpeed= roundOff(windSpeed);

                //card1
                ImageView iconImage= findViewById(R.id.icon);
                int iconResource = get_weather(icon);
                iconImage.setImageResource(iconResource);


                TextView tempTextView= findViewById(R.id.temperature);
                tempTextView.setText(temp + "°F");

                TextView summ= findViewById(R.id.summary);
                summ.setText(ats);

                //card 2
                TextView t= findViewById(R.id.rain_value);
                t.setText(humidity + "%");

                TextView t2= findViewById(R.id.pressure_value);
                t2.setText(pressure + " mb");

                TextView t3= findViewById(R.id.wind_speed_value);
                t3.setText(windSpeed + " mph");

                TextView t4= findViewById(R.id.visibility_value);
                t4.setText(visibility+ " km");

                ImageView i1= findViewById(R.id.humidity_image);
                ImageView i2= findViewById(R.id.pressure_image);
                ImageView i3= findViewById(R.id.wind_image);
                ImageView i4= findViewById(R.id.visibility_image);

                int color= Color.rgb(0, 0, 0);

                colorImage(i1, R.drawable.water_percent, color);
                colorImage(i2, R.drawable.gauge, color);
                colorImage(i3, R.drawable.weather_windy, color);
                colorImage(i4, R.drawable.eye_outline, color);

                LinearLayout scrollViewLL= findViewById(R.id.scroll_view_linear_layout);
                JSONArray data= mWeatherInfo.getJSONArray("obj");

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

                int n= data.length();
                for(int i=0;i<n && i<8;i++)
                {
                    JSONObject d = (JSONObject) data.get(i);
                    Log.d(TAG, d.toString());
                    Log.d("ME",d.toString());
                    JSONObject a = d.getJSONObject("values");
                    //long timestamp= d.getInt("startTime");
                    //String date= getDate(d.get("StartTime").toString());
                    double th= a.getDouble("temperatureMax");
                    double tl= a.getDouble("temperatureMin");
                    String table_icon= "cloudy";//d.getString("icon");

                    String s1= d.get("startTime").toString();
                    String s2= String.valueOf((int)tl);
                    String s3= String.valueOf((int)th);

                    LayoutInflater inflater= LayoutInflater.from(getApplicationContext());
                    View view= inflater.inflate(R.layout.scroll_item, null);
                    LinearLayout linearLayout1= (LinearLayout) view;

                    TextView tv1= linearLayout1.findViewById(R.id.date);
                    tv1.setText(s1);

                    TextView tv2= linearLayout1.findViewById(R.id.maxt);
                    tv2.setText(s2);
                    TextView tv3= linearLayout1.findViewById(R.id.mint);
                    tv3.setText(s3);

                    ImageView iv= linearLayout1.findViewById(R.id.table_icon);
                    int tableiconresource= get_weather(icon);

                    scrollViewLL.addView(linearLayout1);
                }

                mProgressView.setVisibility(View.GONE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getLatLong(String query) {
        TextView loc = findViewById(R.id.location);
        loc.setText(query);

        String weather_url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyBy0JB1QX5Of4-cAUDLzxwPQRkOy-5zphA&address=" + query;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, weather_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d(TAG, "Response" + response.toString());

                        try {
                            JSONArray results = response.getJSONArray("results");
                            JSONObject obj = results.getJSONObject(0);
                            JSONObject geometry = obj.getJSONObject("geometry");

                            JSONObject location = geometry.getJSONObject("location");
                            Double lat = location.getDouble("lat");
                            Double lng = location.getDouble("lng");
                            Log.d(TAG, "lat" + lat + " long: " + lng);

                            getWeather(lat.toString(), lng.toString());

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        mRequestQueue.add(getRequest);
    }

    private void getWeather(String lat, String lng) {

        String weather_url = "https://weathersearch-331907.wl.r.appspot.com/getDailyData/"+lat+","+lng;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, weather_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        mWeatherInfo = response;
                        Log.d(TAG, "weather info:" + mWeatherInfo.toString());
                        fillInfo();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        mRequestQueue.add(getRequest);
    }
}
