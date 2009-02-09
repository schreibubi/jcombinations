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
}
options { 
    language="Java"; 
}

class AssignParser extends Parser;
options {
    k = 2;                // two token lookahead
    exportVocab=Assign;        // Call its vocabulary "Matlab"
    defaultErrorHandler = false;     // Don't generate parser error handlers
    buildAST = true;
    genHashLines = true;
}
tokens {
    LINE;
}

lines
    :
        (line)*
    ;

line
    :
        assignmentExpression (assignmentExpression)* eol!
        {#line = #(#[LINE,"LINE"],#line);}
    ;


assignmentExpression
    :
        VAR ASSIGN^ con
    ;
    
con
    : ( INT_CONST | FLT_CONST | STR_CONST )
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



class AssignLexer extends Lexer;

options {
    testLiterals=true;      // automatically test for literals
    k=3;                    // two characters of lookahead
    importVocab=Assign;
    caseSensitive=true;
    caseSensitiveLiterals = false;
    charVocabulary = '\u0000'..'\uFFFE';
}

ASSIGN:    '='        ;

// Whitespace -- ignored
WS
    :
        (     ' '
        |     '\t'
        |     '\f'
        ) { $setType(Token.SKIP); }
    ;

EOL
    :
        (     "\r\n"  // Evil DOS
        |    '\r'    // Macintosh
        |    '\n'    // Unix (the right way)
        ) { newline(); }
    ;

VAR
    :     ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.')*
    ;

INT_CONST
    : ('+' | '-')? ('0'..'9')+ ( '.' ('0'..'9')* (EXPONENT)?  { $setType(FLT_CONST); }  
                               | EXPONENT { $setType(FLT_CONST); } )? (UNIT { $setType(FLT_CONST); })?
    ;

FLT_CONST
    : '.' ('0'..'9')+ (EXPONENT)? (UNIT)?
    ;
   	 
protected
EXPONENT
    :
        ('e'|'E') ('+'|'-')? ('0'..'9')+
    ;

// string literals
STR_CONST
    :
        '"'! ( ESCqs | ~('"'|'\\'))* '"'!
    ;

protected
ESCqs
    :
        '\\'! ( '"'
           	  | 'u' a:HEX_DIGIT b:HEX_DIGIT c:HEX_DIGIT d:HEX_DIGIT { char ch=(char) Integer.parseInt(a.getText()+b.getText()+c.getText()+d.getText(),16); $setText(ch); }
           	  | '\\'
           	  )
    ;

// hexadecimal digit (again, note it's protected!)
protected
HEX_DIGIT
        :       ('0'..'9'|'A'..'F'|'a'..'f')
        ;

protected
UNIT
	:
		('n' | 'u' | 'm' | 'k' | 'M' )? ( 'V' | 'A' | 's' | "Hz" )
	;
	