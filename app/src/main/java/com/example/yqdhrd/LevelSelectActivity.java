package com.example.yqdhrd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LevelSelectActivity extends MusicalActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);
    }

    public void chooseLevel(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        int level = 0;
        switch (view.getId()) {
            case R.id.button1: level = 1;break;
            case R.id.button2: level = 2;break;
            case R.id.button3: level = 3;break;
            case R.id.button4: level = 4;break;
            case R.id.button5: level = 5;break;
            case R.id.button6: level = 6;break;
            case R.id.button7: level = 7;break;
            case R.id.button8: level = 8;break;
            case R.id.button9: level = 9;break;
            case R.id.button10: level = 10;break;
            case R.id.button11: level= 11;break;
            case R.id.button12: level = 12;break;
        }
        intent.putExtra("Level", level);
        startActivity(intent);
    }
}
