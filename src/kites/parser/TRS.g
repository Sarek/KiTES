grammar TRS;

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

rulelist returns [RuleList e]:
	{ RuleList e = new RuleList(); }
	(rule	{ e.add($rule); }
	)*
	;

instance returns [ASTNode e]:
	nonterminal
	;
	
rule returns [Rule e]:
	{ Rule e = new Rule(); }
	nonterminal		{ e.setLeft($nonterminal); }
	RARROW
	right=(var | terminal | nonterminal) { e.setRight($right); }
	;

nonterminal returns [ASTNode e]:
//	options {
//	  greedy = false;
//	}
	:
	{ $e = new Nonterminal(); }
	IDENT { $e.setName($IDENT.text); }
	LPAR
	(var	{ $e.add($var); }
	| terminal	{ $e.add($terminal); }
	| nonterminal	{ $e.add($nonterminal); }
	)+
	RPAR
	;

terminal returns [ASTNode e]:
	{ Terminal e = new Terminal(); }
	IDENT { e.setName($IDENT.text); }
	;
	
var returns [ASTNode e]:
	{ Variable e = new Variable(); }
	VAR	{ e.setName($IDENT.text); }
	;


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
	
LPAR:
	'('
	;
	
RPAR:
	')'
	;
	
RARROW:
	'-->'
	;
