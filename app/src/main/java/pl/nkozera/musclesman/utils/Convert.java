package pl.nkozera.musclesman.utils;

import org.json.JSONException;
import org.json.JSONObject;


public class Convert {

    public static String getResponse(String response) {
        String rsp = null;
        try {
            JSONObject req = new JSONObject(response);
            rsp = req.getString("return");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rsp;
    }

    public static int intRsp(String response) {
        return Integer.parseInt(getResponse(response));
    }

    public static boolean checkUser(String rsp) {
        return Convert.intRsp(rsp) != -1;
    }

}
