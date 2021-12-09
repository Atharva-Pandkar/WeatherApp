package com.example.weatherapp;

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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.*;
import com.highsoft.highcharts.common.hichartsclasses.HIArearange;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HILine;
import com.highsoft.highcharts.common.hichartsclasses.HIMarker;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WeeklyFragment extends Fragment {

    private String TAG="WeeklyFragment";
    private JSONObject weatherInfo=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.weekly_tab, container, false);

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

    private void fillInfo(View rootView){

        if(weatherInfo !=null)
        {
            Log.d(TAG, "in fill info");

            String icon, summary;

            try {

                HIChartView chartView = rootView.findViewById(R.id.hc);

                HIOptions options = new HIOptions();

                HIChart chart = new HIChart();
                chart.setType("arearange");
                options.setChart(chart);

                HITitle title = new HITitle();
                title.setText("Temperature variation by day ");
                options.setTitle(title);

                HIXAxis xaxis = new HIXAxis();
                xaxis.setType("datetime");
                options.setXAxis(new ArrayList<>(Collections.singletonList(xaxis)));

                HIYAxis yaxis = new HIYAxis();
                yaxis.setTitle(new HITitle());
                yaxis.getTitle().setText("");
                options.setYAxis(new ArrayList<>(Collections.singletonList(yaxis)));

                HITooltip tooltip = new HITooltip();
                tooltip.setShared(true);
                tooltip.setValueSuffix("°F");
                options.setTooltip(tooltip);

                HILegend legend = new HILegend();
                options.setLegend(legend);

                HILine line = new HILine();
                line.setName("Temperature");
                line.setZIndex(1);
                line.setMarker(new HIMarker());
                line.getMarker().setFillColor(HIColor.initWithName("white"));
                line.getMarker().setLineWidth(2);
                line.getMarker().setLineColor(String.valueOf(HIColor.initWithHexValue("7cb5ec")));
                Number[][] lineData = new Number[][]{
                        {1246406400000L, 21.5},
                        {1246492800000L, 22.1},
                        {1246579200000L, 23},
                        {1246665600000L, 23.8},
                        {1246752000000L, 21.4},
                        {1246838400000L, 21.3},
                        {1246924800000L, 18.3},
                        {1247011200000L, 15.4},
                        {1247097600000L, 16.4},
                        {1247184000000L, 17.7},
                        {1247270400000L, 17.5},
                        {1247356800000L, 17.6},
                        {1247443200000L, 17.7},
                        {1247529600000L, 16.8}
                };
                //line.setData(new ArrayList<>(Arrays.asList(lineData)));

                HIArearange arearange = new HIArearange();
                arearange.setType("arearange");
                arearange.setName("Temperature");
                arearange.setLineWidth(0);
                arearange.setLinkedTo(":previous");
                arearange.setColor(HIColor.initWithHexValue("7cb5ec"));
                arearange.setFillOpacity(0.3);
                arearange.setZIndex(0);
                Number[][] arearangeData = new Number[][]{
                        {20211118, 14.3, 27.7},
                        {20211119, 14.5, 27.8},
                        {20211120, 15.5, 29.6},
                        {20211121, 16.7, 30.7},
                        {20211122, 16.5, 25.0},
                        {20211123, 17.8, 25.7},
                        {20211124, 13.5, 24.8},
                        {20211125, 10.5, 21.4},
                        {20211126, 9.2, 23.8},
                        {20211127, 11.6, 21.8},
                        {20211128, 10.7, 23.7},
                        {20211129, 11.0, 23.3},
                        {20211130, 11.6, 23.7},
                        {20211131, 11.8, 20.7}
                };
                arearange.setData(new ArrayList<>(Arrays.asList(arearangeData)));

                options.setSeries(new ArrayList<>(Arrays.asList(arearange,  line)));

                chartView.setOptions(options);

                JSONArray data= weatherInfo.getJSONArray("obj");
                int n= data.length();
                ArrayList<Entry> tempLow = new ArrayList<>();
                ArrayList<Entry> tempHigh = new ArrayList<>();


                for(int i=0;i<n && i<8;i++) {

                    JSONObject d = (JSONObject) data.get(i);
                    Log.d(TAG, d.toString());
                    Log.d("ME", d.toString());
                    JSONObject a = d.getJSONObject("values");
                    //long timestamp= d.getInt("startTime");
                    //String date= getDate(d.get("StartTime").toString());
                    double th = a.getDouble("temperatureMax");
                    double tl = a.getDouble("temperatureMin");


                    tempHigh.add(new Entry(i, (int) th));
                    tempLow.add(new Entry(i, (int) tl));
                }

               // arearange.setData(new ArrayList <>(tempHigh,tempLow));


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
