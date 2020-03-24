package com.wasteless.ui.add_transaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wasteless.MainActivity;
import com.wasteless.R;

import java.util.Calendar;

public class AddTransactionFragment extends Fragment {
    String date;

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
                                  date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        // Gather all the input fields together
        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText categoryField = root.findViewById(R.id.category);
                EditText sumField = root.findViewById(R.id.sum);
                EditText tagsField = root.findViewById(R.id.tags);
                EditText descriptionField = root.findViewById(R.id.description);

                String category = categoryField.getText().toString();
                String sum = (sumField.getText().toString().trim());
//                 Float sum1 = Float.parseFloat(sumField.getText().toString().trim());
                String tags = tagsField.getText().toString();
                String description = descriptionField.getText().toString();

                    Log.i("transaction", "something is missed");
            }
        });
        return root;
    }
}
