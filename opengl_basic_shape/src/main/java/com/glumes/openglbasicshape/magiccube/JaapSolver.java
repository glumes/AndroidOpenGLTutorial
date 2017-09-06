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

import android.util.Log;


public class JaapSolver extends MagicCubeSolver {
	public JaapSolver()
	{
		AuthorName = "Jaap Scherphuis";
		AverageNStep = 16.04f;
		result = "";
	}
	
	/*there is a little problem with the algorithm, which is that 
	 * the algorithm does not haunt automatically, so we have to add a max-loop-no. 
	 * to haunt the algorithm
	 */
	//UF UR UB UL 
	
	//DF DR DB DL 
	
	//FR FL BR BL 
	
	//UFR URB UBL ULF
	
	//DRF DFL DLB DBR

	//F B L R U D
	
	//char index
	public static final int UF = 0;
	public static final int UR = 3;
	public static final int UB = 6;
	public static final int UL = 9;
	public static final int DF = 12;
	public static final int DR = 15;
	public static final int DB = 18;
	public static final int DL = 21;
	public static final int FR = 24;
	public static final int FL = 27;
	public static final int BR = 30;
	public static final int BL = 33;
	public static final int UFR = 36;
	public static final int URB = 40;
	public static final int UBL = 44;
	public static final int ULF = 48;
	public static final int DRF = 52;
	public static final int DFL = 56;
	public static final int DLB = 60;
	public static final int DBR = 64;
	public static final int F = 68;
	public static final int B = 70;
	public static final int L = 72;
	public static final int R = 74;
	public static final int U = 76;
	public static final int D = 78;

	private int nLoop = 0;
	private static final int maxnloop = 3000000;
	private static final char CHAROFFSET = 65;
	
	private int[] tablesize = {1,4096,  6561,4096,  256,1536,  13824,576};
	private char[][] tables = new char[8][];
	private int[] pos = new int[20];
	private int[] ori = new int[20];
	private int[] val = new int[20];
	private int phase=0;
	int[] move = new int[20];
	int[] moveamount = new int[20];
	private char[] perm= {'A', 'I', 'B', 'J', 'T', 'M', 'R', 'O', 'C', 'L', 'D', 'K', 'S', 
			'N', 'Q', 'P', 'E', 'K', 'F', 'I', 'M', 'S', 'P', 'R', 'G', 'J', 'H', 'L', 'N', 
			'T', 'O', 'Q', 'A', 'G', 'C', 'E', 'M', 'T', 'N', 'S', 'B', 'F', 'D', 'H', 'O',
			'R', 'P', 'Q'};
	private char[] faces = {'R', 'L', 'F', 'B', 'U', 'D'};
	private char[] bithash= {'T', 'd', 'X', 'h', 'Q', 'a', 'R', 'b', 'E', 'F', 'I', 'J', 'U', 'Z', 'f', 'i', 'j', 'e', 'Y', 'V'};

	private char[] order = {'A', 'E', 'C', 'G', 'B', 'F', 'D', 'H', 'I', 'J', 'K', 'L', 'M', 'S', 'N', 'T', 'R', 'O', 'Q', 'P'};

	String result = "";
	@Override
	public String AutoSolve(String InitialState) {

		char[] charindex = {'F', 'B', 'R', 'L', 'U', 'D'};
		int f,i=0,j=0,k=0,pc,mor;
		
		

		InitialState = ResetInitial(InitialState);
		
/*		if( 1==1)
		return result;*/
		//Log.e("init", InitialState);
		
		String[] initialState = InitialState.split(" ");
		
		// initialise tables
		for(; k<20; k++) val[k]=k<12?2:3;
		for(; j<8; j++) filltable(j);
		
		// read input, 20 pieces worth
		for(i=0; i<20; i++){
			f=pc=k=mor=0;
			for(;f<val[i];f++){
				// read input from stdin, or...
				//     do{cin>>c;}while(c==' ');
				//     j=strchr(faces,c)-faces;
				// ...from command line and get face number of facelet
				j=Find(faces,initialState[i].charAt(f));
				// keep track of principal facelet for orientation
				if(j>k) {k=j;mor=f;}
				//construct bit hash code
				pc+= 1<<j;
			}
			// find which cubelet it belongs, i.e. the label for this piece
			for(f=0; f<20; f++)
				if( pc==bithash[f]-64 ) break;
			// store piece
			pos[order[i]-CHAROFFSET]=f;
			ori[order[i]-CHAROFFSET]=mor%val[i];
		}
		
		
		//solve the cube
		// four phases
		for( ; phase<8; phase+=2){
			// try each depth till solved
			nLoop = 0;
			for( j=0; !searchphase(j,0,9); j++);
			if( nLoop > maxnloop)
				break;
			//output result of this phase
			for( i=0; i<j; i++)
				result += ""+ ToCmdStr(charindex[move[i]]+""+moveamount[i]) + " ";
		}
		return result;
	}
	
	private String ToCmdStr(String str)
	{
		//Log.e("str", str);
		String cmdStr = "";
		
		switch(str.charAt(0))
		{
			case 'U': 	cmdStr += Command.rotate_row; cmdStr += "0"; break;
			case 'D':	cmdStr += Command.rotate_row; cmdStr += "2"; break;
			case 'F': 	cmdStr += Command.rotate_face; cmdStr += "0"; break;
			case 'B':	cmdStr += Command.rotate_face; cmdStr += "2"; break;
			case 'L': 	cmdStr += Command.rotate_col; cmdStr += "0"; break;
			case 'R':	cmdStr += Command.rotate_col; cmdStr += "2"; break;	 		
		}
		
		char dir = 'a';
		switch(str.charAt(1))
		{
			case '+': dir = Command.counterclockwise; break;
			case '1': dir = Command.counterclockwise; break;
			case '-': dir = Command.clockwise; break;
			case '3': dir = Command.clockwise; break;
			case '2': dir = Command.counterclockwise; break;
		}
		
		
		if(str.charAt(0)=='D' || str.charAt(0)=='L' || str.charAt(0) =='B')
		{
			if (dir == Command.clockwise)
			{
				dir = Command.counterclockwise;
			}
			else
			{
				dir = Command.clockwise;
			}
		}
		
		cmdStr += dir;
		
		if(str.charAt(1) == '2')
		{
			cmdStr += '2';
		}
		else
		{
			cmdStr += '1';
		}
		return cmdStr;
	}
	
	// Pruned tree search. recursive.
	private boolean searchphase(int movesleft, int movesdone,int lastmove){
		if(nLoop++ > maxnloop)
			return true;
		// prune - position must still be solvable in the remaining moves available
		if( tables[phase  ][getposition(phase  )]-1 > movesleft ||
		    tables[phase+1][getposition(phase+1)]-1 > movesleft ) return false;

		// If no moves left to do, we have solved this phase
		if(movesleft==0) return true;

		// not solved. try each face move
		for( int i=6;(i--)!=0;){
			// do not repeat same face, nor do opposite after DLB.
			if( (i-lastmove)!=0 && ((i-lastmove+1)!=0 || (i|1)!=0 ) ){
				move[movesdone]=i;
				// try 1,2,3 quarter turns of that face
				for(int j=0;++j<4;){
					//do move and remember it
					domove(i);
					moveamount[movesdone]=j;
					//Check if phase only allows half moves of this face
					if( (j==2 || i>=phase ) &&
						//search on
						searchphase(movesleft-1,movesdone+1,i) ) return true;
				}
				// put face back to original position.
				domove(i);
			}
		}
		// no solution found
		return false;
	}
	
	private int Find(char[] str, char dest)
	{
		for( int i=0; i<6; i++)
		{
			if (str[i] == dest)
				return i;
		}
		
		return -1;
	}
	// calculate a pruning table
	private void filltable(int ti){
		int n=1,l=1, tl=tablesize [ti];
		// alocate table memory
		char[] tb = tables [ti]=new char[tl];
		//clear table
		memset( tb, 0, tl);
		//mark solved position as depth 1
		reset();
		tb[getposition(ti)]=1;
		
		// while there are positions of depth l
		while(n != 0){
			n=0;
			// find each position of depth l
			for(int i=0;i<tl;i++){
				if( tb[i]==l ){
					//construct that cube position
					setposition(ti,i);
					// try each face any amount
					for( int f=0; f<6; f++){
						for( int q=1;q<4;q++){
							domove(f);
							// get resulting position
							int r=getposition(ti);
							//System.out.println(ti + " " + r);
							// if move as allowed in that phase, and position is a new one
							if( ( q==2 || f>=(ti&6) ) && (tb[r] == 0)){
								// mark that position as depth l+1
								tb[r]=(char) (l+1);
								n++;
							}
						}
						domove(f);
					}
				}
			}
			l++;
		}
	}

	private void reset(){
		for( int i=0; i<20; i++)
		{
			pos[i]=i;
			ori[i]=0;
		}
	}
	
	//do a clockwise quarter turn cube move
	private void domove(int m){
		int shift = 8*m;
		int i=8;
		//cycle the edges
		cycle(pos, perm, shift);
		cycle(ori,perm, shift);
		//cycle the corners
		cycle(pos, perm, shift+4);
		cycle(ori, perm, shift+4);
		//twist corners if RLFB
		if(m<4)
			for(;--i>3;) twist(perm[i+shift],i&1);
		//flip edges if FB
		if(m<2)
			for(i=4;(i--)!=0;) twist(perm[i+shift],0);
		
	}
	
	// twists i-th piece a+1 times.
	private void twist(int i,int a){
		i-=CHAROFFSET;
		ori[i]=(ori[i]+a+1)%val[i];
	}
	
	private void memset(char[] ptr, int val, int size) {
		for(int i=0; i<size; i++)
		{
			ptr[i] = (char) (val);
		}
	}
	
	// get index of cube position from table t
	private int getposition(int t){
		int i=-1,n=0;
		
		switch(t){
		// case 0 does nothing so returns 0
		case 1://edgeflip
			// 12 bits, set bit if edge is flipped
			for(;++i<12;) n+= ori[i]<<i;
			break;
		case 2://cornertwist
			// get base 3 number of 8 digits - each digit is corner twist
			for(i=20;--i>11;) n=n*3+ori[i];
			break;
		case 3://middle edge choice
			// 12 bits, set bit if edge belongs in Um middle slice
			for(;++i<12;) n+= ((pos[i]&8) != 0)?(1<<i):0;
			break;
		case 4://ud slice choice
			// 8 bits, set bit if UD edge belongs in Fm middle slice
			for(;++i<8;) n+= ((pos[i]&4) != 0)?(1<<i):0;
			break;
		case 5://tetrad choice, twist and parity
			int corn[] = new int[8];
			int j,k,l;
			int corn2[] = new int[4];
			// 8 bits, set bit if corner belongs in second tetrad.
			// also separate pieces for twist/parity determination
			k=j=0;
			for(;++i<8;)
				if(((l=pos[i+12]-12)&4) != 0){
					corn[l]=k++;
					n+=1<<i;
				}else corn[j++]=l;
			//Find permutation of second tetrad after solving first
			for(i=0;i<4;i++) corn2[i]=corn[4+corn[i]];
			//Solve one piece of second tetrad
			for(;(--i)!=0;) corn2[i]^=corn2[0];

			// encode parity/tetrad twist
			n=n*6+corn2[1]*2-2;
			if(corn2[3]<corn2[2])n++;
			break;
		case 6://two edge and one corner orbit, permutation
			n=permtonum(pos, 0)*576+permtonum(pos, 4)*24+permtonum(pos, 12);
			break;
		case 7://one edge and one corner orbit, permutation
			n=permtonum(pos, 8)*24+permtonum(pos, 16);
			break;
		}
		return n;
	}
	private int permtonum(int[] p, int shift){
		int n=0;
		for ( int a=0; a<4; a++) {
			n *= 4-a;
			for( int b=a; ++b<4; )
				if (p[b+shift]<p[a+shift]) n++;
		}
		return n;
	}	
	
	// sets cube to any position which has index n in table t
	private void setposition(int t, int n){
		int i=0,j=12,k=0;
		int shift = 0;
		char []corn= {'Q', 'R', 'S', 'T', 'Q', 'R', 'T', 'S', 'Q', 'S', 'R', 'T', 'Q', 'T', 'R', 'S', 'Q', 'S', 'T', 'R', 'Q', 'T', 'S', 'R'};
		reset();
		switch(t){
		// case 0 does nothing so leaves cube solved
		case 1://edge flip
			for(;i<12;i++,n>>=1) ori[i]=n&1;
			break;
		case 2://corner twist
			for(i=12;i<20;i++,n/=3) ori[i]=n%3;
			break;
		case 3://middle edge choice
			for(;i<12;i++,n>>=1) pos[i]= 8*n&8;
			break;
		case 4://ud slice choice
			for(;i<8;i++,n>>=1) pos[i]= 4*n&4;
			break;
		case 5://tetrad choice,parity,twist
			shift +=n%6*4;
			n/=6;
			for(;i<8;i++,n>>=1)
				pos[i+12]= ((n&1)!=0) ? corn[shift+k++]-CHAROFFSET : j++;
			break;
		case 6://slice permutations
			numtoperm(pos,n%24,12);n/=24;
			numtoperm(pos,n%24,4); n/=24;
			numtoperm(pos,n   ,0);
			break;
		case 7://corner permutations
			numtoperm(pos,n/24,8);
			numtoperm(pos,n%24,16);
			break;
		}
	}
	
	// convert number in range 0..23 to permutation of 4 chars.
	private void numtoperm(int[] p,int n,int o){
		p[3+o]=o;
		for (int a=3; (a--)!=0;){
			p[a+o] = n%(4-a) +o;
			n/=4-a;
			for (int b=a; ++b<4; )
				if ( p[b+o] >= p[a+o]) p[b+o]++;
		}
	}
	
	// Cycles 4 pieces in array p, the piece indices given by a[0..3].
	private void cycle(int []p, char[]a, int shift){
		SWAP(p, (int)(a[0+shift]-CHAROFFSET),(int)(a[1+shift]-CHAROFFSET));
		SWAP(p, (int)(a[0+shift]-CHAROFFSET),(int)(a[2+shift]-CHAROFFSET));
		SWAP(p, (int)(a[0+shift]-CHAROFFSET),(int)(a[3+shift]-CHAROFFSET));
	}
	
	private void SWAP(int [] ptr, int a, int b)
	{
		int tmp = ptr[a];
		ptr[a] = ptr[b];
		ptr[b] = tmp;
	}

	private String ResetInitial(String InitialState)
	{
		int fi, li;
		char tmp;
		char[] initialState = InitialState.toCharArray();
		Log.e("e", InitialState);
		
		//deal with the f b position
		for(fi=F; fi<InitialState.length(); fi++)
		{
			if(initialState[fi] == 'F')
			{
				break;
			}
		}

		if( fi == F )
		{
			//do nothing
		}
		else if( fi == B )
		{
			result += Command.rotate_row+"1"+Command.clockwise + "2" + " ";
			tmp = initialState[FL];
			initialState[FL] = initialState[BR];
			initialState[BR] = tmp;
			
			tmp = initialState[FR];
			initialState[FR] = initialState[BL];
			initialState[BL] = tmp;
			
			tmp = initialState[FL+1];
			initialState[FL+1] = initialState[BR+1];
			initialState[BR+1] = tmp;
			
			tmp = initialState[FR+1];
			initialState[FR+1] = initialState[BL+1];
			initialState[BL+1] = tmp;
			
			tmp = initialState[L];
			initialState[L] = initialState[R];
			initialState[R] = tmp;
			
			tmp = initialState[F];
			initialState[F] = initialState[B];
			initialState[B] = tmp;
		}
		else if( fi == L )
		{
			result += Command.rotate_row+"1"+Command.clockwise + "1" + " ";
			tmp = initialState[FL];
			initialState[FL] = initialState[BL+1];
			initialState[BL+1] = initialState[BR];
			initialState[BR] = initialState[FR+1];
			initialState[FR+1] = tmp;
			
			tmp = initialState[FL+1];
			initialState[FL+1] = initialState[BL];
			initialState[BL] = initialState[BR+1];
			initialState[BR+1] = initialState[FR];
			initialState[FR] = tmp;
			
			tmp = initialState[F];
			initialState[F] = initialState[L];
			initialState[L] = initialState[B];
			initialState[B] = initialState[R];
			initialState[R] = tmp;
		}
		else if( fi == R )
		{
			result += Command.rotate_row+"1"+Command.counterclockwise + "1" + " ";
			tmp = initialState[FL];
			initialState[FL] = initialState[FR+1];
			initialState[FR+1] = initialState[BR];
			initialState[BR] = initialState[BL+1];
			initialState[BL+1] = tmp;
			
			tmp = initialState[FL+1];
			initialState[FL+1] = initialState[FR];
			initialState[FR] = initialState[BR+1];
			initialState[BR+1] = initialState[BL];
			initialState[BL] = tmp;
			
			tmp = initialState[F];
			initialState[F] = initialState[R];
			initialState[R] = initialState[B];
			initialState[B] = initialState[L];
			initialState[L] = tmp;
		}
		else if( fi == U )
		{
			result += Command.rotate_col+"1"+Command.clockwise + "1" + " ";
			tmp = initialState[UF+1];
			initialState[UF+1] = initialState[UB];
			initialState[UB] = initialState[DB+1];
			initialState[DB+1] = initialState[DF];
			initialState[DF] = tmp;
			
			tmp = initialState[UF];
			initialState[UF] = initialState[UB+1];
			initialState[UB+1] = initialState[DB];
			initialState[DB] = initialState[DF+1];
			initialState[DF+1] = tmp;
			
			tmp = initialState[F];
			initialState[F] = initialState[U];
			initialState[U] = initialState[B];
			initialState[B] = initialState[D];
			initialState[D] = tmp;
		}
		else if( fi == D )
		{
			result += Command.rotate_col+"1"+Command.counterclockwise + "1" + " ";
			tmp = initialState[UF+1];
			initialState[UF+1] = initialState[DF];
			initialState[DF] = initialState[DB+1];
			initialState[DB+1] = initialState[UB];
			initialState[UB] = tmp;
			
			tmp = initialState[UF];
			initialState[UF] = initialState[DF+1];
			initialState[DF+1] = initialState[DB];
			initialState[DB] = initialState[UB+1];
			initialState[UB+1] = tmp;
			
			tmp = initialState[F];
			initialState[F] = initialState[D];
			initialState[D] = initialState[B];
			initialState[B] = initialState[U];
			initialState[U] = tmp;
		}
		
		//deal with the l r position
		for(li=F; li<InitialState.length(); li++)
		{
			if(initialState[li] == 'L')
			{
				break;
			}
		}
		
		if( li == L)
		{
			//do nothing
			
		}
		else if( li == U)
		{
			result += Command.rotate_face+"1"+Command.clockwise + "1" + " ";
			tmp = initialState[UL+1];
			initialState[UL+1] = initialState[UR];
			initialState[UR] = initialState[DR+1];
			initialState[DR+1] = initialState[DL];
			initialState[DL] = tmp;
			
			tmp = initialState[UL];
			initialState[UL] = initialState[UR+1];
			initialState[UR+1] = initialState[DR];
			initialState[DR] = initialState[DL+1];
			initialState[DL+1] = tmp;
		}
		else if( li == D)
		{
			result += Command.rotate_face+"1"+Command.counterclockwise + "1" + " ";
			tmp = initialState[UL+1];
			initialState[UL+1] = initialState[DL];
			initialState[DL] = initialState[DR+1];
			initialState[DR+1] = initialState[UR];
			initialState[UR] = tmp;
			
			tmp = initialState[UL];
			initialState[UL] = initialState[DL+1];
			initialState[DL+1] = initialState[DR];
			initialState[DR] = initialState[UR+1];
			initialState[UR+1] = tmp;
		}
		else if( li == R)
		{
			result += Command.rotate_face+"1"+Command.counterclockwise + "2" + " ";
			tmp = initialState[UL];
			initialState[UL] = initialState[DR];
			initialState[DR] = tmp;
			
			tmp = initialState[UR];
			initialState[UR] = initialState[DL];
			initialState[DL] = tmp;
			
			tmp = initialState[UL+1];
			initialState[UL+1] = initialState[DR+1];
			initialState[DR+1] = tmp;
			
			tmp = initialState[UR+1];
			initialState[UR+1] = initialState[DL+1];
			initialState[DL+1] = tmp;
		}
		return new String(initialState);
		
	}

}
