package com.iaka.android.zinemaxdewata;

import android.content.ComponentName;
import android.content.Intent;
import java.text.NumberFormat;

import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.R.attr.numeric;
import static android.media.CamcorderProfile.get;
import static java.net.Proxy.Type.HTTP;

public class MainActivity extends AppCompatActivity {
    Spinner spinnerJudulFilm, spinnerKelas, spinnerInclude;
    Button tombolBook;
    EditText jumlahText;
    TextView errorText;

    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    List<String> listFilm = new ArrayList<>(Arrays.asList("","Avengers","Black Panther","Spiderman","Justice League","Rumah 2 Dunia"));

    int jumlah;

    boolean valid = false;
    String judul, kelas, include;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jumlahText = (EditText)findViewById(R.id.text_jumlah);

        tombolBook = (Button) findViewById(R.id.tombol_book) ;

        spinnerJudulFilm = (Spinner)findViewById(R.id.spinner_judul_film);
        ArrayAdapter<String> adapterJudul = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFilm);
        adapterJudul.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJudulFilm.setAdapter(adapterJudul);
        errorText = (TextView)spinnerJudulFilm.getSelectedView();

        spinnerKelas = (Spinner) findViewById(R.id.spinner_kelas);
        ArrayAdapter<CharSequence> adapterKelas = ArrayAdapter.createFromResource(this,
                R.array.kelas, android.R.layout.simple_spinner_item);
        adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKelas.setAdapter(adapterKelas);


        spinnerInclude = (Spinner) findViewById(R.id.spinner_include);
        ArrayAdapter<CharSequence> adapterInclude = ArrayAdapter.createFromResource(this,
                R.array.include, android.R.layout.simple_spinner_item);
        adapterInclude.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInclude.setAdapter(adapterInclude);

    }

    public void bookTicket(View view){
        int total = 0;
        String result = "";
        if(isValid()){
            if(kelas.equalsIgnoreCase("regular")){
                total = jumlah * 50000;
            }else{
                total = jumlah * 75000;
            }
            if(!include.equalsIgnoreCase("no")){
                total = total + 30000;
            }
            composeMessage(setSummary(total));
        }
    }

    private boolean isValid(){
        valid = true;
        judul = spinnerJudulFilm.getSelectedItem().toString();
        kelas = spinnerKelas.getSelectedItem().toString();
        include = spinnerInclude.getSelectedItem().toString();

        errorText = (TextView)spinnerJudulFilm.getSelectedView();
        errorText.setTextColor(Color.RED);

        if(TextUtils.isEmpty(judul)){
            setToast("Pilih salah satu judul film");
            errorText.setText("Pilih salah satu judul film");
            valid = false;
            return valid;
        }else{
            errorText.setError("",null);
            errorText.setTextColor(Color.BLACK);
        }

        if(TextUtils.isEmpty(jumlahText.getText().toString())){
            valid = false;
            jumlahText.setError("Jumlah Masih Kosong");
            return valid;
        }else{
            try
            {
                jumlah = Integer.parseInt(jumlahText.getText().toString());
            }
            catch(NumberFormatException nfe)
            {
                jumlahText.setError("Jumlah harus angka");
                valid = false;
                return valid;
            }

            if(jumlah > 5){
                jumlahText.setError("Batas ticket maksimal 5");
                valid = false;
            }else if (jumlah < 1){
                jumlahText.setError("Batas ticket minimal 1");
                valid = false;
            }
        }
        return valid;
    }

    private String setSummary(int total){

        String result = "Hore! Saya telah berhasil memesan tiket film "+judul+" senilai " + formatRupiah.format((double)total)+",00";
        result += " dengan menggunakan aplikasi " + getString(R.string.app_name);
        return result;
    }

    public void composeMessage(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pesanan Tiket");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.send_to)));
    }

    public void setToast(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
