package com.example.taquio.trasearch6.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch6.R;


/**
 * Created by Edward.
 */

public class ItemDialog extends DialogFragment {

    private static final String TAG = "ConfirmPasswordDialog";

    public interface OnDeletionListener{
         void onConfirmDelete(Boolean check);
    }
    OnDeletionListener mOnDelete;
    public interface OnMarkListener{
        void onConfirmMark(Boolean check);
    }
    OnMarkListener mOnMark;


    //vars
    Boolean checker = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_dialog, container, false);

        Log.d(TAG, "onCreateView: started.");


        TextView confirmMark = view.findViewById(R.id.dialogMark);
        confirmMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = true;
                mOnDelete.onConfirmDelete(true);
            }
        });
        TextView confirmDelete = (TextView) view.findViewById(R.id.dialogDelete);
        confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = true;
                mOnMark.onConfirmMark(true);
            }
        });

        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the dialog");
                getDialog().dismiss();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
//            mOnMark = (OnMarkListener)  getTargetFragment();
            mOnDelete = (OnDeletionListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}




