package br.com.onuse.planegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap image;
    private int x, y, dx;
    private int largura;

    public Background(Bitmap res, int speed){
        image = res;
        dx = speed;
        this.largura = GamePanel.WIDTH;
    }

    public void update(){
        x+=dx;
        if(x<-largura){
            x=0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
        if(x<0){
            canvas.drawBitmap(image, x+largura, y, null);
        }
    }
}
