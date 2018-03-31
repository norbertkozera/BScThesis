package pl.nkozera.musclesman.utils;

public class GetUserUtil {

    private static String userName;
    private static int userId;
    private static int nearestTreaningDayOfWeek;


    public GetUserUtil(final int userId, final String userName) {
        setUserId(userId);
        setUserName(userName);
    }

    public static void setNearestTreaningDayOfWeek(int nearestTreaningDayOfWeek) {
        GetUserUtil.nearestTreaningDayOfWeek = nearestTreaningDayOfWeek;
    }

    public static int getNearestTreaningDayOfWeek() {
        return nearestTreaningDayOfWeek;
    }

    private void setUserName(String userName) {
        GetUserUtil.userName = userName;
    }

    private void setUserId(int id) {
        GetUserUtil.userId = id;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUserName() {
        return userName;
    }


}
