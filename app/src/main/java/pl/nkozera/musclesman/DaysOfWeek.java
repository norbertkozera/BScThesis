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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.nkozera.musclesman.utils.GetUserUtil;
import pl.nkozera.musclesman.utils.Info;
import pl.nkozera.musclesman.utils.Links;
import pl.nkozera.musclesman.utils.MakeToast;

public class DaysOfWeek extends AppCompatActivity {

    RelativeLayout view;
    TextView day;
    CheckBox typeAndDay;
    Button button;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_of_week);
        view = (RelativeLayout) findViewById(R.id.days);
        requestQueue = Volley.newRequestQueue(this);
        showTypesAndDays();


    }

    private void showTypesAndDays() {

        String[] days = {"Poniedziałek",
                "Wtorek",
                "Środa",
                "Czwartek",
                "Piątek",
                "Sobota",
                "Niedziela"};

        final HashMap<String, Integer> daysInWeek = new HashMap<>();
        daysInWeek.put("Poniedziałek", 1);
        daysInWeek.put("Wtorek", 2);
        daysInWeek.put("Środa", 3);
        daysInWeek.put("Czwartek", 4);
        daysInWeek.put("Piątek", 5);
        daysInWeek.put("Sobota", 6);
        daysInWeek.put("Niedziela", 7);

        int k = 0;

        for (String s : days) {


            day = new TextView(this);
            day.setId(k + 1);
            day.setText(s);

            RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            p1.addRule(RelativeLayout.CENTER_HORIZONTAL);


            if (k == 0)
                p1.addRule(RelativeLayout.BELOW, R.id.dayTitle);
            else {
                p1.addRule(RelativeLayout.BELOW, k);
            }
            k++;
            view.addView(day, p1);
            for (int i = 0; i < Info.getTypesOfExcercises().length; i++) {
                if (Info.getTypesOfExcercises()[i][1] == null)
                    continue;

                typeAndDay = new CheckBox(this);

                typeAndDay.setId(k + 1);
                typeAndDay.setHint(Info.getTypesOfExcercises()[i][0] + "," + daysInWeek.get(s));

                typeAndDay.setText(Info.getTypesOfExcercises()[i][1]);

                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                p.addRule(RelativeLayout.BELOW, k);

                view.addView(typeAndDay, p);
                k++;
            }

        }


        button = new Button(this);

        button.setText(R.string.next);
        view.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int a = 0;
                HashMap<String, String> days = new HashMap<>();
                for (int i = 0; i < view.getChildCount(); i++) {
                    if (view.getChildAt(i) instanceof CheckBox) {
                        CheckBox x = (CheckBox) view.getChildAt(i);
                        if (x.isChecked()) {
                            days.put(a + "", x.getHint().toString());
                            a++;

                        }
                    }
                }

                insertToBase(Info.getUserExcer(), days, GetUserUtil.getUserId(), Info.getUserType(), Info.getAge(), Info.getExperience());

                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
            }
        });


    }

    private void insertToBase(ArrayList<String> userExcer, HashMap<String, String> days, int userId, String userType, int age, int exp) {

        new MakeToast(MakeToast.ToastTypes.SUCCESS, getApplicationContext(), "Zaczynamy przygodę :)");

        for (String s : userExcer)
            insertExcercises(userId, s);

        Set mapSet = (Set) days.entrySet();
        for (Object aMapSet : mapSet) {
            Map.Entry mapEntry = (Map.Entry) aMapSet;
            String value = (String) mapEntry.getValue();
            insertDays(userId, value);
        }

        insertUserData(userId, userType, age, exp);

        startAdventure();


    }

    private void insertUserData(final int userId, final String userType, final int age, final int exp) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Links.INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                parameters.put("table", "APPUSERDATA");
                parameters.put("fields", "USER_ID,AGE,EXPERIENCE,TYPEOFTRENING");
                parameters.put("values", "'" + userId + "', '" + age + "', '" + exp + "'" + ", '" + userType + "'");

                return parameters;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void startAdventure() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Links.PROCEDURE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
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

                parameters.put("pName", "P_ADDFIRSTLOGIN");
                parameters.put("pPamams", GetUserUtil.getUserId() + "");

                return parameters;
            }
        };

        requestQueue.add(request);
    }

    private void insertDays(final int userId, final String value) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Links.INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {//int dayForDatabase = days.get(dayOfweek);

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("table", "DAYOFWEEK_TRENINGTYPE");
                parameters.put("fields", "USERID, TRENINGTYPE, DAYOFWEEK");
                parameters.put("values", "'" + userId + "', '" + value.substring(0, value.indexOf(",")) + "', '" + value.substring(value.indexOf(",") + 1) + "'");

                return parameters;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void insertExcercises(final int userId, final String userExcer) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Links.INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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

                parameters.put("table", "APPUSER_EXCERCISE");
                parameters.put("fields", "USERID, EXCERCISEID");
                parameters.put("values", "'" + userId + "', '" + userExcer + "'");

                return parameters;
            }
        };

        requestQueue.add(stringRequest);

    }
}
