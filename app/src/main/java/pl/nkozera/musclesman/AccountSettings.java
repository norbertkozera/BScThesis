package pl.nkozera.musclesman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.nkozera.musclesman.utils.GetUserUtil;
import pl.nkozera.musclesman.utils.Links;
import pl.nkozera.musclesman.utils.MakeToast;
import pl.nkozera.musclesman.utils.Md5Generator;

@SuppressWarnings("ALL")
public class AccountSettings extends AppCompatActivity {

    EditText age, expr, pass, pass2;
    Spinner typeoftrning;
Button saveIt;
    String givenAge="", givenExpr="", givenTypeOfTrning ="";

    HashMap<String, String> treningMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        typeoftrning = (Spinner)findViewById(R.id.spinner);
        saveIt = (Button) findViewById(R.id.button3);


        saveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEverythig();
            }
        });

        getAge();
        getExpr();
        getTypeOfTrening();



        pass = (EditText) findViewById(R.id.editText5);
        pass2 = (EditText) findViewById(R.id.editText6);


    }

    private void saveEverythig() {



        if(!givenAge.equals(age.getText().toString())){
            saveAge(age.getText().toString());
        }

        if(!givenExpr.equals(expr.getText().toString())){
            saveExpr(expr.getText().toString());
        }

        String getType = (typeoftrning.getSelectedItemId()+1)+"";
        if (!givenTypeOfTrning.equals(getType)){
            saveType(getType);
        }

        if (pass.getText().toString().length()>0){
            if(pass2.getText().toString().equals(pass.getText().toString()))
                savePass(pass.getText().toString());
            else
                new MakeToast(MakeToast.ToastTypes.ERROR, getApplicationContext(), "Hasła nie sa identyczne!");
        }

        new MakeToast(MakeToast.ToastTypes.SUCCESS, getApplicationContext(), "Zapisane");

    }


    private void savePass(final String s) {

        Md5Generator md5Generator = new Md5Generator(s);
        final String password = md5Generator.getMd5Code();

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("table", "APPUSER");
                parameters.put("field", "USER_PASSWORD");
                parameters.put("value", "ORA_HASH('"+password+"')");
                parameters.put("condition", "USER_ID = " + GetUserUtil.getUserId());

                return parameters;
            }
        };

        requestQueue.add(request);



    }



    private void saveType(final String s) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("table", "APPUSERDATA");
                parameters.put("field", "TYPEOFTRENING");
                parameters.put("value", s);
                parameters.put("condition", "USER_ID = " + GetUserUtil.getUserId());

                return parameters;
            }
        };

        requestQueue.add(request);



    }

    private void saveAge(final String s) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("table", "APPUSERDATA");
                parameters.put("field", "AGE");
                parameters.put("value", s);
                parameters.put("condition", "USER_ID = " + GetUserUtil.getUserId());

                return parameters;
            }
        };

        requestQueue.add(request);



    }

    private void saveExpr(final String s) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("table", "APPUSERDATA");
                parameters.put("field", "EXPERIENCE");
                parameters.put("value", s);
                parameters.put("condition", "USER_ID = " + GetUserUtil.getUserId());

                return parameters;
            }
        };

        requestQueue.add(request);



    }


    private void setspinner(String givenTypeOfTrning, String[] types) {

        //int selected = Integer.pa

        treningMap.put("1", "Kondycyjny");
        treningMap.put("2", "Siłowy");
        treningMap.put("3", "Obwodowy");



         ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);

        System.out.println("givenTypeOfTrning" + givenTypeOfTrning);

        typeoftrning.setAdapter(adapter);
        typeoftrning.setSelection(Integer.parseInt(String.valueOf(givenTypeOfTrning)) - 1);




    }


    private void getAge() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.SELECT_WITH_CONDITIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {

                        JSONObject result = rspArr.getJSONObject(i);

                        givenAge = result.getString("AGE");


                    }
                } catch (JSONException e) {
                        e.printStackTrace();
                }


                age = (EditText) findViewById(R.id.editText2);
                age.setText(givenAge);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("selection", "AGE");
                parameters.put("table", "APPUSERDATA");
                parameters.put("condition", "USER_ID = " + GetUserUtil.getUserId());

                return parameters;
            }
        };

        requestQueue.add(request);


    }

    private void getExpr() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.SELECT_WITH_CONDITIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println();

                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {

                        JSONObject result = rspArr.getJSONObject(i);

                        givenExpr = result.getString("EXPERIENCE");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                expr = (EditText) findViewById(R.id.editText3);

                expr.setText(givenExpr);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("selection", "EXPERIENCE");
                parameters.put("table", "APPUSERDATA");
                parameters.put("condition", "USER_ID = " + GetUserUtil.getUserId());

                return parameters;
            }
        };

        requestQueue.add(request);


    }

    private void getTypeOfTrening() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.SELECT_WITH_CONDITIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println();
                String[] types = {};
                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {

                        JSONObject result = rspArr.getJSONObject(i);

                        givenTypeOfTrning =  result.getString("TYPEOFTRENING");
                        System.out.println("getTypeOfTrening() -> givenTypeOfTrning: "+givenTypeOfTrning);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getAllTypes(givenTypeOfTrning);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("selection", "TYPEOFTRENING");
                parameters.put("table", "APPUSERDATA");
                parameters.put("condition", "USER_ID = " + GetUserUtil.getUserId());

                return parameters;
            }
        };

        requestQueue.add(request);


    }


    private void getAllTypes(final String givenTypeOfTrning) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.SELECT_WITH_CONDITIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println();
                String[] types = new String[3];
                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {

                        JSONObject result = rspArr.getJSONObject(i);

                        types[i] =  result.getString("TYPE_NAME");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setspinner(givenTypeOfTrning, types);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("selection", "TYPE_NAME");
                parameters.put("table", "TRENING_TYPE");
                parameters.put("condition", "TRENING_TYPE_ID > 0");

                return parameters;
            }
        };

        requestQueue.add(request);


    }
}
