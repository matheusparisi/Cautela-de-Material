package com.example.admin.augscan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BuscarCauteladosActivity extends AppCompatActivity {
    public static EditText resultsearcheview;
    private static Context mContext;
    private FirebaseAuth firebaseAuth;
    private DBHelper dbHelper;
    ImageButton scantosearch;
    Button searchbtn;
    RecyclerView mrecyclerview;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String resultemail = finaluser.replace(".", "");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Cautelados");

// INICIO BANCO DE DADOS LOCAL
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        dbHelper = new DBHelper(BuscarCauteladosActivity.this);
// FIM BANCO DE DADOS LOCAL


// INICIO OBJETOS
        resultsearcheview = findViewById(R.id.searchfield);
        resultsearcheview.setFilters(new InputFilter[] { filter });
        scantosearch = findViewById(R.id.imageButtonsearch);
        searchbtn = findViewById(R.id.searchbtnn);
        mrecyclerview = findViewById(R.id.recyclerViews);
// FIM OBJETOS

// INICIO FUNÇAO OBJETOS
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        scantosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EscanearQrcodeBuscarCauteladosActivity.class));
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = resultsearcheview.getText().toString();
                firebasesearch(searchtext);
            }
        });

    }
// FIM FUNÇAO OBJETOS


// INICIO APARECER LISTA AO INICIAR
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Items, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, UsersViewHolder>
                (Items.class,
                        R.layout.cautelados_list_layout,
                        UsersViewHolder.class,
                        mdatabaseReference) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Items model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getitemmaterial(), model.getiteminfo(), model.getitemmilitar(), model.getitemdata(), model.getitemdestino(), model.getitemquantia());
            }
        };
        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
// FIM APARECER LISTA AO INICIAR

// INICIO APARECER LISTA AO BUSCAR
    public void firebasesearch(String searchtext) {
        Query firebaseSearchQuery = mdatabaseReference.orderByChild("itemmaterial").startAt(searchtext).endAt(searchtext + "\uf8ff");
        FirebaseRecyclerAdapter<Items, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, UsersViewHolder>
                (Items.class,
                        R.layout.cautelados_list_layout,
                        UsersViewHolder.class,
                        firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Items model, int position) {

                viewHolder.setDetails(getApplicationContext(), model.getitemmaterial(), model.getiteminfo(), model.getitemmilitar(), model.getitemdata(), model.getitemdestino(), model.getitemquantia());
            }
        };
        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
// INICIO APARECER LISTA AO BUSCAR

// INICIO LISTA
    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

// INICIO INSERIR DADOS NA LISTA
        public void setDetails(Context ctx, String itemmaterial, String iteminfo, String itemmilitar, String itemdata, String itemdestino, String itemquantia) {
            TextView item_material = (TextView) mView.findViewById(R.id.viewitemmaterial);
            TextView item_militar = (TextView) mView.findViewById(R.id.viewitemmilitar);
            TextView item_info = (TextView) mView.findViewById(R.id.viewiteminfo);
            TextView item_data = (TextView) mView.findViewById(R.id.viewitemdata);
            TextView item_destino = (TextView) mView.findViewById(R.id.viewitemdestino);
            TextView item_quantia = (TextView) mView.findViewById(R.id.viewitemquantia);

            item_material.setText(itemmaterial);
            item_info.setText(iteminfo);
            item_militar.setText(itemmilitar);
            item_data.setText(itemdata);
            item_destino.setText(itemdestino);
            item_quantia.setText(itemquantia);
            descautelarMaterial();
            excluirMaterial();
        }
// FIM INSERIR DADOS NA LISTA

// INICIO EXCLUIR MATERIAL
        public void excluirMaterial() {

            TextView botaoexcluir = (TextView) mView.findViewById(R.id.buttonExcluir);
            TextView textviewmaterialdelete = (TextView) mView.findViewById(R.id.viewitemmaterial);

            DatabaseReference dReference;

            FirebaseAuth firebaseAuth;
            firebaseAuth = FirebaseAuth.getInstance();
            final FirebaseUser users = firebaseAuth.getCurrentUser();
            String finaluser = users.getEmail();
            String resultemail = finaluser.replace(".", "");

            String deletetextviewmaterialvalue = textviewmaterialdelete.getText().toString();

            dReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Cautelados");

            botaoexcluir.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                        AlertDialog.Builder msgBox = new AlertDialog.Builder(mView.getContext());
                        msgBox.setTitle("Excluir");
                        msgBox.setIcon(R.drawable.ic_x_preto_24dp);
                        msgBox.setMessage("Tem certeza que deseja EXCLUIR o material " + '"' + deletetextviewmaterialvalue + '"' + "?");
                        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dReference.child(deletetextviewmaterialvalue).removeValue();
                                Toast.makeText(mView.getContext(), "Excluído com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        });
                        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Toast.makeText(mView.getContext(), "Ação cancelada", Toast.LENGTH_SHORT).show();
                            }
                        });
                        msgBox.show();
                }
            });
        }
// FIM EXCLUIR MATERIAL

// INICIO DESCAUTELAR MATERIAL
        public void descautelarMaterial() {

            TextView botaodescautelar = (TextView) mView.findViewById(R.id.buttonDescautelar);
            TextView textviewmaterial = (TextView) mView.findViewById(R.id.viewitemmaterial);

            DatabaseReference dReference, dReference2;
            FirebaseAuth firebaseAuth;
            firebaseAuth = FirebaseAuth.getInstance();
            final FirebaseUser users = firebaseAuth.getCurrentUser();
            String finaluser = users.getEmail();
            String resultemail = finaluser.replace(".", "");

            String textviewmaterialvalue = textviewmaterial.getText().toString();

            dReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Cautelados");
            dReference2 = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Inventario");

            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatodatahora = new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatomes = new SimpleDateFormat("MM");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatoano = new SimpleDateFormat("yyyy");

            botaodescautelar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder msgBox = new AlertDialog.Builder(mView.getContext());
                    msgBox.setTitle("Descautelar");
                    msgBox.setIcon(R.drawable.ic_seta_baixo_preto_24dp);
                    msgBox.setMessage("Tem certeza que deseja DESCAUTELAR o material " + '"' + textviewmaterialvalue + '"' + "?");
                    msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int which) {

                            dReference.child(textviewmaterialvalue).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String itemano = formatoano.format(calendar.getTime());
                                    String itemdata = formatodatahora.format(calendar.getTime());
                                    String itemmes = formatomes.format(calendar.getTime());
                                    String itemtipo = "Inventario";
                                    String itemdestino = dataSnapshot.child("itemdestino").getValue().toString();
                                    String iteminfo = dataSnapshot.child("iteminfo").getValue().toString();
                                    String itemmaterial = dataSnapshot.child("itemmaterial").getValue().toString();
                                    String itemmilitar = dataSnapshot.child("itemmilitar").getValue().toString();
                                    String itemquantia = dataSnapshot.child("itemquantia").getValue().toString();

                                    Items itemsinventario = new Items(itemmilitar, iteminfo, itemdata, itemano, itemmes, itemdestino, itemtipo, itemmaterial, itemquantia);
                                    dReference2.child(itemmaterial).setValue(itemsinventario);
                                    dReference.child(itemmaterial).removeValue();
                                    Toast.makeText(mView.getContext(), "Descautelado com sucesso", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    });
                    msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Toast.makeText(mView.getContext(), "Ação cancelada", Toast.LENGTH_SHORT).show();
                        }
                    });
                    msgBox.show();
                }
            });
        }
// FIM DESCAUTELAR MATERIAL

// INICIO EXCEL

}
// FIM LISTA

// MENU PARA DESLOGAR

/*
    private void abrirDescautelar()
    {
        startActivity(new Intent(BuscarCauteladosActivity.this, DescautelarActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.descautelarmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.descautelarMenu:{
                abrirDescautelar();
            }
        }
        return super.onOptionsItemSelected(item);
    }

 */

}