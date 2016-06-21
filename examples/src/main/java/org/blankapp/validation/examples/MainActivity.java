package org.blankapp.validation.examples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.blankapp.validation.Rule;
import org.blankapp.validation.ValidationError;
import org.blankapp.validation.ValidationListener;
import org.blankapp.validation.Validator;
import org.blankapp.validation.handlers.DefaultHandler;
import org.blankapp.validation.validators.DateValidator;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEdtEmail;
    private EditText mEdtUsername;
    private EditText mEdtName;
    private EditText mEdtPassword;
    private EditText mEdtBirthday;
    private EditText mEdtAge;
    private EditText mEdtBio;
    private CheckBox mCbAccepted;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mEdtEmail = (EditText) findViewById(R.id.edt_email);
        this.mEdtUsername = (EditText) findViewById(R.id.edt_username);
        this.mEdtName = (EditText) findViewById(R.id.edt_name);
        this.mEdtPassword = (EditText) findViewById(R.id.edt_password);
        this.mEdtBirthday = (EditText) findViewById(R.id.edt_birthday);
        this.mEdtAge = (EditText) findViewById(R.id.edt_age);
        this.mEdtBio = (EditText) findViewById(R.id.edt_bio);
        this.mCbAccepted = (CheckBox) findViewById(R.id.cb_accepted);
        this.mBtnSubmit = (Button) findViewById(R.id.btn_submit);

        final Validator validator = new Validator();

        validator.add(Rule.with(mEdtEmail, "Email").required().email());
        validator.add(Rule.with(mEdtUsername, "Username").required().alphaDash());
        validator.add(Rule.with(mEdtName, "Name").required().minLength(2).maxLength(32));
        validator.add(Rule.with(mEdtPassword, "Password").required().minLength(6).maxLength(32));
        validator.add(Rule.with(mEdtBirthday, "Birthday").required().date("yyyy-MM-dd").before(DateValidator.TODAY));
        validator.add(Rule.with(mEdtAge, "Age").required().integer());
        validator.add(Rule.with(mEdtBio, "Bio").required().maxLength(255));
        validator.add(Rule.with(mCbAccepted, "Accepted").accepted());

        validator.setErrorHandler(new DefaultHandler());

        validator.setValidatorListener(new ValidationListener() {
            @Override
            public void onValid() {
                Toast.makeText(MainActivity.this, "验证通过。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInValid(List<ValidationError> errors) {
                StringBuilder sb = new StringBuilder();
                for (ValidationError error : errors) {
                    Log.w("MainActivity", "Name:" + error.name());
                    for (String key : error.errorMessages().keySet()) {
                        Log.e("MainActivity", error.errorMessages().get(key));
                        sb.append(error.errorMessages().get(key)).append("\n");
                    }
                }
                Toast.makeText(MainActivity.this, "验证失败。\n" + sb.toString(), Toast.LENGTH_LONG).show();
            }
        });

        new DateValidator(DateValidator.THIS_SUNDAY, "yyyy-MM-dd", DateValidator.PATTERN_EQUAL);
        new DateValidator(DateValidator.LAST_SUNDAY, "yyyy-MM-dd", DateValidator.PATTERN_EQUAL);
        new DateValidator(DateValidator.NEXT_SUNDAY, "yyyy-MM-dd", DateValidator.PATTERN_EQUAL);

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }
}
