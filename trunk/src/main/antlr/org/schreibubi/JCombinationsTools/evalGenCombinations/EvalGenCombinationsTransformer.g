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

options { 
	    language="Java";
}
{
    import java.util.*;
    import org.schreibubi.visitor.*;
    import org.schreibubi.symbol.*;
    import antlr.ASTFactory;
}

class EvalGenCombinationsTransformer extends TreeParser;

options {
	importVocab = EvalGenCombinations;
    buildAST = true;
}
{
}

blocks 
    : (block)+
    ;

block
    : #(BLOCK assignments)
    ;

assignments
{
	ArrayList<AST> keyset_accumul=new ArrayList<AST>();
	ArrayList<AST> val=new ArrayList<AST>();
	ArrayList<ArrayList<AST>> valueset_accumul=new ArrayList<ArrayList<AST>>();
}
    :!   ( #(
            ASSIGN k:keyset {keyset_accumul.add(#k); val=new ArrayList<AST>(); }
                ( ( v:valueset {val.add(#v);})+ {valueset_accumul.add(val);}
                | valuesarray )
           ) )+ {
           	 AST ke=#[KEYLIST,"KEYLIST"];
			 for (AST ks : keyset_accumul) {
           	 	ke.addChild(ks);
			 }
           	 AST ke2=#[VALUELIST,"VALUELIST"];
			 for (ArrayList<AST> vs : valueset_accumul) {
	           	AST ke3=#[VALUELIST,"VALUELIST"];
				for (AST vs2 : vs) {
					ke3.addChild(vs2);
				}
           	 	ke2.addChild(ke3);
			 } 
           	 AST ke4=#[ASSIGN,"="];
			 ke4.addChild(ke);
			 ke4.addChild(ke2);
			 #assignments=ke4;
           } 
    ;
    
keyset
    : #( KEYLIST ( keyset | VAR )+ )
    ;
    
valueset 
    : #( VALUELIST (valueset | expr)+ ) 
    ;

valuesarray
     : #(VALUESARRAY expr expr expr) 	
	;

expr
    :
        #(
            PLUS expr expr
        )
    |   #(
            MINUS expr expr
        )
    |   #(
            STAR expr expr
        )
    |   #(
            SLASH expr expr
        )
    |   #(
            XOR expr expr
        )
    |   #(
            OR expr expr
        )
    |   #(
            AND expr expr
        )
    |   #(
            NOT expr
        )
    |   #(
            UNARY_PLUS  expr
        )
    |   #(
            UNARY_MINUS expr
        )
    |   #(
            "ABS" expr
        )
    |   #(
            "SIN" expr
        )
    |   #(
            "COS" expr
        )   
    |   #(
            "TAN" expr
        )
    |   #(
            "ASIN" expr
        )   
    |   #(
            "ACOS" expr
        )   
    |   #(
            "ATAN" expr
        )   
    |   #(
            "POW" expr expr
        )
    |   #(
            "SQRT" expr
        )
    |   #(
            "EXP" expr
        )   
    |   #(
            "LN" expr
        ) 
    |   #(
            "HEX" expr
        ) 
    |   #(
            "UPPERCASE" expr
        ) 
    |   id
    |   con 
    ; 


id
    :
        (f:VAR 	
        | #(POST_INC g:VAR )
        | #(POST_DEC h:VAR )
        | #(PRE_INC i:VAR )
        | #(PRE_DEC j:VAR )
        )
              
    ;

con
    :
      f:FLT_CONST 
    | g:INT_CONST 
    | h:HEX_CONST 
    | i:BIN_CONST 
    | j:STR_CONST 
    ;


