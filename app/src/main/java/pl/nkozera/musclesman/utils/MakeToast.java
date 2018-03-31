package pl.nkozera.musclesman.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.nkozera.musclesman.R;


public class MakeToast {

    public MakeToast(ToastTypes witchOne, Context context, String text) {

        switch (witchOne) {
            case ERROR:
                errorToast(context, text);
                break;
            case SUCCESS:
                successToast(context, text);
                break;
        }

    }

    @SuppressWarnings("deprecation")
    public void errorToast(Context context, String text) {
        Toast toast;
        View toastView;

        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.RED));
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @SuppressWarnings("deprecation")
    public void successToast(Context context, String text) {
        Toast toast;
        View toastView;

        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toastView = toast.getView();
        TextView txt = (TextView) toastView.findViewById(android.R.id.message);
        txt.setTextColor(context.getResources().getColor(R.color.BLACK));
        toastView.setBackgroundColor(context.getResources().getColor(R.color.GREEN));
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public enum ToastTypes {
        ERROR, SUCCESS
    }

}

