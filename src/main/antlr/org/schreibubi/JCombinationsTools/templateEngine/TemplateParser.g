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
package org.schreibubi.JCombinationsTools.templateEngine;
}

options { 
    language="Java"; 
}

/** Parser for Template */
class TemplateParser extends Parser;
options {
    k = 2;                // two token lookahead
    exportVocab=Template;        // Call its vocabulary "Matlab"
    defaultErrorHandler = false;     // Don't generate parser error handlers
    buildAST = true;
    genHashLines = true;
}

statements
    :
        (LITERAL | TEMPLATE_VAR (FORMAT_VAR)? )* EOF!
    ;
    

class TemplateLexer extends Lexer;

options {
    k=3;                    // two characters of lookahead
    importVocab=Template;
    caseSensitive=true;
    charVocabulary = '\u0000'..'\uFFFE';
}

LITERAL
    :  ( (ESC |~( '$' | '%' | '\\' ) ) )+
    ;

TEMPLATE_VAR
    :    '$'! ( (ESCqs) => ESCqs | ~('$') )* '$'!
    ;

FORMAT_VAR
    :    '%'! ( (ESCqs) => ESCqs | ~('%') )* '%'!
    ;

protected
ESC
    :
        '\\'! ( '$' | '%' | '\\' )
    ;

protected
ESCqs
        :
                '\\'! '$'                
        ;

protected
ESCds
        :
                '\\'! '%'
        ;
        