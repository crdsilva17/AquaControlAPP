package com.example.crdsilva;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Gerenciador {
    private String conteudo;
    private String fileName = "dados.cr";
    private File arquivo;

    public Gerenciador(){

    }

    public void write(Context context, String c){
        arquivo = new File(context.getFilesDir(), fileName);
        if(c.isEmpty())
            this.conteudo = "IP null";
        else
            this.conteudo = "IP "+c;
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)){
            fos.write(conteudo.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TAG", "write: ");
            e.printStackTrace();
        }

    }

    public String Read(Context context, String c){
        String[] line = new String[2];
        arquivo = new File(context.getFilesDir(), fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo.getPath()));
            line = br.readLine().split(" ");
            if(line[0].equals(c)){
                this.conteudo = line[1];
            }else{
                this.conteudo = " ";
            }
            br.close();
        } catch (IOException e) {
            Log.d("TAG", "Read: ");
            e.printStackTrace();
        }
        return this.conteudo;
    }

}
