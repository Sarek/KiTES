grammar TRS;

options {
  language = Java;
}

@header {
  package kites.parser;
  import kites.TRSModel.*;
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
	{ $e = new RuleList(); }
	(rule	{ $e.add($rule.e); }
	)*
	;


rule returns [Rule e]:
	{ $e = new Rule(); }
	fun=function		{ $e.setLeft($fun.e); }
	RARROW
	( var				{ $e.setRight($var.e); }
	| constant			{ $e.setRight($constant.e); }
	| f=function		{ $e.setRight($f.e); }
	) 
	;

function returns [ASTNode e]:
//	options {token
//	  greedy = false;
//	}
	IDENT { $e = new Nonterminal($IDENT.text); }
	LPAR
	(var	{ $e.add($var.e); }
	| constant	{ $e.add($constant.e); }
	| function	{ $e.add($function.e); }
	)+
	RPAR
	;

constant returns [ASTNode e]:
	IDENT { $e = new Terminal($IDENT.text); }
	;
	
var returns [ASTNode e]:
	VAR	{ $e = new Variable($VAR.text); }
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
