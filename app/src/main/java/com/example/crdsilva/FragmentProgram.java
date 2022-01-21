package com.example.crdsilva;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONObject;


public class FragmentProgram extends Fragment {

    private TextView cLiga, cDesl;
    private CheckBox c1,c2,c3,c4,c5,c6,c7;
    private Button mCancel, mConfirma;

    private int id;
    private int[] pg = new int[7];

    private String[] hr = {"00:00","00:00"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "p1";
    private static final String ARG_PARAM2 = "p2";
    private static final String ARG_PARAM3 = "p3";

    // TODO: Rename and change types of parameters
    private String[] mParam1 = new String[2];
    private int[] mParam2 = new int[7];
    private int mParam3;


    public FragmentProgram() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProgram newInstance(String[] param1, int[] param2, int param3) {
        FragmentProgram fragment = new FragmentProgram();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param1);
        args.putIntArray(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    public String[] getHr() {
        return hr;
    }

    public int[] getPg() {
        return pg;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArray(ARG_PARAM1);
            mParam2 = getArguments().getIntArray(ARG_PARAM2);
            pg = mParam2;
            hr = mParam1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        cLiga = view.findViewById(R.id.clockLiga);
        cDesl = view.findViewById(R.id.clockDesliga);
        c1 = view.findViewById(R.id.cb1);
        c2 = view.findViewById(R.id.cb2);
        c3 = view.findViewById(R.id.cb3);
        c4 = view.findViewById(R.id.cb4);
        c5 = view.findViewById(R.id.cb5);
        c6 = view.findViewById(R.id.cb6);
        c7 = view.findViewById(R.id.cb7);
        mCancel = view.findViewById(R.id.btn_prog_cancel);
        mConfirma = view.findViewById(R.id.btn_prog_Confirmar);

        c1.setChecked(pg[0]==1);
        c2.setChecked(pg[1]==1);
        c3.setChecked(pg[2]==1);
        c4.setChecked(pg[3]==1);
        c5.setChecked(pg[4]==1);
        c6.setChecked(pg[5]==1);
        c7.setChecked(pg[6]==1);
        cLiga.setText(hr[0]);
        cDesl.setText(hr[1]);

        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pg[0]=1;
                }else{
                    pg[0]=0;
                }
            }
        });

        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pg[1]=1;
                }else{
                    pg[1]=0;
                }
            }
        });

        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pg[2]=1;
                }else{
                    pg[2]=0;
                }
            }
        });

        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pg[3]=1;
                }else{
                    pg[3]=0;
                }
            }
        });

        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pg[4]=1;
                }else{
                    pg[4]=0;
                }
            }
        });

        c6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pg[5]=1;
                }else{
                    pg[5]=0;
                }
            }
        });

        c7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pg[6]=1;
                }else{
                    pg[6]=0;
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            //TODO corrigir esta deletando sempre o primeiro
            @Override
            public void onClick(View view) {
                ((Program)getActivity()).deleteFrag(id);
            }
        });

        mConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Program)getActivity()).saveFrag();
                }
        });


        cLiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClock(cLiga, view, hr, 0);
                hr[0] = cLiga.getText().toString();
                Log.d("HR0", "onClick: "+hr[0]);
            }
        });

        cDesl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClock(cDesl, view, hr, 1);
                Log.d("HR1", "onClick: "+hr[1]);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    public void deleteMe(){
        this.onDetach();
        ((Program)getActivity()).removeFromList(mParam3);
        int[][] p = new int[5][7];
        String[][] h = {{"00:00","00:00"},{"00:00","00:00"},{"00:00","00:00"},
                {"00:00","00:00"},{"00:00","00:00"}};
        int t = ((Program)getActivity()).getTotal();
        int i = 0;
        for (FragmentProgram fr: ((Program)getActivity()).getFragmentList()){
            p[i] = fr.pg;
            h[i] = fr.hr;
            i++;
        }
        String doc = "{\"pgr\":[";
        String doc1 = "{\"hr\":[";
        String doc2 = "{\"count\":";
        try {
            for(int k=0; k<5; k++){
                doc+="[";
                doc1+="[";
                for(int j=0;j<7;j++) {
                    doc += p[k][j];
                    if (j < 6) {
                        doc += ",";
                    }
                }
                for (int l=0;l<2;l++){
                    doc1+="\"";
                    doc1+=h[k][l];
                    doc1+="\"";
                    if(l<1)doc1+=",";
                }
                doc+="]";
                doc1+="]";
                if(k<4){
                    doc+=",";
                    doc1+=",";
                }
            }
            doc+="]}";
            doc1+="]}";
            doc2+=t-1+"}";

            ((Program)getActivity()).sendRemove(doc, doc1, doc2);

            Intent intent = new Intent(getActivity().getApplicationContext(), Program.class);
            getActivity().finishAfterTransition();
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setClock(TextView textView, View view, String[] h, int j){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String value = (i<10?"0"+i:i)+":"+(i1<10?"0"+i1:i1);
                textView.setText(value);
                h[j] = value;
            }
        }, 0, 0, true);
        timePickerDialog.show();
    }
}