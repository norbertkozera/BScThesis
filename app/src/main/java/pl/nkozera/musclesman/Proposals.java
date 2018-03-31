package pl.nkozera.musclesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.nkozera.musclesman.utils.Info;
import pl.nkozera.musclesman.utils.Links;

public class Proposals extends AppCompatActivity {

    String[][] excercises = new String[64][5];
    String[][] typesOfExcercises = new String[9][2];
    TextView nameOfType;
    CheckBox rbtn;
    Button btn;
    RelativeLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposals);
        view = (RelativeLayout) findViewById(R.id.proposalView);
        selectExcercises();
    }

    private void showExcercises2() {

        int types = 1000000;
        for (int i = 0; i < typesOfExcercises.length; i++) {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            p.addRule(RelativeLayout.CENTER_HORIZONTAL);

            if (i == 0)
                p.addRule(RelativeLayout.BELOW, R.id.title);
            else {
                p.addRule(RelativeLayout.BELOW, types);
            }

            nameOfType = new TextView(this);
            nameOfType.setText(typesOfExcercises[i][1]);
            nameOfType.setId(types + 1);
            nameOfType.setHint(typesOfExcercises[i][0]);
            types++;
            view.addView(nameOfType, p);

            int j = 0;
            while (j < excercises.length) {
                if (excercises[j][0].equals(typesOfExcercises[i][0])) {
                    RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );

                    rbtn = new CheckBox(this);
                    rbtn.setId(types + 1);
                    rbtn.setHint(typesOfExcercises[i][0] + excercises[j][1]);
                    rbtn.setText(excercises[j][2]);
                    p1.addRule(RelativeLayout.BELOW, types);
                    view.addView(rbtn, p1);
                    types++;

                }
                j++;
            }

        }

        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        p2.addRule(RelativeLayout.BELOW, types);
        btn = new Button(this);
        btn.setText(R.string.next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < view.getChildCount(); i++) {
                    if (view.getChildAt(i) instanceof CheckBox) {
                        CheckBox x = (CheckBox) view.getChildAt(i);
                        if (x.isChecked()) {
                            list.add(x.getHint().toString().substring(1));
                        }
                    }
                }
                Info.setUserExcer(list);
                Intent intent = new Intent(getApplicationContext(), DaysOfWeek.class);
                startActivity(intent);
            }
        });

        view.addView(btn, p2);
    }

    private void showTypesOfExcercises() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest jsonObjectRequest1 = new StringRequest(Request.Method.POST,
                Links.SELECT_WITH_CONDITIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {

                        JSONObject result = rspArr.getJSONObject(i);

                        String TYPE_ID = result.getString("TYPE_OF_EXCERCISE_ID");
                        String TYPE_NAME = result.getString("TYPE_NAME");
                        typesOfExcercises[i][0] = TYPE_ID;
                        typesOfExcercises[i][1] = TYPE_NAME;
                    }

                    Info.setTypesOfExcercises(typesOfExcercises);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showExcercises2();
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
                parameters.put("selection", "*");
                parameters.put("table", "TYPE_OF_EXCERCISE");
                parameters.put("condition", "TYPE_OF_EXCERCISE_ID>0 ORDER BY TYPE_OF_EXCERCISE_ID");

                return parameters;
            }
        };

        requestQueue.add(jsonObjectRequest1);
    }


    private void selectExcercises() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Links.NESTED_TABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {

                        JSONObject result = rspArr.getJSONObject(i);

                        String EXCERCISE_TYPE = result.getString("EXCERCISE_TYPE");
                        String EXCERCISE_ID = result.getString("EXCERCISE_ID");
                        String EXCERCISE_NAME = result.getString("EXCERCISE_NAME");
                        String REPEAT = result.getString("REPEAT");
                        String SERIES = result.getString("SERIES");


                        excercises[i][0] = EXCERCISE_TYPE;
                        excercises[i][1] = EXCERCISE_ID;
                        excercises[i][2] = EXCERCISE_NAME;
                        excercises[i][3] = REPEAT;
                        excercises[i][4] = SERIES;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showTypesOfExcercises();
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
                String exp;
                exp = Info.getExperience() > 5 ? "5" : Info.getExperience() + "";
                parameters.put("pName", "F_showTraining");
                parameters.put("pPamams", exp + ", " + Info.getUserType());

                return parameters;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
