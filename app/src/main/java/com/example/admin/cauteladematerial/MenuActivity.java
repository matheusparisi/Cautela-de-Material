package com.example.admin.cauteladematerial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cauteladematerial.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener  {
    private FirebaseAuth firebaseAuth;
    TextView firebasenameview;
    Button toast;


    private CardView addItems, deleteItems, scanItems, viewInventory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        firebasenameview = findViewById(R.id.firebasename);

        // APARECER NOME DO USUÁRIO (LOGADO COMO)

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String result = finaluser.substring(0, finaluser.indexOf("@"));
        String resultemail = result.replace(".","");
        firebasenameview.setText(" Logado como: "+resultemail);
//        toast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(dashboardActivity.this, users.getEmail(), Toast.LENGTH_SHORT).show();
//            }
//        });


        addItems = (CardView)findViewById(R.id.addItems);
        deleteItems = (CardView) findViewById(R.id.deleteItems);
        scanItems = (CardView) findViewById(R.id.scanItems);
        viewInventory = (CardView) findViewById(R.id.viewInventory);

        addItems.setOnClickListener(this);
        deleteItems.setOnClickListener(this);
        scanItems.setOnClickListener(this);
        viewInventory.setOnClickListener(this);
    }

    protected OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }


    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.addItems : i = new Intent(this, CautelarActivity.class); startActivity(i); break;
            case R.id.deleteItems : i = new Intent(this, AdicionarAoInventarioActivity.class);startActivity(i); break;
            case R.id.scanItems : i = new Intent(this, BuscarCauteladosActivity.class);startActivity(i); break;
            case R.id.viewInventory : i = new Intent(this, BuscarInventarioActivity.class);startActivity(i); break;
            default: break;
        }
    }

// MENU PARA DESLOGAR

    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(MenuActivity.this, MainLoginActivity.class));
        Toast.makeText(MenuActivity.this,"Deslogado com sucesso", Toast.LENGTH_SHORT).show();

    }

    private void gerarPlanilha()
    {
        startActivity(new Intent(MenuActivity.this, ExportarExcel.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deslogarmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.logoutMenu:{
                Logout();
            }
            case  R.id.planilhaMenu:{
                gerarPlanilha();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
