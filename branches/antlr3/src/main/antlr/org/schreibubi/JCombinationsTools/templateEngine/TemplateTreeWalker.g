/*
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
package org.schreibubi.JCombinationsTools.templateEngine;
}

options { language="Java"; }

{
    import java.util.*;
    import java.lang.*;
    import java.text.*;
    import java.io.*;
    import org.schreibubi.visitor.*;
    import org.schreibubi.symbol.*;
    import org.schreibubi.JCombinationsTools.SymbolVisitors.*;
}

class TemplateTreeWalker extends TreeParser;

options {
    importVocab = Template;
    genHashLines = true;
}
{
    VHashMap<Symbol> symbolTable=null;
    PrintWriter printWriter=null;
}

statements[VHashMap<Symbol> sT, PrintWriter pw] throws Exception
{ symbolTable=sT;
	  printWriter=pw; }
    :    
        ( literal | templatevar )+
    ;


literal throws Exception
    :    #(
            e:LITERAL
            {
                printWriter.print(e.getText());
            }
        )
    ;
    
templatevar throws Exception
    :
        (g:TEMPLATE_VAR (h:FORMAT_VAR)?) { 
        	    Symbol s=symbolTable.get(g.getText());
            if (s==null) { throw new Exception("Undefined Variable "+g.getText()+"!"); }
            else {
            	         SymbolVisitorFormat symbolVisitorFormat;
            	         if (h!=null)
	                         symbolVisitorFormat=new SymbolVisitorFormat(h.getText(), printWriter);
	                     else
		                         symbolVisitorFormat=new SymbolVisitorFormat("", printWriter);                 
	                     s.accept(symbolVisitorFormat);
                }   
           }    
    ;

