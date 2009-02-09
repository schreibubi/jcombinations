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

grammar EvalGenCombinations;

options {
    output=AST;
    ASTLabelType=DebugCommonTree;
}

tokens {
	VALUESARRAY;
    VALUELIST;
    KEYBLOCK;
    BLOCK;
    UNARY_PLUS;
    UNARY_MINUS;
    LINE;
    POST_INC;
    POST_DEC;
    PRE_DEC;
    PRE_INC;
    ALT;
    KEY;
    VALUE;
    VALUEBLOCK;
    OPTION;
}

@header {
package org.schreibubi.JCombinationsTools.evalGenCombinations;
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileReader;
	import java.util.HashMap;
    import org.schreibubi.JCombinationsTools.FileNameLookup.FileNameLookupSingleton;
}
@lexer::header {
package org.schreibubi.JCombinationsTools.evalGenCombinations;
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileReader;
	import java.util.HashMap;
    import org.schreibubi.JCombinationsTools.FileNameLookup.FileNameLookupSingleton;
}

@lexer::members {
File includeDir=new File(".");
public void setIncludeDir(File dir) {
  includeDir=dir;
}

    class SaveStruct {
      SaveStruct(CharStream input){
        this.input = input;
        this.marker = input.mark();
      }
      public CharStream input;
      public int marker;
     }

     Stack<SaveStruct> includes = new Stack<SaveStruct>();

    // We should override this method for handling EOF of included file
     public Token nextToken(){
       Token token = super.nextToken();

       if(token==Token.EOF_TOKEN && !includes.empty()){
        // We've got EOF and have non empty stack.
         SaveStruct ss = includes.pop();
         setCharStream(ss.input);
         input.rewind(ss.marker);
         token = super.nextToken();
       }

      // Skip first token after switching on another input.
       if(((CommonToken)token).getStartIndex() < 0)
         token = super.nextToken();

       return token;
     }
 }


blocks
    :
        (option)* (block)+ EOF!
    ;

option
	: 'option' STR_CONST STR_CONST -> ^(OPTION STR_CONST+)
	;

block
    : LCURLY line RCURLY -> ^(BLOCK line)
    ;

line
    : (assignment | INCLUDE )+      
    ;

lineeof
    : (assignment | INCLUDE )+ EOF!
    ;

annotation
	: ANNOTATION (LPAREN STR_CONST RPAREN)? -> ^(ANNOTATION STR_CONST)
	;
	
assignment
    	:	(annotation)? variables ASSIGN valuesSet -> ^(ASSIGN annotation? variables valuesSet) 
    ;

variables
    : variableOrBracket (COMMA! variableOrBracket)*
    ;

variableOrBracket
	: ( VAR -> ^(KEY VAR)
	  | LBRACK variables RBRACK -> ^(KEYBLOCK variables)
	  )
	;
	
valuelist
	: LCURLY valuesSet RCURLY -> ^(VALUELIST valuesSet)
    ;

valuesSet
    : valueListOrBracket (COMMA! valueListOrBracket)*
    ;

valueListOrBracket
	: ( relationalExpression -> ^(VALUE relationalExpression)
	  | LCURLY valuesSet RCURLY -> ^(VALUELIST valuesSet)
	  | LBRACK valuesSet RBRACK -> ^(VALUEBLOCK valuesSet)
	  )
	;
	
relationalExpression
    : relationalXORExpression
    ;

relationalXORExpression
    : relationalORExpression ( XOR^ relationalORExpression )* 
    ;

relationalORExpression
    : relationalANDExpression (OR^ relationalANDExpression )* 
    ;

relationalANDExpression
    : relationalNOTExpression ( AND^ relationalNOTExpression )* 
    ;

relationalNOTExpression
    : NOT numericExpression -> ^(NOT numericExpression)
    | numericExpression -> numericExpression
    ;

numericExpression
    :
        numericMultiplicativeExpression ((PLUS^ | MINUS^) numericMultiplicativeExpression)*
    ;


numericMultiplicativeExpression
    :    numericUnaryExpression ((STAR^ | SLASH^ ) numericUnaryExpression)*
    ;

numericUnaryExpression
    :
        ( p=PLUS numericPrimaryExpression -> ^(UNARY_PLUS[$p] numericPrimaryExpression)
        | m=MINUS numericPrimaryExpression -> ^(UNARY_MINUS[$m] numericPrimaryExpression)
        | numericPrimaryExpression -> numericPrimaryExpression)
    ;

numericPrimaryExpression
    :
        constExpression
    |    numericValuedFunctionExpression
    |    LPAREN! relationalExpression RPAREN!
    ;

numericValuedFunctionExpression
    :
      'ABS'^ LPAREN! relationalExpression RPAREN!
    | 'SIN'^ LPAREN! relationalExpression RPAREN!
    | 'COS'^ LPAREN! relationalExpression RPAREN!
    | 'TAN'^ LPAREN! relationalExpression RPAREN!
    | 'ASIN'^ LPAREN! relationalExpression RPAREN!
    | 'ACOS'^ LPAREN! relationalExpression RPAREN!    
    | 'ATAN'^ LPAREN! relationalExpression RPAREN!
    | 'SQRT'^ LPAREN! relationalExpression RPAREN!
    | 'EXP'^ LPAREN! relationalExpression RPAREN!
    | 'LN'^ LPAREN! relationalExpression RPAREN!
    | 'POW'^ LPAREN! relationalExpression COMMA! relationalExpression RPAREN!
    | 'HEX'^ LPAREN! relationalExpression RPAREN!
    | 'UPPERCASE'^ LPAREN! relationalExpression RPAREN!
    ;

constExpression
    :
        FLT_CONST 
    |   INT_CONST 
    |   HEX_CONST 
    |   BIN_CONST 
    |   STR_CONST
    |   i=INC VAR -> ^(PRE_INC[$i] VAR) 
    |   d=DEC VAR -> ^(PRE_DEC[$d] VAR)
    |   VAR r=INC -> ^(POST_INC[$r] VAR) 
    |   VAR s=DEC -> ^(POST_DEC[$s] VAR) 
    |   VAR
    ;
    
COLON             :    ':'        ;
LBRACK            :    '['        ;
RBRACK            :    ']'        ;
COMMA             :    ','        ;
LCURLY            :    '{'        ;
RCURLY            :    '}'        ;
LPAREN            :    '('        ;
RPAREN            :    ')'        ;
ASSIGN            :    '='        ;
SLASH             :    '/'        ;
PLUS              :    '+'        ;
MINUS             :    '-'        ;
STAR              :    '*'        ;
XOR               :    '^'        ;
AND               :    '&'        ;
OR                :    '|'        ;
NOT               :    '~'        ;
INC               :   '++'        ;
DEC               :   '--'        ;

SL_COMMENT
    : ';' ~'\n'* '\n' {$channel=HIDDEN; }
    ;

ML_COMMENT
    : '/*'
      ( options {greedy=false;} : . )*
      '*/'
      {$channel=HIDDEN;}
    ;

WS : ( ' '
     | '\t'
     | '\r'? '\n'
     )+
     { $channel=HIDDEN; }
    ;

VAR
    :    ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.')*
    ;

/*INT_CONST
    : ('0'..'9')+ ( '.' ('0'..'9')* (EXPONENT)? { $setType(FLT_CONST); }| EXPONENT { $setType(FLT_CONST); })? (UNIT { $setType(FLT_CONST); })?
    ;

FLT_CONST
    : '.' ('0'..'9')+ (EXPONENT)? (UNIT)?
    ;
*/

INT_CONST: DECIMAL_DIGIT+ UNIT?;

FLT_CONST: DECIMAL_DIGIT+ '.' DECIMAL_DIGIT+ EXPONENT? UNIT?;

HEX_CONST: '0x' HEX_DIGIT+ ;

BIN_CONST: '%' BINARY_DIGIT+ ;

STR_CONST
    : '\"'
      ( options {greedy=false;}
      : ESCAPE_SEQUENCE
      | ~'\\'
      )*
      '\"'
    ;

ANNOTATION
	: '@' ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.')*
	;
	
fragment
ESCAPE_SEQUENCE
    : '\\' '\"'
    | '\\' '\''
    | '\\' '\\'
    | UNICODE_CHAR
    ;

fragment
DECIMAL_DIGIT
    : '0'..'9'
    ;

fragment
HEX_DIGIT
    : '0'..'9'|'a'..'f'|'A'..'F'
    ;

fragment
BINARY_DIGIT
    : '0'..'1'
    ;

fragment
UNICODE_CHAR
    : '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
        
fragment
EXPONENT
    :    ('e'|'E') ('+'|'-')? ('0'..'9')+
    ;

fragment
UNIT
	:
		('n' | 'u' | 'm' | 'k' | 'M' )? ( 'V' | 'A' | 's' | 'Hz' )
	;
  
INCLUDE
    : 'include' (WS)? f=STR_CONST 
	{
       String name = f.getText();
       name = name.substring(1,name.length()-1);
       try {
        // save current lexer's state
         SaveStruct ss = new SaveStruct(input);
         includes.push(ss);

        // switch on new input stream
         setCharStream(new ANTLRFileStream(FileNameLookupSingleton.getInstance().lookup( includeDir, name ).getAbsolutePath() ));
         reset();

       } catch(Exception fnf) { throw new Error("Cannot open file " + name); }
     }
     ;
  