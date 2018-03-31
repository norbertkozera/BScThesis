package pl.nkozera.musclesman;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

import pl.nkozera.musclesman.utils.GetUserUtil;
import pl.nkozera.musclesman.utils.Links;

public class MyTrening extends AppCompatActivity {


    HashMap<Integer, String[][]> excercises = new HashMap<>();
    String[][] allexcer;

    TextView dayofweek, typeofex, nameofex, rep;
    RelativeLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trening);
        view = (RelativeLayout) findViewById(R.id.treview);
        getUserTraining();
    }

    private void getUserTraining() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Links.NESTED_TABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {
                        String[][] excr = new String[1][5];
                        JSONObject result = rspArr.getJSONObject(i);

                        String DAY_OF_WEEK = result.getString("DAY_OF_WEEK");
                        String EXCERCISE_TYPE = result.getString("EXCERCISE_TYPE");
                        String EXCERCISE_NAME = result.getString("EXCERCISE_NAME");
                        String REPEATS = result.getString("REPEAT");
                        String SERIES = result.getString("SERIES");

                        excr[0][0] = DAY_OF_WEEK;
                        excr[0][1] = EXCERCISE_TYPE;
                        excr[0][2] = EXCERCISE_NAME;
                        excr[0][3] = REPEATS;
                        excr[0][4] = SERIES;

                        excercises.put(i, excr);
                    }

                    rememberAll(excercises);


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

                parameters.put("pName", "F_GETTRAINING");
                parameters.put("pPamams", GetUserUtil.getUserId() + "");

                return parameters;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void rememberAll(HashMap<Integer, String[][]> excercises) {

        String[][] excr = new String[excercises.size()][5];

        for (int i = 0; i < excercises.size(); i++) {
            String[][] thisone = excercises.get(i);
            excr[i][0] = thisone[0][0];
            excr[i][1] = thisone[0][1];
            excr[i][2] = thisone[0][2];
            excr[i][3] = thisone[0][3];
            excr[i][4] = thisone[0][4];
        }

        allexcer = excr;

        showUserTraining();

    }


    @SuppressLint("SetTextI18n")
    private void showUserTraining() {

        final HashMap<String, String> daysInWeek = new HashMap<>();
        daysInWeek.put("1", "Poniedziałek");
        daysInWeek.put("2", "Wtorek");
        daysInWeek.put("3", "Środa");
        daysInWeek.put("4", "Czwartek");
        daysInWeek.put("5", "Piątek");
        daysInWeek.put("6", "Sobota");
        daysInWeek.put("7", "Niedziela");


        int daysIds = 10000000;
        for (int i = 0; i < allexcer.length; i++) {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            p.addRule(RelativeLayout.CENTER_HORIZONTAL);
            p.setMargins(0, 50, 0, 0);
            if (i == 0)
                p.addRule(RelativeLayout.BELOW, R.id.title);
            else {
                p.addRule(RelativeLayout.BELOW, daysIds);
            }

            if (
                    i == 0
                            || !allexcer[i][0].equals(allexcer[i - 1][0])
                    ) {
                dayofweek = new TextView(this);
                dayofweek.setText(daysInWeek.get(allexcer[i][0]));
                dayofweek.setId(daysIds + 1);
                daysIds++;
                view.addView(dayofweek, p);


                if (i == 0
                        || !allexcer[i][1].equals(allexcer[i - 1][1])
                        ) {

                    RelativeLayout.LayoutParams t = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    t.addRule(RelativeLayout.ALIGN_LEFT);
                    typeofex = new TextView(this);
                    typeofex.setText(allexcer[i][1]);
                    typeofex.setId(daysIds + 1);


                    t.addRule(RelativeLayout.BELOW, daysIds);
                    daysIds++;
                    view.addView(typeofex, t);
                    if (i == 0
                            || !allexcer[i][2].equals(allexcer[i - 1][2])
                            ) {

                        RelativeLayout.LayoutParams n = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                        n.addRule(RelativeLayout.ALIGN_LEFT);
                        n.setMargins(50, 0, 0, 0);
                        nameofex = new TextView(this);
                        nameofex.setText(allexcer[i][2]);

                        nameofex.setId(daysIds + 1);


                        n.addRule(RelativeLayout.BELOW, daysIds);
                        daysIds++;
                        view.addView(nameofex, n);

                        if (i == 0
                                || !allexcer[i][2].equals(allexcer[i - 1][2])
                                ) {
                            RelativeLayout.LayoutParams rs = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            rs.addRule(RelativeLayout.ALIGN_LEFT);
                            rs.setMargins(70, 0, 0, 0);
                            rep = new TextView(this);
                            rep.setText(getString(R.string.repeats) + allexcer[i][3] + getString(R.string.series) + allexcer[i][4]);

                            rep.setId(daysIds + 1);


                            rs.addRule(RelativeLayout.BELOW, daysIds);
                            daysIds++;
                            view.addView(rep, rs);
                        }

                    }
                }
            }

        }

    }
}
