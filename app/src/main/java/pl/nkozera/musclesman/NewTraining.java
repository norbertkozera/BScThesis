package pl.nkozera.musclesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class NewTraining extends AppCompatActivity implements GestureDetector.OnGestureListener {


    HashMap<String, String> achievements = new HashMap<>();
    TextView titleOfExcercise, howTo, repets, series;
    Button next;
    GestureDetectorCompat gestureDetector;
    int whichOne = 0;
    String[][] allexcer;
    EditText achiev;

    HashMap<Integer, String[][]> excercises = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);
        titleOfExcercise = (TextView) findViewById(R.id.titleOfExcercise);
        howTo = (TextView) findViewById(R.id.howTo);
        repets = (TextView) findViewById(R.id.repeats);
        series = (TextView) findViewById(R.id.series);
        next = (Button) findViewById(R.id.button);
        achiev = (EditText) findViewById(R.id.editText);

        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextPlease();
                    }
                }
        );

        this.gestureDetector = new GestureDetectorCompat(this, this);

        lookForExcercise(GetUserUtil.getUserId(), GetUserUtil.getNearestTreaningDayOfWeek());

    }

    private void lookForExcercise(final int userId, final int nearestTreaningDayOfWeek) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                Links.NESTED_TABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject rspObj = new JSONObject(response);

                    JSONArray rspArr = rspObj.getJSONArray("selection");
                    for (int i = 0; i < rspArr.length(); i++) {
                        String[][] excr = new String[1][4];
                        JSONObject result = rspArr.getJSONObject(i);

                        String EXCERCISE_NAME = result.getString("EXCERCISE_NAME");
                        String EXCERCISE_DESCRYPTION = result.getString("EXCERCISE_DESCR");
                        String REPEATS = result.getString("REPEAT");
                        String SERIES = result.getString("SERIES");

                        excr[0][0] = EXCERCISE_NAME;
                        excr[0][1] = EXCERCISE_DESCRYPTION;
                        excr[0][2] = REPEATS;
                        excr[0][3] = SERIES;

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

                parameters.put("pName", "F_NEARESTTRAINING");
                parameters.put("pPamams", userId + ", " + nearestTreaningDayOfWeek);

                return parameters;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void rememberAll(HashMap<Integer, String[][]> excercises) {

        String[][] excr = new String[excercises.size()][4];

        for (int i = 0; i < excercises.size(); i++) {
            String[][] thisone = excercises.get(i);
            excr[i][0] = thisone[0][0];
            excr[i][1] = thisone[0][1];
            excr[i][2] = thisone[0][2];
            excr[i][3] = thisone[0][3];
        }

        allexcer = excr;

        showExcr();

    }

    private void showExcr() {

        if (whichOne == allexcer.length - 1) {
            saveAchievements();
            titleOfExcercise.setText(R.string.the_end);
            howTo.setText(R.string.achievements_saved);
            repets.setText("");
            series.setText("");

            TextView rep = (TextView) findViewById(R.id.textView);
            TextView ser = (TextView) findViewById(R.id.textView2);
            TextView save = (TextView) findViewById(R.id.textView3);

            rep.setVisibility(View.INVISIBLE);
            ser.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
            achiev.setVisibility(View.INVISIBLE);

            next.setText(R.string.get_back);
            next.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getApplicationContext(), Welcome.class);
                            startActivity(intent);
                        }
                    }
            );
        } else {

            titleOfExcercise.setText(allexcer[whichOne][0]);
            howTo.setText(allexcer[whichOne][1]);
            repets.setText(allexcer[whichOne][2]);
            series.setText(allexcer[whichOne][3]);

            System.out.println();

        }


    }

    private void saveAchievements() {
        for (int i = 0; i < achievements.size(); i++) {
            saveIt(i);
        }


    }

    private void saveIt(final int i) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Links.INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            String excercise = achievements.keySet().toArray()[i].toString();
            String achev = achievements.get(excercise);

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("table", "USER_ACHIEVEMENTS");
                parameters.put("fields", "USERID, EXCERCISE, ACHIEVEMENT, ACHIEW_DATE");
                parameters.put("values", "'" + GetUserUtil.getUserId() + "', " + "(select distinct Excercise_id from excercises where EXCERCISE_NAME like '" + excercise + "')" + ", '" + achev + "', sysdate");

                return parameters;
            }
        };

        requestQueue.add(stringRequest);


    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        nextPlease();
        return true;
    }

    private void nextPlease() {
        whichOne++;
        if (achiev.getText().toString().length() > 0)
            achievements.put(allexcer[whichOne][0], achiev.getText().toString());
        achiev.setText("");
        showExcr();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
