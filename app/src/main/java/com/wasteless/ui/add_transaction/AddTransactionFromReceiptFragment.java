package com.wasteless.ui.add_transaction;

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
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;

public class AddTransactionFromReceiptFragment extends Fragment {
    private AddTransactionViewModel addTransactionViewModel;

    private ImageView previewReceiptImage;
    private TextView tempTextview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        addTransactionViewModel = ViewModelProviders.of(this).get(AddTransactionViewModel.class);
        final View AddTransactionFromReceiptFragmentView = inflater.inflate(R.layout.fragment_add_transaction_from_gallery, container, false);

        return AddTransactionFromReceiptFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        previewReceiptImage = (ImageView)getView().findViewById(R.id.preview_receipt);
        tempTextview = getView().findViewById(R.id.temp_text_view);

        ActivityResultLauncher<String> getReceiptImage = prepareCall(new GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Log.i("receipt", "Image uri: " + result.toString());
                        previewReceiptImage.setImageURI(result);
                        String text = addTransactionViewModel.recognizeText(result);
                        tempTextview.setText(text);
                    }
                });

        getReceiptImage.launch("image/*");
    }
}
