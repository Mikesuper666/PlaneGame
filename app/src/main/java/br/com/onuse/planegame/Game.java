package br.com.onuse.planegame;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remover tela titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setar para a tela inteira
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Usado para determinar em que tamanho tudo deve ser dimensionado. A única coisa ruim é que ele exige um nível de API 17+ devido a getRealMetrics ()
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displaymetrics);
        int altura = displaymetrics.heightPixels;
        int largura = displaymetrics.widthPixels;
        //  Criar nova View com largura e altura da tela
        setContentView(new GamePanel(this));
    }
}
