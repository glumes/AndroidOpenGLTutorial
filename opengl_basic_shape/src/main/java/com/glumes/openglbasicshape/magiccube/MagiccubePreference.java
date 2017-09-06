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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MagiccubePreference {
	public static final String SHAREDPREFERENCES_NAME = "pref_flex_magiccube";
	public static final String Sensitivity = "pref_flex_magiccube_sensitivity";
	public static final String BgVolume = "pref_flex_magiccube_bgvolume";
	public static final String MoveVolume = "pref_flex_magiccube_movevolume";
	public static final String Difficulty = "pref_flex_magiccube_difficulty";
	public static final String IsShowGuide = "pref_flex_magiccube_isshowguide_2013_10_6_08_50";
	public static final String ObserveTime = "pref_flex_magiccube_observetime";
	public static final String IsSameMessup = "pref_flex_magiccube_issamemessup";
	public static final String ServerOrClient = "pref_flex_magiccube_serverorclient";
	public static final String ServerAddress = "pref_flex_magiccube_serveraddress";
	public static final String MinVibration = "pref_flex_magiccube_minvibration";
	public static final String IsShowThanksList = "pref_flex_magiccube_thankslist";

	public static int GetPreference(String name, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				MagiccubePreference.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		if (name.equals(Sensitivity)) {
			return Math.max(1,
					preferences.getInt(MagiccubePreference.Sensitivity, 50));
		} else if (name.equals(Difficulty)) {
			return preferences.getInt(MagiccubePreference.Difficulty, 50);
		} else if (name.equals(BgVolume)) {
			return GetLinearVolume(preferences.getInt(
					MagiccubePreference.BgVolume, 50));
		} else if (name.equals(MoveVolume)) {
			return GetLinearVolume(preferences.getInt(
					MagiccubePreference.MoveVolume, 50));
		} else if (name.equals(IsShowGuide)) {
			return GetLinearVolume(preferences.getInt(
					MagiccubePreference.IsShowGuide, 1));
		} else if (name.equals(IsSameMessup)) {
			return GetLinearVolume(preferences.getInt(name, 1));
		} else if (name.equals(ObserveTime)) {
			return GetLinearVolume(preferences.getInt(name, 10));
		} else if (name.equals(ServerOrClient)) {
			return preferences.getInt(name, -1);
		} else if (name.equals(MinVibration)) {
			return preferences.getInt(name, 13);
		} else if (name.equals(IsShowThanksList)) {
			return preferences.getInt(name, 1);
		}
		return 0;
	}

	private static int GetLinearVolume(int Volume) {
		return Volume;
	}

	public static void SetPreference(String name, int value, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				MagiccubePreference.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		if (name.equals(Sensitivity)) {
			editor.putInt(name, value);
		} else if (name.equals(Difficulty)) {
			editor.putInt(name, value);
		} else if (name.equals(BgVolume)) {
			editor.putInt(name, value);
		} else if (name.equals(MoveVolume)) {
			editor.putInt(name, value);
		} else if (name.equals(IsShowGuide)) {
			editor.putInt(name, value);
		} else {
			editor.putInt(name, value);
		}
		editor.commit();
	}
}
