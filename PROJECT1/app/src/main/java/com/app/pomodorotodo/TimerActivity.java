package com.app.pomodorotodo;

import android.app.Notification;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import maes.tech.intentanim.CustomIntent;
import static com.app.pomodorotodo.AppNotify.CHANNEL1;

public class TimerActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE =
            "com.app.pomodorotodo.EXTRA_TITLE";
    public static final String EXTRA_ID =
            "com.app.pomodorotodo.EXTRA_ID";

    private NotificationManagerCompat notificationManager;

    public CountDownTimer countDownTimer;
    public TextView countDown;
    public ProgressBar countAnimation;
    public Button btn, del;

    public long timeLeftInMS = 0;
    public boolean timerRunning;
    private static final int countDownTime = 1500000, countUpTime = 300000, minute = 60000; //константы для таймера
    private static final short second = 1000; //константы для таймера

    Uri soundNotify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        notificationManager = NotificationManagerCompat.from(this);

        countDown = findViewById(R.id.textView);
        countAnimation = findViewById(R.id.progressBar);
        btn = findViewById(R.id.button);
        del = findViewById(R.id.dlt_task);

        Intent getData = getIntent();
        if (getData != null){
            countDown.setText(getData.getStringExtra(EXTRA_TITLE));
        }else
            countDown.setText(R.string.timer_description);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1){
                    data.putExtra(EXTRA_ID, id);
                }

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    public void updateTimer() { //метод отвечает за отображение обратного отсчёта в TextView
        int minutes = (int) timeLeftInMS / minute;
        int seconds = (int) timeLeftInMS % minute / second;

        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countDown.setText(timeLeftText);
    }

    public void startStop() { //метод проверяет запущен ли таймер
        if (timerRunning) {
            stopTimer();
        } else {
            startDownTimer();
        }
    }

    public void startDownTimer() { //метод отвечает за таймер рабочего времени
        countDownTimer = new CountDownTimer(countDownTime, second) {
            @Override
            public void onTick(long Time) {
                timeLeftInMS = Time;
                updateTimer();
                countAnimation.setProgress((int) Time / 5);
            }

            @Override
            public void onFinish() {
                sendChill();
                countAnimation.setProgress(0);
                startUpTimer();
            }
        }.start();
        btn.setText("СТОП");
        timerRunning = true;
    }

    public void startUpTimer() { //метод отвечает за таймер отдыха
        countDownTimer = new CountDownTimer(countUpTime, second) {
            @Override
            public void onTick(long TimeUp) {
                timeLeftInMS = TimeUp;
                updateTimer();
                long Z = countUpTime - TimeUp;
                countAnimation.setProgress((int) Z + 1000);
            }

            @Override
            public void onFinish() {
                sendEnd();
                btn.setText("СТАРТ");
                countAnimation.setProgress(countUpTime);
                countDown.setText("Закончили? Тогда приступайте к следующему делу! Если нет, то продолжайте работать.");
                del.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
    }

    public void sendChill() {
        Notification chill = new NotificationCompat.Builder(this, CHANNEL1)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher_round))
                .setSound(soundNotify)
                .setContentTitle("Отдохните")
                .setContentText("Рабочий отрезок подошёл к концу. Передохните 5 минут.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        chill.defaults = chill.DEFAULT_VIBRATE;
        notificationManager.notify(0, chill);
    }

    public void sendEnd() {
        Notification end = new NotificationCompat.Builder(this, CHANNEL1)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher_round))
                .setSound(soundNotify)
                .setContentTitle("Пора потрудиться")
                .setContentText("Отдых подошёл к концу. Приступите к работе.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        end.defaults = end.DEFAULT_VIBRATE;
        notificationManager.notify(0, end);
    }

    public void stopTimer() { //метод останавливает работу таймера
        countDownTimer.cancel();
        btn.setText("СТАРТ");
        timerRunning = false;
        countAnimation.setProgress(countUpTime);
        countDown.setText("25:00");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }
}