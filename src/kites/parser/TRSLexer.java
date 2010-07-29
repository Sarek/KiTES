// $ANTLR 3.0.1 /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g 2010-07-29 16:24:52

  package kites.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TRSLexer extends Lexer {
    public static final int INC=11;
    public static final int WS=5;
    public static final int LPAR=8;
    public static final int RARROW=7;
    public static final int INCLUDE=4;
    public static final int MULTI_COMMENT=13;
    public static final int IDENT=6;
    public static final int RPAR=9;
    public static final int VAR=10;
    public static final int COMMENT=12;
    public static final int Tokens=14;
    public static final int EOF=-1;

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

    // $ANTLR start INCLUDE
    public final void mINCLUDE() throws RecognitionException {
        try {
            int _type = INCLUDE;
            Token f=null;

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:59:8: ( '#include' ( WS )? f= IDENT )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:60:2: '#include' ( WS )? f= IDENT
            {
            match("#include"); 

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:60:13: ( WS )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>='\t' && LA1_0<='\n')||(LA1_0>='\f' && LA1_0<='\r')||LA1_0==' '||LA1_0==',') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:60:14: WS
                    {
                    mWS(); 

                    }
                    break;

            }

            int fStart44 = getCharIndex();
            mIDENT(); 
            f = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, fStart44, getCharIndex()-1);

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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:114:6: ( 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )* )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:115:2: 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            {
            matchRange('a','z'); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:116:2: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:119:4: ( ( '#include' | '#INCLUDE' | '#Include' ) )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:120:2: ( '#include' | '#INCLUDE' | '#Include' )
            {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:120:2: ( '#include' | '#INCLUDE' | '#Include' )
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
                            new NoViableAltException("120:2: ( '#include' | '#INCLUDE' | '#Include' )", 3, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA3_1=='i') ) {
                    alt3=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("120:2: ( '#include' | '#INCLUDE' | '#Include' )", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("120:2: ( '#include' | '#INCLUDE' | '#Include' )", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:120:3: '#include'
                    {
                    match("#include"); 


                    }
                    break;
                case 2 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:120:16: '#INCLUDE'
                    {
                    match("#INCLUDE"); 


                    }
                    break;
                case 3 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:120:29: '#Include'
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:123:4: ( 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )* )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:124:2: 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            {
            matchRange('A','Z'); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:125:2: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
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

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:128:3: ( ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+ )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:129:2: ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+
            {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:129:2: ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\t' && LA5_0<='\n')||(LA5_0>='\f' && LA5_0<='\r')||LA5_0==' '||LA5_0==',') ) {
                    alt5=1;
                }


                switch (alt5) {
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
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:134:8: ( '//' ( . )* ( '\\n' | '\\r' )+ )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:135:3: '//' ( . )* ( '\\n' | '\\r' )+
            {
            match("//"); 

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:136:3: ( . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\n'||LA6_0=='\r') ) {
                    alt6=2;
                }
                else if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFE')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:136:3: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:137:3: ( '\\n' | '\\r' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\n'||LA7_0=='\r') ) {
                    alt7=1;
                }


                switch (alt7) {
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
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:140:14: ( '/*' ( . )* '*/' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:141:2: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:142:2: ( . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFE')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFE')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:142:2: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:146:5: ( '(' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:147:2: '('
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:150:5: ( ')' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:151:2: ')'
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:154:7: ( '-->' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:155:2: '-->'
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
        // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:8: ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW )
        int alt9=10;
        switch ( input.LA(1) ) {
        case '#':
            {
            int LA9_1 = input.LA(2);

            if ( (LA9_1=='I') ) {
                alt9=3;
            }
            else if ( (LA9_1=='i') ) {
                int LA9_10 = input.LA(3);

                if ( (LA9_10=='n') ) {
                    int LA9_13 = input.LA(4);

                    if ( (LA9_13=='c') ) {
                        int LA9_14 = input.LA(5);

                        if ( (LA9_14=='l') ) {
                            int LA9_15 = input.LA(6);

                            if ( (LA9_15=='u') ) {
                                int LA9_16 = input.LA(7);

                                if ( (LA9_16=='d') ) {
                                    int LA9_17 = input.LA(8);

                                    if ( (LA9_17=='e') ) {
                                        int LA9_18 = input.LA(9);

                                        if ( ((LA9_18>='\t' && LA9_18<='\n')||(LA9_18>='\f' && LA9_18<='\r')||LA9_18==' '||LA9_18==','||(LA9_18>='a' && LA9_18<='z')) ) {
                                            alt9=1;
                                        }
                                        else {
                                            alt9=3;}
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 17, input);

                                        throw nvae;
                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 16, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 15, input);

                                throw nvae;
                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 14, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 13, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 10, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 1, input);

                throw nvae;
            }
            }
            break;
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt9=2;
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
            {
            alt9=4;
            }
            break;
        case '\t':
        case '\n':
        case '\f':
        case '\r':
        case ' ':
        case ',':
            {
            alt9=5;
            }
            break;
        case '/':
            {
            int LA9_5 = input.LA(2);

            if ( (LA9_5=='*') ) {
                alt9=7;
            }
            else if ( (LA9_5=='/') ) {
                alt9=6;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 5, input);

                throw nvae;
            }
            }
            break;
        case '(':
            {
            alt9=8;
            }
            break;
        case ')':
            {
            alt9=9;
            }
            break;
        case '-':
            {
            alt9=10;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( INCLUDE | IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 9, 0, input);

            throw nvae;
        }

        switch (alt9) {
            case 1 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:10: INCLUDE
                {
                mINCLUDE(); 

                }
                break;
            case 2 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:18: IDENT
                {
                mIDENT(); 

                }
                break;
            case 3 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:24: INC
                {
                mINC(); 

                }
                break;
            case 4 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:28: VAR
                {
                mVAR(); 

                }
                break;
            case 5 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:32: WS
                {
                mWS(); 

                }
                break;
            case 6 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:35: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 7 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:43: MULTI_COMMENT
                {
                mMULTI_COMMENT(); 

                }
                break;
            case 8 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:57: LPAR
                {
                mLPAR(); 

                }
                break;
            case 9 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:62: RPAR
                {
                mRPAR(); 

                }
                break;
            case 10 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:67: RARROW
                {
                mRARROW(); 

                }
                break;

        }

    }


 

}