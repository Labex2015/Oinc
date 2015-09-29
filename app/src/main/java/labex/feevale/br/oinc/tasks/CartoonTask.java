package labex.feevale.br.oinc.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Array;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.utils.ArchiveUtility;
import labex.feevale.br.oinc.views.actions.ProcessImageAction;
/**
 * Created by 0126128 on 10/02/2015.
 */
public class CartoonTask extends AsyncTask<Void, String, String> {

    private Activity activity;
    private ProgressDialog progressDialog;
    public static Bitmap bitmap;
    private String imageName;
    private ProcessImageAction action;
    private Resources resources;

    public CartoonTask(Activity activity, String imageName, ProcessImageAction processImageAction, Resources resources) {
        this.activity = activity;
        this.imageName = imageName;
        this.action = processImageAction;
        this.resources = resources;

    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(activity.getResources().getString(R.string.loading_cartoon));
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        if (!OpenCVLoader.initDebug()) {
            return null;
        }
        try {

            Bitmap paperBitmap = BitmapFactory.decodeResource(resources, R.drawable.paper);
            Mat paper = new Mat();
            String mImagePath = "";
            Mat mImg = new Mat();
            Utils.bitmapToMat(CartoonTask.bitmap, mImg);
            Utils.bitmapToMat(paperBitmap, paper);

            Size sSize = mImg.size();
            sSize.width = CartoonTask.bitmap.getWidth();
            sSize.height = CartoonTask.bitmap.getHeight();

            Size paperSize = paper.size();
            paperSize.width = paperBitmap.getWidth();
            paperSize.height = paperBitmap.getHeight();

            Imgproc.resize(mImg, mImg, sSize);
            Imgproc.resize(paper, paper, paperSize);
            float rate;
            float auxRate;
            auxRate = (748*100)/CartoonTask.bitmap.getWidth()-100;
            rate = ((auxRate < 0) ? -auxRate : auxRate)/100;

            if (paperBitmap.getWidth() > 748) {
                paperSize.width = (int) Math.ceil((paperBitmap.getWidth() - (paperBitmap.getWidth() * rate)));
                paperSize.height = (int) Math.ceil((paperBitmap.getHeight() - (paperBitmap.getHeight() * rate)));
            } else {
                paperSize.width = (int) Math.ceil((paperBitmap.getWidth() + (paperBitmap.getWidth() * rate)));
                paperSize.height = (int) Math.ceil((paperBitmap.getHeight() + (paperBitmap.getHeight() * rate)));
            }

            if (CartoonTask.bitmap.getWidth()  > 748) {
                sSize.width = (int)Math.ceil((CartoonTask.bitmap.getWidth() - (CartoonTask.bitmap.getWidth() * rate)));
                sSize.height = (int)Math.ceil((CartoonTask.bitmap.getHeight() - (CartoonTask.bitmap.getHeight() * rate)));
            } else{
                sSize.width = (int)Math.ceil((CartoonTask.bitmap.getWidth() + (CartoonTask.bitmap.getWidth() * rate)));
                sSize.height = (int)Math.ceil((CartoonTask.bitmap.getHeight() + (CartoonTask.bitmap.getHeight() * rate)));
            }

            Mat edges = new Mat(sSize, CvType.CV_8U);
            Mat smallImg = new Mat(sSize, CvType.CV_8UC3);
            Bitmap result = Bitmap.createBitmap(smallImg.width(), smallImg.height(), Bitmap.Config.ARGB_8888);
            Imgproc.resize(mImg, smallImg, sSize);
            Imgproc.resize(paper, paper, sSize);
            Imgproc.cvtColor(smallImg, smallImg, Imgproc.COLOR_BGRA2BGR);

            Mat srcGray = new Mat();
            Utils.bitmapToMat(CartoonTask.bitmap, srcGray);
            Imgproc.cvtColor(srcGray, srcGray, Imgproc.COLOR_BGR2GRAY);

            Imgproc.medianBlur(srcGray, srcGray, 7);
            Imgproc.Laplacian(srcGray, edges, CvType.CV_8U, 3, 5, 5);
            Imgproc.medianBlur(edges, edges, 5);

            Imgproc.threshold(edges, edges, 5, 255, Imgproc.BORDER_REPLICATE);
            Imgproc.threshold(edges, edges, 15, 255, Imgproc.BORDER_REPLICATE);
            Imgproc.threshold(edges, edges, 35, 255, Imgproc.THRESH_BINARY_INV);

            try {

                Imgproc.resize(edges, edges, sSize);
                paper.copyTo(smallImg, edges);
                Utils.matToBitmap(smallImg, result);
                mImagePath = new ArchiveUtility(activity).takePicture(result, imageName);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                mImg = null;
                edges = null;
                sSize = null;
                srcGray = null;
                smallImg = null;
                if (result != null) {
                    result.recycle();
                    result = null;
                }
                invalidateBitmap();
            }

            return mImagePath;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    @Override
    protected void onPostExecute(String imagePath) {
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        action.executeAction(imagePath);
    }

    public static void invalidateBitmap() {
        try {
            if (CartoonTask.bitmap != null && !CartoonTask.bitmap.isRecycled()) {
                CartoonTask.bitmap.recycle();
                CartoonTask.bitmap = null;
            }
        } catch (Exception e) {
            Log.e("BITMAP", e.getMessage());
        }
        System.gc();
    }
}
