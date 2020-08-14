package com.example.photosearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class Dialog extends AppCompatDialogFragment {
//    public  static SQLiteHelper sqLiteHelper;
    private TextInputEditText et_dialog;
    private Integer id;
    private String image;
    private String name;
    private DialogListener listener;

    public Dialog(Integer id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        et_dialog = view.findViewById(R.id.et_dialog);

        et_dialog.setText(name);

        builder.setView(view).setTitle("Edit").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .setPositiveButton("edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DetailActivity.sqLiteHelper.updateData(id.toString(), et_dialog.getText().toString(), image);

                String name = et_dialog.getText().toString();
                listener.applyText(name);
                Log.wtf("update", image);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must");
        }
    }

    public interface DialogListener{
        void applyText(String name);
    }
}
