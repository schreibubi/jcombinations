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
package org.schreibubi.JCombinationsTools.mergeResults;
    import java.util.*;
    import java.lang.*;
    import org.schreibubi.symbol.*;
    import org.schreibubi.visitor.*;
    import java.text.DecimalFormat;
  	import java.text.NumberFormat;
	  import java.util.Locale;
}

options { language="Java"; }


class DclogParser extends Parser;
options {
    k = 2;                // two token lookahead
    exportVocab=Dclog;        // Call its vocabulary "Matlab"
    defaultErrorHandler = false;     // Don't generate parser error handlers
    buildAST = false;
    genHashLines = false;
}
{
    VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> tdata;
	int dStart=0;
	int dStop=0;
	int dOffset=0;
}

file [VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> data, int dutStart, int dutStop, int dutOffset]
{tdata=data; dStart=dutStart; dStop=dutStop; dOffset=dutOffset;}
    :
        body EOF!
    ;



body
	:
		(data)*
	;
		
	
data
{
	Symbol s;
	int d;
}
	:
		ID COLON! // DATASET
		ID COLON! // TDWN
		ID COLON! // R
		ID COLON! // DEVCNT
		(ID)? COLON! // MR
		(ID)? COLON! // CN
		(ID)? COLON! // DN
		(ID)? COLON! // DB
		ID COLON! // TNAME
		d=intval COLON! // DUT
		(ID)? COLON! // SERIALNUMBER
		(ID)* COLON! // LOTID
		(ID)* COLON! // DESIGN
		(ID)? COLON! // WF
		(ID)? COLON! // X
		(ID)? COLON! // Y
		(ID)? COLON! // PS
		(ID)? SEMI! // LOC
		n:ID COMMA! // Long TESTNAME
		s=flterr eol {
			if ((d>=dStart) & (d<=dStop)) {
				VLinkedHashMap<VLinkedHashMap<Symbol>> h;
				if (tdata.containsKey(n.getText())) {
					h=tdata.get(n.getText());
				} else {
					h=new VLinkedHashMap<VLinkedHashMap<Symbol>>();
				} 
				NumberFormat nf = NumberFormat.getNumberInstance( Locale.US );
				DecimalFormat myFormatter = ( DecimalFormat ) nf;
				myFormatter.applyPattern( "00" );			
				s.setName("DUT"+myFormatter.format( d+dOffset ));
				VLinkedHashMap<Symbol> sublist=new VLinkedHashMap<Symbol>();
				sublist.put("S0",s);
				h.put("DUT"+myFormatter.format( d+dOffset ),sublist);
				tdata.put(n.getText(),h);
			}
		}
	;
		
flterr returns [Symbol s]
	:
		( f:ID {s=new SymbolDouble(f.getText());} )
	;
			

intval returns [int i]
	:
		( f:ID { i=Integer.parseInt(f.getText()); })
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



class DclogLexer extends Lexer;

options {
    testLiterals=true;      // automatically test for literals
    k=4;                    // two characters of lookahead
    importVocab=Dclog;
    caseSensitive=true;
    caseSensitiveLiterals = false;
    charVocabulary = '\u0000'..'\uFFFE';
}


SEMI             :    ';'        ;
COLON            :    ':'        ;
COMMA			 :    ','        ;

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


ID
    :    ('a'..'z'|'A'..'Z'|'0'..'9'|'+'|'-'|'.')+
    ;

