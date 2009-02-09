/**
 * Copyright (C) 2009 Jörg Werner schreibubi@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
header {
package org.schreibubi.JCombinationsTools.patchWalker;
    import java.util.*;
    import java.lang.Double;
    import java.lang.Math;
    import org.schreibubi.JCombinationsTools.chunk.*;
    import org.schreibubi.visitor.*;
}
options { language="Java"; }

class PatchTreeWalker extends TreeParser;

options {
    importVocab = Patch;
    genHashLines = true;
}

patches returns [VArrayList<Patch> ps=new VArrayList<Patch>()] throws Exception
{ Patch p; }
	:
		(p=patch { ps.add(p); })+
	;
	
patch returns [Patch p=null] throws Exception
{ Chunk ch; 
  VArrayList<Chunk> chunks=new VArrayList<Chunk>(); }
    :
        #(PATCH o:VAR n:VAR ( ch=chunk { chunks.add(ch); })*  { p=new Patch(o.getText(),n.getText(),chunks); } )
    ;

chunk returns [Chunk ch=null] throws Exception
{ VArrayList<Integer> plusList, minusList, pos; }
    :
        #(CHUNK (v:VAR)? pos=at minusList=remove plusList=replace 
            {
          	        ch=new Chunk(pos,minusList,plusList); 
            	    if (v!=null) { 
            	        ch.setName(v.getText());             	    	
            	    }
        	    }
        	)
    ;


at returns [VArrayList<Integer> pos=new VArrayList<Integer>()]
{ int e,f,g,h; }
    :   #( AT e=con f=con { pos.add(new Integer(e)); pos.add(new Integer(f));} (g=con h=con {pos.add(new Integer(g)); pos.add(new Integer(h)); })?)
    ;

remove returns [VArrayList<Integer> minusList=new VArrayList<Integer>()]
{ int e; }
    :   #( MINUS (e=con {minusList.add(new Integer(e)); } )+ )
    ;

replace returns [VArrayList<Integer> plusList=new VArrayList<Integer>()]
{ int e; }
    :   #( PLUS (e=con {plusList.add(new Integer(e)); } )+ )
    ;

con returns [int e]
{e=0;}
    :
        ( g:NUM { e=Integer.parseInt(g.getText(),10); }    
        | h:HEXNUM { e=Integer.parseInt(h.getText(),16); }    
	        )
    ;
