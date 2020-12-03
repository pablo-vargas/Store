package com.foxdigitaltech.store.ui.account.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.account.model.UserHistory;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryCardAdapter extends RecyclerView.Adapter<HistoryCardAdapter.ViewHolder>{

    List<UserHistory> userHistories;
    Listener listener;

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

    public HistoryCardAdapter(List<UserHistory> userHistories, Listener listener) {
        this.userHistories = userHistories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UserHistory userHistory = userHistories.get(position);
        Picasso.get().load(userHistory.getImage()).into(holder.imageView);
        holder.date.setText(parseFecha(userHistory.getCreatedAt()));

        holder.status.setText("Estado: "+userHistory.getStatus());
        holder.total.setText("Total: Bs "+userHistory.getTotal()+"0");
        holder.code.setText("Código: "+userHistory.getKey());
        holder.quantity.setText(""+userHistory.getQuantity());

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (holder.hiddenView.getVisibility() == View.VISIBLE) {
                holder.hiddenView.setVisibility(View.GONE);
                holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            }
            else {
                TransitionManager.beginDelayedTransition(holder.cardView,new AutoTransition());
                holder.hiddenView.setVisibility(View.VISIBLE);
                holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            }
            }
        });
        holder.showProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showProducts(userHistory.getKey());
            }
        });

    }


    @Override
    public int getItemCount() {
        return userHistories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageButton arrow,showProducts;
        LinearLayout hiddenView;
        CardView cardView;
        ImageView imageView;
        TextView date,total,quantity,code,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.base_cardview);
            arrow = itemView.findViewById(R.id.arrow_button);
            hiddenView = itemView.findViewById(R.id.hidden_view);
            imageView = itemView.findViewById(R.id.icon);

            date= itemView.findViewById(R.id.textViewDate);
            total  = itemView.findViewById(R.id.textViewTotal);
            quantity = itemView.findViewById(R.id.quantityProduct);
            code = itemView.findViewById(R.id.textViewCodigo);
            status = itemView.findViewById(R.id.textViewStatus);
            showProducts = itemView.findViewById(R.id.btnShowProducts);

        }
    }

    public interface Listener{
        void showProducts(String codeOrder);
    }
}
