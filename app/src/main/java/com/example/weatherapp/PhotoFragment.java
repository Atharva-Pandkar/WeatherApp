package com.example.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.highsoft.highcharts.common.hichartsclasses.*;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.core.HIChartView;
import com.highsoft.highcharts.core.HIFunction;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.HIArearange;
import com.highsoft.highcharts.common.hichartsclasses.HIBackground;
import com.highsoft.highcharts.common.hichartsclasses.HICSSObject;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIData;
import com.highsoft.highcharts.common.hichartsclasses.HIDataLabels;
import com.highsoft.highcharts.common.hichartsclasses.HIEvents;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HILine;
import com.highsoft.highcharts.common.hichartsclasses.HIMarker;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPane;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISolidgauge;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;
import com.highsoft.highcharts.core.HIFunction;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class PhotoFragment extends Fragment {

    private String TAG="PhotoFragment";
    private JSONObject weatherInfo=null;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<String> imageURls;
    private String renderIconsString = "function renderIcons() {" +
            "                            if(!this.series[0].icon) {" +
            "                               this.series[0].icon = this.renderer.path(['M', -8, 0, 'L', 8, 0, 'M', 0, -8, 'L', 8, 0, 0, 8]).attr({'stroke': '#303030','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}this.series[0].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[0].points[0].shapeArgs.innerR -(this.series[0].points[0].shapeArgs.r - this.series[0].points[0].shapeArgs.innerR) / 2); if(!this.series[1].icon) {this.series[1].icon = this.renderer.path(['M', -8, 0, 'L', 8, 0, 'M', 0, -8, 'L', 8, 0, 0, 8,'M', 8, -8, 'L', 16, 0, 8, 8]).attr({'stroke': '#ffffff','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}this.series[1].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[1].points[0].shapeArgs.innerR -(this.series[1].points[0].shapeArgs.r - this.series[1].points[0].shapeArgs.innerR) / 2); if(!this.series[2].icon) {this.series[2].icon = this.renderer.path(['M', 0, 8, 'L', 0, -8, 'M', -8, 0, 'L', 0, -8, 8, 0]).attr({'stroke': '#303030','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}this.series[2].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[2].points[0].shapeArgs.innerR -(this.series[2].points[0].shapeArgs.r - this.series[2].points[0].shapeArgs.innerR) / 2);}";
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.photos_tab, container, false);

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
                chart.setType("solidgauge");
                chart.setEvents(new HIEvents());
                chart.getEvents().setRender(new HIFunction(renderIconsString));
                options.setChart(chart);

                HITitle title = new HITitle();
                title.setText("Stat summary");
                title.setStyle(new HICSSObject());
                title.getStyle().setFontSize("24px");
                options.setTitle(title);

                HITooltip tooltip = new HITooltip();
                tooltip.setBorderWidth(0);
                tooltip.setBackgroundColor(HIColor.initWithName("none"));
                tooltip.setShadow(false);
                tooltip.setStyle(new HICSSObject());
                tooltip.getStyle().setFontSize("16px");
                tooltip.setPointFormat("{series.name}<br><span style=\"font-size:2em; color: {point.color}; font-weight: bold\">{point.y}%</span>");
                tooltip.setPositioner(
                        new HIFunction(
                                "function (labelWidth) {" +
                                        "   return {" +
                                        "       x: (this.chart.chartWidth - labelWidth) /2," +
                                        "       y: (this.chart.plotHeight / 2) + 15" +
                                        "   };" +
                                        "}"
                        ));
                options.setTooltip(tooltip);

                HIPane pane = new HIPane();
                pane.setStartAngle(0);
                pane.setEndAngle(360);

                HIBackground paneBackground1 = new HIBackground();
                paneBackground1.setOuterRadius("112%");
                paneBackground1.setInnerRadius("88%");
                paneBackground1.setBackgroundColor(HIColor.initWithRGBA(106, 165, 231, 0.35));
                paneBackground1.setBorderWidth(0);

                HIBackground paneBackground2 = new HIBackground();
                paneBackground2.setOuterRadius("87%");
                paneBackground2.setInnerRadius("63%");
                paneBackground2.setBackgroundColor(HIColor.initWithRGBA(51, 52, 56, 0.35));
                paneBackground2.setBorderWidth(0);

                HIBackground paneBackground3 = new HIBackground();
                paneBackground3.setOuterRadius("62%");
                paneBackground3.setInnerRadius("38%");
                paneBackground3.setBackgroundColor(HIColor.initWithRGBA(130, 238, 106, 0.35));
                paneBackground3.setBorderWidth(0);

                pane.setBackground(new ArrayList<>(Arrays.asList(paneBackground1, paneBackground2, paneBackground3)));
                options.setPane(pane);

                HIYAxis yaxis = new HIYAxis();
                yaxis.setMin(0);
                yaxis.setMax(100);
                yaxis.setLineWidth(0);
                yaxis.setTickPositions(new ArrayList<>()); // to remove ticks from the chart
                options.setYAxis(new ArrayList<>(Collections.singletonList(yaxis)));

                HIPlotOptions plotOptions = new HIPlotOptions();
                plotOptions.setSolidgauge(new HISolidgauge());



                HIDataLabels dataLabels = new HIDataLabels();
                dataLabels.setEnabled(false);
                ArrayList<HIDataLabels> dataLabelsList = new ArrayList<>();
                dataLabelsList.add(dataLabels);
                plotOptions.getSolidgauge().setDataLabels(dataLabelsList);
                plotOptions.getSolidgauge().getDataLabels();
                plotOptions.getSolidgauge().setLinecap("round");
                plotOptions.getSolidgauge().setStickyTracking(false);
                plotOptions.getSolidgauge().setRounded(true);
                options.setPlotOptions(plotOptions);

                HISolidgauge solidgauge1 = new HISolidgauge();
                solidgauge1.setName("Move");
                HIData data1 = new HIData();
                data1.setColor(HIColor.initWithRGB(106, 165, 231));
                data1.setRadius("112%");
                data1.setInnerRadius("88%");
                data1.setY(80);
                solidgauge1.setData(new ArrayList<>(Collections.singletonList(data1)));

                HISolidgauge solidgauge2 = new HISolidgauge();
                solidgauge2.setName("Exercise");
                HIData data2 = new HIData();
                data2.setColor(HIColor.initWithRGB(51, 52, 56));
                data2.setRadius("87%");
                data2.setInnerRadius("63%");
                data2.setY(65);
                solidgauge2.setData(new ArrayList<>(Collections.singletonList(data2)));

                HISolidgauge solidgauge3 = new HISolidgauge();
                solidgauge3.setName("Stand");
                HIData data3 = new HIData();
                data3.setColor(HIColor.initWithRGB(130, 238, 106));
                data3.setRadius("62%");
                data3.setInnerRadius("38%");
                data3.setY(50);
                solidgauge3.setData(new ArrayList<>(Collections.singletonList(data3)));

                options.setSeries(new ArrayList<>(Arrays.asList(solidgauge1, solidgauge2, solidgauge3)));

                chartView.setOptions(options);





                JSONArray data= weatherInfo.getJSONArray("obj");
                int n= data.length();
                ArrayList<Entry> tempLow = new ArrayList<>();
                ArrayList<Entry> tempHigh = new ArrayList<>();


                for(int i=0;i<n;i++) {

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
