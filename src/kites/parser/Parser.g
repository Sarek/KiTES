grammar Parser;

options {
  language = Java;
}

@header {
  package kites.parser;
}

@lexer::header {
  package kites.parser;
}

//baum:
//	include
//	program
//	instantiation
//	; 
	
//program:
//	'program:'
//	rule*
//	;

ruletree:
	rule*
	;

instance:
	nonterminal*
	;
	
rule:
	 nonterminal
	 '-->'
	 (var | terminal | nonterminal)
	 ;

nonterminal
//	options {
//	  greedy = false;
//	}
	:
	IDENT
	'('
	(var | terminal | nonterminal)+
	')'
	;

terminal:
	IDENT;
var:
	VAR;


/*
 * Lexer rules
 *
 */
IDENT:
	'a'..'z'
	('a'..'z' | 'A'..'Z' | '0'..'9')*
	;
	
VAR:
	'A'..'Z'
	('a'..'z' | 'A'..'Z' | '0'..'9')*
	;

WS:
	(' ' | ',' | '\n' | '\r' | '\t' | '\f')+
	
	{$channel = HIDDEN;}
	; // \r\n matches rules 3,2

COMMENT:
	 '//'
	 .*
	 ('\n' | '\r')+
	 {$channel = HIDDEN;};
	 
MULTI_COMMENT:
	'/*'
	.*
	'*/'
	{$channel = HIDDEN;};
