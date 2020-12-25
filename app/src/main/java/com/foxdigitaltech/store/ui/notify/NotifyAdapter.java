package com.foxdigitaltech.store.ui.notify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foxdigitaltech.store.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder>{
    List<NotificationOrder> list;

    public NotifyAdapter(List<NotificationOrder> list) {
        this.list = list;
    }
    private String meses[]={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};

    public String parseFecha(Long fecha){
        int minuto = 60*1000;
        int hora = 60*60*1000;
        int dia = 60*60*24*1000;
        Long mes = 60*60*24*30*1000L;
        Long date = new Date().getTime();
        Long time = fecha;

        Long tiempo = date-time;
        if(tiempo < minuto){
            return "hace un momento";
        }else if (tiempo <= hora){
            return "hace "+Integer.parseInt((tiempo/minuto)+"")+" minuto(s).";
        }else if ( tiempo <= dia){
            return "hace "+Integer.parseInt((tiempo/hora)+"")+" hora(s).";
        }else if(tiempo <= mes ){
            return "hace "+Integer.parseInt((tiempo/dia)+"")+" dia(s).";
        }
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return meses[calendar.get(Calendar.MONTH)]+" "+calendar.get(Calendar.DAY_OF_MONTH)+", "+calendar.get(Calendar.YEAR);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_notify,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.detail.setText(list.get(position).getDetail());
        holder.date.setText(parseFecha(list.get(position).getDate()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,date,detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            detail = itemView.findViewById(R.id.textViewMessage);
            date = itemView.findViewById(R.id.textViewDate);
        }
    }
}
