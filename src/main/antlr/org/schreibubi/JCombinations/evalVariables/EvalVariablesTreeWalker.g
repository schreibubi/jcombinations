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
package org.schreibubi.JCombinations.evalVariables;
    import java.util.*;
    import java.lang.*;
    import org.schreibubi.symbol.*;
    import org.schreibubi.visitor.*;
}
options { language="Java"; }

class EvalVariablesTreeWalker extends TreeParser;

options {
    importVocab = EvalVariables;
    genHashLines = true;
    defaultErrorHandler=false;
}
{
	    VHashMap<Symbol> symbolTable=null;
	    VHashMap<Symbol> constantVariables=null;
}


lines[VHashMap<Symbol> sT, VHashMap<Symbol> cV] throws Exception
 { 
 	symbolTable=sT; 
	constantVariables=cV;
 }
     :
        (line)*
    ;

line throws Exception
    :
        #(LINE ( assign_statement )+ )
    ;


assign_statement throws Exception
{
    Symbol f;
}
    :   #(
            ASSIGN e:VAR (f=expr 
                {
              	    f.setName(e.getText());
                    symbolTable.put(e.getText(),f);
                }
            )
        ) 
    ;
    exception 
        catch [RecognitionException ex] {
//		System.out.println(ex.toString());
//		ex.printStackTrace();
    }
    
expr returns [Symbol e=null] throws Exception
{
    Symbol e1,e2; 
}
    :
        #(
            PLUS e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.add(e2);
            }
        )
    |   #(
            MINUS e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.sub(e2);
            }
        )
    |   #(
            STAR e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
            	e=e1.mul(e2);
            }
        )
    |   #(
            SLASH e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.div(e2);
            }
        )
    |   #(
            XOR e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.xor(e2);
            }
        )
    |   #(
            OR e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.or(e2);
            }
        )
    |   #(
            AND e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.and(e2);
            }
        )
    |   #(
            NOT e1=expr
            {
                e=e1.not();
            }
        )
    |   #(
            UNARY_PLUS  e1=expr
            {
                e=e1.uplus();
            }
        )
    |   #(
            UNARY_MINUS e1=expr
            {
                e=e1.uminus();
            }
        )
    |   #(
            "ABS" e1=expr
            {
                e=e1.abs();
            }
        )
    |   #(
            "SIN" e1=expr
            {
                e=e1.sin();
            }
        )
    |   #(
            "COS" e1=expr
            {
                e=e1.cos();
            }
        )   
    |   #(
            "TAN" e1=expr
            {
                e=e1.tan();
            }
        )
    |   #(
            "ASIN" e1=expr
            {
                e=e1.asin();
            }
        )   
    |   #(
            "ACOS" e1=expr
            {
                e=e1.acos();
            }
        )   
    |   #(
            "ATAN" e1=expr
            {
                e=e1.atan();
            }
        )   
    |   #(
            "POW" e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.pow(e2);
            }
        )
    |   #(
            "MIN" e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.min(e2);
            }
        )
    |   #(
            "MAX" e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                e=e1.max(e2);
            }
        )
    |   #(
            "SQRT" e1=expr
            {
                e=e1.sqrt();
            }
        )
    |   #(
            "EXP" e1=expr
            {
                e=e1.exp();
            }
        )   
    |   #(
            "LN" e1=expr
            {
                e=e1.ln();
            }
        ) 
    |   e=id
    |   e=con 
    ; 


id  returns [Symbol e=null] throws Exception
    :
        ( f:VAR 	{ 
					if (constantVariables.get(f.getText())!=null) {
						e=constantVariables.get( f.getText() ).clone();
					} else if (symbolTable.get(f.getText())!=null) { 
						e=symbolTable.get( f.getText() ).clone();
					} else if (f.getText().endsWith("TRIM")) {
						e=new SymbolInteger(1);	
					} else {
						throw new RecognitionException("Undefined Variable "+f.getText()+"!");  
					}
              }
        | #(POST_INC g:VAR {
					if (symbolTable.get(g.getText())==null) { e=new SymbolInteger(0); symbolTable.put(g.getText(),new SymbolInteger(1)); } 
					else { e=symbolTable.get(g.getText()).postInc(); }
        })
        | #(POST_DEC h:VAR {
					if (symbolTable.get(h.getText())==null) { e=new SymbolInteger(0); symbolTable.put(h.getText(),new SymbolInteger(-1)); } 
					else { e=symbolTable.get(h.getText()).postDec(); }
        })
        | #(PRE_INC i:VAR {
					if (symbolTable.get(i.getText())==null) { e=new SymbolInteger(1); symbolTable.put(i.getText(),new SymbolInteger(1)); } 
					else { e=symbolTable.get(i.getText()).preInc(); }
        })
        | #(PRE_DEC j:VAR {
					if (symbolTable.get(j.getText())==null) { e=new SymbolInteger(-1); symbolTable.put(j.getText(),new SymbolInteger(-1)); } 
					else { e=symbolTable.get(j.getText()).preDec(); }
        })
        )
              
    ;

con returns [Symbol e=null] throws Exception
    :
      f:FLT_CONST { 
      		e=new SymbolDouble( f.getText() ); 
      	}
    | g:INT_CONST { 
    		e=new SymbolInteger( g.getText() ); 
    	}
    | h:HEX_CONST { 
    		e=new SymbolInteger( Integer.parseInt( h.getText(), 16 ) ); 
    	}
    | i:BIN_CONST { 
    		e=new SymbolInteger( Integer.parseInt( i.getText(), 2 ) ); 
    	}
    | j:STR_CONST  { 
    		e=new SymbolString( j.getText() ); 
    	}    
    ;

