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

import java.util.Vector;

public class Command {
	public int Type;
	public int RowID;
	public int Direction;
	public int N;	//2 for twice rotate
	
	public static final int ROTATE_ROW = 0;
	public static final int ROTATE_COL = 1;
	public static final int ROTATE_FACE = 2;
	
	public static final char rotate_row = '0';
	public static final char rotate_col = '1';
	public static final char rotate_face = '2';
	
	public static final int ClockWise = 0;
	public static final int CounterClockWise = 1;
	
	public static final char clockwise = '0';
	public static final char counterclockwise = '1';
	
	public Command()
	{
		
	}
	
	public Command(int type, int rowID, int direction)
	{
		Type = type;
		RowID = rowID;
		Direction = direction;
		N = 1;
	}
	
	public Command(int type, int rowID, int direction, int n)
	{
		Type = type;
		RowID = rowID;
		Direction = direction;
		N = n;
	}
	
	public Command(String cmdstr)
	{
		switch(cmdstr.charAt(0))
		{
		case rotate_row:Type = ROTATE_ROW;break;
		case rotate_col:Type = ROTATE_COL;break;
		case rotate_face:Type = ROTATE_FACE;break;
		}
		
		switch(cmdstr.charAt(1))
		{
		case '0': RowID = 0;break;
		case '1': RowID = 1;break;
		case '2': RowID = 2;break;
		}
		
		switch(cmdstr.charAt(2))
		{
		case clockwise: Direction = ClockWise;break;
		case counterclockwise: Direction = CounterClockWise;break;
		}
		
		switch(cmdstr.charAt(3))
		{
		case '1': N = 1; break;
		case '2': N = 2; break;
		}
	}
	
	public Command Reverse()
	{
		int dir = Direction==ClockWise? CounterClockWise:ClockWise;
		return new Command(Type, RowID, dir, N);
	}
	
	public static Vector<Command> CmdStrsToCmd(String CmdStr)
	{
		String[] cmdStrs = CmdStr.split(" ");
		Vector<Command> cmds = new Vector<Command>(cmdStrs.length, 1);
		
		if( CmdStr == "" || CmdStr == " " || CmdStr == null)
		{
			return cmds;
		}
		for(int i=0; i<cmdStrs.length; i++)
		{
			cmds.add(new Command(cmdStrs[i]));
		}
		return cmds;
	}
	
	public  String CmdToCmdStr()
	{
		return Type+""+RowID+""+Direction+""+N+"";
	}
	
	@Override
	public String toString()
	{
		return CmdToCmdStr();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj==null)
	        return false;
	    if(this == obj)
	    {
	        return true;
	    }
	    if (obj instanceof Command) {
	    	Command other = (Command) obj;
	        return  other.N == this.N 
	        		&& other.RowID == this.RowID
	        		&& other.Direction == this.Direction
	        		&& other.Type == this.Type;
	    }
	    
	    return false;
	}
	
	public static Command CmdStrToCmd(String cmdstr)
	{
		int type = 0, id = 0, direction = 0, N = 0;
		switch(cmdstr.charAt(0))
		{
		case rotate_row:type = ROTATE_ROW;break;
		case rotate_col:type = ROTATE_COL;break;
		case rotate_face:type = ROTATE_FACE;break;
		}
		
		switch(cmdstr.charAt(1))
		{
		case '0': id = 0;break;
		case '1': id = 1;break;
		case '2': id = 2;break;
		}
		
		switch(cmdstr.charAt(2))
		{
		case clockwise: direction = ClockWise;break;
		case counterclockwise: direction = CounterClockWise;break;
		}
		
		switch(cmdstr.charAt(3))
		{
		case '1': N = 1; break;
		case '2': N = 2; break;
		}
	
		return new Command(type, id, direction, N);
	}
}
