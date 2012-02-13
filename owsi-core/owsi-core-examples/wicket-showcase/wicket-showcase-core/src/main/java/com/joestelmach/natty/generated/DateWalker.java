// $ANTLR 3.2 Sep 23, 2009 14:05:07 com/joestelmach/natty/generated/DateWalker.g 2012-02-13 15:13:28
 package com.joestelmach.natty.generated; 

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeParser;

public class DateWalker extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DOT", "JANVIER", "FEVRIER", "MARS", "AVRIL", "MAI", "JUIN", "JUILLET", "AOUT", "SEPTEMBRE", "OCTOBRE", "NOVEMBRE", "DECEMBRE", "DIMANCHE", "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "HEURE", "HEURE_SHORT", "MINUTE", "JOUR", "SEMAINE", "MOIS", "ANNEE", "SINGLE_QUOTE", "AUJOURD_HUI", "DEMAIN", "APRES_DEMAIN", "HIER", "AVANT_HIER", "DASH", "WEEKEND", "CHAQUE", "JUSQU_A", "A", "APRES", "PASSE", "MINUIT", "APRES_MIDI", "MATIN", "SOIR", "NUIT", "PENDANT", "DANS", "LE", "LA", "OU", "ET", "CE", "DE", "DU", "AU", "MAINTENANT", "SUIVANT", "DERNIER", "WHITE_SPACE", "AVANT", "DEBUT", "FIN", "IL_Y_A", "COLON", "COMMA", "SLASH", "PLUS", "INT_00", "INT_01", "INT_02", "INT_03", "INT_04", "INT_05", "INT_06", "INT_07", "INT_08", "INT_09", "INT_0", "INT_1", "INT_2", "INT_3", "INT_4", "INT_5", "INT_6", "INT_7", "INT_8", "INT_9", "INT_10", "INT_11", "INT_12", "INT_13", "INT_14", "INT_15", "INT_16", "INT_17", "INT_18", "INT_19", "INT_20", "INT_21", "INT_22", "INT_23", "INT_24", "INT_25", "INT_26", "INT_27", "INT_28", "INT_29", "INT_30", "INT_31", "INT_32", "INT_33", "INT_34", "INT_35", "INT_36", "INT_37", "INT_38", "INT_39", "INT_40", "INT_41", "INT_42", "INT_43", "INT_44", "INT_45", "INT_46", "INT_47", "INT_48", "INT_49", "INT_50", "INT_51", "INT_52", "INT_53", "INT_54", "INT_55", "INT_56", "INT_57", "INT_58", "INT_59", "INT_60", "INT_61", "INT_62", "INT_63", "INT_64", "INT_65", "INT_66", "INT_67", "INT_68", "INT_69", "INT_70", "INT_71", "INT_72", "INT_73", "INT_74", "INT_75", "INT_76", "INT_77", "INT_78", "INT_79", "INT_80", "INT_81", "INT_82", "INT_83", "INT_84", "INT_85", "INT_86", "INT_87", "INT_88", "INT_89", "INT_90", "INT_91", "INT_92", "INT_93", "INT_94", "INT_95", "INT_96", "INT_97", "INT_98", "INT_99", "UN", "DEUX", "TROIS", "QUATRE", "CINQ", "SIX", "SEPT", "HUIT", "NEUF", "DIX", "ONZE", "DOUZE", "TREIZE", "QUATORZE", "QUINZE", "SEIZE", "VINGT", "TRENTE", "PREMIER", "DEUXIEME", "TROISIEME", "QUATRIEME", "CINQUIEME", "SIXIEME", "SEPTIEME", "HUITIEME", "NEUVIEME", "DIXIEME", "ONZIEME", "DOUZIEME", "TREIZIEME", "QUATORZIEME", "QUINZIEME", "SEIZIEME", "VINGTIEME", "TRENTIEME", "UNIEME", "SPACE", "UNKNOWN_CHAR", "UNKNOWN", "DIGIT", "INT", "MONTH_OF_YEAR", "DAY_OF_MONTH", "DAY_OF_WEEK", "DAY_OF_YEAR", "YEAR_OF", "DATE_TIME", "DATE_TIME_ALTERNATIVE", "EXPLICIT_DATE", "RELATIVE_DATE", "SEEK", "DIRECTION", "SEEK_BY", "EXPLICIT_SEEK", "SPAN", "EXPLICIT_TIME", "RELATIVE_TIME", "HOURS_OF_DAY", "MINUTES_OF_HOUR", "SECONDS_OF_MINUTE", "AM_PM", "ZONE", "ZONE_OFFSET", "RECURRENCE", "IS_INTERVAL", "SECOND", "WEEK_INDEX"
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
    public static final int WEEK_INDEX=380;
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


        public DateWalker(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public DateWalker(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return DateWalker.tokenNames; }
    public String getGrammarFileName() { return "com/joestelmach/natty/generated/DateWalker.g"; }


      private com.joestelmach.natty.WalkerState _walkerState = new com.joestelmach.natty.WalkerState();
      private java.util.logging.Logger _logger = java.util.logging.Logger.getLogger("com.joestelmach.natty");
      
      public void displayRecognitionError(String[] tokenNames, RecognitionException re) {
        String message = getErrorHeader(re);
        try { message += getErrorMessage(re, tokenNames); } catch(Exception e) {}
        _logger.fine(message);
      }
      
      public void recover(IntStream input, RecognitionException re) {
        reportError(re);
        _walkerState.clearDateGroup();
      }
      
      public com.joestelmach.natty.WalkerState getState() {
        return _walkerState;
      }



    // $ANTLR start "parse"
    // com/joestelmach/natty/generated/DateWalker.g:30:1: parse : date_time_alternative ( recurrence )? ;
    public final void parse() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateWalker.g:31:3: ( date_time_alternative ( recurrence )? )
            // com/joestelmach/natty/generated/DateWalker.g:31:5: date_time_alternative ( recurrence )?
            {
            pushFollow(FOLLOW_date_time_alternative_in_parse45);
            date_time_alternative();

            state._fsp--;

            // com/joestelmach/natty/generated/DateWalker.g:31:27: ( recurrence )?
            int alt1=2;
            switch ( input.LA(1) ) {
                case RECURRENCE:
                    {
                    alt1=1;
                    }
                    break;
            }

            switch (alt1) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:31:27: recurrence
                    {
                    pushFollow(FOLLOW_recurrence_in_parse47);
                    recurrence();

                    state._fsp--;


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
        return ;
    }
    // $ANTLR end "parse"


    // $ANTLR start "recurrence"
    // com/joestelmach/natty/generated/DateWalker.g:34:1: recurrence : ^( RECURRENCE ( date_time )? ) ;
    public final void recurrence() throws RecognitionException {
         
            _walkerState.setRecurring();
          
        try {
            // com/joestelmach/natty/generated/DateWalker.g:38:3: ( ^( RECURRENCE ( date_time )? ) )
            // com/joestelmach/natty/generated/DateWalker.g:38:5: ^( RECURRENCE ( date_time )? )
            {
            match(input,RECURRENCE,FOLLOW_RECURRENCE_in_recurrence71); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // com/joestelmach/natty/generated/DateWalker.g:38:18: ( date_time )?
                int alt2=2;
                switch ( input.LA(1) ) {
                    case DATE_TIME:
                        {
                        alt2=1;
                        }
                        break;
                }

                switch (alt2) {
                    case 1 :
                        // com/joestelmach/natty/generated/DateWalker.g:38:18: date_time
                        {
                        pushFollow(FOLLOW_date_time_in_recurrence73);
                        date_time();

                        state._fsp--;


                        }
                        break;

                }

                 _walkerState.captureDateTime(); 

                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "recurrence"


    // $ANTLR start "date_time_alternative"
    // com/joestelmach/natty/generated/DateWalker.g:41:1: date_time_alternative : ^( DATE_TIME_ALTERNATIVE ( date_time )+ ( is_interval )? ) ;
    public final void date_time_alternative() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateWalker.g:42:3: ( ^( DATE_TIME_ALTERNATIVE ( date_time )+ ( is_interval )? ) )
            // com/joestelmach/natty/generated/DateWalker.g:42:5: ^( DATE_TIME_ALTERNATIVE ( date_time )+ ( is_interval )? )
            {
            match(input,DATE_TIME_ALTERNATIVE,FOLLOW_DATE_TIME_ALTERNATIVE_in_date_time_alternative92); 

            match(input, Token.DOWN, null); 
            // com/joestelmach/natty/generated/DateWalker.g:42:29: ( date_time )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                switch ( input.LA(1) ) {
                case DATE_TIME:
                    {
                    alt3=1;
                    }
                    break;

                }

                switch (alt3) {
            	case 1 :
            	    // com/joestelmach/natty/generated/DateWalker.g:42:29: date_time
            	    {
            	    pushFollow(FOLLOW_date_time_in_date_time_alternative94);
            	    date_time();

            	    state._fsp--;


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

            // com/joestelmach/natty/generated/DateWalker.g:42:40: ( is_interval )?
            int alt4=2;
            switch ( input.LA(1) ) {
                case IS_INTERVAL:
                    {
                    alt4=1;
                    }
                    break;
            }

            switch (alt4) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:42:40: is_interval
                    {
                    pushFollow(FOLLOW_is_interval_in_date_time_alternative97);
                    is_interval();

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "date_time_alternative"


    // $ANTLR start "is_interval"
    // com/joestelmach/natty/generated/DateWalker.g:45:1: is_interval : ^( IS_INTERVAL ) ;
    public final void is_interval() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateWalker.g:46:3: ( ^( IS_INTERVAL ) )
            // com/joestelmach/natty/generated/DateWalker.g:46:5: ^( IS_INTERVAL )
            {
            match(input,IS_INTERVAL,FOLLOW_IS_INTERVAL_in_is_interval113); 

             _walkerState.setInterval(); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "is_interval"


    // $ANTLR start "date_time"
    // com/joestelmach/natty/generated/DateWalker.g:49:1: date_time : ^( DATE_TIME ( date )? ( time )? ) ;
    public final void date_time() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateWalker.g:53:3: ( ^( DATE_TIME ( date )? ( time )? ) )
            // com/joestelmach/natty/generated/DateWalker.g:53:5: ^( DATE_TIME ( date )? ( time )? )
            {
            match(input,DATE_TIME,FOLLOW_DATE_TIME_in_date_time137); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // com/joestelmach/natty/generated/DateWalker.g:53:17: ( date )?
                int alt5=2;
                switch ( input.LA(1) ) {
                    case EXPLICIT_DATE:
                    case RELATIVE_DATE:
                        {
                        alt5=1;
                        }
                        break;
                }

                switch (alt5) {
                    case 1 :
                        // com/joestelmach/natty/generated/DateWalker.g:53:17: date
                        {
                        pushFollow(FOLLOW_date_in_date_time139);
                        date();

                        state._fsp--;


                        }
                        break;

                }

                // com/joestelmach/natty/generated/DateWalker.g:53:23: ( time )?
                int alt6=2;
                switch ( input.LA(1) ) {
                    case EXPLICIT_TIME:
                    case RELATIVE_TIME:
                        {
                        alt6=1;
                        }
                        break;
                }

                switch (alt6) {
                    case 1 :
                        // com/joestelmach/natty/generated/DateWalker.g:53:23: time
                        {
                        pushFollow(FOLLOW_time_in_date_time142);
                        time();

                        state._fsp--;


                        }
                        break;

                }


                match(input, Token.UP, null); 
            }

            }


                _walkerState.captureDateTime(); 
              
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "date_time"


    // $ANTLR start "date"
    // com/joestelmach/natty/generated/DateWalker.g:56:1: date : ( relative_date | explicit_date );
    public final void date() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateWalker.g:57:3: ( relative_date | explicit_date )
            int alt7=2;
            switch ( input.LA(1) ) {
            case RELATIVE_DATE:
                {
                alt7=1;
                }
                break;
            case EXPLICIT_DATE:
                {
                alt7=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:57:5: relative_date
                    {
                    pushFollow(FOLLOW_relative_date_in_date161);
                    relative_date();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateWalker.g:58:5: explicit_date
                    {
                    pushFollow(FOLLOW_explicit_date_in_date168);
                    explicit_date();

                    state._fsp--;


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
        return ;
    }
    // $ANTLR end "date"


    // $ANTLR start "relative_date"
    // com/joestelmach/natty/generated/DateWalker.g:61:1: relative_date : ^( RELATIVE_DATE ( seek )? ( explicit_seek )* ) ;
    public final void relative_date() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateWalker.g:62:3: ( ^( RELATIVE_DATE ( seek )? ( explicit_seek )* ) )
            // com/joestelmach/natty/generated/DateWalker.g:62:5: ^( RELATIVE_DATE ( seek )? ( explicit_seek )* )
            {
            match(input,RELATIVE_DATE,FOLLOW_RELATIVE_DATE_in_relative_date184); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // com/joestelmach/natty/generated/DateWalker.g:62:21: ( seek )?
                int alt8=2;
                switch ( input.LA(1) ) {
                    case SEEK:
                        {
                        alt8=1;
                        }
                        break;
                }

                switch (alt8) {
                    case 1 :
                        // com/joestelmach/natty/generated/DateWalker.g:62:21: seek
                        {
                        pushFollow(FOLLOW_seek_in_relative_date186);
                        seek();

                        state._fsp--;


                        }
                        break;

                }

                // com/joestelmach/natty/generated/DateWalker.g:62:27: ( explicit_seek )*
                loop9:
                do {
                    int alt9=2;
                    switch ( input.LA(1) ) {
                    case EXPLICIT_SEEK:
                        {
                        alt9=1;
                        }
                        break;

                    }

                    switch (alt9) {
                	case 1 :
                	    // com/joestelmach/natty/generated/DateWalker.g:62:27: explicit_seek
                	    {
                	    pushFollow(FOLLOW_explicit_seek_in_relative_date189);
                	    explicit_seek();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop9;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "relative_date"


    // $ANTLR start "week_index"
    // com/joestelmach/natty/generated/DateWalker.g:65:1: week_index : ^( WEEK_INDEX index= INT ^( DAY_OF_WEEK day= INT ) ) ;
    public final void week_index() throws RecognitionException {
        CommonTree index=null;
        CommonTree day=null;

        try {
            // com/joestelmach/natty/generated/DateWalker.g:66:3: ( ^( WEEK_INDEX index= INT ^( DAY_OF_WEEK day= INT ) ) )
            // com/joestelmach/natty/generated/DateWalker.g:66:5: ^( WEEK_INDEX index= INT ^( DAY_OF_WEEK day= INT ) )
            {
            match(input,WEEK_INDEX,FOLLOW_WEEK_INDEX_in_week_index207); 

            match(input, Token.DOWN, null); 
            index=(CommonTree)match(input,INT,FOLLOW_INT_in_week_index211); 
            match(input,DAY_OF_WEEK,FOLLOW_DAY_OF_WEEK_in_week_index214); 

            match(input, Token.DOWN, null); 
            day=(CommonTree)match(input,INT,FOLLOW_INT_in_week_index218); 

            match(input, Token.UP, null); 

            match(input, Token.UP, null); 
            _walkerState.setDayOfWeekIndex((index!=null?index.getText():null), (day!=null?day.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "week_index"


    // $ANTLR start "explicit_date"
    // com/joestelmach/natty/generated/DateWalker.g:70:1: explicit_date : ^( EXPLICIT_DATE ^( MONTH_OF_YEAR month= INT ) ^( DAY_OF_MONTH dom= INT ) ( ^( DAY_OF_WEEK dow= INT ) )? ( ^( YEAR_OF year= INT ) )? ) ;
    public final void explicit_date() throws RecognitionException {
        CommonTree month=null;
        CommonTree dom=null;
        CommonTree dow=null;
        CommonTree year=null;

        try {
            // com/joestelmach/natty/generated/DateWalker.g:71:3: ( ^( EXPLICIT_DATE ^( MONTH_OF_YEAR month= INT ) ^( DAY_OF_MONTH dom= INT ) ( ^( DAY_OF_WEEK dow= INT ) )? ( ^( YEAR_OF year= INT ) )? ) )
            // com/joestelmach/natty/generated/DateWalker.g:71:5: ^( EXPLICIT_DATE ^( MONTH_OF_YEAR month= INT ) ^( DAY_OF_MONTH dom= INT ) ( ^( DAY_OF_WEEK dow= INT ) )? ( ^( YEAR_OF year= INT ) )? )
            {
            match(input,EXPLICIT_DATE,FOLLOW_EXPLICIT_DATE_in_explicit_date242); 

            match(input, Token.DOWN, null); 
            match(input,MONTH_OF_YEAR,FOLLOW_MONTH_OF_YEAR_in_explicit_date245); 

            match(input, Token.DOWN, null); 
            month=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_date249); 

            match(input, Token.UP, null); 
            match(input,DAY_OF_MONTH,FOLLOW_DAY_OF_MONTH_in_explicit_date253); 

            match(input, Token.DOWN, null); 
            dom=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_date257); 

            match(input, Token.UP, null); 
            // com/joestelmach/natty/generated/DateWalker.g:72:9: ( ^( DAY_OF_WEEK dow= INT ) )?
            int alt10=2;
            switch ( input.LA(1) ) {
                case DAY_OF_WEEK:
                    {
                    alt10=1;
                    }
                    break;
            }

            switch (alt10) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:72:10: ^( DAY_OF_WEEK dow= INT )
                    {
                    match(input,DAY_OF_WEEK,FOLLOW_DAY_OF_WEEK_in_explicit_date271); 

                    match(input, Token.DOWN, null); 
                    dow=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_date275); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }

            // com/joestelmach/natty/generated/DateWalker.g:72:35: ( ^( YEAR_OF year= INT ) )?
            int alt11=2;
            switch ( input.LA(1) ) {
                case YEAR_OF:
                    {
                    alt11=1;
                    }
                    break;
            }

            switch (alt11) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:72:36: ^( YEAR_OF year= INT )
                    {
                    match(input,YEAR_OF,FOLLOW_YEAR_OF_in_explicit_date282); 

                    match(input, Token.DOWN, null); 
                    year=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_date286); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }


            match(input, Token.UP, null); 
            _walkerState.setExplicitDate((month!=null?month.getText():null), (dom!=null?dom.getText():null), (dow!=null?dow.getText():null), (year!=null?year.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "explicit_date"


    // $ANTLR start "time"
    // com/joestelmach/natty/generated/DateWalker.g:76:1: time : ( explicit_time | relative_time );
    public final void time() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateWalker.g:77:3: ( explicit_time | relative_time )
            int alt12=2;
            switch ( input.LA(1) ) {
            case EXPLICIT_TIME:
                {
                alt12=1;
                }
                break;
            case RELATIVE_TIME:
                {
                alt12=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }

            switch (alt12) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:77:5: explicit_time
                    {
                    pushFollow(FOLLOW_explicit_time_in_time311);
                    explicit_time();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateWalker.g:78:5: relative_time
                    {
                    pushFollow(FOLLOW_relative_time_in_time317);
                    relative_time();

                    state._fsp--;


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
        return ;
    }
    // $ANTLR end "time"


    // $ANTLR start "explicit_time"
    // com/joestelmach/natty/generated/DateWalker.g:81:1: explicit_time : ^( EXPLICIT_TIME ^( HOURS_OF_DAY hours= INT ) ^( MINUTES_OF_HOUR minutes= INT ) ( ^( SECONDS_OF_MINUTE seconds= INT ) )? ( AM_PM )? (zone= ZONE | zone= ZONE_OFFSET )? ) ;
    public final void explicit_time() throws RecognitionException {
        CommonTree hours=null;
        CommonTree minutes=null;
        CommonTree seconds=null;
        CommonTree zone=null;
        CommonTree AM_PM1=null;

        try {
            // com/joestelmach/natty/generated/DateWalker.g:82:3: ( ^( EXPLICIT_TIME ^( HOURS_OF_DAY hours= INT ) ^( MINUTES_OF_HOUR minutes= INT ) ( ^( SECONDS_OF_MINUTE seconds= INT ) )? ( AM_PM )? (zone= ZONE | zone= ZONE_OFFSET )? ) )
            // com/joestelmach/natty/generated/DateWalker.g:82:5: ^( EXPLICIT_TIME ^( HOURS_OF_DAY hours= INT ) ^( MINUTES_OF_HOUR minutes= INT ) ( ^( SECONDS_OF_MINUTE seconds= INT ) )? ( AM_PM )? (zone= ZONE | zone= ZONE_OFFSET )? )
            {
            match(input,EXPLICIT_TIME,FOLLOW_EXPLICIT_TIME_in_explicit_time333); 

            match(input, Token.DOWN, null); 
            match(input,HOURS_OF_DAY,FOLLOW_HOURS_OF_DAY_in_explicit_time336); 

            match(input, Token.DOWN, null); 
            hours=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_time340); 

            match(input, Token.UP, null); 
            match(input,MINUTES_OF_HOUR,FOLLOW_MINUTES_OF_HOUR_in_explicit_time344); 

            match(input, Token.DOWN, null); 
            minutes=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_time348); 

            match(input, Token.UP, null); 
            // com/joestelmach/natty/generated/DateWalker.g:83:9: ( ^( SECONDS_OF_MINUTE seconds= INT ) )?
            int alt13=2;
            switch ( input.LA(1) ) {
                case SECONDS_OF_MINUTE:
                    {
                    alt13=1;
                    }
                    break;
            }

            switch (alt13) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:83:10: ^( SECONDS_OF_MINUTE seconds= INT )
                    {
                    match(input,SECONDS_OF_MINUTE,FOLLOW_SECONDS_OF_MINUTE_in_explicit_time362); 

                    match(input, Token.DOWN, null); 
                    seconds=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_time366); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }

            // com/joestelmach/natty/generated/DateWalker.g:83:45: ( AM_PM )?
            int alt14=2;
            switch ( input.LA(1) ) {
                case AM_PM:
                    {
                    alt14=1;
                    }
                    break;
            }

            switch (alt14) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:83:45: AM_PM
                    {
                    AM_PM1=(CommonTree)match(input,AM_PM,FOLLOW_AM_PM_in_explicit_time371); 

                    }
                    break;

            }

            // com/joestelmach/natty/generated/DateWalker.g:83:52: (zone= ZONE | zone= ZONE_OFFSET )?
            int alt15=3;
            switch ( input.LA(1) ) {
                case ZONE:
                    {
                    alt15=1;
                    }
                    break;
                case ZONE_OFFSET:
                    {
                    alt15=2;
                    }
                    break;
            }

            switch (alt15) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:83:53: zone= ZONE
                    {
                    zone=(CommonTree)match(input,ZONE,FOLLOW_ZONE_in_explicit_time377); 

                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateWalker.g:83:65: zone= ZONE_OFFSET
                    {
                    zone=(CommonTree)match(input,ZONE_OFFSET,FOLLOW_ZONE_OFFSET_in_explicit_time383); 

                    }
                    break;

            }


            match(input, Token.UP, null); 
            _walkerState.setExplicitTime((hours!=null?hours.getText():null), (minutes!=null?minutes.getText():null), (seconds!=null?seconds.getText():null), (AM_PM1!=null?AM_PM1.getText():null), (zone!=null?zone.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "explicit_time"


    // $ANTLR start "relative_time"
    // com/joestelmach/natty/generated/DateWalker.g:87:1: relative_time : ^( RELATIVE_TIME seek ) ;
    public final void relative_time() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateWalker.g:88:3: ( ^( RELATIVE_TIME seek ) )
            // com/joestelmach/natty/generated/DateWalker.g:88:5: ^( RELATIVE_TIME seek )
            {
            match(input,RELATIVE_TIME,FOLLOW_RELATIVE_TIME_in_relative_time408); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_seek_in_relative_time410);
            seek();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "relative_time"


    // $ANTLR start "seek"
    // com/joestelmach/natty/generated/DateWalker.g:91:1: seek : ( ^( SEEK DIRECTION by= SEEK_BY amount= INT ^( DAY_OF_WEEK day= INT ) ( date )? ) | ^( SEEK DIRECTION SEEK_BY amount= INT ^( MONTH_OF_YEAR month= INT ) ) | ^( SEEK DIRECTION SEEK_BY ( explicit_seek | relative_date )? INT SPAN ) | ^( SEEK DIRECTION SEEK_BY INT date ) );
    public final void seek() throws RecognitionException {
        CommonTree by=null;
        CommonTree amount=null;
        CommonTree day=null;
        CommonTree month=null;
        CommonTree DIRECTION2=null;
        CommonTree DIRECTION3=null;
        CommonTree DIRECTION4=null;
        CommonTree INT5=null;
        CommonTree SPAN6=null;
        CommonTree DIRECTION7=null;
        CommonTree INT8=null;
        CommonTree SEEK_BY9=null;

        try {
            // com/joestelmach/natty/generated/DateWalker.g:92:3: ( ^( SEEK DIRECTION by= SEEK_BY amount= INT ^( DAY_OF_WEEK day= INT ) ( date )? ) | ^( SEEK DIRECTION SEEK_BY amount= INT ^( MONTH_OF_YEAR month= INT ) ) | ^( SEEK DIRECTION SEEK_BY ( explicit_seek | relative_date )? INT SPAN ) | ^( SEEK DIRECTION SEEK_BY INT date ) )
            int alt18=4;
            alt18 = dfa18.predict(input);
            switch (alt18) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:92:5: ^( SEEK DIRECTION by= SEEK_BY amount= INT ^( DAY_OF_WEEK day= INT ) ( date )? )
                    {
                    match(input,SEEK,FOLLOW_SEEK_in_seek427); 

                    match(input, Token.DOWN, null); 
                    DIRECTION2=(CommonTree)match(input,DIRECTION,FOLLOW_DIRECTION_in_seek429); 
                    by=(CommonTree)match(input,SEEK_BY,FOLLOW_SEEK_BY_in_seek433); 
                    amount=(CommonTree)match(input,INT,FOLLOW_INT_in_seek437); 
                    match(input,DAY_OF_WEEK,FOLLOW_DAY_OF_WEEK_in_seek440); 

                    match(input, Token.DOWN, null); 
                    day=(CommonTree)match(input,INT,FOLLOW_INT_in_seek444); 

                    match(input, Token.UP, null); 
                    // com/joestelmach/natty/generated/DateWalker.g:92:68: ( date )?
                    int alt16=2;
                    switch ( input.LA(1) ) {
                        case EXPLICIT_DATE:
                        case RELATIVE_DATE:
                            {
                            alt16=1;
                            }
                            break;
                    }

                    switch (alt16) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateWalker.g:92:68: date
                            {
                            pushFollow(FOLLOW_date_in_seek448);
                            date();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                    _walkerState.seekToDayOfWeek((DIRECTION2!=null?DIRECTION2.getText():null), (by!=null?by.getText():null), (amount!=null?amount.getText():null), (day!=null?day.getText():null));

                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateWalker.g:95:5: ^( SEEK DIRECTION SEEK_BY amount= INT ^( MONTH_OF_YEAR month= INT ) )
                    {
                    match(input,SEEK,FOLLOW_SEEK_in_seek468); 

                    match(input, Token.DOWN, null); 
                    DIRECTION3=(CommonTree)match(input,DIRECTION,FOLLOW_DIRECTION_in_seek470); 
                    match(input,SEEK_BY,FOLLOW_SEEK_BY_in_seek472); 
                    amount=(CommonTree)match(input,INT,FOLLOW_INT_in_seek476); 
                    match(input,MONTH_OF_YEAR,FOLLOW_MONTH_OF_YEAR_in_seek479); 

                    match(input, Token.DOWN, null); 
                    month=(CommonTree)match(input,INT,FOLLOW_INT_in_seek483); 

                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 
                    _walkerState.seekToMonth((DIRECTION3!=null?DIRECTION3.getText():null), (amount!=null?amount.getText():null), (month!=null?month.getText():null));

                    }
                    break;
                case 3 :
                    // com/joestelmach/natty/generated/DateWalker.g:98:5: ^( SEEK DIRECTION SEEK_BY ( explicit_seek | relative_date )? INT SPAN )
                    {
                    match(input,SEEK,FOLLOW_SEEK_in_seek501); 

                    match(input, Token.DOWN, null); 
                    DIRECTION4=(CommonTree)match(input,DIRECTION,FOLLOW_DIRECTION_in_seek503); 
                    match(input,SEEK_BY,FOLLOW_SEEK_BY_in_seek505); 
                    // com/joestelmach/natty/generated/DateWalker.g:98:30: ( explicit_seek | relative_date )?
                    int alt17=3;
                    switch ( input.LA(1) ) {
                        case EXPLICIT_SEEK:
                            {
                            alt17=1;
                            }
                            break;
                        case RELATIVE_DATE:
                            {
                            alt17=2;
                            }
                            break;
                    }

                    switch (alt17) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateWalker.g:98:31: explicit_seek
                            {
                            pushFollow(FOLLOW_explicit_seek_in_seek508);
                            explicit_seek();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // com/joestelmach/natty/generated/DateWalker.g:98:47: relative_date
                            {
                            pushFollow(FOLLOW_relative_date_in_seek512);
                            relative_date();

                            state._fsp--;


                            }
                            break;

                    }

                    INT5=(CommonTree)match(input,INT,FOLLOW_INT_in_seek516); 
                    SPAN6=(CommonTree)match(input,SPAN,FOLLOW_SPAN_in_seek518); 

                    match(input, Token.UP, null); 
                    _walkerState.seekBySpan((DIRECTION4!=null?DIRECTION4.getText():null), (INT5!=null?INT5.getText():null), (SPAN6!=null?SPAN6.getText():null));

                    }
                    break;
                case 4 :
                    // com/joestelmach/natty/generated/DateWalker.g:101:5: ^( SEEK DIRECTION SEEK_BY INT date )
                    {
                    match(input,SEEK,FOLLOW_SEEK_in_seek535); 

                    match(input, Token.DOWN, null); 
                    DIRECTION7=(CommonTree)match(input,DIRECTION,FOLLOW_DIRECTION_in_seek537); 
                    SEEK_BY9=(CommonTree)match(input,SEEK_BY,FOLLOW_SEEK_BY_in_seek539); 
                    INT8=(CommonTree)match(input,INT,FOLLOW_INT_in_seek541); 
                    pushFollow(FOLLOW_date_in_seek543);
                    date();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    _walkerState.seekBySpan((DIRECTION7!=null?DIRECTION7.getText():null), (INT8!=null?INT8.getText():null), (SEEK_BY9!=null?SEEK_BY9.getText():null));

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
        return ;
    }
    // $ANTLR end "seek"


    // $ANTLR start "explicit_seek"
    // com/joestelmach/natty/generated/DateWalker.g:105:1: explicit_seek : ( ^( EXPLICIT_SEEK ^( MONTH_OF_YEAR day= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_MONTH month= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_WEEK day= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_YEAR day= INT ) ) | ^( EXPLICIT_SEEK ^( YEAR_OF year= INT ) ) | ^( EXPLICIT_SEEK index= INT ^( DAY_OF_WEEK day= INT ) ) | ^( EXPLICIT_SEEK explicit_time ) );
    public final void explicit_seek() throws RecognitionException {
        CommonTree day=null;
        CommonTree month=null;
        CommonTree year=null;
        CommonTree index=null;

        try {
            // com/joestelmach/natty/generated/DateWalker.g:106:3: ( ^( EXPLICIT_SEEK ^( MONTH_OF_YEAR day= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_MONTH month= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_WEEK day= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_YEAR day= INT ) ) | ^( EXPLICIT_SEEK ^( YEAR_OF year= INT ) ) | ^( EXPLICIT_SEEK index= INT ^( DAY_OF_WEEK day= INT ) ) | ^( EXPLICIT_SEEK explicit_time ) )
            int alt19=7;
            alt19 = dfa19.predict(input);
            switch (alt19) {
                case 1 :
                    // com/joestelmach/natty/generated/DateWalker.g:106:5: ^( EXPLICIT_SEEK ^( MONTH_OF_YEAR day= INT ) )
                    {
                    match(input,EXPLICIT_SEEK,FOLLOW_EXPLICIT_SEEK_in_explicit_seek566); 

                    match(input, Token.DOWN, null); 
                    match(input,MONTH_OF_YEAR,FOLLOW_MONTH_OF_YEAR_in_explicit_seek569); 

                    match(input, Token.DOWN, null); 
                    day=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_seek573); 

                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 
                    _walkerState.seekToMonth(">", "0", (day!=null?day.getText():null));

                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateWalker.g:109:5: ^( EXPLICIT_SEEK ^( DAY_OF_MONTH month= INT ) )
                    {
                    match(input,EXPLICIT_SEEK,FOLLOW_EXPLICIT_SEEK_in_explicit_seek593); 

                    match(input, Token.DOWN, null); 
                    match(input,DAY_OF_MONTH,FOLLOW_DAY_OF_MONTH_in_explicit_seek596); 

                    match(input, Token.DOWN, null); 
                    month=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_seek600); 

                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 
                    _walkerState.seekToDayOfMonth((month!=null?month.getText():null));

                    }
                    break;
                case 3 :
                    // com/joestelmach/natty/generated/DateWalker.g:112:5: ^( EXPLICIT_SEEK ^( DAY_OF_WEEK day= INT ) )
                    {
                    match(input,EXPLICIT_SEEK,FOLLOW_EXPLICIT_SEEK_in_explicit_seek620); 

                    match(input, Token.DOWN, null); 
                    match(input,DAY_OF_WEEK,FOLLOW_DAY_OF_WEEK_in_explicit_seek623); 

                    match(input, Token.DOWN, null); 
                    day=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_seek627); 

                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 
                    _walkerState.seekToDayOfWeek(">", "by_week", "0", (day!=null?day.getText():null));

                    }
                    break;
                case 4 :
                    // com/joestelmach/natty/generated/DateWalker.g:115:5: ^( EXPLICIT_SEEK ^( DAY_OF_YEAR day= INT ) )
                    {
                    match(input,EXPLICIT_SEEK,FOLLOW_EXPLICIT_SEEK_in_explicit_seek647); 

                    match(input, Token.DOWN, null); 
                    match(input,DAY_OF_YEAR,FOLLOW_DAY_OF_YEAR_in_explicit_seek650); 

                    match(input, Token.DOWN, null); 
                    day=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_seek654); 

                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 
                    _walkerState.seekToDayOfYear((day!=null?day.getText():null));

                    }
                    break;
                case 5 :
                    // com/joestelmach/natty/generated/DateWalker.g:118:5: ^( EXPLICIT_SEEK ^( YEAR_OF year= INT ) )
                    {
                    match(input,EXPLICIT_SEEK,FOLLOW_EXPLICIT_SEEK_in_explicit_seek674); 

                    match(input, Token.DOWN, null); 
                    match(input,YEAR_OF,FOLLOW_YEAR_OF_in_explicit_seek677); 

                    match(input, Token.DOWN, null); 
                    year=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_seek681); 

                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 
                    _walkerState.seekToYear((year!=null?year.getText():null));

                    }
                    break;
                case 6 :
                    // com/joestelmach/natty/generated/DateWalker.g:121:5: ^( EXPLICIT_SEEK index= INT ^( DAY_OF_WEEK day= INT ) )
                    {
                    match(input,EXPLICIT_SEEK,FOLLOW_EXPLICIT_SEEK_in_explicit_seek701); 

                    match(input, Token.DOWN, null); 
                    index=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_seek705); 
                    match(input,DAY_OF_WEEK,FOLLOW_DAY_OF_WEEK_in_explicit_seek708); 

                    match(input, Token.DOWN, null); 
                    day=(CommonTree)match(input,INT,FOLLOW_INT_in_explicit_seek712); 

                    match(input, Token.UP, null); 

                    match(input, Token.UP, null); 
                    _walkerState.setDayOfWeekIndex((index!=null?index.getText():null), (day!=null?day.getText():null));

                    }
                    break;
                case 7 :
                    // com/joestelmach/natty/generated/DateWalker.g:124:5: ^( EXPLICIT_SEEK explicit_time )
                    {
                    match(input,EXPLICIT_SEEK,FOLLOW_EXPLICIT_SEEK_in_explicit_seek732); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_explicit_time_in_explicit_seek734);
                    explicit_time();

                    state._fsp--;


                    match(input, Token.UP, null); 

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
        return ;
    }
    // $ANTLR end "explicit_seek"

    // Delegated rules


    protected DFA18 dfa18 = new DFA18(this);
    protected DFA19 dfa19 = new DFA19(this);
    static final String DFA18_eotS =
        "\12\uffff";
    static final String DFA18_eofS =
        "\12\uffff";
    static final String DFA18_minS =
        "\1\u016c\1\2\1\u016d\1\u016e\1\u00de\1\u0163\4\uffff";
    static final String DFA18_maxS =
        "\1\u016c\1\2\1\u016d\1\u016e\1\u016f\1\u0170\4\uffff";
    static final String DFA18_acceptS =
        "\6\uffff\1\3\1\1\1\2\1\4";
    static final String DFA18_specialS =
        "\12\uffff}>";
    static final String[] DFA18_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3",
            "\1\4",
            "\1\5\u008c\uffff\1\6\3\uffff\1\6",
            "\1\10\1\uffff\1\7\4\uffff\2\11\4\uffff\1\6",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA18_eot = DFA.unpackEncodedString(DFA18_eotS);
    static final short[] DFA18_eof = DFA.unpackEncodedString(DFA18_eofS);
    static final char[] DFA18_min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
    static final char[] DFA18_max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
    static final short[] DFA18_accept = DFA.unpackEncodedString(DFA18_acceptS);
    static final short[] DFA18_special = DFA.unpackEncodedString(DFA18_specialS);
    static final short[][] DFA18_transition;

    static {
        int numStates = DFA18_transitionS.length;
        DFA18_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA18_transition[i] = DFA.unpackEncodedString(DFA18_transitionS[i]);
        }
    }

    class DFA18 extends DFA {

        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA18_eot;
            this.eof = DFA18_eof;
            this.min = DFA18_min;
            this.max = DFA18_max;
            this.accept = DFA18_accept;
            this.special = DFA18_special;
            this.transition = DFA18_transition;
        }
        public String getDescription() {
            return "91:1: seek : ( ^( SEEK DIRECTION by= SEEK_BY amount= INT ^( DAY_OF_WEEK day= INT ) ( date )? ) | ^( SEEK DIRECTION SEEK_BY amount= INT ^( MONTH_OF_YEAR month= INT ) ) | ^( SEEK DIRECTION SEEK_BY ( explicit_seek | relative_date )? INT SPAN ) | ^( SEEK DIRECTION SEEK_BY INT date ) );";
        }
    }
    static final String DFA19_eotS =
        "\12\uffff";
    static final String DFA19_eofS =
        "\12\uffff";
    static final String DFA19_minS =
        "\1\u016f\1\2\1\u00de\7\uffff";
    static final String DFA19_maxS =
        "\1\u016f\1\2\1\u0171\7\uffff";
    static final String DFA19_acceptS =
        "\3\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7";
    static final String DFA19_specialS =
        "\12\uffff}>";
    static final String[] DFA19_transitionS = {
            "\1\1",
            "\1\2",
            "\1\10\u0084\uffff\1\3\1\4\1\5\1\6\1\7\11\uffff\1\11",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }
        public String getDescription() {
            return "105:1: explicit_seek : ( ^( EXPLICIT_SEEK ^( MONTH_OF_YEAR day= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_MONTH month= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_WEEK day= INT ) ) | ^( EXPLICIT_SEEK ^( DAY_OF_YEAR day= INT ) ) | ^( EXPLICIT_SEEK ^( YEAR_OF year= INT ) ) | ^( EXPLICIT_SEEK index= INT ^( DAY_OF_WEEK day= INT ) ) | ^( EXPLICIT_SEEK explicit_time ) );";
        }
    }
 

    public static final BitSet FOLLOW_date_time_alternative_in_parse45 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0200000000000000L});
    public static final BitSet FOLLOW_recurrence_in_parse47 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RECURRENCE_in_recurrence71 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_date_time_in_recurrence73 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DATE_TIME_ALTERNATIVE_in_date_time_alternative92 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_date_time_in_date_time_alternative94 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0400010000000000L});
    public static final BitSet FOLLOW_is_interval_in_date_time_alternative97 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IS_INTERVAL_in_is_interval113 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DATE_TIME_in_date_time137 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_date_in_date_time139 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0006000000000000L});
    public static final BitSet FOLLOW_time_in_date_time142 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_relative_date_in_date161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_explicit_date_in_date168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RELATIVE_DATE_in_relative_date184 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_seek_in_relative_date186 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_explicit_seek_in_relative_date189 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_WEEK_INDEX_in_week_index207 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_week_index211 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_DAY_OF_WEEK_in_week_index214 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_week_index218 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPLICIT_DATE_in_explicit_date242 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MONTH_OF_YEAR_in_explicit_date245 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_date249 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DAY_OF_MONTH_in_explicit_date253 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_date257 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DAY_OF_WEEK_in_explicit_date271 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_date275 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_YEAR_OF_in_explicit_date282 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_date286 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_explicit_time_in_time311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_relative_time_in_time317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXPLICIT_TIME_in_explicit_time333 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_HOURS_OF_DAY_in_explicit_time336 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_time340 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MINUTES_OF_HOUR_in_explicit_time344 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_time348 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SECONDS_OF_MINUTE_in_explicit_time362 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_time366 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AM_PM_in_explicit_time371 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_ZONE_in_explicit_time377 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ZONE_OFFSET_in_explicit_time383 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATIVE_TIME_in_relative_time408 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_seek_in_relative_time410 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEEK_in_seek427 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIRECTION_in_seek429 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_SEEK_BY_in_seek433 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_INT_in_seek437 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_DAY_OF_WEEK_in_seek440 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_seek444 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_date_in_seek448 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEEK_in_seek468 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIRECTION_in_seek470 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_SEEK_BY_in_seek472 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_INT_in_seek476 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_MONTH_OF_YEAR_in_seek479 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_seek483 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEEK_in_seek501 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIRECTION_in_seek503 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_SEEK_BY_in_seek505 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L,0x0000000000000000L,0x0000880000000000L});
    public static final BitSet FOLLOW_explicit_seek_in_seek508 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_relative_date_in_seek512 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_INT_in_seek516 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_SPAN_in_seek518 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEEK_in_seek535 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIRECTION_in_seek537 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_SEEK_BY_in_seek539 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_INT_in_seek541 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x00000C0000000000L});
    public static final BitSet FOLLOW_date_in_seek543 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPLICIT_SEEK_in_explicit_seek566 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MONTH_OF_YEAR_in_explicit_seek569 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_seek573 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPLICIT_SEEK_in_explicit_seek593 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DAY_OF_MONTH_in_explicit_seek596 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_seek600 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPLICIT_SEEK_in_explicit_seek620 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DAY_OF_WEEK_in_explicit_seek623 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_seek627 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPLICIT_SEEK_in_explicit_seek647 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DAY_OF_YEAR_in_explicit_seek650 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_seek654 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPLICIT_SEEK_in_explicit_seek674 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_YEAR_OF_in_explicit_seek677 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_seek681 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPLICIT_SEEK_in_explicit_seek701 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_seek705 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_DAY_OF_WEEK_in_explicit_seek708 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_explicit_seek712 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPLICIT_SEEK_in_explicit_seek732 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_explicit_time_in_explicit_seek734 = new BitSet(new long[]{0x0000000000000008L});

}