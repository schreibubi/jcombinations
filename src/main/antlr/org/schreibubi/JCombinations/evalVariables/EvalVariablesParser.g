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
}
options { language="Java"; }

/** Parser for evaluating variables */
class EvalVariablesParser extends Parser;
options {
    k = 2;                // two token lookahead
    exportVocab=EvalVariables;        // Call its vocabulary "Matlab"
    defaultErrorHandler = false;     // Don't generate parser error handlers
    buildAST = true;
    genHashLines = true;
}
tokens {
    UNARY_PLUS;
    UNARY_MINUS;
    LINE;
}

lines
    :
        (line | eol)*
    ;

line
    :
        assignmentExpression (assignmentExpression)* eol!
        {#line = #(#[LINE,"LINE"],#line);}
    ;


assignmentExpression
    :
        VAR    ASSIGN^    ( relationalExpression )
    ;

relationalExpression
    :        relationalXORExpression
    ;

relationalXORExpression
    :        relationalORExpression    (    XOR^    relationalORExpression    )*
    ;

relationalORExpression
    :        relationalANDExpression        (    OR^     relationalANDExpression     )*
    ;


relationalANDExpression
    :        relationalNOTExpression        (    AND^    relationalNOTExpression        )*
    ;

relationalNOTExpression
    : (NOT^)?    numericExpression
    ;

numericExpression
    :
        numericMultiplicativeExpression 
        (
            options {
                warnWhenFollowAmbig = false;
            }
        :
            (PLUS^ | MINUS^) numericMultiplicativeExpression
        )*
    ;


numericMultiplicativeExpression
    :    numericUnaryExpression ((STAR^ | SLASH^ ) numericUnaryExpression)*
    ;

numericUnaryExpression
    :
        (
            p:PLUS^    {#p.setType(UNARY_PLUS);}
        |    m:MINUS^    {#m.setType(UNARY_MINUS);}
        )?     numericPrimaryExpression
    ;

numericPrimaryExpression
    :
        constExpression
    |    numericValuedFunctionExpression
    |    LPAREN! relationalExpression RPAREN!
    ;

numericValuedFunctionExpression
    :
      "ABS"^ LPAREN! relationalExpression RPAREN!
    | "SIN"^ LPAREN! relationalExpression RPAREN!
    | "COS"^ LPAREN! relationalExpression RPAREN!
    | "TAN"^ LPAREN! relationalExpression RPAREN!
    | "ASIN"^ LPAREN! relationalExpression    RPAREN!
    | "ACOS"^ LPAREN! relationalExpression    RPAREN!    
    | "ATAN"^ LPAREN! relationalExpression    RPAREN!
    | "SQRT"^ LPAREN! relationalExpression    RPAREN!
    | "EXP"^ LPAREN! relationalExpression RPAREN!
    | "LN"^ LPAREN! relationalExpression RPAREN!
    | "POW"^ LPAREN! relationalExpression COMMA! relationalExpression RPAREN!
    | "MIN"^ LPAREN! relationalExpression COMMA! relationalExpression RPAREN!
    | "MAX"^ LPAREN! relationalExpression COMMA! relationalExpression RPAREN!
    ;

constExpression
    :
        FLT_CONST 
    |   INT_CONST 
    |   HEX_CONST 
    |   BIN_CONST 
    |   STR_CONST
    |   VAR
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



class EvalVariablesLexer extends Lexer;

options {
    testLiterals=true;      // automatically test for literals
    k=2;                    // two characters of lookahead
    importVocab=EvalVariables;
    caseSensitive=true;
    caseSensitiveLiterals = false;
    charVocabulary = '\u0000'..'\uFFFE';
}


LPAREN            :    '('        ;
RPAREN            :    ')'        ;
COMMA             :    ','        ;
ASSIGN            :    '='        ;
SLASH             :    '/'        ;
PLUS              :    '+'        ;
MINUS             :    '-'        ;
STAR              :    '*'        ;
XOR               :    '^'     ;
AND               :    '&'        ;
OR                :    '|'        ;
NOT               :    '~'        ;

// Whitespace -- ignored
WS    :
        (    ' '
        |    '\t'
        |    '\f'
        )
        { $setType(Token.SKIP); }
    ;

EOL
    :
        (    "\r\n"  // Evil DOS
        |    '\r'    // Macintosh
        |    '\n'    // Unix (the right way)
        )
        { newline(); }
    ;

// Single-line comments
SL_COMMENT
    :    ';'
        (~('\n'|'\r'))*
        { $setType(Token.SKIP);}
    ;

// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
VAR
    :    ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.')*
    ;

INT_CONST
    : ('0'..'9')+ ( '.' ('0'..'9')* (EXPONENT)? { $setType(FLT_CONST); }| EXPONENT { $setType(FLT_CONST); })? (UNIT { $setType(FLT_CONST); })?
    ;

FLT_CONST
    : '.' ('0'..'9')+ (EXPONENT)? (UNIT)?
    ;

HEX_CONST 
    : '#'! ('0'..'9' | 'A'..'F' | 'a'..'f' )+
    ;

BIN_CONST 
    : '%'! ('0'..'1' )+
    ;
        
// a couple protected methods to assist in matching floating point numbers
protected
EXPONENT
    :    ('e'|'E') ('+'|'-')? ('0'..'9')+
    ;

// string literals
STR_CONST
    :    '"'! ( ESCqs | ~('"'|'\\'))* '"'!
    ;

protected
ESCqs
    :
        '\\'! ( '"'
          	  | 'u' a:HEX_DIGIT b:HEX_DIGIT c:HEX_DIGIT d:HEX_DIGIT { char ch=(char) Integer.parseInt(a.getText()+b.getText()+c.getText()+d.getText(),16); $setText(ch); })
    ;

// hexadecimal digit (again, note it's protected!)
protected
HEX_DIGIT
        :       ('0'..'9'|'A'..'F'|'a'..'f')
        ;

protected
UNIT
	:
		('N' | 'U' | 'M' )? ( 'V' | 'A' )
	;