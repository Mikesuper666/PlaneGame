package br.com.onuse.planegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private MainThread thread;
    private Background[] bg = new Background[4];
    Typeface font;
    public GamePanel(Context context){
        super(context);
        Assets assets = new Assets(getContext(), WIDTH, HEIGHT);
        font = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        for(int i = 0; i < bg.length; i++){
            bg[i] = new Background(Assets.getBitmapFromMemory("bg"+i),-1-i);
        }//adiciona os bgs dinamidamente


        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            }catch(InterruptedException e){e.printStackTrace();}

        }
    }

    public void update(){
        for(int i = 0; i < bg.length; i++){
            bg[i].update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //Reescalamos o canvas para respeitar o tamanho padrão que queremos da nossas imagens
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            for(int i = 0; i < bg.length; i++){
                bg[i].draw(canvas);
            }

            canvas.restoreToCount(savedState);
        }
    }
}
