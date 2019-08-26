package ozd.tellme.com.tellme;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Activity {

    FirebaseAuth firebaseAuth;
    private Button BtnLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    TextView reset_password_label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button register = (Button)findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        editTextEmail=(EditText)findViewById(R.id.login);
        editTextPassword=(EditText)findViewById(R.id.password);

        BtnLogin=(Button)findViewById(R.id.btn_login);
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        reset_password_label = (TextView)findViewById(R.id.reset_password_label);
        reset_password_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ResetPassword.class));
            }
        });

    }


    private void login() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        EditText lgn = (EditText)findViewById(R.id.login);
        EditText pass = (EditText)findViewById(R.id.password);
        lgn.setError(null);

        if(pass.getText().toString().equals("") || lgn.getText().toString().equals(""))
        {
            if(pass.getText().toString().equals(""))
            {
                pass.setError(getString(R.string.error_incorrect_password));
            }
            if(lgn.getText().toString().equals(""))
            {
                lgn.setError(getString(R.string.error_incorrect_login));
            }
        }else{
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(Login.this, MySpace.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Login.this, "Login seccesful!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }



    }
}


