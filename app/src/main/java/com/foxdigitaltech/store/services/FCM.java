package com.foxdigitaltech.store.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.notify.NotifyActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FCM extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().size() > 0){
            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");
            String resp = remoteMessage.getData().get("code");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                oreoPlus(titulo,detalle,resp);
            }else{
                oreoMinus(titulo,detalle,resp);
            }
        }
    }
    private void oreoPlus(String titulo,String detalle,String code){
        createNotify();
        String id = "mensage";
        NotificationManager manager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(id,"Zinzali",NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }
        builder.setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(notificar(code))
                .setContentTitle(titulo)
                .setContentText(detalle)
                .setContentInfo("Orden de pedido");
        Random random = new Random();
        int idb  = random.nextInt(10);
        assert manager != null;
        manager.notify(idb,builder.build());
    }
    private void oreoMinus(String titulo,String detalle,String code){
        String id = "mensaje";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(titulo)
                .setContentText(detalle)
                .setContentIntent(notificar(code))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Random random = new Random();
        int randomCode = random.nextInt(10);
        assert  manager != null;
        manager.notify(randomCode,builder.build());
    }

    private PendingIntent notificar(String code){
        Intent home =  new Intent(getApplicationContext(), NotifyActivity.class);
        home.putExtra("token",code);
        return PendingIntent.getActivity(this,0,home,0);
    }
    private void createNotify(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Orden de Pedido";
            String desc = "ZinZali";
            int importance  = NotificationManager .IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("message",name,importance);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
