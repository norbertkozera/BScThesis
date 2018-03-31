package pl.nkozera.musclesman.utils;

import java.util.ArrayList;

public class Info {


    private static String[][] typesOfExcercises;

    private static int age = 0;
    private static int experience = 0;
    private static int typeOfThrening = 0;
    private static ArrayList<String> userExcer = new ArrayList<>();


    public static String[][] getTypesOfExcercises() {
        return typesOfExcercises;
    }

    public static void setTypesOfExcercises(String[][] typesOfExcercises) {
        Info.typesOfExcercises = typesOfExcercises;
    }

    public static ArrayList<String> getUserExcer() {
        return userExcer;
    }

    public static void setUserExcer(ArrayList<String> userExcer) {
        Info.userExcer = userExcer;
    }

    public enum TYPE {
        STRENGTH, MUSCLE, CONDITION
    }


    public static int getAge() {
        return age;
    }

    public static void setAge(int age) {
        Info.age = age;
    }

    public static int getExperience() {
        return experience;
    }

    public static void setExperience(int egperience) {
        Info.experience = egperience;
    }

    public static String getUserType() {
        return typeOfThrening + "";
    }

    public static void setUserType(TYPE userType) {
        switch (userType) {
            case CONDITION:
                Info.typeOfThrening = 1;
                break;
            case MUSCLE:
                Info.typeOfThrening = 2;
                break;
            case STRENGTH:
                Info.typeOfThrening = 3;
                break;
            default:
                Info.typeOfThrening = 1;
        }
    }


}


