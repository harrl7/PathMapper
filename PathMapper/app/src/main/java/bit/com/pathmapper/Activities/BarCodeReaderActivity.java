package bit.com.pathmapper.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import bit.com.pathmapper.R;

public class BarCodeReaderActivity extends AppCompatActivity
{
    SurfaceView cameraView;
    TextView barcodeInfo;
    Bitmap myQRCode;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_reader);

        barcodeInfo = (TextView)findViewById(R.id.code_info);

        // Barcode detector
        BarcodeDetector.Builder detectorBuilder = new BarcodeDetector.Builder(this);
        detectorBuilder.setBarcodeFormats(Barcode.QR_CODE);

        barcodeDetector = detectorBuilder.build();
        barcodeDetector.setProcessor(new BarcodeDetectorProcessor());

        // Camera
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).build();

        cameraView = (SurfaceView)findViewById(R.id.camera_view);
        cameraView.getHolder().addCallback(new SurfaceHolderSetUp());
        cameraView.setOnClickListener(new ClickGotoInfoHandler());
    }

    // SurfaceView
    public class SurfaceHolderSetUp implements SurfaceHolder.Callback
    {
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {

            try {
                cameraSource.start(cameraView.getHolder());
            } catch (IOException ie) {
                Log.e("CAMERA SOURCE", ie.getMessage());
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            // Empty
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
            cameraSource.stop();
        }
    }

    // Barcode Detector
    public class BarcodeDetectorProcessor implements Detector.Processor<Barcode>
    {
    @Override
    public void release()
    {
        // Empty
    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections)
    {

        final SparseArray<Barcode> barcodes = detections.getDetectedItems();

        if (barcodes.size() != 0)
        {
            barcodeInfo.post(new Runnable()
            {
                public void run()
                {
                    barcodeInfo.setText(barcodes.valueAt(0).displayValue);
                }
            });
        }
    }



//        try {
//            myQRCode = BitmapFactory.decodeStream(getAssets().open("myqrcode.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Frame myFrame = new Frame.Builder().setBitmap(myQRCode).build();
//        SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);



    }

    // Goto info activity test
    public class ClickGotoInfoHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            Intent toInfoIntent = new Intent(BarCodeReaderActivity.this, ItemInfoActivity.class);
            toInfoIntent.putExtra("ItemID", 0);
            startActivity(toInfoIntent);
        }
    }
}
