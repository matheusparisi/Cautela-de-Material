package com.example.admin.augscan;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DescautelarActivity extends AppCompatActivity {
    public static TextView resultdeleteview;
    private FirebaseAuth firebaseAuth;
    ImageButton scantodelete;
    Button deletebtn;
    DatabaseReference databaseReference;

    private String blockCharacterSet = (".#$[]@_&*:;!?~\"`•\\√π÷×¶∆£¢€¥^°{}©®™✓[]<>/'");

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descautelar);
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        resultdeleteview = findViewById(R.id.botaomaterialdelete);
        resultdeleteview.setFilters(new InputFilter[] { filter });
        scantodelete = findViewById(R.id.botaoescaneardelete);
        deletebtn= findViewById(R.id.deleteItemToTheDatabasebtn);

        scantodelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EscanearQrcodeDescautelarMaterialActivity.class));
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletefrmdatabase();
            }
        });

    }

    public void deletefrmdatabase()
    {
        String deletebarcodevalue = resultdeleteview.getText().toString();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        if(!TextUtils.isEmpty(deletebarcodevalue)){
            databaseReference.child(resultemail).child("MaterialNome").child(deletebarcodevalue).removeValue();
            Toast.makeText(DescautelarActivity.this,"Descautelado com sucesso",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(DescautelarActivity.this,"Escaneie o QRCODE",Toast.LENGTH_SHORT).show();
        }
    }

}
