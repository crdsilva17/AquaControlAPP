package com.example.crdsilva;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

public class ColorPickerFrag extends Fragment {
    private ImageView colorPicker;
    private TextView displayValues;
    private View displayColors;
    private Bitmap bitmap;
    private String hex;
    private int r, g, b;

    public ColorPickerFrag(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n", "UseRequireInsteadOfGet"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View display = inflater.inflate(R.layout.fragment_color_picker, container, false);
        colorPicker = display.findViewById(R.id.colorPickers);
        displayValues = display.findViewById(R.id.displayValues);
        displayColors = display.findViewById(R.id.displayColors);
        Button btnCancel = display.findViewById(R.id.btn_Cancel);
        Button btnConfirma = display.findViewById(R.id.btnConfirma);
        colorPicker.setDrawingCacheEnabled(true);
        colorPicker.buildDrawingCache(true);

        btnCancel.setOnClickListener(view -> {
            requireActivity().findViewById(R.id.tela).setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.menu).setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().
                    remove(ColorPickerFrag.this).commit();
        });

        btnConfirma.setOnClickListener(view -> {
            requireActivity().findViewById(R.id.tela).setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).findViewById(R.id.menu).setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).onColorSend(r, g, b,hex);
            getActivity().getSupportFragmentManager().beginTransaction().
                    remove(ColorPickerFrag.this).commit();
        });

        colorPicker.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
                    motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                bitmap = colorPicker.getDrawingCache();
                int pixels = bitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());

                r = Color.red(pixels);
                g = Color.green(pixels);
                b = Color.blue(pixels);

                if(r > 15)
                    hex = "#"+ Integer.toHexString(r);
                else
                    hex = "#0"+Integer.toHexString(r);

                if (g > 15)
                    hex += Integer.toHexString(g);
                else
                    hex += "0"+ Integer.toHexString(g);

                if(b > 15)
                    hex += Integer.toHexString(b);
                else
                    hex += "0"+ Integer.toHexString(b);

                Log.d("ledCor", hex);

                displayColors.setBackgroundColor(Color.rgb(r,g,b));

                displayValues.setText("HEX: "+hex+"\n RGB: "+r+", "+g+", "+b);



            }
            return true;
        });

        return display;
    }
}