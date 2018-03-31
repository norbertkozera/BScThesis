package pl.nkozera.musclesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.nkozera.musclesman.utils.Convert;
import pl.nkozera.musclesman.utils.GetUserUtil;
import pl.nkozera.musclesman.utils.Links;
import pl.nkozera.musclesman.utils.MakeToast;
import pl.nkozera.musclesman.utils.Md5Generator;


public class Login extends AppCompatActivity {
    EditText inputLogin, inputPass;
    Button buttonLogin, buttonRegister;
    Switch rememberLogin;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLogin = (EditText) findViewById(R.id.inputLogin);
        inputPass = (EditText) findViewById(R.id.inputPassword);
        buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonRegister = (Button) findViewById(R.id.registerButton);
        rememberLogin = (Switch) findViewById(R.id.rememberLogin);
        rememberLogin.setChecked(false);
        requestQueue = Volley.newRequestQueue(this);
        try {
            FileInputStream fIn = openFileInput("user.id");
            InputStreamReader isr = new InputStreamReader(fIn);

            String userId = IOUtils.toString(isr);

            if (userId.equals("not")) {
                System.out.println("uzytkownik nie zapamietany!");
                clickButtons();
            } else {
                System.out.println("uzytkownik zapamietany! id: " + userId);
                autologin(userId);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void autologin(final String userId) {
        StringRequest request = new StringRequest(Request.Method.POST, Links.FUNCTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                checkAutoLogin(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            Random rand = new Random();
            int sessionID = Math.abs(rand.nextInt() / 1000);

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("fName", "F_AUTOLOGIN");
                parameters.put("fPamams", userId + ", " + sessionID);

                return parameters;
            }
        };

        requestQueue.add(request);
    }


    private void checkAutoLogin(String response) {
        if (Convert.checkUser(response)) {
            new GetUserUtil(Convert.intRsp(response), "test");
            new MakeToast(MakeToast.ToastTypes.SUCCESS, this, "Witaj " + GetUserUtil.getUserName() + "!");
            rememberLogin.setChecked(true);
            checkSwitch();
            checkFirstLogin();
        } else {
            clickButtons();
        }
    }

    private void clickButtons() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("klikam");
                register();

            }
        });
    }

    private void register() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }


    private void saveUserId(String id) {
        try {
            FileOutputStream fOut = openFileOutput("user.id",
                    MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(id);

            osw.flush();
            osw.close();
            System.out.println("saved user! " + id);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void login() {


        if (!inputLogin.getText().toString().equals("") && !inputPass.getText().toString().equals("")) {
            Md5Generator md5Generator = new Md5Generator(inputPass.getText().toString());
            final String pass = md5Generator.getMd5Code();
            StringRequest request = new StringRequest(Request.Method.POST, Links.FUNCTION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    responseToUser(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                Random rand = new Random();
                int sessionID = Math.abs(rand.nextInt() / 1000);

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();

                    parameters.put("fName", "F_LOGIN");
                    parameters.put("fPamams", "'" + inputLogin.getText().toString() + "', '" + pass + "', " + sessionID + "");

                    return parameters;
                }
            };

            requestQueue.add(request);
        } else if (inputLogin.getText().toString().equals(""))
            new MakeToast(MakeToast.ToastTypes.ERROR, this, "Wprowadź login!");
        else
            new MakeToast(MakeToast.ToastTypes.ERROR, this, "Wprowadź hasło!");
    }


    private void responseToUser(String response) {
        if (Convert.checkUser(response)) {
            new GetUserUtil(Convert.intRsp(response), inputLogin.getText().toString());
            new MakeToast(MakeToast.ToastTypes.SUCCESS, this, "Witaj " + GetUserUtil.getUserName() + "!");
            checkSwitch();
            checkFirstLogin();
        } else
            new MakeToast(MakeToast.ToastTypes.ERROR, this, "Błędna nazwa użytkownika lub hasło, spróbuj ponownie");

    }

    private void checkFirstLogin() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Links.FIRST_LOGIN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray students = response.getJSONArray("selection");
                    String foundHim = null;
                    String thisUserId = GetUserUtil.getUserId() + "";
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i);

                        String id = student.getString("USER_ID");
                        if (thisUserId.equals(id)) {
                            foundHim = thisUserId;
                            break;
                        }

                    }
                    saveUserId(GetUserUtil.getUserId() + "");
                    if (foundHim == null) {
                        Intent intent = new Intent(Login.this, FirstLogin.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Login.this, Welcome.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());

            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void checkSwitch() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Links.PROCEDURE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                int trueFalse;
                if (rememberLogin.isChecked()) {
                    trueFalse = 1;
                    saveUserId(GetUserUtil.getUserId() + "");
                } else {
                    trueFalse = 0;
                }
                parameters.put("pName", "P_ADDREMEMBERLOGIN");
                parameters.put("pPamams", GetUserUtil.getUserId() + ", " + trueFalse);

                return parameters;
            }
        };

        requestQueue.add(request);
    }

}


