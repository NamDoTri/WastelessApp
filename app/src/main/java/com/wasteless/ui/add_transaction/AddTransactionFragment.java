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
import android.widget.AdapterView;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.wasteless.R;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTransactionFragment extends Fragment {
    boolean isIncome;
    private ChipGroup chipGroup;
    ArrayList<String> tags = new ArrayList<String>();
    ArrayList<String> allWalletsNames = new ArrayList<String>();

    private EditText inputDescription, inputAmount, inputSource;
    private TextView inputDate;
    private Spinner inputType;
    private Spinner inputWallet;

    private EditText inputTags;
    private Button submitButton;

    private AddTransactionViewModel addTransactionViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addTransactionViewModel = ViewModelProviders.of(this).get(AddTransactionViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_add_new_transaction, container, false);

        // assign view components to variables
        inputDescription = root.findViewById(R.id.description);
        inputAmount = root.findViewById(R.id.amount);
        inputDate = root.findViewById(R.id.date);
        inputType = root.findViewById(R.id.type);
        inputWallet = root.findViewById(R.id.wallet);
        inputSource = root.findViewById(R.id.source);

        //TODO
        inputTags = root.findViewById(R.id.add_tags);
        chipGroup = root.findViewById(R.id.chipGroup);

        submitButton = root.findViewById(R.id.submit_button);

        // handle description
        addTransactionViewModel.getDescription().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //inputDescription.setText(s);
            }
        });
        inputDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                addTransactionViewModel.getDescription().setValue(String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        // handle amount
        addTransactionViewModel.getAmount().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //inputAmount.setText(s);
            }
        });
        inputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                addTransactionViewModel.getAmount().setValue(String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        // handle date
        addTransactionViewModel.getDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                inputDate.setText(s);
            }
        });
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                addTransactionViewModel.getDate().setValue(((dayOfMonth<9)? "0" : "") + dayOfMonth + "/" + ((monthOfYear<9)? "0" : "") +  (monthOfYear + 1) + "/" + year); // dd/mm/yyy //TODO
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        // handle type
        addTransactionViewModel.getType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
            }
        });
        inputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
                addTransactionViewModel.getType().setValue(inputType.getSelectedItem().toString());
            }
            public void onNothingSelected(AdapterView<?> parentView){}
        });
        // category selection menu
        List<String> categoryList = addTransactionViewModel.getAllCategories();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner, categoryList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputType.setAdapter(dataAdapter);

        // handle wallet
        addTransactionViewModel.getWalletId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
            }
        });
        // wallet selection menu
        List<Wallet> allWallets = addTransactionViewModel.getAllWallets();
        for (int i = 0; i < allWallets.size(); i++) {
            allWalletsNames.add(i,allWallets.get(i).name);
            Log.i("array", allWallets.get(i).name);
        }
        ArrayAdapter<String> walletAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner, allWalletsNames);
        walletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputWallet.setAdapter(walletAdapter);

        inputWallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
                addTransactionViewModel.getWalletId().setValue(inputWallet.getSelectedItem().toString());
            }
            public void onNothingSelected(AdapterView<?> parentView){}
        });

        // handle tags
        addTransactionViewModel.getTags().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                //tags = addTransactionViewModel.getTags().getValue();
            }
        });
        inputTags.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().length() == 1 && s.toString().contains(" ")){
                    inputTags.setText("");
                }
                else if(s.toString().indexOf(" ") != -1){
                    addTransactionViewModel.getTags().setValue(tags);
                    addNewChip(root, inputTags.getText().toString()); //TODO
                    inputTags.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        // handle source
        addTransactionViewModel.getSource().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //inputSource.setText(s);
            }
        });
        inputSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                addTransactionViewModel.getSource().setValue(String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // handle isIncome
        addTransactionViewModel.getIsIncome().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                toggleIncomeView(root);
            }
        });
        toggleIncomeView(root);

        // handle submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTransactionViewModel.handleSubmitButtonPress()){
                    successMessage();
                }else {
                    failedMessage();
                }
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
        tags.add(text.trim()); //TODO: find a better way
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
            }
        });
        chipGroup.addView(chip);
    }

    private void toggleIncomeView(final View root) {
        RadioGroup radioGroup = root.findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (root.findViewById(R.id.expense).getId() == checkedId) {
                    root.findViewById(R.id.source).setVisibility(View.GONE);
                    addTransactionViewModel.getIsIncome().setValue(false);
                } else {
                    root.findViewById(R.id.source).setVisibility(View.VISIBLE);
                    addTransactionViewModel.getIsIncome().setValue(true);
                }
            }
        });
    }
}
