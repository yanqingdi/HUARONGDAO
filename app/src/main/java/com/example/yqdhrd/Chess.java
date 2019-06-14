package com.example.yqdhrd;

public class Chess {
    int pos_x;
    int pos_y;
    int width;
    int height;
    int type;
    // 4个兵 1个马超 1个黄忠 1个赵云 1个张飞 1个关羽 1个曹操

    Chess(int x, int y, int w, int h, int[] typeArray) {
        this.pos_x = x;
        this.pos_y = y;
        this.width = w;
        this.height = h;
        if (width == 1 && height == 1 && typeArray[0] < 4) {
            this.type = 1; // soldier
            typeArray[0]++;
        }
        else if(width == 1 && height == 2) {
            if(typeArray[1] == 0) {
                typeArray[1] = 1;
                this.type = 2; // machao
            }
            else if(typeArray[2] == 0) {
                typeArray[2] = 1;
                this.type = 3; // huangzhong
            }
            else if(typeArray[3] == 0) {
                typeArray[3] = 1;
                this.type = 4; // zhaoyun
            }
            else if(typeArray[4] == 0) {
                typeArray[4] = 1;
                this.type = 5; // zhangfei
            }
        }
        else if(width == 2 && height == 1) {
            if(typeArray[5] == 0) {
                typeArray[5] = 1;
                this.type = 6; // guanyu
            }
            else if(typeArray[5] == 1) {
                typeArray[5] = 2;
                this.type = 8; // zhangfei
            }
            else if(typeArray[5] == 2) {
                typeArray[5] = 3;
                this.type = 9; // machao
            }
        }
        else if(width == 2 && height == 2 && typeArray[6] == 0) {
            typeArray[6] = 1;
            this.type = 7; // caocao
        }
    }
}
