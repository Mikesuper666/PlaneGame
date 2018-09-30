package br.com.onuse.planegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends GameObject {
    private Bitmap spriteSheet;
    private int score;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;

    public Player(Bitmap res, int w, int h, int numberFrames){
        x = 100;
        y = GamePanel.HEIGHT/2;
        dy = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numberFrames];
        spriteSheet = res;

        for(int i = 0; i < image.length; i++){
            image[i] = Bitmap.createBitmap(spriteSheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100);
        startTime = System.nanoTime();
    }

    public void setUp(boolean b){up=b;}

    public void update(){
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100){
            score++;
            startTime = System.nanoTime();
        }
        animation.update();
/*
        if(up){
            dy -=1;
        }else{
            dy +=1;
        }
*/
        if(dy>14)dy = 14;
        if(dy<-14)dy = -14;

        y+= dy*2;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(), x, y,null);
    }

    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDY(){dy = 0;}
    public void resetScore(){score = 0;}
}
