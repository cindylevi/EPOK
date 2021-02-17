package com.example.tp03;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamHelper {
    public static String GetFullStringFromInputReader(InputStream stremEntrada){
        BufferedReader responseReader;
        String responseLine;
        String strResultado="";
        StringBuilder sbResponse;
        try{
            responseReader = new BufferedReader(new InputStreamReader(stremEntrada));
            sbResponse=new StringBuilder();
            while ((responseLine=responseReader.readLine()) != null){
            sbResponse.append(responseLine);
            }
            responseReader.close();
            strResultado = sbResponse.toString();
        } catch (Exception e) {
        }
        return strResultado;
    }
}
