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


tree grammar CheckTreeWalker;

options {
    tokenVocab=EvalGenCombinations;
    ASTLabelType=DebugCommonTree;
}

@header {
package org.schreibubi.JCombinationsTools.evalGenCombinations;
    import org.schreibubi.visitor.*;
    import org.schreibubi.symbol.*;
    import org.antlr.runtime.tree.CommonTree;
}

@members {

	static TreeAdaptor treeadaptor;
	
	protected static boolean _equals(Object key, Object value, TreeAdaptor adaptor) {
		// make sure both are non-null
		if ( key==null || value==null ) {
			return false;
		}
		if (adaptor.getType(value)==VALUELIST) {
			// check children
			int n1 = adaptor.getChildCount(value);
			for (int i=0; i<n1; i++) {
				Object child2 = adaptor.getChild(value, i);
				if ( !_equals(key, child2, adaptor) ) {
					return false;
				}
			}
			return true;	
		} else if (adaptor.getType(value)==VALUEBLOCK && adaptor.getType(key)==KEYBLOCK) {
			// check children
			int n1 = adaptor.getChildCount(key);
			int n2 = adaptor.getChildCount(value);
			if ( n1 != n2 ) {
				System.out.println("number of arguments on lhs (line: "
				+((CommonTree) key).getLine()+" col: "+((CommonTree) key).getCharPositionInLine()+") unequal to rhs (line: "
				+((CommonTree) value).getLine()+" col: "+((CommonTree) value).getCharPositionInLine()+")");
				return false;
			}
			for (int i=0; i<n1; i++) {
				Object child1 = adaptor.getChild(key, i);
				Object child2 = adaptor.getChild(value, i);
				if ( !_equals(child1, child2, adaptor) ) {
					return false;
				}
			}
			return true;	
		} else if (adaptor.getType(value)==VALUE && adaptor.getType(key)==KEY) {
			((DebugCommonTree) value).setLhsName(((DebugCommonTree) adaptor.getChild(key,0)).getToken().getText());
			return true;
		}
		System.out.println("unequal lhs (line: "
		+((CommonTree) key).getLine()+" col: "+((CommonTree) key).getCharPositionInLine()+") to rhs (line: "
		+((CommonTree) value).getLine()+" col: "+((CommonTree) value).getCharPositionInLine()+")");
		return false;
	}

}



blocks  
    : 
    ( block )+
    ;

block 
    : ^(BLOCK assignments)
    ;

assignments 
    :    (assignment)+
    ;

annotate
	: ^(ANNOTATION STR_CONST)
	;

assignment 
    :    ^( ASSIGN annotate? k=. l=. )
    	{
//    		System.out.println("key: "+k.toStringTree());
//    		System.out.println("val: "+l.toStringTree());
   			if (_equals($k,$l,treeadaptor)) 
   				System.out.println("correct");
   			else
   				System.out.println("wrong");
    	}
    ;
    
