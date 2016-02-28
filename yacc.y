%output ="yacc.cpp"
%{
	#include <iostream>
	#include <FlexLexer.h>
	using namespace std;
	int yylex(void);
	int yyparse();
	void yyerror(char *);	
	FlexLexer* lexer = new yyFlexLexer();
	
	class Parser
	{
		public:
		int	parse()
		{
			return yyparse();
		}
	};
%}
%token TO NOUN DET INT Prep Verb phrasalVerb aux presentparticiple pastparticiple ADj LinkingVgp

%nonassoc Prep
%nonassoc r5
%nonassoc r3
%nonassoc r2
%nonassoc r1
%union{
	struct R{
		int i;
		float f;
		char c;
		char* str;
		int myLineNo;
		int myColno;
		}r;
	}
%%

statement: subject predicate              {}
;
subject:NP {}
;
predicate:VP  {}
;
NP:NOUN					{;}
  |DET NP %prec r1
  |AP NP  %prec r3
  |NP PP  %prec r2
;

AP:INT ADj 
  |ADj
  ;
PP:Prep  NP %prec r5
;
VP:Vgp
  |Vgp NP
  |Vgp NP TO NP
  |LinkingVgp NP
  |LinkingVgp AP
  |LinkingVgp PP
  |Vgp NP NP
  |Vgp NP AP
  //|Vgp NP PP
  |Vgp PP
  |Vgp NP NonFinite
 ;
Vgp:Verb
   |phrasalVerb
   |aux Verb
   |aux phrasalVerb
 ;
NonFinite:presentparticiple
          |TO VP
		  |pastparticiple
%%
void yyerror(char *s) 
{
	;
}

int yylex(void)
{
	return lexer->yylex();
}
void main(void)
{
	Parser* p = new Parser();
	p->parse();
	
}