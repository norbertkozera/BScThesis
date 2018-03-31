package pl.nkozera.musclesman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import pl.nkozera.musclesman.utils.Convert;
import pl.nkozera.musclesman.utils.Links;
import pl.nkozera.musclesman.utils.MakeToast;
import pl.nkozera.musclesman.utils.Md5Generator;

public class Register extends AppCompatActivity {

    Button register;
    EditText login, pass, pass2;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (Button) findViewById(R.id.registerButton);
        login = (EditText) findViewById(R.id.inputLogin);
        pass = (EditText) findViewById(R.id.inputPassword);
        pass2 = (EditText) findViewById(R.id.input2Password);
        requestQueue = Volley.newRequestQueue(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegister();
            }
        });
    }


    private void checkRegister() {
        if (pass.getText().toString().equals(pass2.getText().toString())) {
            StringRequest request = new StringRequest(Request.Method.POST, Links.FUNCTION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (Convert.getResponse(response).equals("-1"))
                        registerUser();
                    else
                        new MakeToast(MakeToast.ToastTypes.ERROR, Register.this, "Login " + login.getText().toString() + " jest już zajęty!");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();

                    parameters.put("fName", "F_GETUSERNAME");
                    parameters.put("fPamams", "'" + login.getText().toString() + "'");

                    return parameters;
                }
            };

            requestQueue.add(request);
        } else {
            new MakeToast(MakeToast.ToastTypes.ERROR, this, "Wprowadzone hasła nie są identyczne!");
        }
    }

    private void registerUser() {
        Md5Generator md5Generator = new Md5Generator(pass.getText().toString());
        final String password = md5Generator.getMd5Code();
        StringRequest request = new StringRequest(Request.Method.POST, Links.FUNCTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (Convert.getResponse(response).equals("0")) {
                    new MakeToast(MakeToast.ToastTypes.SUCCESS, Register.this, login.getText().toString() + ", Twoje konto zostało utworzone, możesz się już zalogować!");
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                } else
                    new MakeToast(MakeToast.ToastTypes.ERROR, Register.this, "Coś poszło nie tak - spróbuj ponownie!");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("fName", "F_ADDAPPUSER");
                parameters.put("fPamams", "'" + login.getText().toString() + "', '" + password + "'");

                return parameters;
            }
        };

        requestQueue.add(request);
    }
}
