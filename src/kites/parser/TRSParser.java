// $ANTLR 3.0.1 /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g 2010-09-04 13:41:51

  package kites.parser;
  import kites.TRSModel.*;
  import java.util.LinkedList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TRSParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "INCLUDE", "WS", "FILENAME", "RARROW", "IDENT", "LPAR", "RPAR", "VAR", "INC", "COMMENT", "MULTI_COMMENT", "'#instance'"
    };
    public static final int INC=12;
    public static final int WS=5;
    public static final int LPAR=9;
    public static final int FILENAME=6;
    public static final int INCLUDE=4;
    public static final int RARROW=7;
    public static final int MULTI_COMMENT=14;
    public static final int IDENT=8;
    public static final int RPAR=10;
    public static final int VAR=11;
    public static final int COMMENT=13;
    public static final int EOF=-1;

        public TRSParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "/home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g"; }


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



    // $ANTLR start rulelist
    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:78:1: rulelist returns [RuleList e] : ( rule | INCLUDE )* ( WS )? ( '#instance' instance )? ;
    public final RuleList rulelist() throws RecognitionException {
        RuleList e = null;

        Rule rule1 = null;

        ASTNode instance2 = null;


        try {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:78:30: ( ( rule | INCLUDE )* ( WS )? ( '#instance' instance )? )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:79:2: ( rule | INCLUDE )* ( WS )? ( '#instance' instance )?
            {
             e = new RuleList(); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:80:2: ( rule | INCLUDE )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==IDENT||LA1_0==VAR) ) {
                    alt1=1;
                }
                else if ( (LA1_0==INCLUDE) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:80:4: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_rulelist63);
            	    rule1=rule();
            	    _fsp--;

            	     e.add(rule1); 

            	    }
            	    break;
            	case 2 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:81:4: INCLUDE
            	    {
            	    match(input,INCLUDE,FOLLOW_INCLUDE_in_rulelist70); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:83:2: ( WS )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WS) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:83:3: WS
                    {
                    match(input,WS,FOLLOW_WS_in_rulelist78); 

                    }
                    break;

            }

            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:84:2: ( '#instance' instance )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==15) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:84:3: '#instance' instance
                    {
                    match(input,15,FOLLOW_15_in_rulelist84); 
                    pushFollow(FOLLOW_instance_in_rulelist86);
                    instance2=instance();
                    _fsp--;

                     e.addInstance(instance2); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end rulelist


    // $ANTLR start instance
    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:87:1: instance returns [ASTNode e] : ( function | constant ) ;
    public final ASTNode instance() throws RecognitionException {
        ASTNode e = null;

        ASTNode function3 = null;

        ASTNode constant4 = null;


        try {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:87:29: ( ( function | constant ) )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:88:3: ( function | constant )
            {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:88:3: ( function | constant )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==IDENT) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==LPAR) ) {
                    alt4=1;
                }
                else if ( (LA4_1==EOF) ) {
                    alt4=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("88:3: ( function | constant )", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("88:3: ( function | constant )", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:88:5: function
                    {
                    pushFollow(FOLLOW_function_in_instance108);
                    function3=function();
                    _fsp--;

                     e = function3; 

                    }
                    break;
                case 2 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:89:5: constant
                    {
                    pushFollow(FOLLOW_constant_in_instance117);
                    constant4=constant();
                    _fsp--;

                     e = constant4; 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end instance


    // $ANTLR start rule
    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:115:1: rule returns [Rule e] : (v1= var | c1= constant | f1= function ) RARROW (v2= var | c2= constant | f2= function ) ;
    public final Rule rule() throws RecognitionException {
        Rule e = null;

        ASTNode v1 = null;

        ASTNode c1 = null;

        ASTNode f1 = null;

        ASTNode v2 = null;

        ASTNode c2 = null;

        ASTNode f2 = null;


        try {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:115:22: ( (v1= var | c1= constant | f1= function ) RARROW (v2= var | c2= constant | f2= function ) )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:116:2: (v1= var | c1= constant | f1= function ) RARROW (v2= var | c2= constant | f2= function )
            {
             e = new Rule(); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:117:2: (v1= var | c1= constant | f1= function )
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==VAR) ) {
                alt5=1;
            }
            else if ( (LA5_0==IDENT) ) {
                int LA5_2 = input.LA(2);

                if ( (LA5_2==LPAR) ) {
                    alt5=3;
                }
                else if ( (LA5_2==RARROW) ) {
                    alt5=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("117:2: (v1= var | c1= constant | f1= function )", 5, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("117:2: (v1= var | c1= constant | f1= function )", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:117:4: v1= var
                    {
                    pushFollow(FOLLOW_var_in_rule176);
                    v1=var();
                    _fsp--;

                     e.setLeft(v1); 

                    }
                    break;
                case 2 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:118:4: c1= constant
                    {
                    pushFollow(FOLLOW_constant_in_rule193);
                    c1=constant();
                    _fsp--;

                     e.setLeft(c1); 

                    }
                    break;
                case 3 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:119:4: f1= function
                    {
                    pushFollow(FOLLOW_function_in_rule206);
                    f1=function();
                    _fsp--;

                     e.setLeft(f1); 

                    }
                    break;

            }

            match(input,RARROW,FOLLOW_RARROW_in_rule215); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:122:2: (v2= var | c2= constant | f2= function )
            int alt6=3;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==VAR) ) {
                alt6=1;
            }
            else if ( (LA6_0==IDENT) ) {
                int LA6_2 = input.LA(2);

                if ( (LA6_2==LPAR) ) {
                    alt6=3;
                }
                else if ( (LA6_2==EOF||(LA6_2>=INCLUDE && LA6_2<=WS)||LA6_2==IDENT||LA6_2==VAR||LA6_2==15) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("122:2: (v2= var | c2= constant | f2= function )", 6, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("122:2: (v2= var | c2= constant | f2= function )", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:122:4: v2= var
                    {
                    pushFollow(FOLLOW_var_in_rule222);
                    v2=var();
                    _fsp--;

                     e.setRight(v2); 

                    }
                    break;
                case 2 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:123:4: c2= constant
                    {
                    pushFollow(FOLLOW_constant_in_rule239);
                    c2=constant();
                    _fsp--;

                     e.setRight(c2); 

                    }
                    break;
                case 3 :
                    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:124:4: f2= function
                    {
                    pushFollow(FOLLOW_function_in_rule251);
                    f2=function();
                    _fsp--;

                     e.setRight(f2); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end rule


    // $ANTLR start function
    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:128:1: function returns [ASTNode e] : IDENT LPAR ( var | constant | f= function )+ RPAR ;
    public final ASTNode function() throws RecognitionException {
        ASTNode e = null;

        Token IDENT5=null;
        ASTNode f = null;

        ASTNode var6 = null;

        ASTNode constant7 = null;


        try {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:128:29: ( IDENT LPAR ( var | constant | f= function )+ RPAR )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:132:2: IDENT LPAR ( var | constant | f= function )+ RPAR
            {
            IDENT5=(Token)input.LT(1);
            match(input,IDENT,FOLLOW_IDENT_in_function277); 
             e = new Function(IDENT5.getText()); 
            match(input,LPAR,FOLLOW_LPAR_in_function282); 
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:134:2: ( var | constant | f= function )+
            int cnt7=0;
            loop7:
            do {
                int alt7=4;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==VAR) ) {
                    alt7=1;
                }
                else if ( (LA7_0==IDENT) ) {
                    int LA7_3 = input.LA(2);

                    if ( (LA7_3==LPAR) ) {
                        alt7=3;
                    }
                    else if ( (LA7_3==IDENT||(LA7_3>=RPAR && LA7_3<=VAR)) ) {
                        alt7=2;
                    }


                }


                switch (alt7) {
            	case 1 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:134:3: var
            	    {
            	    pushFollow(FOLLOW_var_in_function286);
            	    var6=var();
            	    _fsp--;

            	     e.add(var6); 

            	    }
            	    break;
            	case 2 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:135:4: constant
            	    {
            	    pushFollow(FOLLOW_constant_in_function293);
            	    constant7=constant();
            	    _fsp--;

            	     e.add(constant7); 

            	    }
            	    break;
            	case 3 :
            	    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:136:4: f= function
            	    {
            	    pushFollow(FOLLOW_function_in_function302);
            	    f=function();
            	    _fsp--;

            	     e.add(f); 

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

            match(input,RPAR,FOLLOW_RPAR_in_function311); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end function


    // $ANTLR start constant
    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:141:1: constant returns [ASTNode e] : IDENT ;
    public final ASTNode constant() throws RecognitionException {
        ASTNode e = null;

        Token IDENT8=null;

        try {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:141:29: ( IDENT )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:142:2: IDENT
            {
            IDENT8=(Token)input.LT(1);
            match(input,IDENT,FOLLOW_IDENT_in_constant325); 
             e = new Constant(IDENT8.getText()); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end constant


    // $ANTLR start var
    // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:145:1: var returns [ASTNode e] : VAR ;
    public final ASTNode var() throws RecognitionException {
        ASTNode e = null;

        Token VAR9=null;

        try {
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:145:24: ( VAR )
            // /home/sarek/Documents/Eclipse Workspace/KiTES/src/kites/parser/TRS.g:146:2: VAR
            {
            VAR9=(Token)input.LT(1);
            match(input,VAR,FOLLOW_VAR_in_var342); 
             e = new Variable(VAR9.getText()); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end var


 

    public static final BitSet FOLLOW_rule_in_rulelist63 = new BitSet(new long[]{0x0000000000008932L});
    public static final BitSet FOLLOW_INCLUDE_in_rulelist70 = new BitSet(new long[]{0x0000000000008932L});
    public static final BitSet FOLLOW_WS_in_rulelist78 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_15_in_rulelist84 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_instance_in_rulelist86 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_instance108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constant_in_instance117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_rule176 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_constant_in_rule193 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_function_in_rule206 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RARROW_in_rule215 = new BitSet(new long[]{0x0000000000000900L});
    public static final BitSet FOLLOW_var_in_rule222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constant_in_rule239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_rule251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_function277 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAR_in_function282 = new BitSet(new long[]{0x0000000000000900L});
    public static final BitSet FOLLOW_var_in_function286 = new BitSet(new long[]{0x0000000000000D00L});
    public static final BitSet FOLLOW_constant_in_function293 = new BitSet(new long[]{0x0000000000000D00L});
    public static final BitSet FOLLOW_function_in_function302 = new BitSet(new long[]{0x0000000000000D00L});
    public static final BitSet FOLLOW_RPAR_in_function311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_constant325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_var342 = new BitSet(new long[]{0x0000000000000002L});

}