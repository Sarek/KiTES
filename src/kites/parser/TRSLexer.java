// $ANTLR 3.0.1 /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g 2010-09-03 11:58:36

  package kites.parser;
  
  import java.util.LinkedList;
  import java.io.File;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TRSLexer extends Lexer {
    public static final int INC=12;
    public static final int WS=5;
    public static final int LPAR=9;
    public static final int FILENAME=6;
    public static final int RARROW=7;
    public static final int INCLUDE=4;
    public static final int MULTI_COMMENT=14;
    public static final int IDENT=8;
    public static final int RPAR=10;
    public static final int VAR=11;
    public static final int T15=15;
    public static final int COMMENT=13;
    public static final int Tokens=16;
    public static final int EOF=-1;

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
     
    public TRSLexer() {;} 
    public TRSLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g"; }

    // $ANTLR start T15
    public final void mT15() throws RecognitionException {
        try {
            int _type = T15;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:57:5: ( '#instance' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:57:7: '#instance'
            {
            match("#instance"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T15

    // $ANTLR start INCLUDE
    public final void mINCLUDE() throws RecognitionException {
        try {
            int _type = INCLUDE;
            Token f=null;

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:93:8: ( '#include' ( WS )? f= FILENAME )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:94:2: '#include' ( WS )? f= FILENAME
            {
            match("#include"); 

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:94:13: ( WS )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>='\t' && LA1_0<='\n')||(LA1_0>='\f' && LA1_0<='\r')||LA1_0==' '||LA1_0==',') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:94:14: WS
                    {
                    mWS(); 

                    }
                    break;

            }

            int fStart53 = getCharIndex();
            mFILENAME(); 
            f = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, fStart53, getCharIndex()-1);

                   String name = f.getText();
                   try {
                      // save current lexer's state
                      SaveStruct ss = new SaveStruct(input);
                      includes.push(ss);
                     
                      // replace path delimiters by the correct, platform-dependent ones
                      name = name.replace("/", File.separator);
                      name = name.replace("\\", File.separator);
                      System.out.println(name);

                      // switch on new input stream
                      setCharStream(new ANTLRFileStream(name));
                      reset();
                   } catch(Exception fnf) { throw new Error("Cannot open file " + name); }
                 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INCLUDE

    // $ANTLR start IDENT
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:154:6: ( 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )* )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:155:2: 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            {
            matchRange('a','z'); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:156:2: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IDENT

    // $ANTLR start INC
    public final void mINC() throws RecognitionException {
        try {
            int _type = INC;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:159:4: ( ( '#include' | '#INCLUDE' | '#Include' ) )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:160:2: ( '#include' | '#INCLUDE' | '#Include' )
            {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:160:2: ( '#include' | '#INCLUDE' | '#Include' )
            int alt3=3;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='#') ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1=='I') ) {
                    int LA3_2 = input.LA(3);

                    if ( (LA3_2=='n') ) {
                        alt3=3;
                    }
                    else if ( (LA3_2=='N') ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("160:2: ( '#include' | '#INCLUDE' | '#Include' )", 3, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA3_1=='i') ) {
                    alt3=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("160:2: ( '#include' | '#INCLUDE' | '#Include' )", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("160:2: ( '#include' | '#INCLUDE' | '#Include' )", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:160:3: '#include'
                    {
                    match("#include"); 


                    }
                    break;
                case 2 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:160:16: '#INCLUDE'
                    {
                    match("#INCLUDE"); 


                    }
                    break;
                case 3 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:160:29: '#Include'
                    {
                    match("#Include"); 


                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INC

    // $ANTLR start VAR
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:163:4: ( 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )* )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:164:2: 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            {
            matchRange('A','Z'); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:165:2: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end VAR

    // $ANTLR start FILENAME
    public final void mFILENAME() throws RecognitionException {
        try {
            int _type = FILENAME;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:168:9: ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | 'Ä' | 'ä' | 'Ö' | 'ö' | 'Ü' | 'ü' | 'ß' | '.' | '-' | '_' | '/' | '\\\\' | '~' | '+' | '*' )+ )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:169:3: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | 'Ä' | 'ä' | 'Ö' | 'ö' | 'Ü' | 'ü' | 'ß' | '.' | '-' | '_' | '/' | '\\\\' | '~' | '+' | '*' )+
            {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:169:3: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | 'Ä' | 'ä' | 'Ö' | 'ö' | 'Ü' | 'ü' | 'ß' | '.' | '-' | '_' | '/' | '\\\\' | '~' | '+' | '*' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='*' && LA5_0<='+')||(LA5_0>='-' && LA5_0<='9')||(LA5_0>='A' && LA5_0<='Z')||LA5_0=='\\'||LA5_0=='_'||(LA5_0>='a' && LA5_0<='z')||LA5_0=='~'||LA5_0=='\u00C4'||LA5_0=='\u00D6'||LA5_0=='\u00DC'||LA5_0=='\u00DF'||LA5_0=='\u00E4'||LA5_0=='\u00F6'||LA5_0=='\u00FC') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:
            	    {
            	    if ( (input.LA(1)>='*' && input.LA(1)<='+')||(input.LA(1)>='-' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='\\'||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||input.LA(1)=='~'||input.LA(1)=='\u00C4'||input.LA(1)=='\u00D6'||input.LA(1)=='\u00DC'||input.LA(1)=='\u00DF'||input.LA(1)=='\u00E4'||input.LA(1)=='\u00F6'||input.LA(1)=='\u00FC' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FILENAME

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:187:3: ( ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+ )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:188:2: ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+
            {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:188:2: ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\t' && LA6_0<='\n')||(LA6_0>='\f' && LA6_0<='\r')||LA6_0==' '||LA6_0==',') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' '||input.LA(1)==',' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            channel = HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WS

    // $ANTLR start COMMENT
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:193:8: ( '//' ( . )* ( '\\n' | '\\r' )+ )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:194:3: '//' ( . )* ( '\\n' | '\\r' )+
            {
            match("//"); 

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:195:3: ( . )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\n'||LA7_0=='\r') ) {
                    alt7=2;
                }
                else if ( ((LA7_0>='\u0000' && LA7_0<='\t')||(LA7_0>='\u000B' && LA7_0<='\f')||(LA7_0>='\u000E' && LA7_0<='\uFFFE')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:195:3: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:196:3: ( '\\n' | '\\r' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='\n'||LA8_0=='\r') ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:
            	    {
            	    if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

            channel = HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMENT

    // $ANTLR start MULTI_COMMENT
    public final void mMULTI_COMMENT() throws RecognitionException {
        try {
            int _type = MULTI_COMMENT;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:199:14: ( '/*' ( . )* '*/' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:200:2: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:201:2: ( . )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='*') ) {
                    int LA9_1 = input.LA(2);

                    if ( (LA9_1=='/') ) {
                        alt9=2;
                    }
                    else if ( ((LA9_1>='\u0000' && LA9_1<='.')||(LA9_1>='0' && LA9_1<='\uFFFE')) ) {
                        alt9=1;
                    }


                }
                else if ( ((LA9_0>='\u0000' && LA9_0<=')')||(LA9_0>='+' && LA9_0<='\uFFFE')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:201:2: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            match("*/"); 

            channel = HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end MULTI_COMMENT

    // $ANTLR start LPAR
    public final void mLPAR() throws RecognitionException {
        try {
            int _type = LPAR;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:205:5: ( '(' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:206:2: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LPAR

    // $ANTLR start RPAR
    public final void mRPAR() throws RecognitionException {
        try {
            int _type = RPAR;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:209:5: ( ')' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:210:2: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RPAR

    // $ANTLR start RARROW
    public final void mRARROW() throws RecognitionException {
        try {
            int _type = RARROW;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:213:7: ( '-->' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:214:2: '-->'
            {
            match("-->"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RARROW

    public void mTokens() throws RecognitionException {
        // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:8: ( T15 | INCLUDE | IDENT | INC | VAR | FILENAME | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW )
        int alt10=12;
        alt10 = dfa10.predict(input);
        switch (alt10) {
            case 1 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:10: T15
                {
                mT15(); 

                }
                break;
            case 2 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:14: INCLUDE
                {
                mINCLUDE(); 

                }
                break;
            case 3 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:22: IDENT
                {
                mIDENT(); 

                }
                break;
            case 4 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:28: INC
                {
                mINC(); 

                }
                break;
            case 5 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:32: VAR
                {
                mVAR(); 

                }
                break;
            case 6 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:36: FILENAME
                {
                mFILENAME(); 

                }
                break;
            case 7 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:45: WS
                {
                mWS(); 

                }
                break;
            case 8 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:48: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 9 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:56: MULTI_COMMENT
                {
                mMULTI_COMMENT(); 

                }
                break;
            case 10 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:70: LPAR
                {
                mLPAR(); 

                }
                break;
            case 11 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:75: RPAR
                {
                mRPAR(); 

                }
                break;
            case 12 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:80: RARROW
                {
                mRARROW(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\2\uffff\1\15\1\16\1\11\1\uffff\1\11\5\uffff\1\15\2\uffff\1\16\3"+
        "\11\1\uffff\1\11\1\uffff\2\11\4\uffff\1\11\3\uffff\1\13\1\uffff";
    static final String DFA10_eofS =
        "\42\uffff";
    static final String DFA10_minS =
        "\1\11\1\111\3\52\1\uffff\1\55\3\uffff\1\156\1\uffff\1\52\2\uffff"+
        "\1\52\2\0\1\76\1\143\1\0\1\uffff\2\0\3\uffff\1\154\1\0\1\165\1\144"+
        "\1\145\1\11\1\uffff";
    static final String DFA10_maxS =
        "\1\u00fc\1\151\2\u00fc\1\57\1\uffff\1\55\3\uffff\1\156\1\uffff\1"+
        "\u00fc\2\uffff\1\u00fc\2\ufffe\1\76\1\163\1\ufffe\1\uffff\2\ufffe"+
        "\3\uffff\1\154\1\ufffe\1\165\1\144\1\145\1\u00fc\1\uffff";
    static final String DFA10_acceptS =
        "\5\uffff\1\7\1\uffff\1\12\1\13\1\6\1\uffff\1\4\1\uffff\1\3\1\5\6"+
        "\uffff\1\11\2\uffff\1\10\1\14\1\1\6\uffff\1\2";
    static final String DFA10_specialS =
        "\42\uffff}>";
    static final String[] DFA10_transitionS = {
            "\2\5\1\uffff\2\5\22\uffff\1\5\2\uffff\1\1\4\uffff\1\7\1\10\2"+
            "\11\1\5\1\6\1\11\1\4\12\11\7\uffff\32\3\1\uffff\1\11\2\uffff"+
            "\1\11\1\uffff\32\2\3\uffff\1\11\105\uffff\1\11\21\uffff\1\11"+
            "\5\uffff\1\11\2\uffff\1\11\4\uffff\1\11\21\uffff\1\11\5\uffff"+
            "\1\11",
            "\1\13\37\uffff\1\12",
            "\2\11\1\uffff\3\11\12\14\7\uffff\32\14\1\uffff\1\11\2\uffff"+
            "\1\11\1\uffff\32\14\3\uffff\1\11\105\uffff\1\11\21\uffff\1\11"+
            "\5\uffff\1\11\2\uffff\1\11\4\uffff\1\11\21\uffff\1\11\5\uffff"+
            "\1\11",
            "\2\11\1\uffff\3\11\12\17\7\uffff\32\17\1\uffff\1\11\2\uffff"+
            "\1\11\1\uffff\32\17\3\uffff\1\11\105\uffff\1\11\21\uffff\1\11"+
            "\5\uffff\1\11\2\uffff\1\11\4\uffff\1\11\21\uffff\1\11\5\uffff"+
            "\1\11",
            "\1\20\4\uffff\1\21",
            "",
            "\1\22",
            "",
            "",
            "",
            "\1\23",
            "",
            "\2\11\1\uffff\3\11\12\14\7\uffff\32\14\1\uffff\1\11\2\uffff"+
            "\1\11\1\uffff\32\14\3\uffff\1\11\105\uffff\1\11\21\uffff\1\11"+
            "\5\uffff\1\11\2\uffff\1\11\4\uffff\1\11\21\uffff\1\11\5\uffff"+
            "\1\11",
            "",
            "",
            "\2\11\1\uffff\3\11\12\17\7\uffff\32\17\1\uffff\1\11\2\uffff"+
            "\1\11\1\uffff\32\17\3\uffff\1\11\105\uffff\1\11\21\uffff\1\11"+
            "\5\uffff\1\11\2\uffff\1\11\4\uffff\1\11\21\uffff\1\11\5\uffff"+
            "\1\11",
            "\52\25\1\24\1\26\1\25\15\26\7\25\32\26\1\25\1\26\2\25\1\26\1"+
            "\25\32\26\3\25\1\26\105\25\1\26\21\25\1\26\5\25\1\26\2\25\1"+
            "\26\4\25\1\26\21\25\1\26\5\25\1\26\uff02\25",
            "\52\30\2\27\1\30\15\27\7\30\32\27\1\30\1\27\2\30\1\27\1\30\32"+
            "\27\3\30\1\27\105\30\1\27\21\30\1\27\5\30\1\27\2\30\1\27\4\30"+
            "\1\27\21\30\1\27\5\30\1\27\uff02\30",
            "\1\31",
            "\1\33\17\uffff\1\32",
            "\52\25\1\24\1\26\1\25\2\26\1\34\12\26\7\25\32\26\1\25\1\26\2"+
            "\25\1\26\1\25\32\26\3\25\1\26\105\25\1\26\21\25\1\26\5\25\1"+
            "\26\2\25\1\26\4\25\1\26\21\25\1\26\5\25\1\26\uff02\25",
            "",
            "\52\25\1\24\1\26\1\25\15\26\7\25\32\26\1\25\1\26\2\25\1\26\1"+
            "\25\32\26\3\25\1\26\105\25\1\26\21\25\1\26\5\25\1\26\2\25\1"+
            "\26\4\25\1\26\21\25\1\26\5\25\1\26\uff02\25",
            "\52\30\2\27\1\30\15\27\7\30\32\27\1\30\1\27\2\30\1\27\1\30\32"+
            "\27\3\30\1\27\105\30\1\27\21\30\1\27\5\30\1\27\2\30\1\27\4\30"+
            "\1\27\21\30\1\27\5\30\1\27\uff02\30",
            "",
            "",
            "",
            "\1\35",
            "\52\25\1\24\1\26\1\25\15\26\7\25\32\26\1\25\1\26\2\25\1\26\1"+
            "\25\32\26\3\25\1\26\105\25\1\26\21\25\1\26\5\25\1\26\2\25\1"+
            "\26\4\25\1\26\21\25\1\26\5\25\1\26\uff02\25",
            "\1\36",
            "\1\37",
            "\1\40",
            "\2\41\1\uffff\2\41\22\uffff\1\41\11\uffff\20\41\7\uffff\32\41"+
            "\1\uffff\1\41\2\uffff\1\41\1\uffff\32\41\3\uffff\1\41\105\uffff"+
            "\1\41\21\uffff\1\41\5\uffff\1\41\2\uffff\1\41\4\uffff\1\41\21"+
            "\uffff\1\41\5\uffff\1\41",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T15 | INCLUDE | IDENT | INC | VAR | FILENAME | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );";
        }
    }
 

}