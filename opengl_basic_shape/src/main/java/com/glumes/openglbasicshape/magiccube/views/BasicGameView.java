/*
 * Copyright 2011-2014 Zhaotian Wang <zhaotianzju@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glumes.openglbasicshape.magiccube.views;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.glumes.comlib.LogUtil;
import com.glumes.openglbasicshape.magiccube.MagicCubeRender;
import com.glumes.openglbasicshape.magiccube.MagiccubePreference;
import com.glumes.openglbasicshape.magiccube.interfaces.MessageSender;
import com.glumes.openglbasicshape.magiccube.interfaces.OnStepListener;
import com.glumes.openglbasicshape.magiccube.interfaces.OnTimerListener;


public class BasicGameView extends GLSurfaceView implements MessageSender, OnStepListener {

    protected OnTimerListener timerListener = null;
    protected OnStepListener stepListener = null;
    protected MessageSender messageSender = null;
    protected MagicCubeRender render;
    protected Context context;

    protected boolean RotateOrMove1;
    protected boolean RotateOrMove2;

    private float[] Point1;
    private float[] Point2;
    private float[] LastPos1;
    private float[] LastPos2;

    private float Point[];
    private int fingerID;

    protected int FaceIndex1;    //last touched face index
    protected int FaceIndex2;    //last touched face index

    protected float Sensitivity;

    protected int MoveTime;
    protected int nStep;
    //
    protected boolean CanMove;
    protected boolean CanRotate;

    protected String CmdStr = null;

    private static final boolean kUseMultisampling = false;

    // If |kUseMultisampling| is set, this is what chose the multisampling config.
    private MultisampleConfigChooser mConfigChooser;
    //protected final float TOUCH_SCALE_FACTOR = 180.f / 360.f;

    protected float LastPos[] = new float[2];

    protected int threadState = THREADRUNNING;
    protected static final int THREADRUNNING = 0;
    protected static final int THREADPAUSE = 1;
    protected static final int THREADSTOP = 2;

    public BasicGameView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        setEGLConfigChooser(true);    //enable depth-buffer
        if (kUseMultisampling)
            setEGLConfigChooser(mConfigChooser = new MultisampleConfigChooser());
        render = new MagicCubeRender(context, 540, 850);
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        CanMove = true;
        CanRotate = true;

        MoveTime = 0;
        nStep = 0;

        Point1 = new float[2];
        Point2 = new float[2];
        LastPos1 = new float[2];
        LastPos2 = new float[2];

        Point = new float[2];

        Point1[0] = -1.f;
        Point1[1] = -1.f;
        Point2[0] = -1.f;
        Point2[1] = -1.f;
        LastPos1[0] = -1.f;
        LastPos2[0] = -1.f;

        Sensitivity = (float) MagiccubePreference.GetPreference(MagiccubePreference.Sensitivity, context) / 100.f;
    }

    public void AutoSolve(String SolverName) {
        this.render.AutoSolve(SolverName);
    }

    public void AutoSolve2(String SolverName) {
        this.SetCanMove(false);
        this.render.AutoSolve2(SolverName);
    }

    public void AutoSolve3(String SolverName) {
        this.SetCanMove(true);
        this.render.AutoSolve3(SolverName);
    }

    public void Reset() {
        MoveTime = 0;
        nStep = 0;

        render.Reset();
    }

    public String MessUp(int nStep) {
        if (render != null) {
            String strmessup = render.MessUp(nStep);
            render.SetCmdStrBefore(strmessup);
            return strmessup;
        }
        return "";
    }

    public boolean IsComplete() {
        if (render != null) {
            return render.IsComplete();
        }
        return false;
    }

    public void MessUp(String cmdstr) {
        if (render != null) {
            render.MessUp(cmdstr);
            render.SetCmdStrBefore(cmdstr);
        }
    }

    public void SetDrawCube(boolean DrawCube) {
        this.render.SetDrawCube(DrawCube);
    }

    public void MoveBack() {
        String cmdstr = "";
        if ((cmdstr = this.render.MoveBack()) != null) {
            if (this.stepListener != null) {
                //this.stepListener.SetStep(++nStep);
            }
            if (this.messageSender != null) {
                //this.messageSender.SendMessage(cmdstr);
            }
        }
    }

    public void MoveForward2() {
        this.render.MoveForward2();
    }

    public void SetSensitivity(float Sensitivity) {
        this.Sensitivity = Sensitivity;
    }

    public void SetSensitivity() {
        Sensitivity = (float) MagiccubePreference.GetPreference(MagiccubePreference.Sensitivity, context) / 100.f;
    }

    public void MoveForward() {
        String cmdstr = "";
        if ((cmdstr = this.render.MoveForward()) != null) {
            if (this.stepListener != null) {
                //this.stepListener.SetStep(++nStep);
            }
            if (this.messageSender != null) {
                //this.messageSender.SendMessage(cmdstr);
            }
        }

    }

	/*public boolean onTouchEvent2(final MotionEvent event) {

		CmdStr = null;	//reset the CmdStr;
		
		float x = event.getX();
		float y = event.getY();
		
		
		float Point[] = new float[2];
		Point[0] = x;
		Point[1] = y;
		
		int [] Pos = null;
		
		int opcode = event.getAction() & MotionEvent.ACTION_MASK;
		
		if(opcode==MotionEvent.ACTION_DOWN)
		{
			Point1[0] = x;
			Point1[1] = y;
			Log.e("down","down");
			FaceIndex = render.IsInCubeArea(Point);
			if(FaceIndex1>=0 && CanMove)
			{
				RotateOrMove1 = MagicCubeRender.MOVE;
				//Log.e("move", render.rx+ " " + render.ry);
			}
			else
			{
				RotateOrMove1 = MagicCubeRender.ROTATE;
				//Log.e("rotate", render.rx+ " " + render.ry);
			}
			if( RotateOrMove1  == MagicCubeRender.ROTATE )
			{
				LastPos[0] = x;
				LastPos[1] = y;
			}
			else
			{
				LastPos[0] = x;
				LastPos[1] = y;
			}
			
		}
		else if(opcode == MotionEvent.ACTION_POINTER_DOWN)
		{
			Point2[0] = x;
			Point2[1] = y;
			Log.e("2down","2down");
		}
		else if(opcode==MotionEvent.ACTION_MOVE)
		{
			//Log.e("move","move");
			if(RotateOrMove1 == MagicCubeRender.MOVE)
				return false;
        	float dx = x - LastPos[0];
        	float dy = y - LastPos[1];
			if(RotateOrMove1 == MagicCubeRender.ROTATE && this.CanRotate)
			{
	        	if(Math.abs(dy)<Math.abs(dx))
	        	{
	        		if(render.rx<90 && render.rx >-90)
	        			render.ry += dx * Sensitivity;
	        		else
	        			render.ry -= dx*Sensitivity;
	        		if(render.ry>180)
	        			render.ry -= 360;
	        		else if(render.ry<-180)
	        			render.ry += 360;
	        	}
	        	
	        	else
	        	{
	        		float tmp = render.rx;
	        		render.rx += dy * Sensitivity;
	        		
	        		if(render.rx>180)
	        			render.rx -= 360;
	        		else if(render.rx<-180)
	        			render.rx += 360;
	        	}
	        	//render.AdjustFace();
				LastPos[0] = x;
				LastPos[1] = y;
	    		//requestRender();		
			}
		}
		else if(opcode == MotionEvent.ACTION_UP)
		{
			Log.e("up","up");
			if(RotateOrMove1 == MagicCubeRender.MOVE && render.IsMoveValid(LastPos, Point) && this.CanMove)
			{
				//CmdStr = render.CalcCommand(LastPos, Point, FaceIndex);
				String cmdstr = render.CalcCommand(LastPos, Point, FaceIndex);
				if(cmdstr != null)
				{
					if( this.stepListener != null)
					{
						//this.stepListener.SetStep(++nStep);
					}
					if( this.messageSender != null)
					{
						//this.messageSender.SendMessage(cmdstr);
					}
				}
				//mediaPlayer.start();
			}
		}
		else if(opcode == MotionEvent.ACTION_POINTER_UP)
		{
			Log.e("2up","2up");
		}
		return true;
	}*/

    public boolean onTouchEvent(final MotionEvent event) {

        if (event.getPointerCount() > 2) {
            LogUtil.d("get pointer count  > 3");
            return false;
        }

        CmdStr = null;    //reset the CmdStr;

        //GetXY(Point, event);
        //get the xy
        //int n = event.getPointerCount();

        //float x = event.getX();
        //float y = event.getY();

        //Point[0] = x;
        //Point[1] = y;

        int[] Pos = null;

        int opcode = event.getAction() & MotionEvent.ACTION_MASK;


        if (opcode == MotionEvent.ACTION_DOWN) {
            LogUtil.d("action down");
            Point1[0] = event.getX();
            Point1[1] = event.getY();
            LastPos1[0] = Point1[0];
            LastPos1[1] = Point1[1];

            FaceIndex1 = render.IsInCubeArea(Point1);
            //Log.e("down","down"+FaceIndex1);
            if (FaceIndex1 >= 0 && CanMove) {
                RotateOrMove1 = MagicCubeRender.MOVE;
                LogUtil.d("RotateOrMove1 = MagicCubeRender.MOVE");
            } else {
                RotateOrMove1 = MagicCubeRender.ROTATE;
                LogUtil.d("RotateOrMove1 = MagicCubeRender.ROTATE;");
            }
        } else if (opcode == MotionEvent.ACTION_POINTER_DOWN) {
            int n = event.getPointerCount();
            LogUtil.d("action pointer down num is " + n);
            float max = -1.f;
            float curdist;
            for (int i = 0; i < n; i++) {
                if ((curdist = GetDist(LastPos1[0], LastPos1[1], event.getX(i), event.getY(i)))
                        > max) // the furthest from the first touch point is the second point
                {
                    Point2[0] = event.getX(i);
                    Point2[1] = event.getY(i);
                    LastPos2[0] = Point2[0];
                    LastPos2[1] = Point2[1];
                    max = curdist;
                }
            }

            FaceIndex2 = render.IsInCubeArea(Point2);
            if (FaceIndex2 >= 0 && CanMove) {
                RotateOrMove2 = MagicCubeRender.MOVE;
                LogUtil.d("RotateOrMove1 = MagicCubeRender.MOVE");

            } else {
                RotateOrMove2 = MagicCubeRender.ROTATE;
                LogUtil.d("RotateOrMove1 = MagicCubeRender.ROTATE;");

            }
            //Log.e("2down","2down"+FaceIndex2);
        } else if (opcode == MotionEvent.ACTION_MOVE) {
            LogUtil.d("action move");
            fingerID = GetFingerID(Point, event);
            if (fingerID == 1) {
                LogUtil.d("finger num is 1");
/*				LastPos1[0] = Point[0];
                LastPos1[1] = Point[1];*/
                if (RotateOrMove1 == MagicCubeRender.MOVE)
                    return false;
                float dx = Point[0] - Point1[0];
                float dy = Point[1] - Point1[1];
                if (RotateOrMove1 == MagicCubeRender.ROTATE && this.CanRotate) {
                    if (Math.abs(dy) < Math.abs(dx)) {
                        LogUtil.d("dy < dx");
                        if (render.rx < 90 && render.rx > -90)
                            render.ry += dx * Sensitivity;
                        else
                            render.ry -= dx * Sensitivity;
                        if (render.ry > 180)
                            render.ry -= 360;
                        else if (render.ry < -180)
                            render.ry += 360;
                    } else {
                        LogUtil.d("dy > dx");
                        float tmp = render.rx;
                        render.rx += dy * Sensitivity;

                        if (render.rx > 180)
                            render.rx -= 360;
                        else if (render.rx < -180)
                            render.rx += 360;
                    }
                    //render.AdjustFace();
                    Point1[0] = Point[0];
                    Point1[1] = Point[1];
                    //requestRender();
                }
            } else {
                LogUtil.d("finger num is not 1");
                if (RotateOrMove2 == MagicCubeRender.MOVE)
                    return false;
/*				LastPos2[0] = Point[0];
                LastPos2[1] = Point[1];*/
                Point2[0] = Point[0];
                Point2[1] = Point[1];
                return false;
/*				if(RotateOrMove2 == MagicCubeRender.MOVE)
                    return false;
	        	float dx = Point[0] - Point2[0];
	        	float dy = Point[1] - Point2[1];
				if(RotateOrMove2 == MagicCubeRender.ROTATE && this.CanRotate)
				{
		        	if(Math.abs(dy)<Math.abs(dx))
		        	{
		        		if(render.rx<90 && render.rx >-90)
		        			render.ry += dx * Sensitivity;
		        		else
		        			render.ry -= dx*Sensitivity;
		        		if(render.ry>180)
		        			render.ry -= 360;
		        		else if(render.ry<-180)
		        			render.ry += 360;
		        	}
		        	
		        	else
		        	{
		        		float tmp = render.rx;
		        		render.rx += dy * Sensitivity;
		        		
		        		if(render.rx>180)
		        			render.rx -= 360;
		        		else if(render.rx<-180)
		        			render.rx += 360;
		        	}
		        	//render.AdjustFace();
		        	Point2[0] = Point[0];
		        	Point2[1] = Point[1];
		    		//requestRender();		
				}*/
            }
        } else if (opcode == MotionEvent.ACTION_UP) {
            LogUtil.d("action up ");
            //Log.e("up","up");
            Point[0] = event.getX();
            Point[1] = event.getY();

            if (RotateOrMove1 == MagicCubeRender.MOVE && render.IsMoveValid(Point1, Point) && this.CanMove) {
                LogUtil.d("Let`s move");
                String cmdstr = render.CalcCommand(Point1, Point, FaceIndex1);
            }
        } else if (opcode == MotionEvent.ACTION_POINTER_UP) {
            LogUtil.d("action pointer up");
            int index = event.getActionIndex();
            Point[0] = event.getX(index);
            Point[1] = event.getY(index);
            if (GetDist(Point[0], Point[1], LastPos1[0], LastPos1[1])
                    < GetDist(Point[0], Point[1], LastPos2[0], LastPos2[1])) {
                if (RotateOrMove1 == MagicCubeRender.MOVE && render.IsMoveValid(Point1, Point) && this.CanMove) {
                    String cmdstr = render.CalcCommand(Point1, Point, FaceIndex1);
                }

                Point1[0] = Point2[0];
                Point1[1] = Point2[1];

                LastPos1[0] = LastPos2[0];
                LastPos1[1] = LastPos2[1];

                RotateOrMove1 = RotateOrMove2;
                FaceIndex1 = FaceIndex2;
                Point2[0] = -1.f;
                Point2[1] = -1.f;
            } else {
                if (RotateOrMove2 == MagicCubeRender.MOVE && render.IsMoveValid(Point2, Point) && this.CanMove) {
                    String cmdstr = render.CalcCommand(Point2, Point, FaceIndex2);
                }
            }


            Point2[0] = -1.f;
            Point2[1] = -1.f;
            //Log.e("2up","2up");
            //Log.e("2upn", event.getPointerCount()+"");
        }
        return true;
    }


    private float GetMinDist(float x, float y) {
        float dist1 = GetDist(LastPos1[0], LastPos1[1], x, y);
        float dist2 = GetDist(LastPos2[0], LastPos2[1], x, y);

        return Math.min(dist1, dist2);
    }

    public boolean IsSolved() {
        return render.IsSolved();
    }

    public void SetCanMove(boolean CanMove) {
        this.CanMove = CanMove;
    }

    public void SetCanRotate(boolean CanRotate) {
        this.CanRotate = CanRotate;
    }

    public void SetOnTimerListener(OnTimerListener TimerListener) {
        this.timerListener = TimerListener;
    }

    public void SetOnStepListener(OnStepListener stepListener) {
        this.stepListener = stepListener;
    }

    public void SetMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
//		render.SetMessageSender(this);
    }

    private class RefreshTime extends Thread {
        public void run() {
            Log.e("e", "basic");
            if (BasicGameView.this.timerListener != null) {
                while (MoveTime >= 0) {
                    timerListener.onTimer(MoveTime);
                    MoveTime++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void SendMessage(String msg) {
        if (this.messageSender != null) {
            this.messageSender.SendMessage(msg);
        }
    }

    @Override
    public void AddStep() {
        // TODO Auto-generated method stub
        if (this.stepListener != null) {
            this.stepListener.SetStep(++nStep);
        }
    }

    @Override
    public void AddStep(int nStep) {
        // TODO Auto-generated method stub
        if (this.stepListener != null) {
            this.stepListener.SetStep(++nStep);
        }
    }

    @Override
    public void SetStep(int nStep) {
        // TODO Auto-generated method stub
        if (this.stepListener != null) {
            this.stepListener.SetStep(++nStep);
        }
    }

    private int GetFingerID(float[] P) {
        if (this.Point1[0] < 0 && Point2[0] < 0) {
            return 1;
        }
        if (this.Point1[0] > 0 && Point2[0] < 0) {
            return 1;
        }
        if (this.Point1[0] < 0 && Point2[0] > 0) {
            return 2;
        }

        if (GetDist(P[0], P[1], Point1[0], Point1[1]) < GetDist(P[0], P[1], Point2[0], Point2[1])) {
            return 1;
        }
        return 2;
    }

    private int GetFingerID(float[] P, MotionEvent event)    //called when move to find which finger is moving
    {
        int n;
        int result;
        if ((n = event.getPointerCount()) == 1) {
            P[0] = event.getX();
            P[1] = event.getY();

            LastPos1[0] = P[0];
            LastPos1[1] = P[1];
            LogUtil.d("get Pointer Count num is  1");
            return 1;
        } else {
            LogUtil.d("pointer count num is " + n);
        }

        float maxdist = -1.f;//Float.MAX_VALUE;
        int index = -1;
        float curdist;

        for (int i = 0; i < n; i++) {
            if ((curdist = this.GetMinDist(event.getX(i), event.getY(i))) > maxdist) {
                index = i;
                maxdist = curdist;
            }
        }

        LogUtil.d("index is " + index);
        //index = event.getActionIndex();

        P[0] = event.getX(index);
        P[1] = event.getY(index);

        if (GetDist(P[0], P[1], LastPos1[0], LastPos1[1]) < GetDist(P[0], P[1], LastPos2[0], LastPos2[1])) {
            LastPos2[0] = event.getX(1 - index);
            LastPos2[1] = event.getY(1 - index);
            LastPos1[0] = event.getX(index);
            LastPos1[1] = event.getY(index);
            LogUtil.d("get Finger Id == 1");
            return 1;
        } else {
            LastPos2[0] = event.getX(index);
            LastPos2[1] = event.getY(index);
            LastPos1[0] = event.getX(1 - index);
            LastPos1[1] = event.getY(1 - index);
            LogUtil.d("get Finger Id == 2");

            return 2;
        }

/*		//���޸�
        if( GetDist(event.getX(0), event.getY(0), LastPos1[0], LastPos1[1])
				< GetDist(event.getX(0), event.getY(0), LastPos2[0], LastPos2[1]))
		{
			LastPos2[0] = event.getX(1);
			LastPos2[0] = event.getY(1);
			LastPos1[0] = event.getX(0);
			LastPos1[1] = event.getY(0);
		}
		else
		{
			LastPos2[0] = event.getX(0);
			LastPos2[0] = event.getY(0);
			LastPos1[0] = event.getX(1);
			LastPos1[1] = event.getY(1);
		}
		//
		return result;*/

    }

    private float GetDist(float x1, float y1, float x2, float y2) {
        float dist = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        //Log.e("dist", dist+"");

        return dist;
    }

    public void setVolume(int volume) {
        // TODO Auto-generated method stub
        render.setVolume(volume);
    }
}

