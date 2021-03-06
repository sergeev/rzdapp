package com.example.opencvapp;

        import java.io.IOException;

        import android.app.Activity;
        import android.graphics.Matrix;
        import android.graphics.RectF;
        import android.hardware.Camera;
        import android.hardware.Camera.CameraInfo;
        import android.hardware.Camera.Size;
        import android.os.Bundle;
        import android.view.Display;
        import android.view.Surface;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import android.view.Window;
        import android.view.WindowManager;

public class CameraScreen extends Activity {

    SurfaceView sv;
    SurfaceHolder holder;
    HolderCallback holderCallback;
    @SuppressWarnings("deprecation")
    Camera camera;

    final int CAMERA_ID = 0;
    final boolean FULL_SCREEN = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        sv = (SurfaceView) findViewById(R.id.surfaceView);
        holder = sv.getHolder();
        //noinspection deprecation
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holderCallback = new HolderCallback();
        holder.addCallback(holderCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //noinspection deprecation
        camera = Camera.open(CAMERA_ID);
        setPreviewSize(FULL_SCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null)
            //noinspection deprecation
            camera.release();
        camera = null;
    }

    class HolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                //noinspection deprecation
                camera.setPreviewDisplay(holder);
                //noinspection deprecation
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            //noinspection deprecation
            camera.stopPreview();
            setCameraDisplayOrientation(CAMERA_ID);
            try {
                //noinspection deprecation
                camera.setPreviewDisplay(holder);
                //noinspection deprecation
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

    }

    void setPreviewSize(boolean fullScreen) {

        // ???????????????? ?????????????? ????????????
        Display display = getWindowManager().getDefaultDisplay();
        //noinspection deprecation
        boolean widthIsMax = display.getWidth() > display.getHeight();

        // ???????????????????? ?????????????? ???????????? ????????????
        //noinspection deprecation
        Size size = camera.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        // RectF ????????????, ???????????????????????? ???????????????? ????????????
        //noinspection deprecation
        rectDisplay.set(0, 0, display.getWidth(), display.getHeight());

        // RectF ????????????
        if (widthIsMax) {
            // ???????????? ?? ???????????????????????????? ????????????????????
            //noinspection deprecation
            rectPreview.set(0, 0, size.width, size.height);
        } else {
            // ???????????? ?? ???????????????????????? ????????????????????
            //noinspection deprecation
            rectPreview.set(0, 0, size.height, size.width);
        }

        Matrix matrix = new Matrix();
        // ???????????????????? ?????????????? ????????????????????????????
        if (!fullScreen) {
            // ???????? ???????????? ?????????? "??????????????" ?? ?????????? (???????????? ?????????????? ???? ??????????)
            matrix.setRectToRect(rectPreview, rectDisplay,
                    Matrix.ScaleToFit.START);
        } else {
            // ???????? ?????????? ?????????? "??????????????" ?? ???????????? (???????????? ?????????????? ???? ??????????)
            matrix.setRectToRect(rectDisplay, rectPreview,
                    Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }
        // ????????????????????????????
        matrix.mapRect(rectPreview);

        // ?????????????????? ???????????????? surface ???? ?????????????????????????? ????????????????????????????
        sv.getLayoutParams().height = (int) (rectPreview.bottom);
        sv.getLayoutParams().width = (int) (rectPreview.right);
    }

    void setCameraDisplayOrientation(int cameraId) {
        // ???????????????????? ?????????????????? ???????????????? ?????????? ???? ?????????????????????? ??????????????????
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = 0;

        // ???????????????? ???????? ???? ???????????? cameraId
        @SuppressWarnings("deprecation") CameraInfo info = new CameraInfo();
        //noinspection deprecation
        Camera.getCameraInfo(cameraId, info);

        // ???????????? ????????????
        //noinspection deprecation
        if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
            result = ((360 - degrees) + info.orientation);
        } else
            // ???????????????? ????????????
            //noinspection deprecation
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                result = ((360 - degrees) - info.orientation);
                result += 360;
            }
        result = result % 360;
        //noinspection deprecation
        camera.setDisplayOrientation(result);
    }
}