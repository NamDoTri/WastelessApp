package com.wasteless.ui.add_transaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.wasteless.R;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTransactionFragment extends Fragment {
    DatePickerDialog picker;
    TextView tvw;
    boolean isIncome;
    private EditText editText;
    private Button button;
    private ChipGroup chipGroup;
    ArrayList<String> tags =new ArrayList<String>();

    private AddTransactionViewModel addTransactionViewModel;
    private AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addTransactionViewModel = ViewModelProviders.of(this).get(AddTransactionViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_add_new_transaction, container, false);
        tvw = root.findViewById(R.id.date);
        // Tags
        editText = root.findViewById(R.id.e);
        chipGroup = root.findViewById(R.id.chipGroup);
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s,
                                      int start, int before, int count)
            {
                if (s.toString().length() == 1 && s.toString().contains(" ")){
                    editText.setText("");
                } else if(s.toString().indexOf(" ") != -1)
                {
                    Log.i("key",  s.toString());
                    addNewChip(root, editText.getText().toString());
                    editText.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Picking up the date
        root.findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvw.setText(((dayOfMonth<9)? "0" : "") + dayOfMonth + "/" + ((monthOfYear<9)? "0" : "") +  (monthOfYear + 1) + "/" + year); // dd/mm/yyy
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        Spinner spinner;
        spinner = root.findViewById(R.id.category);
        List<String> categoryList = addTransactionViewModel.getAllCategories();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.custom_spinner, categoryList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        checkTheExpense(root);

        // Gather all the input fields together
        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText dateField = root.findViewById(R.id.date);
                Spinner categoryField = root.findViewById(R.id.category);
                EditText sumField = root.findViewById(R.id.sum);
                EditText descriptionField = root.findViewById(R.id.description);
                EditText sourceField = root.findViewById(R.id.source);

                String date = dateField.getText().toString();
                String category = String.valueOf(categoryField.getSelectedItem());
                String sum = sumField.getText().toString().trim();
                //Float sum1 = Float.parseFloat(sumField.getText().toString().trim());
//                String tags= tags.toString();
                String source = sourceField.getText().toString();
                String description = descriptionField.getText().toString();

                //Validation of all the input fields
                if(date.trim().length() > 0 &&
                        category.trim().length() > 0 &&
                        sum.trim().length() > 0 &&
//                        tags.trim().length() > 0 &&
                        description.trim().length() > 0) {
                    if (isIncome == false) {
                        addTransactionViewModel.insertExpense(date, Float.parseFloat(sum), description, Long.valueOf(1), false, category);
                        successMessage();
                    } else {
                        if (source.trim().length() <= 0) {
                            failedMessage();
                        } else {
                            addTransactionViewModel.insertIncome(date, Float.parseFloat(sum), description, Long.valueOf(1), true, category, source);
                            successMessage();
                        }
                    }
                } else {
                    failedMessage();
                    Log.i("transaction", "something is missed");
                };
            }

            private void failedMessage() {
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
            }

            private void successMessage() {
                // Pop-up message
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Done");
                alertDialog.setMessage("Succesfully added");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                AddTransactionFragment homeFragment = new AddTransactionFragment();
                                FragmentTransaction changeTheScreen = getFragmentManager().beginTransaction();
                                changeTheScreen.replace(R.id.nav_host_fragment, homeFragment);
                                changeTheScreen.addToBackStack(null);
                                changeTheScreen.commit();
                            }
                        });
                alertDialog.show();
            }
        });
        return root;
    }

    private void addNewChip(View root, final String text) {
        tags.add(text);
        final Chip chip = new Chip(getContext());
        ChipDrawable drawable = ChipDrawable.createFromAttributes(getContext(),
                null, 0 , R.style.Widget_MaterialComponents_Chip_Entry);
        chip.setChipDrawable(drawable);
        chip.setCheckable(false);
        chip.setClickable(false);
        chip.setChipIconResource(R.drawable.ic_extension_black_24dp);
        chip.setIconStartPadding(3f);
        chip.setPadding(60, 10, 60, 10);
        chip.setText(text);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);
                tags.remove(chip.getText().toString());
                String yes = chip.getText().toString();
                Log.i("tag", String.valueOf(tags.size()));
            }
        });
        chipGroup.addView(chip);
    }

    private void checkTheExpense(final View root) {
        RadioGroup radioGroup = root.findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (root.findViewById(R.id.expense).getId() == checkedId) {
                    root.findViewById(R.id.source).setVisibility(View.GONE);
                    isIncome = false;
                } else {
                    root.findViewById(R.id.source).setVisibility(View.VISIBLE);
                    isIncome = true;
                }
            }
        });
    }
}
