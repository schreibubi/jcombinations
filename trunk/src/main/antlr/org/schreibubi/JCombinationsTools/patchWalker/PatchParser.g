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
}
options { language="Java"; }


class PatchParser extends Parser;
options {
	    k = 2;				// two token lookahead
	    exportVocab=Patch;		// Call its vocabulary "Matlab"
	    defaultErrorHandler = false;     // Don't generate parser error handlers
	    buildAST = true;
	    genHashLines = true;
}
tokens {
    CHUNK;
    PATCH;
}

patches
	:
		(patch)+
	;
	
patch
    :
        MINUS! MINUS! MINUS! VAR eol! 
        PLUS! PLUS! PLUS! VAR eol! 
        (chunk)+
        {#patch = #(#[PATCH,"PATCH"],#patch);}
    ;

chunk
    :
        (VAR COLON! eol!)? 
        pos eol! remove eol! replace eol!
        {#chunk = #(#[CHUNK,"CHUNK"],#chunk);}
    ;


pos
		    :
        AT^	 value COMMA! value (COMMA! value COMMA! value)?
	    ;

remove
    	:
		        MINUS^ (value)+
	    ;

replace
	    :
		        PLUS^ (value)+
	    ;

	
eol!
	    :
        (
            options {
                warnWhenFollowAmbig = false;
            }
		            :
            EOL!
        )+
	    ; 

value
	    :
	        ( NUM | HEXNUM )
	    ;

class PatchLexer extends Lexer;

options {
	    testLiterals=true;      // automatically test for literals
	    k=2;                    // two characters of lookahead
	    importVocab=Patch;
	    caseSensitive=true;
	    caseSensitiveLiterals = false;
    charVocabulary = '\u0000'..'\uFFFE';
}

AT     			: 	'@'		;
COMMA			  :	 ','		;
PLUS   			:	 '+'		;
MINUS			  : 	'-'		;
COLON  : ':';

// Whitespace -- ignored
WS	:
    		(	 ' '
		    | 	'\t'
		    | 	'\f'
		    ) { $setType(Token.SKIP); }
	    ;

EOL
	    :
		    ( 	"\r\n"  // Evil DOS
		    | 	'\r'    // Macintosh
		    | 	'\n'    // Unix (the right way)
		    ) { newline(); }
	    ;


HEXNUM
    : "0x"! ('0'..'9' | 'A'..'F' | 'a'..'f' )+ 
    ;

NUM
    : ( '0'..'9' )+ 
    ;

VAR
		    : 	('a'..'z'|'A'..'Z'|'_'|'.'|'/'|'\\') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.'|'/'|'\\')*
//		    : 	('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.'|'/'|'\\')+
	;
