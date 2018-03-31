package pl.nkozera.musclesman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pl.nkozera.musclesman.utils.Convert;
import pl.nkozera.musclesman.utils.GetUserUtil;
import pl.nkozera.musclesman.utils.Links;
import pl.nkozera.musclesman.utils.MakeToast;

import static pl.nkozera.musclesman.utils.GetUserUtil.getUserName;

public class Welcome extends AppCompatActivity {

    TextView userName, loginCounter, lastDate, dateOfNextTrening;
    Button startNew, logout, myTening, settings, achiev, meas;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        test();
        userName = (TextView) findViewById(R.id.userName);
        userName.setText("Witaj " + getUserName() + "!");
        dateOfNextTrening = (TextView) findViewById(R.id.dateOfNextTrening);

        startNew = (Button) findViewById(R.id.startNew);
        logout = (Button) findViewById(R.id.logout);
        myTening = (Button) findViewById(R.id.button2);
        settings = (Button) findViewById(R.id.settings);
        achiev = (Button) findViewById(R.id.meButton);
        meas = (Button) findViewById(R.id.measurements);


        achiev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Achievements.class);
                startActivity(intent);
            }
        });

        meas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Measurements.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountSettings.class);
                startActivity(intent);
            }
        });

        myTening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyTrening.class);
                startActivity(intent);
            }
        });

        startNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewTraining.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                new MakeToast(MakeToast.ToastTypes.SUCCESS, getApplicationContext(), "Zostałeś wylogowany");
            }
        });
    }

    private void logout() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, Links.PROCEDURE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("pName", "P_LOGOUT");
                parameters.put("pPamams", GetUserUtil.getUserId() + "");


                return parameters;
            }
        };

        requestQueue.add(request);
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("deprecation")
    private void setLastLoginCounterAndDate(String response) {

        loginCounter = (TextView) findViewById(R.id.WrongLoginCounts);
        lastDate = (TextView) findViewById(R.id.LastWrongLogin);

        if (response.equals("0")) {
            loginCounter.setText("");
            lastDate.setText("");
        } else {
            loginCounter.setText(getString(R.string.wrong_login) + response.substring(0, 1));
            loginCounter.setTextColor(this.getResources().getColor(R.color.RED));
            lastDate.setText(getString(R.string.last_wrong_login) + response.substring(3));
            lastDate.setTextColor(this.getResources().getColor(R.color.RED));
        }


    }


    private void test() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Links.SELECT_WITH_CONDITIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rspObj = new JSONObject(response);
                    ArrayList<String> list = new ArrayList<>();

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {

                        JSONObject result = rspArr.getJSONObject(i);

                        list.add(result.getString("DAYOFWEEK"));
                    }
                    daymap(list);

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

                parameters.put("selection", "distinct dayofweek");
                parameters.put("table", "DAYOFWEEK_TRENINGTYPE");
                parameters.put("condition", "USERID = '" + GetUserUtil.getUserId() + "' order by dayofweek");

                return parameters;
            }
        };

        requestQueue.add(request);


    }

    private void daymap(ArrayList<String> list) {
        HashMap<Integer, Integer> day = new HashMap<>();
        day.put(Calendar.SUNDAY, 7);
        day.put(Calendar.MONDAY, 1);
        day.put(Calendar.TUESDAY, 2);
        day.put(Calendar.WEDNESDAY, 3);
        day.put(Calendar.THURSDAY, 4);
        day.put(Calendar.FRIDAY, 5);
        day.put(Calendar.SATURDAY, 6);

        HashMap<Integer, String> dayinPolish = new HashMap<>();
        dayinPolish.put(1, "Poniedziałek");
        dayinPolish.put(2, "Wtorek");
        dayinPolish.put(3, "Środa");
        dayinPolish.put(4, "Czwartek");
        dayinPolish.put(5, "Piątek");
        dayinPolish.put(6, "Sobota");
        dayinPolish.put(7, "Niedziela");

        Calendar c = Calendar.getInstance();
        int today = day.get(c.get(Calendar.DAY_OF_WEEK));
        int nearest = 0;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(today);
        for (int i = 1; i <= day.size(); i++) {

            if (list.contains(day.get(c.get(Calendar.DAY_OF_WEEK)) + "")) {
                nearest = day.get(c.get(Calendar.DAY_OF_WEEK));
                date = dayinPolish.get(nearest) + ", " + sdf.format(c.getTime());
                break;
            } else
                c.add(Calendar.DAY_OF_WEEK, 1);
        }

        dateOfNextTrening.setText(date);

        GetUserUtil.setNearestTreaningDayOfWeek(nearest);

        lastNegLogin();
    }

    private void lastNegLogin() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, Links.FUNCTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setLastLoginCounterAndDate(Convert.getResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("fName", "F_LASTNEGLOGIN");
                parameters.put("fPamams", "" + GetUserUtil.getUserId());

                return parameters;
            }
        };

        requestQueue.add(request);
    }
}

