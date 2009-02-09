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


tree grammar GetCombinationTreeWalker;

options {
    tokenVocab=EvalGenCombinations;
    ASTLabelType=DebugCommonTree;
}

@header {
package org.schreibubi.JCombinationsTools.evalGenCombinations;
    import org.schreibubi.visitor.*;
    import org.schreibubi.symbol.*;
    import org.antlr.runtime.tree.CommonTree;
	import java.util.ListIterator;
}

@members {

	    VHashMap<Symbol> symbolTable=null;
	    VHashMap<Symbol> globalSymbolTable=null;

		public void setSymbolTable(VHashMap<Symbol> sT) {
			symbolTable=sT;
		}

		public VHashMap<Symbol> getSymbolTable() {
			return symbolTable;
		}

		public void setGlobalSymbolTable(VHashMap<Symbol> sT) {
			globalSymbolTable=sT;
		}

		public VHashMap<Symbol> getGlobalSymbolTable() {
			return globalSymbolTable;
		}

}



blocks [int alt]
@init { 
  int ind=0; 
  ArrayList<Integer> treelist=new ArrayList<Integer>();
}
    : 
    ( { ind=input.index(); } . { treelist.add(ind); } )+ 
    {
      int cum=0;
      for (int i: treelist) {
      	int curAlts=((DebugCommonTree) input.get(i)).getAlts();
        if ((alt>=cum) && (alt<cum+curAlts)) {
            ((CommonTreeNodeStream)input).push(i);
            block(alt-cum);
            ((CommonTreeNodeStream)input).pop();
            break;
        }
        cum+=curAlts;
      }
    }
    ;

block [int alt]
@init {   
  int ind=0; 
  ArrayList<Integer> treelist=new ArrayList<Integer>();
 }
    : ^(BLOCK ( { ind=input.index(); } . { treelist.add(ind); } )+ ) 
      {
        int previous=1;
		ArrayList<Integer> cumAltsList=new ArrayList<Integer>();
        for (ListIterator<Integer> iterList=treelist.listIterator(treelist.size()); iterList.hasPrevious();) {
		  int index=iterList.previous();
      	  int curAlts=((DebugCommonTree) input.get(index)).getAlts();
		  cumAltsList.add(0,previous);
		  previous*=curAlts;
        }
        int altb=alt;
        for (int j = 0; j < treelist.size(); j++) {
			int choosenAlt=altb / cumAltsList.get(j);
            ((CommonTreeNodeStream)input).push(treelist.get(j));
            assignment(choosenAlt);
            ((CommonTreeNodeStream)input).pop();
			altb = altb \% cumAltsList.get(j);
		}
        
      }
    ;

annotate
	: ^(ANNOTATION STR_CONST)
	;

assignment [int alt]
    :    ^( as=ASSIGN annotate? keyOrKeyBlock valueListOrValueBlock[alt] )
    ;
    
keyOrKeyBlock
	:	(key | keyblock)
	;
	
key
	: ^( KEY .)
	;
	
keyblock
	: ^(KEYBLOCK keyOrKeyBlock+)
	;
	
valueListOrValueBlock [int alt]
@init { 
  int ind=0; 
  ArrayList<Integer> treelist=new ArrayList<Integer>();
}
	: ( ^(v=VALUE e=expr) { e.setName(v.getLhsName()); symbolTable.put(v.getLhsName(),e); } 
	  | ^(VALUELIST ({ ind=input.index(); } . { treelist.add(ind); })+ ) 
	    {
          int cum=0;
          for (int i: treelist) {
      	    int curAlts=((DebugCommonTree) input.get(i)).getAlts();
            if ((alt>=cum) && (alt<cum+curAlts)) {
              ((CommonTreeNodeStream)input).push(i);
              valueListOrValueBlock(alt-cum);
              ((CommonTreeNodeStream)input).pop();
              break;
            }
            cum+=curAlts;
          }
	    }
	  | ^(VALUEBLOCK ({ ind=input.index(); } . { treelist.add(ind); })+ )
	    {
  	      int previous=1;
		  ArrayList<Integer> cumAltsList=new ArrayList<Integer>();
          for (ListIterator<Integer> iterList=treelist.listIterator(treelist.size()); iterList.hasPrevious();) {
		    int index=iterList.previous();
      	    int curAlts=((DebugCommonTree) input.get(index)).getAlts();
		    cumAltsList.add(0,previous);
		    previous*=curAlts;
          }
          int altb=alt;
          for (int j = 0; j < treelist.size(); j++) {
			int choosenAlt=altb / cumAltsList.get(j);
            ((CommonTreeNodeStream)input).push(treelist.get(j));
            valueListOrValueBlock(choosenAlt);
            ((CommonTreeNodeStream)input).pop();
			altb = altb \% cumAltsList.get(j);
		  }
	    }
	  ) 
	  ;
	  
	  
expr returns [Symbol value=null] throws Exception
    :
        ^(
            PLUS e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                $value=e1.add(e2);
            }
        )
    |   ^(
            MINUS e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                $value=e1.sub(e2);
            }
        )
    |   ^(
            STAR e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
            	$value=e1.mul(e2);
            }
        )
    |   ^(
            SLASH e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                $value=e1.div(e2);
            }
        )
    |   ^(
            XOR e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                $value=e1.xor(e2);
            }
        )
    |   ^(
            OR e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                $value=e1.or(e2);
            }
        )
    |   ^(
            AND e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                $value=e1.and(e2);
            }
        )
    |   ^(
            NOT e1=expr
            {
                $value=e1.not();
            }
        )
    |   ^(
            UNARY_PLUS  e1=expr
            {
                $value=e1.uplus();
            }
        )
    |   ^(
            UNARY_MINUS e1=expr
            {
                $value=e1.uminus();
            }
        )
    |   ^(
            'ABS' e1=expr
            {
                $value=e1.abs();
            }
        )
    |   ^(
            'SIN' e1=expr
            {
                $value=e1.sin();
            }
        )
    |   ^(
            'COS' e1=expr
            {
                $value=e1.cos();
            }
        )   
    |   ^(
            'TAN' e1=expr
            {
                $value=e1.tan();
            }
        )
    |   ^(
            'ASIN' e1=expr
            {
                $value=e1.asin();
            }
        )   
    |   ^(
            'ACOS' e1=expr
            {
                $value=e1.acos();
            }
        )   
    |   ^(
            'ATAN' e1=expr
            {
                $value=e1.atan();
            }
        )   
    |   ^(
            'POW' e1=expr e2=expr
            {
            	    if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }	
                $value=e1.pow(e2);
            }
        )
    |   ^(
            'SQRT' e1=expr
            {
                $value=e1.sqrt();
            }
        )
    |   ^(
            'EXP' e1=expr
            {
                $value=e1.exp();
            }
        )   
    |   ^(
            'LN' e1=expr
            {
                $value=e1.ln();
            }
        ) 
    |   ^(
            'HEX' e1=expr
            {
                $value=new SymbolString(Integer.toHexString(e1.convertToInt().getValue()).toUpperCase());
            }
        ) 
    |   ^(
            'UPPERCASE' e1=expr
            {
                $value=new SymbolString(e1.convertToString().getValue().toUpperCase());
            }
        ) 
    |   e1=id { $value=e1; }
    |   e1=con { $value=e1; }
    ; 
  	catch [Exception e] {}
   

id returns [Symbol value=null] throws Exception
    :
        (f=VAR 	
        	{ if (symbolTable.get($f.text)==null) 
        			{ 
        				if (globalSymbolTable.get($f.text)==null) 
        					{ 
        						throw new RecognitionException(); //"Undefined Variable "+$f.text+"!"); 
        					} 
						else 
							{
								$value=globalSymbolTable.get( $f.text ).clone(); 
							}
					}
				else 
					{ 
						$value=symbolTable.get( $f.text ).clone(); 
					} 
             }
        | ^(POST_INC g=VAR {
					if (globalSymbolTable.get($g.text)==null) { $value=new SymbolInteger(0); globalSymbolTable.put($g.text,new SymbolInteger(1)); } 
					else { $value=globalSymbolTable.get($g.text).postInc(); }
        })
        | ^(POST_DEC h=VAR {
					if (globalSymbolTable.get($h.text)==null) { $value=new SymbolInteger(0); globalSymbolTable.put($h.text,new SymbolInteger(-1)); } 
					else { $value=globalSymbolTable.get($h.text).postDec(); }
        })
        | ^(PRE_INC i=VAR {
					if (globalSymbolTable.get($i.text)==null) { $value=new SymbolInteger(1); globalSymbolTable.put($i.text,new SymbolInteger(1)); } 
					else { $value=globalSymbolTable.get($i.text).preInc(); }
        })
        | ^(PRE_DEC j=VAR {
					if (globalSymbolTable.get($j.text)==null) { $value=new SymbolInteger(-1); globalSymbolTable.put($j.text,new SymbolInteger(-1)); } 
					else { $value=globalSymbolTable.get($j.text).preDec(); }
        })
        )
              
    ;
  	catch [Exception e] {}


con returns [Symbol value=null] throws Exception
    :
      f=FLT_CONST { 
      		$value=new SymbolDouble( $f.text ); 
      	}
    | f=INT_CONST { 
    		$value=new SymbolInteger( $f.text ); 
    	}
    | f=HEX_CONST { 
    		$value=new SymbolInteger( Integer.parseInt( $f.text, 16 ) ); 
    	}
    | f=BIN_CONST { 
    		$value=new SymbolInteger( Integer.parseInt( $f.text, 2 ) ); 
    	}
    | f=STR_CONST  { 
    		$value=new SymbolString( $f.text ); 
    	}    
    ;


	  