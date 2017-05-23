package demo.com.collapsablenotification;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    private final static String sample_url = "http://codeversed.com/androidifysteve.png";
    private final static int NORMAL = 0x00;
    private final static int BIG_TEXT_STYLE = 0x01;
    private final static int BIG_PICTURE_STYLE = 0x02;
    private final static int INBOX_STYLE = 0x03;
    private final static int CUSTOM_VIEW = 0x04;

    private static NotificationManager mNotificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void setNormalStyle(View view) {
        new CreateNotification(NORMAL).execute();
    }

    public void setBigTextStyle(View view) {
        new CreateNotification(BIG_TEXT_STYLE).execute();
    }

    public void setBigPictureStyle(View view) {
        new CreateNotification(BIG_PICTURE_STYLE).execute();
    }

    public void setInboxStyle(View view) {
        new CreateNotification(INBOX_STYLE).execute();
    }

    public void setCustomView(View view) {
        new CreateNotification(CUSTOM_VIEW).execute();
    }

    /**
     * Notification AsyncTask to create and return the
     * requested notification.
     *
     * @see CreateNotification#CreateNotification(int)
     */
    public class CreateNotification extends AsyncTask<Void, Void, Void> {

        int style = NORMAL;

        /**
         * Main constructor for AsyncTask that accepts the parameters below.
         *
         * @param style {@link #NORMAL}, {@link #BIG_TEXT_STYLE}, {@link #BIG_PICTURE_STYLE}, {@link #INBOX_STYLE}
         * @see #doInBackground
         */
        public CreateNotification(int style) {
            this.style = style;
        }

        /**
         * Creates the notification object.
         *
         * @see #setNormalNotification
         * @see #setBigTextStyleNotification
         * @see #setBigPictureStyleNotification
         * @see #setInboxStyleNotification
         */
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected Void doInBackground(Void... params) {
            Notification noti = new Notification();

            switch (style)
            {
                case NORMAL:
                    noti = setNormalNotification();
                    break;

                case BIG_TEXT_STYLE:
                    noti = setBigTextStyleNotification();
                    break;

                case BIG_PICTURE_STYLE:
                    noti = setBigPictureStyleNotification();
                    break;

                case INBOX_STYLE:
                    noti = setInboxStyleNotification();
                    break;

                case CUSTOM_VIEW:
                    noti = setCustomViewNotification();
                    break;

            }

            noti.defaults |= Notification.DEFAULT_LIGHTS;
            noti.defaults |= Notification.DEFAULT_VIBRATE;
            noti.defaults |= Notification.DEFAULT_SOUND;

            noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

            mNotificationManager.notify(0, noti);

            return null;

        }
    }

    /**
     * Normal Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    private Notification setNormalNotification() {
        Bitmap remote_picture = null;
        try
        {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "One", resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Two", resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Three", resultPendingIntent)
                .setContentTitle("Normal Notification")
                .setContentText("This is an example of a Normal Style.").build();
    }
    /**
     * Big Text Style Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification setBigTextStyleNotification() {
        Bitmap remote_picture = null;
        NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
        notiStyle.setBigContentTitle("Big Text Expanded");
        notiStyle.setSummaryText("Nice big text.");

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        CharSequence bigText = "This is an example of a large string to demo how much " +
                "text you can show in a 'Big Text Style' notification.";
        notiStyle.bigText(bigText);
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "One", resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Two", resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Three", resultPendingIntent)
                .setContentTitle("Big Text Normal")
                .setContentText("This is an example of a Big Text Style.")
                .setStyle(notiStyle).build();
    }

    /**
     * Big Picture Style Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification setBigPictureStyleNotification() {
        Bitmap remote_picture = null;
        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle("Big Picture Expanded");
        notiStyle.setSummaryText("Nice big picture.");

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        notiStyle.bigPicture(remote_picture);
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "One", resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Two", resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Three", resultPendingIntent)
                .setContentTitle("Big Picture Normal")
                .setContentText("This is an example of a Big Picture Style.")
                .setStyle(notiStyle).build();
    }

    /**
     * Inbox Style Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification setInboxStyleNotification() {
        Bitmap remote_picture = null;
        NotificationCompat.InboxStyle notiStyle = new NotificationCompat.InboxStyle();
        notiStyle.setBigContentTitle("Inbox Style Expanded");
        for (int i=0; i < 5; i++) {
            notiStyle.addLine("(" + i + " of 6) Line one here.");
        }
        notiStyle.setSummaryText("+2 more Line Samples");

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(true).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "One", resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Two", resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Three", resultPendingIntent)
                .setContentTitle("Inbox Style Normal")
                .setContentText("This is an example of a Inbox Style.")
                .setStyle(notiStyle).build();
    }

    /**
     * Custom View Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification setCustomViewNotification() {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.custom_notification_layout);
        expandedView.setTextViewText(R.id.text_view, "DL9CAU73403");

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(true).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setContentIntent(resultPendingIntent)
                .setContentText("DL1YE4033")
                .setContentTitle("Your ride is on the Way.").build();

        notification.bigContentView = expandedView;

        return notification;
    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.driver : R.drawable.app_icon;
    }
}
