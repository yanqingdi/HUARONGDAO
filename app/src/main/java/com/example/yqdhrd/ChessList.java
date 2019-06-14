package com.example.yqdhrd;
import java.util.ArrayList;

public class ChessList {
    private String TAG = "ChessList";
    private int[] typeArray = {0, 0, 0, 0, 0, 0, 0};
    public static Object [][]Maps;
    private ArrayList<Chess> chessList;

    ChessList(int level) {
        chessList = new ArrayList<Chess>(10);
        initChessList(level);
    }

    private void initChessList(int level) {
        if (level <= 12){
            Maps = Consts.Map[level-1];
            for (int i = 0; i< Maps.length;i++)
            {
                chessList.add(new Chess((int)Maps[i][0], (int)Maps[i][1], (int)Maps[i][2], (int)Maps[i][3], typeArray));
            }
        }
    }


    public ArrayList<Chess> getChessList() {
        return this.chessList;
    }
}
