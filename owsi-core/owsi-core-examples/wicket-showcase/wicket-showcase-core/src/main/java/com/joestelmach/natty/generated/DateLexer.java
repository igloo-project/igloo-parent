// $ANTLR 3.2 Sep 23, 2009 14:05:07 com/joestelmach/natty/generated/DateLexer.g 2012-02-13 15:09:07
 package com.joestelmach.natty.generated; 

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class DateLexer extends Lexer {
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
    public static final int UN=181;
    public static final int AOUT=12;
    public static final int ET=54;
    public static final int HEURE=24;
    public static final int PREMIER=199;
    public static final int QUATORZE=194;
    public static final int A=41;
    public static final int PASSE=43;
    public static final int DEMAIN=33;
    public static final int WEEKEND=38;
    public static final int SEPTIEME=205;
    public static final int SIX=186;
    public static final int JEUDI=21;
    public static final int SEPT=187;
    public static final int JOUR=27;
    public static final int FEVRIER=6;
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
    public static final int HUIT=188;
    public static final int JUIN=10;
    public static final int INT_20=101;
    public static final int INT_21=102;
    public static final int CHAQUE=39;
    public static final int MARS=7;
    public static final int COLON=67;
    public static final int INT_16=97;
    public static final int INT_15=96;
    public static final int INT_18=99;
    public static final int INT_17=98;
    public static final int INT_12=93;
    public static final int INT_11=92;
    public static final int INT_14=95;
    public static final int INT_13=94;
    public static final int DEBUT=64;
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
    public static final int INT_72=153;
    public static final int INT_71=152;
    public static final int INT_70=151;
    public static final int INT_76=157;
    public static final int QUINZE=195;
    public static final int INT_75=156;
    public static final int INT_74=155;
    public static final int INT_73=154;
    public static final int SUIVANT=60;
    public static final int INT_79=160;
    public static final int INT_77=158;
    public static final int INT_78=159;
    public static final int INT_63=144;
    public static final int SEIZE=196;
    public static final int INT_62=143;
    public static final int INT_65=146;
    public static final int INT_64=145;
    public static final int JUILLET=11;
    public static final int INT_61=142;
    public static final int INT_60=141;
    public static final int SIXIEME=204;
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
    public static final int OU=53;
    public static final int MOIS=29;
    public static final int TRENTIEME=216;
    public static final int DERNIER=61;
    public static final int HEURE_SHORT=25;
    public static final int INT_59=140;
    public static final int INT_57=138;
    public static final int INT_58=139;
    public static final int INT_55=136;
    public static final int MARDI=19;
    public static final int INT_56=137;
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
    public static final int NEUF=189;
    public static final int ONZIEME=209;
    public static final int SOIR=47;
    public static final int QUATORZIEME=212;
    public static final int QUATRE=184;
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

      private java.util.logging.Logger _logger = java.util.logging.Logger.getLogger("com.joestelmach.natty");
      
      public void displayRecognitionError(String[] tokenNames, RecognitionException re) {
        String message = getErrorHeader(re);
        try { message += getErrorMessage(re, tokenNames); } catch(Exception e) {}
        _logger.fine(message);
      }


    // delegates
    // delegators

    public DateLexer() {;} 
    public DateLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public DateLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "com/joestelmach/natty/generated/DateLexer.g"; }

    // $ANTLR start "JANVIER"
    public final void mJANVIER() throws RecognitionException {
        try {
            int _type = JANVIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:17:11: ( 'janvier' | 'jan' ( DOT )? )
            int alt2=2;
            switch ( input.LA(1) ) {
            case 'j':
                {
                switch ( input.LA(2) ) {
                case 'a':
                    {
                    switch ( input.LA(3) ) {
                    case 'n':
                        {
                        switch ( input.LA(4) ) {
                        case 'v':
                            {
                            alt2=1;
                            }
                            break;
                        default:
                            alt2=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:17:13: 'janvier'
                    {
                    match("janvier"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:17:27: 'jan' ( DOT )?
                    {
                    match("jan"); 

                    // com/joestelmach/natty/generated/DateLexer.g:17:33: ( DOT )?
                    int alt1=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt1=1;
                            }
                            break;
                    }

                    switch (alt1) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:17:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JANVIER"

    // $ANTLR start "FEVRIER"
    public final void mFEVRIER() throws RecognitionException {
        try {
            int _type = FEVRIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:18:11: ( 'février' | 'fev' ( DOT )? )
            int alt4=2;
            switch ( input.LA(1) ) {
            case 'f':
                {
                switch ( input.LA(2) ) {
                case '\u00E9':
                    {
                    alt4=1;
                    }
                    break;
                case 'e':
                    {
                    alt4=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:18:13: 'février'
                    {
                    match("février"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:18:27: 'fev' ( DOT )?
                    {
                    match("fev"); 

                    // com/joestelmach/natty/generated/DateLexer.g:18:33: ( DOT )?
                    int alt3=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt3=1;
                            }
                            break;
                    }

                    switch (alt3) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:18:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FEVRIER"

    // $ANTLR start "MARS"
    public final void mMARS() throws RecognitionException {
        try {
            int _type = MARS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:19:11: ( 'mars' | 'mar' ( DOT )? )
            int alt6=2;
            switch ( input.LA(1) ) {
            case 'm':
                {
                switch ( input.LA(2) ) {
                case 'a':
                    {
                    switch ( input.LA(3) ) {
                    case 'r':
                        {
                        switch ( input.LA(4) ) {
                        case 's':
                            {
                            alt6=1;
                            }
                            break;
                        default:
                            alt6=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:19:13: 'mars'
                    {
                    match("mars"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:19:27: 'mar' ( DOT )?
                    {
                    match("mar"); 

                    // com/joestelmach/natty/generated/DateLexer.g:19:33: ( DOT )?
                    int alt5=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt5=1;
                            }
                            break;
                    }

                    switch (alt5) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:19:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MARS"

    // $ANTLR start "AVRIL"
    public final void mAVRIL() throws RecognitionException {
        try {
            int _type = AVRIL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:20:11: ( 'avril' | 'avr' ( DOT )? )
            int alt8=2;
            switch ( input.LA(1) ) {
            case 'a':
                {
                switch ( input.LA(2) ) {
                case 'v':
                    {
                    switch ( input.LA(3) ) {
                    case 'r':
                        {
                        switch ( input.LA(4) ) {
                        case 'i':
                            {
                            alt8=1;
                            }
                            break;
                        default:
                            alt8=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:20:13: 'avril'
                    {
                    match("avril"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:20:27: 'avr' ( DOT )?
                    {
                    match("avr"); 

                    // com/joestelmach/natty/generated/DateLexer.g:20:33: ( DOT )?
                    int alt7=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt7=1;
                            }
                            break;
                    }

                    switch (alt7) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:20:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AVRIL"

    // $ANTLR start "MAI"
    public final void mMAI() throws RecognitionException {
        try {
            int _type = MAI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:21:11: ( 'mai' )
            // com/joestelmach/natty/generated/DateLexer.g:21:13: 'mai'
            {
            match("mai"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MAI"

    // $ANTLR start "JUIN"
    public final void mJUIN() throws RecognitionException {
        try {
            int _type = JUIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:22:11: ( 'juin' )
            // com/joestelmach/natty/generated/DateLexer.g:22:13: 'juin'
            {
            match("juin"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JUIN"

    // $ANTLR start "JUILLET"
    public final void mJUILLET() throws RecognitionException {
        try {
            int _type = JUILLET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:23:11: ( 'juillet' | 'juil' ( DOT )? )
            int alt10=2;
            switch ( input.LA(1) ) {
            case 'j':
                {
                switch ( input.LA(2) ) {
                case 'u':
                    {
                    switch ( input.LA(3) ) {
                    case 'i':
                        {
                        switch ( input.LA(4) ) {
                        case 'l':
                            {
                            switch ( input.LA(5) ) {
                            case 'l':
                                {
                                alt10=1;
                                }
                                break;
                            default:
                                alt10=2;}

                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 10, 3, input);

                            throw nvae;
                        }

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:23:13: 'juillet'
                    {
                    match("juillet"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:23:27: 'juil' ( DOT )?
                    {
                    match("juil"); 

                    // com/joestelmach/natty/generated/DateLexer.g:23:34: ( DOT )?
                    int alt9=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt9=1;
                            }
                            break;
                    }

                    switch (alt9) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:23:34: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JUILLET"

    // $ANTLR start "AOUT"
    public final void mAOUT() throws RecognitionException {
        try {
            int _type = AOUT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:24:11: ( 'août' | 'aou' ( DOT )? )
            int alt12=2;
            switch ( input.LA(1) ) {
            case 'a':
                {
                switch ( input.LA(2) ) {
                case 'o':
                    {
                    switch ( input.LA(3) ) {
                    case '\u00FB':
                        {
                        alt12=1;
                        }
                        break;
                    case 'u':
                        {
                        alt12=2;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 12, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }

            switch (alt12) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:24:13: 'août'
                    {
                    match("août"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:24:27: 'aou' ( DOT )?
                    {
                    match("aou"); 

                    // com/joestelmach/natty/generated/DateLexer.g:24:33: ( DOT )?
                    int alt11=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt11=1;
                            }
                            break;
                    }

                    switch (alt11) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:24:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AOUT"

    // $ANTLR start "SEPTEMBRE"
    public final void mSEPTEMBRE() throws RecognitionException {
        try {
            int _type = SEPTEMBRE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:25:11: ( 'septembre' | 'sep' ( DOT )? )
            int alt14=2;
            switch ( input.LA(1) ) {
            case 's':
                {
                switch ( input.LA(2) ) {
                case 'e':
                    {
                    switch ( input.LA(3) ) {
                    case 'p':
                        {
                        switch ( input.LA(4) ) {
                        case 't':
                            {
                            alt14=1;
                            }
                            break;
                        default:
                            alt14=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:25:13: 'septembre'
                    {
                    match("septembre"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:25:27: 'sep' ( DOT )?
                    {
                    match("sep"); 

                    // com/joestelmach/natty/generated/DateLexer.g:25:33: ( DOT )?
                    int alt13=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt13=1;
                            }
                            break;
                    }

                    switch (alt13) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:25:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEPTEMBRE"

    // $ANTLR start "OCTOBRE"
    public final void mOCTOBRE() throws RecognitionException {
        try {
            int _type = OCTOBRE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:26:11: ( 'octobre' | 'oct' ( DOT )? )
            int alt16=2;
            switch ( input.LA(1) ) {
            case 'o':
                {
                switch ( input.LA(2) ) {
                case 'c':
                    {
                    switch ( input.LA(3) ) {
                    case 't':
                        {
                        switch ( input.LA(4) ) {
                        case 'o':
                            {
                            alt16=1;
                            }
                            break;
                        default:
                            alt16=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:26:13: 'octobre'
                    {
                    match("octobre"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:26:27: 'oct' ( DOT )?
                    {
                    match("oct"); 

                    // com/joestelmach/natty/generated/DateLexer.g:26:33: ( DOT )?
                    int alt15=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt15=1;
                            }
                            break;
                    }

                    switch (alt15) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:26:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OCTOBRE"

    // $ANTLR start "NOVEMBRE"
    public final void mNOVEMBRE() throws RecognitionException {
        try {
            int _type = NOVEMBRE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:27:11: ( 'novembre' | 'nov' ( DOT )? )
            int alt18=2;
            switch ( input.LA(1) ) {
            case 'n':
                {
                switch ( input.LA(2) ) {
                case 'o':
                    {
                    switch ( input.LA(3) ) {
                    case 'v':
                        {
                        switch ( input.LA(4) ) {
                        case 'e':
                            {
                            alt18=1;
                            }
                            break;
                        default:
                            alt18=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:27:13: 'novembre'
                    {
                    match("novembre"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:27:27: 'nov' ( DOT )?
                    {
                    match("nov"); 

                    // com/joestelmach/natty/generated/DateLexer.g:27:33: ( DOT )?
                    int alt17=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt17=1;
                            }
                            break;
                    }

                    switch (alt17) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:27:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOVEMBRE"

    // $ANTLR start "DECEMBRE"
    public final void mDECEMBRE() throws RecognitionException {
        try {
            int _type = DECEMBRE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:28:11: ( 'décembre' | 'dec' ( DOT )? )
            int alt20=2;
            switch ( input.LA(1) ) {
            case 'd':
                {
                switch ( input.LA(2) ) {
                case '\u00E9':
                    {
                    alt20=1;
                    }
                    break;
                case 'e':
                    {
                    alt20=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:28:13: 'décembre'
                    {
                    match("décembre"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:28:27: 'dec' ( DOT )?
                    {
                    match("dec"); 

                    // com/joestelmach/natty/generated/DateLexer.g:28:33: ( DOT )?
                    int alt19=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt19=1;
                            }
                            break;
                    }

                    switch (alt19) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:28:33: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DECEMBRE"

    // $ANTLR start "DIMANCHE"
    public final void mDIMANCHE() throws RecognitionException {
        try {
            int _type = DIMANCHE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:30:11: ( 'dimanche' ( 's' )? | 'dim' ( DOT )? )
            int alt23=2;
            switch ( input.LA(1) ) {
            case 'd':
                {
                switch ( input.LA(2) ) {
                case 'i':
                    {
                    switch ( input.LA(3) ) {
                    case 'm':
                        {
                        switch ( input.LA(4) ) {
                        case 'a':
                            {
                            alt23=1;
                            }
                            break;
                        default:
                            alt23=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 23, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 23, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:30:13: 'dimanche' ( 's' )?
                    {
                    match("dimanche"); 

                    // com/joestelmach/natty/generated/DateLexer.g:30:26: ( 's' )?
                    int alt21=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt21=1;
                            }
                            break;
                    }

                    switch (alt21) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:30:26: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:30:34: 'dim' ( DOT )?
                    {
                    match("dim"); 

                    // com/joestelmach/natty/generated/DateLexer.g:30:40: ( DOT )?
                    int alt22=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt22=1;
                            }
                            break;
                    }

                    switch (alt22) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:30:40: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIMANCHE"

    // $ANTLR start "LUNDI"
    public final void mLUNDI() throws RecognitionException {
        try {
            int _type = LUNDI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:31:11: ( 'lundi' ( 's' )? | 'lun' ( DOT )? )
            int alt26=2;
            switch ( input.LA(1) ) {
            case 'l':
                {
                switch ( input.LA(2) ) {
                case 'u':
                    {
                    switch ( input.LA(3) ) {
                    case 'n':
                        {
                        switch ( input.LA(4) ) {
                        case 'd':
                            {
                            alt26=1;
                            }
                            break;
                        default:
                            alt26=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }

            switch (alt26) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:31:13: 'lundi' ( 's' )?
                    {
                    match("lundi"); 

                    // com/joestelmach/natty/generated/DateLexer.g:31:26: ( 's' )?
                    int alt24=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt24=1;
                            }
                            break;
                    }

                    switch (alt24) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:31:26: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:31:34: 'lun' ( DOT )?
                    {
                    match("lun"); 

                    // com/joestelmach/natty/generated/DateLexer.g:31:40: ( DOT )?
                    int alt25=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt25=1;
                            }
                            break;
                    }

                    switch (alt25) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:31:40: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LUNDI"

    // $ANTLR start "MARDI"
    public final void mMARDI() throws RecognitionException {
        try {
            int _type = MARDI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:32:11: ( 'mardi' ( 's' )? | 'mar' ( DOT )? )
            int alt29=2;
            switch ( input.LA(1) ) {
            case 'm':
                {
                switch ( input.LA(2) ) {
                case 'a':
                    {
                    switch ( input.LA(3) ) {
                    case 'r':
                        {
                        switch ( input.LA(4) ) {
                        case 'd':
                            {
                            alt29=1;
                            }
                            break;
                        default:
                            alt29=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 29, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 29, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }

            switch (alt29) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:32:13: 'mardi' ( 's' )?
                    {
                    match("mardi"); 

                    // com/joestelmach/natty/generated/DateLexer.g:32:26: ( 's' )?
                    int alt27=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt27=1;
                            }
                            break;
                    }

                    switch (alt27) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:32:26: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:32:34: 'mar' ( DOT )?
                    {
                    match("mar"); 

                    // com/joestelmach/natty/generated/DateLexer.g:32:40: ( DOT )?
                    int alt28=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt28=1;
                            }
                            break;
                    }

                    switch (alt28) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:32:40: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MARDI"

    // $ANTLR start "MERCREDI"
    public final void mMERCREDI() throws RecognitionException {
        try {
            int _type = MERCREDI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:33:11: ( 'mercredi' ( 's' )? | 'mer' ( DOT )? )
            int alt32=2;
            switch ( input.LA(1) ) {
            case 'm':
                {
                switch ( input.LA(2) ) {
                case 'e':
                    {
                    switch ( input.LA(3) ) {
                    case 'r':
                        {
                        switch ( input.LA(4) ) {
                        case 'c':
                            {
                            alt32=1;
                            }
                            break;
                        default:
                            alt32=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 32, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }

            switch (alt32) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:33:13: 'mercredi' ( 's' )?
                    {
                    match("mercredi"); 

                    // com/joestelmach/natty/generated/DateLexer.g:33:26: ( 's' )?
                    int alt30=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt30=1;
                            }
                            break;
                    }

                    switch (alt30) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:33:26: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:33:34: 'mer' ( DOT )?
                    {
                    match("mer"); 

                    // com/joestelmach/natty/generated/DateLexer.g:33:40: ( DOT )?
                    int alt31=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt31=1;
                            }
                            break;
                    }

                    switch (alt31) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:33:40: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MERCREDI"

    // $ANTLR start "JEUDI"
    public final void mJEUDI() throws RecognitionException {
        try {
            int _type = JEUDI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:34:11: ( 'jeudi' ( 's' )? | 'jeu' ( DOT )? )
            int alt35=2;
            switch ( input.LA(1) ) {
            case 'j':
                {
                switch ( input.LA(2) ) {
                case 'e':
                    {
                    switch ( input.LA(3) ) {
                    case 'u':
                        {
                        switch ( input.LA(4) ) {
                        case 'd':
                            {
                            alt35=1;
                            }
                            break;
                        default:
                            alt35=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 35, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 35, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;
            }

            switch (alt35) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:34:13: 'jeudi' ( 's' )?
                    {
                    match("jeudi"); 

                    // com/joestelmach/natty/generated/DateLexer.g:34:26: ( 's' )?
                    int alt33=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt33=1;
                            }
                            break;
                    }

                    switch (alt33) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:34:26: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:34:34: 'jeu' ( DOT )?
                    {
                    match("jeu"); 

                    // com/joestelmach/natty/generated/DateLexer.g:34:40: ( DOT )?
                    int alt34=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt34=1;
                            }
                            break;
                    }

                    switch (alt34) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:34:40: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JEUDI"

    // $ANTLR start "VENDREDI"
    public final void mVENDREDI() throws RecognitionException {
        try {
            int _type = VENDREDI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:35:11: ( 'vendredi' ( 's' )? | 'ven' ( DOT )? )
            int alt38=2;
            switch ( input.LA(1) ) {
            case 'v':
                {
                switch ( input.LA(2) ) {
                case 'e':
                    {
                    switch ( input.LA(3) ) {
                    case 'n':
                        {
                        switch ( input.LA(4) ) {
                        case 'd':
                            {
                            alt38=1;
                            }
                            break;
                        default:
                            alt38=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 38, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 38, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }

            switch (alt38) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:35:13: 'vendredi' ( 's' )?
                    {
                    match("vendredi"); 

                    // com/joestelmach/natty/generated/DateLexer.g:35:26: ( 's' )?
                    int alt36=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt36=1;
                            }
                            break;
                    }

                    switch (alt36) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:35:26: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:35:34: 'ven' ( DOT )?
                    {
                    match("ven"); 

                    // com/joestelmach/natty/generated/DateLexer.g:35:40: ( DOT )?
                    int alt37=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt37=1;
                            }
                            break;
                    }

                    switch (alt37) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:35:40: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VENDREDI"

    // $ANTLR start "SAMEDI"
    public final void mSAMEDI() throws RecognitionException {
        try {
            int _type = SAMEDI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:36:11: ( 'samedi' ( 's' )? | 'sam' ( DOT )? )
            int alt41=2;
            switch ( input.LA(1) ) {
            case 's':
                {
                switch ( input.LA(2) ) {
                case 'a':
                    {
                    switch ( input.LA(3) ) {
                    case 'm':
                        {
                        switch ( input.LA(4) ) {
                        case 'e':
                            {
                            alt41=1;
                            }
                            break;
                        default:
                            alt41=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 41, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 41, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;
            }

            switch (alt41) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:36:13: 'samedi' ( 's' )?
                    {
                    match("samedi"); 

                    // com/joestelmach/natty/generated/DateLexer.g:36:26: ( 's' )?
                    int alt39=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt39=1;
                            }
                            break;
                    }

                    switch (alt39) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:36:26: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:36:34: 'sam' ( DOT )?
                    {
                    match("sam"); 

                    // com/joestelmach/natty/generated/DateLexer.g:36:40: ( DOT )?
                    int alt40=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt40=1;
                            }
                            break;
                    }

                    switch (alt40) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:36:40: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SAMEDI"

    // $ANTLR start "HEURE"
    public final void mHEURE() throws RecognitionException {
        try {
            int _type = HEURE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:38:13: ( 'heure' ( 's' )? )
            // com/joestelmach/natty/generated/DateLexer.g:38:15: 'heure' ( 's' )?
            {
            match("heure"); 

            // com/joestelmach/natty/generated/DateLexer.g:38:25: ( 's' )?
            int alt42=2;
            switch ( input.LA(1) ) {
                case 's':
                    {
                    alt42=1;
                    }
                    break;
            }

            switch (alt42) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:38:25: 's'
                    {
                    match('s'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HEURE"

    // $ANTLR start "HEURE_SHORT"
    public final void mHEURE_SHORT() throws RecognitionException {
        try {
            int _type = HEURE_SHORT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:39:13: ( 'h' )
            // com/joestelmach/natty/generated/DateLexer.g:39:15: 'h'
            {
            match('h'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HEURE_SHORT"

    // $ANTLR start "MINUTE"
    public final void mMINUTE() throws RecognitionException {
        try {
            int _type = MINUTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:40:13: ( 'minute' ( 's' )? | 'min' ( 's' )? ( DOT )? )
            int alt46=2;
            switch ( input.LA(1) ) {
            case 'm':
                {
                switch ( input.LA(2) ) {
                case 'i':
                    {
                    switch ( input.LA(3) ) {
                    case 'n':
                        {
                        switch ( input.LA(4) ) {
                        case 'u':
                            {
                            alt46=1;
                            }
                            break;
                        default:
                            alt46=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 46, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 46, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 46, 0, input);

                throw nvae;
            }

            switch (alt46) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:40:15: 'minute' ( 's' )?
                    {
                    match("minute"); 

                    // com/joestelmach/natty/generated/DateLexer.g:40:25: ( 's' )?
                    int alt43=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt43=1;
                            }
                            break;
                    }

                    switch (alt43) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:40:25: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:40:32: 'min' ( 's' )? ( DOT )?
                    {
                    match("min"); 

                    // com/joestelmach/natty/generated/DateLexer.g:40:38: ( 's' )?
                    int alt44=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt44=1;
                            }
                            break;
                    }

                    switch (alt44) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:40:38: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }

                    // com/joestelmach/natty/generated/DateLexer.g:40:43: ( DOT )?
                    int alt45=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt45=1;
                            }
                            break;
                    }

                    switch (alt45) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:40:43: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUTE"

    // $ANTLR start "JOUR"
    public final void mJOUR() throws RecognitionException {
        try {
            int _type = JOUR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:41:13: ( 'jour' ( 's' )? | 'jr' ( 's' )? ( DOT )? )
            int alt50=2;
            switch ( input.LA(1) ) {
            case 'j':
                {
                switch ( input.LA(2) ) {
                case 'o':
                    {
                    alt50=1;
                    }
                    break;
                case 'r':
                    {
                    alt50=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }

            switch (alt50) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:41:15: 'jour' ( 's' )?
                    {
                    match("jour"); 

                    // com/joestelmach/natty/generated/DateLexer.g:41:25: ( 's' )?
                    int alt47=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt47=1;
                            }
                            break;
                    }

                    switch (alt47) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:41:25: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:41:32: 'jr' ( 's' )? ( DOT )?
                    {
                    match("jr"); 

                    // com/joestelmach/natty/generated/DateLexer.g:41:38: ( 's' )?
                    int alt48=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt48=1;
                            }
                            break;
                    }

                    switch (alt48) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:41:38: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }

                    // com/joestelmach/natty/generated/DateLexer.g:41:43: ( DOT )?
                    int alt49=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt49=1;
                            }
                            break;
                    }

                    switch (alt49) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:41:43: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JOUR"

    // $ANTLR start "SEMAINE"
    public final void mSEMAINE() throws RecognitionException {
        try {
            int _type = SEMAINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:42:13: ( 'semaine' ( 's' )? | 'sem' ( 's' )? ( DOT )? )
            int alt54=2;
            switch ( input.LA(1) ) {
            case 's':
                {
                switch ( input.LA(2) ) {
                case 'e':
                    {
                    switch ( input.LA(3) ) {
                    case 'm':
                        {
                        switch ( input.LA(4) ) {
                        case 'a':
                            {
                            alt54=1;
                            }
                            break;
                        default:
                            alt54=2;}

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 54, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }

            switch (alt54) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:42:15: 'semaine' ( 's' )?
                    {
                    match("semaine"); 

                    // com/joestelmach/natty/generated/DateLexer.g:42:25: ( 's' )?
                    int alt51=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt51=1;
                            }
                            break;
                    }

                    switch (alt51) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:42:25: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:42:32: 'sem' ( 's' )? ( DOT )?
                    {
                    match("sem"); 

                    // com/joestelmach/natty/generated/DateLexer.g:42:38: ( 's' )?
                    int alt52=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt52=1;
                            }
                            break;
                    }

                    switch (alt52) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:42:38: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }

                    // com/joestelmach/natty/generated/DateLexer.g:42:43: ( DOT )?
                    int alt53=2;
                    switch ( input.LA(1) ) {
                        case '.':
                            {
                            alt53=1;
                            }
                            break;
                    }

                    switch (alt53) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:42:43: DOT
                            {
                            mDOT(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMAINE"

    // $ANTLR start "MOIS"
    public final void mMOIS() throws RecognitionException {
        try {
            int _type = MOIS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:43:13: ( 'mois' )
            // com/joestelmach/natty/generated/DateLexer.g:43:15: 'mois'
            {
            match("mois"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MOIS"

    // $ANTLR start "ANNEE"
    public final void mANNEE() throws RecognitionException {
        try {
            int _type = ANNEE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:44:13: ( 'année' ( 's' )? | 'an' ( 's' )? )
            int alt57=2;
            switch ( input.LA(1) ) {
            case 'a':
                {
                switch ( input.LA(2) ) {
                case 'n':
                    {
                    switch ( input.LA(3) ) {
                    case 'n':
                        {
                        alt57=1;
                        }
                        break;
                    default:
                        alt57=2;}

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 57, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 57, 0, input);

                throw nvae;
            }

            switch (alt57) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:44:15: 'année' ( 's' )?
                    {
                    match("année"); 

                    // com/joestelmach/natty/generated/DateLexer.g:44:25: ( 's' )?
                    int alt55=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt55=1;
                            }
                            break;
                    }

                    switch (alt55) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:44:25: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:44:32: 'an' ( 's' )?
                    {
                    match("an"); 

                    // com/joestelmach/natty/generated/DateLexer.g:44:37: ( 's' )?
                    int alt56=2;
                    switch ( input.LA(1) ) {
                        case 's':
                            {
                            alt56=1;
                            }
                            break;
                    }

                    switch (alt56) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:44:37: 's'
                            {
                            match('s'); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANNEE"

    // $ANTLR start "AUJOURD_HUI"
    public final void mAUJOURD_HUI() throws RecognitionException {
        try {
            int _type = AUJOURD_HUI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:46:15: ( 'aujourd' SINGLE_QUOTE 'hui' )
            // com/joestelmach/natty/generated/DateLexer.g:46:17: 'aujourd' SINGLE_QUOTE 'hui'
            {
            match("aujourd"); 

            mSINGLE_QUOTE(); 
            match("hui"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AUJOURD_HUI"

    // $ANTLR start "DEMAIN"
    public final void mDEMAIN() throws RecognitionException {
        try {
            int _type = DEMAIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:47:15: ( 'demain' )
            // com/joestelmach/natty/generated/DateLexer.g:47:17: 'demain'
            {
            match("demain"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEMAIN"

    // $ANTLR start "APRES_DEMAIN"
    public final void mAPRES_DEMAIN() throws RecognitionException {
        try {
            int _type = APRES_DEMAIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:48:15: ( 'après-demain' )
            // com/joestelmach/natty/generated/DateLexer.g:48:17: 'après-demain'
            {
            match("après-demain"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "APRES_DEMAIN"

    // $ANTLR start "HIER"
    public final void mHIER() throws RecognitionException {
        try {
            int _type = HIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:49:15: ( 'hier' )
            // com/joestelmach/natty/generated/DateLexer.g:49:17: 'hier'
            {
            match("hier"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HIER"

    // $ANTLR start "AVANT_HIER"
    public final void mAVANT_HIER() throws RecognitionException {
        try {
            int _type = AVANT_HIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:50:15: ( 'avant-hier' )
            // com/joestelmach/natty/generated/DateLexer.g:50:17: 'avant-hier'
            {
            match("avant-hier"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AVANT_HIER"

    // $ANTLR start "WEEKEND"
    public final void mWEEKEND() throws RecognitionException {
        try {
            int _type = WEEKEND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:51:15: ( 'week' ( DASH )? 'end' )
            // com/joestelmach/natty/generated/DateLexer.g:51:17: 'week' ( DASH )? 'end'
            {
            match("week"); 

            // com/joestelmach/natty/generated/DateLexer.g:51:24: ( DASH )?
            int alt58=2;
            switch ( input.LA(1) ) {
                case '-':
                    {
                    alt58=1;
                    }
                    break;
            }

            switch (alt58) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:51:24: DASH
                    {
                    mDASH(); 

                    }
                    break;

            }

            match("end"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WEEKEND"

    // $ANTLR start "CHAQUE"
    public final void mCHAQUE() throws RecognitionException {
        try {
            int _type = CHAQUE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:55:9: ( 'chaque' )
            // com/joestelmach/natty/generated/DateLexer.g:55:11: 'chaque'
            {
            match("chaque"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CHAQUE"

    // $ANTLR start "JUSQU_A"
    public final void mJUSQU_A() throws RecognitionException {
        try {
            int _type = JUSQU_A;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:56:9: ( 'jusqu' SINGLE_QUOTE ( 'à' | 'a' | 'au' ) )
            // com/joestelmach/natty/generated/DateLexer.g:56:11: 'jusqu' SINGLE_QUOTE ( 'à' | 'a' | 'au' )
            {
            match("jusqu"); 

            mSINGLE_QUOTE(); 
            // com/joestelmach/natty/generated/DateLexer.g:56:32: ( 'à' | 'a' | 'au' )
            int alt59=3;
            switch ( input.LA(1) ) {
            case '\u00E0':
                {
                alt59=1;
                }
                break;
            case 'a':
                {
                switch ( input.LA(2) ) {
                case 'u':
                    {
                    alt59=3;
                    }
                    break;
                default:
                    alt59=2;}

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;
            }

            switch (alt59) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:56:33: 'à'
                    {
                    match('\u00E0'); 

                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:56:39: 'a'
                    {
                    match('a'); 

                    }
                    break;
                case 3 :
                    // com/joestelmach/natty/generated/DateLexer.g:56:45: 'au'
                    {
                    match("au"); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JUSQU_A"

    // $ANTLR start "A"
    public final void mA() throws RecognitionException {
        try {
            int _type = A;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:60:7: ( 'à' | 'a' )
            // com/joestelmach/natty/generated/DateLexer.g:
            {
            if ( input.LA(1)=='a'||input.LA(1)=='\u00E0' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "A"

    // $ANTLR start "APRES"
    public final void mAPRES() throws RecognitionException {
        try {
            int _type = APRES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:61:7: ( 'après' )
            // com/joestelmach/natty/generated/DateLexer.g:61:9: 'après'
            {
            match("après"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "APRES"

    // $ANTLR start "PASSE"
    public final void mPASSE() throws RecognitionException {
        try {
            int _type = PASSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:62:7: ( 'passé' ( 'é' )? ( 's' )? )
            // com/joestelmach/natty/generated/DateLexer.g:62:9: 'passé' ( 'é' )? ( 's' )?
            {
            match("passé"); 

            // com/joestelmach/natty/generated/DateLexer.g:62:17: ( 'é' )?
            int alt60=2;
            switch ( input.LA(1) ) {
                case '\u00E9':
                    {
                    alt60=1;
                    }
                    break;
            }

            switch (alt60) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:62:17: 'é'
                    {
                    match('\u00E9'); 

                    }
                    break;

            }

            // com/joestelmach/natty/generated/DateLexer.g:62:22: ( 's' )?
            int alt61=2;
            switch ( input.LA(1) ) {
                case 's':
                    {
                    alt61=1;
                    }
                    break;
            }

            switch (alt61) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:62:22: 's'
                    {
                    match('s'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PASSE"

    // $ANTLR start "MINUIT"
    public final void mMINUIT() throws RecognitionException {
        try {
            int _type = MINUIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:64:13: ( 'minuit' )
            // com/joestelmach/natty/generated/DateLexer.g:64:15: 'minuit'
            {
            match("minuit"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUIT"

    // $ANTLR start "APRES_MIDI"
    public final void mAPRES_MIDI() throws RecognitionException {
        try {
            int _type = APRES_MIDI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:65:13: ( 'après-midi' )
            // com/joestelmach/natty/generated/DateLexer.g:65:15: 'après-midi'
            {
            match("après-midi"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "APRES_MIDI"

    // $ANTLR start "MATIN"
    public final void mMATIN() throws RecognitionException {
        try {
            int _type = MATIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:66:13: ( 'matin' | 'matinée' )
            int alt62=2;
            switch ( input.LA(1) ) {
            case 'm':
                {
                switch ( input.LA(2) ) {
                case 'a':
                    {
                    switch ( input.LA(3) ) {
                    case 't':
                        {
                        switch ( input.LA(4) ) {
                        case 'i':
                            {
                            switch ( input.LA(5) ) {
                            case 'n':
                                {
                                switch ( input.LA(6) ) {
                                case '\u00E9':
                                    {
                                    alt62=2;
                                    }
                                    break;
                                default:
                                    alt62=1;}

                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("", 62, 4, input);

                                throw nvae;
                            }

                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 62, 3, input);

                            throw nvae;
                        }

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 62, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 62, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 62, 0, input);

                throw nvae;
            }

            switch (alt62) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:66:15: 'matin'
                    {
                    match("matin"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:66:25: 'matinée'
                    {
                    match("matinée"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MATIN"

    // $ANTLR start "SOIR"
    public final void mSOIR() throws RecognitionException {
        try {
            int _type = SOIR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:67:13: ( 'soir' | 'soirée' )
            int alt63=2;
            switch ( input.LA(1) ) {
            case 's':
                {
                switch ( input.LA(2) ) {
                case 'o':
                    {
                    switch ( input.LA(3) ) {
                    case 'i':
                        {
                        switch ( input.LA(4) ) {
                        case 'r':
                            {
                            switch ( input.LA(5) ) {
                            case '\u00E9':
                                {
                                alt63=2;
                                }
                                break;
                            default:
                                alt63=1;}

                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 63, 3, input);

                            throw nvae;
                        }

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 63, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 63, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 63, 0, input);

                throw nvae;
            }

            switch (alt63) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:67:15: 'soir'
                    {
                    match("soir"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:67:24: 'soirée'
                    {
                    match("soirée"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SOIR"

    // $ANTLR start "NUIT"
    public final void mNUIT() throws RecognitionException {
        try {
            int _type = NUIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:68:13: ( 'nuit' )
            // com/joestelmach/natty/generated/DateLexer.g:68:15: 'nuit'
            {
            match("nuit"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUIT"

    // $ANTLR start "PENDANT"
    public final void mPENDANT() throws RecognitionException {
        try {
            int _type = PENDANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:72:9: ( 'pendant' )
            // com/joestelmach/natty/generated/DateLexer.g:72:11: 'pendant'
            {
            match("pendant"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PENDANT"

    // $ANTLR start "DANS"
    public final void mDANS() throws RecognitionException {
        try {
            int _type = DANS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:73:9: ( 'dans' )
            // com/joestelmach/natty/generated/DateLexer.g:73:11: 'dans'
            {
            match("dans"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DANS"

    // $ANTLR start "LE"
    public final void mLE() throws RecognitionException {
        try {
            int _type = LE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:74:9: ( 'le' )
            // com/joestelmach/natty/generated/DateLexer.g:74:11: 'le'
            {
            match("le"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LE"

    // $ANTLR start "LA"
    public final void mLA() throws RecognitionException {
        try {
            int _type = LA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:75:9: ( 'la' )
            // com/joestelmach/natty/generated/DateLexer.g:75:11: 'la'
            {
            match("la"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LA"

    // $ANTLR start "OU"
    public final void mOU() throws RecognitionException {
        try {
            int _type = OU;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:76:9: ( 'ou' )
            // com/joestelmach/natty/generated/DateLexer.g:76:11: 'ou'
            {
            match("ou"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OU"

    // $ANTLR start "ET"
    public final void mET() throws RecognitionException {
        try {
            int _type = ET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:77:9: ( 'et' )
            // com/joestelmach/natty/generated/DateLexer.g:77:11: 'et'
            {
            match("et"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ET"

    // $ANTLR start "CE"
    public final void mCE() throws RecognitionException {
        try {
            int _type = CE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:78:9: ( 'ce' | 'cet' | 'cette' )
            int alt64=3;
            switch ( input.LA(1) ) {
            case 'c':
                {
                switch ( input.LA(2) ) {
                case 'e':
                    {
                    switch ( input.LA(3) ) {
                    case 't':
                        {
                        switch ( input.LA(4) ) {
                        case 't':
                            {
                            alt64=3;
                            }
                            break;
                        default:
                            alt64=2;}

                        }
                        break;
                    default:
                        alt64=1;}

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 64, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 64, 0, input);

                throw nvae;
            }

            switch (alt64) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:78:11: 'ce'
                    {
                    match("ce"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:78:18: 'cet'
                    {
                    match("cet"); 


                    }
                    break;
                case 3 :
                    // com/joestelmach/natty/generated/DateLexer.g:78:26: 'cette'
                    {
                    match("cette"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CE"

    // $ANTLR start "DE"
    public final void mDE() throws RecognitionException {
        try {
            int _type = DE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:79:9: ( 'de' )
            // com/joestelmach/natty/generated/DateLexer.g:79:11: 'de'
            {
            match("de"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DE"

    // $ANTLR start "DU"
    public final void mDU() throws RecognitionException {
        try {
            int _type = DU;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:80:9: ( 'du' )
            // com/joestelmach/natty/generated/DateLexer.g:80:11: 'du'
            {
            match("du"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DU"

    // $ANTLR start "AU"
    public final void mAU() throws RecognitionException {
        try {
            int _type = AU;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:81:9: ( 'au' )
            // com/joestelmach/natty/generated/DateLexer.g:81:11: 'au'
            {
            match("au"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AU"

    // $ANTLR start "MAINTENANT"
    public final void mMAINTENANT() throws RecognitionException {
        try {
            int _type = MAINTENANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:82:12: ( 'maintenant' )
            // com/joestelmach/natty/generated/DateLexer.g:82:14: 'maintenant'
            {
            match("maintenant"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MAINTENANT"

    // $ANTLR start "SUIVANT"
    public final void mSUIVANT() throws RecognitionException {
        try {
            int _type = SUIVANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:83:12: ( 'suivant' ( 'e' )? | 'prochain' ( 'e' )? )
            int alt67=2;
            switch ( input.LA(1) ) {
            case 's':
                {
                alt67=1;
                }
                break;
            case 'p':
                {
                alt67=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 67, 0, input);

                throw nvae;
            }

            switch (alt67) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:83:14: 'suivant' ( 'e' )?
                    {
                    match("suivant"); 

                    // com/joestelmach/natty/generated/DateLexer.g:83:24: ( 'e' )?
                    int alt65=2;
                    switch ( input.LA(1) ) {
                        case 'e':
                            {
                            alt65=1;
                            }
                            break;
                    }

                    switch (alt65) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:83:24: 'e'
                            {
                            match('e'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:83:31: 'prochain' ( 'e' )?
                    {
                    match("prochain"); 

                    // com/joestelmach/natty/generated/DateLexer.g:83:42: ( 'e' )?
                    int alt66=2;
                    switch ( input.LA(1) ) {
                        case 'e':
                            {
                            alt66=1;
                            }
                            break;
                    }

                    switch (alt66) {
                        case 1 :
                            // com/joestelmach/natty/generated/DateLexer.g:83:42: 'e'
                            {
                            match('e'); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SUIVANT"

    // $ANTLR start "DERNIER"
    public final void mDERNIER() throws RecognitionException {
        try {
            int _type = DERNIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:84:12: ( 'dernier' | 'dernière' )
            int alt68=2;
            switch ( input.LA(1) ) {
            case 'd':
                {
                switch ( input.LA(2) ) {
                case 'e':
                    {
                    switch ( input.LA(3) ) {
                    case 'r':
                        {
                        switch ( input.LA(4) ) {
                        case 'n':
                            {
                            switch ( input.LA(5) ) {
                            case 'i':
                                {
                                switch ( input.LA(6) ) {
                                case 'e':
                                    {
                                    alt68=1;
                                    }
                                    break;
                                case '\u00E8':
                                    {
                                    alt68=2;
                                    }
                                    break;
                                default:
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 68, 5, input);

                                    throw nvae;
                                }

                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("", 68, 4, input);

                                throw nvae;
                            }

                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 68, 3, input);

                            throw nvae;
                        }

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 68, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 68, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                throw nvae;
            }

            switch (alt68) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:84:14: 'dernier'
                    {
                    match("dernier"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:84:26: 'dernière'
                    {
                    match("dernière"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DERNIER"

    // $ANTLR start "AVANT"
    public final void mAVANT() throws RecognitionException {
        try {
            int _type = AVANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:85:12: ( 'avant' | 'plus' WHITE_SPACE 'tôt' )
            int alt69=2;
            switch ( input.LA(1) ) {
            case 'a':
                {
                alt69=1;
                }
                break;
            case 'p':
                {
                alt69=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;
            }

            switch (alt69) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:85:14: 'avant'
                    {
                    match("avant"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:85:24: 'plus' WHITE_SPACE 'tôt'
                    {
                    match("plus"); 

                    mWHITE_SPACE(); 
                    match("tôt"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AVANT"

    // $ANTLR start "DEBUT"
    public final void mDEBUT() throws RecognitionException {
        try {
            int _type = DEBUT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:86:12: ( 'début' )
            // com/joestelmach/natty/generated/DateLexer.g:86:14: 'début'
            {
            match("début"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEBUT"

    // $ANTLR start "FIN"
    public final void mFIN() throws RecognitionException {
        try {
            int _type = FIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:87:12: ( 'fin' )
            // com/joestelmach/natty/generated/DateLexer.g:87:14: 'fin'
            {
            match("fin"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FIN"

    // $ANTLR start "IL_Y_A"
    public final void mIL_Y_A() throws RecognitionException {
        try {
            int _type = IL_Y_A;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:88:12: ( 'il' WHITE_SPACE 'y' WHITE_SPACE A )
            // com/joestelmach/natty/generated/DateLexer.g:88:14: 'il' WHITE_SPACE 'y' WHITE_SPACE A
            {
            match("il"); 

            mWHITE_SPACE(); 
            match('y'); 
            mWHITE_SPACE(); 
            mA(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IL_Y_A"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:92:7: ( ':' )
            // com/joestelmach/natty/generated/DateLexer.g:92:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COLON"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:93:7: ( ',' )
            // com/joestelmach/natty/generated/DateLexer.g:93:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "DASH"
    public final void mDASH() throws RecognitionException {
        try {
            int _type = DASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:94:7: ( '-' )
            // com/joestelmach/natty/generated/DateLexer.g:94:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DASH"

    // $ANTLR start "SLASH"
    public final void mSLASH() throws RecognitionException {
        try {
            int _type = SLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:95:7: ( '/' )
            // com/joestelmach/natty/generated/DateLexer.g:95:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SLASH"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:96:7: ( '.' )
            // com/joestelmach/natty/generated/DateLexer.g:96:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:97:7: ( '+' )
            // com/joestelmach/natty/generated/DateLexer.g:97:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "SINGLE_QUOTE"
    public final void mSINGLE_QUOTE() throws RecognitionException {
        try {
            int _type = SINGLE_QUOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:98:14: ( '\\'' )
            // com/joestelmach/natty/generated/DateLexer.g:98:16: '\\''
            {
            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SINGLE_QUOTE"

    // $ANTLR start "INT_00"
    public final void mINT_00() throws RecognitionException {
        try {
            int _type = INT_00;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:102:8: ( '00' )
            // com/joestelmach/natty/generated/DateLexer.g:102:10: '00'
            {
            match("00"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_00"

    // $ANTLR start "INT_01"
    public final void mINT_01() throws RecognitionException {
        try {
            int _type = INT_01;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:103:8: ( '01' )
            // com/joestelmach/natty/generated/DateLexer.g:103:10: '01'
            {
            match("01"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_01"

    // $ANTLR start "INT_02"
    public final void mINT_02() throws RecognitionException {
        try {
            int _type = INT_02;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:104:8: ( '02' )
            // com/joestelmach/natty/generated/DateLexer.g:104:10: '02'
            {
            match("02"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_02"

    // $ANTLR start "INT_03"
    public final void mINT_03() throws RecognitionException {
        try {
            int _type = INT_03;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:105:8: ( '03' )
            // com/joestelmach/natty/generated/DateLexer.g:105:10: '03'
            {
            match("03"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_03"

    // $ANTLR start "INT_04"
    public final void mINT_04() throws RecognitionException {
        try {
            int _type = INT_04;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:106:8: ( '04' )
            // com/joestelmach/natty/generated/DateLexer.g:106:10: '04'
            {
            match("04"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_04"

    // $ANTLR start "INT_05"
    public final void mINT_05() throws RecognitionException {
        try {
            int _type = INT_05;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:107:8: ( '05' )
            // com/joestelmach/natty/generated/DateLexer.g:107:10: '05'
            {
            match("05"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_05"

    // $ANTLR start "INT_06"
    public final void mINT_06() throws RecognitionException {
        try {
            int _type = INT_06;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:108:8: ( '06' )
            // com/joestelmach/natty/generated/DateLexer.g:108:10: '06'
            {
            match("06"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_06"

    // $ANTLR start "INT_07"
    public final void mINT_07() throws RecognitionException {
        try {
            int _type = INT_07;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:109:8: ( '07' )
            // com/joestelmach/natty/generated/DateLexer.g:109:10: '07'
            {
            match("07"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_07"

    // $ANTLR start "INT_08"
    public final void mINT_08() throws RecognitionException {
        try {
            int _type = INT_08;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:110:8: ( '08' )
            // com/joestelmach/natty/generated/DateLexer.g:110:10: '08'
            {
            match("08"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_08"

    // $ANTLR start "INT_09"
    public final void mINT_09() throws RecognitionException {
        try {
            int _type = INT_09;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:111:8: ( '09' )
            // com/joestelmach/natty/generated/DateLexer.g:111:10: '09'
            {
            match("09"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_09"

    // $ANTLR start "INT_0"
    public final void mINT_0() throws RecognitionException {
        try {
            int _type = INT_0;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:112:8: ( '0' )
            // com/joestelmach/natty/generated/DateLexer.g:112:10: '0'
            {
            match('0'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_0"

    // $ANTLR start "INT_1"
    public final void mINT_1() throws RecognitionException {
        try {
            int _type = INT_1;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:113:8: ( '1' )
            // com/joestelmach/natty/generated/DateLexer.g:113:10: '1'
            {
            match('1'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_1"

    // $ANTLR start "INT_2"
    public final void mINT_2() throws RecognitionException {
        try {
            int _type = INT_2;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:114:8: ( '2' )
            // com/joestelmach/natty/generated/DateLexer.g:114:10: '2'
            {
            match('2'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_2"

    // $ANTLR start "INT_3"
    public final void mINT_3() throws RecognitionException {
        try {
            int _type = INT_3;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:115:8: ( '3' )
            // com/joestelmach/natty/generated/DateLexer.g:115:10: '3'
            {
            match('3'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_3"

    // $ANTLR start "INT_4"
    public final void mINT_4() throws RecognitionException {
        try {
            int _type = INT_4;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:116:8: ( '4' )
            // com/joestelmach/natty/generated/DateLexer.g:116:10: '4'
            {
            match('4'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_4"

    // $ANTLR start "INT_5"
    public final void mINT_5() throws RecognitionException {
        try {
            int _type = INT_5;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:117:8: ( '5' )
            // com/joestelmach/natty/generated/DateLexer.g:117:10: '5'
            {
            match('5'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_5"

    // $ANTLR start "INT_6"
    public final void mINT_6() throws RecognitionException {
        try {
            int _type = INT_6;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:118:8: ( '6' )
            // com/joestelmach/natty/generated/DateLexer.g:118:10: '6'
            {
            match('6'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_6"

    // $ANTLR start "INT_7"
    public final void mINT_7() throws RecognitionException {
        try {
            int _type = INT_7;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:119:8: ( '7' )
            // com/joestelmach/natty/generated/DateLexer.g:119:10: '7'
            {
            match('7'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_7"

    // $ANTLR start "INT_8"
    public final void mINT_8() throws RecognitionException {
        try {
            int _type = INT_8;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:120:8: ( '8' )
            // com/joestelmach/natty/generated/DateLexer.g:120:10: '8'
            {
            match('8'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_8"

    // $ANTLR start "INT_9"
    public final void mINT_9() throws RecognitionException {
        try {
            int _type = INT_9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:121:8: ( '9' )
            // com/joestelmach/natty/generated/DateLexer.g:121:10: '9'
            {
            match('9'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_9"

    // $ANTLR start "INT_10"
    public final void mINT_10() throws RecognitionException {
        try {
            int _type = INT_10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:122:8: ( '10' )
            // com/joestelmach/natty/generated/DateLexer.g:122:10: '10'
            {
            match("10"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_10"

    // $ANTLR start "INT_11"
    public final void mINT_11() throws RecognitionException {
        try {
            int _type = INT_11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:123:8: ( '11' )
            // com/joestelmach/natty/generated/DateLexer.g:123:10: '11'
            {
            match("11"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_11"

    // $ANTLR start "INT_12"
    public final void mINT_12() throws RecognitionException {
        try {
            int _type = INT_12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:124:8: ( '12' )
            // com/joestelmach/natty/generated/DateLexer.g:124:10: '12'
            {
            match("12"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_12"

    // $ANTLR start "INT_13"
    public final void mINT_13() throws RecognitionException {
        try {
            int _type = INT_13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:125:8: ( '13' )
            // com/joestelmach/natty/generated/DateLexer.g:125:10: '13'
            {
            match("13"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_13"

    // $ANTLR start "INT_14"
    public final void mINT_14() throws RecognitionException {
        try {
            int _type = INT_14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:126:8: ( '14' )
            // com/joestelmach/natty/generated/DateLexer.g:126:10: '14'
            {
            match("14"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_14"

    // $ANTLR start "INT_15"
    public final void mINT_15() throws RecognitionException {
        try {
            int _type = INT_15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:127:8: ( '15' )
            // com/joestelmach/natty/generated/DateLexer.g:127:10: '15'
            {
            match("15"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_15"

    // $ANTLR start "INT_16"
    public final void mINT_16() throws RecognitionException {
        try {
            int _type = INT_16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:128:8: ( '16' )
            // com/joestelmach/natty/generated/DateLexer.g:128:10: '16'
            {
            match("16"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_16"

    // $ANTLR start "INT_17"
    public final void mINT_17() throws RecognitionException {
        try {
            int _type = INT_17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:129:8: ( '17' )
            // com/joestelmach/natty/generated/DateLexer.g:129:10: '17'
            {
            match("17"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_17"

    // $ANTLR start "INT_18"
    public final void mINT_18() throws RecognitionException {
        try {
            int _type = INT_18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:130:8: ( '18' )
            // com/joestelmach/natty/generated/DateLexer.g:130:10: '18'
            {
            match("18"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_18"

    // $ANTLR start "INT_19"
    public final void mINT_19() throws RecognitionException {
        try {
            int _type = INT_19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:131:8: ( '19' )
            // com/joestelmach/natty/generated/DateLexer.g:131:10: '19'
            {
            match("19"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_19"

    // $ANTLR start "INT_20"
    public final void mINT_20() throws RecognitionException {
        try {
            int _type = INT_20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:132:8: ( '20' )
            // com/joestelmach/natty/generated/DateLexer.g:132:10: '20'
            {
            match("20"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_20"

    // $ANTLR start "INT_21"
    public final void mINT_21() throws RecognitionException {
        try {
            int _type = INT_21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:133:8: ( '21' )
            // com/joestelmach/natty/generated/DateLexer.g:133:10: '21'
            {
            match("21"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_21"

    // $ANTLR start "INT_22"
    public final void mINT_22() throws RecognitionException {
        try {
            int _type = INT_22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:134:8: ( '22' )
            // com/joestelmach/natty/generated/DateLexer.g:134:10: '22'
            {
            match("22"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_22"

    // $ANTLR start "INT_23"
    public final void mINT_23() throws RecognitionException {
        try {
            int _type = INT_23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:135:8: ( '23' )
            // com/joestelmach/natty/generated/DateLexer.g:135:10: '23'
            {
            match("23"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_23"

    // $ANTLR start "INT_24"
    public final void mINT_24() throws RecognitionException {
        try {
            int _type = INT_24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:136:8: ( '24' )
            // com/joestelmach/natty/generated/DateLexer.g:136:10: '24'
            {
            match("24"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_24"

    // $ANTLR start "INT_25"
    public final void mINT_25() throws RecognitionException {
        try {
            int _type = INT_25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:137:8: ( '25' )
            // com/joestelmach/natty/generated/DateLexer.g:137:10: '25'
            {
            match("25"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_25"

    // $ANTLR start "INT_26"
    public final void mINT_26() throws RecognitionException {
        try {
            int _type = INT_26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:138:8: ( '26' )
            // com/joestelmach/natty/generated/DateLexer.g:138:10: '26'
            {
            match("26"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_26"

    // $ANTLR start "INT_27"
    public final void mINT_27() throws RecognitionException {
        try {
            int _type = INT_27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:139:8: ( '27' )
            // com/joestelmach/natty/generated/DateLexer.g:139:10: '27'
            {
            match("27"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_27"

    // $ANTLR start "INT_28"
    public final void mINT_28() throws RecognitionException {
        try {
            int _type = INT_28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:140:8: ( '28' )
            // com/joestelmach/natty/generated/DateLexer.g:140:10: '28'
            {
            match("28"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_28"

    // $ANTLR start "INT_29"
    public final void mINT_29() throws RecognitionException {
        try {
            int _type = INT_29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:141:8: ( '29' )
            // com/joestelmach/natty/generated/DateLexer.g:141:10: '29'
            {
            match("29"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_29"

    // $ANTLR start "INT_30"
    public final void mINT_30() throws RecognitionException {
        try {
            int _type = INT_30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:142:8: ( '30' )
            // com/joestelmach/natty/generated/DateLexer.g:142:10: '30'
            {
            match("30"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_30"

    // $ANTLR start "INT_31"
    public final void mINT_31() throws RecognitionException {
        try {
            int _type = INT_31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:143:8: ( '31' )
            // com/joestelmach/natty/generated/DateLexer.g:143:10: '31'
            {
            match("31"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_31"

    // $ANTLR start "INT_32"
    public final void mINT_32() throws RecognitionException {
        try {
            int _type = INT_32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:144:8: ( '32' )
            // com/joestelmach/natty/generated/DateLexer.g:144:10: '32'
            {
            match("32"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_32"

    // $ANTLR start "INT_33"
    public final void mINT_33() throws RecognitionException {
        try {
            int _type = INT_33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:145:8: ( '33' )
            // com/joestelmach/natty/generated/DateLexer.g:145:10: '33'
            {
            match("33"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_33"

    // $ANTLR start "INT_34"
    public final void mINT_34() throws RecognitionException {
        try {
            int _type = INT_34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:146:8: ( '34' )
            // com/joestelmach/natty/generated/DateLexer.g:146:10: '34'
            {
            match("34"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_34"

    // $ANTLR start "INT_35"
    public final void mINT_35() throws RecognitionException {
        try {
            int _type = INT_35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:147:8: ( '35' )
            // com/joestelmach/natty/generated/DateLexer.g:147:10: '35'
            {
            match("35"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_35"

    // $ANTLR start "INT_36"
    public final void mINT_36() throws RecognitionException {
        try {
            int _type = INT_36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:148:8: ( '36' )
            // com/joestelmach/natty/generated/DateLexer.g:148:10: '36'
            {
            match("36"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_36"

    // $ANTLR start "INT_37"
    public final void mINT_37() throws RecognitionException {
        try {
            int _type = INT_37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:149:8: ( '37' )
            // com/joestelmach/natty/generated/DateLexer.g:149:10: '37'
            {
            match("37"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_37"

    // $ANTLR start "INT_38"
    public final void mINT_38() throws RecognitionException {
        try {
            int _type = INT_38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:150:8: ( '38' )
            // com/joestelmach/natty/generated/DateLexer.g:150:10: '38'
            {
            match("38"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_38"

    // $ANTLR start "INT_39"
    public final void mINT_39() throws RecognitionException {
        try {
            int _type = INT_39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:151:8: ( '39' )
            // com/joestelmach/natty/generated/DateLexer.g:151:10: '39'
            {
            match("39"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_39"

    // $ANTLR start "INT_40"
    public final void mINT_40() throws RecognitionException {
        try {
            int _type = INT_40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:152:8: ( '40' )
            // com/joestelmach/natty/generated/DateLexer.g:152:10: '40'
            {
            match("40"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_40"

    // $ANTLR start "INT_41"
    public final void mINT_41() throws RecognitionException {
        try {
            int _type = INT_41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:153:8: ( '41' )
            // com/joestelmach/natty/generated/DateLexer.g:153:10: '41'
            {
            match("41"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_41"

    // $ANTLR start "INT_42"
    public final void mINT_42() throws RecognitionException {
        try {
            int _type = INT_42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:154:8: ( '42' )
            // com/joestelmach/natty/generated/DateLexer.g:154:10: '42'
            {
            match("42"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_42"

    // $ANTLR start "INT_43"
    public final void mINT_43() throws RecognitionException {
        try {
            int _type = INT_43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:155:8: ( '43' )
            // com/joestelmach/natty/generated/DateLexer.g:155:10: '43'
            {
            match("43"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_43"

    // $ANTLR start "INT_44"
    public final void mINT_44() throws RecognitionException {
        try {
            int _type = INT_44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:156:8: ( '44' )
            // com/joestelmach/natty/generated/DateLexer.g:156:10: '44'
            {
            match("44"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_44"

    // $ANTLR start "INT_45"
    public final void mINT_45() throws RecognitionException {
        try {
            int _type = INT_45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:157:8: ( '45' )
            // com/joestelmach/natty/generated/DateLexer.g:157:10: '45'
            {
            match("45"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_45"

    // $ANTLR start "INT_46"
    public final void mINT_46() throws RecognitionException {
        try {
            int _type = INT_46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:158:8: ( '46' )
            // com/joestelmach/natty/generated/DateLexer.g:158:10: '46'
            {
            match("46"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_46"

    // $ANTLR start "INT_47"
    public final void mINT_47() throws RecognitionException {
        try {
            int _type = INT_47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:159:8: ( '47' )
            // com/joestelmach/natty/generated/DateLexer.g:159:10: '47'
            {
            match("47"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_47"

    // $ANTLR start "INT_48"
    public final void mINT_48() throws RecognitionException {
        try {
            int _type = INT_48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:160:8: ( '48' )
            // com/joestelmach/natty/generated/DateLexer.g:160:10: '48'
            {
            match("48"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_48"

    // $ANTLR start "INT_49"
    public final void mINT_49() throws RecognitionException {
        try {
            int _type = INT_49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:161:8: ( '49' )
            // com/joestelmach/natty/generated/DateLexer.g:161:10: '49'
            {
            match("49"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_49"

    // $ANTLR start "INT_50"
    public final void mINT_50() throws RecognitionException {
        try {
            int _type = INT_50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:162:8: ( '50' )
            // com/joestelmach/natty/generated/DateLexer.g:162:10: '50'
            {
            match("50"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_50"

    // $ANTLR start "INT_51"
    public final void mINT_51() throws RecognitionException {
        try {
            int _type = INT_51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:163:8: ( '51' )
            // com/joestelmach/natty/generated/DateLexer.g:163:10: '51'
            {
            match("51"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_51"

    // $ANTLR start "INT_52"
    public final void mINT_52() throws RecognitionException {
        try {
            int _type = INT_52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:164:8: ( '52' )
            // com/joestelmach/natty/generated/DateLexer.g:164:10: '52'
            {
            match("52"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_52"

    // $ANTLR start "INT_53"
    public final void mINT_53() throws RecognitionException {
        try {
            int _type = INT_53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:165:8: ( '53' )
            // com/joestelmach/natty/generated/DateLexer.g:165:10: '53'
            {
            match("53"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_53"

    // $ANTLR start "INT_54"
    public final void mINT_54() throws RecognitionException {
        try {
            int _type = INT_54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:166:8: ( '54' )
            // com/joestelmach/natty/generated/DateLexer.g:166:10: '54'
            {
            match("54"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_54"

    // $ANTLR start "INT_55"
    public final void mINT_55() throws RecognitionException {
        try {
            int _type = INT_55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:167:8: ( '55' )
            // com/joestelmach/natty/generated/DateLexer.g:167:10: '55'
            {
            match("55"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_55"

    // $ANTLR start "INT_56"
    public final void mINT_56() throws RecognitionException {
        try {
            int _type = INT_56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:168:8: ( '56' )
            // com/joestelmach/natty/generated/DateLexer.g:168:10: '56'
            {
            match("56"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_56"

    // $ANTLR start "INT_57"
    public final void mINT_57() throws RecognitionException {
        try {
            int _type = INT_57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:169:8: ( '57' )
            // com/joestelmach/natty/generated/DateLexer.g:169:10: '57'
            {
            match("57"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_57"

    // $ANTLR start "INT_58"
    public final void mINT_58() throws RecognitionException {
        try {
            int _type = INT_58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:170:8: ( '58' )
            // com/joestelmach/natty/generated/DateLexer.g:170:10: '58'
            {
            match("58"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_58"

    // $ANTLR start "INT_59"
    public final void mINT_59() throws RecognitionException {
        try {
            int _type = INT_59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:171:8: ( '59' )
            // com/joestelmach/natty/generated/DateLexer.g:171:10: '59'
            {
            match("59"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_59"

    // $ANTLR start "INT_60"
    public final void mINT_60() throws RecognitionException {
        try {
            int _type = INT_60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:172:8: ( '60' )
            // com/joestelmach/natty/generated/DateLexer.g:172:10: '60'
            {
            match("60"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_60"

    // $ANTLR start "INT_61"
    public final void mINT_61() throws RecognitionException {
        try {
            int _type = INT_61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:173:8: ( '61' )
            // com/joestelmach/natty/generated/DateLexer.g:173:10: '61'
            {
            match("61"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_61"

    // $ANTLR start "INT_62"
    public final void mINT_62() throws RecognitionException {
        try {
            int _type = INT_62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:174:8: ( '62' )
            // com/joestelmach/natty/generated/DateLexer.g:174:10: '62'
            {
            match("62"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_62"

    // $ANTLR start "INT_63"
    public final void mINT_63() throws RecognitionException {
        try {
            int _type = INT_63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:175:8: ( '63' )
            // com/joestelmach/natty/generated/DateLexer.g:175:10: '63'
            {
            match("63"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_63"

    // $ANTLR start "INT_64"
    public final void mINT_64() throws RecognitionException {
        try {
            int _type = INT_64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:176:8: ( '64' )
            // com/joestelmach/natty/generated/DateLexer.g:176:10: '64'
            {
            match("64"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_64"

    // $ANTLR start "INT_65"
    public final void mINT_65() throws RecognitionException {
        try {
            int _type = INT_65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:177:8: ( '65' )
            // com/joestelmach/natty/generated/DateLexer.g:177:10: '65'
            {
            match("65"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_65"

    // $ANTLR start "INT_66"
    public final void mINT_66() throws RecognitionException {
        try {
            int _type = INT_66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:178:8: ( '66' )
            // com/joestelmach/natty/generated/DateLexer.g:178:10: '66'
            {
            match("66"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_66"

    // $ANTLR start "INT_67"
    public final void mINT_67() throws RecognitionException {
        try {
            int _type = INT_67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:179:8: ( '67' )
            // com/joestelmach/natty/generated/DateLexer.g:179:10: '67'
            {
            match("67"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_67"

    // $ANTLR start "INT_68"
    public final void mINT_68() throws RecognitionException {
        try {
            int _type = INT_68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:180:8: ( '68' )
            // com/joestelmach/natty/generated/DateLexer.g:180:10: '68'
            {
            match("68"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_68"

    // $ANTLR start "INT_69"
    public final void mINT_69() throws RecognitionException {
        try {
            int _type = INT_69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:181:8: ( '69' )
            // com/joestelmach/natty/generated/DateLexer.g:181:10: '69'
            {
            match("69"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_69"

    // $ANTLR start "INT_70"
    public final void mINT_70() throws RecognitionException {
        try {
            int _type = INT_70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:182:8: ( '70' )
            // com/joestelmach/natty/generated/DateLexer.g:182:10: '70'
            {
            match("70"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_70"

    // $ANTLR start "INT_71"
    public final void mINT_71() throws RecognitionException {
        try {
            int _type = INT_71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:183:8: ( '71' )
            // com/joestelmach/natty/generated/DateLexer.g:183:10: '71'
            {
            match("71"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_71"

    // $ANTLR start "INT_72"
    public final void mINT_72() throws RecognitionException {
        try {
            int _type = INT_72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:184:8: ( '72' )
            // com/joestelmach/natty/generated/DateLexer.g:184:10: '72'
            {
            match("72"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_72"

    // $ANTLR start "INT_73"
    public final void mINT_73() throws RecognitionException {
        try {
            int _type = INT_73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:185:8: ( '73' )
            // com/joestelmach/natty/generated/DateLexer.g:185:10: '73'
            {
            match("73"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_73"

    // $ANTLR start "INT_74"
    public final void mINT_74() throws RecognitionException {
        try {
            int _type = INT_74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:186:8: ( '74' )
            // com/joestelmach/natty/generated/DateLexer.g:186:10: '74'
            {
            match("74"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_74"

    // $ANTLR start "INT_75"
    public final void mINT_75() throws RecognitionException {
        try {
            int _type = INT_75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:187:8: ( '75' )
            // com/joestelmach/natty/generated/DateLexer.g:187:10: '75'
            {
            match("75"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_75"

    // $ANTLR start "INT_76"
    public final void mINT_76() throws RecognitionException {
        try {
            int _type = INT_76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:188:8: ( '76' )
            // com/joestelmach/natty/generated/DateLexer.g:188:10: '76'
            {
            match("76"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_76"

    // $ANTLR start "INT_77"
    public final void mINT_77() throws RecognitionException {
        try {
            int _type = INT_77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:189:8: ( '77' )
            // com/joestelmach/natty/generated/DateLexer.g:189:10: '77'
            {
            match("77"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_77"

    // $ANTLR start "INT_78"
    public final void mINT_78() throws RecognitionException {
        try {
            int _type = INT_78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:190:8: ( '78' )
            // com/joestelmach/natty/generated/DateLexer.g:190:10: '78'
            {
            match("78"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_78"

    // $ANTLR start "INT_79"
    public final void mINT_79() throws RecognitionException {
        try {
            int _type = INT_79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:191:8: ( '79' )
            // com/joestelmach/natty/generated/DateLexer.g:191:10: '79'
            {
            match("79"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_79"

    // $ANTLR start "INT_80"
    public final void mINT_80() throws RecognitionException {
        try {
            int _type = INT_80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:192:8: ( '80' )
            // com/joestelmach/natty/generated/DateLexer.g:192:10: '80'
            {
            match("80"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_80"

    // $ANTLR start "INT_81"
    public final void mINT_81() throws RecognitionException {
        try {
            int _type = INT_81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:193:8: ( '81' )
            // com/joestelmach/natty/generated/DateLexer.g:193:10: '81'
            {
            match("81"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_81"

    // $ANTLR start "INT_82"
    public final void mINT_82() throws RecognitionException {
        try {
            int _type = INT_82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:194:8: ( '82' )
            // com/joestelmach/natty/generated/DateLexer.g:194:10: '82'
            {
            match("82"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_82"

    // $ANTLR start "INT_83"
    public final void mINT_83() throws RecognitionException {
        try {
            int _type = INT_83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:195:8: ( '83' )
            // com/joestelmach/natty/generated/DateLexer.g:195:10: '83'
            {
            match("83"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_83"

    // $ANTLR start "INT_84"
    public final void mINT_84() throws RecognitionException {
        try {
            int _type = INT_84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:196:8: ( '84' )
            // com/joestelmach/natty/generated/DateLexer.g:196:10: '84'
            {
            match("84"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_84"

    // $ANTLR start "INT_85"
    public final void mINT_85() throws RecognitionException {
        try {
            int _type = INT_85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:197:8: ( '85' )
            // com/joestelmach/natty/generated/DateLexer.g:197:10: '85'
            {
            match("85"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_85"

    // $ANTLR start "INT_86"
    public final void mINT_86() throws RecognitionException {
        try {
            int _type = INT_86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:198:8: ( '86' )
            // com/joestelmach/natty/generated/DateLexer.g:198:10: '86'
            {
            match("86"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_86"

    // $ANTLR start "INT_87"
    public final void mINT_87() throws RecognitionException {
        try {
            int _type = INT_87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:199:8: ( '87' )
            // com/joestelmach/natty/generated/DateLexer.g:199:10: '87'
            {
            match("87"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_87"

    // $ANTLR start "INT_88"
    public final void mINT_88() throws RecognitionException {
        try {
            int _type = INT_88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:200:8: ( '88' )
            // com/joestelmach/natty/generated/DateLexer.g:200:10: '88'
            {
            match("88"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_88"

    // $ANTLR start "INT_89"
    public final void mINT_89() throws RecognitionException {
        try {
            int _type = INT_89;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:201:8: ( '89' )
            // com/joestelmach/natty/generated/DateLexer.g:201:10: '89'
            {
            match("89"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_89"

    // $ANTLR start "INT_90"
    public final void mINT_90() throws RecognitionException {
        try {
            int _type = INT_90;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:202:8: ( '90' )
            // com/joestelmach/natty/generated/DateLexer.g:202:10: '90'
            {
            match("90"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_90"

    // $ANTLR start "INT_91"
    public final void mINT_91() throws RecognitionException {
        try {
            int _type = INT_91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:203:8: ( '91' )
            // com/joestelmach/natty/generated/DateLexer.g:203:10: '91'
            {
            match("91"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_91"

    // $ANTLR start "INT_92"
    public final void mINT_92() throws RecognitionException {
        try {
            int _type = INT_92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:204:8: ( '92' )
            // com/joestelmach/natty/generated/DateLexer.g:204:10: '92'
            {
            match("92"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_92"

    // $ANTLR start "INT_93"
    public final void mINT_93() throws RecognitionException {
        try {
            int _type = INT_93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:205:8: ( '93' )
            // com/joestelmach/natty/generated/DateLexer.g:205:10: '93'
            {
            match("93"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_93"

    // $ANTLR start "INT_94"
    public final void mINT_94() throws RecognitionException {
        try {
            int _type = INT_94;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:206:8: ( '94' )
            // com/joestelmach/natty/generated/DateLexer.g:206:10: '94'
            {
            match("94"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_94"

    // $ANTLR start "INT_95"
    public final void mINT_95() throws RecognitionException {
        try {
            int _type = INT_95;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:207:8: ( '95' )
            // com/joestelmach/natty/generated/DateLexer.g:207:10: '95'
            {
            match("95"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_95"

    // $ANTLR start "INT_96"
    public final void mINT_96() throws RecognitionException {
        try {
            int _type = INT_96;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:208:8: ( '96' )
            // com/joestelmach/natty/generated/DateLexer.g:208:10: '96'
            {
            match("96"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_96"

    // $ANTLR start "INT_97"
    public final void mINT_97() throws RecognitionException {
        try {
            int _type = INT_97;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:209:8: ( '97' )
            // com/joestelmach/natty/generated/DateLexer.g:209:10: '97'
            {
            match("97"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_97"

    // $ANTLR start "INT_98"
    public final void mINT_98() throws RecognitionException {
        try {
            int _type = INT_98;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:210:8: ( '98' )
            // com/joestelmach/natty/generated/DateLexer.g:210:10: '98'
            {
            match("98"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_98"

    // $ANTLR start "INT_99"
    public final void mINT_99() throws RecognitionException {
        try {
            int _type = INT_99;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:211:8: ( '99' )
            // com/joestelmach/natty/generated/DateLexer.g:211:10: '99'
            {
            match("99"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_99"

    // $ANTLR start "UN"
    public final void mUN() throws RecognitionException {
        try {
            int _type = UN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:213:10: ( 'un' | 'une' )
            int alt70=2;
            switch ( input.LA(1) ) {
            case 'u':
                {
                switch ( input.LA(2) ) {
                case 'n':
                    {
                    switch ( input.LA(3) ) {
                    case 'e':
                        {
                        alt70=2;
                        }
                        break;
                    default:
                        alt70=1;}

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 70, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 70, 0, input);

                throw nvae;
            }

            switch (alt70) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:213:12: 'un'
                    {
                    match("un"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:213:19: 'une'
                    {
                    match("une"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UN"

    // $ANTLR start "DEUX"
    public final void mDEUX() throws RecognitionException {
        try {
            int _type = DEUX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:214:10: ( 'deux' )
            // com/joestelmach/natty/generated/DateLexer.g:214:12: 'deux'
            {
            match("deux"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEUX"

    // $ANTLR start "TROIS"
    public final void mTROIS() throws RecognitionException {
        try {
            int _type = TROIS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:215:10: ( 'trois' )
            // com/joestelmach/natty/generated/DateLexer.g:215:12: 'trois'
            {
            match("trois"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TROIS"

    // $ANTLR start "QUATRE"
    public final void mQUATRE() throws RecognitionException {
        try {
            int _type = QUATRE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:216:10: ( 'quatre' )
            // com/joestelmach/natty/generated/DateLexer.g:216:12: 'quatre'
            {
            match("quatre"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUATRE"

    // $ANTLR start "CINQ"
    public final void mCINQ() throws RecognitionException {
        try {
            int _type = CINQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:217:10: ( 'cinq' )
            // com/joestelmach/natty/generated/DateLexer.g:217:12: 'cinq'
            {
            match("cinq"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CINQ"

    // $ANTLR start "SIX"
    public final void mSIX() throws RecognitionException {
        try {
            int _type = SIX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:218:10: ( 'six' )
            // com/joestelmach/natty/generated/DateLexer.g:218:12: 'six'
            {
            match("six"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIX"

    // $ANTLR start "SEPT"
    public final void mSEPT() throws RecognitionException {
        try {
            int _type = SEPT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:219:10: ( 'sept' )
            // com/joestelmach/natty/generated/DateLexer.g:219:12: 'sept'
            {
            match("sept"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEPT"

    // $ANTLR start "HUIT"
    public final void mHUIT() throws RecognitionException {
        try {
            int _type = HUIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:220:10: ( 'huit' )
            // com/joestelmach/natty/generated/DateLexer.g:220:12: 'huit'
            {
            match("huit"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HUIT"

    // $ANTLR start "NEUF"
    public final void mNEUF() throws RecognitionException {
        try {
            int _type = NEUF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:221:10: ( 'neuf' )
            // com/joestelmach/natty/generated/DateLexer.g:221:12: 'neuf'
            {
            match("neuf"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEUF"

    // $ANTLR start "DIX"
    public final void mDIX() throws RecognitionException {
        try {
            int _type = DIX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:222:10: ( 'dix' )
            // com/joestelmach/natty/generated/DateLexer.g:222:12: 'dix'
            {
            match("dix"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIX"

    // $ANTLR start "ONZE"
    public final void mONZE() throws RecognitionException {
        try {
            int _type = ONZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:223:10: ( 'onze' )
            // com/joestelmach/natty/generated/DateLexer.g:223:12: 'onze'
            {
            match("onze"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ONZE"

    // $ANTLR start "DOUZE"
    public final void mDOUZE() throws RecognitionException {
        try {
            int _type = DOUZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:224:10: ( 'douze' )
            // com/joestelmach/natty/generated/DateLexer.g:224:12: 'douze'
            {
            match("douze"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUZE"

    // $ANTLR start "TREIZE"
    public final void mTREIZE() throws RecognitionException {
        try {
            int _type = TREIZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:225:10: ( 'treize' )
            // com/joestelmach/natty/generated/DateLexer.g:225:12: 'treize'
            {
            match("treize"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TREIZE"

    // $ANTLR start "QUATORZE"
    public final void mQUATORZE() throws RecognitionException {
        try {
            int _type = QUATORZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:226:10: ( 'quatorze' )
            // com/joestelmach/natty/generated/DateLexer.g:226:12: 'quatorze'
            {
            match("quatorze"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUATORZE"

    // $ANTLR start "QUINZE"
    public final void mQUINZE() throws RecognitionException {
        try {
            int _type = QUINZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:227:10: ( 'quinze' )
            // com/joestelmach/natty/generated/DateLexer.g:227:12: 'quinze'
            {
            match("quinze"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUINZE"

    // $ANTLR start "SEIZE"
    public final void mSEIZE() throws RecognitionException {
        try {
            int _type = SEIZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:228:10: ( 'seize' )
            // com/joestelmach/natty/generated/DateLexer.g:228:12: 'seize'
            {
            match("seize"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEIZE"

    // $ANTLR start "VINGT"
    public final void mVINGT() throws RecognitionException {
        try {
            int _type = VINGT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:229:10: ( 'vingt' )
            // com/joestelmach/natty/generated/DateLexer.g:229:12: 'vingt'
            {
            match("vingt"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VINGT"

    // $ANTLR start "TRENTE"
    public final void mTRENTE() throws RecognitionException {
        try {
            int _type = TRENTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:230:10: ( 'trente' )
            // com/joestelmach/natty/generated/DateLexer.g:230:12: 'trente'
            {
            match("trente"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRENTE"

    // $ANTLR start "PREMIER"
    public final void mPREMIER() throws RecognitionException {
        try {
            int _type = PREMIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:232:9: ( 'premier' )
            // com/joestelmach/natty/generated/DateLexer.g:232:11: 'premier'
            {
            match("premier"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PREMIER"

    // $ANTLR start "DEUXIEME"
    public final void mDEUXIEME() throws RecognitionException {
        try {
            int _type = DEUXIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:233:10: ( 'deuxième' | 'second' )
            int alt71=2;
            switch ( input.LA(1) ) {
            case 'd':
                {
                alt71=1;
                }
                break;
            case 's':
                {
                alt71=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }

            switch (alt71) {
                case 1 :
                    // com/joestelmach/natty/generated/DateLexer.g:233:12: 'deuxième'
                    {
                    match("deuxième"); 


                    }
                    break;
                case 2 :
                    // com/joestelmach/natty/generated/DateLexer.g:233:25: 'second'
                    {
                    match("second"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEUXIEME"

    // $ANTLR start "TROISIEME"
    public final void mTROISIEME() throws RecognitionException {
        try {
            int _type = TROISIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:234:11: ( 'troisième' )
            // com/joestelmach/natty/generated/DateLexer.g:234:13: 'troisième'
            {
            match("troisième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TROISIEME"

    // $ANTLR start "QUATRIEME"
    public final void mQUATRIEME() throws RecognitionException {
        try {
            int _type = QUATRIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:235:11: ( 'quatrième' )
            // com/joestelmach/natty/generated/DateLexer.g:235:13: 'quatrième'
            {
            match("quatrième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUATRIEME"

    // $ANTLR start "CINQUIEME"
    public final void mCINQUIEME() throws RecognitionException {
        try {
            int _type = CINQUIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:236:11: ( 'cinquième' )
            // com/joestelmach/natty/generated/DateLexer.g:236:13: 'cinquième'
            {
            match("cinquième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CINQUIEME"

    // $ANTLR start "SIXIEME"
    public final void mSIXIEME() throws RecognitionException {
        try {
            int _type = SIXIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:237:9: ( 'sixième' )
            // com/joestelmach/natty/generated/DateLexer.g:237:11: 'sixième'
            {
            match("sixième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIXIEME"

    // $ANTLR start "SEPTIEME"
    public final void mSEPTIEME() throws RecognitionException {
        try {
            int _type = SEPTIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:238:10: ( 'septième' )
            // com/joestelmach/natty/generated/DateLexer.g:238:12: 'septième'
            {
            match("septième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEPTIEME"

    // $ANTLR start "HUITIEME"
    public final void mHUITIEME() throws RecognitionException {
        try {
            int _type = HUITIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:239:10: ( 'huitième' )
            // com/joestelmach/natty/generated/DateLexer.g:239:12: 'huitième'
            {
            match("huitième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HUITIEME"

    // $ANTLR start "NEUVIEME"
    public final void mNEUVIEME() throws RecognitionException {
        try {
            int _type = NEUVIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:240:10: ( 'neuvième' )
            // com/joestelmach/natty/generated/DateLexer.g:240:12: 'neuvième'
            {
            match("neuvième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEUVIEME"

    // $ANTLR start "DIXIEME"
    public final void mDIXIEME() throws RecognitionException {
        try {
            int _type = DIXIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:241:9: ( 'dixième' )
            // com/joestelmach/natty/generated/DateLexer.g:241:11: 'dixième'
            {
            match("dixième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIXIEME"

    // $ANTLR start "ONZIEME"
    public final void mONZIEME() throws RecognitionException {
        try {
            int _type = ONZIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:242:9: ( 'onzième' )
            // com/joestelmach/natty/generated/DateLexer.g:242:11: 'onzième'
            {
            match("onzième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ONZIEME"

    // $ANTLR start "DOUZIEME"
    public final void mDOUZIEME() throws RecognitionException {
        try {
            int _type = DOUZIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:243:10: ( 'douzième' )
            // com/joestelmach/natty/generated/DateLexer.g:243:12: 'douzième'
            {
            match("douzième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUZIEME"

    // $ANTLR start "TREIZIEME"
    public final void mTREIZIEME() throws RecognitionException {
        try {
            int _type = TREIZIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:244:11: ( 'treizième' )
            // com/joestelmach/natty/generated/DateLexer.g:244:13: 'treizième'
            {
            match("treizième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TREIZIEME"

    // $ANTLR start "QUATORZIEME"
    public final void mQUATORZIEME() throws RecognitionException {
        try {
            int _type = QUATORZIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:245:13: ( 'quatorzième' )
            // com/joestelmach/natty/generated/DateLexer.g:245:15: 'quatorzième'
            {
            match("quatorzième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUATORZIEME"

    // $ANTLR start "QUINZIEME"
    public final void mQUINZIEME() throws RecognitionException {
        try {
            int _type = QUINZIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:246:11: ( 'quinzième' )
            // com/joestelmach/natty/generated/DateLexer.g:246:13: 'quinzième'
            {
            match("quinzième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUINZIEME"

    // $ANTLR start "SEIZIEME"
    public final void mSEIZIEME() throws RecognitionException {
        try {
            int _type = SEIZIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:247:10: ( 'seizième' )
            // com/joestelmach/natty/generated/DateLexer.g:247:12: 'seizième'
            {
            match("seizième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEIZIEME"

    // $ANTLR start "VINGTIEME"
    public final void mVINGTIEME() throws RecognitionException {
        try {
            int _type = VINGTIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:248:11: ( 'vingtième' )
            // com/joestelmach/natty/generated/DateLexer.g:248:13: 'vingtième'
            {
            match("vingtième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VINGTIEME"

    // $ANTLR start "TRENTIEME"
    public final void mTRENTIEME() throws RecognitionException {
        try {
            int _type = TRENTIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:249:11: ( 'trentième' )
            // com/joestelmach/natty/generated/DateLexer.g:249:13: 'trentième'
            {
            match("trentième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRENTIEME"

    // $ANTLR start "UNIEME"
    public final void mUNIEME() throws RecognitionException {
        try {
            int _type = UNIEME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:250:8: ( 'unième' )
            // com/joestelmach/natty/generated/DateLexer.g:250:10: 'unième'
            {
            match("unième"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNIEME"

    // $ANTLR start "WHITE_SPACE"
    public final void mWHITE_SPACE() throws RecognitionException {
        try {
            int _type = WHITE_SPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:255:3: ( ( DOT | SPACE )+ )
            // com/joestelmach/natty/generated/DateLexer.g:255:5: ( DOT | SPACE )+
            {
            // com/joestelmach/natty/generated/DateLexer.g:255:5: ( DOT | SPACE )+
            int cnt72=0;
            loop72:
            do {
                int alt72=2;
                switch ( input.LA(1) ) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                case '.':
                    {
                    alt72=1;
                    }
                    break;

                }

                switch (alt72) {
            	case 1 :
            	    // com/joestelmach/natty/generated/DateLexer.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' '||input.LA(1)=='.' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt72 >= 1 ) break loop72;
                        EarlyExitException eee =
                            new EarlyExitException(72, input);
                        throw eee;
                }
                cnt72++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHITE_SPACE"

    // $ANTLR start "UNKNOWN"
    public final void mUNKNOWN() throws RecognitionException {
        try {
            int _type = UNKNOWN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/joestelmach/natty/generated/DateLexer.g:259:3: ( UNKNOWN_CHAR )
            // com/joestelmach/natty/generated/DateLexer.g:259:5: UNKNOWN_CHAR
            {
            mUNKNOWN_CHAR(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNKNOWN"

    // $ANTLR start "UNKNOWN_CHAR"
    public final void mUNKNOWN_CHAR() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateLexer.g:263:3: (~ ( SPACE | DOT ) )
            // com/joestelmach/natty/generated/DateLexer.g:263:5: ~ ( SPACE | DOT )
            {
            if ( (input.LA(1)>='\u0000' && input.LA(1)<='\b')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\u001F')||(input.LA(1)>='!' && input.LA(1)<='-')||(input.LA(1)>='/' && input.LA(1)<='\uFFFF') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "UNKNOWN_CHAR"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateLexer.g:266:16: ( '0' .. '9' )
            // com/joestelmach/natty/generated/DateLexer.g:266:18: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            // com/joestelmach/natty/generated/DateLexer.g:268:16: ( ' ' | '\\t' | '\\n' | '\\r' )
            // com/joestelmach/natty/generated/DateLexer.g:
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "SPACE"

    public void mTokens() throws RecognitionException {
        // com/joestelmach/natty/generated/DateLexer.g:1:8: ( JANVIER | FEVRIER | MARS | AVRIL | MAI | JUIN | JUILLET | AOUT | SEPTEMBRE | OCTOBRE | NOVEMBRE | DECEMBRE | DIMANCHE | LUNDI | MARDI | MERCREDI | JEUDI | VENDREDI | SAMEDI | HEURE | HEURE_SHORT | MINUTE | JOUR | SEMAINE | MOIS | ANNEE | AUJOURD_HUI | DEMAIN | APRES_DEMAIN | HIER | AVANT_HIER | WEEKEND | CHAQUE | JUSQU_A | A | APRES | PASSE | MINUIT | APRES_MIDI | MATIN | SOIR | NUIT | PENDANT | DANS | LE | LA | OU | ET | CE | DE | DU | AU | MAINTENANT | SUIVANT | DERNIER | AVANT | DEBUT | FIN | IL_Y_A | COLON | COMMA | DASH | SLASH | DOT | PLUS | SINGLE_QUOTE | INT_00 | INT_01 | INT_02 | INT_03 | INT_04 | INT_05 | INT_06 | INT_07 | INT_08 | INT_09 | INT_0 | INT_1 | INT_2 | INT_3 | INT_4 | INT_5 | INT_6 | INT_7 | INT_8 | INT_9 | INT_10 | INT_11 | INT_12 | INT_13 | INT_14 | INT_15 | INT_16 | INT_17 | INT_18 | INT_19 | INT_20 | INT_21 | INT_22 | INT_23 | INT_24 | INT_25 | INT_26 | INT_27 | INT_28 | INT_29 | INT_30 | INT_31 | INT_32 | INT_33 | INT_34 | INT_35 | INT_36 | INT_37 | INT_38 | INT_39 | INT_40 | INT_41 | INT_42 | INT_43 | INT_44 | INT_45 | INT_46 | INT_47 | INT_48 | INT_49 | INT_50 | INT_51 | INT_52 | INT_53 | INT_54 | INT_55 | INT_56 | INT_57 | INT_58 | INT_59 | INT_60 | INT_61 | INT_62 | INT_63 | INT_64 | INT_65 | INT_66 | INT_67 | INT_68 | INT_69 | INT_70 | INT_71 | INT_72 | INT_73 | INT_74 | INT_75 | INT_76 | INT_77 | INT_78 | INT_79 | INT_80 | INT_81 | INT_82 | INT_83 | INT_84 | INT_85 | INT_86 | INT_87 | INT_88 | INT_89 | INT_90 | INT_91 | INT_92 | INT_93 | INT_94 | INT_95 | INT_96 | INT_97 | INT_98 | INT_99 | UN | DEUX | TROIS | QUATRE | CINQ | SIX | SEPT | HUIT | NEUF | DIX | ONZE | DOUZE | TREIZE | QUATORZE | QUINZE | SEIZE | VINGT | TRENTE | PREMIER | DEUXIEME | TROISIEME | QUATRIEME | CINQUIEME | SIXIEME | SEPTIEME | HUITIEME | NEUVIEME | DIXIEME | ONZIEME | DOUZIEME | TREIZIEME | QUATORZIEME | QUINZIEME | SEIZIEME | VINGTIEME | TRENTIEME | UNIEME | WHITE_SPACE | UNKNOWN )
        int alt73=215;
        alt73 = dfa73.predict(input);
        switch (alt73) {
            case 1 :
                // com/joestelmach/natty/generated/DateLexer.g:1:10: JANVIER
                {
                mJANVIER(); 

                }
                break;
            case 2 :
                // com/joestelmach/natty/generated/DateLexer.g:1:18: FEVRIER
                {
                mFEVRIER(); 

                }
                break;
            case 3 :
                // com/joestelmach/natty/generated/DateLexer.g:1:26: MARS
                {
                mMARS(); 

                }
                break;
            case 4 :
                // com/joestelmach/natty/generated/DateLexer.g:1:31: AVRIL
                {
                mAVRIL(); 

                }
                break;
            case 5 :
                // com/joestelmach/natty/generated/DateLexer.g:1:37: MAI
                {
                mMAI(); 

                }
                break;
            case 6 :
                // com/joestelmach/natty/generated/DateLexer.g:1:41: JUIN
                {
                mJUIN(); 

                }
                break;
            case 7 :
                // com/joestelmach/natty/generated/DateLexer.g:1:46: JUILLET
                {
                mJUILLET(); 

                }
                break;
            case 8 :
                // com/joestelmach/natty/generated/DateLexer.g:1:54: AOUT
                {
                mAOUT(); 

                }
                break;
            case 9 :
                // com/joestelmach/natty/generated/DateLexer.g:1:59: SEPTEMBRE
                {
                mSEPTEMBRE(); 

                }
                break;
            case 10 :
                // com/joestelmach/natty/generated/DateLexer.g:1:69: OCTOBRE
                {
                mOCTOBRE(); 

                }
                break;
            case 11 :
                // com/joestelmach/natty/generated/DateLexer.g:1:77: NOVEMBRE
                {
                mNOVEMBRE(); 

                }
                break;
            case 12 :
                // com/joestelmach/natty/generated/DateLexer.g:1:86: DECEMBRE
                {
                mDECEMBRE(); 

                }
                break;
            case 13 :
                // com/joestelmach/natty/generated/DateLexer.g:1:95: DIMANCHE
                {
                mDIMANCHE(); 

                }
                break;
            case 14 :
                // com/joestelmach/natty/generated/DateLexer.g:1:104: LUNDI
                {
                mLUNDI(); 

                }
                break;
            case 15 :
                // com/joestelmach/natty/generated/DateLexer.g:1:110: MARDI
                {
                mMARDI(); 

                }
                break;
            case 16 :
                // com/joestelmach/natty/generated/DateLexer.g:1:116: MERCREDI
                {
                mMERCREDI(); 

                }
                break;
            case 17 :
                // com/joestelmach/natty/generated/DateLexer.g:1:125: JEUDI
                {
                mJEUDI(); 

                }
                break;
            case 18 :
                // com/joestelmach/natty/generated/DateLexer.g:1:131: VENDREDI
                {
                mVENDREDI(); 

                }
                break;
            case 19 :
                // com/joestelmach/natty/generated/DateLexer.g:1:140: SAMEDI
                {
                mSAMEDI(); 

                }
                break;
            case 20 :
                // com/joestelmach/natty/generated/DateLexer.g:1:147: HEURE
                {
                mHEURE(); 

                }
                break;
            case 21 :
                // com/joestelmach/natty/generated/DateLexer.g:1:153: HEURE_SHORT
                {
                mHEURE_SHORT(); 

                }
                break;
            case 22 :
                // com/joestelmach/natty/generated/DateLexer.g:1:165: MINUTE
                {
                mMINUTE(); 

                }
                break;
            case 23 :
                // com/joestelmach/natty/generated/DateLexer.g:1:172: JOUR
                {
                mJOUR(); 

                }
                break;
            case 24 :
                // com/joestelmach/natty/generated/DateLexer.g:1:177: SEMAINE
                {
                mSEMAINE(); 

                }
                break;
            case 25 :
                // com/joestelmach/natty/generated/DateLexer.g:1:185: MOIS
                {
                mMOIS(); 

                }
                break;
            case 26 :
                // com/joestelmach/natty/generated/DateLexer.g:1:190: ANNEE
                {
                mANNEE(); 

                }
                break;
            case 27 :
                // com/joestelmach/natty/generated/DateLexer.g:1:196: AUJOURD_HUI
                {
                mAUJOURD_HUI(); 

                }
                break;
            case 28 :
                // com/joestelmach/natty/generated/DateLexer.g:1:208: DEMAIN
                {
                mDEMAIN(); 

                }
                break;
            case 29 :
                // com/joestelmach/natty/generated/DateLexer.g:1:215: APRES_DEMAIN
                {
                mAPRES_DEMAIN(); 

                }
                break;
            case 30 :
                // com/joestelmach/natty/generated/DateLexer.g:1:228: HIER
                {
                mHIER(); 

                }
                break;
            case 31 :
                // com/joestelmach/natty/generated/DateLexer.g:1:233: AVANT_HIER
                {
                mAVANT_HIER(); 

                }
                break;
            case 32 :
                // com/joestelmach/natty/generated/DateLexer.g:1:244: WEEKEND
                {
                mWEEKEND(); 

                }
                break;
            case 33 :
                // com/joestelmach/natty/generated/DateLexer.g:1:252: CHAQUE
                {
                mCHAQUE(); 

                }
                break;
            case 34 :
                // com/joestelmach/natty/generated/DateLexer.g:1:259: JUSQU_A
                {
                mJUSQU_A(); 

                }
                break;
            case 35 :
                // com/joestelmach/natty/generated/DateLexer.g:1:267: A
                {
                mA(); 

                }
                break;
            case 36 :
                // com/joestelmach/natty/generated/DateLexer.g:1:269: APRES
                {
                mAPRES(); 

                }
                break;
            case 37 :
                // com/joestelmach/natty/generated/DateLexer.g:1:275: PASSE
                {
                mPASSE(); 

                }
                break;
            case 38 :
                // com/joestelmach/natty/generated/DateLexer.g:1:281: MINUIT
                {
                mMINUIT(); 

                }
                break;
            case 39 :
                // com/joestelmach/natty/generated/DateLexer.g:1:288: APRES_MIDI
                {
                mAPRES_MIDI(); 

                }
                break;
            case 40 :
                // com/joestelmach/natty/generated/DateLexer.g:1:299: MATIN
                {
                mMATIN(); 

                }
                break;
            case 41 :
                // com/joestelmach/natty/generated/DateLexer.g:1:305: SOIR
                {
                mSOIR(); 

                }
                break;
            case 42 :
                // com/joestelmach/natty/generated/DateLexer.g:1:310: NUIT
                {
                mNUIT(); 

                }
                break;
            case 43 :
                // com/joestelmach/natty/generated/DateLexer.g:1:315: PENDANT
                {
                mPENDANT(); 

                }
                break;
            case 44 :
                // com/joestelmach/natty/generated/DateLexer.g:1:323: DANS
                {
                mDANS(); 

                }
                break;
            case 45 :
                // com/joestelmach/natty/generated/DateLexer.g:1:328: LE
                {
                mLE(); 

                }
                break;
            case 46 :
                // com/joestelmach/natty/generated/DateLexer.g:1:331: LA
                {
                mLA(); 

                }
                break;
            case 47 :
                // com/joestelmach/natty/generated/DateLexer.g:1:334: OU
                {
                mOU(); 

                }
                break;
            case 48 :
                // com/joestelmach/natty/generated/DateLexer.g:1:337: ET
                {
                mET(); 

                }
                break;
            case 49 :
                // com/joestelmach/natty/generated/DateLexer.g:1:340: CE
                {
                mCE(); 

                }
                break;
            case 50 :
                // com/joestelmach/natty/generated/DateLexer.g:1:343: DE
                {
                mDE(); 

                }
                break;
            case 51 :
                // com/joestelmach/natty/generated/DateLexer.g:1:346: DU
                {
                mDU(); 

                }
                break;
            case 52 :
                // com/joestelmach/natty/generated/DateLexer.g:1:349: AU
                {
                mAU(); 

                }
                break;
            case 53 :
                // com/joestelmach/natty/generated/DateLexer.g:1:352: MAINTENANT
                {
                mMAINTENANT(); 

                }
                break;
            case 54 :
                // com/joestelmach/natty/generated/DateLexer.g:1:363: SUIVANT
                {
                mSUIVANT(); 

                }
                break;
            case 55 :
                // com/joestelmach/natty/generated/DateLexer.g:1:371: DERNIER
                {
                mDERNIER(); 

                }
                break;
            case 56 :
                // com/joestelmach/natty/generated/DateLexer.g:1:379: AVANT
                {
                mAVANT(); 

                }
                break;
            case 57 :
                // com/joestelmach/natty/generated/DateLexer.g:1:385: DEBUT
                {
                mDEBUT(); 

                }
                break;
            case 58 :
                // com/joestelmach/natty/generated/DateLexer.g:1:391: FIN
                {
                mFIN(); 

                }
                break;
            case 59 :
                // com/joestelmach/natty/generated/DateLexer.g:1:395: IL_Y_A
                {
                mIL_Y_A(); 

                }
                break;
            case 60 :
                // com/joestelmach/natty/generated/DateLexer.g:1:402: COLON
                {
                mCOLON(); 

                }
                break;
            case 61 :
                // com/joestelmach/natty/generated/DateLexer.g:1:408: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 62 :
                // com/joestelmach/natty/generated/DateLexer.g:1:414: DASH
                {
                mDASH(); 

                }
                break;
            case 63 :
                // com/joestelmach/natty/generated/DateLexer.g:1:419: SLASH
                {
                mSLASH(); 

                }
                break;
            case 64 :
                // com/joestelmach/natty/generated/DateLexer.g:1:425: DOT
                {
                mDOT(); 

                }
                break;
            case 65 :
                // com/joestelmach/natty/generated/DateLexer.g:1:429: PLUS
                {
                mPLUS(); 

                }
                break;
            case 66 :
                // com/joestelmach/natty/generated/DateLexer.g:1:434: SINGLE_QUOTE
                {
                mSINGLE_QUOTE(); 

                }
                break;
            case 67 :
                // com/joestelmach/natty/generated/DateLexer.g:1:447: INT_00
                {
                mINT_00(); 

                }
                break;
            case 68 :
                // com/joestelmach/natty/generated/DateLexer.g:1:454: INT_01
                {
                mINT_01(); 

                }
                break;
            case 69 :
                // com/joestelmach/natty/generated/DateLexer.g:1:461: INT_02
                {
                mINT_02(); 

                }
                break;
            case 70 :
                // com/joestelmach/natty/generated/DateLexer.g:1:468: INT_03
                {
                mINT_03(); 

                }
                break;
            case 71 :
                // com/joestelmach/natty/generated/DateLexer.g:1:475: INT_04
                {
                mINT_04(); 

                }
                break;
            case 72 :
                // com/joestelmach/natty/generated/DateLexer.g:1:482: INT_05
                {
                mINT_05(); 

                }
                break;
            case 73 :
                // com/joestelmach/natty/generated/DateLexer.g:1:489: INT_06
                {
                mINT_06(); 

                }
                break;
            case 74 :
                // com/joestelmach/natty/generated/DateLexer.g:1:496: INT_07
                {
                mINT_07(); 

                }
                break;
            case 75 :
                // com/joestelmach/natty/generated/DateLexer.g:1:503: INT_08
                {
                mINT_08(); 

                }
                break;
            case 76 :
                // com/joestelmach/natty/generated/DateLexer.g:1:510: INT_09
                {
                mINT_09(); 

                }
                break;
            case 77 :
                // com/joestelmach/natty/generated/DateLexer.g:1:517: INT_0
                {
                mINT_0(); 

                }
                break;
            case 78 :
                // com/joestelmach/natty/generated/DateLexer.g:1:523: INT_1
                {
                mINT_1(); 

                }
                break;
            case 79 :
                // com/joestelmach/natty/generated/DateLexer.g:1:529: INT_2
                {
                mINT_2(); 

                }
                break;
            case 80 :
                // com/joestelmach/natty/generated/DateLexer.g:1:535: INT_3
                {
                mINT_3(); 

                }
                break;
            case 81 :
                // com/joestelmach/natty/generated/DateLexer.g:1:541: INT_4
                {
                mINT_4(); 

                }
                break;
            case 82 :
                // com/joestelmach/natty/generated/DateLexer.g:1:547: INT_5
                {
                mINT_5(); 

                }
                break;
            case 83 :
                // com/joestelmach/natty/generated/DateLexer.g:1:553: INT_6
                {
                mINT_6(); 

                }
                break;
            case 84 :
                // com/joestelmach/natty/generated/DateLexer.g:1:559: INT_7
                {
                mINT_7(); 

                }
                break;
            case 85 :
                // com/joestelmach/natty/generated/DateLexer.g:1:565: INT_8
                {
                mINT_8(); 

                }
                break;
            case 86 :
                // com/joestelmach/natty/generated/DateLexer.g:1:571: INT_9
                {
                mINT_9(); 

                }
                break;
            case 87 :
                // com/joestelmach/natty/generated/DateLexer.g:1:577: INT_10
                {
                mINT_10(); 

                }
                break;
            case 88 :
                // com/joestelmach/natty/generated/DateLexer.g:1:584: INT_11
                {
                mINT_11(); 

                }
                break;
            case 89 :
                // com/joestelmach/natty/generated/DateLexer.g:1:591: INT_12
                {
                mINT_12(); 

                }
                break;
            case 90 :
                // com/joestelmach/natty/generated/DateLexer.g:1:598: INT_13
                {
                mINT_13(); 

                }
                break;
            case 91 :
                // com/joestelmach/natty/generated/DateLexer.g:1:605: INT_14
                {
                mINT_14(); 

                }
                break;
            case 92 :
                // com/joestelmach/natty/generated/DateLexer.g:1:612: INT_15
                {
                mINT_15(); 

                }
                break;
            case 93 :
                // com/joestelmach/natty/generated/DateLexer.g:1:619: INT_16
                {
                mINT_16(); 

                }
                break;
            case 94 :
                // com/joestelmach/natty/generated/DateLexer.g:1:626: INT_17
                {
                mINT_17(); 

                }
                break;
            case 95 :
                // com/joestelmach/natty/generated/DateLexer.g:1:633: INT_18
                {
                mINT_18(); 

                }
                break;
            case 96 :
                // com/joestelmach/natty/generated/DateLexer.g:1:640: INT_19
                {
                mINT_19(); 

                }
                break;
            case 97 :
                // com/joestelmach/natty/generated/DateLexer.g:1:647: INT_20
                {
                mINT_20(); 

                }
                break;
            case 98 :
                // com/joestelmach/natty/generated/DateLexer.g:1:654: INT_21
                {
                mINT_21(); 

                }
                break;
            case 99 :
                // com/joestelmach/natty/generated/DateLexer.g:1:661: INT_22
                {
                mINT_22(); 

                }
                break;
            case 100 :
                // com/joestelmach/natty/generated/DateLexer.g:1:668: INT_23
                {
                mINT_23(); 

                }
                break;
            case 101 :
                // com/joestelmach/natty/generated/DateLexer.g:1:675: INT_24
                {
                mINT_24(); 

                }
                break;
            case 102 :
                // com/joestelmach/natty/generated/DateLexer.g:1:682: INT_25
                {
                mINT_25(); 

                }
                break;
            case 103 :
                // com/joestelmach/natty/generated/DateLexer.g:1:689: INT_26
                {
                mINT_26(); 

                }
                break;
            case 104 :
                // com/joestelmach/natty/generated/DateLexer.g:1:696: INT_27
                {
                mINT_27(); 

                }
                break;
            case 105 :
                // com/joestelmach/natty/generated/DateLexer.g:1:703: INT_28
                {
                mINT_28(); 

                }
                break;
            case 106 :
                // com/joestelmach/natty/generated/DateLexer.g:1:710: INT_29
                {
                mINT_29(); 

                }
                break;
            case 107 :
                // com/joestelmach/natty/generated/DateLexer.g:1:717: INT_30
                {
                mINT_30(); 

                }
                break;
            case 108 :
                // com/joestelmach/natty/generated/DateLexer.g:1:724: INT_31
                {
                mINT_31(); 

                }
                break;
            case 109 :
                // com/joestelmach/natty/generated/DateLexer.g:1:731: INT_32
                {
                mINT_32(); 

                }
                break;
            case 110 :
                // com/joestelmach/natty/generated/DateLexer.g:1:738: INT_33
                {
                mINT_33(); 

                }
                break;
            case 111 :
                // com/joestelmach/natty/generated/DateLexer.g:1:745: INT_34
                {
                mINT_34(); 

                }
                break;
            case 112 :
                // com/joestelmach/natty/generated/DateLexer.g:1:752: INT_35
                {
                mINT_35(); 

                }
                break;
            case 113 :
                // com/joestelmach/natty/generated/DateLexer.g:1:759: INT_36
                {
                mINT_36(); 

                }
                break;
            case 114 :
                // com/joestelmach/natty/generated/DateLexer.g:1:766: INT_37
                {
                mINT_37(); 

                }
                break;
            case 115 :
                // com/joestelmach/natty/generated/DateLexer.g:1:773: INT_38
                {
                mINT_38(); 

                }
                break;
            case 116 :
                // com/joestelmach/natty/generated/DateLexer.g:1:780: INT_39
                {
                mINT_39(); 

                }
                break;
            case 117 :
                // com/joestelmach/natty/generated/DateLexer.g:1:787: INT_40
                {
                mINT_40(); 

                }
                break;
            case 118 :
                // com/joestelmach/natty/generated/DateLexer.g:1:794: INT_41
                {
                mINT_41(); 

                }
                break;
            case 119 :
                // com/joestelmach/natty/generated/DateLexer.g:1:801: INT_42
                {
                mINT_42(); 

                }
                break;
            case 120 :
                // com/joestelmach/natty/generated/DateLexer.g:1:808: INT_43
                {
                mINT_43(); 

                }
                break;
            case 121 :
                // com/joestelmach/natty/generated/DateLexer.g:1:815: INT_44
                {
                mINT_44(); 

                }
                break;
            case 122 :
                // com/joestelmach/natty/generated/DateLexer.g:1:822: INT_45
                {
                mINT_45(); 

                }
                break;
            case 123 :
                // com/joestelmach/natty/generated/DateLexer.g:1:829: INT_46
                {
                mINT_46(); 

                }
                break;
            case 124 :
                // com/joestelmach/natty/generated/DateLexer.g:1:836: INT_47
                {
                mINT_47(); 

                }
                break;
            case 125 :
                // com/joestelmach/natty/generated/DateLexer.g:1:843: INT_48
                {
                mINT_48(); 

                }
                break;
            case 126 :
                // com/joestelmach/natty/generated/DateLexer.g:1:850: INT_49
                {
                mINT_49(); 

                }
                break;
            case 127 :
                // com/joestelmach/natty/generated/DateLexer.g:1:857: INT_50
                {
                mINT_50(); 

                }
                break;
            case 128 :
                // com/joestelmach/natty/generated/DateLexer.g:1:864: INT_51
                {
                mINT_51(); 

                }
                break;
            case 129 :
                // com/joestelmach/natty/generated/DateLexer.g:1:871: INT_52
                {
                mINT_52(); 

                }
                break;
            case 130 :
                // com/joestelmach/natty/generated/DateLexer.g:1:878: INT_53
                {
                mINT_53(); 

                }
                break;
            case 131 :
                // com/joestelmach/natty/generated/DateLexer.g:1:885: INT_54
                {
                mINT_54(); 

                }
                break;
            case 132 :
                // com/joestelmach/natty/generated/DateLexer.g:1:892: INT_55
                {
                mINT_55(); 

                }
                break;
            case 133 :
                // com/joestelmach/natty/generated/DateLexer.g:1:899: INT_56
                {
                mINT_56(); 

                }
                break;
            case 134 :
                // com/joestelmach/natty/generated/DateLexer.g:1:906: INT_57
                {
                mINT_57(); 

                }
                break;
            case 135 :
                // com/joestelmach/natty/generated/DateLexer.g:1:913: INT_58
                {
                mINT_58(); 

                }
                break;
            case 136 :
                // com/joestelmach/natty/generated/DateLexer.g:1:920: INT_59
                {
                mINT_59(); 

                }
                break;
            case 137 :
                // com/joestelmach/natty/generated/DateLexer.g:1:927: INT_60
                {
                mINT_60(); 

                }
                break;
            case 138 :
                // com/joestelmach/natty/generated/DateLexer.g:1:934: INT_61
                {
                mINT_61(); 

                }
                break;
            case 139 :
                // com/joestelmach/natty/generated/DateLexer.g:1:941: INT_62
                {
                mINT_62(); 

                }
                break;
            case 140 :
                // com/joestelmach/natty/generated/DateLexer.g:1:948: INT_63
                {
                mINT_63(); 

                }
                break;
            case 141 :
                // com/joestelmach/natty/generated/DateLexer.g:1:955: INT_64
                {
                mINT_64(); 

                }
                break;
            case 142 :
                // com/joestelmach/natty/generated/DateLexer.g:1:962: INT_65
                {
                mINT_65(); 

                }
                break;
            case 143 :
                // com/joestelmach/natty/generated/DateLexer.g:1:969: INT_66
                {
                mINT_66(); 

                }
                break;
            case 144 :
                // com/joestelmach/natty/generated/DateLexer.g:1:976: INT_67
                {
                mINT_67(); 

                }
                break;
            case 145 :
                // com/joestelmach/natty/generated/DateLexer.g:1:983: INT_68
                {
                mINT_68(); 

                }
                break;
            case 146 :
                // com/joestelmach/natty/generated/DateLexer.g:1:990: INT_69
                {
                mINT_69(); 

                }
                break;
            case 147 :
                // com/joestelmach/natty/generated/DateLexer.g:1:997: INT_70
                {
                mINT_70(); 

                }
                break;
            case 148 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1004: INT_71
                {
                mINT_71(); 

                }
                break;
            case 149 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1011: INT_72
                {
                mINT_72(); 

                }
                break;
            case 150 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1018: INT_73
                {
                mINT_73(); 

                }
                break;
            case 151 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1025: INT_74
                {
                mINT_74(); 

                }
                break;
            case 152 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1032: INT_75
                {
                mINT_75(); 

                }
                break;
            case 153 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1039: INT_76
                {
                mINT_76(); 

                }
                break;
            case 154 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1046: INT_77
                {
                mINT_77(); 

                }
                break;
            case 155 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1053: INT_78
                {
                mINT_78(); 

                }
                break;
            case 156 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1060: INT_79
                {
                mINT_79(); 

                }
                break;
            case 157 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1067: INT_80
                {
                mINT_80(); 

                }
                break;
            case 158 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1074: INT_81
                {
                mINT_81(); 

                }
                break;
            case 159 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1081: INT_82
                {
                mINT_82(); 

                }
                break;
            case 160 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1088: INT_83
                {
                mINT_83(); 

                }
                break;
            case 161 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1095: INT_84
                {
                mINT_84(); 

                }
                break;
            case 162 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1102: INT_85
                {
                mINT_85(); 

                }
                break;
            case 163 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1109: INT_86
                {
                mINT_86(); 

                }
                break;
            case 164 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1116: INT_87
                {
                mINT_87(); 

                }
                break;
            case 165 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1123: INT_88
                {
                mINT_88(); 

                }
                break;
            case 166 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1130: INT_89
                {
                mINT_89(); 

                }
                break;
            case 167 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1137: INT_90
                {
                mINT_90(); 

                }
                break;
            case 168 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1144: INT_91
                {
                mINT_91(); 

                }
                break;
            case 169 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1151: INT_92
                {
                mINT_92(); 

                }
                break;
            case 170 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1158: INT_93
                {
                mINT_93(); 

                }
                break;
            case 171 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1165: INT_94
                {
                mINT_94(); 

                }
                break;
            case 172 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1172: INT_95
                {
                mINT_95(); 

                }
                break;
            case 173 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1179: INT_96
                {
                mINT_96(); 

                }
                break;
            case 174 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1186: INT_97
                {
                mINT_97(); 

                }
                break;
            case 175 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1193: INT_98
                {
                mINT_98(); 

                }
                break;
            case 176 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1200: INT_99
                {
                mINT_99(); 

                }
                break;
            case 177 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1207: UN
                {
                mUN(); 

                }
                break;
            case 178 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1210: DEUX
                {
                mDEUX(); 

                }
                break;
            case 179 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1215: TROIS
                {
                mTROIS(); 

                }
                break;
            case 180 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1221: QUATRE
                {
                mQUATRE(); 

                }
                break;
            case 181 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1228: CINQ
                {
                mCINQ(); 

                }
                break;
            case 182 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1233: SIX
                {
                mSIX(); 

                }
                break;
            case 183 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1237: SEPT
                {
                mSEPT(); 

                }
                break;
            case 184 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1242: HUIT
                {
                mHUIT(); 

                }
                break;
            case 185 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1247: NEUF
                {
                mNEUF(); 

                }
                break;
            case 186 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1252: DIX
                {
                mDIX(); 

                }
                break;
            case 187 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1256: ONZE
                {
                mONZE(); 

                }
                break;
            case 188 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1261: DOUZE
                {
                mDOUZE(); 

                }
                break;
            case 189 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1267: TREIZE
                {
                mTREIZE(); 

                }
                break;
            case 190 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1274: QUATORZE
                {
                mQUATORZE(); 

                }
                break;
            case 191 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1283: QUINZE
                {
                mQUINZE(); 

                }
                break;
            case 192 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1290: SEIZE
                {
                mSEIZE(); 

                }
                break;
            case 193 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1296: VINGT
                {
                mVINGT(); 

                }
                break;
            case 194 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1302: TRENTE
                {
                mTRENTE(); 

                }
                break;
            case 195 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1309: PREMIER
                {
                mPREMIER(); 

                }
                break;
            case 196 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1317: DEUXIEME
                {
                mDEUXIEME(); 

                }
                break;
            case 197 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1326: TROISIEME
                {
                mTROISIEME(); 

                }
                break;
            case 198 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1336: QUATRIEME
                {
                mQUATRIEME(); 

                }
                break;
            case 199 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1346: CINQUIEME
                {
                mCINQUIEME(); 

                }
                break;
            case 200 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1356: SIXIEME
                {
                mSIXIEME(); 

                }
                break;
            case 201 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1364: SEPTIEME
                {
                mSEPTIEME(); 

                }
                break;
            case 202 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1373: HUITIEME
                {
                mHUITIEME(); 

                }
                break;
            case 203 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1382: NEUVIEME
                {
                mNEUVIEME(); 

                }
                break;
            case 204 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1391: DIXIEME
                {
                mDIXIEME(); 

                }
                break;
            case 205 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1399: ONZIEME
                {
                mONZIEME(); 

                }
                break;
            case 206 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1407: DOUZIEME
                {
                mDOUZIEME(); 

                }
                break;
            case 207 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1416: TREIZIEME
                {
                mTREIZIEME(); 

                }
                break;
            case 208 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1426: QUATORZIEME
                {
                mQUATORZIEME(); 

                }
                break;
            case 209 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1438: QUINZIEME
                {
                mQUINZIEME(); 

                }
                break;
            case 210 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1448: SEIZIEME
                {
                mSEIZIEME(); 

                }
                break;
            case 211 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1457: VINGTIEME
                {
                mVINGTIEME(); 

                }
                break;
            case 212 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1467: TRENTIEME
                {
                mTRENTIEME(); 

                }
                break;
            case 213 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1477: UNIEME
                {
                mUNIEME(); 

                }
                break;
            case 214 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1484: WHITE_SPACE
                {
                mWHITE_SPACE(); 

                }
                break;
            case 215 :
                // com/joestelmach/natty/generated/DateLexer.g:1:1496: UNKNOWN
                {
                mUNKNOWN(); 

                }
                break;

        }

    }


    protected DFA73 dfa73 = new DFA73(this);
    static final String DFA73_eotS =
        "\1\uffff\3\47\1\67\6\47\1\121\2\47\1\uffff\3\47\4\uffff\1\140\2"+
        "\uffff\1\155\1\170\1\u0083\1\u008e\1\u0099\1\u00a4\1\u00af\1\u00ba"+
        "\1\u00c5\1\u00d0\3\47\17\uffff\1\u00dd\16\uffff\1\u00eb\u008c\uffff"+
        "\1\u00f3\4\uffff\1\u00fb\1\u00ff\1\uffff\1\u0101\5\uffff\1\u0105"+
        "\3\uffff\1\u0108\11\uffff\1\u010f\26\uffff\1\u011d\10\uffff\1\u0120"+
        "\4\uffff\1\u0125\1\u0127\6\uffff\1\131\1\u0130\7\uffff\1\u0132\4"+
        "\uffff\1\u0134\32\uffff";
    static final String DFA73_eofS =
        "\u0143\uffff";
    static final String DFA73_minS =
        "\1\0\1\141\1\145\1\141\1\156\1\141\1\143\1\145\2\141\4\145\1\uffff"+
        "\1\141\1\164\1\154\4\uffff\1\11\2\uffff\12\60\1\156\1\162\1\165"+
        "\3\uffff\1\151\4\uffff\1\151\1\uffff\1\156\1\uffff\1\141\2\uffff"+
        "\1\152\1\162\1\uffff\1\143\3\uffff\1\170\2\uffff\1\172\2\uffff\1"+
        "\165\1\142\1\143\1\155\2\uffff\1\165\4\uffff\1\156\2\uffff\1\151"+
        "\4\uffff\1\156\2\uffff\1\145\170\uffff\1\151\1\145\1\141\1\154\1"+
        "\uffff\1\56\1\156\1\uffff\1\165\1\uffff\1\156\2\uffff\1\u00e8\1"+
        "\164\1\uffff\1\172\1\uffff\1\151\1\145\1\146\4\uffff\1\170\2\uffff"+
        "\1\151\1\172\1\147\1\164\1\161\3\uffff\2\151\1\164\1\156\7\uffff"+
        "\1\151\1\uffff\1\164\1\163\1\145\1\uffff\1\145\6\uffff\1\151\2\uffff"+
        "\1\145\1\164\1\151\1\165\1\163\1\172\1\164\1\157\1\172\1\uffff\2"+
        "\55\7\uffff\1\151\4\uffff\1\151\3\145\1\162\1\145\1\uffff\1\144"+
        "\13\uffff\1\172\4\uffff\1\145\2\uffff";
    static final String DFA73_maxS =
        "\1\uffff\1\165\1\u00e9\1\157\1\166\3\165\1\u00e9\1\165\1\151\1\165"+
        "\1\145\1\151\1\uffff\1\162\1\164\1\154\4\uffff\1\56\2\uffff\12\71"+
        "\1\156\1\162\1\165\3\uffff\1\163\4\uffff\1\164\1\uffff\1\156\1\uffff"+
        "\1\162\2\uffff\1\152\1\162\1\uffff\1\160\3\uffff\1\170\2\uffff\1"+
        "\172\2\uffff\1\165\1\143\1\165\1\170\2\uffff\1\165\4\uffff\1\156"+
        "\2\uffff\1\151\4\uffff\1\156\2\uffff\1\157\170\uffff\1\151\1\157"+
        "\1\151\1\156\1\uffff\1\144\1\156\1\uffff\1\165\1\uffff\1\156\2\uffff"+
        "\1\u00e8\1\164\1\uffff\1\172\1\uffff\2\151\1\166\4\uffff\1\170\2"+
        "\uffff\1\151\1\172\1\147\1\164\1\161\3\uffff\1\151\1\156\1\164\1"+
        "\156\7\uffff\1\164\1\uffff\1\164\1\163\1\151\1\uffff\1\151\6\uffff"+
        "\1\151\2\uffff\1\151\1\164\1\151\1\165\1\163\1\172\1\164\1\162\1"+
        "\172\1\uffff\2\55\7\uffff\1\151\4\uffff\4\151\1\162\1\151\1\uffff"+
        "\1\155\13\uffff\1\172\4\uffff\1\151\2\uffff";
    static final String DFA73_acceptS =
        "\16\uffff\1\43\3\uffff\1\74\1\75\1\76\1\77\1\uffff\1\101\1\102\15"+
        "\uffff\1\u00d6\1\u00d7\1\1\1\uffff\1\21\1\27\1\2\1\72\1\uffff\1"+
        "\20\1\uffff\1\31\1\uffff\1\10\1\32\2\uffff\1\43\1\uffff\1\23\1\51"+
        "\1\66\1\uffff\1\12\1\57\1\uffff\1\13\1\52\4\uffff\1\54\1\63\1\uffff"+
        "\1\16\1\55\1\56\1\22\1\uffff\1\24\1\36\1\uffff\1\25\1\40\1\41\1"+
        "\61\1\uffff\1\45\1\53\1\uffff\1\70\1\60\1\73\1\74\1\75\1\76\1\77"+
        "\1\100\1\101\1\102\1\103\1\104\1\105\1\106\1\107\1\110\1\111\1\112"+
        "\1\113\1\114\1\115\1\127\1\130\1\131\1\132\1\133\1\134\1\135\1\136"+
        "\1\137\1\140\1\116\1\141\1\142\1\143\1\144\1\145\1\146\1\147\1\150"+
        "\1\151\1\152\1\117\1\153\1\154\1\155\1\156\1\157\1\160\1\161\1\162"+
        "\1\163\1\164\1\120\1\165\1\166\1\167\1\170\1\171\1\172\1\173\1\174"+
        "\1\175\1\176\1\121\1\177\1\u0080\1\u0081\1\u0082\1\u0083\1\u0084"+
        "\1\u0085\1\u0086\1\u0087\1\u0088\1\122\1\u0089\1\u008a\1\u008b\1"+
        "\u008c\1\u008d\1\u008e\1\u008f\1\u0090\1\u0091\1\u0092\1\123\1\u0093"+
        "\1\u0094\1\u0095\1\u0096\1\u0097\1\u0098\1\u0099\1\u009a\1\u009b"+
        "\1\u009c\1\124\1\u009d\1\u009e\1\u009f\1\u00a0\1\u00a1\1\u00a2\1"+
        "\u00a3\1\u00a4\1\u00a5\1\u00a6\1\125\1\u00a7\1\u00a8\1\u00a9\1\u00aa"+
        "\1\u00ab\1\u00ac\1\u00ad\1\u00ae\1\u00af\1\u00b0\1\126\4\uffff\1"+
        "\42\2\uffff\1\50\1\uffff\1\4\1\uffff\1\33\1\64\2\uffff\1\30\1\uffff"+
        "\1\u00c4\3\uffff\1\14\1\71\1\34\1\67\1\uffff\1\62\1\15\5\uffff\1"+
        "\u00c3\1\u00b1\1\u00d5\4\uffff\1\6\1\7\1\3\1\17\1\3\1\65\1\5\1\uffff"+
        "\1\26\3\uffff\1\11\1\uffff\1\u00c8\1\u00b6\1\u00bb\1\u00cd\1\u00b9"+
        "\1\u00cb\1\uffff\1\u00cc\1\u00ba\11\uffff\1\46\2\uffff\1\u00c9\1"+
        "\u00b7\1\u00c0\1\u00d2\1\u00b2\1\u00bc\1\u00ce\1\uffff\1\u00ca\1"+
        "\u00b8\1\u00c7\1\u00b5\6\uffff\1\37\1\uffff\1\44\1\u00d3\1\u00c1"+
        "\1\u00c5\1\u00b3\1\u00bd\1\u00cf\1\u00c2\1\u00d4\1\u00b4\1\u00c6"+
        "\1\uffff\1\u00bf\1\u00d1\1\35\1\47\1\uffff\1\u00be\1\u00d0";
    static final String DFA73_specialS =
        "\1\0\u0142\uffff}>";
    static final String[] DFA73_transitionS = {
            "\11\47\2\46\2\47\1\46\22\47\1\46\6\47\1\30\3\47\1\27\1\23\1"+
            "\24\1\26\1\25\1\31\1\32\1\33\1\34\1\35\1\36\1\37\1\40\1\41\1"+
            "\42\1\22\46\47\1\4\1\47\1\15\1\10\1\20\1\2\1\47\1\13\1\21\1"+
            "\1\1\47\1\11\1\3\1\7\1\6\1\17\1\45\1\47\1\5\1\44\1\43\1\12\1"+
            "\14\150\47\1\16\uff1f\47",
            "\1\50\3\uffff\1\52\11\uffff\1\53\2\uffff\1\53\2\uffff\1\51",
            "\1\54\3\uffff\1\55\177\uffff\1\54",
            "\1\56\3\uffff\1\57\3\uffff\1\60\5\uffff\1\61",
            "\1\64\1\63\1\66\4\uffff\1\65\1\62",
            "\1\71\3\uffff\1\70\3\uffff\1\74\5\uffff\1\72\5\uffff\1\73",
            "\1\75\12\uffff\1\77\6\uffff\1\76",
            "\1\102\11\uffff\1\100\5\uffff\1\101",
            "\1\106\3\uffff\1\104\3\uffff\1\105\5\uffff\1\110\5\uffff\1"+
            "\107\163\uffff\1\103",
            "\1\113\3\uffff\1\112\17\uffff\1\111",
            "\1\114\3\uffff\1\115",
            "\1\116\3\uffff\1\117\13\uffff\1\120",
            "\1\122",
            "\1\124\2\uffff\1\123\1\125",
            "",
            "\1\126\3\uffff\1\127\6\uffff\1\131\5\uffff\1\130",
            "\1\132",
            "\1\133",
            "",
            "",
            "",
            "",
            "\2\46\2\uffff\1\46\22\uffff\1\46\15\uffff\1\46",
            "",
            "",
            "\1\143\1\144\1\145\1\146\1\147\1\150\1\151\1\152\1\153\1\154",
            "\1\156\1\157\1\160\1\161\1\162\1\163\1\164\1\165\1\166\1\167",
            "\1\171\1\172\1\173\1\174\1\175\1\176\1\177\1\u0080\1\u0081"+
            "\1\u0082",
            "\1\u0084\1\u0085\1\u0086\1\u0087\1\u0088\1\u0089\1\u008a\1"+
            "\u008b\1\u008c\1\u008d",
            "\1\u008f\1\u0090\1\u0091\1\u0092\1\u0093\1\u0094\1\u0095\1"+
            "\u0096\1\u0097\1\u0098",
            "\1\u009a\1\u009b\1\u009c\1\u009d\1\u009e\1\u009f\1\u00a0\1"+
            "\u00a1\1\u00a2\1\u00a3",
            "\1\u00a5\1\u00a6\1\u00a7\1\u00a8\1\u00a9\1\u00aa\1\u00ab\1"+
            "\u00ac\1\u00ad\1\u00ae",
            "\1\u00b0\1\u00b1\1\u00b2\1\u00b3\1\u00b4\1\u00b5\1\u00b6\1"+
            "\u00b7\1\u00b8\1\u00b9",
            "\1\u00bb\1\u00bc\1\u00bd\1\u00be\1\u00bf\1\u00c0\1\u00c1\1"+
            "\u00c2\1\u00c3\1\u00c4",
            "\1\u00c6\1\u00c7\1\u00c8\1\u00c9\1\u00ca\1\u00cb\1\u00cc\1"+
            "\u00cd\1\u00ce\1\u00cf",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "",
            "",
            "",
            "\1\u00d4\11\uffff\1\u00d5",
            "",
            "",
            "",
            "",
            "\1\u00d7\10\uffff\1\u00d6\1\uffff\1\u00d8",
            "",
            "\1\u00d9",
            "",
            "\1\u00db\20\uffff\1\u00da",
            "",
            "",
            "\1\u00dc",
            "\1\u00de",
            "",
            "\1\u00e2\5\uffff\1\u00e1\3\uffff\1\u00e0\2\uffff\1\u00df",
            "",
            "",
            "",
            "\1\u00e3",
            "",
            "",
            "\1\u00e4",
            "",
            "",
            "\1\u00e5",
            "\1\u00e7\1\u00e6",
            "\1\u00e6\11\uffff\1\u00e8\4\uffff\1\u00e9\2\uffff\1\u00ea",
            "\1\u00ec\12\uffff\1\u00ed",
            "",
            "",
            "\1\u00ee",
            "",
            "",
            "",
            "",
            "\1\u00ef",
            "",
            "",
            "\1\u00f0",
            "",
            "",
            "",
            "",
            "\1\u00f1",
            "",
            "",
            "\1\u00f2\11\uffff\1\73",
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
            "\1\u00f4",
            "\1\u00f6\11\uffff\1\u00f5",
            "\1\u00f7\7\uffff\1\u00f8",
            "\1\u00fa\1\uffff\1\u00f9",
            "",
            "\1\u00fd\65\uffff\1\u00fc",
            "\1\u00fe",
            "",
            "\1\u0100",
            "",
            "\1\u0102",
            "",
            "",
            "\1\u0103",
            "\1\u0104",
            "",
            "\1\u0106",
            "",
            "\1\u0107",
            "\1\u0109\3\uffff\1\u010a",
            "\1\u010b\17\uffff\1\u010c",
            "",
            "",
            "",
            "",
            "\1\u010d",
            "",
            "",
            "\1\u010e",
            "\1\u0110",
            "\1\u0111",
            "\1\u0112",
            "\1\u0113",
            "",
            "",
            "",
            "\1\u0114",
            "\1\u0115\4\uffff\1\u0116",
            "\1\u0117",
            "\1\u0118",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0119\12\uffff\1\u0101",
            "",
            "\1\u011a",
            "\1\u011b",
            "\1\u0105\3\uffff\1\u011c",
            "",
            "\1\u011e\3\uffff\1\u011f",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u00e2",
            "",
            "",
            "\1\u0121\3\uffff\1\u0122",
            "\1\u0123",
            "\1\u0124",
            "\1\u0126",
            "\1\u0128",
            "\1\u0129",
            "\1\u012a",
            "\1\u012c\2\uffff\1\u012b",
            "\1\u012d",
            "",
            "\1\u012e",
            "\1\u012f",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0131",
            "",
            "",
            "",
            "",
            "\1\u0133",
            "\1\u0135\3\uffff\1\u0136",
            "\1\u0137\3\uffff\1\u0138",
            "\1\u0139\3\uffff\1\u013a",
            "\1\u013b",
            "\1\u013c\3\uffff\1\u013d",
            "",
            "\1\u013e\10\uffff\1\u013f",
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
            "\1\u0140",
            "",
            "",
            "",
            "",
            "\1\u0141\3\uffff\1\u0142",
            "",
            ""
    };

    static final short[] DFA73_eot = DFA.unpackEncodedString(DFA73_eotS);
    static final short[] DFA73_eof = DFA.unpackEncodedString(DFA73_eofS);
    static final char[] DFA73_min = DFA.unpackEncodedStringToUnsignedChars(DFA73_minS);
    static final char[] DFA73_max = DFA.unpackEncodedStringToUnsignedChars(DFA73_maxS);
    static final short[] DFA73_accept = DFA.unpackEncodedString(DFA73_acceptS);
    static final short[] DFA73_special = DFA.unpackEncodedString(DFA73_specialS);
    static final short[][] DFA73_transition;

    static {
        int numStates = DFA73_transitionS.length;
        DFA73_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA73_transition[i] = DFA.unpackEncodedString(DFA73_transitionS[i]);
        }
    }

    class DFA73 extends DFA {

        public DFA73(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 73;
            this.eot = DFA73_eot;
            this.eof = DFA73_eof;
            this.min = DFA73_min;
            this.max = DFA73_max;
            this.accept = DFA73_accept;
            this.special = DFA73_special;
            this.transition = DFA73_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( JANVIER | FEVRIER | MARS | AVRIL | MAI | JUIN | JUILLET | AOUT | SEPTEMBRE | OCTOBRE | NOVEMBRE | DECEMBRE | DIMANCHE | LUNDI | MARDI | MERCREDI | JEUDI | VENDREDI | SAMEDI | HEURE | HEURE_SHORT | MINUTE | JOUR | SEMAINE | MOIS | ANNEE | AUJOURD_HUI | DEMAIN | APRES_DEMAIN | HIER | AVANT_HIER | WEEKEND | CHAQUE | JUSQU_A | A | APRES | PASSE | MINUIT | APRES_MIDI | MATIN | SOIR | NUIT | PENDANT | DANS | LE | LA | OU | ET | CE | DE | DU | AU | MAINTENANT | SUIVANT | DERNIER | AVANT | DEBUT | FIN | IL_Y_A | COLON | COMMA | DASH | SLASH | DOT | PLUS | SINGLE_QUOTE | INT_00 | INT_01 | INT_02 | INT_03 | INT_04 | INT_05 | INT_06 | INT_07 | INT_08 | INT_09 | INT_0 | INT_1 | INT_2 | INT_3 | INT_4 | INT_5 | INT_6 | INT_7 | INT_8 | INT_9 | INT_10 | INT_11 | INT_12 | INT_13 | INT_14 | INT_15 | INT_16 | INT_17 | INT_18 | INT_19 | INT_20 | INT_21 | INT_22 | INT_23 | INT_24 | INT_25 | INT_26 | INT_27 | INT_28 | INT_29 | INT_30 | INT_31 | INT_32 | INT_33 | INT_34 | INT_35 | INT_36 | INT_37 | INT_38 | INT_39 | INT_40 | INT_41 | INT_42 | INT_43 | INT_44 | INT_45 | INT_46 | INT_47 | INT_48 | INT_49 | INT_50 | INT_51 | INT_52 | INT_53 | INT_54 | INT_55 | INT_56 | INT_57 | INT_58 | INT_59 | INT_60 | INT_61 | INT_62 | INT_63 | INT_64 | INT_65 | INT_66 | INT_67 | INT_68 | INT_69 | INT_70 | INT_71 | INT_72 | INT_73 | INT_74 | INT_75 | INT_76 | INT_77 | INT_78 | INT_79 | INT_80 | INT_81 | INT_82 | INT_83 | INT_84 | INT_85 | INT_86 | INT_87 | INT_88 | INT_89 | INT_90 | INT_91 | INT_92 | INT_93 | INT_94 | INT_95 | INT_96 | INT_97 | INT_98 | INT_99 | UN | DEUX | TROIS | QUATRE | CINQ | SIX | SEPT | HUIT | NEUF | DIX | ONZE | DOUZE | TREIZE | QUATORZE | QUINZE | SEIZE | VINGT | TRENTE | PREMIER | DEUXIEME | TROISIEME | QUATRIEME | CINQUIEME | SIXIEME | SEPTIEME | HUITIEME | NEUVIEME | DIXIEME | ONZIEME | DOUZIEME | TREIZIEME | QUATORZIEME | QUINZIEME | SEIZIEME | VINGTIEME | TRENTIEME | UNIEME | WHITE_SPACE | UNKNOWN );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA73_0 = input.LA(1);

                        s = -1;
                        if ( (LA73_0=='j') ) {s = 1;}

                        else if ( (LA73_0=='f') ) {s = 2;}

                        else if ( (LA73_0=='m') ) {s = 3;}

                        else if ( (LA73_0=='a') ) {s = 4;}

                        else if ( (LA73_0=='s') ) {s = 5;}

                        else if ( (LA73_0=='o') ) {s = 6;}

                        else if ( (LA73_0=='n') ) {s = 7;}

                        else if ( (LA73_0=='d') ) {s = 8;}

                        else if ( (LA73_0=='l') ) {s = 9;}

                        else if ( (LA73_0=='v') ) {s = 10;}

                        else if ( (LA73_0=='h') ) {s = 11;}

                        else if ( (LA73_0=='w') ) {s = 12;}

                        else if ( (LA73_0=='c') ) {s = 13;}

                        else if ( (LA73_0=='\u00E0') ) {s = 14;}

                        else if ( (LA73_0=='p') ) {s = 15;}

                        else if ( (LA73_0=='e') ) {s = 16;}

                        else if ( (LA73_0=='i') ) {s = 17;}

                        else if ( (LA73_0==':') ) {s = 18;}

                        else if ( (LA73_0==',') ) {s = 19;}

                        else if ( (LA73_0=='-') ) {s = 20;}

                        else if ( (LA73_0=='/') ) {s = 21;}

                        else if ( (LA73_0=='.') ) {s = 22;}

                        else if ( (LA73_0=='+') ) {s = 23;}

                        else if ( (LA73_0=='\'') ) {s = 24;}

                        else if ( (LA73_0=='0') ) {s = 25;}

                        else if ( (LA73_0=='1') ) {s = 26;}

                        else if ( (LA73_0=='2') ) {s = 27;}

                        else if ( (LA73_0=='3') ) {s = 28;}

                        else if ( (LA73_0=='4') ) {s = 29;}

                        else if ( (LA73_0=='5') ) {s = 30;}

                        else if ( (LA73_0=='6') ) {s = 31;}

                        else if ( (LA73_0=='7') ) {s = 32;}

                        else if ( (LA73_0=='8') ) {s = 33;}

                        else if ( (LA73_0=='9') ) {s = 34;}

                        else if ( (LA73_0=='u') ) {s = 35;}

                        else if ( (LA73_0=='t') ) {s = 36;}

                        else if ( (LA73_0=='q') ) {s = 37;}

                        else if ( ((LA73_0>='\t' && LA73_0<='\n')||LA73_0=='\r'||LA73_0==' ') ) {s = 38;}

                        else if ( ((LA73_0>='\u0000' && LA73_0<='\b')||(LA73_0>='\u000B' && LA73_0<='\f')||(LA73_0>='\u000E' && LA73_0<='\u001F')||(LA73_0>='!' && LA73_0<='&')||(LA73_0>='(' && LA73_0<='*')||(LA73_0>=';' && LA73_0<='`')||LA73_0=='b'||LA73_0=='g'||LA73_0=='k'||LA73_0=='r'||(LA73_0>='x' && LA73_0<='\u00DF')||(LA73_0>='\u00E1' && LA73_0<='\uFFFF')) ) {s = 39;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 73, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}