package com.example.crdsilva;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ColorPickerFrag extends Fragment {
    private View display;
    private ImageView colorPicker;
    private TextView displayValues;
    private View displayColors;
    private Button btnCancel;
    private Button btnConfirma;
    private Bitmap bitmap;
    private String hex;
    private int r, g, b;

    public ColorPickerFrag(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        display = inflater.inflate(R.layout.fragment_color_picker, container, false);
        colorPicker = (ImageView) display.findViewById(R.id.colorPickers);
        displayValues = (TextView) display.findViewById(R.id.displayValues);
        displayColors = display.findViewById(R.id.displayColors);
        btnCancel = (Button) display.findViewById(R.id.btn_Cancel);
        btnConfirma = (Button) display.findViewById(R.id.btnConfirma);
        colorPicker.setDrawingCacheEnabled(true);
        colorPicker.buildDrawingCache(true);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.tela).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.menu).setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().
                        remove(ColorPickerFrag.this).commit();
            }
        });

        btnConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.tela).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.menu).setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).onColorSend(r, g, b,hex);
                getActivity().getSupportFragmentManager().beginTransaction().
                        remove(ColorPickerFrag.this).commit();
            }
        });

        colorPicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
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
            }
        });

        return display;
    }
}