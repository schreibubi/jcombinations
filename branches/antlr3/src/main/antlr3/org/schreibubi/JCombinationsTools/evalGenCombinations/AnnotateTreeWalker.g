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


tree grammar AnnotateTreeWalker;

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
}



blocks returns [int alts]
@init { int sumalts=0; }
    : 
    ( b=block { sumalts+=b; } )+ { alts=sumalts; }
    ;

block returns [int alts]
@init { int sumalts=1; }
    : ^(b=BLOCK ( as=assignment { sumalts*=as; } )+ { alts=sumalts; b.setAlts(alts); } )
    ;

annotate
	: ^(ANNOTATION STR_CONST)
	;

assignment returns [int alts]
    :    ^( as=ASSIGN annotate? keyOrKeyBlock alt=valueListOrValueBlock ) { alts=alt; as.setAlts(alts); }
    ;
    
keyOrKeyBlock
	:	(key | keyblock)
	;
	
key
	: ^(KEY . )
	;
	
keyblock
	: ^(KEYBLOCK keyOrKeyBlock+)
	;
	
valueListOrValueBlock returns [int alts]
@init { int sumalts; }
	: ( ^(v=VALUE .) { alts=1;  }
	  | ^(v=VALUELIST  { sumalts=0; } ( (s=valueListOrValueBlock { sumalts+=s; } )+ ) { alts=sumalts; } )
	  | ^(v=VALUEBLOCK { sumalts=1; } ( (s=valueListOrValueBlock { sumalts*=s; } )+ ) { alts=sumalts; } )
	  ) { v.setAlts(alts); }
	  ;
	  