package com.example.yqdhrd;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class Chesser implements View.OnTouchListener {

    private static final String TAG = "Chesser";
    GameActivity context;
    private ArrayList<Chess> chessList; //所有棋子
    private ArrayList<ImageButton> chessbtnList; //所有棋子
    private int chessWidth;
    private int chessHeight;

    Chesser(Context context, ArrayList<Chess> chessList, double chessWidth, double chessHeight) {
        this.context = (GameActivity) context;
        this.chessList = chessList;
        this.chessbtnList = new ArrayList<ImageButton>(10);
        this.chessWidth = (int) chessWidth;
        this.chessHeight = (int) chessHeight;
        init();
    }

    private int mouse_lastX;
    private int mouse_lastY;
    private int firstDownRecordX;
    private int firstDownRecordY;
    public int lastImageX;
    public int lastImageY;
    private int direction = -1;


    public void mouseMove(View v, int x, int y) {
        int moveX = x - (mouse_lastX + firstDownRecordX);
        int moveY = y - (mouse_lastY + firstDownRecordY);
        int dir[] ={v.getLeft(),v.getTop(),v.getRight(),v.getBottom()};
        if (direction == -1) {
            if(Math.abs(moveX) > Math.abs(moveY))
            {
                direction = 0;
            }else {
                direction = 1;
            }
        }
        if (direction == 0) {
            if(context.ifmoveX(dir,moveX,v.getId()) != null) {
                v.layout(dir[0]+moveX, dir[1] , dir[2]+moveX, dir[3] );
            }
        } 
        else {
            if(context.ifmoveY(dir,moveY,v.getId()) != null) {
                v.layout(dir[0], dir[1] +moveY, dir[2], dir[3]+moveY );
            }
        }
        mouse_lastX = (int) v.getX();
        mouse_lastY = (int) v.getY();
    }

    public void mouseUp(View v) {
        int chessUniWidth = v.getRight() - v.getLeft();
        int chessUniHeight = v.getBottom() - v.getTop();
        int moveX = v.getLeft() - lastImageX;
        int moveY = v.getTop() - lastImageY;

        if (direction == 0) {
            int dir[] ={lastImageX + moveX,lastImageY,lastImageX + chessUniWidth + moveX,lastImageY + chessUniHeight};
            if((Math.abs(moveX) % chessWidth) > (chessWidth / 2))
            {
                moveX = ((moveX / chessWidth +(moveX/Math.abs(moveX)))* chessWidth);
            }
            else
            {
                moveX = (moveX / chessWidth) * chessWidth;
            }
            ArrayList<Integer> returnList = context.ifmoveup(dir, v.getId(), direction, moveX, 0);
            if(returnList != null) {
                v.layout(returnList.get(1) * chessWidth, returnList.get(2) * chessHeight, returnList.get(3) * chessWidth, returnList.get(4) * chessHeight);
            }
        } else {
            int dir[] ={lastImageX ,lastImageY+ moveY,lastImageX + chessUniWidth ,lastImageY+ moveY + chessUniHeight};

            if((Math.abs(moveY) % chessHeight) > (chessHeight / 2))
            {
                moveY = ((moveY / chessHeight +(moveY/Math.abs(moveY)))* chessHeight);
            }
            else
            {
                moveY = (moveY / chessHeight) * chessHeight;
            }
            ArrayList<Integer> returnList = context.ifmoveup(dir, v.getId(), direction, 0, moveY);
            if(returnList != null) {
                v.layout(returnList.get(1) * chessWidth, returnList.get(2) * chessHeight, returnList.get(3) * chessWidth, returnList.get(4) * chessHeight);
            }
        }

        direction = -1;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                lastImageX = v.getLeft();
                lastImageY = v.getTop();
                mouse_lastX = (int) v.getX();//图片左上角的位置
                mouse_lastY = (int) v.getY();
                firstDownRecordX = (int) event.getX();//鼠标相对与左上角的位置
                firstDownRecordY = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mouseMove(v, (int)(event.getX() + v.getX()), (int)(event.getY() + v.getY()));
                break;
            }
            case MotionEvent.ACTION_UP: {
                mouseUp(v);
                break;
            }
        }
        return false;
    };


    private void init() {
        for (int i = 0; i < chessList.size(); i++) {
            ImageButton imageButton = createImageButton(context, chessList.get(i));
            imageButton.setOnTouchListener(this);
            chessbtnList.add(imageButton);
        }
    }

    public ImageButton createImageButton(Context context, Chess chess) {
        ImageButton img = new ImageButton(context);
        img.setChess(chess);
        img.setVisibility(View.VISIBLE);
        img.setId(View.generateViewId());
        Drawable[] pictures = {
                context.getResources().getDrawable(R.drawable.zu),
                context.getResources().getDrawable(R.drawable.machao),
                context.getResources().getDrawable(R.drawable.huangzhong),
                context.getResources().getDrawable(R.drawable.zhaoyun),
                context.getResources().getDrawable(R.drawable.zhangfei),
                context.getResources().getDrawable(R.drawable.guanyu),
                context.getResources().getDrawable(R.drawable.caocao),
                context.getResources().getDrawable(R.drawable.zhangfei1),
                context.getResources().getDrawable(R.drawable.zhaoyun1),
        };
        img.setBackground(pictures[chess.type - 1]);
        return img;
    }

    public ArrayList<ImageButton> getChessbtnList() {
        return chessbtnList;
    }

}



