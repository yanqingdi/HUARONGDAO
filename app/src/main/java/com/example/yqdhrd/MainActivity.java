package com.example.yqdhrd;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends MusicalActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ConstraintLayout layout = findViewById(R.id.constraint);
        layout.setBackground(getResources().getDrawable(R.drawable.menu));
        Button startGame = findViewById(R.id.start_game);
        startGame.setTextSize(20);
        Button rankList = findViewById(R.id.sound);
        rankList.setTextSize(20);
        Button exitGame = findViewById(R.id.exit_game);
        exitGame.setTextSize(20);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, LevelSelectActivity.class);
        startActivity(intent);
    }

    public void exitGame(View view) {
        finish();
    }

     public void gameIntroduction(View view) {
        Intent intent = new Intent(MainActivity.this, GameIntroduction.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer = null;
    }

    public void sound(View view) {
        if (mPlayer.isPlaying()){
            mPlayer.pause();
            ((TextView)view).setText("播放\n音乐");
        }
        else {
            mPlayer.start();
            ((TextView)view).setText("暂停\n音乐");
        }
    }
}
