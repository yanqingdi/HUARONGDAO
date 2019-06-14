package com.example.yqdhrd;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

public class ImageButton extends AppCompatButton {
    private int lastX;
    private int lastY; //棋子上一次的位置
    private Chess chess;

    public ImageButton(Context context) {
        super(context);
    }

    public void setChess(Chess chess) {
        this.chess = chess;
    }

    public Chess getChess() {
        return chess;
    }
}
