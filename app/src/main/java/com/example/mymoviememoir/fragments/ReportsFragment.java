package com.example.mymoviememoir.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Network;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private ArrayList<Integer> postalCodes;
    private ArrayList<Integer> totalNumberOfMovies;
    private int sumTotalOfMovies;
    NetworkConnection networkConnection  = null;
    public ReportsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_fragment, container, false);
        networkConnection = new NetworkConnection();
        startdateEt = view.findViewById(R.id.startdate_form);
        enddateEt = view.findViewById(R.id.enddate_form);
        startDateButton = view.findViewById(R.id.startdate_button);
        endDateButton = view.findViewById(R.id.enddate_button);
        generatePieButton = view.findViewById(R.id.pie_button);
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        pieChart = view.findViewById(R.id.pie_chart);
        Description description = new Description(); description.setTextColor(ColorTemplate.rgb("#ffffff"));
        description.setText("Pie chart data");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleAlpha(0);
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

        return view;
    }

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
            addDataSet();
        }
    }

    private void addDataSet(){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Float> percentage = new ArrayList<>();
        ArrayList<Integer> usedColors = new ArrayList<>();
        ArrayList<Integer> dataColors = addCinemaChartColor();
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
                pieEntries.add(new PieEntry(percentage.get(i), String.valueOf(postalCodes.get(i))));
                usedColors.add(dataColors.get(i));
            }
        }

        //loading data to the pie chart
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Total number of movies in %");
        pieDataSet.setValueTextSize(14);
        pieDataSet.setColors(usedColors);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.animateY(1000);
        pieChart.setDrawEntryLabels(false);

        //add Legend to chart
        Legend pieLegend = pieChart.getLegend();
        pieLegend.setForm(Legend.LegendForm.CIRCLE);
        pieLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        pieLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        pieLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    private ArrayList<Integer> addCinemaChartColor(){
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.argb(250, 5, 5,1));
        colors.add(Color.argb(77, 44, 44,1));
        colors.add(Color.argb(196, 134, 20,1));
        colors.add(Color.argb(203, 240, 38,1));
        colors.add(Color.argb(38, 150, 100,1));
        colors.add(Color.argb(47, 147, 196,1));
        colors.add(Color.argb(32, 30, 176,1));
        colors.add(Color.argb(127, 42, 189,1));
        colors.add(Color.argb(156, 76, 51,1));
        colors.add(Color.argb(170, 173, 173,1));
        return colors;
    }
}
