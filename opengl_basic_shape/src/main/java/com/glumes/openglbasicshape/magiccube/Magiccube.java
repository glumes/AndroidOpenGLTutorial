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

package com.glumes.openglbasicshape.magiccube;

import android.content.Context;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;


public class Magiccube {
	public Cube[] cubes;
	public int cubei[];
	public Face[] faces;
	
	public float rx, ry;
	
	public Magiccube()
	{
/*		this.rx = 22.f;
		this.ry = -34.f;*/
		
		rx = 0.f;
		ry = 0.f;
		
	    cubes = new Cube[27];
	    cubei = new int[27];
	    faces = new Face[6];
	    //Initial the cubes
	    for( int i=0; i<27; i++)
	    {
	    	cubes[i] = new Cube(i);
	    	cubei[i] = i;
	    }	  
	      
	    for( int i=0; i<6; i++)
	    {
	    	faces[i] = new Face(i);
	    }
	}
	
	public void LoadTexture(GL10 gl, Context context)
	{
	      //Initial the cubes
	      for( int i=0; i<27; i++)
	      {
	    	  cubes[i].LoadTexture(gl, context);
	      }	  
	}
	
	public void RotateRow(int RowId, int Direction,float angle, boolean IfSetMap)
	{

		if(Direction==Cube.ClockWise)
		{
			for(int i=RowId*9;i<RowId*9+9;i++)
			{
				cubes[cubei[i]].Rotate(Cube.YAxis, angle);
			}
			if(IfSetMap)
			{
				int[] temp = new int[9];
				for(int i=0;i<9;i++)
					temp[i] = cubei[RowId*9+i];
				cubei[RowId*9+0] = temp[6];
				cubei[RowId*9+1] = temp[3];
				cubei[RowId*9+2] = temp[0];
				cubei[RowId*9+3] = temp[7];
				cubei[RowId*9+4] = temp[4];
				cubei[RowId*9+5] = temp[1];
				cubei[RowId*9+6] = temp[8];
				cubei[RowId*9+7] = temp[5];
				cubei[RowId*9+8] = temp[2];	
				
				//solve the changing color of each subfaces
				if(RowId == 0)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.TOP].Subfaces[i];
					}
					
					faces[Face.TOP].Subfaces[Face.Y1] = temp[Face.S1];
					faces[Face.TOP].Subfaces[Face.Y2] = temp[Face.E1];
					faces[Face.TOP].Subfaces[Face.Y3] = temp[Face.Y1];
					faces[Face.TOP].Subfaces[Face.E1] = temp[Face.S2];
					faces[Face.TOP].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.TOP].Subfaces[Face.E3] = temp[Face.Y2];
					faces[Face.TOP].Subfaces[Face.S1] = temp[Face.S3];
					faces[Face.TOP].Subfaces[Face.S2] = temp[Face.E3];
					faces[Face.TOP].Subfaces[Face.S3] = temp[Face.Y3];
					
					temp[0] = faces[Face.FRONT].Subfaces[Face.S1];
					temp[1] = faces[Face.FRONT].Subfaces[Face.S2];
					temp[2] = faces[Face.FRONT].Subfaces[Face.S3];
					
					faces[Face.FRONT].Subfaces[Face.S1] = faces[Face.LEFT].Subfaces[Face.S1];
					faces[Face.FRONT].Subfaces[Face.S2] = faces[Face.LEFT].Subfaces[Face.S2];
					faces[Face.FRONT].Subfaces[Face.S3] = faces[Face.LEFT].Subfaces[Face.S3];
					
					faces[Face.LEFT].Subfaces[Face.S1] = faces[Face.BACK].Subfaces[Face.S1];
					faces[Face.LEFT].Subfaces[Face.S2] = faces[Face.BACK].Subfaces[Face.S2];
					faces[Face.LEFT].Subfaces[Face.S3] = faces[Face.BACK].Subfaces[Face.S3];
					
					faces[Face.BACK].Subfaces[Face.S1] = faces[Face.RIGHT].Subfaces[Face.S1];
					faces[Face.BACK].Subfaces[Face.S2] = faces[Face.RIGHT].Subfaces[Face.S2];
					faces[Face.BACK].Subfaces[Face.S3] = faces[Face.RIGHT].Subfaces[Face.S3];
					
					faces[Face.RIGHT].Subfaces[Face.S1] = temp[0];
					faces[Face.RIGHT].Subfaces[Face.S2] = temp[1];
					faces[Face.RIGHT].Subfaces[Face.S3] = temp[2];
				}
				else if(RowId == 1)
				{
					temp[0] = faces[Face.FRONT].Subfaces[Face.E1];
					temp[1] = faces[Face.FRONT].Subfaces[Face.E2];
					temp[2] = faces[Face.FRONT].Subfaces[Face.E3];
					
					faces[Face.FRONT].Subfaces[Face.E1] = faces[Face.LEFT].Subfaces[Face.E1];
					faces[Face.FRONT].Subfaces[Face.E2] = faces[Face.LEFT].Subfaces[Face.E2];
					faces[Face.FRONT].Subfaces[Face.E3] = faces[Face.LEFT].Subfaces[Face.E3];
					
					faces[Face.LEFT].Subfaces[Face.E1] = faces[Face.BACK].Subfaces[Face.E1];
					faces[Face.LEFT].Subfaces[Face.E2] = faces[Face.BACK].Subfaces[Face.E2];
					faces[Face.LEFT].Subfaces[Face.E3] = faces[Face.BACK].Subfaces[Face.E3];
					
					faces[Face.BACK].Subfaces[Face.E1] = faces[Face.RIGHT].Subfaces[Face.E1];
					faces[Face.BACK].Subfaces[Face.E2] = faces[Face.RIGHT].Subfaces[Face.E2];
					faces[Face.BACK].Subfaces[Face.E3] = faces[Face.RIGHT].Subfaces[Face.E3];
					
					faces[Face.RIGHT].Subfaces[Face.E1] = temp[0];
					faces[Face.RIGHT].Subfaces[Face.E2] = temp[1];
					faces[Face.RIGHT].Subfaces[Face.E3] = temp[2];
				}
				else if(RowId == 2)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.BOTTOM].Subfaces[i];
					}
					
					faces[Face.BOTTOM].Subfaces[Face.Y1] = temp[Face.Y3];
					faces[Face.BOTTOM].Subfaces[Face.Y2] = temp[Face.E3];
					faces[Face.BOTTOM].Subfaces[Face.Y3] = temp[Face.S3];
					faces[Face.BOTTOM].Subfaces[Face.E1] = temp[Face.Y2];
					faces[Face.BOTTOM].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.BOTTOM].Subfaces[Face.E3] = temp[Face.S2];
					faces[Face.BOTTOM].Subfaces[Face.S1] = temp[Face.Y1];
					faces[Face.BOTTOM].Subfaces[Face.S2] = temp[Face.E1];
					faces[Face.BOTTOM].Subfaces[Face.S3] = temp[Face.S1];
					
					temp[0] = faces[Face.FRONT].Subfaces[Face.Y1];
					temp[1] = faces[Face.FRONT].Subfaces[Face.Y2];
					temp[2] = faces[Face.FRONT].Subfaces[Face.Y3];
					
					faces[Face.FRONT].Subfaces[Face.Y1] = faces[Face.LEFT].Subfaces[Face.Y1];
					faces[Face.FRONT].Subfaces[Face.Y2] = faces[Face.LEFT].Subfaces[Face.Y2];
					faces[Face.FRONT].Subfaces[Face.Y3] = faces[Face.LEFT].Subfaces[Face.Y3];
					
					faces[Face.LEFT].Subfaces[Face.Y1] = faces[Face.BACK].Subfaces[Face.Y1];
					faces[Face.LEFT].Subfaces[Face.Y2] = faces[Face.BACK].Subfaces[Face.Y2];
					faces[Face.LEFT].Subfaces[Face.Y3] = faces[Face.BACK].Subfaces[Face.Y3];
					
					faces[Face.BACK].Subfaces[Face.Y1] = faces[Face.RIGHT].Subfaces[Face.Y1];
					faces[Face.BACK].Subfaces[Face.Y2] = faces[Face.RIGHT].Subfaces[Face.Y2];
					faces[Face.BACK].Subfaces[Face.Y3] = faces[Face.RIGHT].Subfaces[Face.Y3];
					
					faces[Face.RIGHT].Subfaces[Face.Y1] = temp[0];
					faces[Face.RIGHT].Subfaces[Face.Y2] = temp[1];
					faces[Face.RIGHT].Subfaces[Face.Y3] = temp[2];
				}
				
				
			}
		}
		else
		{
			for(int i=RowId*9;i<RowId*9+9;i++)
			{
				cubes[cubei[i]].Rotate(Cube.YAxis, -angle);
			}
			if(IfSetMap)
			{
				int[] temp = new int[9];
				for(int i=0;i<9;i++)
					temp[i] = cubei[RowId*9+i];	
				
				cubei[RowId*9+0] = temp[2];
				cubei[RowId*9+1] = temp[5];
				cubei[RowId*9+2] = temp[8];
				cubei[RowId*9+3] = temp[1];
				cubei[RowId*9+4] = temp[4];
				cubei[RowId*9+5] = temp[7];
				cubei[RowId*9+6] = temp[0];
				cubei[RowId*9+7] = temp[3];
				cubei[RowId*9+8] = temp[6];	
				
				//sovle the changing color of each subfaces
				if(RowId == 0)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.TOP].Subfaces[i];
					}
					
					faces[Face.TOP].Subfaces[Face.Y1] = temp[Face.Y3];
					faces[Face.TOP].Subfaces[Face.Y2] = temp[Face.E3];
					faces[Face.TOP].Subfaces[Face.Y3] = temp[Face.S3];
					faces[Face.TOP].Subfaces[Face.E1] = temp[Face.Y2];
					faces[Face.TOP].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.TOP].Subfaces[Face.E3] = temp[Face.S2];
					faces[Face.TOP].Subfaces[Face.S1] = temp[Face.Y1];
					faces[Face.TOP].Subfaces[Face.S2] = temp[Face.E1];
					faces[Face.TOP].Subfaces[Face.S3] = temp[Face.S1];
					
					temp[0] = faces[Face.FRONT].Subfaces[Face.S1];
					temp[1] = faces[Face.FRONT].Subfaces[Face.S2];
					temp[2] = faces[Face.FRONT].Subfaces[Face.S3];
					
					faces[Face.FRONT].Subfaces[Face.S1] = faces[Face.RIGHT].Subfaces[Face.S1];
					faces[Face.FRONT].Subfaces[Face.S2] = faces[Face.RIGHT].Subfaces[Face.S2];
					faces[Face.FRONT].Subfaces[Face.S3] = faces[Face.RIGHT].Subfaces[Face.S3];
					
					faces[Face.RIGHT].Subfaces[Face.S1] = faces[Face.BACK].Subfaces[Face.S1];
					faces[Face.RIGHT].Subfaces[Face.S2] = faces[Face.BACK].Subfaces[Face.S2];
					faces[Face.RIGHT].Subfaces[Face.S3] = faces[Face.BACK].Subfaces[Face.S3];
					
					faces[Face.BACK].Subfaces[Face.S1] = faces[Face.LEFT].Subfaces[Face.S1];
					faces[Face.BACK].Subfaces[Face.S2] = faces[Face.LEFT].Subfaces[Face.S2];
					faces[Face.BACK].Subfaces[Face.S3] = faces[Face.LEFT].Subfaces[Face.S3];
					
					faces[Face.LEFT].Subfaces[Face.S1] = temp[0];
					faces[Face.LEFT].Subfaces[Face.S2] = temp[1];
					faces[Face.LEFT].Subfaces[Face.S3] = temp[2];
				}
				else if(RowId == 1)
				{
					temp[0] = faces[Face.FRONT].Subfaces[Face.E1];
					temp[1] = faces[Face.FRONT].Subfaces[Face.E2];
					temp[2] = faces[Face.FRONT].Subfaces[Face.E3];
					
					faces[Face.FRONT].Subfaces[Face.E1] = faces[Face.RIGHT].Subfaces[Face.E1];
					faces[Face.FRONT].Subfaces[Face.E2] = faces[Face.RIGHT].Subfaces[Face.E2];
					faces[Face.FRONT].Subfaces[Face.E3] = faces[Face.RIGHT].Subfaces[Face.E3];
					
					faces[Face.RIGHT].Subfaces[Face.E1] = faces[Face.BACK].Subfaces[Face.E1];
					faces[Face.RIGHT].Subfaces[Face.E2] = faces[Face.BACK].Subfaces[Face.E2];
					faces[Face.RIGHT].Subfaces[Face.E3] = faces[Face.BACK].Subfaces[Face.E3];
					
					faces[Face.BACK].Subfaces[Face.E1] = faces[Face.LEFT].Subfaces[Face.E1];
					faces[Face.BACK].Subfaces[Face.E2] = faces[Face.LEFT].Subfaces[Face.E2];
					faces[Face.BACK].Subfaces[Face.E3] = faces[Face.LEFT].Subfaces[Face.E3];
					
					faces[Face.LEFT].Subfaces[Face.E1] = temp[0];
					faces[Face.LEFT].Subfaces[Face.E2] = temp[1];
					faces[Face.LEFT].Subfaces[Face.E3] = temp[2];
				}
				else if(RowId == 2)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.BOTTOM].Subfaces[i];
					}
					
					faces[Face.BOTTOM].Subfaces[Face.Y1] = temp[Face.S1];
					faces[Face.BOTTOM].Subfaces[Face.Y2] = temp[Face.E1];
					faces[Face.BOTTOM].Subfaces[Face.Y3] = temp[Face.Y1];
					faces[Face.BOTTOM].Subfaces[Face.E1] = temp[Face.S2];
					faces[Face.BOTTOM].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.BOTTOM].Subfaces[Face.E3] = temp[Face.Y2];
					faces[Face.BOTTOM].Subfaces[Face.S1] = temp[Face.S3];
					faces[Face.BOTTOM].Subfaces[Face.S2] = temp[Face.E3];
					faces[Face.BOTTOM].Subfaces[Face.S3] = temp[Face.Y3];
					
					temp[0] = faces[Face.FRONT].Subfaces[Face.Y1];
					temp[1] = faces[Face.FRONT].Subfaces[Face.Y2];
					temp[2] = faces[Face.FRONT].Subfaces[Face.Y3];
					
					faces[Face.FRONT].Subfaces[Face.Y1] = faces[Face.RIGHT].Subfaces[Face.Y1];
					faces[Face.FRONT].Subfaces[Face.Y2] = faces[Face.RIGHT].Subfaces[Face.Y2];
					faces[Face.FRONT].Subfaces[Face.Y3] = faces[Face.RIGHT].Subfaces[Face.Y3];
					
					faces[Face.RIGHT].Subfaces[Face.Y1] = faces[Face.BACK].Subfaces[Face.Y1];
					faces[Face.RIGHT].Subfaces[Face.Y2] = faces[Face.BACK].Subfaces[Face.Y2];
					faces[Face.RIGHT].Subfaces[Face.Y3] = faces[Face.BACK].Subfaces[Face.Y3];
					
					faces[Face.BACK].Subfaces[Face.Y1] = faces[Face.LEFT].Subfaces[Face.Y1];
					faces[Face.BACK].Subfaces[Face.Y2] = faces[Face.LEFT].Subfaces[Face.Y2];
					faces[Face.BACK].Subfaces[Face.Y3] = faces[Face.LEFT].Subfaces[Face.Y3];
					
					faces[Face.LEFT].Subfaces[Face.Y1] = temp[0];
					faces[Face.LEFT].Subfaces[Face.Y2] = temp[1];
					faces[Face.LEFT].Subfaces[Face.Y3] = temp[2];
				}
			}
		}
	}
	
	public void RotateCol(int RowId, int Direction, float angle, boolean IfSetMap)
	{
		if(Direction==Cube.CounterClockWise)
		{
			for(int i=0;i<=18;i+=9)
			{
				cubes[cubei[0+RowId+i]].Rotate(Cube.XAxis, -angle);
				cubes[cubei[3+RowId+i]].Rotate(Cube.XAxis, -angle);
				cubes[cubei[6+RowId+i]].Rotate(Cube.XAxis, -angle);
			}
			if( IfSetMap )
			{
				int[] temp = new int[9];
				temp[0] = cubei[RowId+24];	
				temp[1] = cubei[RowId+21];	
				temp[2] = cubei[RowId+18];	
				temp[3] = cubei[RowId+15];	
				temp[4] = cubei[RowId+12];	
				temp[5] = cubei[RowId+9];	
				temp[6] = cubei[RowId+6];	
				temp[7] = cubei[RowId+3];	
				temp[8] = cubei[RowId+0];	
				cubei[RowId+24] = temp[6];	
				cubei[RowId+21] = temp[3];	
				cubei[RowId+18] = temp[0];	
				cubei[RowId+15] = temp[7];	
				cubei[RowId+12] = temp[4];	
				cubei[RowId+9] = temp[1];	
				cubei[RowId+6] = temp[8];	
				cubei[RowId+3] = temp[5];	
				cubei[RowId+0] = temp[2];
				
				//sovle the changing color of each subfaces
				if(RowId == 2)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.RIGHT].Subfaces[i];
					}
					
					faces[Face.RIGHT].Subfaces[Face.Y1] = temp[Face.Y3];
					faces[Face.RIGHT].Subfaces[Face.Y2] = temp[Face.E3];
					faces[Face.RIGHT].Subfaces[Face.Y3] = temp[Face.S3];
					faces[Face.RIGHT].Subfaces[Face.E1] = temp[Face.Y2];
					faces[Face.RIGHT].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.RIGHT].Subfaces[Face.E3] = temp[Face.S2];
					faces[Face.RIGHT].Subfaces[Face.S1] = temp[Face.Y1];
					faces[Face.RIGHT].Subfaces[Face.S2] = temp[Face.E1];
					faces[Face.RIGHT].Subfaces[Face.S3] = temp[Face.S1];
					
					temp[0] = faces[Face.FRONT].Subfaces[Face.Y3];
					temp[1] = faces[Face.FRONT].Subfaces[Face.E3];
					temp[2] = faces[Face.FRONT].Subfaces[Face.S3];
					
					faces[Face.FRONT].Subfaces[Face.Y3] = faces[Face.BOTTOM].Subfaces[Face.Y3];
					faces[Face.FRONT].Subfaces[Face.E3] = faces[Face.BOTTOM].Subfaces[Face.E3];
					faces[Face.FRONT].Subfaces[Face.S3] = faces[Face.BOTTOM].Subfaces[Face.S3];
					
					faces[Face.BOTTOM].Subfaces[Face.Y3] = faces[Face.BACK].Subfaces[Face.S1];
					faces[Face.BOTTOM].Subfaces[Face.E3] = faces[Face.BACK].Subfaces[Face.E1];
					faces[Face.BOTTOM].Subfaces[Face.S3] = faces[Face.BACK].Subfaces[Face.Y1];
					
					faces[Face.BACK].Subfaces[Face.Y1] = faces[Face.TOP].Subfaces[Face.S3];
					faces[Face.BACK].Subfaces[Face.E1] = faces[Face.TOP].Subfaces[Face.E3];
					faces[Face.BACK].Subfaces[Face.S1] = faces[Face.TOP].Subfaces[Face.Y3];
					
					faces[Face.TOP].Subfaces[Face.Y3] = temp[0];
					faces[Face.TOP].Subfaces[Face.E3] = temp[1];
					faces[Face.TOP].Subfaces[Face.S3] = temp[2];
				}
				else if(RowId == 1)
				{
					temp[0] = faces[Face.FRONT].Subfaces[Face.Y2];
					temp[1] = faces[Face.FRONT].Subfaces[Face.E2];
					temp[2] = faces[Face.FRONT].Subfaces[Face.S2];
					
					faces[Face.FRONT].Subfaces[Face.Y2] = faces[Face.BOTTOM].Subfaces[Face.Y2];
					faces[Face.FRONT].Subfaces[Face.E2] = faces[Face.BOTTOM].Subfaces[Face.E2];
					faces[Face.FRONT].Subfaces[Face.S2] = faces[Face.BOTTOM].Subfaces[Face.S2];
					
					faces[Face.BOTTOM].Subfaces[Face.Y2] = faces[Face.BACK].Subfaces[Face.S2];
					faces[Face.BOTTOM].Subfaces[Face.E2] = faces[Face.BACK].Subfaces[Face.E2];
					faces[Face.BOTTOM].Subfaces[Face.S2] = faces[Face.BACK].Subfaces[Face.Y2];
					
					faces[Face.BACK].Subfaces[Face.Y2] = faces[Face.TOP].Subfaces[Face.S2];
					faces[Face.BACK].Subfaces[Face.E2] = faces[Face.TOP].Subfaces[Face.E2];
					faces[Face.BACK].Subfaces[Face.S2] = faces[Face.TOP].Subfaces[Face.Y2];
					
					faces[Face.TOP].Subfaces[Face.Y2] = temp[0];
					faces[Face.TOP].Subfaces[Face.E2] = temp[1];
					faces[Face.TOP].Subfaces[Face.S2] = temp[2];
				}
				else if(RowId == 0)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.LEFT].Subfaces[i];
					}
					
					faces[Face.LEFT].Subfaces[Face.Y1] = temp[Face.S1];
					faces[Face.LEFT].Subfaces[Face.Y2] = temp[Face.E1];
					faces[Face.LEFT].Subfaces[Face.Y3] = temp[Face.Y1];
					faces[Face.LEFT].Subfaces[Face.E1] = temp[Face.S2];
					faces[Face.LEFT].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.LEFT].Subfaces[Face.E3] = temp[Face.Y2];
					faces[Face.LEFT].Subfaces[Face.S1] = temp[Face.S3];
					faces[Face.LEFT].Subfaces[Face.S2] = temp[Face.E3];
					faces[Face.LEFT].Subfaces[Face.S3] = temp[Face.Y3];
					
					temp[0] = faces[Face.FRONT].Subfaces[Face.Y1];
					temp[1] = faces[Face.FRONT].Subfaces[Face.E1];
					temp[2] = faces[Face.FRONT].Subfaces[Face.S1];
					
					faces[Face.FRONT].Subfaces[Face.Y1] = faces[Face.BOTTOM].Subfaces[Face.Y1];
					faces[Face.FRONT].Subfaces[Face.E1] = faces[Face.BOTTOM].Subfaces[Face.E1];
					faces[Face.FRONT].Subfaces[Face.S1] = faces[Face.BOTTOM].Subfaces[Face.S1];
					
					faces[Face.BOTTOM].Subfaces[Face.Y1] = faces[Face.BACK].Subfaces[Face.S3];
					faces[Face.BOTTOM].Subfaces[Face.E1] = faces[Face.BACK].Subfaces[Face.E3];
					faces[Face.BOTTOM].Subfaces[Face.S1] = faces[Face.BACK].Subfaces[Face.Y3];
					
					faces[Face.BACK].Subfaces[Face.Y3] = faces[Face.TOP].Subfaces[Face.S1];
					faces[Face.BACK].Subfaces[Face.E3] = faces[Face.TOP].Subfaces[Face.E1];
					faces[Face.BACK].Subfaces[Face.S3] = faces[Face.TOP].Subfaces[Face.Y1];
					
					faces[Face.TOP].Subfaces[Face.Y1] = temp[0];
					faces[Face.TOP].Subfaces[Face.E1] = temp[1];
					faces[Face.TOP].Subfaces[Face.S1] = temp[2];
				}
			}
		}
		else
		{
			for(int i=0;i<=18;i+=9)
			{
				cubes[cubei[0+RowId+i]].Rotate(Cube.XAxis,angle);
				cubes[cubei[3+RowId+i]].Rotate(Cube.XAxis,angle);
				cubes[cubei[6+RowId+i]].Rotate(Cube.XAxis,angle);
			}
			if(IfSetMap)
			{
				int[] temp = new int[9];
				temp[0] = cubei[RowId+24];	
				temp[1] = cubei[RowId+21];	
				temp[2] = cubei[RowId+18];	
				temp[3] = cubei[RowId+15];	
				temp[4] = cubei[RowId+12];	
				temp[5] = cubei[RowId+9];	
				temp[6] = cubei[RowId+6];	
				temp[7] = cubei[RowId+3];	
				temp[8] = cubei[RowId+0];	
				cubei[RowId+24] = temp[2];	
				cubei[RowId+21] = temp[5];	
				cubei[RowId+18] = temp[8];	
				cubei[RowId+15] = temp[1];	
				cubei[RowId+12] = temp[4];	
				cubei[RowId+9] = temp[7];	
				cubei[RowId+6] = temp[0];	
				cubei[RowId+3] = temp[3];	
				cubei[RowId+0] = temp[6];	
				
				if(RowId == 2)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.RIGHT].Subfaces[i];
					}
					
					faces[Face.RIGHT].Subfaces[Face.Y1] = temp[Face.S1];
					faces[Face.RIGHT].Subfaces[Face.Y2] = temp[Face.E1];
					faces[Face.RIGHT].Subfaces[Face.Y3] = temp[Face.Y1];
					faces[Face.RIGHT].Subfaces[Face.E1] = temp[Face.S2];
					faces[Face.RIGHT].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.RIGHT].Subfaces[Face.E3] = temp[Face.Y2];
					faces[Face.RIGHT].Subfaces[Face.S1] = temp[Face.S3];
					faces[Face.RIGHT].Subfaces[Face.S2] = temp[Face.E3];
					faces[Face.RIGHT].Subfaces[Face.S3] = temp[Face.Y3];
					
					temp[0] = faces[Face.FRONT].Subfaces[Face.Y3];
					temp[1] = faces[Face.FRONT].Subfaces[Face.E3];
					temp[2] = faces[Face.FRONT].Subfaces[Face.S3];
					
					faces[Face.FRONT].Subfaces[Face.Y3] = faces[Face.TOP].Subfaces[Face.Y3];
					faces[Face.FRONT].Subfaces[Face.E3] = faces[Face.TOP].Subfaces[Face.E3];
					faces[Face.FRONT].Subfaces[Face.S3] = faces[Face.TOP].Subfaces[Face.S3];
					
					faces[Face.TOP].Subfaces[Face.Y3] = faces[Face.BACK].Subfaces[Face.S1];
					faces[Face.TOP].Subfaces[Face.E3] = faces[Face.BACK].Subfaces[Face.E1];
					faces[Face.TOP].Subfaces[Face.S3] = faces[Face.BACK].Subfaces[Face.Y1];
					
					faces[Face.BACK].Subfaces[Face.Y1] = faces[Face.BOTTOM].Subfaces[Face.S3];
					faces[Face.BACK].Subfaces[Face.E1] = faces[Face.BOTTOM].Subfaces[Face.E3];
					faces[Face.BACK].Subfaces[Face.S1] = faces[Face.BOTTOM].Subfaces[Face.Y3];
					
					faces[Face.BOTTOM].Subfaces[Face.Y3] = temp[0];
					faces[Face.BOTTOM].Subfaces[Face.E3] = temp[1];
					faces[Face.BOTTOM].Subfaces[Face.S3] = temp[2];
				}
				else if(RowId == 1)
				{
					temp[0] = faces[Face.FRONT].Subfaces[Face.Y2];
					temp[1] = faces[Face.FRONT].Subfaces[Face.E2];
					temp[2] = faces[Face.FRONT].Subfaces[Face.S2];
					
					faces[Face.FRONT].Subfaces[Face.Y2] = faces[Face.TOP].Subfaces[Face.Y2];
					faces[Face.FRONT].Subfaces[Face.E2] = faces[Face.TOP].Subfaces[Face.E2];
					faces[Face.FRONT].Subfaces[Face.S2] = faces[Face.TOP].Subfaces[Face.S2];
					
					faces[Face.TOP].Subfaces[Face.Y2] = faces[Face.BACK].Subfaces[Face.S2];
					faces[Face.TOP].Subfaces[Face.E2] = faces[Face.BACK].Subfaces[Face.E2];
					faces[Face.TOP].Subfaces[Face.S2] = faces[Face.BACK].Subfaces[Face.Y2];
					
					faces[Face.BACK].Subfaces[Face.Y2] = faces[Face.BOTTOM].Subfaces[Face.S2];
					faces[Face.BACK].Subfaces[Face.E2] = faces[Face.BOTTOM].Subfaces[Face.E2];
					faces[Face.BACK].Subfaces[Face.S2] = faces[Face.BOTTOM].Subfaces[Face.Y2];
					
					faces[Face.BOTTOM].Subfaces[Face.Y2] = temp[0];
					faces[Face.BOTTOM].Subfaces[Face.E2] = temp[1];
					faces[Face.BOTTOM].Subfaces[Face.S2] = temp[2];
				}
				else if(RowId == 0)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.LEFT].Subfaces[i];
					}
					
					faces[Face.LEFT].Subfaces[Face.Y1] = temp[Face.Y3];
					faces[Face.LEFT].Subfaces[Face.Y2] = temp[Face.E3];
					faces[Face.LEFT].Subfaces[Face.Y3] = temp[Face.S3];
					faces[Face.LEFT].Subfaces[Face.E1] = temp[Face.Y2];
					faces[Face.LEFT].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.LEFT].Subfaces[Face.E3] = temp[Face.S2];
					faces[Face.LEFT].Subfaces[Face.S1] = temp[Face.Y1];
					faces[Face.LEFT].Subfaces[Face.S2] = temp[Face.E1];
					faces[Face.LEFT].Subfaces[Face.S3] = temp[Face.S1];
					
					temp[0] = faces[Face.FRONT].Subfaces[Face.Y1];
					temp[1] = faces[Face.FRONT].Subfaces[Face.E1];
					temp[2] = faces[Face.FRONT].Subfaces[Face.S1];
					
					faces[Face.FRONT].Subfaces[Face.Y1] = faces[Face.TOP].Subfaces[Face.Y1];
					faces[Face.FRONT].Subfaces[Face.E1] = faces[Face.TOP].Subfaces[Face.E1];
					faces[Face.FRONT].Subfaces[Face.S1] = faces[Face.TOP].Subfaces[Face.S1];
					
					faces[Face.TOP].Subfaces[Face.Y1] = faces[Face.BACK].Subfaces[Face.S3];
					faces[Face.TOP].Subfaces[Face.E1] = faces[Face.BACK].Subfaces[Face.E3];
					faces[Face.TOP].Subfaces[Face.S1] = faces[Face.BACK].Subfaces[Face.Y3];
					
					faces[Face.BACK].Subfaces[Face.Y3] = faces[Face.BOTTOM].Subfaces[Face.S1];
					faces[Face.BACK].Subfaces[Face.E3] = faces[Face.BOTTOM].Subfaces[Face.E1];
					faces[Face.BACK].Subfaces[Face.S3] = faces[Face.BOTTOM].Subfaces[Face.Y1];
					
					faces[Face.BOTTOM].Subfaces[Face.Y1] = temp[0];
					faces[Face.BOTTOM].Subfaces[Face.E1] = temp[1];
					faces[Face.BOTTOM].Subfaces[Face.S1] = temp[2];
				}	
			}
		}
	}

	public void RotateFace(int RowId, int Direction, float angle, boolean IfSetMap)	//near middle far
	{
		if(Direction==Cube.CounterClockWise)
		{
			for(int i=0;i<=18;i+=9)
			{
				cubes[cubei[RowId*3+0+i]].Rotate(Cube.ZAxis,-angle);
				cubes[cubei[RowId*3+1+i]].Rotate(Cube.ZAxis,-angle);
				cubes[cubei[RowId*3+2+i]].Rotate(Cube.ZAxis,-angle);
			}
			if(IfSetMap)
			{
				int[] temp = new int[9];
				temp[0] = cubei[RowId*3+0];	
				temp[1] = cubei[RowId*3+1];	
				temp[2] = cubei[RowId*3+2];	
				temp[3] = cubei[RowId*3+9];	
				temp[4] = cubei[RowId*3+10];	
				temp[5] = cubei[RowId*3+11];	
				temp[6] = cubei[RowId*3+18];	
				temp[7] = cubei[RowId*3+19];	
				temp[8] = cubei[RowId*3+20];	
				cubei[RowId*3+0] = temp[6];	
				cubei[RowId*3+1] = temp[3];	
				cubei[RowId*3+2] = temp[0];	
				cubei[RowId*3+9] = temp[7];	
				cubei[RowId*3+10] = temp[4];	
				cubei[RowId*3+11] = temp[1];	
				cubei[RowId*3+18] = temp[8];	
				cubei[RowId*3+19] = temp[5];	
				cubei[RowId*3+20] = temp[2];
				
				//sovle the changing color of each subfaces
				if(RowId == 2)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.BACK].Subfaces[i];
					}
					
					faces[Face.BACK].Subfaces[Face.Y1] = temp[Face.S1];
					faces[Face.BACK].Subfaces[Face.Y2] = temp[Face.E1];
					faces[Face.BACK].Subfaces[Face.Y3] = temp[Face.Y1];
					faces[Face.BACK].Subfaces[Face.E1] = temp[Face.S2];
					faces[Face.BACK].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.BACK].Subfaces[Face.E3] = temp[Face.Y2];
					faces[Face.BACK].Subfaces[Face.S1] = temp[Face.S3];
					faces[Face.BACK].Subfaces[Face.S2] = temp[Face.E3];
					faces[Face.BACK].Subfaces[Face.S3] = temp[Face.Y3];
					
					temp[0] = faces[Face.LEFT].Subfaces[Face.Y1];
					temp[1] = faces[Face.LEFT].Subfaces[Face.E1];
					temp[2] = faces[Face.LEFT].Subfaces[Face.S1];
					
					faces[Face.LEFT].Subfaces[Face.Y1] = faces[Face.BOTTOM].Subfaces[Face.Y3];
					faces[Face.LEFT].Subfaces[Face.E1] = faces[Face.BOTTOM].Subfaces[Face.Y2];
					faces[Face.LEFT].Subfaces[Face.S1] = faces[Face.BOTTOM].Subfaces[Face.Y1];
					
					faces[Face.BOTTOM].Subfaces[Face.Y1] = faces[Face.RIGHT].Subfaces[Face.Y3];
					faces[Face.BOTTOM].Subfaces[Face.Y2] = faces[Face.RIGHT].Subfaces[Face.E3];
					faces[Face.BOTTOM].Subfaces[Face.Y3] = faces[Face.RIGHT].Subfaces[Face.S3];
					
					faces[Face.RIGHT].Subfaces[Face.Y3] = faces[Face.TOP].Subfaces[Face.S3];
					faces[Face.RIGHT].Subfaces[Face.E3] = faces[Face.TOP].Subfaces[Face.S2];
					faces[Face.RIGHT].Subfaces[Face.S3] = faces[Face.TOP].Subfaces[Face.S1];
					
					faces[Face.TOP].Subfaces[Face.S1] = temp[0];
					faces[Face.TOP].Subfaces[Face.S2] = temp[1];
					faces[Face.TOP].Subfaces[Face.S3] = temp[2];
				}
				else if(RowId == 1)
				{
					temp[0] = faces[Face.LEFT].Subfaces[Face.Y2];
					temp[1] = faces[Face.LEFT].Subfaces[Face.E2];
					temp[2] = faces[Face.LEFT].Subfaces[Face.S2];
					
					faces[Face.LEFT].Subfaces[Face.Y2] = faces[Face.BOTTOM].Subfaces[Face.E3];
					faces[Face.LEFT].Subfaces[Face.E2] = faces[Face.BOTTOM].Subfaces[Face.E2];
					faces[Face.LEFT].Subfaces[Face.S2] = faces[Face.BOTTOM].Subfaces[Face.E1];
					
					faces[Face.BOTTOM].Subfaces[Face.E1] = faces[Face.RIGHT].Subfaces[Face.Y2];
					faces[Face.BOTTOM].Subfaces[Face.E2] = faces[Face.RIGHT].Subfaces[Face.E2];
					faces[Face.BOTTOM].Subfaces[Face.E3] = faces[Face.RIGHT].Subfaces[Face.S2];
					
					faces[Face.RIGHT].Subfaces[Face.Y2] = faces[Face.TOP].Subfaces[Face.E3];
					faces[Face.RIGHT].Subfaces[Face.E2] = faces[Face.TOP].Subfaces[Face.E2];
					faces[Face.RIGHT].Subfaces[Face.S2] = faces[Face.TOP].Subfaces[Face.E1];
					
					faces[Face.TOP].Subfaces[Face.E1] = temp[0];
					faces[Face.TOP].Subfaces[Face.E2] = temp[1];
					faces[Face.TOP].Subfaces[Face.E3] = temp[2];
				}
				else if(RowId == 0)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.FRONT].Subfaces[i];
					}
					
					faces[Face.FRONT].Subfaces[Face.Y1] = temp[Face.Y3];
					faces[Face.FRONT].Subfaces[Face.Y2] = temp[Face.E3];
					faces[Face.FRONT].Subfaces[Face.Y3] = temp[Face.S3];
					faces[Face.FRONT].Subfaces[Face.E1] = temp[Face.Y2];
					faces[Face.FRONT].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.FRONT].Subfaces[Face.E3] = temp[Face.S2];
					faces[Face.FRONT].Subfaces[Face.S1] = temp[Face.Y1];
					faces[Face.FRONT].Subfaces[Face.S2] = temp[Face.E1];
					faces[Face.FRONT].Subfaces[Face.S3] = temp[Face.S1];
					
					temp[0] = faces[Face.LEFT].Subfaces[Face.Y3];
					temp[1] = faces[Face.LEFT].Subfaces[Face.E3];
					temp[2] = faces[Face.LEFT].Subfaces[Face.S3];
					
					faces[Face.LEFT].Subfaces[Face.Y3] = faces[Face.BOTTOM].Subfaces[Face.S3];
					faces[Face.LEFT].Subfaces[Face.E3] = faces[Face.BOTTOM].Subfaces[Face.S2];
					faces[Face.LEFT].Subfaces[Face.S3] = faces[Face.BOTTOM].Subfaces[Face.S1];
					
					faces[Face.BOTTOM].Subfaces[Face.S1] = faces[Face.RIGHT].Subfaces[Face.Y1];
					faces[Face.BOTTOM].Subfaces[Face.S2] = faces[Face.RIGHT].Subfaces[Face.E1];
					faces[Face.BOTTOM].Subfaces[Face.S3] = faces[Face.RIGHT].Subfaces[Face.S1];
					
					faces[Face.RIGHT].Subfaces[Face.Y1] = faces[Face.TOP].Subfaces[Face.Y3];
					faces[Face.RIGHT].Subfaces[Face.E1] = faces[Face.TOP].Subfaces[Face.Y2];
					faces[Face.RIGHT].Subfaces[Face.S1] = faces[Face.TOP].Subfaces[Face.Y1];
					
					faces[Face.TOP].Subfaces[Face.Y1] = temp[0];
					faces[Face.TOP].Subfaces[Face.Y2] = temp[1];
					faces[Face.TOP].Subfaces[Face.Y3] = temp[2];
				}
			}
		}
		else if(Direction==Cube.ClockWise)
		{
			for(int i=0;i<=18;i+=9)
			{
				cubes[cubei[RowId*3+0+i]].Rotate(Cube.ZAxis,angle);
				cubes[cubei[RowId*3+1+i]].Rotate(Cube.ZAxis,angle);
				cubes[cubei[RowId*3+2+i]].Rotate(Cube.ZAxis,angle);
			}
			if(IfSetMap)
			{
				int[] temp = new int[9];
				temp[0] = cubei[RowId*3+0];	
				temp[1] = cubei[RowId*3+1];	
				temp[2] = cubei[RowId*3+2];	
				temp[3] = cubei[RowId*3+9];	
				temp[4] = cubei[RowId*3+10];	
				temp[5] = cubei[RowId*3+11];	
				temp[6] = cubei[RowId*3+18];	
				temp[7] = cubei[RowId*3+19];	
				temp[8] = cubei[RowId*3+20];	
				cubei[RowId*3+0] = temp[2];	
				cubei[RowId*3+1] = temp[5];	
				cubei[RowId*3+2] = temp[8];	
				cubei[RowId*3+9] = temp[1];	
				cubei[RowId*3+10] = temp[4];	
				cubei[RowId*3+11] = temp[7];	
				cubei[RowId*3+18] = temp[0];	
				cubei[RowId*3+19] = temp[3];	
				cubei[RowId*3+20] = temp[6];
				
				//sovle the changing color of each subfaces
				if(RowId == 2)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.BACK].Subfaces[i];
					}
					
					faces[Face.BACK].Subfaces[Face.Y1] = temp[Face.Y3];
					faces[Face.BACK].Subfaces[Face.Y2] = temp[Face.E3];
					faces[Face.BACK].Subfaces[Face.Y3] = temp[Face.S3];
					faces[Face.BACK].Subfaces[Face.E1] = temp[Face.Y2];
					faces[Face.BACK].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.BACK].Subfaces[Face.E3] = temp[Face.S2];
					faces[Face.BACK].Subfaces[Face.S1] = temp[Face.Y1];
					faces[Face.BACK].Subfaces[Face.S2] = temp[Face.E1];
					faces[Face.BACK].Subfaces[Face.S3] = temp[Face.S1];
					
					temp[0] = faces[Face.LEFT].Subfaces[Face.Y1];
					temp[1] = faces[Face.LEFT].Subfaces[Face.E1];
					temp[2] = faces[Face.LEFT].Subfaces[Face.S1];
					
					faces[Face.LEFT].Subfaces[Face.Y1] = faces[Face.TOP].Subfaces[Face.S1];
					faces[Face.LEFT].Subfaces[Face.E1] = faces[Face.TOP].Subfaces[Face.S2];
					faces[Face.LEFT].Subfaces[Face.S1] = faces[Face.TOP].Subfaces[Face.S3];
					
					faces[Face.TOP].Subfaces[Face.S1] = faces[Face.RIGHT].Subfaces[Face.S3];
					faces[Face.TOP].Subfaces[Face.S2] = faces[Face.RIGHT].Subfaces[Face.E3];
					faces[Face.TOP].Subfaces[Face.S3] = faces[Face.RIGHT].Subfaces[Face.Y3];
					
					faces[Face.RIGHT].Subfaces[Face.Y3] = faces[Face.BOTTOM].Subfaces[Face.Y1];
					faces[Face.RIGHT].Subfaces[Face.E3] = faces[Face.BOTTOM].Subfaces[Face.Y2];
					faces[Face.RIGHT].Subfaces[Face.S3] = faces[Face.BOTTOM].Subfaces[Face.Y3];
					
					faces[Face.BOTTOM].Subfaces[Face.Y1] = temp[2];
					faces[Face.BOTTOM].Subfaces[Face.Y2] = temp[1];
					faces[Face.BOTTOM].Subfaces[Face.Y3] = temp[0];
				}
				else if(RowId == 1)
				{
					temp[0] = faces[Face.LEFT].Subfaces[Face.Y2];
					temp[1] = faces[Face.LEFT].Subfaces[Face.E2];
					temp[2] = faces[Face.LEFT].Subfaces[Face.S2];
					
					faces[Face.LEFT].Subfaces[Face.Y2] = faces[Face.TOP].Subfaces[Face.E1];
					faces[Face.LEFT].Subfaces[Face.E2] = faces[Face.TOP].Subfaces[Face.E2];
					faces[Face.LEFT].Subfaces[Face.S2] = faces[Face.TOP].Subfaces[Face.E3];
					
					faces[Face.TOP].Subfaces[Face.E1] = faces[Face.RIGHT].Subfaces[Face.S2];
					faces[Face.TOP].Subfaces[Face.E2] = faces[Face.RIGHT].Subfaces[Face.E2];
					faces[Face.TOP].Subfaces[Face.E3] = faces[Face.RIGHT].Subfaces[Face.Y2];
					
					faces[Face.RIGHT].Subfaces[Face.Y2] = faces[Face.BOTTOM].Subfaces[Face.E1];
					faces[Face.RIGHT].Subfaces[Face.E2] = faces[Face.BOTTOM].Subfaces[Face.E2];
					faces[Face.RIGHT].Subfaces[Face.S2] = faces[Face.BOTTOM].Subfaces[Face.E3];
					
					faces[Face.BOTTOM].Subfaces[Face.E1] = temp[2];
					faces[Face.BOTTOM].Subfaces[Face.E2] = temp[1];
					faces[Face.BOTTOM].Subfaces[Face.E3] = temp[0];
				}
				else if(RowId == 0)
				{
					for(int i=0; i<9; i++)
					{
						temp[i] = faces[Face.FRONT].Subfaces[i];
					}
					
					faces[Face.FRONT].Subfaces[Face.Y1] = temp[Face.S1];
					faces[Face.FRONT].Subfaces[Face.Y2] = temp[Face.E1];
					faces[Face.FRONT].Subfaces[Face.Y3] = temp[Face.Y1];
					faces[Face.FRONT].Subfaces[Face.E1] = temp[Face.S2];
					faces[Face.FRONT].Subfaces[Face.E2] = temp[Face.E2];
					faces[Face.FRONT].Subfaces[Face.E3] = temp[Face.Y2];
					faces[Face.FRONT].Subfaces[Face.S1] = temp[Face.S3];
					faces[Face.FRONT].Subfaces[Face.S2] = temp[Face.E3];
					faces[Face.FRONT].Subfaces[Face.S3] = temp[Face.Y3];
					
					temp[0] = faces[Face.LEFT].Subfaces[Face.Y3];
					temp[1] = faces[Face.LEFT].Subfaces[Face.E3];
					temp[2] = faces[Face.LEFT].Subfaces[Face.S3];
					
					faces[Face.LEFT].Subfaces[Face.Y3] = faces[Face.TOP].Subfaces[Face.Y1];
					faces[Face.LEFT].Subfaces[Face.E3] = faces[Face.TOP].Subfaces[Face.Y2];
					faces[Face.LEFT].Subfaces[Face.S3] = faces[Face.TOP].Subfaces[Face.Y3];
					
					faces[Face.TOP].Subfaces[Face.Y1] = faces[Face.RIGHT].Subfaces[Face.S1];
					faces[Face.TOP].Subfaces[Face.Y2] = faces[Face.RIGHT].Subfaces[Face.E1];
					faces[Face.TOP].Subfaces[Face.Y3] = faces[Face.RIGHT].Subfaces[Face.Y1];
					
					faces[Face.RIGHT].Subfaces[Face.Y1] = faces[Face.BOTTOM].Subfaces[Face.S1];
					faces[Face.RIGHT].Subfaces[Face.E1] = faces[Face.BOTTOM].Subfaces[Face.S2];
					faces[Face.RIGHT].Subfaces[Face.S1] = faces[Face.BOTTOM].Subfaces[Face.S3];
					
					faces[Face.BOTTOM].Subfaces[Face.S1] = temp[2];
					faces[Face.BOTTOM].Subfaces[Face.S2] = temp[1];
					faces[Face.BOTTOM].Subfaces[Face.S3] = temp[0];
				}
			}
		}
	}
	
	public void Draw(GL10 gl)
	{
		
	    gl.glPushMatrix();
	    gl.glRotatef(rx, 1, 0, 0);	//rotate
	    gl.glRotatef(ry, 0, 1, 0);
	    
	   // Log.e("rxry", rx + " " + ry);
	    for(int i=0; i<27; i++)
	    {
	    	this.cubes[i].Draw(gl);
	    }
	  	gl.glPopMatrix();
	  	
/*	    for(int i=0; i<27; i++)
	    {
	    	this.cubes[i].Draw(gl);
	    }*/
	}
	
	public void DrawSimple(GL10 gl)
	{
		
	    gl.glPushMatrix();
	    gl.glRotatef(rx, 1, 0, 0);	//rotate
	    gl.glRotatef(ry, 0, 1, 0);
	    
	   // Log.e("rxry", rx + " " + ry);
	    for(int i=0; i<27; i++)
	    {
	    	this.cubes[i].DrawSimple(gl);
	    }
	  	gl.glPopMatrix();
	  	
/*	    for(int i=0; i<27; i++)
	    {
	    	this.cubes[i].Draw(gl);
	    }*/
	}

	public String GetState()
	{
		String state = "";
		if( faces == null)
		{
			return null;
		}
		
		state += 
				//UF UR UB UL 
				Face.FaceToChar(faces[Face.U].Subfaces[Face.Y2])
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.S2])
				+ " "
				+ Face.FaceToChar(faces[Face.U].Subfaces[Face.E3])
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.S2])
				+ " "
				+ Face.FaceToChar(faces[Face.U].Subfaces[Face.S2])
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.S2])
				+ " "
				+ Face.FaceToChar(faces[Face.U].Subfaces[Face.E1])
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.S2])
				+ " "
				
				//DF DR DB DL 
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.S2])
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.Y2])
				+ " "
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.E3])
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.Y2])
				+ " "
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.Y2])
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.Y2])
				+ " "
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.E1])
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.Y2])
				+ " "
				
				//FR FL BR BL 
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.E3])
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.E1])
				+ " "
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.E1])
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.E3])
				+ " "
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.E1])
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.E3])
				+ " "
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.E3])
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.E1])
				+ " "
				
				//UFR URB UBL ULF
				+ Face.FaceToChar(faces[Face.U].Subfaces[Face.Y3])
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.S3])
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.S1])
				+ " "
				+ Face.FaceToChar(faces[Face.U].Subfaces[Face.S3])
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.S3])
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.S1])
				+ " "
				+ Face.FaceToChar(faces[Face.U].Subfaces[Face.S1])
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.S3])
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.S1])
				+ " "
				+ Face.FaceToChar(faces[Face.U].Subfaces[Face.Y1])
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.S3])
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.S1])
				+ " "
				
				//DRF DFL DLB DBR
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.S3])
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.Y1])
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.Y3])
				+ " "
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.S1])
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.Y1])
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.Y3])
				+ " "
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.Y1])
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.Y1])
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.Y3])
				+ " "
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.Y3])
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.Y1])
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.Y3])
				+ " "
				
				//F B L R U D
				+ Face.FaceToChar(faces[Face.F].Subfaces[Face.E2])
				+ " "
				
				+ Face.FaceToChar(faces[Face.B].Subfaces[Face.E2])
				+ " "
				
				+ Face.FaceToChar(faces[Face.L].Subfaces[Face.E2])
				+ " "
				
				+ Face.FaceToChar(faces[Face.R].Subfaces[Face.E2])
				+ " "
				
				+ Face.FaceToChar(faces[Face.U].Subfaces[Face.E2])
				+ " "
				
				+ Face.FaceToChar(faces[Face.D].Subfaces[Face.E2])
				;
		
		
		return state;
	}
	
	public void Reset()
	{
/*		this.rx = 22.f;
		this.ry = -34.f;*/
		rx = 0.f;
		ry = 0.f;
		
		for(int i=0; i<27; i++)
		{
			cubes[i].Reset();
			cubei[i] = i;
		}
		
	    for( int i=0; i<6; i++)
	    {
	    	faces[i] = new Face(i);
	    }
	}
	
	public String MessUp(int nStep)
	{
		
		 Random random = new Random();
		 String result = "";
		 Command cmd = new Command();
		 for( int i=0; i<nStep; i++)
		 {
			 int RowID, Direction = 0;
			 
			 int r = Math.abs(random.nextInt())%2*2;
			 RowID = r;
			 //Log.e("rowID", RowID+"");
			 
			 r = Math.abs(random.nextInt())%2;
			 switch(r)
			 {
				 case 0: Direction = Cube.ClockWise;
				 case 1: Direction = Cube.CounterClockWise;
			 }
			 
			 r = Math.abs(random.nextInt())%3;
			 switch(r)
			 {
				 case 0: RotateRow(RowID, Direction, 90.f, true); 
				 			cmd = new Command(Command.ROTATE_ROW, RowID, Direction);
				 			break;
				 case 1: RotateCol(RowID, Direction, 90.f, true);
				 			cmd = new Command(Command.ROTATE_COL, RowID, Direction);
				 			break;
				 case 2: RotateFace(RowID, Direction, 90.f, true);
				 			cmd = new Command(Command.ROTATE_FACE, RowID, Direction);
				 			break;
			 }
			 result += cmd + " ";
		 }
		 
		 return result;
	}
	
	public void MessUp(String cmdstr)
	{
		String[] cmdstrs = cmdstr.split(" ");
		Command cmd;
		for(int i=0; i<cmdstrs.length; i++)
		{
			cmd = new Command(cmdstrs[i]);
			int RowID = cmd.RowID;
			int Direction = cmd.Direction;
			switch(cmd.Type)
			{
				case Command.ROTATE_COL: RotateCol(RowID, Direction, 90.f, true); break;
				case Command.ROTATE_FACE: RotateFace(RowID, Direction, 90.f, true); break;
				case Command.ROTATE_ROW: RotateRow(RowID, Direction, 90.f, true); break;
			}
		}
		
		return;
	}
	
	public boolean IsComplete()
	{
		for(int i=0; i<6; i++)
		{
			if( !faces[i].IsSameColor())
			{
				return false;
			}
		}
		return true;
	}
}
