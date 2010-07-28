// $ANTLR 3.0.1 /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g 2010-07-28 13:47:49

  package kites.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TRSLexer extends Lexer {
    public static final int INC=4;
    public static final int WS=10;
    public static final int LPAR=7;
    public static final int RARROW=6;
    public static final int MULTI_COMMENT=12;
    public static final int IDENT=5;
    public static final int RPAR=8;
    public static final int VAR=9;
    public static final int COMMENT=11;
    public static final int Tokens=13;
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

    // $ANTLR start IDENT
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:114:6: ( 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )* )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:115:2: 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            {
            matchRange('a','z'); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:116:2: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
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
            	    break loop1;
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
            int alt2=3;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='#') ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1=='I') ) {
                    int LA2_2 = input.LA(3);

                    if ( (LA2_2=='n') ) {
                        alt2=3;
                    }
                    else if ( (LA2_2=='N') ) {
                        alt2=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("120:2: ( '#include' | '#INCLUDE' | '#Include' )", 2, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA2_1=='i') ) {
                    alt2=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("120:2: ( '#include' | '#INCLUDE' | '#Include' )", 2, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("120:2: ( '#include' | '#INCLUDE' | '#Include' )", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
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
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
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
            	    break loop3;
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
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||(LA4_0>='\f' && LA4_0<='\r')||LA4_0==' '||LA4_0==',') ) {
                    alt4=1;
                }


                switch (alt4) {
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
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\n'||LA5_0=='\r') ) {
                    alt5=2;
                }
                else if ( ((LA5_0>='\u0000' && LA5_0<='\t')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\uFFFE')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:136:3: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:137:3: ( '\\n' | '\\r' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\n'||LA6_0=='\r') ) {
                    alt6=1;
                }


                switch (alt6) {
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
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='*') ) {
                    int LA7_1 = input.LA(2);

                    if ( (LA7_1=='/') ) {
                        alt7=2;
                    }
                    else if ( ((LA7_1>='\u0000' && LA7_1<='.')||(LA7_1>='0' && LA7_1<='\uFFFE')) ) {
                        alt7=1;
                    }


                }
                else if ( ((LA7_0>='\u0000' && LA7_0<=')')||(LA7_0>='+' && LA7_0<='\uFFFE')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:142:2: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop7;
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
        // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:8: ( IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW )
        int alt8=9;
        switch ( input.LA(1) ) {
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
            alt8=1;
            }
            break;
        case '#':
            {
            alt8=2;
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
            alt8=3;
            }
            break;
        case '\t':
        case '\n':
        case '\f':
        case '\r':
        case ' ':
        case ',':
            {
            alt8=4;
            }
            break;
        case '/':
            {
            int LA8_5 = input.LA(2);

            if ( (LA8_5=='/') ) {
                alt8=5;
            }
            else if ( (LA8_5=='*') ) {
                alt8=6;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1:1: Tokens : ( IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 8, 5, input);

                throw nvae;
            }
            }
            break;
        case '(':
            {
            alt8=7;
            }
            break;
        case ')':
            {
            alt8=8;
            }
            break;
        case '-':
            {
            alt8=9;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( IDENT | INC | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 8, 0, input);

            throw nvae;
        }

        switch (alt8) {
            case 1 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:10: IDENT
                {
                mIDENT(); 

                }
                break;
            case 2 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:16: INC
                {
                mINC(); 

                }
                break;
            case 3 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:20: VAR
                {
                mVAR(); 

                }
                break;
            case 4 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:24: WS
                {
                mWS(); 

                }
                break;
            case 5 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:27: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 6 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:35: MULTI_COMMENT
                {
                mMULTI_COMMENT(); 

                }
                break;
            case 7 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:49: LPAR
                {
                mLPAR(); 

                }
                break;
            case 8 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:54: RPAR
                {
                mRPAR(); 

                }
                break;
            case 9 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:59: RARROW
                {
                mRARROW(); 

                }
                break;

        }

    }


 

}