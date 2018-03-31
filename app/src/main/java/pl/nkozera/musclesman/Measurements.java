package pl.nkozera.musclesman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Measurements extends AppCompatActivity {

    HashMap<Integer, String[][]> measurements = new HashMap<>();
    String[][] allMeasurements;
    RelativeLayout view;
    TextView meas;

    Spinner partOfBody;
    EditText measurment;
    Button addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);

        view = (RelativeLayout) findViewById(R.id.measur);
        partOfBody = (Spinner) findViewById(R.id.spinner2);
        measurment = (EditText) findViewById(R.id.editText4);
        addNew = (Button) findViewById(R.id.button4);


        setSpinner();


        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNew();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Links.NESTED_TABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {
                        String[][] excr = new String[1][3];
                        JSONObject result = rspArr.getJSONObject(i);

                        String PART_OF_BODY = result.getString("PART_OF_BODY");
                        String DATE_MEAS = result.getString("DATE_MEAS");
                        String MEAS = result.getString("MEAS");
                        excr[0][0] = PART_OF_BODY;
                        excr[0][1] = DATE_MEAS;
                        excr[0][2] = MEAS;
                        measurements.put(i, excr);
                    }

                    remember(measurements);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("VolleyError ERROR!");
                System.out.println("errMsg: " + error.getMessage());

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("pName", "f_GETMEAS");
                parameters.put("pPamams", GetUserUtil.getUserId() + "");

                return parameters;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void setSpinner() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Links.SELECT_WITH_CONDITIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] types = new String[15];
                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {

                        JSONObject result = rspArr.getJSONObject(i);

                        types[i] = result.getString("PART_NAME");


                    }

                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, types);


                    partOfBody.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("selection", "*");
                parameters.put("table", "PART_OF_BODY");
                parameters.put("condition", "PART_ID > 0");

                return parameters;
            }
        };

        requestQueue.add(request);


    }

    private void saveNew() {

        final long part = partOfBody.getSelectedItemId() + 1;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Links.INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                new MakeToast(MakeToast.ToastTypes.SUCCESS, getApplicationContext(), "Zapisano");
                Intent intent = new Intent(getApplicationContext(), Measurements.class);
                startActivity(intent);
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

                parameters.put("table", "USER_MEASUREMENTS");
                parameters.put("fields", "USERID, PART_OF_BODY, MEASUREMENT, MEAS_DATE");
                parameters.put("values", "'" + GetUserUtil.getUserId() + "', '" + part + "', '" + measurment.getText().toString() + "', sysdate");

                return parameters;
            }
        };

        requestQueue.add(stringRequest);
    }


    private void remember(HashMap<Integer, String[][]> measurements) {

        String[][] excr = new String[measurements.size()][3];

        for (int i = 0; i < measurements.size(); i++) {
            String[][] thisone = measurements.get(i);
            excr[i][0] = thisone[0][0];
            excr[i][1] = thisone[0][1];
            excr[i][2] = thisone[0][2];
        }

        allMeasurements = excr;
        showAchiew();
    }


    @SuppressLint("SetTextI18n")
    private void showAchiew() {


        for (int i = 0; i < allMeasurements.length - 1; i++) {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            p.addRule(RelativeLayout.ALIGN_LEFT);
            p.setMargins(50, 20, 50, 10);

            if (i == 0)
                p.addRule(RelativeLayout.BELOW, R.id.actual);
            else {
                p.addRule(RelativeLayout.BELOW, i);
            }

            meas = new TextView(this);
            meas.setText(allMeasurements[i][1] + " - " + allMeasurements[i][0] + " - " + allMeasurements[i][2]);
            meas.setId(i + 1);
            view.addView(meas, p);
        }
    }


}





