package br.com.onuse.planegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private MainThread thread;
    private ArrayList<Background> bg;
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
        bg = new ArrayList<>();

        bg.add(new Background(Assets.getBitmapFromMemory("bg0"),-1,0));
        bg.add(new Background(Assets.getBitmapFromMemory("bg1"),-2,(int)(HEIGHT * 0.34f)));
        bg.add(new Background(Assets.getBitmapFromMemory("bg2"),-3,(int)(HEIGHT * 0.5f)));
        bg.add(new Background(Assets.getBitmapFromMemory("bg3"),-4,(int)(HEIGHT * 0.6f)));
        bg.add(new Background(Assets.getBitmapFromMemory("bg4"),-5,HEIGHT-45));



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
        for (Background aBg : bg) {
            //update missile
            aBg.update();
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
            for (Background aBg : bg) {
                aBg.draw(canvas);
            }

            canvas.restoreToCount(savedState);
        }
    }
}
