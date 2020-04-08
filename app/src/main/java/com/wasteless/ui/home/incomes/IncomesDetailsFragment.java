package com.wasteless.ui.home.incomes;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wasteless.R;
import com.wasteless.ui.home.HomeViewModel;

public class IncomesDetailsFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private PieChart incomePieChart;
    private boolean usePercentage = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_incomes, container, false);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        incomePieChart = (PieChart) root.findViewById(R.id.income_pie_chart);
        renderMonthlyIncomePieChart();
        return root;
    }

    private void renderMonthlyIncomePieChart(){
        PieData incomePieChartData = homeViewModel.getMonthlyIncomePieChart();
        Log.i("chart", incomePieChartData.toString());
        // value settings
        incomePieChartData.setValueTextSize(20f);
        incomePieChartData.setValueTextColor(Color.DKGRAY);
        incomePieChartData.setValueFormatter(new PercentFormatter(incomePieChart)); //TODO: render value inside the slices

        //// chart settings
        incomePieChart.setUsePercentValues(true);
        incomePieChart.setTransparentCircleRadius(35f);
        incomePieChart.setHoleRadius(30f);
        incomePieChart.getDescription().setEnabled(false);

        // center text settings
        //TODO: update this when changing wallet
        incomePieChart.setCenterText(String.valueOf(homeViewModel.getTotalIncomeByMonth())); //TODO: display currency
        incomePieChart.setCenterTextSize(27f);

        // entry label settings
        incomePieChart.setEntryLabelTextSize(17f);
        incomePieChart.setEntryLabelColor(Color.DKGRAY);

        // legends settings
        Legend incomePieChartLegend = incomePieChart.getLegend();
        incomePieChartLegend.setTextSize(15f);

        incomePieChart.notifyDataSetChanged();
        incomePieChart.invalidate();
        incomePieChart.setData(incomePieChartData);

        incomePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            private Entry prevSelectedEntry = null;
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // only if user select the same value again
                if(prevSelectedEntry == null){
                    usePercentage = !usePercentage;
                    incomePieChart.setUsePercentValues(usePercentage);
                }
                prevSelectedEntry = e;
            }

            @Override
            public void onNothingSelected() {
                usePercentage = !usePercentage;
                incomePieChart.setUsePercentValues(usePercentage);
                prevSelectedEntry = null;
            }
        });
    }
}
