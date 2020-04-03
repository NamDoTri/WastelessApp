package com.wasteless.ui.home.expenses;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wasteless.R;
import com.wasteless.ui.transaction.TransactionViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ExpensesFragment extends Fragment {
    private ExpensesViewModel expensesViewModel;
    private TransactionViewModel transactionViewModel;

    private PieChart expensePieChart;
    private BarChart expenseBarChart;

    private boolean usePercentage = true;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_expenses, container, false);
        expensesViewModel = ViewModelProviders.of(this).get(ExpensesViewModel.class);
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);

        final Bundle expensesBundle = this.getArguments();

        expensePieChart = root.findViewById(R.id.expenses_pie_chart);
        expenseBarChart = root.findViewById(R.id.expenses_bar_chart);

        renderMonthlyExpensesPieChart();
        renderMonthlyExpenseBarChart();

        Context context = getContext();
        CharSequence text = String.valueOf(expensesBundle.getLong("walletId"));
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        return root;
    }

    private void renderMonthlyExpensesPieChart(){
        //EXPENSE PIE CHART
        PieData expensePieChartData = expensesViewModel.getMonthlyExpensePieChart();
        //expensePieChartData.setHighlightEnabled(false);
        //expensePieChart.setHighlightPerTapEnabled(false);

        //Value settings
        expensePieChartData.setValueTextSize(20f);
        expensePieChartData.setValueTextColor(Color.DKGRAY);
        expensePieChartData.setValueFormatter(new PercentFormatter(expensePieChart));


        //Chart settings
        expensePieChart.setUsePercentValues(usePercentage);
        expensePieChart.setTransparentCircleRadius(35f);
        expensePieChart.setHoleRadius(30f);
        expensePieChart.getDescription().setEnabled(false);

        //Center text settings
        //expensePieChart.setCenterText(homeViewModel.getTotalExpensesByMonth() +"€");
        expensePieChart.setCenterTextSize(27f);

        //Entry label settings --- Removing the labels for now since sometimes they seem to overlap
        expensePieChart.setDrawEntryLabels(false);

        //Legend settings
        Legend expensePieChartLegend = expensePieChart.getLegend();
        expensePieChartLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        expensePieChartLegend.setTextSize(15f);

        expensePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            private Entry prevSelectedEntry = null;
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // only if user select the same value again
                if(prevSelectedEntry == null){
                    usePercentage = !usePercentage;
                    expensePieChart.setUsePercentValues(usePercentage);
                    //expensePieChart.getData().setValueFormatter(new EuroFormatter());
                }
                prevSelectedEntry = e;
            }

            @Override
            public void onNothingSelected() {
                usePercentage = !usePercentage;
                expensePieChart.setUsePercentValues(usePercentage);
                prevSelectedEntry = null;
            }
        });

        expensePieChart.setData(expensePieChartData);
        expensePieChart.notifyDataSetChanged();
        expensePieChart.invalidate();


    }

    private void renderMonthlyExpenseBarChart(){
        //EXPENSE BAR CHART --- idea: stack expenses per category in different colors for each day
        //TODO: scale the chart if there is a huge amount of expenses on one day
        BarData expenseBarChartData = expensesViewModel.getExpenseBarChart();
        expenseBarChartData.setValueFormatter(new ZeroFormatter());
        XAxis xAxis = expenseBarChart.getXAxis();
        YAxis yAxisLeft = expenseBarChart.getAxisLeft();
        YAxis yAxisRight =  expenseBarChart.getAxisRight();

        //Label settings
        ArrayList dates = expensesViewModel.getDateLabels();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setLabelCount(dates.size()/2);

        //Testing scaling down
        /*if (expenseBarChart.getYChartMax() > 500) {
            expenseBarChart.setScaleMinima(1f,5f);
        }
        else if (expenseBarChart.getYChartMax() > 1000){
            expenseBarChart.setScaleMinima(1f, 10f);
        }*/

        //float maxVal = expenseBarChart.getYChartMax();


        //Axis settings
        yAxisLeft.setAxisMinimum(0);
        yAxisRight.setAxisMinimum(0);
        yAxisLeft.setValueFormatter(new EuroFormatter());
        yAxisRight.setValueFormatter(new EuroFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //Grid settings
        yAxisLeft.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);

        //Descriptive settings
        expenseBarChart.getLegend().setEnabled(false);
        expenseBarChart.getDescription().setEnabled(false);

        expenseBarChart.setData(expenseBarChartData);
        expenseBarChart.notifyDataSetChanged();
        expenseBarChart.invalidate();
    }

    //Testing out these formatters
    private static class EuroFormatter extends IndexAxisValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return new DecimalFormat("#").format(value) + "€";
        }
    }

    private static class ZeroFormatter extends IndexAxisValueFormatter{
        @Override
        public String getFormattedValue(float value) {
            if(value > 0){
                return new DecimalFormat("#").format(value);
            }
            else{
                return "";
            }
        }
    }
}
