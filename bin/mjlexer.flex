package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{
	//Buffer u kojem pamtimo charConst
	StringBuffer string = new StringBuffer();

	//Klasa Symbol se nalazi u javaCup paketu
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}




%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}


%%

" " 	{ }
"\t"	{ }
"\b"	{ }
"\r\n" 	{ }
"\f"	{ }

"program" 			{ return new_symbol(sym.PROG, yytext()); }
"break" 			{ return new_symbol(sym.BREAK, yytext()); }
"class"				{ return new_symbol(sym.CLASS, yytext()); }
"else"				{ return new_symbol(sym.ELSE, yytext()); }
"if"				{ return new_symbol(sym.IF, yytext()); }
"new"				{ return new_symbol(sym.NEW, yytext()); }
"print"				{ return new_symbol(sym.PRINT, yytext()); }
"read"				{ return new_symbol(sym.READ, yytext()); }
"return"			{ return new_symbol(sym.RETURN, yytext()); }
"void"				{ return new_symbol(sym.VOID, yytext()); }
"do"				{ return new_symbol(sym.DO, yytext()); }
"while"				{ return new_symbol(sym.WHILE, yytext()); }
"extends"			{ return new_symbol(sym.EXTENDS, yytext()); }
"continue"			{ return new_symbol(sym.CONTINUE, yytext()); }
"const"				{ return new_symbol(sym.CONST, yytext()); }

"=="                { return new_symbol(sym.BIN_EQUAL, yytext()); }
"!="				{ return new_symbol(sym.NOT_BIN_EQUAL, yytext()); }
">="				{ return new_symbol(sym.GREATER_EQUAL, yytext()); }
"<="				{ return new_symbol(sym.LOWER_EQUAL, yytext()); }
"="					{ return new_symbol(sym.EQUAL, yytext()); }
"++"				{ return new_symbol(sym.INCREMENT, yytext()); }
"--"				{ return new_symbol(sym.DECREMENT, yytext()); }
"+"					{ return new_symbol(sym.PLUS, yytext()); }
"-"					{ return new_symbol(sym.MINUS, yytext()); }
"*"					{ return new_symbol(sym.MUL, yytext()); }
"/"					{ return new_symbol(sym.DIV, yytext()); }
"%"					{ return new_symbol(sym.MOD, yytext()); }
">"					{ return new_symbol(sym.GREATER, yytext()); }
"<"					{ return new_symbol(sym.LOWER, yytext()); }
"&&"				{ return new_symbol(sym.LOGICAL_AND, yytext()); }
"||"				{ return new_symbol(sym.LOGICAL_OR, yytext()); }
";"					{ return new_symbol(sym.SEMI_COMMA, yytext()); }
","					{ return new_symbol(sym.COMMA, yytext()); }
"."					{ return new_symbol(sym.DOT, yytext()); }
"("					{ return new_symbol(sym.LPARENT, yytext()); }
")"					{ return new_symbol(sym.RPARENT, yytext()); }
"["					{ return new_symbol(sym.L_SQUARE_PARENT, yytext()); }
"]"					{ return new_symbol(sym.R_SQUARE_PARENT, yytext()); }
"{"					{ return new_symbol(sym.L_BRACE, yytext()); }
"}"					{ return new_symbol(sym.R_BRACE, yytext()); }

"true"							{ return new_symbol(sym.BOOL_CONST, new Boolean(yytext())); }
"false"							{ return new_symbol(sym.BOOL_CONST, new Boolean(yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]*	{return new_symbol (sym.IDENT, yytext()); }
[0-9]+  						{ return new_symbol(sym.NUM_CONST, new Integer (yytext())); }

"'"[\040-\176]"'"	{return new_symbol (sym.CHAR_CONST, yytext()); }


"//"				{ yybegin(COMMENT); }
<COMMENT> .			{ yybegin(COMMENT); }
<COMMENT> "\r\n" 	{ yybegin(YYINITIAL); }

. { System.err.println("Leksicka greska, nije prepoznat (" +yytext()+") u liniji "+(yyline+1)+" i koloni "+yycolumn + "\r\n"); }
