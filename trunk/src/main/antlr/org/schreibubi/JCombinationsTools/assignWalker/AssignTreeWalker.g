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
package org.schreibubi.JCombinationsTools.assignWalker;
    import java.util.*;
    import java.lang.*;
    import org.schreibubi.visitor.*;
    import org.schreibubi.symbol.*;
    import org.schreibubi.JCombinationsTools.SymbolVisitors.*;
}

options { language="Java"; }

class AssignTreeWalker extends TreeParser;

options {
    importVocab = Assign;
    genHashLines = true;
}
{
    VHashMap<Symbol> sT=null;
}

lines returns [VArrayList<VHashMap<Symbol>> symbolTableLines=new VArrayList<VHashMap<Symbol>>();] throws Exception
    :
        ( line {symbolTableLines.add(sT); } )*
    ;

line throws Exception
    :
        #(LINE { sT=new VHashMap<Symbol>(); } ( assign_statement )+  )
    ;

maxlines[String templateVariableName] returns [VHashMap<VHashMap<Symbol>> typeSizeMap=new VHashMap<VHashMap<Symbol>>();] throws Exception
{
    SymbolVisitorMax visitorMax = new SymbolVisitorMax( );
}
    :
        ( maxline 
            {
				String templateName="";
            	if (!templateVariableName.equals("")) {
            		Symbol templateNameSymbol=sT.get(templateVariableName);
					if (templateNameSymbol!=null)
						templateName=templateNameSymbol.convertToString().getValue();
					else
						throw new Exception("Variable "+templateVariableName+" is not defined");
            	} else {
            		templateName="";
            	}
				VHashMap<Symbol> oldST=typeSizeMap.get(templateName);
            	if (oldST==null) oldST=new VHashMap<Symbol>();
            	visitorMax.setMaxHash(oldST);
                sT.accept(visitorMax);
                typeSizeMap.put(templateName,visitorMax.getMaxHash());
            }
        )*
    ;

maxline throws Exception
    :
        #(LINE { sT=new VHashMap<Symbol>(); } ( assign_statement )+  )
    ;

assign_statement throws Exception
{
    Symbol f;
}
    :    #(
            ASSIGN e:VAR f=con
            {
                f.setName(e.getText());
                sT.put(e.getText(),f);
            }
        )
    ;
    
con returns [Symbol e=null] throws Exception
    :
      f:INT_CONST { 
      		e=new SymbolInteger( f.getText() ); 
      	}    
    | g:FLT_CONST { 
    		e=new SymbolDouble( g.getText() );  
    	}
    | h:STR_CONST {
    		e=new SymbolString( h.getText() ); 
    	}
    ;

