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

package com.glumes.openglbasicshape.magiccube.interfaces;

public interface OnStateListener{
	public static final int LOADING = 0;
	public static final int MAINMENULOADED = 65574;
	public static final int LOADED = -1;
	public static final int NONE = -1;
	public static final int BEFOREOBSERVE = 1;
	public static final int OBSERVING = 2;
	public static final int GAMING = 3;
	public static final int QUIT = 4;
	public static final int FINISH = 5;
	public static final int WIN = 6;
	public static final int LOSE = 7;
	
	public static final int CANMOVEFORWARD = 8;
	public static final int CANMOVEBACK = 9;
	public static final int CANNOTMOVEFORWARD = 10;
	public static final int CANNOTMOVEBACK = 11;
	public static final int CANAUTOSOLVE = 12;
	public static final int CANNOTAUTOSOLVE = 13;
	
	public static final int WAITING_FOR_CONNECTION = -2;
	public static final int LEAVING_ON_ERROR = -13;
	
	public void OnStateChanged(int StateMode);
	public void OnStateNotify(int StateMode);
}
