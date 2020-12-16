package com.hellohasan.sqlite_project.Features.Linea.CreateLinea;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import com.hellohasan.sqlite_project.Database.DatabaseQueryClass;
import com.hellohasan.sqlite_project.Features.Linea.CreateLinea.Linea;
import com.hellohasan.sqlite_project.Features.Linea.CreateLinea.LineaCreateListener;
import com.hellohasan.sqlite_project.R;
import com.hellohasan.sqlite_project.Util.Config;


public class LineaCreateDialogFragment extends DialogFragment {

    private static LineaCreateListener lineaCreateListener;

    private EditText nameEditText;
  //  private EditText stateEditText;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "";
    private char state = 'A';


    public LineaCreateDialogFragment() {
        // Required empty public constructor
    }

    public static LineaCreateDialogFragment newInstance(String title, LineaCreateListener listener){
        lineaCreateListener = listener;
        LineaCreateDialogFragment lineaCreateDialogFragment = new LineaCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        lineaCreateDialogFragment.setArguments(args);

        lineaCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return lineaCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_linea_create_dialog, container, false);

        nameEditText = view.findViewById(R.id.lineaNameEditText);
     //   stateEditText = view.findViewById(R.id.stateEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = nameEditText.getText().toString();
                Linea linea = new Linea(-1, nameString);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertLinea(linea);

                if(id>0){
                    linea.setId((int) id);
                    lineaCreateListener.onLineaCreated(linea);
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
