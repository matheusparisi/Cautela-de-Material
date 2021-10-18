package com.example.admin.cauteladematerial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cauteladematerial.R;

public class AdicionarAoInventarioActivity extends AppCompatActivity {
    private EditText iteminfoET, itemmaterialET, itemquantiaET;
    public static TextView itemmaterialETQRCODE;
    private FirebaseAuth firebaseAuth;
    private DBHelper dbHelper;
    ImageButton scanbutton, scanbutton2, scanbutton3, scanbutton5;
    Button additemtodatabase;
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
        setContentView(R.layout.activity_adicionar_inventario);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

// INICIO BANCO DE DADOS LOCAL
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        dbHelper = new DBHelper(AdicionarAoInventarioActivity.this);
// FIM BANCO DE DADOS LOCAL

// INICIO EDITTEXT
        iteminfoET = findViewById(R.id.editinfo);
        itemmaterialET = findViewById(R.id.editmaterial);
        itemmaterialETQRCODE = findViewById(R.id.editmaterial);
        itemmaterialETQRCODE.setFilters(new InputFilter[] { filter });
        itemmaterialET.setFilters(new InputFilter[] { filter });
        itemquantiaET = findViewById(R.id.editquantia);
// FIM EDITTEXT

// INICIO OBJETOS
        additemtodatabase = findViewById(R.id.additembuttontodatabase);
        scanbutton = findViewById(R.id.buttonscan);
        scanbutton2 = findViewById(R.id.buttonscan2);
        scanbutton3 = findViewById(R.id.buttonscan3);
        scanbutton5 = findViewById(R.id.buttonscan5);
// FIM OBJETOS

// INICIO FUNÇAO OBJETOS
        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EscanearQrcodeAdicionarAoInventarioMaterialActivity.class));
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
// FIM FUNÇAO OBJETOS

// INICIO ADICIONAR NA PLANILHA
    private void salvarnaPlanilha() {

        String itemmilitar = "Sistema";
        String iteminfo = iteminfoET.getText().toString();
        String itemdata = formatodatahora.format(calendar.getTime());
        String itemano = formatoano.format(calendar.getTime());
        String itemmes = formatomes.format(calendar.getTime());
        String itemdestino = "Inventario";
        String itemtipo = "Adicionado";
        String itemmaterial = itemmaterialET.getText().toString();
        String itemquantia = itemquantiaET.getText().toString();

        if (!TextUtils.isEmpty(itemmaterial) && !TextUtils.isEmpty(itemquantia) && !TextUtils.isEmpty(iteminfo)) {
        }else{
            Toast.makeText(AdicionarAoInventarioActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }

        Items itemsModel = new Items(itemmilitar, iteminfo, itemdata, itemano, itemmes, itemdestino, itemtipo, itemmaterial, itemquantia);
        long id = dbHelper.insertUser(itemsModel);

        if (id > 0) {
            additem();
        } else {
            Toast.makeText(AdicionarAoInventarioActivity.this, "Algo deu errado\nErro (x0001)", Toast.LENGTH_SHORT).show();
        }

    }
// FIM ADICIONAR NA PLANILHA

// INICIO ADICIONAR ITEM AO BANCO DE DADOS
    public void additem() {

        String itemmilitar = "Sistema";
        String iteminfo = iteminfoET.getText().toString();
        String itemdata = formatodatahora.format(calendar.getTime());
        String itemano = formatoano.format(calendar.getTime());
        String itemmes = formatomes.format(calendar.getTime());
        String itemdestino = "Inventario";
        String itemtipo = "Inventario";
        String itemmaterial = itemmaterialET.getText().toString();
        String itemquantia = itemquantiaET.getText().toString();

        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".", "");

        if (!TextUtils.isEmpty(itemmaterial) && !TextUtils.isEmpty(itemquantia) && !TextUtils.isEmpty(iteminfo)) {

            Items items = new Items(itemmilitar, iteminfo, itemdata, itemano, itemmes, itemdestino, itemtipo, itemmaterial, itemquantia);
            databaseReference.child(resultemail).child("Inventario").child(itemmaterial).setValue(items);
            itemquantiaET.setText("1");
            itemmaterialET.setText("");
            iteminfoET.setText("");

            Toast.makeText(AdicionarAoInventarioActivity.this, itemmaterial + "\nAdicionado com sucesso", Toast.LENGTH_SHORT).show();
        } else {
        }
    }
// FIM ADICIONAR ITEM AO BANCO DE DADOS

}
