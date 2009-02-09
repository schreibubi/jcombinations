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
package org.schreibubi.JCombinationsTools.evalGenCombinations;
}

options { language="Java"; }
{
    import java.util.*;
    import org.schreibubi.visitor.*;
    import org.schreibubi.symbol.*;
    import antlr.ASTFactory;
}

class EvalGenCombinationsTreeWalker extends TreeParser;

options {
//	codeGenMakeSwitchThreshold=999;
//	codeGenBitsetTestThreshold=999;
    importVocab = EvalGenCombinations;
    genHashLines = true;
}
{
	    VHashMap<Symbol> symbolTable=null;
	    VHashMap<Symbol> globalSymbolTable=null;
	    VHashMap<String> options=null;

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

		public VHashMap<String> getOptions() {
			return options;
		}

		public void setOptions(VHashMap<String> options) {
			this.options=options;
		}
	
}

blocks returns [VArrayList<VArrayList<AlternativesASTSet>> blocks=new VArrayList<VArrayList<AlternativesASTSet>>()] throws Exception
{ 
	VArrayList<AlternativesASTSet> blockval; 
}
    : (option)* (blockval=block { blocks.add(blockval); })+
    ;

option
	: #(OPTION n:STR_CONST v:STR_CONST {
				options.put(n.getText(),v.getText());
			}
		)
	;

block returns [VArrayList<AlternativesASTSet> allAlternatives=null] throws Exception
    : #(BLOCK allAlternatives=assignments)
    ;

assignments returns [VArrayList<AlternativesASTSet> allAlternatives=new VArrayList<AlternativesASTSet>()] throws Exception
{  AlternativesASTSet alternative; }
    :    (alternative=assignment { allAlternatives.add(alternative); })+
    ;



assignment returns [AlternativesASTSet alternativeSet] throws Exception
{ alternativeSet=new AlternativesASTSet(); 
  VArrayList<String> keylist;
  VArrayList<AST> valuelist; }
    :    #(
            ASSIGN keylist=keyset { alternativeSet.setKeys(keylist); } 
                ( ( valuelist=valueset { alternativeSet.addAlternative(valuelist); })+
                | valuesarray[alternativeSet] )
        )
    ;
    
keyset returns [VArrayList<String> keylist=new VArrayList<String>()]
    : #( KEYLIST ( a:VAR {keylist.add(a.getText());})+ )
    ;
    
valueset returns [VArrayList<AST> valuelist=new VArrayList<AST>();]
    : VALUELIST { 
    	AST t=#valueset.getFirstChild(); 
    	do { 
    		valuelist.add(t); 
    	} while ((t=t.getNextSibling())!=null);
    	}
    ;

valuesarray [AlternativesASTSet alternativeSet] throws Exception
{
	Symbol sstart,sstep,sstop;
}
     : #(VALUESARRAY sstart=expr sstep=expr sstop=expr) {
     	if ( sstart.getType().compareTo( sstep.getType() ) < 0 ) {
            sstep = sstart.convert( sstep );
        } else {
        	sstart = sstep.convert( sstart );
        }	
     	if ( sstep.getType().compareTo( sstop.getType() ) < 0 ) {
            sstop = sstep.convert( sstop );
        } else {
        	sstep = sstop.convert( sstep );
        	sstart= sstop.convert( sstart );
        }	
     	
		ASTFactory factory = new ASTFactory();
		Symbol s=sstart.clone();
		if ( sstep.gt( sstep.convert( new SymbolInteger( 0 ) ) ) ) {
			Symbol send=sstop.clone().add(sstep.clone().div(sstep.convert(new SymbolInteger(10))).abs());
			while (s.le(send) ) {
				VArrayList<AST> v=new VArrayList<AST>();
				if (s.getType()==Symbol.SymType.DOUBLE) {
					v.add(factory.create(FLT_CONST,s.getValueUnitString()));
				} else if (s.getType()==Symbol.SymType.INTEGER) {
					v.add(factory.create(INT_CONST,s.getValueUnitString()));
				}
				alternativeSet.addAlternative(v);
				s.add(sstep);
			}			
		} else {
			Symbol send=sstop.clone().sub(sstep.clone().div(sstep.convert(new SymbolInteger(10))).abs());
			while (s.ge(send) ){
				VArrayList<AST> v=new VArrayList<AST>();
				if (s.getType()==Symbol.SymType.DOUBLE) {
					v.add(factory.create(FLT_CONST,s.getValueUnitString()));
				} else if (s.getType()==Symbol.SymType.INTEGER) {
					v.add(factory.create(INT_CONST,s.getValueUnitString()));
				}
				alternativeSet.addAlternative(v);
				s.add(sstep);
			}			
		}		
		
      }
	;

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
    |   #(
            "HEX" e1=expr
            {
                e=new SymbolString(Integer.toHexString(e1.convertToInt().getValue()).toUpperCase());
            }
        ) 
    |   #(
            "UPPERCASE" e1=expr
            {
                e=new SymbolString(e1.convertToString().getValue().toUpperCase());
            }
        ) 
    |   e=id
    |   e=con 
    ; 


id  returns [Symbol e=null] throws Exception
    :
        (f:VAR 	{ if (symbolTable.get(f.getText())==null) 
        			{ 
        				if (globalSymbolTable.get(f.getText())==null) 
        					{ 
        						throw new RecognitionException("Undefined Variable "+f.getText()+"!"); 
        					} 
						else 
							{
								e=globalSymbolTable.get( f.getText() ).clone(); 
							}
					}
				else 
					{ 
						e=symbolTable.get( f.getText() ).clone(); 
					} 
              }
        | #(POST_INC g:VAR {
					if (globalSymbolTable.get(g.getText())==null) { e=new SymbolInteger(0); globalSymbolTable.put(g.getText(),new SymbolInteger(1)); } 
					else { e=globalSymbolTable.get(g.getText()).postInc(); }
        })
        | #(POST_DEC h:VAR {
					if (globalSymbolTable.get(h.getText())==null) { e=new SymbolInteger(0); globalSymbolTable.put(h.getText(),new SymbolInteger(-1)); } 
					else { e=globalSymbolTable.get(h.getText()).postDec(); }
        })
        | #(PRE_INC i:VAR {
					if (globalSymbolTable.get(i.getText())==null) { e=new SymbolInteger(1); globalSymbolTable.put(i.getText(),new SymbolInteger(1)); } 
					else { e=globalSymbolTable.get(i.getText()).preInc(); }
        })
        | #(PRE_DEC j:VAR {
					if (globalSymbolTable.get(j.getText())==null) { e=new SymbolInteger(-1); globalSymbolTable.put(j.getText(),new SymbolInteger(-1)); } 
					else { e=globalSymbolTable.get(j.getText()).preDec(); }
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


