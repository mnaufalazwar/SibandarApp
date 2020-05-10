package com.mnaufalazwar.sibandarapp.common;

public class NumberToRupiah {

    public static String convertNumberToRupiah(String numb){

        String temp = "";
        int dot = 1;

        for(int i = numb.length() - 1 ; i >= 0 ; i--){
            if(dot == 3 && i != 0){
                temp = "." + numb.charAt(i) + temp;
                dot=1;
            }
            else{
                temp = numb.charAt(i) + temp;
                dot++;
            }
        }

        return "Rp " + temp;
    }

}
