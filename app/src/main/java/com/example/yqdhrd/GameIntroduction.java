package com.example.yqdhrd;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

public class GameIntroduction extends MusicalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ConstraintLayout constraintLayout = findViewById(R.id.game_introduction);
        constraintLayout.setBackground(getResources().getDrawable(R.drawable.game_rule_bg));
    }
}
