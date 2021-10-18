package com.example.admin.augscan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditarInventarioActivity extends AppCompatActivity {
    private EditText iteminfoET, itemmaterialET, itemquantiaET;
    public static TextView itemmaterialETQRCODE;
    private FirebaseAuth firebaseAuth;
    private DBHelper dbHelper;
    ImageButton scanbutton, scanbutton2, scanbutton3, scanbutton5;
    Button additemtodatabase;
    DatabaseReference mdatabaseReference;

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


// INICIO FORMATOS DE DATA
    Calendar calendar = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatodatahora = new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatomes = new SimpleDateFormat("MM");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatoano = new SimpleDateFormat("yyyy");
// FIM FORMATOS DE DATA

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_inventario);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".", "");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Inventario");

// INICIO BANCO DE DADOS LOCAL
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        dbHelper = new DBHelper(EditarInventarioActivity.this);
// FIM BANCO DE DADOS LOCAL

// INICIO EDITTEXT
        iteminfoET = findViewById(R.id.editinfo);
        itemmaterialET = findViewById(R.id.editmaterial);
        itemmaterialET.setFilters(new InputFilter[] { filter });
        itemmaterialETQRCODE = findViewById(R.id.editmaterial);
        itemmaterialETQRCODE.setFilters(new InputFilter[] { filter });
        itemquantiaET = findViewById(R.id.editquantia);
// FIM EDITTEXT

// INICIO BOTOES
        additemtodatabase = findViewById(R.id.additembuttontodatabase);
        scanbutton = findViewById(R.id.buttonscan);
        scanbutton2 = findViewById(R.id.buttonscan2);
        scanbutton3 = findViewById(R.id.buttonscan3);
        scanbutton5 = findViewById(R.id.buttonscan5);
// FIM BOTOES


// INICIO PUXAR INFORMAÇÕES
        Intent intent = getIntent();
        String materialkey = intent.getStringExtra("material_key");
        String infokey = intent.getStringExtra("info_key");
        String quantiakey = intent.getStringExtra("quantia_key");
        itemmaterialET.setText(materialkey);
        iteminfoET.setText(infokey);
        itemquantiaET.setText(quantiakey);
// FIM PUXAR INFORMAÇÕES

// INICIO FUNÇAO BOTOES
        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemmaterialET.clearFocus();
                startActivity(new Intent(getApplicationContext(), EscanearQrcodeEditarInventarioMaterialActivity.class));
            }
        });

        scanbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemquantiavalor = itemquantiaET.getText().toString();
                if (itemquantiavalor.matches("")) {
                    int valor = 1;
                    itemquantiaET.setText(String.valueOf(valor));
                } else {
                    int valor = Integer.parseInt(itemquantiavalor);
                    valor++;
                    itemquantiaET.setText(String.valueOf(valor));
                }
            }
        });

        scanbutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemquantiavalor = itemquantiaET.getText().toString();
                if (itemquantiavalor.matches("")) {
                    int valor = 1;
                    itemquantiaET.setText(String.valueOf(valor));
                } else {
                    int valor = Integer.parseInt(itemquantiavalor);
                    valor--;
                    itemquantiaET.setText(String.valueOf(valor));
                    if (valor <= 0) {
                        valor = 1;
                        itemquantiaET.setText(String.valueOf(valor));
                    }
                }
            }
        });

        scanbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infopadrao = "Sem alteração";
                iteminfoET.setText(infopadrao);
            }
        });

        additemtodatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarnaPlanilha();
            }
        });

    }
// FIM FUNÇAO BOTOES

// INICIO ADICIONAR NA PLANILHA
    private void salvarnaPlanilha() {

        Intent intent = getIntent();
        String itemmilitar = intent.getStringExtra("militar_key");
        String itemdata = intent.getStringExtra("data_key");
        String itemano = intent.getStringExtra("ano_key");
        String itemmes = intent.getStringExtra("mes_key");
        String itemdestino = "Inventario";
        String itemtipo = "Editado";
        String iteminfo = iteminfoET.getText().toString();
        String itemmaterial = itemmaterialET.getText().toString();
        String itemquantia = itemquantiaET.getText().toString();


        //check edit text data
        if (TextUtils.isEmpty(itemmaterial)) {
            itemmaterialET.setError("Vazio");
            itemmaterialET.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(itemquantia)) {
            itemquantiaET.setError("Vazio");
            itemquantiaET.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(iteminfo)) {
            iteminfoET.setError("Vazio");
            iteminfoET.requestFocus();
            return;
        }

        Items itemsModel = new Items(itemmilitar, iteminfo, itemdata, itemano, itemmes, itemdestino, itemtipo, itemmaterial, itemquantia);
        long id = dbHelper.insertUser(itemsModel);

        if (id > 0) {
            additem();
        } else {
            Toast.makeText(EditarInventarioActivity.this, "Algo deu errado\nErro (x0001)", Toast.LENGTH_SHORT).show();
        }

    }
// FIM ADICIONAR NA PLANILHA

// INICIO ADICIONAR ITEM AO BANCO DE DADOS
    public void additem() {

        Intent intent = getIntent();
        String itemmilitar = intent.getStringExtra("militar_key");
        String itemdata = intent.getStringExtra("data_key");
        String itemano = intent.getStringExtra("ano_key");
        String itemmes = intent.getStringExtra("mes_key");
        String removeritemmaterial = intent.getStringExtra("material_key");
        String itemdestino = "Inventario";
        String itemtipo = "Inventario";
        String iteminfo = iteminfoET.getText().toString();
        String itemmaterial = itemmaterialET.getText().toString();
        String itemquantia = itemquantiaET.getText().toString();

        if (!TextUtils.isEmpty(itemmaterial) && !TextUtils.isEmpty(iteminfo) && !TextUtils.isEmpty(itemquantia)) {

            Items editarinventario = new Items(itemmilitar, iteminfo, itemdata, itemano, itemmes, itemdestino, itemtipo, itemmaterial, itemquantia);

            mdatabaseReference.child(removeritemmaterial).removeValue();
            mdatabaseReference.child(itemmaterial).setValue(editarinventario);

            finish();

            Toast.makeText(EditarInventarioActivity.this, itemmaterial + "\nEditado com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditarInventarioActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }
// FIM ADICIONAR ITEM AO BANCO DE DADOS

}
