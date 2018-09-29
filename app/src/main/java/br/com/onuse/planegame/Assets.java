package br.com.onuse.planegame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Assets {
    private static HashMap<String, Bitmap> bitmapDb;
    private static HashMap<String, AssetFileDescriptor> soundDb;
    private static HashMap<String, String> messages;
    private Context context;
    private static int width;
    private static int height;
    public Assets(Context context, int width, int height){
        this.width = width;
        this.height = height;
        this.context = context;
        bitmapDb = new HashMap<>();
        soundDb = new HashMap<>();
        messages = new HashMap<>();
        init();
    }

    /**
     * {@link Bitmap} Inicia todos assets e souns do jogo aqui
     */
    public void init(){

        //background //assets e parallax
        for(int i = 0; i <=4; i++){
            bitmapDb.put("bg"+i, getBitmap(context, "background/bg"+i+".png"));
        }
    }

    /**
     * Retorna um {@link Bitmap} como objeto, escalado de acordo com o tamnho da tela
     * @param context O {@link Context} passado pela classe principal
     * @param filePath O caminho do arquivo
     * @return {@link Bitmap}
     */
    public static Bitmap getBitmap(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        // Create a new input stream, and open the path
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            // Assuming it is a bitmap, decode the stream
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {}

        // Escala o bitmat com a tela //se desmarcado claro
        double x = bitmap.getWidth();// * width / 1920;
        double y = bitmap.getHeight();// * height / 1080;

        return Bitmap.createScaledBitmap(bitmap, (int) x, (int) y, true);
    }

    /**
     * Retorna um {@link AssetFileDescriptor} de arquivo de som
     * @param context O {@link Context} passado pela classe principal
     * @param filePath o caminho do arquivo
     * @return A {@link AssetFileDescriptor}
     */
    public static AssetFileDescriptor getSoundDesc(Context context, String filePath){
        try{
            AssetFileDescriptor afd = context.getAssets().openFd(filePath);
            return afd;
        }catch(IOException e){
            return null;
        }
    }

    /**
     * Returns a {@link AssetFileDescriptor} from memory.
     * @param name The name of the {@link AssetFileDescriptor}
     * @return A {@link AssetFileDescriptor}
     */
    public static AssetFileDescriptor getSoundDescFromMemory(String name){
        if (soundDb.containsKey(name)){
            return soundDb.get(name);
        }
        return null;
    }

    /**
     * Returns a {@link Bitmap} from memory.
     * @param name The name of the {@link Bitmap}
     * @return A {@link Bitmap}
     */
    public static Bitmap getBitmapFromMemory(String name){
        if (bitmapDb.containsKey(name)){
            return bitmapDb.get(name);
        }
        return null;
    }
    /**
     * Returns a {@link Bitmap} from memory.
     * @param name The name of the {@link Bitmap}
     * @return A {@link Bitmap} rescaled for a fullscreen backgrounds
     */
    public static Bitmap getBitmapFullScreen(String name){
        if (bitmapDb.containsKey(name)){
            // Scale bitmap to screen height and width
            return Bitmap.createScaledBitmap(bitmapDb.get(name), width, height, true);
        }
        return null;
    }
}
