package com.example.mymoviememoir.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportsFragment extends Fragment {
    private EditText startdateEt;
    private EditText enddateEt;
    private ImageView startDateButton;
    private ImageView endDateButton;
    private PieChart pieChart;
    private Button generatePieButton;
    private Button generateBarButton;
    private Spinner yearSpinner;
    private BarChart barChart;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private ArrayList<Integer> postalCodes;
    private ArrayList<Integer> totalNumberOfMovies;
    private ArrayList<String> months;
    private ArrayList<Float> totalNumberOfMoviesBar;
    private int sumTotalOfMovies;
    NetworkConnection networkConnection  = null;
    public ReportsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_fragment, container, false);

        //views are initialised here because all of them need a view object
        networkConnection = new NetworkConnection();
        startdateEt = view.findViewById(R.id.startdate_form);
        enddateEt = view.findViewById(R.id.enddate_form);
        startDateButton = view.findViewById(R.id.startdate_button);
        endDateButton = view.findViewById(R.id.enddate_button);
        generatePieButton = view.findViewById(R.id.pie_button);
        generateBarButton = view.findViewById(R.id.bar_button);
        yearSpinner  = view.findViewById(R.id.chart_spinner);
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        pieChart = view.findViewById(R.id.pie_chart);
        barChart = view.findViewById(R.id.bar_chart);

        //date picker dialog's separate methods start here
        final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                String datePattern = "yyyy-MM-dd";
                startDateCalendar.set(Calendar.YEAR, year);
                startDateCalendar.set(Calendar.MONTH, month);
                startDateCalendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                startdateEt.setText(dateFormat.format(startDateCalendar.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                String datePattern = "yyyy-MM-dd";
                endDateCalendar.set(Calendar.YEAR, year);
                endDateCalendar.set(Calendar.MONTH, month);
                endDateCalendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                enddateEt.setText(dateFormat.format(endDateCalendar.getTime()));
            }
        };

        //listeners start here
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), startDate, startDateCalendar
                        .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                        startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), endDate, endDateCalendar
                        .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        generatePieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!startdateEt.getText().toString().equals("") && !enddateEt.getText().toString().equals("")){
                    String[] dates = new String[]{startdateEt.getText().toString(), enddateEt.getText().toString()};
                    GetMoviesBySuburbTask getMoviesBySuburbTask = new GetMoviesBySuburbTask();
                    getMoviesBySuburbTask.execute(dates);
                }
            }
        });

        generateBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = yearSpinner.getSelectedItem().toString();
                String[] yearArg = new String[]{year};
                GetMoviesByMonthTask getMoviesByMonthTask = new GetMoviesByMonthTask();
                getMoviesByMonthTask.execute(yearArg);
            }
        });
        return view;
    }

    //method for getting movies by suburb(postcode to be exact) from the database
    private class GetMoviesBySuburbTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            postalCodes = new ArrayList<>();
            totalNumberOfMovies = new ArrayList<>();
            SharedPreferences sf = getContext().getSharedPreferences("dashboardPreferences", Context.MODE_PRIVATE);
            String result = networkConnection.getMoviesByPostcode(sf.getInt("personId", 1), params[0], params[1]);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < jsonArray.length(); i++)
            {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    postalCodes.add(obj.getInt("postcode"));
                    totalNumberOfMovies.add(obj.getInt("totalNumberOfMovies"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("string", postalCodes.toString());
            Log.d("string", totalNumberOfMovies.toString());
            return "The data has been successfully loaded!";
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            Description description = new Description();
            description.setTextColor(ColorTemplate.rgb("#ffffff"));
            description.setText("Pie chart data");
            pieChart.setDescription(description);
            pieChart.setRotationEnabled(true);
            pieChart.setHoleRadius(0f);
            pieChart.setTransparentCircleAlpha(0);
            pieChart.setDescription(description);
            pieChart.setRotationEnabled(true);
            pieChart.setHoleRadius(0f);
            pieChart.setTransparentCircleAlpha(0);
            pieChart.animateY(1000);
            pieChart.setDrawEntryLabels(false);

            //add Legend to chart
            Legend pieLegend = pieChart.getLegend();
            pieLegend.setForm(Legend.LegendForm.CIRCLE);
            pieLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            pieLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            pieLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            addDataSet();
        }
    }

    //method for getting movies watched by each month
    private class GetMoviesByMonthTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            months = new ArrayList<>();
            totalNumberOfMoviesBar = new ArrayList<>();
            SharedPreferences sf = getContext().getSharedPreferences("dashboardPreferences", Context.MODE_PRIVATE);
            String result = networkConnection.getMoviesPerMonth(sf.getInt("personId", 1), params[0]);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < jsonArray.length(); i++)
            {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    months.add(obj.getString("month"));
                    totalNumberOfMoviesBar.add((float)obj.getInt("totalMovies"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("string", months.toString());
            Log.d("string", totalNumberOfMoviesBar.toString());
            return "The data has been successfully loaded!";
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            Description description = new Description();
            description.setTextColor(ColorTemplate.rgb("#ffffff"));
            description.setText("Bar chart data");
            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.setFitBars(true);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
            xAxis.setPosition(XAxis.XAxisPosition.TOP);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(months.size());
            xAxis.setLabelRotationAngle(325);
            addBarDataSet();
        }
    }

    private void addDataSet(){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Float> percentage = new ArrayList<>();
        sumTotalOfMovies = 0;
        for (int number:totalNumberOfMovies)
        {
            sumTotalOfMovies += number;
        }
        for (int number:totalNumberOfMovies){
            percentage.add(number / (float)(sumTotalOfMovies) * 100);
        }
        for(int i = 0; i < postalCodes.size(); i++)
        {
            if(percentage.get(i) > 0f)
            {
                pieEntries.add(new PieEntry(percentage.get(i), String.valueOf(postalCodes.get(i))));;
            }
        }

        //loading data to the pie chart
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Total number of movies in %");
        pieDataSet.setValueTextSize(14);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    //separate method for adding dataset to the bar chart
    private void addBarDataSet(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < months.size(); i++){
            barEntries.add(new BarEntry(i, totalNumberOfMoviesBar.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Total number of movies");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.4f);
        barChart.setData(data);
        barChart.invalidate();
    }
}
