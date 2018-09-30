package br.com.onuse.planegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

class Background {
    private Bitmap image;
    private int x, y, dx;
    private int largura;

    Background(Bitmap res, int speed, int y){
        image = res;
        dx = speed;
        this.largura = GamePanel.WIDTH;
        this.y = y;
    }

    void update(){
        x+=dx;
        if(x<-largura){
            x=0;
        }
    }

    void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
        if(x<0){
            canvas.drawBitmap(image, x+largura, y, null);
        }
    }
}
