package com.wasteless.ui.add_transaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wasteless.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTransactionFragment extends Fragment {
    DatePickerDialog picker;
    TextView tvw;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_add_new_transaction, container, false);
        tvw = root.findViewById(R.id.date);

        // Picking up the date
        root.findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Log.i("data",dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                  tvw.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        Spinner spinner;
        spinner = root.findViewById(R.id.category);
        List<String> list = new ArrayList<String>();
        // Here we need to get categories from the db
        list.add("No category");
        list.add("Girls");
        list.add("Alcohol");
        list.add("Friends");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.custom_spinner, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        // Gather all the input fields together
        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText dateField = root.findViewById(R.id.date);
                Spinner categoryField = root.findViewById(R.id.category);
                EditText sumField = root.findViewById(R.id.sum);
                EditText tagsField = root.findViewById(R.id.tags);
                EditText descriptionField = root.findViewById(R.id.description);

                String date = dateField.getText().toString();
                String category = String.valueOf(categoryField.getSelectedItem());
                String sum = sumField.getText().toString().trim();
                //Float sum1 = Float.parseFloat(sumField.getText().toString().trim());
                String tags = tagsField.getText().toString();
                String description = descriptionField.getText().toString();

                //Validation of all the input fields
                if(date.trim().length() > 0 &&
                        category.trim().length() > 0 &&
                        sum.trim().length() > 0 &&
                        tags.trim().length() > 0 &&
                        description.trim().length() > 0) {
                    // Pop-up message
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Done");
                    alertDialog.setMessage("Succesfully added to nowhere (yet)");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    Log.i("transaction", "category " + category +
                            " sum " + sum + " tags " + tags + " description " + description);
                } else {
                    //Pop-up message
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Failed");
                    alertDialog.setMessage("You may have missed a field");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    Log.i("transaction", "something is missed");
                };
            }
        });
        return root;
    }
}
