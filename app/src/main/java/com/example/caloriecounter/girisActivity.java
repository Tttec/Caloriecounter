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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class girisActivity extends AppCompatActivity {

    EditText et_Email_giris,et_sifre_giris;

    Button btn_giris_activity;

    TextView tv_kayitsayfasina_git;

    FirebaseAuth girisyetkisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        et_Email_giris=findViewById(R.id.et_Email_giris);
        et_sifre_giris=findViewById(R.id.et_Sifre_giris);

        btn_giris_activity=findViewById(R.id.btn_giris_activity);

        girisyetkisi=FirebaseAuth.getInstance();

        tv_kayitsayfasina_git=findViewById(R.id.tv_kayıtsayfasina_git);

        tv_kayitsayfasina_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(girisActivity.this,kaydolActivity.class));

            }
        });

        btn_giris_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pdgiris=new ProgressDialog(girisActivity.this);
                pdgiris.setMessage("Giriş yapılıyor...");
                pdgiris.show();

                String str_emailgiris=et_Email_giris.getText().toString();
                String str_sifregiris=et_sifre_giris.getText().toString();

                if(TextUtils.isEmpty(str_emailgiris)||TextUtils.isEmpty(str_sifregiris))
                {
                    Toast.makeText(girisActivity.this,"Bütün alanları doldurun",Toast.LENGTH_LONG).show();
                } else
                {
                    //Giris yapma kodları
                    girisyetkisi.signInWithEmailAndPassword(str_emailgiris,str_sifregiris)
                            .addOnCompleteListener(girisActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        DatabaseReference reffgiris= FirebaseDatabase.getInstance().getReference()
                                                .child("Kullanıcılar").child(girisyetkisi.getCurrentUser().getUid());

                                        reffgiris.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                pdgiris.dismiss();
                                                Intent intent=new Intent(girisActivity.this,anasayfaActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                pdgiris.dismiss();
                                            }
                                        });
                                    }else
                                    {
                                        pdgiris.dismiss();
                                        Toast.makeText(girisActivity.this,"Giriş başarısız oldu",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}
