package com.hellohasan.sqlite_project.Features.Linea.UpdateLineaInfo;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import com.hellohasan.sqlite_project.Database.DatabaseQueryClass;
import com.hellohasan.sqlite_project.Features.Linea.CreateLinea.Linea;
import com.hellohasan.sqlite_project.R;
import com.hellohasan.sqlite_project.Util.Config;


public class LineaUpdateDialogFragment extends DialogFragment {

    private static long lineaRegNo;
    private static int lineaItemPosition;
    private static LineaUpdateListener lineaUpdateListener;

    private Linea mLinea;

    private EditText nameEditText;
    private EditText stateEditText;
    private Button updateButton;
    private Button cancelButton;

    private static String nameString = "";
    private String state="A";

    private DatabaseQueryClass databaseQueryClass;

    public LineaUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static LineaUpdateDialogFragment newInstance(String name, int position, LineaUpdateListener listener){
        nameString = name;
        lineaItemPosition = position;
        lineaUpdateListener = listener;
        LineaUpdateDialogFragment lineaUpdateDialogFragment = new LineaUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update linea information");
        lineaUpdateDialogFragment.setArguments(args);

        lineaUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return lineaUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_linea_update_dialog, container, false);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        nameEditText = view.findViewById(R.id.lineaNameEditText);
       stateEditText = view.findViewById(R.id.state);
        updateButton = view.findViewById(R.id.updateLineaInfoButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        mLinea = databaseQueryClass.getLineaName(nameString);

        if(mLinea!=null) {
            Log.d("Agregando", "onCreateView: ");
            nameEditText.setText(mLinea.getName());
            stateEditText.setText(mLinea.getState());

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameString = nameEditText.getText().toString();
                    state = (stateEditText.getText().toString());

                    mLinea.setName(nameString);
                    mLinea.setState(state);

                    long id = databaseQueryClass.updateLineaInfo(mLinea);

                    if(id>0){
                        lineaUpdateListener.onLineaInfoUpdated(mLinea, lineaItemPosition);
                        getDialog().dismiss();
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

}
