package labex.feevale.br.oinc.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.List;

import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.utils.ArchiveUtility;

/**
 * Created by 0126128 on 27/03/2015.
 */
public class CreateSequence {

    private static Bitmap[] sequence = {null, null, null, null};
    private static Bitmap aux, auxCanvas, auxBorder;

    public static Boolean generateSequence(Entry entry, List<String> comments, Activity activity, String name){
        BitmapFactory.Options options = new BitmapFactory.Options();

        int itemsSize = entry.getImagesPath().size() <= 4 ? entry.getImagesPath().size() : 4;
        int index =  entry.getImagesPath().size();
        int width = 0, height = 0;


        for(int i = 0; i < itemsSize; i++){
            if(!comments.get(i).equals("")){
                aux = BitmapFactory.decodeFile(entry.getImagesPath().get(i), options);
                sequence[i] = addText(comments.get(i));
            }else{
                sequence[i] = BitmapFactory.decodeFile(entry.getImagesPath().get(i), options);
            }
            height += sequence[i].getHeight() + 10;
            invalidateAux();
            invalidateAuxBarAndCanvas();
        }

        width = 768;
        height  += 10 ;


        aux = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas listImages = new Canvas(aux);
        listImages.drawColor(0xffffffff);

        int margin = 10;
        listImages.drawBitmap(sequence[0], 10f, margin, null);

        height = 0;
        for(int i = 1; i < index; i++){
            margin += 10;
            height += sequence[i-1].getHeight();

            listImages.drawBitmap(sequence[i], 10f, height + margin, null);
        }
        entry.setPicturePath(new ArchiveUtility(activity).saveImageToInternalStorage(aux,name));
        invalidateAux();
        invalidateArrayBitmap();
        return true;
    }

    private static void invalidateArrayBitmap() {
        for(int i = 0; i < sequence.length; i++){
            if(sequence[i] != null && sequence[i].isRecycled())
                sequence[i].recycle();
            sequence[i] = null;
        }
        System.gc();
    }

    private static Bitmap addText(String legend) {

        Canvas cvBar, cvPhoto;
        int width, height, barHeight;

        width       = aux.getWidth();
        height      = aux.getHeight();
        barHeight   = height / 6;

        auxBorder     = Bitmap.createBitmap(width, barHeight, Bitmap.Config.ARGB_8888);
        auxCanvas   = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        auxBorder.eraseColor(Color.BLACK);
        cvBar   = new Canvas(auxBorder);
        cvPhoto = new Canvas(auxCanvas);

        TextPaint mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(47);
        StaticLayout mTextLayout = new StaticLayout(legend, mTextPaint, cvBar.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        cvBar.save();
        cvBar.translate(5, 5);
        mTextLayout.draw(cvBar);
        cvBar.restore();

        cvPhoto.drawBitmap(aux, 0f, 0f, null);
        cvPhoto.drawBitmap(auxBorder, 0f, cvPhoto.getHeight() - barHeight, null);
        return auxCanvas;
    }

    private static void invalidateAux(){
        if(aux != null && aux.isRecycled())
            aux.recycle();
        aux = null;
        System.gc();
    }

    private static void invalidateAuxBarAndCanvas(){
        if(auxBorder != null && auxBorder.isRecycled())
            auxBorder.recycle();
        auxBorder = null;
        System.gc();

        if(auxCanvas != null && auxCanvas.isRecycled())
            auxCanvas.recycle();
        auxCanvas = null;
        System.gc();
    }

}
