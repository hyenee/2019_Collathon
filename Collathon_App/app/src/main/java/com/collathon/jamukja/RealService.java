package com.collathon.jamukja;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.collathon.janolja.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RealService extends Service {
    private static final String TAG = "BG Service";
    private Thread mainThread;
    public String reservation_id = null;
    public String shop_id = null;
    public String client_id = null;
    public String owner_id = null;
    public String get_client = null;
    public String get_owner = null;
    public String check_user = "Y";

    public RealService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i(TAG, "Start Service");

        SharedPreferences check = getSharedPreferences("checkUser", Activity.MODE_PRIVATE);
        check_user = check.getString("check","Y");
        if(check_user.equals("Y")){
            SharedPreferences user = getSharedPreferences("user", Activity.MODE_PRIVATE);
            get_client = user.getString("userID", "null");
        }else if(check_user.equals("N")){
            SharedPreferences owner = getSharedPreferences("owner", Activity.MODE_PRIVATE);
            get_owner = owner.getString("ownerID", "null");
        }

        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean run = true;
                while (run) {
                    try {
                        Thread.sleep(1000 * 60 * 1); // 1 minute
                        String site = NetworkManager.url + "/reservation/getDelete";
                        Log.i(TAG, site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if(connection != null){
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                Log.i(TAG, "서버 연결됨");
                                InputStream is = connection.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                                BufferedReader br = new BufferedReader(isr);
                                String str = null;
                                StringBuffer buf = new StringBuffer();

                                do{
                                    str = br.readLine();
                                    if(str!=null){
                                        buf.append(str);
                                    }
                                }while(str!=null);
                                br.close(); // 스트림 해제

                                String rec_data = buf.toString();
                                Log.i(TAG, "서버: "+rec_data);

                                if(!rec_data.equals("null")){
                                    JSONArray jsonArray = new JSONArray(rec_data);
                                    for(int i=0; i<jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        reservation_id = jsonObject.getString("id");
                                        shop_id = jsonObject.getString("shop_id");
                                        client_id = jsonObject.getString("user");
                                        owner_id = jsonObject.getString("owner");
                                        Log.i(TAG, "추출 결과 :  "+  reservation_id +", "+ shop_id+", "+ client_id+", "+owner_id);
                                    }
                                    if(check_user.equals("Y")) {
                                        if (client_id.equals(get_client)) {
                                            String send = "[고객 알림]: ID " + client_id + "님의 [예약번호 " + reservation_id + "] 예약이 취소되었습니다.";
                                            sendNotification(send);
//                                        SharedPreferences re = getSharedPreferences("auto",Activity.MODE_PRIVATE);
//                                        SharedPreferences.Editor reset = re.edit();
//                                        reset.clear();
//                                        reset.commit();
                                        }
                                    }else if(check_user.equals("N")){
                                        if (owner_id.equals(get_owner)){
                                            String send = "[자영업자 알림]: ID " + client_id + "님의 [예약번호 " + reservation_id + "] 예약이 취소되었습니다.";
                                            sendNotification(send);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException | IOException | JSONException e) {
                        run = false;
                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                        .setContentTitle("예약취소")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}