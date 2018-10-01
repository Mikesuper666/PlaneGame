package br.com.onuse.planegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private MainThread thread;
    private ArrayList<Background> bg;
    private ArrayList<Missile> missiles;
    private Player player;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private long smokeStartTime;
    private long missileStartTime;
    Typeface font;
    //aumenta a progressao da dificuldade
    private int progressDenom = 20;
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

        player = new Player(Assets.getBitmapFromMemory("player"),  120, 80, 2);
        missileStartTime = System.nanoTime();


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

        // calcula o limite de altura que a borda pode ter com base na pontuação
        // O limite máximo e mínimo da borda são atualizados, e a borda muda a direção quando
        // min é atendido

        maxBorderHeight = 30+player.getScore()/progressDenom;
        //limite a altura máxima da borda de modo que as bordas só possam ocupar um total de 1/2 da tela
        if(maxBorderHeight > HEIGHT/4)maxBorderHeight = HEIGHT/4;
        minBorderHeight = 5+player.getScore()/progressDenom;

        //add missiles on timer
        long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
        if(missileElapsed >(2000 - player.getScore()/4)) {
            //o primeiro missil sempre será no meio
            if (missiles.size() == 0) {
                missiles.add(new Missile(Assets.getBitmapFromMemory("enemy"), WIDTH + 10, HEIGHT / 2, 130, 80, player.getScore(), 3));
            } else {
                missiles.add(new Missile(Assets.getBitmapFromMemory("enemy"),
                        WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - (maxBorderHeight * 2)) + maxBorderHeight), 130, 80, player.getScore(), 3));
            }
            //reset timer
            missileStartTime = System.nanoTime();
        }
        //loop through every missile and check collision and remove
        for(int i = 0; i<missiles.size();i++)
        {
            //update missile
            missiles.get(i).update();

            if(collision(missiles.get(i),player))
            {
                missiles.remove(i);
                player.setPlaying(false);
                break;
            }
            //remove missile if it is way off the screen
            if(missiles.get(i).getX()<-100)
            {
                missiles.remove(i);
                break;
            }
        }
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

    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }
}
