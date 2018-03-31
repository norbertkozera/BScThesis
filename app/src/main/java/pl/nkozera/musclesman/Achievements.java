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

public class Achievements extends AppCompatActivity {

    HashMap<Integer, String[][]> achievements = new HashMap<>();
    String[][] allAchievements;
    RelativeLayout view;
    TextView achiew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        view = (RelativeLayout) findViewById(R.id.achiev);

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

                        String NAME_OF_EXCERCISE = result.getString("NAME_OF_EXCERCISE");
                        String DATE_ACHIEW = result.getString("DATE_ACHIEW");
                        String ACHIEW = result.getString("ACHIEW");

                        excr[0][0] = NAME_OF_EXCERCISE;
                        excr[0][1] = DATE_ACHIEW;
                        excr[0][2] = ACHIEW;


                        achievements.put(i, excr);
                    }

                    remember(achievements);


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

                parameters.put("pName", "F_GETACHIEW");
                parameters.put("pPamams", GetUserUtil.getUserId() + "");

                return parameters;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void remember(HashMap<Integer, String[][]> achievements) {

        String[][] excr = new String[achievements.size()][3];

        for (int i = 0; i < achievements.size(); i++) {
            String[][] thisone = achievements.get(i);
            excr[i][0] = thisone[0][0];
            excr[i][1] = thisone[0][1];
            excr[i][2] = thisone[0][2];
        }

        allAchievements = excr;

        showAchiew();
    }


    @SuppressLint("SetTextI18n")
    private void showAchiew() {


        for (int i = 0; i < allAchievements.length - 1; i++) {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            p.addRule(RelativeLayout.ALIGN_LEFT);
            p.setMargins(50, 20, 50, 10);

            if (i == 0)
                p.addRule(RelativeLayout.BELOW, R.id.title);
            else {
                p.addRule(RelativeLayout.BELOW, i);
            }

            achiew = new TextView(this);
            achiew.setText(allAchievements[i][1] + " - " + allAchievements[i][0] + " - " + allAchievements[i][2]);
            achiew.setId(i + 1);
            view.addView(achiew, p);


        }
    }


}





