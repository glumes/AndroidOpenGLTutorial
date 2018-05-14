package com.glumes.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.Surface;

import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.SortedSet;

/**
 * Created by glumes on 27/03/2018
 */

public class Camera2 implements ICamera {


    private CameraDevice mCameraDevice;
    private CameraManager mCameraManager;
    private CameraCharacteristics mCameraCharacteristics;

    private Context mContext;

    private String mCameraId;

    private int mFacing = Constants.FACING_BACK;

    private static final SparseIntArray INTERNAL_FACINGS = new SparseIntArray();

    private AspectRatio mAspectRatio = Constants.DEFAULT_ASPECT_RATIO;


    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;


    private int mSurfaceWidth;
    private int mSurfaceHeight;

    private HandlerThread mCameraThread;
    private Handler mCameraHandler;


    static {
        INTERNAL_FACINGS.put(Constants.FACING_BACK, CameraCharacteristics.LENS_FACING_BACK);
        INTERNAL_FACINGS.put(Constants.FACING_FRONT, CameraCharacteristics.LENS_FACING_FRONT);
    }

    // 相机预览帧的数据尺寸，默认为 SurfaceTexture.class
    private Class<?> mPreviewClass = SurfaceTexture.class;

    private final SizeMap mPreviewSizes = new SizeMap();

    private final SizeMap mPictureSizes = new SizeMap();


    private ImageReader mImageReader;

    private Surface mPreviewSurface;

    private SurfaceTexture mSurfaceTexture;

    private CaptureRequest.Builder mPreviewRequestBuilder;

    private CameraCaptureSession mCaptureSession;


    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            startCaptureSession(mSurfaceWidth, mSurfaceHeight);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            mCameraDevice = null;
        }
    };


    private final CameraCaptureSession.StateCallback mSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            if (mCameraDevice == null) {
                return;
            }
            mCaptureSession = session;
            // 处理手动对焦
            // 处理闪关灯

            try {
                mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, mCameraHandler);
                Logger.d("set repeating request");
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Logger.e(e, e.getMessage());
            }

        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };


    /**
     * 拍摄时的回调方法
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }

        @Override
        public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
            super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
        }

        @Override
        public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
            super.onCaptureSequenceAborted(session, sequenceId);
        }

        @Override
        public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
            super.onCaptureBufferLost(session, request, target, frameNumber);
        }
    };


    public Camera2(Context context) {
        mContext = context;
        startCameraThread();

    }

    private void startCameraThread() {
        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }


    public void setPreviewClass(Class<?> previewClass) {
        mPreviewClass = previewClass;
    }


    public void setPreviewSurface(Surface previewSurface) {
        mPreviewSurface = previewSurface;
    }

    /**
     * 选择摄像头，默认为后置的摄像头
     *
     * @return
     */
    private boolean chooseCamera() {

        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);

        try {
            int internalFacing = INTERNAL_FACINGS.get(mFacing);
            final String[] ids = mCameraManager.getCameraIdList();

            if (ids.length == 0) { // No camera
                return false;
            }

            for (String id : ids) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(id);

                Integer internal = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (internal == null) {
                    throw new NullPointerException("Unexpected state: LENS_FACING null");
                }
                if (internal == internalFacing) {
                    mCameraId = id;
                    mCameraCharacteristics = characteristics;
                    return true;
                }
            }


            mCameraId = ids[0];
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);

            // 摄像头相对于屏幕的面
            Integer internal = mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (internal == null) {
                throw new NullPointerException("Unexpected state: LENS_FACING null");
            }
            for (int i = 0, count = INTERNAL_FACINGS.size(); i < count; i++) {
                if (INTERNAL_FACINGS.valueAt(i) == internal) {
                    mFacing = INTERNAL_FACINGS.keyAt(i);
                    return true;
                }
            }

            mFacing = Constants.FACING_BACK;

            return true;
        } catch (CameraAccessException e) {
            throw new RuntimeException("Failed to get a list of camera devices", e);
        }
    }


    private void collectCameraInfo() {

        StreamConfigurationMap map = mCameraCharacteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map == null) {
            throw new IllegalStateException("Failed to get configuration map: " + mCameraId);
        }


        collectPreviewSizes(map);

        collectPictureSizes(map);

        chooseAspectRatio();
    }


    void collectPreviewSizes(StreamConfigurationMap map) {
        mPreviewSizes.clear();
        for (android.util.Size size : map.getOutputSizes(mPreviewClass)) {
            int width = size.getWidth();
            int height = size.getHeight();
            if (width <= MAX_PREVIEW_WIDTH && height <= MAX_PREVIEW_HEIGHT) {
                mPreviewSizes.add(new Size(width, height));
            }
        }
    }


    protected void collectPictureSizes(StreamConfigurationMap map) {
        mPictureSizes.clear();
        for (android.util.Size size : map.getOutputSizes(ImageFormat.JPEG)) {
            mPictureSizes.add(new Size(size.getWidth(), size.getHeight()));
        }
    }


    /**
     * 选择合适的比例
     */
    void chooseAspectRatio() {

        for (AspectRatio ratio : mPreviewSizes.ratios()) {
            if (!mPictureSizes.ratios().contains(ratio)) {
                mPreviewSizes.remove(ratio);
            }
        }

        if (!mPreviewSizes.ratios().contains(mAspectRatio)) {
            mAspectRatio = mPreviewSizes.ratios().iterator().next();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public boolean openCamera() {

        if (!chooseCamera()) {
            return false;
        }

        collectCameraInfo();
        prepareImageReader();

        try {
            mCameraManager.openCamera(mCameraId, mStateCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void prepareImageReader() {
        if (mImageReader != null) {
            mImageReader.close();
        }
        Size largest = mPictureSizes.sizes(mAspectRatio).last();
        mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                ImageFormat.JPEG, /* maxImages */ 2);
        mImageReader.setOnImageAvailableListener(new OnImageAvailableListener(), mCameraHandler);
    }


    /**
     * @param previewWidth
     * @param previewHeight
     */
    void startCaptureSession(int previewWidth, int previewHeight) {
        if (!isCameraOpened()) {
            return;
        }

        Size previewSize = chooseOptimalSize(previewWidth, previewHeight);

        if (mSurfaceTexture != null) {
            mSurfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        }

//        printCameraInfo();


        Surface surface = new Surface(mSurfaceTexture);


        try {
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            mPreviewRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(Arrays.asList(surface/*, mImageReader.getSurface()*/), mSessionCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    private void printCameraInfo() {
        int[] AFModes = mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        for (int AFMode : AFModes) {
            Logger.d("af mode value is " + AFMode);
        }

        int[] AEModes = mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
        for (int AEMode : AEModes) {
            Logger.d("ae mode value is " + AEMode);
        }

        int[] AWBModes = mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
        for (int AWBMode : AWBModes) {
            Logger.d("awb mode value is " + AWBMode);
        }
    }

    /**
     * 根据 view 的宽高比和相机提供的预览的宽高比 选择合适的宽高比比例
     *
     * @param previewWidth
     * @param previewHeight
     * @return
     */
    private Size chooseOptimalSize(int previewWidth, int previewHeight) {
        int surfaceLonger, surfaceShorter;
        final int surfaceWidth = previewWidth;
        final int surfaceHeight = previewHeight;
        if (surfaceWidth < surfaceHeight) {
            surfaceLonger = surfaceHeight;
            surfaceShorter = surfaceWidth;
        } else {
            surfaceLonger = surfaceWidth;
            surfaceShorter = surfaceHeight;
        }
        SortedSet<Size> candidates = mPreviewSizes.sizes(mAspectRatio);

        // Pick the smallest of those big enough
        for (Size size : candidates) {
            if (size.getWidth() >= surfaceLonger && size.getHeight() >= surfaceShorter) {
                return size;
            }
        }
        // If no size is big enough, pick the largest one.
        return candidates.last();
    }


    public void startPreview() {
        startCaptureSession(mSurfaceWidth, mSurfaceHeight);
    }


    @Override
    public void stopPreview() {

    }

    @Override
    public void closeCamera() {

    }

    @Override
    public int getFacing() {
        return mFacing;
    }

    @Override
    public void setFacing(int facing) {
        mFacing = facing;
        if (isCameraOpened()) {
            stopPreview();
        }
    }

    @Override
    public boolean isCameraOpened() {
        return mCameraDevice != null;
    }

    public void setPreviewSize(int SurfaceWidth, int SurfaceHeight) {
        mSurfaceWidth = SurfaceWidth;
        mSurfaceHeight = SurfaceHeight;
    }

    public void setPreviewSurfaceTexture(@Nullable SurfaceTexture SurfaceTexture) {
        mSurfaceTexture = SurfaceTexture;
    }
}
