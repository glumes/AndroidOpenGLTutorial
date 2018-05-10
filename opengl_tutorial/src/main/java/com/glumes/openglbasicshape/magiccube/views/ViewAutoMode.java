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
import android.util.AttributeSet;

import com.glumes.openglbasicshape.magiccube.interfaces.OnStateListener;
import com.glumes.openglbasicshape.magiccube.interfaces.OnStepListener;


public class ViewAutoMode extends BasicGameView implements OnStateListener{
	private OnStateListener stateListener = null;
	//private RefreshTime refreshTime;


	public ViewAutoMode(Context context, AttributeSet attr) {
		super(context, attr);
		// TODO Auto-generated constructor stub
	}
	
	public void setOnStateListener(OnStateListener stateListener){
		this.stateListener = stateListener;
	}

	@Override
	public void OnStateChanged(int StateMode) {
		// TODO Auto-generated method stub
		
	}
	
	public void SetStepListener(OnStepListener stepListener)
	{
		this.stepListener = stepListener;
	}

	@Override
	public void OnStateNotify(int StateMode) {
		// TODO Auto-generated method stub
		
	}

}
