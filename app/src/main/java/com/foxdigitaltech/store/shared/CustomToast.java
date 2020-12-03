package com.foxdigitaltech.store.shared;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foxdigitaltech.store.R;

public class CustomToast {

    private Toast toast;

    /**
     *
      * @param context context
     * @param icon 'success'|
     *             'error'|
     *             'warning'
     * @param message
     * @return
     */
    public Toast custom(Context context,String icon,String message){
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast,null);
        LinearLayout linearLayout = view.findViewById(R.id.lytLayout);
        ImageView imageView = view.findViewById(R.id.imgIcon);
        TextView textView = view.findViewById(R.id.txtMessage);
        switch (icon){
            case "success":
                linearLayout.setBackgroundResource(R.drawable.custom_toast_success);
                imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
                break;
            case "error":
                linearLayout.setBackgroundResource(R.drawable.custom_toast_danger);
                imageView.setImageResource(R.drawable.ic_baseline_error_outline_24);
                break;
            case "warning":
                linearLayout.setBackgroundResource(R.drawable.custom_toast_warning);
                imageView.setImageResource(R.drawable.ic_baseline_warning_24);
                break;
        }
        textView.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    /**
     *
     * @param gravity
     *
     */
    public Toast setGravity(int gravity,int x, int y){
        //rigth = 5, left 3, top 48,bottom 80,center 17,center horizontal 1,center vertical 16;
        toast.setGravity(gravity, x, y);
        return toast;
    }
    public void show(){
        this.toast.show();
    }

}
