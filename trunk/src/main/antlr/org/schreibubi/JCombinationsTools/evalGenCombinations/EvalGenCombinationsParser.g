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
package org.schreibubi.JCombinationsTools.evalGenCombinations;
    import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileReader;
    import antlr.ASTFactory;
    import antlr.RecognitionException;
    import antlr.TokenStreamException;
    import antlr.collections.AST;
    import antlr.debug.misc.ASTFrame;   
    import org.schreibubi.JCombinationsTools.FileNameLookup.FileNameLookupSingleton;
}

options { 
    language="Java"; 
}

class EvalGenCombinationsParser extends Parser;
options {
    k=2;
    exportVocab=EvalGenCombinations;        
    defaultErrorHandler = false;
    buildAST = true;
    genHashLines = true;
}
tokens {
	VALUESARRAY;
    VALUELIST;
    KEYLIST;
    BLOCK;
    INCLUDE;
    UNARY_PLUS;
    UNARY_MINUS;
    LINE;
    POST_INC;
    POST_DEC;
    PRE_DEC;
    PRE_INC;
    OPTION;
}
{

File includeDir=new File(".");
public void setIncludeDir(File dir) {
  includeDir=dir;
}

}

blocks
    :
        (option)* (block | includeOutBlock)+ EOF!
    ;

	
option
	: "option"! STR_CONST STR_CONST
    {#option = #(#[OPTION,"OPTION"],#option);}
	;
	
block
    : LCURLY! line RCURLY!
    {#block = #(#[BLOCK,"BLOCK"],#block);}
    ;

line
    : (assignment | includeInBlock )+      
    ;

lineeof
    : (assignment | includeInBlock )+ EOF!
    ;

includeInBlock!
    : "include" s:STR_CONST
    {
  		String name=s.getText();
    	try {
        	EvalGenCombinationsLexer lexer = new EvalGenCombinationsLexer(new BufferedReader( new FileReader( FileNameLookupSingleton.getInstance().lookup( includeDir, name ) ) ) );
        	EvalGenCombinationsParser parser = new EvalGenCombinationsParser( lexer );
        	parser.lineeof();
        	
	    	#includeInBlock = parser.getAST();
    	} catch ( TokenStreamException e ) {
            System.out.println( "EvalGenCombinations "+name+" TokenStreamException: " + e );
        } catch ( RecognitionException e ) {
            System.out.println( "EvalGenCombinations "+name+" RecognitionException: " + e );
        } catch ( Exception e ) {
            System.out.println( "EvalGenCombinations "+name+" error: " + e );
            e.printStackTrace();
        }
    }
    ;

includeOutBlock!
    : "include" s:STR_CONST
    {
  		String name=s.getText();
    	try {
        	EvalGenCombinationsLexer lexer = new EvalGenCombinationsLexer(new BufferedReader( new FileReader( FileNameLookupSingleton.getInstance().lookup( includeDir, name ) ) ) );
        	EvalGenCombinationsParser parser = new EvalGenCombinationsParser( lexer );
        	parser.blocks();
        	
	    	#includeOutBlock = parser.getAST();
    	} catch ( TokenStreamException e ) {
            System.out.println( "EvalGenCombinations "+name+" TokenStreamException: " + e );
        } catch ( RecognitionException e ) {
            System.out.println( "EvalGenCombinations "+name+" RecognitionException: " + e );
        } catch ( Exception e ) {
            System.out.println( "EvalGenCombinations "+name+" error: " + e );
            e.printStackTrace();
        }
    }
    ;
    
assignment
    : variable ASSIGN^ valuesListArray 
    | variableslist ASSIGN^ valuesBracketList 
    ;

variableslist
    : LBRACK! variables RBRACK!
    {#variableslist = #(#[KEYLIST,"KEYLIST"],#variableslist);}
    ;

variables
    : variableOrBracket (COMMA! variableOrBracket)*
    ;

variableOrBracket
	: (VAR | variableslist)
	;
	
variable
    : VAR
    {#variable = #(#[KEYLIST,"KEYLIST"],#variable);}
    ;

valuesBracketList
    : valuesBracket (COMMA! valuesBracket)*
    ;

valuesBracket
    : LBRACK! valuesSet RBRACK!
    ;

valuesSet
    : valueOrBracket (COMMA! valueOrBracket)*
    {#valuesSet = #(#[VALUELIST,"VALUELIST"],#valuesSet);}
    ;

valueOrBracket
	: ( relationalExpression | valuesBracket )
	;

valuesListArray
	: ( (valuesArray) => valuesArray | valuesList  )
	;
	
valuesList
    : values (COMMA! values)*
    ;

valuesArray
	: relationalExpression COLON! relationalExpression COLON! relationalExpression
//	: arrayConst COLON! arrayConst COLON! arrayConst
    {#valuesArray = #(#[VALUESARRAY,"VALUESARRAY"],#valuesArray);}
	;

/* arrayConst
    :   
    ( (
            p:PLUS^    {#p.setType(UNARY_PLUS);}
        |    m:MINUS^    {#m.setType(UNARY_MINUS);}
        )?  FLT_CONST 
    |   INT_CONST 
    |   HEX_CONST 
    |   BIN_CONST 
    |   VAR )
	; */
	
values
    : relationalExpression
    {#values = #(#[VALUELIST,"VALUELIST"],#values);}
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
    | "ASIN"^ LPAREN! relationalExpression RPAREN!
    | "ACOS"^ LPAREN! relationalExpression RPAREN!    
    | "ATAN"^ LPAREN! relationalExpression RPAREN!
    | "SQRT"^ LPAREN! relationalExpression RPAREN!
    | "EXP"^ LPAREN! relationalExpression RPAREN!
    | "LN"^ LPAREN! relationalExpression RPAREN!
    | "POW"^ LPAREN! relationalExpression COMMA! relationalExpression RPAREN!
    | "HEX"^ LPAREN! relationalExpression RPAREN!
    | "UPPERCASE"^ LPAREN! relationalExpression RPAREN!
    ;

constExpression
    :
        FLT_CONST 
    |   INT_CONST 
    |   HEX_CONST 
    |   BIN_CONST 
    |   STR_CONST
    |   (p:INC^ { #p.setType(PRE_INC); } | q:DEC^ { #q.setType(PRE_DEC); } ) VAR
    |   (VAR (INC|DEC)) => VAR (r:INC^ { #r.setType(POST_INC); } | s:DEC^ { #s.setType(POST_DEC); } )
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




class EvalGenCombinationsLexer extends Lexer;

options {
    k=2;
    testLiterals=true;      // automatically test for literals
    importVocab=EvalGenCombinations;
    caseSensitive=true;
    caseSensitiveLiterals = false;
    charVocabulary = '\u0000'..'\uFFFE';
}

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
INC               :   "++"        ;
DEC               :   "--"        ;


// Whitespace -- ignored
WS    :
        (    ' '
        |    '\t'
        |    '\f'
        )
        { $setType(Token.SKIP); }
    ;

EOL    :
        (    "\r\n"  // Evil DOS
        |    '\r'    // Macintosh
        |    '\n'    // Unix (the right way)
        )
        { newline(); $setType(Token.SKIP); }
    ;

// Single-line comments
SL_COMMENT
    :    ";"
        (~('\n'|'\r'))*
        { $setType(Token.SKIP);}
    ;

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
    : "0x"! ('0'..'9' | 'A'..'F' | 'a'..'f' )+
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
        :       '"'! ( ESCqs | ~('"'|'\\'))* '"'!
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
  
