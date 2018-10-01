package br.com.onuse.planegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Missile extends GameObject {
    private int score;
    private int speed;
    private Random random = new Random();
    private Animation animation = new Animation();
    private Bitmap spriteSheet;

    public Missile(Bitmap res, int x, int y, int w, int h, int numberFrames){
        spriteSheet = res;
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        speed = 7 + (int)(random.nextDouble()*score/30);
        if(speed>40)speed=40;

        Bitmap[] image = new Bitmap[numberFrames];

        for(int i = 0; i < image.length; i++){
            image[i] = Bitmap.createBitmap(spriteSheet, 0, i*height, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100-speed);
    }

    public void update(){
        x-=speed;
        animation.update();
    }

    public void draw(Canvas canvas){
        try {
            canvas.drawBitmap(animation.getImage(),x,y,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        //offset para uma melhora na colisão
        return width-10;
    }
}
