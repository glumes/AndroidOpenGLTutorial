//package com.glumes.openglbasicshape.helper;
//
//import android.opengl.GLSurfaceView;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//import java.nio.IntBuffer;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//
//public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer, View.OnClickListener {
//
//    private GLSurfaceView mGLSurfaceView;
//    //    private TextureDrawer mTextureDrawer;
//    private ShaderDrawer mShaderDrawer;
//    private static final int mInputWidth = 320;
//    private static final int mInputHeight = 200;
//
//    private int mDisplayWidth;
//    private int mDisplayHeight;
//    private Button mButton;
//
//    private int mTextureId;
//    private IntBuffer mGLRgbBuffer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mGLSurfaceView = findViewById(R.id.glsurfaceview);
//        mButton = findViewById(R.id.request);
//
//        mGLSurfaceView.setEGLContextClientVersion(2);
//
//        mGLSurfaceView.setRenderer(this);
//
//        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//
//        mButton.setOnClickListener(this);
//    }
//
//    @Override
//    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//
////        mTextureDrawer = new TextureDrawer(this, Constant.VERTEX_SHADER, Constant.FRAGMENT_SHADER);
//
//        mShaderDrawer = new ShaderDrawer(this);
//
//        mTextureId = TextureHelper.loadTexture(this, R.drawable.texture);
//
////        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.texture);
////
////        mGLRgbBuffer = IntBuffer.allocate(bitmap.getByteCount());
////
////        bitmap.copyPixelsToBuffer(mGLRgbBuffer);
////
////        mTextureId = TextureHelper.loadTexture(mInputWidth, mInputHeight, mGLRgbBuffer);
//
//    }
//
//    @Override
//    public void onSurfaceChanged(GL10 gl, int width, int height) {
//
//        mDisplayWidth = width;
//        mDisplayHeight = height;
//
////        mTextureDrawer.setInputSize(mInputWidth, mInputHeight);
////        mTextureDrawer.setDisplaySize(mDisplayWidth, mDisplayHeight);
////        mTextureDrawer.init();
//
//        mShaderDrawer.setInputSize(mInputWidth, mInputHeight);
//        mShaderDrawer.setDisplaySize(mDisplayWidth, mDisplayHeight);
//        mShaderDrawer.init();
//    }
//
//    @Override
//    public void onDrawFrame(GL10 gl) {
////        mTextureDrawer.renderer(mTextureId);
//        mShaderDrawer.renderer(mTextureId);
//    }
//
//    @Override
//    public void onClick(View v) {
//        mGLSurfaceView.requestRender();
//    }
//}
