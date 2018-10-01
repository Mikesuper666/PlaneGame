package br.com.onuse.planegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private MainThread thread;
    private ArrayList<Background> bg;
    private ArrayList<Missile> missiles;
    private Player player;
    Typeface font;
    public GamePanel(Context context){
        super(context);
        Assets assets = new Assets(getContext(), WIDTH, HEIGHT);
        font = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
        //adiciona o callback para o surfaceholder e interpleta os eventos
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bg = new ArrayList<>();
        missiles = new ArrayList<>();

        bg.add(new Background(Assets.getBitmapFromMemory("bg0"),-1,0));
        bg.add(new Background(Assets.getBitmapFromMemory("bg1"),-2,(int)(HEIGHT * 0.34f)));
        bg.add(new Background(Assets.getBitmapFromMemory("bg2"),-3,(int)(HEIGHT * 0.5f)));
        bg.add(new Background(Assets.getBitmapFromMemory("bg3"),-4,(int)(HEIGHT * 0.6f)));
        bg.add(new Background(Assets.getBitmapFromMemory("bg4"),-5,HEIGHT-45));

        player = new Player(Assets.getBitmapFromMemory("enemy"),  130, 80, 3);


        thread = new MainThread(getHolder(), this);
        //inicia o looping
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
            //atualiza o background
            aBg.update();
        }
        player.update();

        //o primeiro missil sempre será no meio
        if(missiles.size()==0) {
            missiles.add(new Missile(Assets.getBitmapFromMemory("missile"), WIDTH + 10, HEIGHT / 2, 45, 15, 13));
        }

        missiles.get(0).update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying()) {
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying())
            {
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
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
            player.draw(canvas);

            //desenha os missils
            for (Missile m : missiles) {
                m.draw(canvas);
            }

            canvas.restoreToCount(savedState);
        }
    }
}
