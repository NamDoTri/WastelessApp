package com.wasteless.ui.add_transaction;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wasteless.R;

public class AddTransactionFromReceiptFragment extends Fragment {
    private AddTransactionViewModel addTransactionViewModel;

    private ImageView previewReceiptImage;
    private TextView tempTextview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        addTransactionViewModel = new ViewModelProvider(requireActivity()).get(AddTransactionViewModel.class);
        final View AddTransactionFromReceiptFragmentView = inflater.inflate(R.layout.fragment_add_transaction_from_gallery, container, false);



        return AddTransactionFromReceiptFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        Uri inputUri = addTransactionViewModel.getInputReceiptUri().getValue();

        previewReceiptImage = (ImageView)getView().findViewById(R.id.preview_receipt);
        tempTextview = getView().findViewById(R.id.temp_text_view);

        if(inputUri != null){
            previewReceiptImage.setImageURI(inputUri);
            addTransactionViewModel.recognizeText(inputUri).observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String text) {
                    // tempTextview.setText(text);
                }
            });
        }else{
            // get bitmap from bundle
            Bundle bitmapBundle = this.getArguments();
            Bitmap receiptBitmap;
            if(bitmapBundle != null){
                receiptBitmap = bitmapBundle.getParcelable("receiptBitmap");
                previewReceiptImage.setImageBitmap(receiptBitmap);
                //recognize text
            }

        }

        getView().findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction(); //TODO: find a new way to do this because this is deprecated
                fragmentTransaction.replace(R.id.nav_host_fragment, addTransactionFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
