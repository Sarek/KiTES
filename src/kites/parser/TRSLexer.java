// $ANTLR 3.0.1 /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g 2010-06-30 02:53:29

  package kites.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TRSLexer extends Lexer {
    public static final int WS=9;
    public static final int LPAR=6;
    public static final int RARROW=4;
    public static final int MULTI_COMMENT=11;
    public static final int IDENT=5;
    public static final int RPAR=7;
    public static final int VAR=8;
    public static final int COMMENT=10;
    public static final int Tokens=12;
    public static final int EOF=-1;
    public TRSLexer() {;} 
    public TRSLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g"; }

    // $ANTLR start IDENT
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:73:6: ( 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )* )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:74:2: 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            {
            matchRange('a','z'); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:75:2: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
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

    // $ANTLR start VAR
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:78:4: ( 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )* )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:79:2: 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
            {
            matchRange('A','Z'); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:80:2: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )*
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
    // $ANTLR end VAR

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:83:3: ( ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+ )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:84:2: ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+
            {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:84:2: ( ' ' | ',' | '\\n' | '\\r' | '\\t' | '\\f' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='\t' && LA3_0<='\n')||(LA3_0>='\f' && LA3_0<='\r')||LA3_0==' '||LA3_0==',') ) {
                    alt3=1;
                }


                switch (alt3) {
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
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:89:8: ( '//' ( . )* ( '\\n' | '\\r' )+ )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:90:3: '//' ( . )* ( '\\n' | '\\r' )+
            {
            match("//"); 

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:91:3: ( . )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='\n'||LA4_0=='\r') ) {
                    alt4=2;
                }
                else if ( ((LA4_0>='\u0000' && LA4_0<='\t')||(LA4_0>='\u000B' && LA4_0<='\f')||(LA4_0>='\u000E' && LA4_0<='\uFFFE')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:91:3: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:92:3: ( '\\n' | '\\r' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\n'||LA5_0=='\r') ) {
                    alt5=1;
                }


                switch (alt5) {
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
    // $ANTLR end COMMENT

    // $ANTLR start MULTI_COMMENT
    public final void mMULTI_COMMENT() throws RecognitionException {
        try {
            int _type = MULTI_COMMENT;
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:95:14: ( '/*' ( . )* '*/' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:96:2: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:97:2: ( . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='*') ) {
                    int LA6_1 = input.LA(2);

                    if ( (LA6_1=='/') ) {
                        alt6=2;
                    }
                    else if ( ((LA6_1>='\u0000' && LA6_1<='.')||(LA6_1>='0' && LA6_1<='\uFFFE')) ) {
                        alt6=1;
                    }


                }
                else if ( ((LA6_0>='\u0000' && LA6_0<=')')||(LA6_0>='+' && LA6_0<='\uFFFE')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:97:2: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:101:5: ( '(' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:102:2: '('
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:105:5: ( ')' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:106:2: ')'
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
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:109:7: ( '-->' )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:110:2: '-->'
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
        // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:8: ( IDENT | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW )
        int alt7=8;
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
            alt7=1;
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
            alt7=2;
            }
            break;
        case '\t':
        case '\n':
        case '\f':
        case '\r':
        case ' ':
        case ',':
            {
            alt7=3;
            }
            break;
        case '/':
            {
            int LA7_4 = input.LA(2);

            if ( (LA7_4=='/') ) {
                alt7=4;
            }
            else if ( (LA7_4=='*') ) {
                alt7=5;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1:1: Tokens : ( IDENT | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 7, 4, input);

                throw nvae;
            }
            }
            break;
        case '(':
            {
            alt7=6;
            }
            break;
        case ')':
            {
            alt7=7;
            }
            break;
        case '-':
            {
            alt7=8;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( IDENT | VAR | WS | COMMENT | MULTI_COMMENT | LPAR | RPAR | RARROW );", 7, 0, input);

            throw nvae;
        }

        switch (alt7) {
            case 1 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:10: IDENT
                {
                mIDENT(); 

                }
                break;
            case 2 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:16: VAR
                {
                mVAR(); 

                }
                break;
            case 3 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:20: WS
                {
                mWS(); 

                }
                break;
            case 4 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:23: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 5 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:31: MULTI_COMMENT
                {
                mMULTI_COMMENT(); 

                }
                break;
            case 6 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:45: LPAR
                {
                mLPAR(); 

                }
                break;
            case 7 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:50: RPAR
                {
                mRPAR(); 

                }
                break;
            case 8 :
                // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:1:55: RARROW
                {
                mRARROW(); 

                }
                break;

        }

    }


 

}