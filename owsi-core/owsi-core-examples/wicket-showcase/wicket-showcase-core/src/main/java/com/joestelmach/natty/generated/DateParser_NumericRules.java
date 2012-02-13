// $ANTLR 3.2 Sep 23, 2009 14:05:07 NumericRules.g 2012-02-13 15:13:28

  package com.joestelmach.natty.generated;


import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.debug.DebugEventListener;
import org.antlr.runtime.debug.DebugParser;
import org.antlr.runtime.debug.DebugTreeAdaptor;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.TreeAdaptor;

@SuppressWarnings("unused")
public class DateParser_NumericRules extends DebugParser {
    public static final int DIRECTION=365;
    public static final int UNKNOWN_CHAR=219;
    public static final int INT_08=79;
    public static final int INT_09=80;
    public static final int DIXIEME=208;
    public static final int MAINTENANT=59;
    public static final int INT_02=73;
    public static final int DU=57;
    public static final int INT_03=74;
    public static final int INT_00=71;
    public static final int INT_01=72;
    public static final int INT_06=77;
    public static final int APRES_DEMAIN=34;
    public static final int INT_07=78;
    public static final int INT_04=75;
    public static final int INT_05=76;
    public static final int DEUXIEME=200;
    public static final int DE=56;
    public static final int EOF=-1;
    public static final int IL_Y_A=66;
    public static final int PENDANT=49;
    public static final int AM_PM=374;
    public static final int UN=181;
    public static final int SPAN=368;
    public static final int AOUT=12;
    public static final int ET=54;
    public static final int HEURE=24;
    public static final int PREMIER=199;
    public static final int QUATORZE=194;
    public static final int A=41;
    public static final int PASSE=43;
    public static final int DEMAIN=33;
    public static final int MONTH_OF_YEAR=355;
    public static final int WEEKEND=38;
    public static final int DAY_OF_WEEK=357;
    public static final int EXPLICIT_DATE=362;
    public static final int INT=222;
    public static final int SEPTIEME=205;
    public static final int SIX=186;
    public static final int JEUDI=21;
    public static final int DAY_OF_YEAR=358;
    public static final int SEPT=187;
    public static final int JOUR=27;
    public static final int FEVRIER=6;
    public static final int SECONDS_OF_MINUTE=373;
    public static final int EXPLICIT_SEEK=367;
    public static final int INT_47=128;
    public static final int INT_46=127;
    public static final int INT_45=126;
    public static final int INT_44=125;
    public static final int VINGTIEME=215;
    public static final int INT_49=130;
    public static final int INT_48=129;
    public static final int TREIZE=193;
    public static final int QUATRIEME=202;
    public static final int INT_42=123;
    public static final int WHITE_SPACE=62;
    public static final int INT_43=124;
    public static final int INT_40=121;
    public static final int ONZE=191;
    public static final int INT_41=122;
    public static final int INT_34=115;
    public static final int INT_33=114;
    public static final int INT_36=117;
    public static final int SINGLE_QUOTE=31;
    public static final int INT_35=116;
    public static final int INT_38=119;
    public static final int SLASH=69;
    public static final int INT_37=118;
    public static final int INT_39=120;
    public static final int AU=58;
    public static final int MATIN=46;
    public static final int ZONE=375;
    public static final int PLUS=70;
    public static final int TROIS=183;
    public static final int INT_30=111;
    public static final int INT_31=112;
    public static final int INT_32=113;
    public static final int INT_29=110;
    public static final int INT_28=109;
    public static final int INT_27=108;
    public static final int INT_26=107;
    public static final int INT_25=106;
    public static final int AVRIL=8;
    public static final int INT_24=105;
    public static final int INT_23=104;
    public static final int INT_22=103;
    public static final int ANNEE=30;
    public static final int TROISIEME=201;
    public static final int JUIN=10;
    public static final int HUIT=188;
    public static final int INT_20=101;
    public static final int INT_21=102;
    public static final int CHAQUE=39;
    public static final int MARS=7;
    public static final int INT_16=97;
    public static final int COLON=67;
    public static final int INT_15=96;
    public static final int INT_18=99;
    public static final int INT_17=98;
    public static final int INT_12=93;
    public static final int INT_11=92;
    public static final int INT_14=95;
    public static final int INT_13=94;
    public static final int DEBUT=64;
    public static final int DAY_OF_MONTH=356;
    public static final int INT_19=100;
    public static final int AUJOURD_HUI=32;
    public static final int DOUZIEME=210;
    public static final int CE=55;
    public static final int SEMAINE=28;
    public static final int DIX=190;
    public static final int VENDREDI=22;
    public static final int AVANT_HIER=36;
    public static final int INT_10=91;
    public static final int INT_81=162;
    public static final int INT_80=161;
    public static final int INT_83=164;
    public static final int INT_82=163;
    public static final int INT_85=166;
    public static final int TRENTE=198;
    public static final int INT_84=165;
    public static final int INT_87=168;
    public static final int INT_86=167;
    public static final int MINUIT=44;
    public static final int INT_88=169;
    public static final int INT_89=170;
    public static final int FIN=65;
    public static final int DATE_TIME=360;
    public static final int INT_72=153;
    public static final int INT_71=152;
    public static final int RECURRENCE=377;
    public static final int INT_70=151;
    public static final int INT_76=157;
    public static final int QUINZE=195;
    public static final int INT_75=156;
    public static final int INT_74=155;
    public static final int INT_73=154;
    public static final int MINUTES_OF_HOUR=372;
    public static final int SUIVANT=60;
    public static final int INT_79=160;
    public static final int INT_77=158;
    public static final int INT_78=159;
    public static final int RELATIVE_DATE=363;
    public static final int INT_63=144;
    public static final int SEIZE=196;
    public static final int INT_62=143;
    public static final int INT_65=146;
    public static final int INT_64=145;
    public static final int SECOND=379;
    public static final int JUILLET=11;
    public static final int INT_61=142;
    public static final int INT_60=141;
    public static final int SIXIEME=204;
    public static final int IS_INTERVAL=378;
    public static final int INT_66=147;
    public static final int INT_67=148;
    public static final int INT_68=149;
    public static final int DANS=50;
    public static final int INT_69=150;
    public static final int INT_54=135;
    public static final int INT_53=134;
    public static final int INT_52=133;
    public static final int INT_51=132;
    public static final int INT_50=131;
    public static final int VINGT=197;
    public static final int CINQ=185;
    public static final int TREIZIEME=211;
    public static final int RELATIVE_TIME=370;
    public static final int OU=53;
    public static final int DATE_TIME_ALTERNATIVE=361;
    public static final int MOIS=29;
    public static final int ZONE_OFFSET=376;
    public static final int SEEK_BY=366;
    public static final int TRENTIEME=216;
    public static final int EXPLICIT_TIME=369;
    public static final int DERNIER=61;
    public static final int HEURE_SHORT=25;
    public static final int INT_59=140;
    public static final int INT_57=138;
    public static final int INT_58=139;
    public static final int INT_55=136;
    public static final int MARDI=19;
    public static final int INT_56=137;
    public static final int HOURS_OF_DAY=371;
    public static final int DEUX=182;
    public static final int AVANT=63;
    public static final int MERCREDI=20;
    public static final int INT_9=90;
    public static final int INT_8=89;
    public static final int INT_7=88;
    public static final int SPACE=218;
    public static final int INT_6=87;
    public static final int INT_5=86;
    public static final int INT_4=85;
    public static final int INT_3=84;
    public static final int INT_2=83;
    public static final int INT_1=82;
    public static final int INT_0=81;
    public static final int APRES_MIDI=45;
    public static final int APRES=42;
    public static final int UNKNOWN=220;
    public static final int SAMEDI=23;
    public static final int HIER=35;
    public static final int COMMA=68;
    public static final int SEPTEMBRE=13;
    public static final int JANVIER=5;
    public static final int SEIZIEME=214;
    public static final int CINQUIEME=203;
    public static final int DIGIT=221;
    public static final int DOT=4;
    public static final int DOUZE=192;
    public static final int DIMANCHE=17;
    public static final int DASH=37;
    public static final int HUITIEME=206;
    public static final int YEAR_OF=359;
    public static final int NEUF=189;
    public static final int ONZIEME=209;
    public static final int SOIR=47;
    public static final int QUATORZIEME=212;
    public static final int QUATRE=184;
    public static final int SEEK=364;
    public static final int INT_90=171;
    public static final int UNIEME=217;
    public static final int JUSQU_A=40;
    public static final int MAI=9;
    public static final int INT_97=178;
    public static final int QUINZIEME=213;
    public static final int INT_98=179;
    public static final int INT_95=176;
    public static final int INT_96=177;
    public static final int INT_93=174;
    public static final int INT_94=175;
    public static final int INT_91=172;
    public static final int NEUVIEME=207;
    public static final int INT_92=173;
    public static final int LA=52;
    public static final int NOVEMBRE=15;
    public static final int NUIT=48;
    public static final int OCTOBRE=14;
    public static final int INT_99=180;
    public static final int MINUTE=26;
    public static final int DECEMBRE=16;
    public static final int LE=51;
    public static final int LUNDI=18;

    // delegates
    // delegators
    public DateParser gDateParser;
    public DateParser gParent;

     
        public int getRuleLevel() { return gDateParser.getRuleLevel(); }
        public void incRuleLevel() { gDateParser.incRuleLevel(); }
        public void decRuleLevel() { gDateParser.decRuleLevel(); }
        public DateParser_NumericRules(TokenStream input, DebugEventListener dbg, RecognizerSharedState state, DateParser gDateParser) {
            super(input, dbg, state);
            this.gDateParser = gDateParser;
        }
    protected boolean evalPredicate(boolean result, String predicate) {
        dbg.semanticPredicate(result, predicate);
        return result;
    }

    protected DebugTreeAdaptor adaptor;
    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = (DebugTreeAdaptor)adaptor; // delegator sends dbg adaptor 

    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }


    public String[] getTokenNames() { return DateParser.tokenNames; }
    public String getGrammarFileName() { return "NumericRules.g"; }


    public static class int_00_to_59_mandatory_prefix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_00_to_59_mandatory_prefix"
    // NumericRules.g:14:1: int_00_to_59_mandatory_prefix : ( INT_00 | int_01_to_12 | int_13_to_23 | int_24_to_31 | int_32_to_59 ) -> INT[$int_00_to_59_mandatory_prefix.text] ;
    public final DateParser_NumericRules.int_00_to_59_mandatory_prefix_return int_00_to_59_mandatory_prefix() throws RecognitionException {
        DateParser_NumericRules.int_00_to_59_mandatory_prefix_return retval = new DateParser_NumericRules.int_00_to_59_mandatory_prefix_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token INT_001=null;
        DateParser_NumericRules.int_01_to_12_return int_01_to_122 = null;

        DateParser_NumericRules.int_13_to_23_return int_13_to_233 = null;

        DateParser_NumericRules.int_24_to_31_return int_24_to_314 = null;

        DateParser_NumericRules.int_32_to_59_return int_32_to_595 = null;


        Object INT_001_tree=null;
        RewriteRuleTokenStream stream_INT_00=new RewriteRuleTokenStream(adaptor,"token INT_00");
        RewriteRuleSubtreeStream stream_int_13_to_23=new RewriteRuleSubtreeStream(adaptor,"rule int_13_to_23");
        RewriteRuleSubtreeStream stream_int_32_to_59=new RewriteRuleSubtreeStream(adaptor,"rule int_32_to_59");
        RewriteRuleSubtreeStream stream_int_01_to_12=new RewriteRuleSubtreeStream(adaptor,"rule int_01_to_12");
        RewriteRuleSubtreeStream stream_int_24_to_31=new RewriteRuleSubtreeStream(adaptor,"rule int_24_to_31");
        try { dbg.enterRule(getGrammarFileName(), "int_00_to_59_mandatory_prefix");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(14, 1);

        try {
            // NumericRules.g:15:3: ( ( INT_00 | int_01_to_12 | int_13_to_23 | int_24_to_31 | int_32_to_59 ) -> INT[$int_00_to_59_mandatory_prefix.text] )
            dbg.enterAlt(1);

            // NumericRules.g:15:5: ( INT_00 | int_01_to_12 | int_13_to_23 | int_24_to_31 | int_32_to_59 )
            {
            dbg.location(15,5);
            // NumericRules.g:15:5: ( INT_00 | int_01_to_12 | int_13_to_23 | int_24_to_31 | int_32_to_59 )
            int alt1=5;
            try { dbg.enterSubRule(1);
            try { dbg.enterDecision(1);

            switch ( input.LA(1) ) {
            case INT_00:
                {
                alt1=1;
                }
                break;
            case INT_01:
            case INT_02:
            case INT_03:
            case INT_04:
            case INT_05:
            case INT_06:
            case INT_07:
            case INT_08:
            case INT_09:
            case INT_10:
            case INT_11:
            case INT_12:
                {
                alt1=2;
                }
                break;
            case INT_13:
            case INT_14:
            case INT_15:
            case INT_16:
            case INT_17:
            case INT_18:
            case INT_19:
            case INT_20:
            case INT_21:
            case INT_22:
            case INT_23:
                {
                alt1=3;
                }
                break;
            case INT_24:
            case INT_25:
            case INT_26:
            case INT_27:
            case INT_28:
            case INT_29:
            case INT_30:
            case INT_31:
                {
                alt1=4;
                }
                break;
            case INT_32:
            case INT_33:
            case INT_34:
            case INT_35:
            case INT_36:
            case INT_37:
            case INT_38:
            case INT_39:
            case INT_40:
            case INT_41:
            case INT_42:
            case INT_43:
            case INT_44:
            case INT_45:
            case INT_46:
            case INT_47:
            case INT_48:
            case INT_49:
            case INT_50:
            case INT_51:
            case INT_52:
            case INT_53:
            case INT_54:
            case INT_55:
            case INT_56:
            case INT_57:
            case INT_58:
            case INT_59:
                {
                alt1=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(1);}

            switch (alt1) {
                case 1 :
                    dbg.enterAlt(1);

                    // NumericRules.g:15:6: INT_00
                    {
                    dbg.location(15,6);
                    INT_001=(Token)match(input,INT_00,FOLLOW_INT_00_in_int_00_to_59_mandatory_prefix42); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT_00.add(INT_001);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // NumericRules.g:16:5: int_01_to_12
                    {
                    dbg.location(16,5);
                    pushFollow(FOLLOW_int_01_to_12_in_int_00_to_59_mandatory_prefix48);
                    int_01_to_122=int_01_to_12();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_01_to_12.add(int_01_to_122.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // NumericRules.g:17:5: int_13_to_23
                    {
                    dbg.location(17,5);
                    pushFollow(FOLLOW_int_13_to_23_in_int_00_to_59_mandatory_prefix54);
                    int_13_to_233=int_13_to_23();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_13_to_23.add(int_13_to_233.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // NumericRules.g:18:5: int_24_to_31
                    {
                    dbg.location(18,5);
                    pushFollow(FOLLOW_int_24_to_31_in_int_00_to_59_mandatory_prefix60);
                    int_24_to_314=int_24_to_31();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_24_to_31.add(int_24_to_314.getTree());

                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // NumericRules.g:19:5: int_32_to_59
                    {
                    dbg.location(19,5);
                    pushFollow(FOLLOW_int_32_to_59_in_int_00_to_59_mandatory_prefix66);
                    int_32_to_595=int_32_to_59();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_32_to_59.add(int_32_to_595.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(1);}



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 19:19: -> INT[$int_00_to_59_mandatory_prefix.text]
            {
                dbg.location(19,22);
                adaptor.addChild(root_0, (Object)adaptor.create(INT, input.toString(retval.start,input.LT(-1))));

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(20, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_00_to_59_mandatory_prefix");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_00_to_59_mandatory_prefix"

    public static class int_00_to_99_mandatory_prefix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_00_to_99_mandatory_prefix"
    // NumericRules.g:23:1: int_00_to_99_mandatory_prefix : ( int_00_to_59_mandatory_prefix | int_60_to_99 ) -> INT[$int_00_to_99_mandatory_prefix.text] ;
    public final DateParser_NumericRules.int_00_to_99_mandatory_prefix_return int_00_to_99_mandatory_prefix() throws RecognitionException {
        DateParser_NumericRules.int_00_to_99_mandatory_prefix_return retval = new DateParser_NumericRules.int_00_to_99_mandatory_prefix_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DateParser_NumericRules.int_00_to_59_mandatory_prefix_return int_00_to_59_mandatory_prefix6 = null;

        DateParser_NumericRules.int_60_to_99_return int_60_to_997 = null;


        RewriteRuleSubtreeStream stream_int_00_to_59_mandatory_prefix=new RewriteRuleSubtreeStream(adaptor,"rule int_00_to_59_mandatory_prefix");
        RewriteRuleSubtreeStream stream_int_60_to_99=new RewriteRuleSubtreeStream(adaptor,"rule int_60_to_99");
        try { dbg.enterRule(getGrammarFileName(), "int_00_to_99_mandatory_prefix");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(23, 1);

        try {
            // NumericRules.g:24:3: ( ( int_00_to_59_mandatory_prefix | int_60_to_99 ) -> INT[$int_00_to_99_mandatory_prefix.text] )
            dbg.enterAlt(1);

            // NumericRules.g:24:5: ( int_00_to_59_mandatory_prefix | int_60_to_99 )
            {
            dbg.location(24,5);
            // NumericRules.g:24:5: ( int_00_to_59_mandatory_prefix | int_60_to_99 )
            int alt2=2;
            try { dbg.enterSubRule(2);
            try { dbg.enterDecision(2);

            switch ( input.LA(1) ) {
            case INT_00:
            case INT_01:
            case INT_02:
            case INT_03:
            case INT_04:
            case INT_05:
            case INT_06:
            case INT_07:
            case INT_08:
            case INT_09:
            case INT_10:
            case INT_11:
            case INT_12:
            case INT_13:
            case INT_14:
            case INT_15:
            case INT_16:
            case INT_17:
            case INT_18:
            case INT_19:
            case INT_20:
            case INT_21:
            case INT_22:
            case INT_23:
            case INT_24:
            case INT_25:
            case INT_26:
            case INT_27:
            case INT_28:
            case INT_29:
            case INT_30:
            case INT_31:
            case INT_32:
            case INT_33:
            case INT_34:
            case INT_35:
            case INT_36:
            case INT_37:
            case INT_38:
            case INT_39:
            case INT_40:
            case INT_41:
            case INT_42:
            case INT_43:
            case INT_44:
            case INT_45:
            case INT_46:
            case INT_47:
            case INT_48:
            case INT_49:
            case INT_50:
            case INT_51:
            case INT_52:
            case INT_53:
            case INT_54:
            case INT_55:
            case INT_56:
            case INT_57:
            case INT_58:
            case INT_59:
                {
                alt2=1;
                }
                break;
            case INT_60:
            case INT_61:
            case INT_62:
            case INT_63:
            case INT_64:
            case INT_65:
            case INT_66:
            case INT_67:
            case INT_68:
            case INT_69:
            case INT_70:
            case INT_71:
            case INT_72:
            case INT_73:
            case INT_74:
            case INT_75:
            case INT_76:
            case INT_77:
            case INT_78:
            case INT_79:
            case INT_80:
            case INT_81:
            case INT_82:
            case INT_83:
            case INT_84:
            case INT_85:
            case INT_86:
            case INT_87:
            case INT_88:
            case INT_89:
            case INT_90:
            case INT_91:
            case INT_92:
            case INT_93:
            case INT_94:
            case INT_95:
            case INT_96:
            case INT_97:
            case INT_98:
            case INT_99:
                {
                alt2=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(2);}

            switch (alt2) {
                case 1 :
                    dbg.enterAlt(1);

                    // NumericRules.g:24:6: int_00_to_59_mandatory_prefix
                    {
                    dbg.location(24,6);
                    pushFollow(FOLLOW_int_00_to_59_mandatory_prefix_in_int_00_to_99_mandatory_prefix89);
                    int_00_to_59_mandatory_prefix6=int_00_to_59_mandatory_prefix();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_00_to_59_mandatory_prefix.add(int_00_to_59_mandatory_prefix6.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // NumericRules.g:24:38: int_60_to_99
                    {
                    dbg.location(24,38);
                    pushFollow(FOLLOW_int_60_to_99_in_int_00_to_99_mandatory_prefix93);
                    int_60_to_997=int_60_to_99();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_60_to_99.add(int_60_to_997.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(2);}



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 25:5: -> INT[$int_00_to_99_mandatory_prefix.text]
            {
                dbg.location(25,8);
                adaptor.addChild(root_0, (Object)adaptor.create(INT, input.toString(retval.start,input.LT(-1))));

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(26, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_00_to_99_mandatory_prefix");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_00_to_99_mandatory_prefix"

    public static class int_01_to_12_optional_prefix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_01_to_12_optional_prefix"
    // NumericRules.g:29:1: int_01_to_12_optional_prefix : ( int_1_to_9 | int_01_to_12 ) -> INT[$int_01_to_12_optional_prefix.text] ;
    public final DateParser_NumericRules.int_01_to_12_optional_prefix_return int_01_to_12_optional_prefix() throws RecognitionException {
        DateParser_NumericRules.int_01_to_12_optional_prefix_return retval = new DateParser_NumericRules.int_01_to_12_optional_prefix_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DateParser_NumericRules.int_1_to_9_return int_1_to_98 = null;

        DateParser_NumericRules.int_01_to_12_return int_01_to_129 = null;


        RewriteRuleSubtreeStream stream_int_01_to_12=new RewriteRuleSubtreeStream(adaptor,"rule int_01_to_12");
        RewriteRuleSubtreeStream stream_int_1_to_9=new RewriteRuleSubtreeStream(adaptor,"rule int_1_to_9");
        try { dbg.enterRule(getGrammarFileName(), "int_01_to_12_optional_prefix");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(29, 1);

        try {
            // NumericRules.g:30:3: ( ( int_1_to_9 | int_01_to_12 ) -> INT[$int_01_to_12_optional_prefix.text] )
            dbg.enterAlt(1);

            // NumericRules.g:30:5: ( int_1_to_9 | int_01_to_12 )
            {
            dbg.location(30,5);
            // NumericRules.g:30:5: ( int_1_to_9 | int_01_to_12 )
            int alt3=2;
            try { dbg.enterSubRule(3);
            try { dbg.enterDecision(3);

            switch ( input.LA(1) ) {
            case INT_1:
            case INT_2:
            case INT_3:
            case INT_4:
            case INT_5:
            case INT_6:
            case INT_7:
            case INT_8:
            case INT_9:
                {
                alt3=1;
                }
                break;
            case INT_01:
            case INT_02:
            case INT_03:
            case INT_04:
            case INT_05:
            case INT_06:
            case INT_07:
            case INT_08:
            case INT_09:
            case INT_10:
            case INT_11:
            case INT_12:
                {
                alt3=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(3);}

            switch (alt3) {
                case 1 :
                    dbg.enterAlt(1);

                    // NumericRules.g:30:6: int_1_to_9
                    {
                    dbg.location(30,6);
                    pushFollow(FOLLOW_int_1_to_9_in_int_01_to_12_optional_prefix120);
                    int_1_to_98=int_1_to_9();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_1_to_9.add(int_1_to_98.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // NumericRules.g:30:19: int_01_to_12
                    {
                    dbg.location(30,19);
                    pushFollow(FOLLOW_int_01_to_12_in_int_01_to_12_optional_prefix124);
                    int_01_to_129=int_01_to_12();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_01_to_12.add(int_01_to_129.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(3);}



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 30:33: -> INT[$int_01_to_12_optional_prefix.text]
            {
                dbg.location(30,36);
                adaptor.addChild(root_0, (Object)adaptor.create(INT, input.toString(retval.start,input.LT(-1))));

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(31, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_01_to_12_optional_prefix");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_01_to_12_optional_prefix"

    public static class int_00_to_23_optional_prefix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_00_to_23_optional_prefix"
    // NumericRules.g:34:1: int_00_to_23_optional_prefix : ( INT_00 | INT_0 | int_1_to_9 | int_01_to_12 | int_13_to_23 ) -> INT[$int_00_to_23_optional_prefix.text] ;
    public final DateParser_NumericRules.int_00_to_23_optional_prefix_return int_00_to_23_optional_prefix() throws RecognitionException {
        DateParser_NumericRules.int_00_to_23_optional_prefix_return retval = new DateParser_NumericRules.int_00_to_23_optional_prefix_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token INT_0010=null;
        Token INT_011=null;
        DateParser_NumericRules.int_1_to_9_return int_1_to_912 = null;

        DateParser_NumericRules.int_01_to_12_return int_01_to_1213 = null;

        DateParser_NumericRules.int_13_to_23_return int_13_to_2314 = null;


        Object INT_0010_tree=null;
        Object INT_011_tree=null;
        RewriteRuleTokenStream stream_INT_00=new RewriteRuleTokenStream(adaptor,"token INT_00");
        RewriteRuleTokenStream stream_INT_0=new RewriteRuleTokenStream(adaptor,"token INT_0");
        RewriteRuleSubtreeStream stream_int_13_to_23=new RewriteRuleSubtreeStream(adaptor,"rule int_13_to_23");
        RewriteRuleSubtreeStream stream_int_01_to_12=new RewriteRuleSubtreeStream(adaptor,"rule int_01_to_12");
        RewriteRuleSubtreeStream stream_int_1_to_9=new RewriteRuleSubtreeStream(adaptor,"rule int_1_to_9");
        try { dbg.enterRule(getGrammarFileName(), "int_00_to_23_optional_prefix");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(34, 1);

        try {
            // NumericRules.g:35:3: ( ( INT_00 | INT_0 | int_1_to_9 | int_01_to_12 | int_13_to_23 ) -> INT[$int_00_to_23_optional_prefix.text] )
            dbg.enterAlt(1);

            // NumericRules.g:35:5: ( INT_00 | INT_0 | int_1_to_9 | int_01_to_12 | int_13_to_23 )
            {
            dbg.location(35,5);
            // NumericRules.g:35:5: ( INT_00 | INT_0 | int_1_to_9 | int_01_to_12 | int_13_to_23 )
            int alt4=5;
            try { dbg.enterSubRule(4);
            try { dbg.enterDecision(4);

            switch ( input.LA(1) ) {
            case INT_00:
                {
                alt4=1;
                }
                break;
            case INT_0:
                {
                alt4=2;
                }
                break;
            case INT_1:
            case INT_2:
            case INT_3:
            case INT_4:
            case INT_5:
            case INT_6:
            case INT_7:
            case INT_8:
            case INT_9:
                {
                alt4=3;
                }
                break;
            case INT_01:
            case INT_02:
            case INT_03:
            case INT_04:
            case INT_05:
            case INT_06:
            case INT_07:
            case INT_08:
            case INT_09:
            case INT_10:
            case INT_11:
            case INT_12:
                {
                alt4=4;
                }
                break;
            case INT_13:
            case INT_14:
            case INT_15:
            case INT_16:
            case INT_17:
            case INT_18:
            case INT_19:
            case INT_20:
            case INT_21:
            case INT_22:
            case INT_23:
                {
                alt4=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(4);}

            switch (alt4) {
                case 1 :
                    dbg.enterAlt(1);

                    // NumericRules.g:35:6: INT_00
                    {
                    dbg.location(35,6);
                    INT_0010=(Token)match(input,INT_00,FOLLOW_INT_00_in_int_00_to_23_optional_prefix147); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT_00.add(INT_0010);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // NumericRules.g:36:5: INT_0
                    {
                    dbg.location(36,5);
                    INT_011=(Token)match(input,INT_0,FOLLOW_INT_0_in_int_00_to_23_optional_prefix154); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT_0.add(INT_011);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // NumericRules.g:37:5: int_1_to_9
                    {
                    dbg.location(37,5);
                    pushFollow(FOLLOW_int_1_to_9_in_int_00_to_23_optional_prefix160);
                    int_1_to_912=int_1_to_9();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_1_to_9.add(int_1_to_912.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // NumericRules.g:38:5: int_01_to_12
                    {
                    dbg.location(38,5);
                    pushFollow(FOLLOW_int_01_to_12_in_int_00_to_23_optional_prefix166);
                    int_01_to_1213=int_01_to_12();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_01_to_12.add(int_01_to_1213.getTree());

                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // NumericRules.g:39:5: int_13_to_23
                    {
                    dbg.location(39,5);
                    pushFollow(FOLLOW_int_13_to_23_in_int_00_to_23_optional_prefix172);
                    int_13_to_2314=int_13_to_23();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_13_to_23.add(int_13_to_2314.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(4);}



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 39:19: -> INT[$int_00_to_23_optional_prefix.text]
            {
                dbg.location(39,22);
                adaptor.addChild(root_0, (Object)adaptor.create(INT, input.toString(retval.start,input.LT(-1))));

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(40, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_00_to_23_optional_prefix");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_00_to_23_optional_prefix"

    public static class int_01_to_31_optional_prefix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_01_to_31_optional_prefix"
    // NumericRules.g:43:1: int_01_to_31_optional_prefix : ( int_01_to_12 | int_1_to_9 | int_13_to_23 | int_24_to_31 ) -> INT[$int_01_to_31_optional_prefix.text] ;
    public final DateParser_NumericRules.int_01_to_31_optional_prefix_return int_01_to_31_optional_prefix() throws RecognitionException {
        DateParser_NumericRules.int_01_to_31_optional_prefix_return retval = new DateParser_NumericRules.int_01_to_31_optional_prefix_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DateParser_NumericRules.int_01_to_12_return int_01_to_1215 = null;

        DateParser_NumericRules.int_1_to_9_return int_1_to_916 = null;

        DateParser_NumericRules.int_13_to_23_return int_13_to_2317 = null;

        DateParser_NumericRules.int_24_to_31_return int_24_to_3118 = null;


        RewriteRuleSubtreeStream stream_int_13_to_23=new RewriteRuleSubtreeStream(adaptor,"rule int_13_to_23");
        RewriteRuleSubtreeStream stream_int_01_to_12=new RewriteRuleSubtreeStream(adaptor,"rule int_01_to_12");
        RewriteRuleSubtreeStream stream_int_1_to_9=new RewriteRuleSubtreeStream(adaptor,"rule int_1_to_9");
        RewriteRuleSubtreeStream stream_int_24_to_31=new RewriteRuleSubtreeStream(adaptor,"rule int_24_to_31");
        try { dbg.enterRule(getGrammarFileName(), "int_01_to_31_optional_prefix");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(43, 1);

        try {
            // NumericRules.g:44:3: ( ( int_01_to_12 | int_1_to_9 | int_13_to_23 | int_24_to_31 ) -> INT[$int_01_to_31_optional_prefix.text] )
            dbg.enterAlt(1);

            // NumericRules.g:44:5: ( int_01_to_12 | int_1_to_9 | int_13_to_23 | int_24_to_31 )
            {
            dbg.location(44,5);
            // NumericRules.g:44:5: ( int_01_to_12 | int_1_to_9 | int_13_to_23 | int_24_to_31 )
            int alt5=4;
            try { dbg.enterSubRule(5);
            try { dbg.enterDecision(5);

            switch ( input.LA(1) ) {
            case INT_01:
            case INT_02:
            case INT_03:
            case INT_04:
            case INT_05:
            case INT_06:
            case INT_07:
            case INT_08:
            case INT_09:
            case INT_10:
            case INT_11:
            case INT_12:
                {
                alt5=1;
                }
                break;
            case INT_1:
            case INT_2:
            case INT_3:
            case INT_4:
            case INT_5:
            case INT_6:
            case INT_7:
            case INT_8:
            case INT_9:
                {
                alt5=2;
                }
                break;
            case INT_13:
            case INT_14:
            case INT_15:
            case INT_16:
            case INT_17:
            case INT_18:
            case INT_19:
            case INT_20:
            case INT_21:
            case INT_22:
            case INT_23:
                {
                alt5=3;
                }
                break;
            case INT_24:
            case INT_25:
            case INT_26:
            case INT_27:
            case INT_28:
            case INT_29:
            case INT_30:
            case INT_31:
                {
                alt5=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(5);}

            switch (alt5) {
                case 1 :
                    dbg.enterAlt(1);

                    // NumericRules.g:44:6: int_01_to_12
                    {
                    dbg.location(44,6);
                    pushFollow(FOLLOW_int_01_to_12_in_int_01_to_31_optional_prefix195);
                    int_01_to_1215=int_01_to_12();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_01_to_12.add(int_01_to_1215.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // NumericRules.g:45:5: int_1_to_9
                    {
                    dbg.location(45,5);
                    pushFollow(FOLLOW_int_1_to_9_in_int_01_to_31_optional_prefix201);
                    int_1_to_916=int_1_to_9();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_1_to_9.add(int_1_to_916.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // NumericRules.g:46:5: int_13_to_23
                    {
                    dbg.location(46,5);
                    pushFollow(FOLLOW_int_13_to_23_in_int_01_to_31_optional_prefix207);
                    int_13_to_2317=int_13_to_23();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_13_to_23.add(int_13_to_2317.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // NumericRules.g:47:5: int_24_to_31
                    {
                    dbg.location(47,5);
                    pushFollow(FOLLOW_int_24_to_31_in_int_01_to_31_optional_prefix213);
                    int_24_to_3118=int_24_to_31();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_int_24_to_31.add(int_24_to_3118.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(5);}



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 47:19: -> INT[$int_01_to_31_optional_prefix.text]
            {
                dbg.location(47,22);
                adaptor.addChild(root_0, (Object)adaptor.create(INT, input.toString(retval.start,input.LT(-1))));

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(48, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_01_to_31_optional_prefix");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_01_to_31_optional_prefix"

    public static class int_four_digits_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_four_digits"
    // NumericRules.g:51:1: int_four_digits : int_00_to_99_mandatory_prefix int_00_to_99_mandatory_prefix -> INT[$int_four_digits.text] ;
    public final DateParser_NumericRules.int_four_digits_return int_four_digits() throws RecognitionException {
        DateParser_NumericRules.int_four_digits_return retval = new DateParser_NumericRules.int_four_digits_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DateParser_NumericRules.int_00_to_99_mandatory_prefix_return int_00_to_99_mandatory_prefix19 = null;

        DateParser_NumericRules.int_00_to_99_mandatory_prefix_return int_00_to_99_mandatory_prefix20 = null;


        RewriteRuleSubtreeStream stream_int_00_to_99_mandatory_prefix=new RewriteRuleSubtreeStream(adaptor,"rule int_00_to_99_mandatory_prefix");
        try { dbg.enterRule(getGrammarFileName(), "int_four_digits");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(51, 1);

        try {
            // NumericRules.g:52:3: ( int_00_to_99_mandatory_prefix int_00_to_99_mandatory_prefix -> INT[$int_four_digits.text] )
            dbg.enterAlt(1);

            // NumericRules.g:52:5: int_00_to_99_mandatory_prefix int_00_to_99_mandatory_prefix
            {
            dbg.location(52,5);
            pushFollow(FOLLOW_int_00_to_99_mandatory_prefix_in_int_four_digits235);
            int_00_to_99_mandatory_prefix19=int_00_to_99_mandatory_prefix();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_int_00_to_99_mandatory_prefix.add(int_00_to_99_mandatory_prefix19.getTree());
            dbg.location(52,35);
            pushFollow(FOLLOW_int_00_to_99_mandatory_prefix_in_int_four_digits237);
            int_00_to_99_mandatory_prefix20=int_00_to_99_mandatory_prefix();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_int_00_to_99_mandatory_prefix.add(int_00_to_99_mandatory_prefix20.getTree());


            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 53:7: -> INT[$int_four_digits.text]
            {
                dbg.location(53,10);
                adaptor.addChild(root_0, (Object)adaptor.create(INT, input.toString(retval.start,input.LT(-1))));

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(54, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_four_digits");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_four_digits"

    public static class spelled_or_int_01_to_31_optional_prefix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "spelled_or_int_01_to_31_optional_prefix"
    // NumericRules.g:57:1: spelled_or_int_01_to_31_optional_prefix : ( int_01_to_31_optional_prefix | spelled_one_to_thirty_one );
    public final DateParser_NumericRules.spelled_or_int_01_to_31_optional_prefix_return spelled_or_int_01_to_31_optional_prefix() throws RecognitionException {
        DateParser_NumericRules.spelled_or_int_01_to_31_optional_prefix_return retval = new DateParser_NumericRules.spelled_or_int_01_to_31_optional_prefix_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DateParser_NumericRules.int_01_to_31_optional_prefix_return int_01_to_31_optional_prefix21 = null;

        DateParser_NumericRules.spelled_one_to_thirty_one_return spelled_one_to_thirty_one22 = null;



        try { dbg.enterRule(getGrammarFileName(), "spelled_or_int_01_to_31_optional_prefix");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(57, 1);

        try {
            // NumericRules.g:58:3: ( int_01_to_31_optional_prefix | spelled_one_to_thirty_one )
            int alt6=2;
            try { dbg.enterDecision(6);

            switch ( input.LA(1) ) {
            case INT_01:
            case INT_02:
            case INT_03:
            case INT_04:
            case INT_05:
            case INT_06:
            case INT_07:
            case INT_08:
            case INT_09:
            case INT_1:
            case INT_2:
            case INT_3:
            case INT_4:
            case INT_5:
            case INT_6:
            case INT_7:
            case INT_8:
            case INT_9:
            case INT_10:
            case INT_11:
            case INT_12:
            case INT_13:
            case INT_14:
            case INT_15:
            case INT_16:
            case INT_17:
            case INT_18:
            case INT_19:
            case INT_20:
            case INT_21:
            case INT_22:
            case INT_23:
            case INT_24:
            case INT_25:
            case INT_26:
            case INT_27:
            case INT_28:
            case INT_29:
            case INT_30:
            case INT_31:
                {
                alt6=1;
                }
                break;
            case UN:
            case DEUX:
            case TROIS:
            case QUATRE:
            case CINQ:
            case SIX:
            case SEPT:
            case HUIT:
            case NEUF:
            case DIX:
            case ONZE:
            case DOUZE:
            case TREIZE:
            case QUATORZE:
            case QUINZE:
            case SEIZE:
            case VINGT:
            case TRENTE:
            case PREMIER:
                {
                alt6=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(6);}

            switch (alt6) {
                case 1 :
                    dbg.enterAlt(1);

                    // NumericRules.g:58:5: int_01_to_31_optional_prefix
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(58,5);
                    pushFollow(FOLLOW_int_01_to_31_optional_prefix_in_spelled_or_int_01_to_31_optional_prefix264);
                    int_01_to_31_optional_prefix21=int_01_to_31_optional_prefix();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, int_01_to_31_optional_prefix21.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // NumericRules.g:59:5: spelled_one_to_thirty_one
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(59,5);
                    pushFollow(FOLLOW_spelled_one_to_thirty_one_in_spelled_or_int_01_to_31_optional_prefix270);
                    spelled_one_to_thirty_one22=spelled_one_to_thirty_one();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, spelled_one_to_thirty_one22.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(60, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "spelled_or_int_01_to_31_optional_prefix");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "spelled_or_int_01_to_31_optional_prefix"

    public static class spelled_or_int_optional_prefix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "spelled_or_int_optional_prefix"
    // NumericRules.g:63:1: spelled_or_int_optional_prefix : ( spelled_one_to_thirty_one | ( ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 ) ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 )? -> INT[$spelled_or_int_optional_prefix.text] ) );
    public final DateParser_NumericRules.spelled_or_int_optional_prefix_return spelled_or_int_optional_prefix() throws RecognitionException {
        DateParser_NumericRules.spelled_or_int_optional_prefix_return retval = new DateParser_NumericRules.spelled_or_int_optional_prefix_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DateParser_NumericRules.spelled_one_to_thirty_one_return spelled_one_to_thirty_one23 = null;

        DateParser_NumericRules.int_01_to_31_optional_prefix_return int_01_to_31_optional_prefix24 = null;

        DateParser_NumericRules.int_32_to_59_return int_32_to_5925 = null;

        DateParser_NumericRules.int_60_to_99_return int_60_to_9926 = null;

        DateParser_NumericRules.int_01_to_31_optional_prefix_return int_01_to_31_optional_prefix27 = null;

        DateParser_NumericRules.int_32_to_59_return int_32_to_5928 = null;

        DateParser_NumericRules.int_60_to_99_return int_60_to_9929 = null;


        RewriteRuleSubtreeStream stream_int_32_to_59=new RewriteRuleSubtreeStream(adaptor,"rule int_32_to_59");
        RewriteRuleSubtreeStream stream_int_01_to_31_optional_prefix=new RewriteRuleSubtreeStream(adaptor,"rule int_01_to_31_optional_prefix");
        RewriteRuleSubtreeStream stream_int_60_to_99=new RewriteRuleSubtreeStream(adaptor,"rule int_60_to_99");
        try { dbg.enterRule(getGrammarFileName(), "spelled_or_int_optional_prefix");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(63, 1);

        try {
            // NumericRules.g:64:3: ( spelled_one_to_thirty_one | ( ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 ) ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 )? -> INT[$spelled_or_int_optional_prefix.text] ) )
            int alt9=2;
            try { dbg.enterDecision(9);

            switch ( input.LA(1) ) {
            case UN:
            case DEUX:
            case TROIS:
            case QUATRE:
            case CINQ:
            case SIX:
            case SEPT:
            case HUIT:
            case NEUF:
            case DIX:
            case ONZE:
            case DOUZE:
            case TREIZE:
            case QUATORZE:
            case QUINZE:
            case SEIZE:
            case VINGT:
            case TRENTE:
            case PREMIER:
                {
                alt9=1;
                }
                break;
            case INT_01:
            case INT_02:
            case INT_03:
            case INT_04:
            case INT_05:
            case INT_06:
            case INT_07:
            case INT_08:
            case INT_09:
            case INT_1:
            case INT_2:
            case INT_3:
            case INT_4:
            case INT_5:
            case INT_6:
            case INT_7:
            case INT_8:
            case INT_9:
            case INT_10:
            case INT_11:
            case INT_12:
            case INT_13:
            case INT_14:
            case INT_15:
            case INT_16:
            case INT_17:
            case INT_18:
            case INT_19:
            case INT_20:
            case INT_21:
            case INT_22:
            case INT_23:
            case INT_24:
            case INT_25:
            case INT_26:
            case INT_27:
            case INT_28:
            case INT_29:
            case INT_30:
            case INT_31:
            case INT_32:
            case INT_33:
            case INT_34:
            case INT_35:
            case INT_36:
            case INT_37:
            case INT_38:
            case INT_39:
            case INT_40:
            case INT_41:
            case INT_42:
            case INT_43:
            case INT_44:
            case INT_45:
            case INT_46:
            case INT_47:
            case INT_48:
            case INT_49:
            case INT_50:
            case INT_51:
            case INT_52:
            case INT_53:
            case INT_54:
            case INT_55:
            case INT_56:
            case INT_57:
            case INT_58:
            case INT_59:
            case INT_60:
            case INT_61:
            case INT_62:
            case INT_63:
            case INT_64:
            case INT_65:
            case INT_66:
            case INT_67:
            case INT_68:
            case INT_69:
            case INT_70:
            case INT_71:
            case INT_72:
            case INT_73:
            case INT_74:
            case INT_75:
            case INT_76:
            case INT_77:
            case INT_78:
            case INT_79:
            case INT_80:
            case INT_81:
            case INT_82:
            case INT_83:
            case INT_84:
            case INT_85:
            case INT_86:
            case INT_87:
            case INT_88:
            case INT_89:
            case INT_90:
            case INT_91:
            case INT_92:
            case INT_93:
            case INT_94:
            case INT_95:
            case INT_96:
            case INT_97:
            case INT_98:
            case INT_99:
                {
                alt9=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(9);}

            switch (alt9) {
                case 1 :
                    dbg.enterAlt(1);

                    // NumericRules.g:64:5: spelled_one_to_thirty_one
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(64,5);
                    pushFollow(FOLLOW_spelled_one_to_thirty_one_in_spelled_or_int_optional_prefix286);
                    spelled_one_to_thirty_one23=spelled_one_to_thirty_one();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, spelled_one_to_thirty_one23.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // NumericRules.g:65:5: ( ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 ) ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 )? -> INT[$spelled_or_int_optional_prefix.text] )
                    {
                    dbg.location(65,5);
                    // NumericRules.g:65:5: ( ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 ) ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 )? -> INT[$spelled_or_int_optional_prefix.text] )
                    dbg.enterAlt(1);

                    // NumericRules.g:65:6: ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 ) ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 )?
                    {
                    dbg.location(65,6);
                    // NumericRules.g:65:6: ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 )
                    int alt7=3;
                    try { dbg.enterSubRule(7);
                    try { dbg.enterDecision(7);

                    switch ( input.LA(1) ) {
                    case INT_01:
                    case INT_02:
                    case INT_03:
                    case INT_04:
                    case INT_05:
                    case INT_06:
                    case INT_07:
                    case INT_08:
                    case INT_09:
                    case INT_1:
                    case INT_2:
                    case INT_3:
                    case INT_4:
                    case INT_5:
                    case INT_6:
                    case INT_7:
                    case INT_8:
                    case INT_9:
                    case INT_10:
                    case INT_11:
                    case INT_12:
                    case INT_13:
                    case INT_14:
                    case INT_15:
                    case INT_16:
                    case INT_17:
                    case INT_18:
                    case INT_19:
                    case INT_20:
                    case INT_21:
                    case INT_22:
                    case INT_23:
                    case INT_24:
                    case INT_25:
                    case INT_26:
                    case INT_27:
                    case INT_28:
                    case INT_29:
                    case INT_30:
                    case INT_31:
                        {
                        alt7=1;
                        }
                        break;
                    case INT_32:
                    case INT_33:
                    case INT_34:
                    case INT_35:
                    case INT_36:
                    case INT_37:
                    case INT_38:
                    case INT_39:
                    case INT_40:
                    case INT_41:
                    case INT_42:
                    case INT_43:
                    case INT_44:
                    case INT_45:
                    case INT_46:
                    case INT_47:
                    case INT_48:
                    case INT_49:
                    case INT_50:
                    case INT_51:
                    case INT_52:
                    case INT_53:
                    case INT_54:
                    case INT_55:
                    case INT_56:
                    case INT_57:
                    case INT_58:
                    case INT_59:
                        {
                        alt7=2;
                        }
                        break;
                    case INT_60:
                    case INT_61:
                    case INT_62:
                    case INT_63:
                    case INT_64:
                    case INT_65:
                    case INT_66:
                    case INT_67:
                    case INT_68:
                    case INT_69:
                    case INT_70:
                    case INT_71:
                    case INT_72:
                    case INT_73:
                    case INT_74:
                    case INT_75:
                    case INT_76:
                    case INT_77:
                    case INT_78:
                    case INT_79:
                    case INT_80:
                    case INT_81:
                    case INT_82:
                    case INT_83:
                    case INT_84:
                    case INT_85:
                    case INT_86:
                    case INT_87:
                    case INT_88:
                    case INT_89:
                    case INT_90:
                    case INT_91:
                    case INT_92:
                    case INT_93:
                    case INT_94:
                    case INT_95:
                    case INT_96:
                    case INT_97:
                    case INT_98:
                    case INT_99:
                        {
                        alt7=3;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 7, 0, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }

                    } finally {dbg.exitDecision(7);}

                    switch (alt7) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:65:7: int_01_to_31_optional_prefix
                            {
                            dbg.location(65,7);
                            pushFollow(FOLLOW_int_01_to_31_optional_prefix_in_spelled_or_int_optional_prefix295);
                            int_01_to_31_optional_prefix24=int_01_to_31_optional_prefix();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_int_01_to_31_optional_prefix.add(int_01_to_31_optional_prefix24.getTree());

                            }
                            break;
                        case 2 :
                            dbg.enterAlt(2);

                            // NumericRules.g:65:38: int_32_to_59
                            {
                            dbg.location(65,38);
                            pushFollow(FOLLOW_int_32_to_59_in_spelled_or_int_optional_prefix299);
                            int_32_to_5925=int_32_to_59();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_int_32_to_59.add(int_32_to_5925.getTree());

                            }
                            break;
                        case 3 :
                            dbg.enterAlt(3);

                            // NumericRules.g:65:53: int_60_to_99
                            {
                            dbg.location(65,53);
                            pushFollow(FOLLOW_int_60_to_99_in_spelled_or_int_optional_prefix303);
                            int_60_to_9926=int_60_to_99();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_int_60_to_99.add(int_60_to_9926.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(7);}

                    dbg.location(66,6);
                    // NumericRules.g:66:6: ( int_01_to_31_optional_prefix | int_32_to_59 | int_60_to_99 )?
                    int alt8=4;
                    try { dbg.enterSubRule(8);
                    try { dbg.enterDecision(8);

                    switch ( input.LA(1) ) {
                        case INT_01:
                        case INT_02:
                        case INT_03:
                        case INT_04:
                        case INT_05:
                        case INT_06:
                        case INT_07:
                        case INT_08:
                        case INT_09:
                        case INT_1:
                        case INT_2:
                        case INT_3:
                        case INT_4:
                        case INT_5:
                        case INT_6:
                        case INT_7:
                        case INT_8:
                        case INT_9:
                        case INT_10:
                        case INT_11:
                        case INT_12:
                        case INT_13:
                        case INT_14:
                        case INT_15:
                        case INT_16:
                        case INT_17:
                        case INT_18:
                        case INT_19:
                        case INT_20:
                        case INT_21:
                        case INT_22:
                        case INT_23:
                        case INT_24:
                        case INT_25:
                        case INT_26:
                        case INT_27:
                        case INT_28:
                        case INT_29:
                        case INT_30:
                        case INT_31:
                            {
                            alt8=1;
                            }
                            break;
                        case INT_32:
                        case INT_33:
                        case INT_34:
                        case INT_35:
                        case INT_36:
                        case INT_37:
                        case INT_38:
                        case INT_39:
                        case INT_40:
                        case INT_41:
                        case INT_42:
                        case INT_43:
                        case INT_44:
                        case INT_45:
                        case INT_46:
                        case INT_47:
                        case INT_48:
                        case INT_49:
                        case INT_50:
                        case INT_51:
                        case INT_52:
                        case INT_53:
                        case INT_54:
                        case INT_55:
                        case INT_56:
                        case INT_57:
                        case INT_58:
                        case INT_59:
                            {
                            alt8=2;
                            }
                            break;
                        case INT_60:
                        case INT_61:
                        case INT_62:
                        case INT_63:
                        case INT_64:
                        case INT_65:
                        case INT_66:
                        case INT_67:
                        case INT_68:
                        case INT_69:
                        case INT_70:
                        case INT_71:
                        case INT_72:
                        case INT_73:
                        case INT_74:
                        case INT_75:
                        case INT_76:
                        case INT_77:
                        case INT_78:
                        case INT_79:
                        case INT_80:
                        case INT_81:
                        case INT_82:
                        case INT_83:
                        case INT_84:
                        case INT_85:
                        case INT_86:
                        case INT_87:
                        case INT_88:
                        case INT_89:
                        case INT_90:
                        case INT_91:
                        case INT_92:
                        case INT_93:
                        case INT_94:
                        case INT_95:
                        case INT_96:
                        case INT_97:
                        case INT_98:
                        case INT_99:
                            {
                            alt8=3;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(8);}

                    switch (alt8) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:66:7: int_01_to_31_optional_prefix
                            {
                            dbg.location(66,7);
                            pushFollow(FOLLOW_int_01_to_31_optional_prefix_in_spelled_or_int_optional_prefix312);
                            int_01_to_31_optional_prefix27=int_01_to_31_optional_prefix();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_int_01_to_31_optional_prefix.add(int_01_to_31_optional_prefix27.getTree());

                            }
                            break;
                        case 2 :
                            dbg.enterAlt(2);

                            // NumericRules.g:66:38: int_32_to_59
                            {
                            dbg.location(66,38);
                            pushFollow(FOLLOW_int_32_to_59_in_spelled_or_int_optional_prefix316);
                            int_32_to_5928=int_32_to_59();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_int_32_to_59.add(int_32_to_5928.getTree());

                            }
                            break;
                        case 3 :
                            dbg.enterAlt(3);

                            // NumericRules.g:66:53: int_60_to_99
                            {
                            dbg.location(66,53);
                            pushFollow(FOLLOW_int_60_to_99_in_spelled_or_int_optional_prefix320);
                            int_60_to_9929=int_60_to_99();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_int_60_to_99.add(int_60_to_9929.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(8);}



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 67:8: -> INT[$spelled_or_int_optional_prefix.text]
                    {
                        dbg.location(67,11);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, input.toString(retval.start,input.LT(-1))));

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(68, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "spelled_or_int_optional_prefix");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "spelled_or_int_optional_prefix"

    public static class spelled_one_to_thirty_one_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "spelled_one_to_thirty_one"
    // NumericRules.g:72:1: spelled_one_to_thirty_one : ( ( UN | PREMIER ) -> INT[\"1\"] | DEUX -> INT[\"2\"] | TROIS -> INT[\"3\"] | QUATRE -> INT[\"4\"] | CINQ -> INT[\"5\"] | SIX -> INT[\"6\"] | SEPT -> INT[\"7\"] | HUIT -> INT[\"8\"] | NEUF -> INT[\"9\"] | ONZE -> INT[\"11\"] | DOUZE -> INT[\"12\"] | TREIZE -> INT[\"13\"] | QUATORZE -> INT[\"14\"] | QUINZE -> INT[\"15\"] | SEIZE -> INT[\"16\"] | ( DIX WHITE_SPACE SEPT )=> DIX WHITE_SPACE SEPT -> INT[\"17\"] | DIX ( DASH )? SEPT -> INT[\"17\"] | ( DIX WHITE_SPACE HUIT )=> DIX WHITE_SPACE HUIT -> INT[\"18\"] | DIX ( DASH )? HUIT -> INT[\"18\"] | ( DIX WHITE_SPACE NEUF )=> DIX WHITE_SPACE NEUF -> INT[\"19\"] | DIX ( DASH )? NEUF -> INT[\"19\"] | DIX -> INT[\"10\"] | ( VINGT WHITE_SPACE ET WHITE_SPACE UN )=> VINGT WHITE_SPACE ET WHITE_SPACE UN -> INT[\"21\"] | VINGT ( DASH )? ET ( DASH )? UN -> INT[\"21\"] | ( VINGT WHITE_SPACE DEUX )=> VINGT WHITE_SPACE DEUX -> INT[\"22\"] | VINGT ( DASH )? DEUX -> INT[\"22\"] | ( VINGT WHITE_SPACE TROIS )=> VINGT WHITE_SPACE TROIS -> INT[\"23\"] | VINGT ( DASH )? TROIS -> INT[\"23\"] | ( VINGT WHITE_SPACE QUATRE )=> VINGT WHITE_SPACE QUATRE -> INT[\"24\"] | VINGT ( DASH )? QUATRE -> INT[\"24\"] | ( VINGT WHITE_SPACE CINQ )=> VINGT WHITE_SPACE CINQ -> INT[\"25\"] | VINGT ( DASH )? CINQ -> INT[\"25\"] | ( VINGT WHITE_SPACE SIX )=> VINGT WHITE_SPACE SIX -> INT[\"26\"] | VINGT ( DASH )? SIX -> INT[\"26\"] | ( VINGT WHITE_SPACE SEPT )=> VINGT WHITE_SPACE SEPT -> INT[\"27\"] | VINGT ( DASH )? SEPT -> INT[\"27\"] | ( VINGT WHITE_SPACE HUIT )=> VINGT WHITE_SPACE HUIT -> INT[\"28\"] | VINGT ( DASH )? HUIT -> INT[\"28\"] | ( VINGT WHITE_SPACE NEUF )=> VINGT WHITE_SPACE NEUF -> INT[\"29\"] | VINGT ( DASH )? NEUF -> INT[\"29\"] | VINGT -> INT[\"20\"] | ( TRENTE WHITE_SPACE ET WHITE_SPACE UN )=> TRENTE WHITE_SPACE ET WHITE_SPACE UN -> INT[\"31\"] | TRENTE ( DASH )? ET ( DASH )? UN -> INT[\"31\"] | TRENTE -> INT[\"30\"] );
    public final DateParser_NumericRules.spelled_one_to_thirty_one_return spelled_one_to_thirty_one() throws RecognitionException {
        DateParser_NumericRules.spelled_one_to_thirty_one_return retval = new DateParser_NumericRules.spelled_one_to_thirty_one_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UN30=null;
        Token PREMIER31=null;
        Token DEUX32=null;
        Token TROIS33=null;
        Token QUATRE34=null;
        Token CINQ35=null;
        Token SIX36=null;
        Token SEPT37=null;
        Token HUIT38=null;
        Token NEUF39=null;
        Token ONZE40=null;
        Token DOUZE41=null;
        Token TREIZE42=null;
        Token QUATORZE43=null;
        Token QUINZE44=null;
        Token SEIZE45=null;
        Token DIX46=null;
        Token WHITE_SPACE47=null;
        Token SEPT48=null;
        Token DIX49=null;
        Token DASH50=null;
        Token SEPT51=null;
        Token DIX52=null;
        Token WHITE_SPACE53=null;
        Token HUIT54=null;
        Token DIX55=null;
        Token DASH56=null;
        Token HUIT57=null;
        Token DIX58=null;
        Token WHITE_SPACE59=null;
        Token NEUF60=null;
        Token DIX61=null;
        Token DASH62=null;
        Token NEUF63=null;
        Token DIX64=null;
        Token VINGT65=null;
        Token WHITE_SPACE66=null;
        Token ET67=null;
        Token WHITE_SPACE68=null;
        Token UN69=null;
        Token VINGT70=null;
        Token DASH71=null;
        Token ET72=null;
        Token DASH73=null;
        Token UN74=null;
        Token VINGT75=null;
        Token WHITE_SPACE76=null;
        Token DEUX77=null;
        Token VINGT78=null;
        Token DASH79=null;
        Token DEUX80=null;
        Token VINGT81=null;
        Token WHITE_SPACE82=null;
        Token TROIS83=null;
        Token VINGT84=null;
        Token DASH85=null;
        Token TROIS86=null;
        Token VINGT87=null;
        Token WHITE_SPACE88=null;
        Token QUATRE89=null;
        Token VINGT90=null;
        Token DASH91=null;
        Token QUATRE92=null;
        Token VINGT93=null;
        Token WHITE_SPACE94=null;
        Token CINQ95=null;
        Token VINGT96=null;
        Token DASH97=null;
        Token CINQ98=null;
        Token VINGT99=null;
        Token WHITE_SPACE100=null;
        Token SIX101=null;
        Token VINGT102=null;
        Token DASH103=null;
        Token SIX104=null;
        Token VINGT105=null;
        Token WHITE_SPACE106=null;
        Token SEPT107=null;
        Token VINGT108=null;
        Token DASH109=null;
        Token SEPT110=null;
        Token VINGT111=null;
        Token WHITE_SPACE112=null;
        Token HUIT113=null;
        Token VINGT114=null;
        Token DASH115=null;
        Token HUIT116=null;
        Token VINGT117=null;
        Token WHITE_SPACE118=null;
        Token NEUF119=null;
        Token VINGT120=null;
        Token DASH121=null;
        Token NEUF122=null;
        Token VINGT123=null;
        Token TRENTE124=null;
        Token WHITE_SPACE125=null;
        Token ET126=null;
        Token WHITE_SPACE127=null;
        Token UN128=null;
        Token TRENTE129=null;
        Token DASH130=null;
        Token ET131=null;
        Token DASH132=null;
        Token UN133=null;
        Token TRENTE134=null;

        Object UN30_tree=null;
        Object PREMIER31_tree=null;
        Object DEUX32_tree=null;
        Object TROIS33_tree=null;
        Object QUATRE34_tree=null;
        Object CINQ35_tree=null;
        Object SIX36_tree=null;
        Object SEPT37_tree=null;
        Object HUIT38_tree=null;
        Object NEUF39_tree=null;
        Object ONZE40_tree=null;
        Object DOUZE41_tree=null;
        Object TREIZE42_tree=null;
        Object QUATORZE43_tree=null;
        Object QUINZE44_tree=null;
        Object SEIZE45_tree=null;
        Object DIX46_tree=null;
        Object WHITE_SPACE47_tree=null;
        Object SEPT48_tree=null;
        Object DIX49_tree=null;
        Object DASH50_tree=null;
        Object SEPT51_tree=null;
        Object DIX52_tree=null;
        Object WHITE_SPACE53_tree=null;
        Object HUIT54_tree=null;
        Object DIX55_tree=null;
        Object DASH56_tree=null;
        Object HUIT57_tree=null;
        Object DIX58_tree=null;
        Object WHITE_SPACE59_tree=null;
        Object NEUF60_tree=null;
        Object DIX61_tree=null;
        Object DASH62_tree=null;
        Object NEUF63_tree=null;
        Object DIX64_tree=null;
        Object VINGT65_tree=null;
        Object WHITE_SPACE66_tree=null;
        Object ET67_tree=null;
        Object WHITE_SPACE68_tree=null;
        Object UN69_tree=null;
        Object VINGT70_tree=null;
        Object DASH71_tree=null;
        Object ET72_tree=null;
        Object DASH73_tree=null;
        Object UN74_tree=null;
        Object VINGT75_tree=null;
        Object WHITE_SPACE76_tree=null;
        Object DEUX77_tree=null;
        Object VINGT78_tree=null;
        Object DASH79_tree=null;
        Object DEUX80_tree=null;
        Object VINGT81_tree=null;
        Object WHITE_SPACE82_tree=null;
        Object TROIS83_tree=null;
        Object VINGT84_tree=null;
        Object DASH85_tree=null;
        Object TROIS86_tree=null;
        Object VINGT87_tree=null;
        Object WHITE_SPACE88_tree=null;
        Object QUATRE89_tree=null;
        Object VINGT90_tree=null;
        Object DASH91_tree=null;
        Object QUATRE92_tree=null;
        Object VINGT93_tree=null;
        Object WHITE_SPACE94_tree=null;
        Object CINQ95_tree=null;
        Object VINGT96_tree=null;
        Object DASH97_tree=null;
        Object CINQ98_tree=null;
        Object VINGT99_tree=null;
        Object WHITE_SPACE100_tree=null;
        Object SIX101_tree=null;
        Object VINGT102_tree=null;
        Object DASH103_tree=null;
        Object SIX104_tree=null;
        Object VINGT105_tree=null;
        Object WHITE_SPACE106_tree=null;
        Object SEPT107_tree=null;
        Object VINGT108_tree=null;
        Object DASH109_tree=null;
        Object SEPT110_tree=null;
        Object VINGT111_tree=null;
        Object WHITE_SPACE112_tree=null;
        Object HUIT113_tree=null;
        Object VINGT114_tree=null;
        Object DASH115_tree=null;
        Object HUIT116_tree=null;
        Object VINGT117_tree=null;
        Object WHITE_SPACE118_tree=null;
        Object NEUF119_tree=null;
        Object VINGT120_tree=null;
        Object DASH121_tree=null;
        Object NEUF122_tree=null;
        Object VINGT123_tree=null;
        Object TRENTE124_tree=null;
        Object WHITE_SPACE125_tree=null;
        Object ET126_tree=null;
        Object WHITE_SPACE127_tree=null;
        Object UN128_tree=null;
        Object TRENTE129_tree=null;
        Object DASH130_tree=null;
        Object ET131_tree=null;
        Object DASH132_tree=null;
        Object UN133_tree=null;
        Object TRENTE134_tree=null;
        RewriteRuleTokenStream stream_SEIZE=new RewriteRuleTokenStream(adaptor,"token SEIZE");
        RewriteRuleTokenStream stream_QUATORZE=new RewriteRuleTokenStream(adaptor,"token QUATORZE");
        RewriteRuleTokenStream stream_TRENTE=new RewriteRuleTokenStream(adaptor,"token TRENTE");
        RewriteRuleTokenStream stream_QUINZE=new RewriteRuleTokenStream(adaptor,"token QUINZE");
        RewriteRuleTokenStream stream_UN=new RewriteRuleTokenStream(adaptor,"token UN");
        RewriteRuleTokenStream stream_DEUX=new RewriteRuleTokenStream(adaptor,"token DEUX");
        RewriteRuleTokenStream stream_VINGT=new RewriteRuleTokenStream(adaptor,"token VINGT");
        RewriteRuleTokenStream stream_CINQ=new RewriteRuleTokenStream(adaptor,"token CINQ");
        RewriteRuleTokenStream stream_DASH=new RewriteRuleTokenStream(adaptor,"token DASH");
        RewriteRuleTokenStream stream_SEPT=new RewriteRuleTokenStream(adaptor,"token SEPT");
        RewriteRuleTokenStream stream_TREIZE=new RewriteRuleTokenStream(adaptor,"token TREIZE");
        RewriteRuleTokenStream stream_NEUF=new RewriteRuleTokenStream(adaptor,"token NEUF");
        RewriteRuleTokenStream stream_ET=new RewriteRuleTokenStream(adaptor,"token ET");
        RewriteRuleTokenStream stream_TROIS=new RewriteRuleTokenStream(adaptor,"token TROIS");
        RewriteRuleTokenStream stream_DIX=new RewriteRuleTokenStream(adaptor,"token DIX");
        RewriteRuleTokenStream stream_HUIT=new RewriteRuleTokenStream(adaptor,"token HUIT");
        RewriteRuleTokenStream stream_SIX=new RewriteRuleTokenStream(adaptor,"token SIX");
        RewriteRuleTokenStream stream_WHITE_SPACE=new RewriteRuleTokenStream(adaptor,"token WHITE_SPACE");
        RewriteRuleTokenStream stream_QUATRE=new RewriteRuleTokenStream(adaptor,"token QUATRE");
        RewriteRuleTokenStream stream_ONZE=new RewriteRuleTokenStream(adaptor,"token ONZE");
        RewriteRuleTokenStream stream_DOUZE=new RewriteRuleTokenStream(adaptor,"token DOUZE");
        RewriteRuleTokenStream stream_PREMIER=new RewriteRuleTokenStream(adaptor,"token PREMIER");

        try { dbg.enterRule(getGrammarFileName(), "spelled_one_to_thirty_one");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(72, 1);

        try {
            // NumericRules.g:73:3: ( ( UN | PREMIER ) -> INT[\"1\"] | DEUX -> INT[\"2\"] | TROIS -> INT[\"3\"] | QUATRE -> INT[\"4\"] | CINQ -> INT[\"5\"] | SIX -> INT[\"6\"] | SEPT -> INT[\"7\"] | HUIT -> INT[\"8\"] | NEUF -> INT[\"9\"] | ONZE -> INT[\"11\"] | DOUZE -> INT[\"12\"] | TREIZE -> INT[\"13\"] | QUATORZE -> INT[\"14\"] | QUINZE -> INT[\"15\"] | SEIZE -> INT[\"16\"] | ( DIX WHITE_SPACE SEPT )=> DIX WHITE_SPACE SEPT -> INT[\"17\"] | DIX ( DASH )? SEPT -> INT[\"17\"] | ( DIX WHITE_SPACE HUIT )=> DIX WHITE_SPACE HUIT -> INT[\"18\"] | DIX ( DASH )? HUIT -> INT[\"18\"] | ( DIX WHITE_SPACE NEUF )=> DIX WHITE_SPACE NEUF -> INT[\"19\"] | DIX ( DASH )? NEUF -> INT[\"19\"] | DIX -> INT[\"10\"] | ( VINGT WHITE_SPACE ET WHITE_SPACE UN )=> VINGT WHITE_SPACE ET WHITE_SPACE UN -> INT[\"21\"] | VINGT ( DASH )? ET ( DASH )? UN -> INT[\"21\"] | ( VINGT WHITE_SPACE DEUX )=> VINGT WHITE_SPACE DEUX -> INT[\"22\"] | VINGT ( DASH )? DEUX -> INT[\"22\"] | ( VINGT WHITE_SPACE TROIS )=> VINGT WHITE_SPACE TROIS -> INT[\"23\"] | VINGT ( DASH )? TROIS -> INT[\"23\"] | ( VINGT WHITE_SPACE QUATRE )=> VINGT WHITE_SPACE QUATRE -> INT[\"24\"] | VINGT ( DASH )? QUATRE -> INT[\"24\"] | ( VINGT WHITE_SPACE CINQ )=> VINGT WHITE_SPACE CINQ -> INT[\"25\"] | VINGT ( DASH )? CINQ -> INT[\"25\"] | ( VINGT WHITE_SPACE SIX )=> VINGT WHITE_SPACE SIX -> INT[\"26\"] | VINGT ( DASH )? SIX -> INT[\"26\"] | ( VINGT WHITE_SPACE SEPT )=> VINGT WHITE_SPACE SEPT -> INT[\"27\"] | VINGT ( DASH )? SEPT -> INT[\"27\"] | ( VINGT WHITE_SPACE HUIT )=> VINGT WHITE_SPACE HUIT -> INT[\"28\"] | VINGT ( DASH )? HUIT -> INT[\"28\"] | ( VINGT WHITE_SPACE NEUF )=> VINGT WHITE_SPACE NEUF -> INT[\"29\"] | VINGT ( DASH )? NEUF -> INT[\"29\"] | VINGT -> INT[\"20\"] | ( TRENTE WHITE_SPACE ET WHITE_SPACE UN )=> TRENTE WHITE_SPACE ET WHITE_SPACE UN -> INT[\"31\"] | TRENTE ( DASH )? ET ( DASH )? UN -> INT[\"31\"] | TRENTE -> INT[\"30\"] )
            int alt26=44;
            try { dbg.enterDecision(26);

            try {
                isCyclicDecision = true;
                alt26 = dfa26.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(26);}

            switch (alt26) {
                case 1 :
                    dbg.enterAlt(1);

                    // NumericRules.g:73:5: ( UN | PREMIER )
                    {
                    dbg.location(73,5);
                    // NumericRules.g:73:5: ( UN | PREMIER )
                    int alt10=2;
                    try { dbg.enterSubRule(10);
                    try { dbg.enterDecision(10);

                    switch ( input.LA(1) ) {
                    case UN:
                        {
                        alt10=1;
                        }
                        break;
                    case PREMIER:
                        {
                        alt10=2;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 0, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }

                    } finally {dbg.exitDecision(10);}

                    switch (alt10) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:73:6: UN
                            {
                            dbg.location(73,6);
                            UN30=(Token)match(input,UN,FOLLOW_UN_in_spelled_one_to_thirty_one354); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_UN.add(UN30);


                            }
                            break;
                        case 2 :
                            dbg.enterAlt(2);

                            // NumericRules.g:73:11: PREMIER
                            {
                            dbg.location(73,11);
                            PREMIER31=(Token)match(input,PREMIER,FOLLOW_PREMIER_in_spelled_one_to_thirty_one358); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_PREMIER.add(PREMIER31);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(10);}



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 73:26: -> INT[\"1\"]
                    {
                        dbg.location(73,29);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "1"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // NumericRules.g:74:5: DEUX
                    {
                    dbg.location(74,5);
                    DEUX32=(Token)match(input,DEUX,FOLLOW_DEUX_in_spelled_one_to_thirty_one376); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DEUX.add(DEUX32);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 74:17: -> INT[\"2\"]
                    {
                        dbg.location(74,20);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "2"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // NumericRules.g:75:5: TROIS
                    {
                    dbg.location(75,5);
                    TROIS33=(Token)match(input,TROIS,FOLLOW_TROIS_in_spelled_one_to_thirty_one394); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TROIS.add(TROIS33);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 75:16: -> INT[\"3\"]
                    {
                        dbg.location(75,19);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "3"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // NumericRules.g:76:5: QUATRE
                    {
                    dbg.location(76,5);
                    QUATRE34=(Token)match(input,QUATRE,FOLLOW_QUATRE_in_spelled_one_to_thirty_one410); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUATRE.add(QUATRE34);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 76:18: -> INT[\"4\"]
                    {
                        dbg.location(76,21);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "4"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // NumericRules.g:77:5: CINQ
                    {
                    dbg.location(77,5);
                    CINQ35=(Token)match(input,CINQ,FOLLOW_CINQ_in_spelled_one_to_thirty_one427); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CINQ.add(CINQ35);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 77:16: -> INT[\"5\"]
                    {
                        dbg.location(77,19);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "5"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 6 :
                    dbg.enterAlt(6);

                    // NumericRules.g:78:5: SIX
                    {
                    dbg.location(78,5);
                    SIX36=(Token)match(input,SIX,FOLLOW_SIX_in_spelled_one_to_thirty_one444); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SIX.add(SIX36);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 78:16: -> INT[\"6\"]
                    {
                        dbg.location(78,19);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "6"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 7 :
                    dbg.enterAlt(7);

                    // NumericRules.g:79:5: SEPT
                    {
                    dbg.location(79,5);
                    SEPT37=(Token)match(input,SEPT,FOLLOW_SEPT_in_spelled_one_to_thirty_one462); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEPT.add(SEPT37);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 79:15: -> INT[\"7\"]
                    {
                        dbg.location(79,18);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "7"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 8 :
                    dbg.enterAlt(8);

                    // NumericRules.g:80:5: HUIT
                    {
                    dbg.location(80,5);
                    HUIT38=(Token)match(input,HUIT,FOLLOW_HUIT_in_spelled_one_to_thirty_one478); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_HUIT.add(HUIT38);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 80:15: -> INT[\"8\"]
                    {
                        dbg.location(80,18);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "8"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 9 :
                    dbg.enterAlt(9);

                    // NumericRules.g:81:5: NEUF
                    {
                    dbg.location(81,5);
                    NEUF39=(Token)match(input,NEUF,FOLLOW_NEUF_in_spelled_one_to_thirty_one494); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEUF.add(NEUF39);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 81:16: -> INT[\"9\"]
                    {
                        dbg.location(81,19);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "9"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 10 :
                    dbg.enterAlt(10);

                    // NumericRules.g:82:5: ONZE
                    {
                    dbg.location(82,5);
                    ONZE40=(Token)match(input,ONZE,FOLLOW_ONZE_in_spelled_one_to_thirty_one511); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ONZE.add(ONZE40);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 82:14: -> INT[\"11\"]
                    {
                        dbg.location(82,17);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "11"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 11 :
                    dbg.enterAlt(11);

                    // NumericRules.g:83:5: DOUZE
                    {
                    dbg.location(83,5);
                    DOUZE41=(Token)match(input,DOUZE,FOLLOW_DOUZE_in_spelled_one_to_thirty_one526); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DOUZE.add(DOUZE41);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 83:15: -> INT[\"12\"]
                    {
                        dbg.location(83,18);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "12"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 12 :
                    dbg.enterAlt(12);

                    // NumericRules.g:84:5: TREIZE
                    {
                    dbg.location(84,5);
                    TREIZE42=(Token)match(input,TREIZE,FOLLOW_TREIZE_in_spelled_one_to_thirty_one541); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TREIZE.add(TREIZE42);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 84:14: -> INT[\"13\"]
                    {
                        dbg.location(84,17);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "13"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 13 :
                    dbg.enterAlt(13);

                    // NumericRules.g:85:5: QUATORZE
                    {
                    dbg.location(85,5);
                    QUATORZE43=(Token)match(input,QUATORZE,FOLLOW_QUATORZE_in_spelled_one_to_thirty_one554); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUATORZE.add(QUATORZE43);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 85:16: -> INT[\"14\"]
                    {
                        dbg.location(85,19);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "14"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 14 :
                    dbg.enterAlt(14);

                    // NumericRules.g:86:5: QUINZE
                    {
                    dbg.location(86,5);
                    QUINZE44=(Token)match(input,QUINZE,FOLLOW_QUINZE_in_spelled_one_to_thirty_one567); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUINZE.add(QUINZE44);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 86:15: -> INT[\"15\"]
                    {
                        dbg.location(86,18);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "15"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 15 :
                    dbg.enterAlt(15);

                    // NumericRules.g:87:5: SEIZE
                    {
                    dbg.location(87,5);
                    SEIZE45=(Token)match(input,SEIZE,FOLLOW_SEIZE_in_spelled_one_to_thirty_one581); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEIZE.add(SEIZE45);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 87:14: -> INT[\"16\"]
                    {
                        dbg.location(87,17);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "16"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 16 :
                    dbg.enterAlt(16);

                    // NumericRules.g:88:5: ( DIX WHITE_SPACE SEPT )=> DIX WHITE_SPACE SEPT
                    {
                    dbg.location(88,30);
                    DIX46=(Token)match(input,DIX,FOLLOW_DIX_in_spelled_one_to_thirty_one604); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIX.add(DIX46);

                    dbg.location(88,34);
                    WHITE_SPACE47=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one606); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE47);

                    dbg.location(88,46);
                    SEPT48=(Token)match(input,SEPT,FOLLOW_SEPT_in_spelled_one_to_thirty_one608); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEPT.add(SEPT48);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 88:53: -> INT[\"17\"]
                    {
                        dbg.location(88,56);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "17"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 17 :
                    dbg.enterAlt(17);

                    // NumericRules.g:89:5: DIX ( DASH )? SEPT
                    {
                    dbg.location(89,5);
                    DIX49=(Token)match(input,DIX,FOLLOW_DIX_in_spelled_one_to_thirty_one621); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIX.add(DIX49);

                    dbg.location(89,9);
                    // NumericRules.g:89:9: ( DASH )?
                    int alt11=2;
                    try { dbg.enterSubRule(11);
                    try { dbg.enterDecision(11);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt11=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(11);}

                    switch (alt11) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:89:9: DASH
                            {
                            dbg.location(89,9);
                            DASH50=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one623); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH50);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(11);}

                    dbg.location(89,15);
                    SEPT51=(Token)match(input,SEPT,FOLLOW_SEPT_in_spelled_one_to_thirty_one626); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEPT.add(SEPT51);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 89:53: -> INT[\"17\"]
                    {
                        dbg.location(89,56);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "17"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 18 :
                    dbg.enterAlt(18);

                    // NumericRules.g:90:5: ( DIX WHITE_SPACE HUIT )=> DIX WHITE_SPACE HUIT
                    {
                    dbg.location(90,30);
                    DIX52=(Token)match(input,DIX,FOLLOW_DIX_in_spelled_one_to_thirty_one679); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIX.add(DIX52);

                    dbg.location(90,34);
                    WHITE_SPACE53=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one681); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE53);

                    dbg.location(90,46);
                    HUIT54=(Token)match(input,HUIT,FOLLOW_HUIT_in_spelled_one_to_thirty_one683); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_HUIT.add(HUIT54);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 90:53: -> INT[\"18\"]
                    {
                        dbg.location(90,56);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "18"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 19 :
                    dbg.enterAlt(19);

                    // NumericRules.g:91:5: DIX ( DASH )? HUIT
                    {
                    dbg.location(91,5);
                    DIX55=(Token)match(input,DIX,FOLLOW_DIX_in_spelled_one_to_thirty_one696); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIX.add(DIX55);

                    dbg.location(91,9);
                    // NumericRules.g:91:9: ( DASH )?
                    int alt12=2;
                    try { dbg.enterSubRule(12);
                    try { dbg.enterDecision(12);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt12=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(12);}

                    switch (alt12) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:91:9: DASH
                            {
                            dbg.location(91,9);
                            DASH56=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one698); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH56);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(12);}

                    dbg.location(91,15);
                    HUIT57=(Token)match(input,HUIT,FOLLOW_HUIT_in_spelled_one_to_thirty_one701); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_HUIT.add(HUIT57);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 91:53: -> INT[\"18\"]
                    {
                        dbg.location(91,56);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "18"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 20 :
                    dbg.enterAlt(20);

                    // NumericRules.g:92:5: ( DIX WHITE_SPACE NEUF )=> DIX WHITE_SPACE NEUF
                    {
                    dbg.location(92,30);
                    DIX58=(Token)match(input,DIX,FOLLOW_DIX_in_spelled_one_to_thirty_one754); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIX.add(DIX58);

                    dbg.location(92,34);
                    WHITE_SPACE59=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one756); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE59);

                    dbg.location(92,46);
                    NEUF60=(Token)match(input,NEUF,FOLLOW_NEUF_in_spelled_one_to_thirty_one758); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEUF.add(NEUF60);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 92:53: -> INT[\"19\"]
                    {
                        dbg.location(92,56);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "19"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 21 :
                    dbg.enterAlt(21);

                    // NumericRules.g:93:5: DIX ( DASH )? NEUF
                    {
                    dbg.location(93,5);
                    DIX61=(Token)match(input,DIX,FOLLOW_DIX_in_spelled_one_to_thirty_one771); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIX.add(DIX61);

                    dbg.location(93,9);
                    // NumericRules.g:93:9: ( DASH )?
                    int alt13=2;
                    try { dbg.enterSubRule(13);
                    try { dbg.enterDecision(13);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt13=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(13);}

                    switch (alt13) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:93:9: DASH
                            {
                            dbg.location(93,9);
                            DASH62=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one773); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH62);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(13);}

                    dbg.location(93,15);
                    NEUF63=(Token)match(input,NEUF,FOLLOW_NEUF_in_spelled_one_to_thirty_one776); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEUF.add(NEUF63);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 93:53: -> INT[\"19\"]
                    {
                        dbg.location(93,56);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "19"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 22 :
                    dbg.enterAlt(22);

                    // NumericRules.g:94:5: DIX
                    {
                    dbg.location(94,5);
                    DIX64=(Token)match(input,DIX,FOLLOW_DIX_in_spelled_one_to_thirty_one820); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DIX.add(DIX64);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 94:53: -> INT[\"10\"]
                    {
                        dbg.location(94,56);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "10"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 23 :
                    dbg.enterAlt(23);

                    // NumericRules.g:95:5: ( VINGT WHITE_SPACE ET WHITE_SPACE UN )=> VINGT WHITE_SPACE ET WHITE_SPACE UN
                    {
                    dbg.location(95,45);
                    VINGT65=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one888); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT65);

                    dbg.location(95,51);
                    WHITE_SPACE66=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one890); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE66);

                    dbg.location(95,63);
                    ET67=(Token)match(input,ET,FOLLOW_ET_in_spelled_one_to_thirty_one892); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ET.add(ET67);

                    dbg.location(95,66);
                    WHITE_SPACE68=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one894); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE68);

                    dbg.location(95,78);
                    UN69=(Token)match(input,UN,FOLLOW_UN_in_spelled_one_to_thirty_one896); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_UN.add(UN69);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 95:81: -> INT[\"21\"]
                    {
                        dbg.location(95,84);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "21"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 24 :
                    dbg.enterAlt(24);

                    // NumericRules.g:96:5: VINGT ( DASH )? ET ( DASH )? UN
                    {
                    dbg.location(96,5);
                    VINGT70=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one907); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT70);

                    dbg.location(96,11);
                    // NumericRules.g:96:11: ( DASH )?
                    int alt14=2;
                    try { dbg.enterSubRule(14);
                    try { dbg.enterDecision(14);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt14=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(14);}

                    switch (alt14) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:96:11: DASH
                            {
                            dbg.location(96,11);
                            DASH71=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one909); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH71);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(14);}

                    dbg.location(96,17);
                    ET72=(Token)match(input,ET,FOLLOW_ET_in_spelled_one_to_thirty_one912); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ET.add(ET72);

                    dbg.location(96,20);
                    // NumericRules.g:96:20: ( DASH )?
                    int alt15=2;
                    try { dbg.enterSubRule(15);
                    try { dbg.enterDecision(15);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt15=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(15);}

                    switch (alt15) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:96:20: DASH
                            {
                            dbg.location(96,20);
                            DASH73=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one914); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH73);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(15);}

                    dbg.location(96,26);
                    UN74=(Token)match(input,UN,FOLLOW_UN_in_spelled_one_to_thirty_one917); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_UN.add(UN74);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 96:81: -> INT[\"21\"]
                    {
                        dbg.location(96,84);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "21"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 25 :
                    dbg.enterAlt(25);

                    // NumericRules.g:97:5: ( VINGT WHITE_SPACE DEUX )=> VINGT WHITE_SPACE DEUX
                    {
                    dbg.location(97,35);
                    VINGT75=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one992); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT75);

                    dbg.location(97,41);
                    WHITE_SPACE76=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one994); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE76);

                    dbg.location(97,53);
                    DEUX77=(Token)match(input,DEUX,FOLLOW_DEUX_in_spelled_one_to_thirty_one996); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DEUX.add(DEUX77);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 97:61: -> INT[\"22\"]
                    {
                        dbg.location(97,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "22"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 26 :
                    dbg.enterAlt(26);

                    // NumericRules.g:98:5: VINGT ( DASH )? DEUX
                    {
                    dbg.location(98,5);
                    VINGT78=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1010); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT78);

                    dbg.location(98,11);
                    // NumericRules.g:98:11: ( DASH )?
                    int alt16=2;
                    try { dbg.enterSubRule(16);
                    try { dbg.enterDecision(16);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt16=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(16);}

                    switch (alt16) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:98:11: DASH
                            {
                            dbg.location(98,11);
                            DASH79=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1012); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH79);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(16);}

                    dbg.location(98,17);
                    DEUX80=(Token)match(input,DEUX,FOLLOW_DEUX_in_spelled_one_to_thirty_one1015); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DEUX.add(DEUX80);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 98:61: -> INT[\"22\"]
                    {
                        dbg.location(98,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "22"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 27 :
                    dbg.enterAlt(27);

                    // NumericRules.g:99:5: ( VINGT WHITE_SPACE TROIS )=> VINGT WHITE_SPACE TROIS
                    {
                    dbg.location(99,35);
                    VINGT81=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1076); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT81);

                    dbg.location(99,41);
                    WHITE_SPACE82=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1078); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE82);

                    dbg.location(99,53);
                    TROIS83=(Token)match(input,TROIS,FOLLOW_TROIS_in_spelled_one_to_thirty_one1080); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TROIS.add(TROIS83);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 99:61: -> INT[\"23\"]
                    {
                        dbg.location(99,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "23"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 28 :
                    dbg.enterAlt(28);

                    // NumericRules.g:100:5: VINGT ( DASH )? TROIS
                    {
                    dbg.location(100,5);
                    VINGT84=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1093); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT84);

                    dbg.location(100,11);
                    // NumericRules.g:100:11: ( DASH )?
                    int alt17=2;
                    try { dbg.enterSubRule(17);
                    try { dbg.enterDecision(17);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt17=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(17);}

                    switch (alt17) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:100:11: DASH
                            {
                            dbg.location(100,11);
                            DASH85=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1095); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH85);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(17);}

                    dbg.location(100,17);
                    TROIS86=(Token)match(input,TROIS,FOLLOW_TROIS_in_spelled_one_to_thirty_one1098); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TROIS.add(TROIS86);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 100:61: -> INT[\"23\"]
                    {
                        dbg.location(100,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "23"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 29 :
                    dbg.enterAlt(29);

                    // NumericRules.g:101:5: ( VINGT WHITE_SPACE QUATRE )=> VINGT WHITE_SPACE QUATRE
                    {
                    dbg.location(101,35);
                    VINGT87=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1157); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT87);

                    dbg.location(101,41);
                    WHITE_SPACE88=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1159); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE88);

                    dbg.location(101,53);
                    QUATRE89=(Token)match(input,QUATRE,FOLLOW_QUATRE_in_spelled_one_to_thirty_one1161); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUATRE.add(QUATRE89);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 101:61: -> INT[\"24\"]
                    {
                        dbg.location(101,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "24"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 30 :
                    dbg.enterAlt(30);

                    // NumericRules.g:102:5: VINGT ( DASH )? QUATRE
                    {
                    dbg.location(102,5);
                    VINGT90=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1173); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT90);

                    dbg.location(102,11);
                    // NumericRules.g:102:11: ( DASH )?
                    int alt18=2;
                    try { dbg.enterSubRule(18);
                    try { dbg.enterDecision(18);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt18=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(18);}

                    switch (alt18) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:102:11: DASH
                            {
                            dbg.location(102,11);
                            DASH91=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1175); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH91);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(18);}

                    dbg.location(102,17);
                    QUATRE92=(Token)match(input,QUATRE,FOLLOW_QUATRE_in_spelled_one_to_thirty_one1178); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUATRE.add(QUATRE92);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 102:61: -> INT[\"24\"]
                    {
                        dbg.location(102,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "24"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 31 :
                    dbg.enterAlt(31);

                    // NumericRules.g:103:5: ( VINGT WHITE_SPACE CINQ )=> VINGT WHITE_SPACE CINQ
                    {
                    dbg.location(103,35);
                    VINGT93=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1238); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT93);

                    dbg.location(103,41);
                    WHITE_SPACE94=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1240); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE94);

                    dbg.location(103,53);
                    CINQ95=(Token)match(input,CINQ,FOLLOW_CINQ_in_spelled_one_to_thirty_one1242); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CINQ.add(CINQ95);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 103:61: -> INT[\"25\"]
                    {
                        dbg.location(103,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "25"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 32 :
                    dbg.enterAlt(32);

                    // NumericRules.g:104:5: VINGT ( DASH )? CINQ
                    {
                    dbg.location(104,5);
                    VINGT96=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1256); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT96);

                    dbg.location(104,11);
                    // NumericRules.g:104:11: ( DASH )?
                    int alt19=2;
                    try { dbg.enterSubRule(19);
                    try { dbg.enterDecision(19);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt19=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(19);}

                    switch (alt19) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:104:11: DASH
                            {
                            dbg.location(104,11);
                            DASH97=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1258); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH97);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(19);}

                    dbg.location(104,17);
                    CINQ98=(Token)match(input,CINQ,FOLLOW_CINQ_in_spelled_one_to_thirty_one1261); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CINQ.add(CINQ98);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 104:61: -> INT[\"25\"]
                    {
                        dbg.location(104,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "25"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 33 :
                    dbg.enterAlt(33);

                    // NumericRules.g:105:5: ( VINGT WHITE_SPACE SIX )=> VINGT WHITE_SPACE SIX
                    {
                    dbg.location(105,35);
                    VINGT99=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1324); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT99);

                    dbg.location(105,41);
                    WHITE_SPACE100=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1326); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE100);

                    dbg.location(105,53);
                    SIX101=(Token)match(input,SIX,FOLLOW_SIX_in_spelled_one_to_thirty_one1328); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SIX.add(SIX101);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 105:61: -> INT[\"26\"]
                    {
                        dbg.location(105,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "26"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 34 :
                    dbg.enterAlt(34);

                    // NumericRules.g:106:5: VINGT ( DASH )? SIX
                    {
                    dbg.location(106,5);
                    VINGT102=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1343); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT102);

                    dbg.location(106,11);
                    // NumericRules.g:106:11: ( DASH )?
                    int alt20=2;
                    try { dbg.enterSubRule(20);
                    try { dbg.enterDecision(20);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt20=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(20);}

                    switch (alt20) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:106:11: DASH
                            {
                            dbg.location(106,11);
                            DASH103=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1345); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH103);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(20);}

                    dbg.location(106,17);
                    SIX104=(Token)match(input,SIX,FOLLOW_SIX_in_spelled_one_to_thirty_one1348); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SIX.add(SIX104);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 106:61: -> INT[\"26\"]
                    {
                        dbg.location(106,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "26"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 35 :
                    dbg.enterAlt(35);

                    // NumericRules.g:107:5: ( VINGT WHITE_SPACE SEPT )=> VINGT WHITE_SPACE SEPT
                    {
                    dbg.location(107,35);
                    VINGT105=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1411); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT105);

                    dbg.location(107,41);
                    WHITE_SPACE106=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1413); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE106);

                    dbg.location(107,53);
                    SEPT107=(Token)match(input,SEPT,FOLLOW_SEPT_in_spelled_one_to_thirty_one1415); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEPT.add(SEPT107);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 107:61: -> INT[\"27\"]
                    {
                        dbg.location(107,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "27"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 36 :
                    dbg.enterAlt(36);

                    // NumericRules.g:108:5: VINGT ( DASH )? SEPT
                    {
                    dbg.location(108,5);
                    VINGT108=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1429); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT108);

                    dbg.location(108,11);
                    // NumericRules.g:108:11: ( DASH )?
                    int alt21=2;
                    try { dbg.enterSubRule(21);
                    try { dbg.enterDecision(21);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt21=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(21);}

                    switch (alt21) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:108:11: DASH
                            {
                            dbg.location(108,11);
                            DASH109=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1431); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH109);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(21);}

                    dbg.location(108,17);
                    SEPT110=(Token)match(input,SEPT,FOLLOW_SEPT_in_spelled_one_to_thirty_one1434); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEPT.add(SEPT110);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 108:61: -> INT[\"27\"]
                    {
                        dbg.location(108,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "27"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 37 :
                    dbg.enterAlt(37);

                    // NumericRules.g:109:5: ( VINGT WHITE_SPACE HUIT )=> VINGT WHITE_SPACE HUIT
                    {
                    dbg.location(109,35);
                    VINGT111=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1496); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT111);

                    dbg.location(109,41);
                    WHITE_SPACE112=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1498); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE112);

                    dbg.location(109,53);
                    HUIT113=(Token)match(input,HUIT,FOLLOW_HUIT_in_spelled_one_to_thirty_one1500); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_HUIT.add(HUIT113);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 109:61: -> INT[\"28\"]
                    {
                        dbg.location(109,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "28"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 38 :
                    dbg.enterAlt(38);

                    // NumericRules.g:110:5: VINGT ( DASH )? HUIT
                    {
                    dbg.location(110,5);
                    VINGT114=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1514); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT114);

                    dbg.location(110,11);
                    // NumericRules.g:110:11: ( DASH )?
                    int alt22=2;
                    try { dbg.enterSubRule(22);
                    try { dbg.enterDecision(22);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt22=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(22);}

                    switch (alt22) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:110:11: DASH
                            {
                            dbg.location(110,11);
                            DASH115=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1516); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH115);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(22);}

                    dbg.location(110,17);
                    HUIT116=(Token)match(input,HUIT,FOLLOW_HUIT_in_spelled_one_to_thirty_one1519); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_HUIT.add(HUIT116);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 110:61: -> INT[\"28\"]
                    {
                        dbg.location(110,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "28"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 39 :
                    dbg.enterAlt(39);

                    // NumericRules.g:111:5: ( VINGT WHITE_SPACE NEUF )=> VINGT WHITE_SPACE NEUF
                    {
                    dbg.location(111,35);
                    VINGT117=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1581); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT117);

                    dbg.location(111,41);
                    WHITE_SPACE118=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1583); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE118);

                    dbg.location(111,53);
                    NEUF119=(Token)match(input,NEUF,FOLLOW_NEUF_in_spelled_one_to_thirty_one1585); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEUF.add(NEUF119);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 111:61: -> INT[\"29\"]
                    {
                        dbg.location(111,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "29"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 40 :
                    dbg.enterAlt(40);

                    // NumericRules.g:112:5: VINGT ( DASH )? NEUF
                    {
                    dbg.location(112,5);
                    VINGT120=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1599); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT120);

                    dbg.location(112,11);
                    // NumericRules.g:112:11: ( DASH )?
                    int alt23=2;
                    try { dbg.enterSubRule(23);
                    try { dbg.enterDecision(23);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt23=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(23);}

                    switch (alt23) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:112:11: DASH
                            {
                            dbg.location(112,11);
                            DASH121=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1601); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH121);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(23);}

                    dbg.location(112,17);
                    NEUF122=(Token)match(input,NEUF,FOLLOW_NEUF_in_spelled_one_to_thirty_one1604); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEUF.add(NEUF122);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 112:61: -> INT[\"29\"]
                    {
                        dbg.location(112,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "29"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 41 :
                    dbg.enterAlt(41);

                    // NumericRules.g:113:5: VINGT
                    {
                    dbg.location(113,5);
                    VINGT123=(Token)match(input,VINGT,FOLLOW_VINGT_in_spelled_one_to_thirty_one1654); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VINGT.add(VINGT123);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 113:61: -> INT[\"20\"]
                    {
                        dbg.location(113,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "20"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 42 :
                    dbg.enterAlt(42);

                    // NumericRules.g:114:5: ( TRENTE WHITE_SPACE ET WHITE_SPACE UN )=> TRENTE WHITE_SPACE ET WHITE_SPACE UN
                    {
                    dbg.location(114,46);
                    TRENTE124=(Token)match(input,TRENTE,FOLLOW_TRENTE_in_spelled_one_to_thirty_one1728); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TRENTE.add(TRENTE124);

                    dbg.location(114,53);
                    WHITE_SPACE125=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1730); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE125);

                    dbg.location(114,65);
                    ET126=(Token)match(input,ET,FOLLOW_ET_in_spelled_one_to_thirty_one1732); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ET.add(ET126);

                    dbg.location(114,68);
                    WHITE_SPACE127=(Token)match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1734); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHITE_SPACE.add(WHITE_SPACE127);

                    dbg.location(114,80);
                    UN128=(Token)match(input,UN,FOLLOW_UN_in_spelled_one_to_thirty_one1736); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_UN.add(UN128);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 114:84: -> INT[\"31\"]
                    {
                        dbg.location(114,87);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "31"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 43 :
                    dbg.enterAlt(43);

                    // NumericRules.g:115:5: TRENTE ( DASH )? ET ( DASH )? UN
                    {
                    dbg.location(115,5);
                    TRENTE129=(Token)match(input,TRENTE,FOLLOW_TRENTE_in_spelled_one_to_thirty_one1748); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TRENTE.add(TRENTE129);

                    dbg.location(115,12);
                    // NumericRules.g:115:12: ( DASH )?
                    int alt24=2;
                    try { dbg.enterSubRule(24);
                    try { dbg.enterDecision(24);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt24=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(24);}

                    switch (alt24) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:115:12: DASH
                            {
                            dbg.location(115,12);
                            DASH130=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1750); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH130);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(24);}

                    dbg.location(115,18);
                    ET131=(Token)match(input,ET,FOLLOW_ET_in_spelled_one_to_thirty_one1753); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ET.add(ET131);

                    dbg.location(115,21);
                    // NumericRules.g:115:21: ( DASH )?
                    int alt25=2;
                    try { dbg.enterSubRule(25);
                    try { dbg.enterDecision(25);

                    switch ( input.LA(1) ) {
                        case DASH:
                            {
                            alt25=1;
                            }
                            break;
                    }

                    } finally {dbg.exitDecision(25);}

                    switch (alt25) {
                        case 1 :
                            dbg.enterAlt(1);

                            // NumericRules.g:115:21: DASH
                            {
                            dbg.location(115,21);
                            DASH132=(Token)match(input,DASH,FOLLOW_DASH_in_spelled_one_to_thirty_one1755); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_DASH.add(DASH132);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(25);}

                    dbg.location(115,27);
                    UN133=(Token)match(input,UN,FOLLOW_UN_in_spelled_one_to_thirty_one1758); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_UN.add(UN133);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 115:84: -> INT[\"31\"]
                    {
                        dbg.location(115,87);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "31"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 44 :
                    dbg.enterAlt(44);

                    // NumericRules.g:116:5: TRENTE
                    {
                    dbg.location(116,5);
                    TRENTE134=(Token)match(input,TRENTE,FOLLOW_TRENTE_in_spelled_one_to_thirty_one1823); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TRENTE.add(TRENTE134);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 116:61: -> INT[\"30\"]
                    {
                        dbg.location(116,64);
                        adaptor.addChild(root_0, (Object)adaptor.create(INT, "30"));

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(117, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "spelled_one_to_thirty_one");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "spelled_one_to_thirty_one"

    public static class int_60_to_99_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_60_to_99"
    // NumericRules.g:155:1: int_60_to_99 : ( INT_60 | INT_61 | INT_62 | INT_63 | INT_64 | INT_65 | INT_66 | INT_67 | INT_68 | INT_69 | INT_70 | INT_71 | INT_72 | INT_73 | INT_74 | INT_75 | INT_76 | INT_77 | INT_78 | INT_79 | INT_80 | INT_81 | INT_82 | INT_83 | INT_84 | INT_85 | INT_86 | INT_87 | INT_88 | INT_89 | INT_90 | INT_91 | INT_92 | INT_93 | INT_94 | INT_95 | INT_96 | INT_97 | INT_98 | INT_99 );
    public final DateParser_NumericRules.int_60_to_99_return int_60_to_99() throws RecognitionException {
        DateParser_NumericRules.int_60_to_99_return retval = new DateParser_NumericRules.int_60_to_99_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set135=null;

        Object set135_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "int_60_to_99");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(155, 1);

        try {
            // NumericRules.g:156:3: ( INT_60 | INT_61 | INT_62 | INT_63 | INT_64 | INT_65 | INT_66 | INT_67 | INT_68 | INT_69 | INT_70 | INT_71 | INT_72 | INT_73 | INT_74 | INT_75 | INT_76 | INT_77 | INT_78 | INT_79 | INT_80 | INT_81 | INT_82 | INT_83 | INT_84 | INT_85 | INT_86 | INT_87 | INT_88 | INT_89 | INT_90 | INT_91 | INT_92 | INT_93 | INT_94 | INT_95 | INT_96 | INT_97 | INT_98 | INT_99 )
            dbg.enterAlt(1);

            // NumericRules.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(156,3);
            set135=(Token)input.LT(1);
            if ( (input.LA(1)>=INT_60 && input.LA(1)<=INT_99) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set135));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(161, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_60_to_99");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_60_to_99"

    public static class int_32_to_59_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_32_to_59"
    // NumericRules.g:163:1: int_32_to_59 : ( INT_32 | INT_33 | INT_34 | INT_35 | INT_36 | INT_37 | INT_38 | INT_39 | INT_40 | INT_41 | INT_42 | INT_43 | INT_44 | INT_45 | INT_46 | INT_47 | INT_48 | INT_49 | INT_50 | INT_51 | INT_52 | INT_53 | INT_54 | INT_55 | INT_56 | INT_57 | INT_58 | INT_59 );
    public final DateParser_NumericRules.int_32_to_59_return int_32_to_59() throws RecognitionException {
        DateParser_NumericRules.int_32_to_59_return retval = new DateParser_NumericRules.int_32_to_59_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set136=null;

        Object set136_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "int_32_to_59");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(163, 1);

        try {
            // NumericRules.g:164:3: ( INT_32 | INT_33 | INT_34 | INT_35 | INT_36 | INT_37 | INT_38 | INT_39 | INT_40 | INT_41 | INT_42 | INT_43 | INT_44 | INT_45 | INT_46 | INT_47 | INT_48 | INT_49 | INT_50 | INT_51 | INT_52 | INT_53 | INT_54 | INT_55 | INT_56 | INT_57 | INT_58 | INT_59 )
            dbg.enterAlt(1);

            // NumericRules.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(164,3);
            set136=(Token)input.LT(1);
            if ( (input.LA(1)>=INT_32 && input.LA(1)<=INT_59) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set136));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(168, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_32_to_59");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_32_to_59"

    public static class int_24_to_31_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_24_to_31"
    // NumericRules.g:170:1: int_24_to_31 : ( INT_24 | INT_25 | INT_26 | INT_27 | INT_28 | INT_29 | INT_30 | INT_31 );
    public final DateParser_NumericRules.int_24_to_31_return int_24_to_31() throws RecognitionException {
        DateParser_NumericRules.int_24_to_31_return retval = new DateParser_NumericRules.int_24_to_31_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set137=null;

        Object set137_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "int_24_to_31");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(170, 1);

        try {
            // NumericRules.g:171:3: ( INT_24 | INT_25 | INT_26 | INT_27 | INT_28 | INT_29 | INT_30 | INT_31 )
            dbg.enterAlt(1);

            // NumericRules.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(171,3);
            set137=(Token)input.LT(1);
            if ( (input.LA(1)>=INT_24 && input.LA(1)<=INT_31) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set137));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(172, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_24_to_31");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_24_to_31"

    public static class int_13_to_23_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_13_to_23"
    // NumericRules.g:174:1: int_13_to_23 : ( INT_13 | INT_14 | INT_15 | INT_16 | INT_17 | INT_18 | INT_19 | INT_20 | INT_21 | INT_22 | INT_23 );
    public final DateParser_NumericRules.int_13_to_23_return int_13_to_23() throws RecognitionException {
        DateParser_NumericRules.int_13_to_23_return retval = new DateParser_NumericRules.int_13_to_23_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set138=null;

        Object set138_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "int_13_to_23");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(174, 1);

        try {
            // NumericRules.g:175:3: ( INT_13 | INT_14 | INT_15 | INT_16 | INT_17 | INT_18 | INT_19 | INT_20 | INT_21 | INT_22 | INT_23 )
            dbg.enterAlt(1);

            // NumericRules.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(175,3);
            set138=(Token)input.LT(1);
            if ( (input.LA(1)>=INT_13 && input.LA(1)<=INT_23) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set138));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(177, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_13_to_23");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_13_to_23"

    public static class int_01_to_12_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_01_to_12"
    // NumericRules.g:179:1: int_01_to_12 : ( INT_01 | INT_02 | INT_03 | INT_04 | INT_05 | INT_06 | INT_07 | INT_08 | INT_09 | INT_10 | INT_11 | INT_12 );
    public final DateParser_NumericRules.int_01_to_12_return int_01_to_12() throws RecognitionException {
        DateParser_NumericRules.int_01_to_12_return retval = new DateParser_NumericRules.int_01_to_12_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set139=null;

        Object set139_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "int_01_to_12");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(179, 1);

        try {
            // NumericRules.g:180:3: ( INT_01 | INT_02 | INT_03 | INT_04 | INT_05 | INT_06 | INT_07 | INT_08 | INT_09 | INT_10 | INT_11 | INT_12 )
            dbg.enterAlt(1);

            // NumericRules.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(180,3);
            set139=(Token)input.LT(1);
            if ( (input.LA(1)>=INT_01 && input.LA(1)<=INT_09)||(input.LA(1)>=INT_10 && input.LA(1)<=INT_12) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set139));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(182, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_01_to_12");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_01_to_12"

    public static class int_1_to_9_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_1_to_9"
    // NumericRules.g:184:1: int_1_to_9 : ( INT_1 | INT_2 | INT_3 | INT_4 | INT_5 | INT_6 | INT_7 | INT_8 | INT_9 );
    public final DateParser_NumericRules.int_1_to_9_return int_1_to_9() throws RecognitionException {
        DateParser_NumericRules.int_1_to_9_return retval = new DateParser_NumericRules.int_1_to_9_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set140=null;

        Object set140_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "int_1_to_9");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(184, 1);

        try {
            // NumericRules.g:185:3: ( INT_1 | INT_2 | INT_3 | INT_4 | INT_5 | INT_6 | INT_7 | INT_8 | INT_9 )
            dbg.enterAlt(1);

            // NumericRules.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(185,3);
            set140=(Token)input.LT(1);
            if ( (input.LA(1)>=INT_1 && input.LA(1)<=INT_9) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set140));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(186, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_1_to_9");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_1_to_9"

    public static class int_1_to_5_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_1_to_5"
    // NumericRules.g:188:1: int_1_to_5 : ( INT_1 | INT_2 | INT_3 | INT_4 | INT_5 );
    public final DateParser_NumericRules.int_1_to_5_return int_1_to_5() throws RecognitionException {
        DateParser_NumericRules.int_1_to_5_return retval = new DateParser_NumericRules.int_1_to_5_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set141=null;

        Object set141_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "int_1_to_5");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(188, 1);

        try {
            // NumericRules.g:189:3: ( INT_1 | INT_2 | INT_3 | INT_4 | INT_5 )
            dbg.enterAlt(1);

            // NumericRules.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(189,3);
            set141=(Token)input.LT(1);
            if ( (input.LA(1)>=INT_1 && input.LA(1)<=INT_5) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set141));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(190, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "int_1_to_5");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "int_1_to_5"

    // $ANTLR start synpred1_NumericRules
    public final void synpred1_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:88:5: ( DIX WHITE_SPACE SEPT )
        dbg.enterAlt(1);

        // NumericRules.g:88:6: DIX WHITE_SPACE SEPT
        {
        dbg.location(88,6);
        match(input,DIX,FOLLOW_DIX_in_synpred1_NumericRules596); if (state.failed) return ;
        dbg.location(88,10);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred1_NumericRules598); if (state.failed) return ;
        dbg.location(88,22);
        match(input,SEPT,FOLLOW_SEPT_in_synpred1_NumericRules600); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_NumericRules

    // $ANTLR start synpred2_NumericRules
    public final void synpred2_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:90:5: ( DIX WHITE_SPACE HUIT )
        dbg.enterAlt(1);

        // NumericRules.g:90:6: DIX WHITE_SPACE HUIT
        {
        dbg.location(90,6);
        match(input,DIX,FOLLOW_DIX_in_synpred2_NumericRules671); if (state.failed) return ;
        dbg.location(90,10);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred2_NumericRules673); if (state.failed) return ;
        dbg.location(90,22);
        match(input,HUIT,FOLLOW_HUIT_in_synpred2_NumericRules675); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_NumericRules

    // $ANTLR start synpred3_NumericRules
    public final void synpred3_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:92:5: ( DIX WHITE_SPACE NEUF )
        dbg.enterAlt(1);

        // NumericRules.g:92:6: DIX WHITE_SPACE NEUF
        {
        dbg.location(92,6);
        match(input,DIX,FOLLOW_DIX_in_synpred3_NumericRules746); if (state.failed) return ;
        dbg.location(92,10);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred3_NumericRules748); if (state.failed) return ;
        dbg.location(92,22);
        match(input,NEUF,FOLLOW_NEUF_in_synpred3_NumericRules750); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred3_NumericRules

    // $ANTLR start synpred4_NumericRules
    public final void synpred4_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:95:5: ( VINGT WHITE_SPACE ET WHITE_SPACE UN )
        dbg.enterAlt(1);

        // NumericRules.g:95:6: VINGT WHITE_SPACE ET WHITE_SPACE UN
        {
        dbg.location(95,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred4_NumericRules876); if (state.failed) return ;
        dbg.location(95,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred4_NumericRules878); if (state.failed) return ;
        dbg.location(95,24);
        match(input,ET,FOLLOW_ET_in_synpred4_NumericRules880); if (state.failed) return ;
        dbg.location(95,27);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred4_NumericRules882); if (state.failed) return ;
        dbg.location(95,39);
        match(input,UN,FOLLOW_UN_in_synpred4_NumericRules884); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred4_NumericRules

    // $ANTLR start synpred5_NumericRules
    public final void synpred5_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:97:5: ( VINGT WHITE_SPACE DEUX )
        dbg.enterAlt(1);

        // NumericRules.g:97:6: VINGT WHITE_SPACE DEUX
        {
        dbg.location(97,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred5_NumericRules981); if (state.failed) return ;
        dbg.location(97,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred5_NumericRules983); if (state.failed) return ;
        dbg.location(97,24);
        match(input,DEUX,FOLLOW_DEUX_in_synpred5_NumericRules985); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred5_NumericRules

    // $ANTLR start synpred6_NumericRules
    public final void synpred6_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:99:5: ( VINGT WHITE_SPACE TROIS )
        dbg.enterAlt(1);

        // NumericRules.g:99:6: VINGT WHITE_SPACE TROIS
        {
        dbg.location(99,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred6_NumericRules1066); if (state.failed) return ;
        dbg.location(99,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred6_NumericRules1068); if (state.failed) return ;
        dbg.location(99,24);
        match(input,TROIS,FOLLOW_TROIS_in_synpred6_NumericRules1070); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred6_NumericRules

    // $ANTLR start synpred7_NumericRules
    public final void synpred7_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:101:5: ( VINGT WHITE_SPACE QUATRE )
        dbg.enterAlt(1);

        // NumericRules.g:101:6: VINGT WHITE_SPACE QUATRE
        {
        dbg.location(101,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred7_NumericRules1148); if (state.failed) return ;
        dbg.location(101,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred7_NumericRules1150); if (state.failed) return ;
        dbg.location(101,24);
        match(input,QUATRE,FOLLOW_QUATRE_in_synpred7_NumericRules1152); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred7_NumericRules

    // $ANTLR start synpred8_NumericRules
    public final void synpred8_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:103:5: ( VINGT WHITE_SPACE CINQ )
        dbg.enterAlt(1);

        // NumericRules.g:103:6: VINGT WHITE_SPACE CINQ
        {
        dbg.location(103,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred8_NumericRules1227); if (state.failed) return ;
        dbg.location(103,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred8_NumericRules1229); if (state.failed) return ;
        dbg.location(103,24);
        match(input,CINQ,FOLLOW_CINQ_in_synpred8_NumericRules1231); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred8_NumericRules

    // $ANTLR start synpred9_NumericRules
    public final void synpred9_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:105:5: ( VINGT WHITE_SPACE SIX )
        dbg.enterAlt(1);

        // NumericRules.g:105:6: VINGT WHITE_SPACE SIX
        {
        dbg.location(105,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred9_NumericRules1312); if (state.failed) return ;
        dbg.location(105,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred9_NumericRules1314); if (state.failed) return ;
        dbg.location(105,24);
        match(input,SIX,FOLLOW_SIX_in_synpred9_NumericRules1316); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred9_NumericRules

    // $ANTLR start synpred10_NumericRules
    public final void synpred10_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:107:5: ( VINGT WHITE_SPACE SEPT )
        dbg.enterAlt(1);

        // NumericRules.g:107:6: VINGT WHITE_SPACE SEPT
        {
        dbg.location(107,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred10_NumericRules1400); if (state.failed) return ;
        dbg.location(107,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred10_NumericRules1402); if (state.failed) return ;
        dbg.location(107,24);
        match(input,SEPT,FOLLOW_SEPT_in_synpred10_NumericRules1404); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred10_NumericRules

    // $ANTLR start synpred11_NumericRules
    public final void synpred11_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:109:5: ( VINGT WHITE_SPACE HUIT )
        dbg.enterAlt(1);

        // NumericRules.g:109:6: VINGT WHITE_SPACE HUIT
        {
        dbg.location(109,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred11_NumericRules1485); if (state.failed) return ;
        dbg.location(109,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred11_NumericRules1487); if (state.failed) return ;
        dbg.location(109,24);
        match(input,HUIT,FOLLOW_HUIT_in_synpred11_NumericRules1489); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred11_NumericRules

    // $ANTLR start synpred12_NumericRules
    public final void synpred12_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:111:5: ( VINGT WHITE_SPACE NEUF )
        dbg.enterAlt(1);

        // NumericRules.g:111:6: VINGT WHITE_SPACE NEUF
        {
        dbg.location(111,6);
        match(input,VINGT,FOLLOW_VINGT_in_synpred12_NumericRules1570); if (state.failed) return ;
        dbg.location(111,12);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred12_NumericRules1572); if (state.failed) return ;
        dbg.location(111,24);
        match(input,NEUF,FOLLOW_NEUF_in_synpred12_NumericRules1574); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_NumericRules

    // $ANTLR start synpred13_NumericRules
    public final void synpred13_NumericRules_fragment() throws RecognitionException {   
        // NumericRules.g:114:5: ( TRENTE WHITE_SPACE ET WHITE_SPACE UN )
        dbg.enterAlt(1);

        // NumericRules.g:114:6: TRENTE WHITE_SPACE ET WHITE_SPACE UN
        {
        dbg.location(114,6);
        match(input,TRENTE,FOLLOW_TRENTE_in_synpred13_NumericRules1716); if (state.failed) return ;
        dbg.location(114,13);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred13_NumericRules1718); if (state.failed) return ;
        dbg.location(114,25);
        match(input,ET,FOLLOW_ET_in_synpred13_NumericRules1720); if (state.failed) return ;
        dbg.location(114,28);
        match(input,WHITE_SPACE,FOLLOW_WHITE_SPACE_in_synpred13_NumericRules1722); if (state.failed) return ;
        dbg.location(114,40);
        match(input,UN,FOLLOW_UN_in_synpred13_NumericRules1724); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred13_NumericRules

    // Delegated rules

    public final boolean synpred5_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred5_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred8_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred13_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred13_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred1_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred2_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred6_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred9_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred4_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred12_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred3_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred7_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred11_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred11_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_NumericRules() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred10_NumericRules_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA26 dfa26 = new DFA26(this);
    static final String DFA26_eotS =
        "\106\uffff";
    static final String DFA26_eofS =
        "\20\uffff\1\30\1\42\1\46\1\30\5\uffff\1\42\13\uffff\1\46\40\uffff";
    static final String DFA26_minS =
        "\1\u00b5\17\uffff\4\5\1\u00bb\4\uffff\1\5\1\66\12\uffff\1\5\2\uffff"+
        "\3\0\1\76\10\0\1\76\3\uffff\1\5\10\uffff\1\5\2\0\2\uffff";
    static final String DFA26_maxS =
        "\1\u00c7\17\uffff\2\u00bd\1\104\1\u017b\1\u00bd\4\uffff\1\u017b"+
        "\1\u00bd\12\uffff\1\u017b\2\uffff\3\0\1\76\10\0\1\76\3\uffff\1\u017b"+
        "\10\uffff\1\u017b\2\0\2\uffff";
    static final String DFA26_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
        "\15\1\16\1\17\5\uffff\1\25\1\21\1\23\1\26\2\uffff\1\34\1\40\1\44"+
        "\1\30\1\32\1\50\1\36\1\51\1\46\1\42\1\uffff\1\54\1\53\15\uffff\1"+
        "\20\1\22\1\24\1\uffff\1\31\1\33\1\35\1\37\1\41\1\43\1\45\1\47\3"+
        "\uffff\1\27\1\52";
    static final String DFA26_specialS =
        "\50\uffff\1\10\1\14\1\12\1\uffff\1\11\1\5\1\1\1\4\1\3\1\2\1\0\1"+
        "\7\16\uffff\1\13\1\6\2\uffff}>";
    static final String[] DFA26_transitionS = {
            "\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\20\1\12\1\13\1\14\1"+
            "\15\1\16\1\17\1\21\1\22\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\14\30\24\uffff\1\24\30\uffff\1\23\5\uffff\1\30\166\uffff\1"+
            "\26\1\27\1\25",
            "\14\42\24\uffff\1\32\20\uffff\1\36\7\uffff\1\31\5\uffff\1\42"+
            "\161\uffff\1\37\1\33\1\41\1\34\1\44\1\35\1\43\1\40",
            "\14\46\24\uffff\1\47\20\uffff\1\47\7\uffff\1\45\5\uffff\1\46",
            "\24\30\1\uffff\5\30\6\uffff\1\30\2\uffff\2\30\1\uffff\6\30"+
            "\1\uffff\1\30\2\uffff\6\30\2\uffff\1\30\11\uffff\164\30\1\50"+
            "\1\51\1\52\12\30\u00b3\uffff\1\30",
            "\1\26\1\27\1\25",
            "",
            "",
            "",
            "",
            "\24\42\1\uffff\5\42\6\uffff\1\42\2\uffff\2\42\1\uffff\6\42"+
            "\1\uffff\1\42\2\uffff\1\42\1\53\4\42\2\uffff\1\42\11\uffff\157"+
            "\42\1\54\1\55\1\56\1\57\1\60\1\61\1\62\1\63\12\42\u00b3\uffff"+
            "\1\42",
            "\1\36\177\uffff\1\37\1\33\1\41\1\34\1\44\1\35\1\43\1\40",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\24\46\1\uffff\5\46\6\uffff\1\46\2\uffff\2\46\1\uffff\6\46"+
            "\1\uffff\1\46\2\uffff\1\46\1\64\4\46\2\uffff\1\46\11\uffff\u0081"+
            "\46\u00b3\uffff\1\46",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\70",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\101",
            "",
            "",
            "",
            "\24\42\1\uffff\2\42\4\uffff\5\42\4\uffff\1\42\1\uffff\6\42"+
            "\1\uffff\3\42\2\uffff\1\42\3\uffff\4\42\1\uffff\3\42\1\uffff"+
            "\1\42\2\uffff\156\42\1\102\26\42\u00af\uffff\1\42",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\24\46\1\uffff\2\46\4\uffff\5\46\4\uffff\1\46\1\uffff\6\46"+
            "\1\uffff\3\46\2\uffff\1\46\3\uffff\4\46\1\uffff\3\46\1\uffff"+
            "\1\46\2\uffff\156\46\1\103\26\46\u00af\uffff\1\46",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA26_eot = DFA.unpackEncodedString(DFA26_eotS);
    static final short[] DFA26_eof = DFA.unpackEncodedString(DFA26_eofS);
    static final char[] DFA26_min = DFA.unpackEncodedStringToUnsignedChars(DFA26_minS);
    static final char[] DFA26_max = DFA.unpackEncodedStringToUnsignedChars(DFA26_maxS);
    static final short[] DFA26_accept = DFA.unpackEncodedString(DFA26_acceptS);
    static final short[] DFA26_special = DFA.unpackEncodedString(DFA26_specialS);
    static final short[][] DFA26_transition;

    static {
        int numStates = DFA26_transitionS.length;
        DFA26_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA26_transition[i] = DFA.unpackEncodedString(DFA26_transitionS[i]);
        }
    }

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = DFA26_eot;
            this.eof = DFA26_eof;
            this.min = DFA26_min;
            this.max = DFA26_max;
            this.accept = DFA26_accept;
            this.special = DFA26_special;
            this.transition = DFA26_transition;
        }
        public String getDescription() {
            return "72:1: spelled_one_to_thirty_one : ( ( UN | PREMIER ) -> INT[\"1\"] | DEUX -> INT[\"2\"] | TROIS -> INT[\"3\"] | QUATRE -> INT[\"4\"] | CINQ -> INT[\"5\"] | SIX -> INT[\"6\"] | SEPT -> INT[\"7\"] | HUIT -> INT[\"8\"] | NEUF -> INT[\"9\"] | ONZE -> INT[\"11\"] | DOUZE -> INT[\"12\"] | TREIZE -> INT[\"13\"] | QUATORZE -> INT[\"14\"] | QUINZE -> INT[\"15\"] | SEIZE -> INT[\"16\"] | ( DIX WHITE_SPACE SEPT )=> DIX WHITE_SPACE SEPT -> INT[\"17\"] | DIX ( DASH )? SEPT -> INT[\"17\"] | ( DIX WHITE_SPACE HUIT )=> DIX WHITE_SPACE HUIT -> INT[\"18\"] | DIX ( DASH )? HUIT -> INT[\"18\"] | ( DIX WHITE_SPACE NEUF )=> DIX WHITE_SPACE NEUF -> INT[\"19\"] | DIX ( DASH )? NEUF -> INT[\"19\"] | DIX -> INT[\"10\"] | ( VINGT WHITE_SPACE ET WHITE_SPACE UN )=> VINGT WHITE_SPACE ET WHITE_SPACE UN -> INT[\"21\"] | VINGT ( DASH )? ET ( DASH )? UN -> INT[\"21\"] | ( VINGT WHITE_SPACE DEUX )=> VINGT WHITE_SPACE DEUX -> INT[\"22\"] | VINGT ( DASH )? DEUX -> INT[\"22\"] | ( VINGT WHITE_SPACE TROIS )=> VINGT WHITE_SPACE TROIS -> INT[\"23\"] | VINGT ( DASH )? TROIS -> INT[\"23\"] | ( VINGT WHITE_SPACE QUATRE )=> VINGT WHITE_SPACE QUATRE -> INT[\"24\"] | VINGT ( DASH )? QUATRE -> INT[\"24\"] | ( VINGT WHITE_SPACE CINQ )=> VINGT WHITE_SPACE CINQ -> INT[\"25\"] | VINGT ( DASH )? CINQ -> INT[\"25\"] | ( VINGT WHITE_SPACE SIX )=> VINGT WHITE_SPACE SIX -> INT[\"26\"] | VINGT ( DASH )? SIX -> INT[\"26\"] | ( VINGT WHITE_SPACE SEPT )=> VINGT WHITE_SPACE SEPT -> INT[\"27\"] | VINGT ( DASH )? SEPT -> INT[\"27\"] | ( VINGT WHITE_SPACE HUIT )=> VINGT WHITE_SPACE HUIT -> INT[\"28\"] | VINGT ( DASH )? HUIT -> INT[\"28\"] | ( VINGT WHITE_SPACE NEUF )=> VINGT WHITE_SPACE NEUF -> INT[\"29\"] | VINGT ( DASH )? NEUF -> INT[\"29\"] | VINGT -> INT[\"20\"] | ( TRENTE WHITE_SPACE ET WHITE_SPACE UN )=> TRENTE WHITE_SPACE ET WHITE_SPACE UN -> INT[\"31\"] | TRENTE ( DASH )? ET ( DASH )? UN -> INT[\"31\"] | TRENTE -> INT[\"30\"] );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA26_50 = input.LA(1);

                         
                        int index26_50 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_NumericRules()) ) {s = 63;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_50);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA26_46 = input.LA(1);

                         
                        int index26_46 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_NumericRules()) ) {s = 59;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_46);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA26_49 = input.LA(1);

                         
                        int index26_49 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_NumericRules()) ) {s = 62;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_49);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA26_48 = input.LA(1);

                         
                        int index26_48 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred9_NumericRules()) ) {s = 61;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_48);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA26_47 = input.LA(1);

                         
                        int index26_47 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred8_NumericRules()) ) {s = 60;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_47);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA26_45 = input.LA(1);

                         
                        int index26_45 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred6_NumericRules()) ) {s = 58;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_45);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA26_67 = input.LA(1);

                         
                        int index26_67 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred13_NumericRules()) ) {s = 69;}

                        else if ( (true) ) {s = 38;}

                         
                        input.seek(index26_67);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA26_51 = input.LA(1);

                         
                        int index26_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_NumericRules()) ) {s = 64;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_51);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA26_40 = input.LA(1);

                         
                        int index26_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_NumericRules()) ) {s = 53;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index26_40);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA26_44 = input.LA(1);

                         
                        int index26_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_NumericRules()) ) {s = 57;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_44);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA26_42 = input.LA(1);

                         
                        int index26_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred3_NumericRules()) ) {s = 55;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index26_42);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA26_66 = input.LA(1);

                         
                        int index26_66 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_NumericRules()) ) {s = 68;}

                        else if ( (true) ) {s = 34;}

                         
                        input.seek(index26_66);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA26_41 = input.LA(1);

                         
                        int index26_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_NumericRules()) ) {s = 54;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index26_41);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 26, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_INT_00_in_int_00_to_59_mandatory_prefix42 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_01_to_12_in_int_00_to_59_mandatory_prefix48 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_13_to_23_in_int_00_to_59_mandatory_prefix54 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_24_to_31_in_int_00_to_59_mandatory_prefix60 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_32_to_59_in_int_00_to_59_mandatory_prefix66 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_00_to_59_mandatory_prefix_in_int_00_to_99_mandatory_prefix89 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_60_to_99_in_int_00_to_99_mandatory_prefix93 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_1_to_9_in_int_01_to_12_optional_prefix120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_01_to_12_in_int_01_to_12_optional_prefix124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_00_in_int_00_to_23_optional_prefix147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_0_in_int_00_to_23_optional_prefix154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_1_to_9_in_int_00_to_23_optional_prefix160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_01_to_12_in_int_00_to_23_optional_prefix166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_13_to_23_in_int_00_to_23_optional_prefix172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_01_to_12_in_int_01_to_31_optional_prefix195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_1_to_9_in_int_01_to_31_optional_prefix201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_13_to_23_in_int_01_to_31_optional_prefix207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_24_to_31_in_int_01_to_31_optional_prefix213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_00_to_99_mandatory_prefix_in_int_four_digits235 = new BitSet(new long[]{0x0000000000000000L,0xFFFFFFFFF801FF80L,0x001FFFFFFFFFFFFFL});
    public static final BitSet FOLLOW_int_00_to_99_mandatory_prefix_in_int_four_digits237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_01_to_31_optional_prefix_in_spelled_or_int_01_to_31_optional_prefix264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_spelled_one_to_thirty_one_in_spelled_or_int_01_to_31_optional_prefix270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_spelled_one_to_thirty_one_in_spelled_or_int_optional_prefix286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_01_to_31_optional_prefix_in_spelled_or_int_optional_prefix295 = new BitSet(new long[]{0x0000000000000002L,0xFFFFFFFFFFFDFF80L,0x001FFFFFFFFFFFFFL});
    public static final BitSet FOLLOW_int_32_to_59_in_spelled_or_int_optional_prefix299 = new BitSet(new long[]{0x0000000000000002L,0xFFFFFFFFFFFDFF80L,0x001FFFFFFFFFFFFFL});
    public static final BitSet FOLLOW_int_60_to_99_in_spelled_or_int_optional_prefix303 = new BitSet(new long[]{0x0000000000000002L,0xFFFFFFFFFFFDFF80L,0x001FFFFFFFFFFFFFL});
    public static final BitSet FOLLOW_int_01_to_31_optional_prefix_in_spelled_or_int_optional_prefix312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_32_to_59_in_spelled_or_int_optional_prefix316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_60_to_99_in_spelled_or_int_optional_prefix320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UN_in_spelled_one_to_thirty_one354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PREMIER_in_spelled_one_to_thirty_one358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEUX_in_spelled_one_to_thirty_one376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TROIS_in_spelled_one_to_thirty_one394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUATRE_in_spelled_one_to_thirty_one410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CINQ_in_spelled_one_to_thirty_one427 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIX_in_spelled_one_to_thirty_one444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEPT_in_spelled_one_to_thirty_one462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HUIT_in_spelled_one_to_thirty_one478 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEUF_in_spelled_one_to_thirty_one494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ONZE_in_spelled_one_to_thirty_one511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUZE_in_spelled_one_to_thirty_one526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TREIZE_in_spelled_one_to_thirty_one541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUATORZE_in_spelled_one_to_thirty_one554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUINZE_in_spelled_one_to_thirty_one567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEIZE_in_spelled_one_to_thirty_one581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_spelled_one_to_thirty_one604 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one606 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_SEPT_in_spelled_one_to_thirty_one608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_spelled_one_to_thirty_one621 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one623 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_SEPT_in_spelled_one_to_thirty_one626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_spelled_one_to_thirty_one679 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one681 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_HUIT_in_spelled_one_to_thirty_one683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_spelled_one_to_thirty_one696 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one698 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_HUIT_in_spelled_one_to_thirty_one701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_spelled_one_to_thirty_one754 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one756 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_NEUF_in_spelled_one_to_thirty_one758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_spelled_one_to_thirty_one771 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one773 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_NEUF_in_spelled_one_to_thirty_one776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_spelled_one_to_thirty_one820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one888 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one890 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_ET_in_spelled_one_to_thirty_one892 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one894 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_UN_in_spelled_one_to_thirty_one896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one907 = new BitSet(new long[]{0x0040002000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one909 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_ET_in_spelled_one_to_thirty_one912 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one914 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_UN_in_spelled_one_to_thirty_one917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one992 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one994 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_DEUX_in_spelled_one_to_thirty_one996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1010 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1012 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_DEUX_in_spelled_one_to_thirty_one1015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1076 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1078 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_TROIS_in_spelled_one_to_thirty_one1080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1093 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1095 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_TROIS_in_spelled_one_to_thirty_one1098 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1157 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1159 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_QUATRE_in_spelled_one_to_thirty_one1161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1173 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1175 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_QUATRE_in_spelled_one_to_thirty_one1178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1238 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1240 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_CINQ_in_spelled_one_to_thirty_one1242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1256 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1258 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_CINQ_in_spelled_one_to_thirty_one1261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1324 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1326 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_SIX_in_spelled_one_to_thirty_one1328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1343 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1345 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_SIX_in_spelled_one_to_thirty_one1348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1411 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1413 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_SEPT_in_spelled_one_to_thirty_one1415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1429 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1431 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_SEPT_in_spelled_one_to_thirty_one1434 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1496 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1498 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_HUIT_in_spelled_one_to_thirty_one1500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1514 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1516 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_HUIT_in_spelled_one_to_thirty_one1519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1581 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1583 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_NEUF_in_spelled_one_to_thirty_one1585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1599 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1601 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_NEUF_in_spelled_one_to_thirty_one1604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_spelled_one_to_thirty_one1654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRENTE_in_spelled_one_to_thirty_one1728 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1730 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_ET_in_spelled_one_to_thirty_one1732 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_spelled_one_to_thirty_one1734 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_UN_in_spelled_one_to_thirty_one1736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRENTE_in_spelled_one_to_thirty_one1748 = new BitSet(new long[]{0x0040002000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1750 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_ET_in_spelled_one_to_thirty_one1753 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_DASH_in_spelled_one_to_thirty_one1755 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_UN_in_spelled_one_to_thirty_one1758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRENTE_in_spelled_one_to_thirty_one1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_int_60_to_990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_int_32_to_590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_int_24_to_310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_int_13_to_230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_int_01_to_120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_int_1_to_90 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_int_1_to_50 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_synpred1_NumericRules596 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred1_NumericRules598 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_SEPT_in_synpred1_NumericRules600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_synpred2_NumericRules671 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred2_NumericRules673 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_HUIT_in_synpred2_NumericRules675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIX_in_synpred3_NumericRules746 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred3_NumericRules748 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_NEUF_in_synpred3_NumericRules750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred4_NumericRules876 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred4_NumericRules878 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_ET_in_synpred4_NumericRules880 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred4_NumericRules882 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_UN_in_synpred4_NumericRules884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred5_NumericRules981 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred5_NumericRules983 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_DEUX_in_synpred5_NumericRules985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred6_NumericRules1066 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred6_NumericRules1068 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_TROIS_in_synpred6_NumericRules1070 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred7_NumericRules1148 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred7_NumericRules1150 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_QUATRE_in_synpred7_NumericRules1152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred8_NumericRules1227 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred8_NumericRules1229 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_CINQ_in_synpred8_NumericRules1231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred9_NumericRules1312 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred9_NumericRules1314 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0400000000000000L});
    public static final BitSet FOLLOW_SIX_in_synpred9_NumericRules1316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred10_NumericRules1400 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred10_NumericRules1402 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0800000000000000L});
    public static final BitSet FOLLOW_SEPT_in_synpred10_NumericRules1404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred11_NumericRules1485 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred11_NumericRules1487 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x1000000000000000L});
    public static final BitSet FOLLOW_HUIT_in_synpred11_NumericRules1489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VINGT_in_synpred12_NumericRules1570 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred12_NumericRules1572 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_NEUF_in_synpred12_NumericRules1574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRENTE_in_synpred13_NumericRules1716 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred13_NumericRules1718 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_ET_in_synpred13_NumericRules1720 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WHITE_SPACE_in_synpred13_NumericRules1722 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_UN_in_synpred13_NumericRules1724 = new BitSet(new long[]{0x0000000000000002L});

}