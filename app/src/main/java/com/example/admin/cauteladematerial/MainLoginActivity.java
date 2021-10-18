package com.example.admin.cauteladematerial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class MainLoginActivity extends AppCompatActivity {

    private EditText Email, Password,passwordresetemail;
    private Button Login;
    private TextView passwordreset;
    private CheckBox ckb_mostrar_senha;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        mAuth =  FirebaseAuth.getInstance();
        Email = findViewById(R.id.emailSignIn);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.Login);
        passwordreset = findViewById(R.id.forgotpassword);
        passwordresetemail = findViewById(R.id.emailSignIn);
        ckb_mostrar_senha = findViewById(R.id.ckb_mostrar_senha);
        mAuth = FirebaseAuth.getInstance();


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail = Email.getText().toString();
                String loginSenha = Password.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginSenha)) {
                    mAuth.signInWithEmailAndPassword(loginEmail, loginSenha)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainLoginActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                                        abrirTelaPrincipal();
                                    } else {
                                        String error;
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            error = "Dados informados incorretos\nVerifique e tente novamente";
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            error = "Dados informados incorretos\nVerifique e tente novamente";
                                        } catch (Exception e) {
                                            error = "Erro desconhecido\nTente novamente";
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(MainLoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(MainLoginActivity.this, "E-mail e/ou senha não informado\nVerifique e tente novamente", Toast.LENGTH_SHORT).show();
                }
            }


        });

        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });


        ckb_mostrar_senha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

    public void resetpassword(){
        final String resetemail = passwordresetemail.getText().toString();

        if (resetemail.isEmpty()) {
            passwordresetemail.setError("Vazio");
            passwordresetemail.requestFocus();
            return;
        }
        mAuth.sendPasswordResetEmail(resetemail)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainLoginActivity.this, "E-mail de recuperação enviado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainLoginActivity.this, "E-mail de recuperação enviado", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(MainLoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}