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

@lexer::members {
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

       if(token.getType() == Token.EOF && !includes.empty()){
        // We've got EOF and have non empty stack.
         SaveStruct ss = includes.pop();
         setCharStream(ss.input);
         input.rewind(ss.marker);
         //this should be used instead of super [like below] to handle exits from nested includes
         //it matters, when the 'include' token is the last in previous stream (using super, lexer 'crashes' returning EOF token)
         token = this.nextToken();
       }

      // Skip first token after switching on another input.
      // You need to use this rather than super as there may be nested include files
       if(((CommonToken)token).getStartIndex() < 0)
         token = this.nextToken();

       return token;
     }
 }


rulelist returns [RuleList e]:
	{ $e = new RuleList(); }
	( rule	{ $e.add($rule.e); }
	| include
	)*
	;

include:
	INC f=IDENT
	{
       String name = f.getText();
       name = name.substring(1,name.length()-1);
       try {
        // save current lexer's state
         SaveStruct ss = new SaveStruct(input);
         includes.push(ss);

        // switch on new input stream
         setCharStream(new ANTLRFileStream(name));
         reset();

       } catch(Exception fnf) { throw new Error("Cannot open file " + name); }
     }
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
	IDENT { $e = new Function($IDENT.text); }
	LPAR
	(var	{ $e.add($var.e); }
	| constant	{ $e.add($constant.e); }
	| f=function	{ $e.add($f.e); }
	)+
	RPAR
	;

constant returns [ASTNode e]:
	IDENT { $e = new Constant($IDENT.text); }
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
	
INC:
	('#include' | '#INCLUDE' | '#Include')
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
