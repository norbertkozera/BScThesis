package pl.nkozera.musclesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import pl.nkozera.musclesman.utils.Info;
import pl.nkozera.musclesman.utils.MakeToast;

public class FirstLogin extends AppCompatActivity {

    Info.TYPE typeOfTrening = null;
    RadioButton rbtn1, rbtn2, rbtn3;
    EditText age, experience;
    Button sendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        rbtn1 = (RadioButton) findViewById(R.id.radioButton);
        rbtn2 = (RadioButton) findViewById(R.id.radioButton2);
        rbtn3 = (RadioButton) findViewById(R.id.radioButton3);
        age = (EditText) findViewById(R.id.givenAge);
        experience = (EditText) findViewById(R.id.givenExperience);
        sendInfo = (Button) findViewById(R.id.ready_send);

        rbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTrening = Info.TYPE.STRENGTH;
                rbtn1.setChecked(true);
                rbtn2.setChecked(false);
                rbtn3.setChecked(false);
            }
        });
        rbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTrening = Info.TYPE.MUSCLE;
                rbtn1.setChecked(false);
                rbtn2.setChecked(true);
                rbtn3.setChecked(false);
            }
        });
        rbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTrening = Info.TYPE.CONDITION;
                rbtn1.setChecked(false);
                rbtn2.setChecked(false);
                rbtn3.setChecked(true);
            }
        });


        sendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfoAndSend(typeOfTrening, age.getText().toString(), experience.getText().toString());
            }
        });

    }

    private void saveInfoAndSend(Info.TYPE type, String age, String experience) {

        if (type == null) {
            new MakeToast(MakeToast.ToastTypes.ERROR, getApplicationContext(), "Nie wybrałeś typu treningu!");
        } else if ("".equals(age)) {
            new MakeToast(MakeToast.ToastTypes.ERROR, getApplicationContext(), "Podaj swój wiek!");
        } else if ("".equals(experience)) {
            new MakeToast(MakeToast.ToastTypes.ERROR, getApplicationContext(), "Podaj swoje doświadczenie!");
        } else {

            try {
                int agee = Integer.parseInt(age);
                int exp = Integer.parseInt(experience);

                if (agee <= 18 && (typeOfTrening != Info.TYPE.CONDITION)) {
                    new MakeToast(MakeToast.ToastTypes.ERROR, getApplicationContext(), "Jesteś zbyt młody - wybierz trening kondycyjny!");
                } else if (exp <= 2 && (typeOfTrening == Info.TYPE.STRENGTH)) {
                    new MakeToast(MakeToast.ToastTypes.ERROR, getApplicationContext(), "Masz zbyt małe doświadczenie dla treningu siłowego, wybierz inny!");
                } else {
                    Info.setAge(agee);
                    Info.setExperience(exp);
                    Info.setUserType(type);
                    Intent intent = new Intent(getApplicationContext(), Proposals.class);
                    startActivity(intent);
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


}
