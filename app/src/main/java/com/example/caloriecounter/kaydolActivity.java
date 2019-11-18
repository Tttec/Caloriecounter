package com.example.caloriecounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class kaydolActivity extends AppCompatActivity {
    EditText et_kullaniciAdi,et_ad,et_Email,et_sifre;

    Button btn_kaydol;

    TextView tv_girisSayfasinaGit;
    FirebaseAuth yetki;
    DatabaseReference reff;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);

        et_kullaniciAdi=findViewById(R.id.et_kullanıcıAdı);
        et_ad=findViewById(R.id.et_Ad);
        et_Email=findViewById(R.id.et_Email);
        et_sifre=findViewById(R.id.et_Sifre);

        btn_kaydol=findViewById(R.id.btn_kaydol_activity);

        tv_girisSayfasinaGit=findViewById(R.id.tv_girisSayfasina_git);

        yetki=FirebaseAuth.getInstance();

        tv_girisSayfasinaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(kaydolActivity.this,girisActivity.class));
            }
        });

        btn_kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(kaydolActivity.this);
                pd.setMessage("Lütfen bekleyin..");
                pd.show();

                String str_kullaniciAdi=et_kullaniciAdi.getText().toString();
                String str_ad=et_ad.getText().toString();
                String str_email=et_Email.getText().toString();
                String str_sifre=et_sifre.getText().toString();

                if(TextUtils.isEmpty(str_kullaniciAdi)||TextUtils.isEmpty(str_ad)
                ||TextUtils.isEmpty(str_email)||TextUtils.isEmpty(str_sifre))
                {
                    Toast.makeText(kaydolActivity.this,"Lütfen bütün alanları doldurun...",Toast.LENGTH_SHORT).show();
                }else if(str_sifre.length()<6)
                {
                    Toast.makeText(kaydolActivity.this,"Şifreniz minimum 6 karakter olmalı..",Toast.LENGTH_SHORT).show();
                }
                else
                {
                //Yeni Kullanıcı kaydetme kodlarını çağır
                    kaydet(str_kullaniciAdi,str_ad,str_email,str_sifre);
                }
            }
        });
    }
    private void kaydet(final String kullaniciadi, final String ad, String email, String sifre)
    {
        //Yeni Kullanıcı kaydetme kodları
        yetki.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener(kaydolActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    FirebaseUser firebaseuser=yetki.getCurrentUser();

                    String kullaniciId=firebaseuser.getUid();

                    reff= FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(kullaniciId);

                    HashMap<String,Object> hashmap=new HashMap<>();
                    hashmap.put("id",kullaniciId);
                    hashmap.put("kullaniciadi",kullaniciadi.toLowerCase());
                    hashmap.put("ad",ad);
                    hashmap.put("bio","");

                    reff.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                pd.dismiss();

                                Intent intent=new Intent(kaydolActivity.this,anasayfaActivity.class  );
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }
                    });
                }
                else
                {
                    pd.dismiss();
                    Toast.makeText(kaydolActivity.this,"Bu mail veya şifre ile kayıt başarısız",Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}
