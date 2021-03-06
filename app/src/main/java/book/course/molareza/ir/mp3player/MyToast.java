package book.course.molareza.ir.mp3player;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MyToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MyToast(Context context, CharSequence text, int duration) {
        super(context);


        View view = G.inflater.inflate(R.layout.toast,null);

        TextView txtToast = (TextView) view.findViewById(R.id.txtTextToast);
        txtToast.setText(text);

        this.setView(view);
        this.setDuration(duration);
        this.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM , 0 , 50);

    }

    public static Toast makeText(Context context , CharSequence text , int duration){

        return new MyToast(context,text , duration);
    }

    @Override
    public void show() {
        super.show();
    }
}
