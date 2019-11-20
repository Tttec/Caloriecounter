package com.example.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class anasayfaActivity extends AppCompatActivity {
    EditText et_besinAdi,et_protein, et_yag, et_karbonhidrat, et_kalori;

    Button btn_besinYolla;

    DatabaseReference reff;
    Besin besin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        besin=new Besin();

        et_besinAdi=findViewById(R.id.et_besinadi);
        et_protein=findViewById(R.id.et_protein);
        et_yag=findViewById(R.id.et_yag);
        et_karbonhidrat=findViewById(R.id.et_karbonhidrat);
        et_kalori=findViewById(R.id.et_kalori);

        btn_besinYolla=findViewById(R.id.btn_besinEkle);

        reff=FirebaseDatabase.getInstance().getReference().child("Besinler");

        btn_besinYolla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String besinadı=et_besinAdi.getText().toString();
                Integer protein=Integer.parseInt(et_protein.getText().toString());
                Integer yag=Integer.parseInt(et_yag.getText().toString());
                Integer karbonhidrat=Integer.parseInt(et_karbonhidrat.getText().toString());
                Integer kalori=Integer.parseInt(et_kalori.getText().toString());

                besin.setName(besinadı);
                besin.setProtein(protein);
                besin.setKarbonhidrat(karbonhidrat);
                besin.setYag(yag);
                besin.setKalori(kalori);
                reff.push().setValue(besin);
                Toast.makeText(anasayfaActivity.this,"Besin eklendi",Toast.LENGTH_SHORT);
            }
        });

    }
}
