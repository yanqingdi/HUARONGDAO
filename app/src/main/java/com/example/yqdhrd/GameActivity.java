package com.example.yqdhrd;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class GameActivity extends MusicalActivity {

    private static final String TAG = "GameActivity";
    static double windowWidth, windowHeight;
    static int chessWidth, chessHeight;

    private static ArrayList<ArrayList<Integer>> chessPosList; //
    private ArrayList<ImageButton> chessbtnlist;
    private static Stack<ArrayList<Integer>> stepStack;
    private RelativeLayout chessBoard;
    private TextView timeSpent;
    private TextView stepSpent;

    private int caocao = -1;
    private int level = -1;
    private int time = 0;
    private int step = 0;
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
             {
                time++;
                timeSpent.setText(String.format(getString(R.string.time_check), Integer.toString(time)));
                mHandler.postDelayed(this, 1000);
            }
        }
    };   //创建线程开始计时


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.postDelayed(runnable, 1000);
        setContentView(R.layout.activity_playgame);
        Bundle bundle = this.getIntent().getExtras();
        level = bundle.getInt("Level");
        init();//初始
        initChecker(level);
    }


    private void initChecker(int level) {
        chessBoard.removeAllViews();
        ArrayList<Chess> chessList = (new ChessList(level)).getChessList();//生成棋子队列地图
        chessbtnlist = (new Chesser(this, chessList, chessWidth, chessHeight)).getChessbtnList();//获取所有棋子btn
        for (int i = 0; i < chessbtnlist.size(); i++) {
            if(chessList.get(i).width == 2 && chessList.get(i).height == 2){
                this.caocao = i;
            }
            chessBoard.addView(createbtnView(this, chessbtnlist.get(i)));
        }
    }

    private void init() {
        stepStack = new Stack<ArrayList<Integer>>();
        chessPosList = new ArrayList<ArrayList<Integer>>();
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth = size.x;
        windowHeight = size.y;//获取屏幕宽高
        ConstraintLayout Layout = findViewById(R.id.linear);
        Layout.setBackground(getResources().getDrawable(R.drawable.chessboard_bg));
        timeSpent = findViewById(R.id.timetext);
        stepSpent = findViewById(R.id.steptext);
        chessBoard = findViewById(R.id.chessboard);
        chessBoard.setBackground(getResources().getDrawable(R.drawable.game_bg));
        LinearLayout.LayoutParams chessBoardParams = new LinearLayout.LayoutParams((int) (windowWidth * 0.8), (int) (windowHeight * 0.6));
        chessBoardParams.setMargins((int) (windowWidth * 0.05), (int) (windowHeight * 0.05), (int) (windowWidth * 0.05), 0);
        chessBoard.setLayoutParams(chessBoardParams);
        chessWidth = (int) (windowWidth * 0.8 / 4);
        chessHeight = (int) (windowHeight * 0.6 / 5);
    }

    private ArrayList<Integer> getChessPosInfo(ArrayList<Integer> chessPos, Chess chess, int id) {
        chessPos.add(id);
        chessPos.add(chess.pos_x);
        chessPos.add(chess.pos_y);
        chessPos.add(chess.pos_x + chess.width);
        chessPos.add(chess.pos_y + chess.height);
        return chessPos;
    }

    public void reinit(View view) {
        stepStack = new Stack<ArrayList<Integer>>();
        step = -1;
        addStep();
        time = 0;
        for (int i = 0; i < 10; i++) {
            int id = chessbtnlist.get(i).getId();
            ImageButton ImageButton = (ImageButton) findViewById(id);
            Chess chess = chessbtnlist.get(i).getChess();
            ImageButton.layout((int) (chess.pos_x * chessWidth), (int) (chess.pos_y * chessHeight), (int) ((chess.pos_x + chess.width) * chessWidth), (int) ((chess.pos_y + chess.height) * chessHeight));
            ArrayList<Integer> chessPos = new ArrayList<Integer>();
            chessPosList.set(i, getChessPosInfo(chessPos, chess, id));
        }
    }

    public ImageButton createbtnView(Context context, ImageButton chessbtn) {
        Chess chess = chessbtn.getChess();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(((int) (chess.width * chessWidth)),
                ((int) (chess.height * chessHeight)));
        params.setMargins(((int) (chess.pos_x * chessWidth)), ((int) (chess.pos_y * chessHeight)), 0, 0);
        chessbtn.setLayoutParams(params);
        ArrayList<Integer> chessPos = new ArrayList<Integer>();
        chessPosList.add(getChessPosInfo(chessPos, chess, chessbtn.getId()));
        return chessbtn;
    }  //生成棋子视图

    public void rePos(int chessIndex, int move1, int move2,int dir) {
        ArrayList<Integer> stepList = new ArrayList<Integer>();
        ArrayList<Integer> posList = chessPosList.get(chessIndex);
        stepList.add(chessIndex);
        stepList.add(posList.get(1));
        stepList.add(posList.get(2));
        stepList.add(posList.get(3));
        stepList.add(posList.get(4));
        if (dir == 0)
        {
            stepList.add(move1);
            stepList.add(posList.get(2));
            stepList.add(move2);
            stepList.add(posList.get(4));
            stepStack.add(stepList);
            chessPosList.get(chessIndex).set(1, move1);
            chessPosList.get(chessIndex).set(3, move2);
        }
        else if(dir == 1)
        {
            stepList.add(move1);
            stepList.add(posList.get(1));
            stepList.add(move2);
            stepList.add(posList.get(3));
            stepStack.add(stepList);
            chessPosList.get(chessIndex).set(2, move1);
            chessPosList.get(chessIndex).set(4, move2);
        }

    }

    public ArrayList<Integer> ifmoveX(int dir[], int moveX, int id) {
        int left = dir[0]+moveX;
        int top = dir[1];
        int right = dir[2]+moveX;
        int down = dir[3];
        if(Math.abs(moveX)>chessWidth){
            return null;
        }
        if (left < -1 || top < -1 || right > 4 * chessWidth || down > 5 * chessHeight) {
            return null;
        }
        int chessid = -1;
        if (moveX > 0) {
            left = left % chessWidth == 0 ? left / chessWidth : left / chessWidth + 1;
            right = right % chessWidth == 0 ? right / chessWidth : right / chessWidth + 1;
            top = top / chessHeight;
            down = down / chessHeight;
            for (int i = 0; i < 10; i++) {
                ArrayList<Integer> chess = chessPosList.get(i);
                if (chess.get(0) == id) {
                    chessid = i;
                }
                if (chess.get(0) != id && !(chess.get(2) >= down || chess.get(4) <= top) && left <= chess.get(1) && right > chess.get(1)) {
                    return null;
                }
            }
            if (chessPosList.get(chessid).get(1) != left ) {
                rePos(chessid, left, right,0);
            }
        } else {
            left = left / chessWidth;
            right = right / chessWidth;
            top = top / chessHeight;
            down = down / chessHeight;
            for (int i = 0; i < 10; i++) {
                ArrayList<Integer> chess = chessPosList.get(i);
                if (chess.get(0) == id) {
                    chessid = i;
                }
                if (chess.get(0) != id && !(chess.get(2) >= down || chess.get(4) <= top) && left < chess.get(3) && right >= chess.get(3)) {
                    return null;
                }
            }
            if (chessPosList.get(chessid).get(1) != left ) {
                rePos(chessid, left, right,0);
            }
        }
        checkwin();
        return chessPosList.get(chessid);
    }

    public ArrayList<Integer> ifmoveY(int dir[], int moveY, int id) {
        int left = dir[0];
        int top = dir[1]+moveY;
        int right = dir[2];
        int down = dir[3]+moveY;
        if(Math.abs(moveY)>chessWidth){
            return null;
        }
        if (left < -1 || top < -1 || right > 4 * chessWidth || down > 5 * chessHeight) {
            return null;
        }
        int chessid = -1;
        if (moveY > 0) {
            top = top % chessHeight == 0 ? top / chessHeight : top / chessHeight + 1;
            down = down % chessHeight == 0 ? down / chessHeight : down / chessHeight + 1;
            left = left / chessWidth;
            right = right / chessWidth;
            for (int i = 0; i < 10; i++) {
                ArrayList<Integer> chess = chessPosList.get(i);
                if (chess.get(0) == id) {
                    chessid = i;
                }
                if (chess.get(0) != id && !(chess.get(1) >= right || chess.get(3) <= left) && top <= chess.get(2) && down > chess.get(2)) {
                    return null;
                }
            }
            if (chessPosList.get(chessid).get(2) != top ) {
                rePos(chessid, top, down,1);
            }
        } else {
            top = top / chessHeight;
            down = down / chessHeight;
            left = left / chessWidth;
            right = right / chessWidth;
            for (int i = 0; i < 10; i++) {
                ArrayList<Integer> Chess = chessPosList.get(i);
                if (Chess.get(0) == id) {
                    chessid = i;
                }
                if (Chess.get(0) != id && !(Chess.get(1) >= right || Chess.get(3) <= left) && top < Chess.get(4) && down >= Chess.get(4)) {
                    return null;
                }
            }
            if (chessPosList.get(chessid).get(2) != top ) {
                rePos(chessid, top, down,1);
            }
        }
        checkwin();
        return chessPosList.get(chessid);
    }

    public ArrayList<Integer> ifmoveup(int dir[], int id, int direction, int moveX, int moveY) {
        int left = dir[0];
        int top = dir[1];
        int right = dir[2];
        int down = dir[3];

        if (left < -1 || top < -1 || right > 4 * chessWidth || down > 5 * chessHeight) {
            return null;
        }
        int chessid = -1;
        if (direction == 0) {
            if (moveX > 0) {
                left = left % chessWidth == 0 ? left / chessWidth : left / chessWidth + 1;
                right = right % chessWidth == 0 ? right / chessWidth : right / chessWidth + 1;
                top = top / chessHeight;
                down = down / chessHeight;
                for (int i = 0; i < 10; i++) {
                    ArrayList<Integer> chess = chessPosList.get(i);
                    if (chess.get(0) == id) {
                        chessid = i;
                    }
                    if (chess.get(0) != id && !(chess.get(2) >= down || chess.get(4) <= top) && left <= chess.get(1) && right > chess.get(1)) {
                        return null;
                    }
                }
                if (chessPosList.get(chessid).get(1) != left ) {
                    rePos(chessid, left, right,0);
                }
            } else {
                left = left / chessWidth;
                right = right / chessWidth;
                top = top / chessHeight;
                down = down / chessHeight;
                for (int i = 0; i < 10; i++) {
                    ArrayList<Integer> chess = chessPosList.get(i);
                    if (chess.get(0) == id) {
                        chessid = i;
                    }
                    if (chess.get(0) != id && !(chess.get(2) >= down || chess.get(4) <= top) && left < chess.get(3) && right >= chess.get(3)) {
                        return null;
                    }
                }
                if (chessPosList.get(chessid).get(1) != left ) {
                    rePos(chessid, left, right,0);
                }
            }
        } else {
            if (moveY > 0) {
                top = top % chessHeight == 0 ? top / chessHeight : top / chessHeight + 1;
                down = down % chessHeight == 0 ? down / chessHeight : down / chessHeight + 1;
                left = left / chessWidth;
                right = right / chessWidth;
                for (int i = 0; i < 10; i++) {
                    ArrayList<Integer> chess = chessPosList.get(i);
                    if (chess.get(0) == id) {
                        chessid = i;
                    }
                    if (chess.get(0) != id && !(chess.get(1) >= right || chess.get(3) <= left) && top <= chess.get(2) && down > chess.get(2)) {
                        return null;
                    }
                }
                if (chessPosList.get(chessid).get(2) != top) {
                    rePos(chessid, top, down,1);
                }
            } else {
                top = top / chessHeight;
                down = down / chessHeight;
                left = left / chessWidth;
                right = right / chessWidth;
                for (int i = 0; i < 10; i++) {
                    ArrayList<Integer> chess = chessPosList.get(i);
                    if (chess.get(0) == id) {
                        chessid = i;
                    }
                    if (chess.get(0) != id && !(chess.get(1) >= right || chess.get(3) <= left) && top < chess.get(4) && down >= chess.get(4)) {
                        return null;
                    }
                }
                if (chessPosList.get(chessid).get(2) != top) {
                    rePos(chessid, top, down,1);
                }
            }
        }
        if(moveX!=0 || moveY!=0)
        {
            this.addStep();
        }

        checkwin();
        return chessPosList.get(chessid);
    }

    private void checkwin() {
        ArrayList<Integer> caocaoPos = chessPosList.get(this.caocao);
        if (caocaoPos.get(1) == 1 && caocaoPos.get(2) == 3) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.success)
                    .setMessage("恭喜通关")
                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();//Exit Activity
                        }
                    })
                    .setNegativeButton("继续游戏", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            level++;
                            init();
                            initChecker(level);
                            reinit(null);
                        }
                    }).create().show();
        }
    }
    public void addStep() {
        step++;
        stepSpent.setText(String.format(getString(R.string.step_check), step));
    }
    public void deleteStep() {
        step--;
        stepSpent.setText(String.format(getString(R.string.step_check), step));
    }
    public void backStep(View view) {
        if (stepStack.empty()) {
            Toast.makeText(GameActivity.this, "已经回到最初", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            ArrayList<Integer> backStep = stepStack.pop();
            int chessIndex = backStep.get(0);
            ImageButton chess = chessbtnlist.get(chessIndex);
            chess.layout(backStep.get(1) * chessWidth, backStep.get(2) * chessHeight, backStep.get(3) * chessWidth, backStep.get(4) * chessHeight);
            chessPosList.get(chessIndex).set(1, backStep.get(1));
            chessPosList.get(chessIndex).set(2, backStep.get(2));
            chessPosList.get(chessIndex).set(3, backStep.get(3));
            chessPosList.get(chessIndex).set(4, backStep.get(4));
            deleteStep();
        }
    }
    public void exitGame(View view) {
        finish();
    }
}
