grammar TRS;

options {
  language = Java;
}

@header {
  package kites.parser;
  import kites.TRSModel.*;
  import java.util.LinkedList;
}

@members {
  private List<String> errors = new LinkedList<String>();
      public void displayRecognitionError(String[] tokenNames,
                                          RecognitionException e) {
          String hdr = getErrorHeader(e);
          String msg = getErrorMessage(e, tokenNames);
          errors.add(hdr + " " + msg);
      }
      public List<String> getErrors() {
          return errors;
      }
}

@lexer::header {
  package kites.parser;
  
  import java.util.LinkedList;
  import java.io.File;
}

@lexer::members {
    private List<String> errors = new LinkedList<String>();
      public void displayRecognitionError(String[] tokenNames,
                                          RecognitionException e) {
          String hdr = getErrorHeader(e);
          String msg = getErrorMessage(e, tokenNames);
          errors.add(hdr + " " + msg);
      }
      public List<String> getErrors() {
          return errors;
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
	| INCLUDE
	)*
	;
	
instance returns [ASTNode e]:
  ( function  { $e = $function.e; }
  | constant  { $e = $constant.e; }
  )
  ;

INCLUDE:
	'#include' (WS)? f=FILENAME
	{
       String name = f.getText();
       try {
          // save current lexer's state
          SaveStruct ss = new SaveStruct(input);
          includes.push(ss);
         
          // replace path delimiters by the correct, platform-dependent ones
          name = name.replace("/", File.separator);
          System.out.println(name);

          // switch on new input stream
          setCharStream(new ANTLRFileStream(name));
          reset();
       } catch(Exception fnf) { throw new Error("Cannot open file " + name); }
     }
     ;
	
	 
rule returns [Rule e]:
	{ $e = new Rule(); }
	( v1=var         { $e.setLeft($v1.e); }
	| c1=constant    { $e.setLeft($c1.e); }
	| f1= function	 { $e.setLeft($f1.e); }
	)
	RARROW
	( v2=var         { $e.setRight($v2.e); }
	| c2=constant    { $e.setRight($c2.e); }
	| f2=function    { $e.setRight($f2.e); }
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
	
FILENAME:
  ( 'a'..'z'
  | 'A'..'Z'
  | '0'..'9'
  | 'Ä' | 'ä'
  | 'Ö' | 'ö'
  | 'Ü' | 'ü'
  | 'ß'
  | '.'
  | '-'
  | '_'
  | '/'
  | '\\'
  | '~'
  | '+'
  | '*'
  )+
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
