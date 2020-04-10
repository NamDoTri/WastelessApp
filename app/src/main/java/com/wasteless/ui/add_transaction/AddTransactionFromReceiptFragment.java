package com.wasteless.ui.add_transaction;

import android.graphics.Bitmap;
import android.graphics.Matrix;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        addTransactionViewModel = new ViewModelProvider(requireActivity()).get(AddTransactionViewModel.class);
        final View AddTransactionFromReceiptFragmentView = inflater.inflate(R.layout.fragment_add_transaction_from_gallery, container, false);
        return AddTransactionFromReceiptFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        previewReceiptImage = (ImageView)getView().findViewById(R.id.preview_receipt);

        Bundle bundle = this.getArguments();

        if(bundle != null && bundle.getParcelable("receiptImage") instanceof Uri){
            Uri inputUri = bundle.getParcelable("receiptImage");
            previewReceiptImage.setImageURI(inputUri);
            addTransactionViewModel.recognizeText(inputUri).observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String text) {
                    // tempTextview.setText(text);
                }
            });
        }else{
            Bitmap receiptBitmap = bundle.getParcelable("receiptImage");
            // rotate bitmap
            // TODO: get rotation degrees programmatically
            Matrix container = new Matrix();
            float degrees = 90;
            container.postRotate(degrees);
            receiptBitmap = Bitmap.createBitmap(receiptBitmap, 0,0, receiptBitmap.getWidth(), receiptBitmap.getHeight(), container, true);

            previewReceiptImage.setImageBitmap(receiptBitmap);
            //recognize text
            addTransactionViewModel.recognizeText(receiptBitmap).observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String text) {
                    // tempTextview.setText(text);
                }
            });
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
