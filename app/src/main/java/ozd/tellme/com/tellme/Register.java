package ozd.tellme.com.tellme;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.FeatureGroupInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.basgeekball.awesomevalidation.AwesomeValidation;

import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends Activity {

    EditText edit_name , edit_email, edit_password,confirm_edit_password,phone_number;
    Button regis;

    AwesomeValidation awesomeValidation;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        Button register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TesterAvantEvoyer();
            }
        });
    }

    void TesterAvantEvoyer()
    {
        edit_name = (EditText) findViewById(R.id.name);
        edit_email = (EditText) findViewById(R.id.email);
        edit_password = (EditText) findViewById(R.id.pass);
        confirm_edit_password = (EditText)findViewById(R.id.confirm_pass);
        phone_number = (EditText)findViewById(R.id.phone);
        awesomeValidation.addValidation(Register.this,R.id.name,"[a-zA-Z\\s]+", R.string.err_name);
        awesomeValidation.addValidation(Register.this,R.id.email,android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        awesomeValidation.addValidation(Register.this,R.id.phone, RegexTemplate.TELEPHONE, R.string.err_phone);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(Register.this, R.id.pass, regexPassword, R.string.err_password);
        awesomeValidation.addValidation(Register.this, R.id.confirm_pass, R.id.pass, R.string.err_confirm_password);

        if(awesomeValidation.validate())
        {
            registeruser();
            edit_name.setText("");
            edit_email.setText("");
            edit_password.setText("");
            confirm_edit_password.setText("");
            phone_number.setText("");
        }
        else {
            Toast.makeText(Register.this,"Error",Toast.LENGTH_LONG).show();
        }

    }

    public void registeruser()
    {
        String username = edit_name.getText().toString().trim();
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();
        String phone = phone_number.getText().toString().trim();

        sendUserData(username,email,phone,password);

        firebaseAuth.createUserWithEmailAndPassword (email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { }
        });
        Toast.makeText(Register.this,"Register Success!",Toast.LENGTH_LONG).show();
        //startActivity(new Intent(Register.this,Login.class));
        this.finish();
    }

    public void sendUserData(String name,String email,String phone,String pass)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserInfo userInfo = new UserInfo(name,email,phone,pass);
        databaseReference.setValue(userInfo);
    }
}
