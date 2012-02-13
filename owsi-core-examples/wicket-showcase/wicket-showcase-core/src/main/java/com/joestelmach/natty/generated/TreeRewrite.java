// $ANTLR 3.2 Sep 23, 2009 14:05:07 com/joestelmach/natty/generated/TreeRewrite.g 2012-02-13 15:13:28
 package com.joestelmach.natty.generated; 

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteRuleNodeStream;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeRewriter;
import org.antlr.runtime.tree.TreeRuleReturnScope;

@SuppressWarnings("unused")
public class TreeRewrite extends TreeRewriter {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DOT", "JANVIER", "FEVRIER", "MARS", "AVRIL", "MAI", "JUIN", "JUILLET", "AOUT", "SEPTEMBRE", "OCTOBRE", "NOVEMBRE", "DECEMBRE", "DIMANCHE", "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "HEURE", "HEURE_SHORT", "MINUTE", "JOUR", "SEMAINE", "MOIS", "ANNEE", "SINGLE_QUOTE", "AUJOURD_HUI", "DEMAIN", "APRES_DEMAIN", "HIER", "AVANT_HIER", "DASH", "WEEKEND", "CHAQUE", "JUSQU_A", "A", "APRES", "PASSE", "MINUIT", "APRES_MIDI", "MATIN", "SOIR", "NUIT", "PENDANT", "DANS", "LE", "LA", "OU", "ET", "CE", "DE", "DU", "AU", "MAINTENANT", "SUIVANT", "DERNIER", "WHITE_SPACE", "AVANT", "DEBUT", "FIN", "IL_Y_A", "COLON", "COMMA", "SLASH", "PLUS", "INT_00", "INT_01", "INT_02", "INT_03", "INT_04", "INT_05", "INT_06", "INT_07", "INT_08", "INT_09", "INT_0", "INT_1", "INT_2", "INT_3", "INT_4", "INT_5", "INT_6", "INT_7", "INT_8", "INT_9", "INT_10", "INT_11", "INT_12", "INT_13", "INT_14", "INT_15", "INT_16", "INT_17", "INT_18", "INT_19", "INT_20", "INT_21", "INT_22", "INT_23", "INT_24", "INT_25", "INT_26", "INT_27", "INT_28", "INT_29", "INT_30", "INT_31", "INT_32", "INT_33", "INT_34", "INT_35", "INT_36", "INT_37", "INT_38", "INT_39", "INT_40", "INT_41", "INT_42", "INT_43", "INT_44", "INT_45", "INT_46", "INT_47", "INT_48", "INT_49", "INT_50", "INT_51", "INT_52", "INT_53", "INT_54", "INT_55", "INT_56", "INT_57", "INT_58", "INT_59", "INT_60", "INT_61", "INT_62", "INT_63", "INT_64", "INT_65", "INT_66", "INT_67", "INT_68", "INT_69", "INT_70", "INT_71", "INT_72", "INT_73", "INT_74", "INT_75", "INT_76", "INT_77", "INT_78", "INT_79", "INT_80", "INT_81", "INT_82", "INT_83", "INT_84", "INT_85", "INT_86", "INT_87", "INT_88", "INT_89", "INT_90", "INT_91", "INT_92", "INT_93", "INT_94", "INT_95", "INT_96", "INT_97", "INT_98", "INT_99", "UN", "DEUX", "TROIS", "QUATRE", "CINQ", "SIX", "SEPT", "HUIT", "NEUF", "DIX", "ONZE", "DOUZE", "TREIZE", "QUATORZE", "QUINZE", "SEIZE", "VINGT", "TRENTE", "PREMIER", "DEUXIEME", "TROISIEME", "QUATRIEME", "CINQUIEME", "SIXIEME", "SEPTIEME", "HUITIEME", "NEUVIEME", "DIXIEME", "ONZIEME", "DOUZIEME", "TREIZIEME", "QUATORZIEME", "QUINZIEME", "SEIZIEME", "VINGTIEME", "TRENTIEME", "UNIEME", "SPACE", "UNKNOWN_CHAR", "UNKNOWN", "DIGIT", "INT", "MONTH_OF_YEAR", "DAY_OF_MONTH", "DAY_OF_WEEK", "DAY_OF_YEAR", "YEAR_OF", "DATE_TIME", "DATE_TIME_ALTERNATIVE", "EXPLICIT_DATE", "RELATIVE_DATE", "SEEK", "DIRECTION", "SEEK_BY", "EXPLICIT_SEEK", "SPAN", "EXPLICIT_TIME", "RELATIVE_TIME", "HOURS_OF_DAY", "MINUTES_OF_HOUR", "SECONDS_OF_MINUTE", "AM_PM", "ZONE", "ZONE_OFFSET", "RECURRENCE", "IS_INTERVAL", "SECOND"
    };
    public static final int DIRECTION=365;
    public static final int UNKNOWN_CHAR=219;
    public static final int INT_08=79;
    public static final int INT_09=80;
    public static final int DIXIEME=208;
    public static final int MAINTENANT=59;
    public static final int INT_02=73;
    public static final int INT_03=74;
    public static final int DU=57;
    public static final int INT_00=71;
    public static final int INT_01=72;
    public static final int INT_06=77;
    public static final int INT_07=78;
    public static final int APRES_DEMAIN=34;
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
    public static final int SEPT=187;
    public static final int DAY_OF_YEAR=358;
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
    public static final int WHITE_SPACE=62;
    public static final int INT_42=123;
    public static final int INT_43=124;
    public static final int ONZE=191;
    public static final int INT_40=121;
    public static final int INT_41=122;
    public static final int INT_34=115;
    public static final int INT_33=114;
    public static final int SINGLE_QUOTE=31;
    public static final int INT_36=117;
    public static final int INT_35=116;
    public static final int SLASH=69;
    public static final int INT_38=119;
    public static final int INT_37=118;
    public static final int AU=58;
    public static final int INT_39=120;
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
    public static final int AVRIL=8;
    public static final int INT_25=106;
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
    public static final int INT_6=87;
    public static final int SPACE=218;
    public static final int INT_5=86;
    public static final int INT_4=85;
    public static final int INT_3=84;
    public static final int INT_2=83;
    public static final int INT_1=82;
    public static final int INT_0=81;
    public static final int APRES=42;
    public static final int APRES_MIDI=45;
    public static final int SAMEDI=23;
    public static final int UNKNOWN=220;
    public static final int HIER=35;
    public static final int JANVIER=5;
    public static final int SEPTEMBRE=13;
    public static final int COMMA=68;
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
    public static final int UNIEME=217;
    public static final int INT_90=171;
    public static final int MAI=9;
    public static final int JUSQU_A=40;
    public static final int QUINZIEME=213;
    public static final int INT_97=178;
    public static final int INT_98=179;
    public static final int INT_95=176;
    public static final int INT_96=177;
    public static final int INT_93=174;
    public static final int INT_94=175;
    public static final int NEUVIEME=207;
    public static final int INT_91=172;
    public static final int INT_92=173;
    public static final int LA=52;
    public static final int NUIT=48;
    public static final int NOVEMBRE=15;
    public static final int OCTOBRE=14;
    public static final int INT_99=180;
    public static final int MINUTE=26;
    public static final int DECEMBRE=16;
    public static final int LE=51;
    public static final int LUNDI=18;

    // delegates
    // delegators


        public TreeRewrite(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public TreeRewrite(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return TreeRewrite.tokenNames; }
    public String getGrammarFileName() { return "com/joestelmach/natty/generated/TreeRewrite.g"; }


      private java.util.logging.Logger _logger = java.util.logging.Logger.getLogger("com.joestelmach.natty");
      
      public void displayRecognitionError(String[] tokenNames, RecognitionException re) {
        String message = getErrorHeader(re);
        try { message += getErrorMessage(re, tokenNames); } catch(Exception e) {}
        _logger.fine(message);
      }


    public static class topdown_return extends TreeRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "topdown"
    // com/joestelmach/natty/generated/TreeRewrite.g:22:1: topdown : ( ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK INT ) ^( DAY_OF_WEEK dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH INT ) ^( DAY_OF_MONTH dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR INT ) ^( MONTH_OF_YEAR dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR INT ) amount= INT ^( MONTH_OF_YEAR dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY $amount ^( MONTH_OF_YEAR $dow) ) );
    public final TreeRewrite.topdown_return topdown() throws RecognitionException {
        TreeRewrite.topdown_return retval = new TreeRewrite.topdown_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree dow=null;
        CommonTree amount=null;
        CommonTree SEEK1=null;
        CommonTree DIRECTION2=null;
        CommonTree SEEK_BY3=null;
        CommonTree INT4=null;
        CommonTree DAY_OF_WEEK5=null;
        CommonTree INT6=null;
        CommonTree DAY_OF_WEEK7=null;
        CommonTree SEEK8=null;
        CommonTree DIRECTION9=null;
        CommonTree SEEK_BY10=null;
        CommonTree INT11=null;
        CommonTree DAY_OF_MONTH12=null;
        CommonTree INT13=null;
        CommonTree DAY_OF_MONTH14=null;
        CommonTree SEEK15=null;
        CommonTree DIRECTION16=null;
        CommonTree SEEK_BY17=null;
        CommonTree INT18=null;
        CommonTree MONTH_OF_YEAR19=null;
        CommonTree INT20=null;
        CommonTree MONTH_OF_YEAR21=null;
        CommonTree SEEK22=null;
        CommonTree DIRECTION23=null;
        CommonTree SEEK_BY24=null;
        CommonTree INT25=null;
        CommonTree MONTH_OF_YEAR26=null;
        CommonTree INT27=null;
        CommonTree MONTH_OF_YEAR28=null;

        CommonTree dow_tree=null;
        CommonTree amount_tree=null;
        CommonTree SEEK1_tree=null;
        CommonTree DIRECTION2_tree=null;
        CommonTree SEEK_BY3_tree=null;
        CommonTree INT4_tree=null;
        CommonTree DAY_OF_WEEK5_tree=null;
        CommonTree INT6_tree=null;
        CommonTree DAY_OF_WEEK7_tree=null;
        CommonTree SEEK8_tree=null;
        CommonTree DIRECTION9_tree=null;
        CommonTree SEEK_BY10_tree=null;
        CommonTree INT11_tree=null;
        CommonTree DAY_OF_MONTH12_tree=null;
        CommonTree INT13_tree=null;
        CommonTree DAY_OF_MONTH14_tree=null;
        CommonTree SEEK15_tree=null;
        CommonTree DIRECTION16_tree=null;
        CommonTree SEEK_BY17_tree=null;
        CommonTree INT18_tree=null;
        CommonTree MONTH_OF_YEAR19_tree=null;
        CommonTree INT20_tree=null;
        CommonTree MONTH_OF_YEAR21_tree=null;
        CommonTree SEEK22_tree=null;
        CommonTree DIRECTION23_tree=null;
        CommonTree SEEK_BY24_tree=null;
        CommonTree INT25_tree=null;
        CommonTree MONTH_OF_YEAR26_tree=null;
        CommonTree INT27_tree=null;
        CommonTree MONTH_OF_YEAR28_tree=null;
        RewriteRuleNodeStream stream_DIRECTION=new RewriteRuleNodeStream(adaptor,"token DIRECTION");
        RewriteRuleNodeStream stream_SEEK_BY=new RewriteRuleNodeStream(adaptor,"token SEEK_BY");
        RewriteRuleNodeStream stream_DAY_OF_WEEK=new RewriteRuleNodeStream(adaptor,"token DAY_OF_WEEK");
        RewriteRuleNodeStream stream_INT=new RewriteRuleNodeStream(adaptor,"token INT");
        RewriteRuleNodeStream stream_MONTH_OF_YEAR=new RewriteRuleNodeStream(adaptor,"token MONTH_OF_YEAR");
        RewriteRuleNodeStream stream_DAY_OF_MONTH=new RewriteRuleNodeStream(adaptor,"token DAY_OF_MONTH");
        RewriteRuleNodeStream stream_SEEK=new RewriteRuleNodeStream(adaptor,"token SEEK");

        try {
            // com/joestelmach/natty/generated/TreeRewrite.g:23:3: ( ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK INT ) ^( DAY_OF_WEEK dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH INT ) ^( DAY_OF_MONTH dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR INT ) ^( MONTH_OF_YEAR dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR INT ) amount= INT ^( MONTH_OF_YEAR dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY $amount ^( MONTH_OF_YEAR $dow) ) )
            int alt1=4;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // com/joestelmach/natty/generated/TreeRewrite.g:23:5: ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK INT ) ^( DAY_OF_WEEK dow= INT ) )
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    SEEK1=(CommonTree)match(input,SEEK,FOLLOW_SEEK_in_topdown60); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SEEK.add(SEEK1);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = SEEK1;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    DIRECTION2=(CommonTree)match(input,DIRECTION,FOLLOW_DIRECTION_in_topdown62); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DIRECTION.add(DIRECTION2);

                    _last = (CommonTree)input.LT(1);
                    SEEK_BY3=(CommonTree)match(input,SEEK_BY,FOLLOW_SEEK_BY_in_topdown64); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SEEK_BY.add(SEEK_BY3);

                    _last = (CommonTree)input.LT(1);
                    INT4=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown66); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(INT4);

                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_2 = _last;
                    CommonTree _first_2 = null;
                    _last = (CommonTree)input.LT(1);
                    DAY_OF_WEEK5=(CommonTree)match(input,DAY_OF_WEEK,FOLLOW_DAY_OF_WEEK_in_topdown69); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DAY_OF_WEEK.add(DAY_OF_WEEK5);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = DAY_OF_WEEK5;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    INT6=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown71); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(INT6);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }

                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_2 = _last;
                    CommonTree _first_2 = null;
                    _last = (CommonTree)input.LT(1);
                    DAY_OF_WEEK7=(CommonTree)match(input,DAY_OF_WEEK,FOLLOW_DAY_OF_WEEK_in_topdown75); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DAY_OF_WEEK.add(DAY_OF_WEEK7);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = DAY_OF_WEEK7;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    dow=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown79); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(dow);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: INT, SEEK, dow, DIRECTION, DAY_OF_WEEK, SEEK_BY
                    // token labels: dow
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleNodeStream stream_dow=new RewriteRuleNodeStream(adaptor,"token dow",dow);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 24:7: -> ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK $dow) )
                    {
                        // com/joestelmach/natty/generated/TreeRewrite.g:24:10: ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK $dow) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_SEEK.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_DIRECTION.nextNode());
                        adaptor.addChild(root_1, stream_SEEK_BY.nextNode());
                        adaptor.addChild(root_1, stream_INT.nextNode());
                        // com/joestelmach/natty/generated/TreeRewrite.g:24:39: ^( DAY_OF_WEEK $dow)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_DAY_OF_WEEK.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_dow.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/TreeRewrite.g:26:5: ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH INT ) ^( DAY_OF_MONTH dow= INT ) )
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    SEEK8=(CommonTree)match(input,SEEK,FOLLOW_SEEK_in_topdown120); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SEEK.add(SEEK8);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = SEEK8;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    DIRECTION9=(CommonTree)match(input,DIRECTION,FOLLOW_DIRECTION_in_topdown122); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DIRECTION.add(DIRECTION9);

                    _last = (CommonTree)input.LT(1);
                    SEEK_BY10=(CommonTree)match(input,SEEK_BY,FOLLOW_SEEK_BY_in_topdown124); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SEEK_BY.add(SEEK_BY10);

                    _last = (CommonTree)input.LT(1);
                    INT11=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown126); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(INT11);

                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_2 = _last;
                    CommonTree _first_2 = null;
                    _last = (CommonTree)input.LT(1);
                    DAY_OF_MONTH12=(CommonTree)match(input,DAY_OF_MONTH,FOLLOW_DAY_OF_MONTH_in_topdown129); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DAY_OF_MONTH.add(DAY_OF_MONTH12);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = DAY_OF_MONTH12;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    INT13=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown131); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(INT13);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }

                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_2 = _last;
                    CommonTree _first_2 = null;
                    _last = (CommonTree)input.LT(1);
                    DAY_OF_MONTH14=(CommonTree)match(input,DAY_OF_MONTH,FOLLOW_DAY_OF_MONTH_in_topdown135); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DAY_OF_MONTH.add(DAY_OF_MONTH14);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = DAY_OF_MONTH14;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    dow=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown139); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(dow);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: SEEK, DIRECTION, dow, DAY_OF_MONTH, INT, SEEK_BY
                    // token labels: dow
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleNodeStream stream_dow=new RewriteRuleNodeStream(adaptor,"token dow",dow);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 27:7: -> ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH $dow) )
                    {
                        // com/joestelmach/natty/generated/TreeRewrite.g:27:10: ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH $dow) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_SEEK.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_DIRECTION.nextNode());
                        adaptor.addChild(root_1, stream_SEEK_BY.nextNode());
                        adaptor.addChild(root_1, stream_INT.nextNode());
                        // com/joestelmach/natty/generated/TreeRewrite.g:27:39: ^( DAY_OF_MONTH $dow)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_DAY_OF_MONTH.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_dow.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;
                case 3 :
                    // com/joestelmach/natty/generated/TreeRewrite.g:29:5: ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR INT ) ^( MONTH_OF_YEAR dow= INT ) )
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    SEEK15=(CommonTree)match(input,SEEK,FOLLOW_SEEK_in_topdown180); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SEEK.add(SEEK15);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = SEEK15;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    DIRECTION16=(CommonTree)match(input,DIRECTION,FOLLOW_DIRECTION_in_topdown182); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DIRECTION.add(DIRECTION16);

                    _last = (CommonTree)input.LT(1);
                    SEEK_BY17=(CommonTree)match(input,SEEK_BY,FOLLOW_SEEK_BY_in_topdown184); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SEEK_BY.add(SEEK_BY17);

                    _last = (CommonTree)input.LT(1);
                    INT18=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown186); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(INT18);

                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_2 = _last;
                    CommonTree _first_2 = null;
                    _last = (CommonTree)input.LT(1);
                    MONTH_OF_YEAR19=(CommonTree)match(input,MONTH_OF_YEAR,FOLLOW_MONTH_OF_YEAR_in_topdown189); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_MONTH_OF_YEAR.add(MONTH_OF_YEAR19);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = MONTH_OF_YEAR19;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    INT20=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown191); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(INT20);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }

                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_2 = _last;
                    CommonTree _first_2 = null;
                    _last = (CommonTree)input.LT(1);
                    MONTH_OF_YEAR21=(CommonTree)match(input,MONTH_OF_YEAR,FOLLOW_MONTH_OF_YEAR_in_topdown195); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_MONTH_OF_YEAR.add(MONTH_OF_YEAR21);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = MONTH_OF_YEAR21;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    dow=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown199); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(dow);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: dow, INT, MONTH_OF_YEAR, SEEK_BY, SEEK, DIRECTION
                    // token labels: dow
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleNodeStream stream_dow=new RewriteRuleNodeStream(adaptor,"token dow",dow);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 30:7: -> ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR $dow) )
                    {
                        // com/joestelmach/natty/generated/TreeRewrite.g:30:10: ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR $dow) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_SEEK.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_DIRECTION.nextNode());
                        adaptor.addChild(root_1, stream_SEEK_BY.nextNode());
                        adaptor.addChild(root_1, stream_INT.nextNode());
                        // com/joestelmach/natty/generated/TreeRewrite.g:30:39: ^( MONTH_OF_YEAR $dow)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_MONTH_OF_YEAR.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_dow.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;
                case 4 :
                    // com/joestelmach/natty/generated/TreeRewrite.g:32:5: ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR INT ) amount= INT ^( MONTH_OF_YEAR dow= INT ) )
                    {
                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    _last = (CommonTree)input.LT(1);
                    SEEK22=(CommonTree)match(input,SEEK,FOLLOW_SEEK_in_topdown240); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SEEK.add(SEEK22);


                    if ( state.backtracking==1 )
                    if ( _first_0==null ) _first_0 = SEEK22;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    DIRECTION23=(CommonTree)match(input,DIRECTION,FOLLOW_DIRECTION_in_topdown242); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_DIRECTION.add(DIRECTION23);

                    _last = (CommonTree)input.LT(1);
                    SEEK_BY24=(CommonTree)match(input,SEEK_BY,FOLLOW_SEEK_BY_in_topdown244); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_SEEK_BY.add(SEEK_BY24);

                    _last = (CommonTree)input.LT(1);
                    INT25=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown246); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(INT25);

                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_2 = _last;
                    CommonTree _first_2 = null;
                    _last = (CommonTree)input.LT(1);
                    MONTH_OF_YEAR26=(CommonTree)match(input,MONTH_OF_YEAR,FOLLOW_MONTH_OF_YEAR_in_topdown249); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_MONTH_OF_YEAR.add(MONTH_OF_YEAR26);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = MONTH_OF_YEAR26;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    INT27=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown251); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(INT27);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }

                    _last = (CommonTree)input.LT(1);
                    amount=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown256); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(amount);

                    _last = (CommonTree)input.LT(1);
                    {
                    CommonTree _save_last_2 = _last;
                    CommonTree _first_2 = null;
                    _last = (CommonTree)input.LT(1);
                    MONTH_OF_YEAR28=(CommonTree)match(input,MONTH_OF_YEAR,FOLLOW_MONTH_OF_YEAR_in_topdown259); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_MONTH_OF_YEAR.add(MONTH_OF_YEAR28);


                    if ( state.backtracking==1 )
                    if ( _first_1==null ) _first_1 = MONTH_OF_YEAR28;
                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    _last = (CommonTree)input.LT(1);
                    dow=(CommonTree)match(input,INT,FOLLOW_INT_in_topdown263); if (state.failed) return retval; 
                    if ( state.backtracking==1 ) stream_INT.add(dow);


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_2;
                    }


                    match(input, Token.UP, null); if (state.failed) return retval;_last = _save_last_1;
                    }



                    // AST REWRITE
                    // elements: dow, amount, SEEK_BY, DIRECTION, MONTH_OF_YEAR, SEEK
                    // token labels: amount, dow
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==1 ) {
                    retval.tree = root_0;
                    RewriteRuleNodeStream stream_amount=new RewriteRuleNodeStream(adaptor,"token amount",amount);
                    RewriteRuleNodeStream stream_dow=new RewriteRuleNodeStream(adaptor,"token dow",dow);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 33:7: -> ^( SEEK DIRECTION SEEK_BY $amount ^( MONTH_OF_YEAR $dow) )
                    {
                        // com/joestelmach/natty/generated/TreeRewrite.g:33:10: ^( SEEK DIRECTION SEEK_BY $amount ^( MONTH_OF_YEAR $dow) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_SEEK.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_DIRECTION.nextNode());
                        adaptor.addChild(root_1, stream_SEEK_BY.nextNode());
                        adaptor.addChild(root_1, stream_amount.nextNode());
                        // com/joestelmach/natty/generated/TreeRewrite.g:33:43: ^( MONTH_OF_YEAR $dow)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_MONTH_OF_YEAR.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_dow.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                    input.replaceChildren(adaptor.getParent(retval.start),
                                          adaptor.getChildIndex(retval.start),
                                          adaptor.getChildIndex(_last),
                                          retval.tree);}
                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "topdown"

    // Delegated rules


    protected DFA1 dfa1 = new DFA1(this);
    static final String DFA1_eotS =
        "\16\uffff";
    static final String DFA1_eofS =
        "\16\uffff";
    static final String DFA1_minS =
        "\1\u016c\1\2\1\u016d\1\u016e\1\u00de\1\u0163\2\uffff\1\2\1\u00de"+
        "\1\3\1\u00de\2\uffff";
    static final String DFA1_maxS =
        "\1\u016c\1\2\1\u016d\1\u016e\1\u00de\1\u0165\2\uffff\1\2\1\u00de"+
        "\1\3\1\u0163\2\uffff";
    static final String DFA1_acceptS =
        "\6\uffff\1\1\1\2\4\uffff\1\3\1\4";
    static final String DFA1_specialS =
        "\16\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3",
            "\1\4",
            "\1\5",
            "\1\10\1\7\1\6",
            "",
            "",
            "\1\11",
            "\1\12",
            "\1\13",
            "\1\15\u0084\uffff\1\14",
            "",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "22:1: topdown : ( ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK INT ) ^( DAY_OF_WEEK dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_WEEK $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH INT ) ^( DAY_OF_MONTH dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( DAY_OF_MONTH $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR INT ) ^( MONTH_OF_YEAR dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR $dow) ) | ^( SEEK DIRECTION SEEK_BY INT ^( MONTH_OF_YEAR INT ) amount= INT ^( MONTH_OF_YEAR dow= INT ) ) -> ^( SEEK DIRECTION SEEK_BY $amount ^( MONTH_OF_YEAR $dow) ) );";
        }
    }
 

    public static final BitSet FOLLOW_SEEK_in_topdown60 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIRECTION_in_topdown62 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_SEEK_BY_in_topdown64 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_INT_in_topdown66 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_DAY_OF_WEEK_in_topdown69 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_topdown71 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DAY_OF_WEEK_in_topdown75 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_topdown79 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEEK_in_topdown120 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIRECTION_in_topdown122 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_SEEK_BY_in_topdown124 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_INT_in_topdown126 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000001000000000L});
    public static final BitSet FOLLOW_DAY_OF_MONTH_in_topdown129 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_topdown131 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DAY_OF_MONTH_in_topdown135 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_topdown139 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEEK_in_topdown180 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIRECTION_in_topdown182 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_SEEK_BY_in_topdown184 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_INT_in_topdown186 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_MONTH_OF_YEAR_in_topdown189 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_topdown191 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MONTH_OF_YEAR_in_topdown195 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_topdown199 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEEK_in_topdown240 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIRECTION_in_topdown242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_SEEK_BY_in_topdown244 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_INT_in_topdown246 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_MONTH_OF_YEAR_in_topdown249 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_topdown251 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INT_in_topdown256 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_MONTH_OF_YEAR_in_topdown259 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_topdown263 = new BitSet(new long[]{0x0000000000000008L});

}