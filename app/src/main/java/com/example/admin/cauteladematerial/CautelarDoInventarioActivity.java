package com.example.admin.cauteladematerial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CautelarDoInventarioActivity extends AppCompatActivity {
    private EditText itemmilitarET, iteminfoET, itemmaterialET, itemquantiaET;
    private Spinner itemdestinoSpinner;
    private FirebaseAuth firebaseAuth;
    private DBHelper dbHelper;
    public static TextView resulttextview, resulttextview2, resulttextview3, resulttextview4;
    ImageButton scanbutton, scanbutton2, scanbutton3, scanbutton4, scanbutton5;
    Button additemtodatabase, botaoexport;
    DatabaseReference databaseReference, mdatabaseReference, mdatabaseReference2, dReference, dReference2;

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

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cautelar);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".", "");

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Inventario");
        mdatabaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Cautelados");

        dbHelper = new DBHelper(CautelarDoInventarioActivity.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        resulttextview = findViewById(R.id.editmaterial);
        resulttextview.setFilters(new InputFilter[]{filter});
        resulttextview2 = findViewById(R.id.editmilitar);
        resulttextview2.setFilters(new InputFilter[]{filter});
        resulttextview3 = findViewById(R.id.editinfo);
        resulttextview4 = findViewById(R.id.editquantia);
        additemtodatabase = findViewById(R.id.additembuttontodatabase);
        scanbutton = findViewById(R.id.buttonscan);
        scanbutton2 = findViewById(R.id.buttonscan2);
        scanbutton3 = findViewById(R.id.buttonscan3);
        scanbutton4 = findViewById(R.id.buttonscan4);
        scanbutton5 = findViewById(R.id.buttonscan5);
        itemmilitarET = findViewById(R.id.editmilitar);
        itemmilitarET.setFilters(new InputFilter[]{filter});
        iteminfoET = findViewById(R.id.editinfo);
        itemquantiaET = findViewById(R.id.editquantia);
        itemmaterialET = findViewById(R.id.editmaterial);
        itemmaterialET.setFilters(new InputFilter[]{filter});
        itemdestinoSpinner = findViewById(R.id.spinnerDestinos);

        ArrayAdapter adapterDestinos = ArrayAdapter.createFromResource(this, R.array.Destinos, R.layout.spinner_destinos);
        itemdestinoSpinner.setAdapter(adapterDestinos);

        // String result = finaluser.substring(0, finaluser.indexOf("@"));

// INICIO PUXAR INFORMAÇÕES
        Intent intent = getIntent();
        String materialkey = intent.getStringExtra("material_key");
        String infokey = intent.getStringExtra("info_key");
        itemmaterialET.setText(materialkey);
        iteminfoET.setText(infokey);
// FIM PUXAR INFORMAÇÕES

        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EscanearQrcodeCautelarDoInventarioMaterialActivity.class));
            }
        });

        scanbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EscanearQrcodeCautelarDoInventarioMilitarActivity.class));
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

        scanbutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infopadrao = "Sem alteração";
                resulttextview3.setText(infopadrao);
            }
        });

        additemtodatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarnaPlanilha();
            }
        });

    }


    private void salvarnaPlanilha() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatodatahora = new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatomes = new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatoano = new SimpleDateFormat("yyyy");

        String itemmilitar = itemmilitarET.getText().toString();
        String iteminfo = iteminfoET.getText().toString();
        String itemdata = formatodatahora.format(calendar.getTime());
        String itemano = formatoano.format(calendar.getTime());
        String itemmes = formatomes.format(calendar.getTime());
        String itemdestino = itemdestinoSpinner.getSelectedItem().toString();
        String itemtipo = "Cautelado";
        String itemmaterial = itemmaterialET.getText().toString();
        String itemquantia = itemquantiaET.getText().toString();

        if (!TextUtils.isEmpty(itemmaterial) && !TextUtils.isEmpty(itemmilitar) && !TextUtils.isEmpty(iteminfo) && !TextUtils.isEmpty(itemquantia)) {
        } else {
            Toast.makeText(CautelarDoInventarioActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }

        Items itemsModel = new Items(itemmilitar, iteminfo, itemdata, itemano, itemmes, itemdestino, itemtipo, itemmaterial, itemquantia);
        long id = dbHelper.insertUser(itemsModel);

        if (id > 0) {
            additem();
        } else {
            Toast.makeText(CautelarDoInventarioActivity.this, "Algo deu errado\nErro (x0001)", Toast.LENGTH_SHORT).show();
        }

    }

    // addding item to database
    public void additem() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatodatahora = new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatomes = new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatoano = new SimpleDateFormat("yyyy");

        String itemmilitar = itemmilitarET.getText().toString();
        String iteminfo = iteminfoET.getText().toString();
        String itemdata = formatodatahora.format(calendar.getTime());
        String itemano = formatoano.format(calendar.getTime());
        String itemmes = formatomes.format(calendar.getTime());
        String itemdestino = itemdestinoSpinner.getSelectedItem().toString();
        String itemtipo = "Cautela";
        String itemmaterial = itemmaterialET.getText().toString();
        String itemquantia = itemquantiaET.getText().toString();


        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".", "");

        dReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Inventario");
        dReference2 = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Cautelados");

        if (!TextUtils.isEmpty(itemmaterial) && !TextUtils.isEmpty(itemmilitar) && !TextUtils.isEmpty(iteminfo) && !TextUtils.isEmpty(itemquantia)) {

            dReference.child(itemmaterial).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String itemquantiainv = dataSnapshot.child("itemquantia").getValue().toString();
                    int itemquantiainventario = Integer.parseInt(itemquantiainv);
                    int itemquantiacautela = Integer.parseInt(itemquantia);
                    int soma = (itemquantiainventario - itemquantiacautela);
                    String resultado = Integer.toString(soma);

                    Items items = new Items(itemmilitar, iteminfo, itemdata, itemano, itemmes, itemdestino, itemtipo, itemmaterial, itemquantia);

                    if (itemquantiainventario < itemquantiacautela) {
                        Toast.makeText(CautelarDoInventarioActivity.this, "Erro: Quantia indisponível\nNo inventário: " + itemquantiainv, Toast.LENGTH_SHORT).show();
                    } else {
                        if (itemquantiainventario == itemquantiacautela) {
                            dReference.child(itemmaterial).removeValue();
                            dReference2.child(itemmaterial).setValue(items);

                            itemmilitarET.setText("");
                            itemmaterialET.setText("");
                            iteminfoET.setText("");
                            itemquantiaET.setText("1");
//                      itemdestinoSpinner.setSelection(0);
                            Toast.makeText(CautelarDoInventarioActivity.this, itemmaterial + "\nCautelado com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            if (itemquantiainventario > itemquantiacautela) {
                                mdatabaseReference.child(itemmaterial).child("itemquantia").setValue(resultado);
                                mdatabaseReference2.child(itemmaterial).setValue(items);

                                itemmilitarET.setText("");
                                itemmaterialET.setText("");
                                iteminfoET.setText("");
                                itemquantiaET.setText("1");
//                      itemdestinoSpinner.setSelection(0);
                                Toast.makeText(CautelarDoInventarioActivity.this, itemmaterial + "\nCautelado com sucesso", Toast.LENGTH_SHORT).show();
                            } else {
                                itemquantiainventario = 0;
                                if (itemquantiainventario == 0) {
                                    Toast.makeText(CautelarDoInventarioActivity.this, itemmaterial + "\nNão existe no inventário", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

}
