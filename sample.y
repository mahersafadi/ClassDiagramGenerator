%output ="yacc.cpp"
%{
	#include <iostream>
	using namespace std;
	#include <FlexLexer.h>
	#include "Function/AST.h"
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
	AST * ast = new AST();
%}
%token OPEN_S CLOSE_S OPEN_P CLOSE_P COMMA SEMI_COMMA AND_AND OR_OR ASSIGN EQUAL SCASSIGN VOID
%token NOT_EQUAL LESS_THAN LESS_OR_EQUAL MORE_THAN MORE_OR_EQUAL  PLUS_ASSIGN DEFAULT
%token MINUS_ASSIGN MULTI_ASSIGN DIV_ASSIGN MOD_ASSIGN VAR FALSE AND FLOAT FOR ID IF IMPORT
%token OPEN_ARR CLOSE_ARR FLOAT_VALUE INTERFACE TYPE NOT SEMI_COLUMN PLUS_PLUS TRUE OR NIL
%token MINUS MOD SWITCH CONST SEMI_COMMA CHAIN DOT PLUS MINUS RETURN NEW INTEGER_VALUE 
%token FUNC DIV PACKAGE BOOL MINUS_MINUS STRING STRUCT HAT WHILE CASE MULTI CHAR_VALUE
%token INT STRING_VALUE NEWLINE ELSE
%token STATIC OVERRIDE PUBLIC PROTECTED PRIVATE END

%nonassoc prec__1
%nonassoc prec__2
%left MINUS PLUS
%nonassoc VAR ID IF INTEGER_VALUE WHILE STRING_VALUE

%union{
	struct R{
		int i;
		float f;
		char c;
		char* str;
		int myLineNo;
		int myColno;
		}r;
		class TreeNode * tn;
	}
%%
program:
	func_list							{executer_1}
;

func_list:	
			func_list func				{executer_2}
			|func						{executer_3}
;

func:	
		func_header body	{executer_5}
;

func_header:		
		FUNC	ID	OPEN_P param_list	CLOSE_P type 	{executer_6}
		|FUNC	ID	OPEN_P				CLOSE_P type 	{executer_7}
;

param_list: 
			param_list COMMA param						{executer_8}
			|param										{executer_9}
;

param:	
		ID type											{executer_10}
;

type:	
		ID												{executer_11}
		|INT											{executer_12}
		|STRING											{executer_13}
;

body:
		OPEN_S stmt_list	CLOSE_S						{executer_14}
		|OPEN_S 			CLOSE_S						{executer_15}
;
stmt_list:
		stmt_list stmt									{executer_16}
		|stmt											{executer_17}
;
stmt:	
		if_stmt											{executer_18}
		|decl_stmt										{executer_19}
		|while_stmt										{executer_20}
		|expression_stmt								{executer_21}
;
decl_stmt: 
		VAR ID type										{executer_22}
		|VAR ID type ASSIGN expr						{executer_23}
;

if_stmt:
		if_header	body								{executer_24}
;

if_header: 
		IF OPEN_P expr MORE_THAN expr CLOSE_P			{executer_25}						
		|IF OPEN_P expr LESS_THAN expr CLOSE_P			{executer_26}
;

expr:	
	expr	PLUS	expr								{executer_27}
	|expr	MINUS	expr								{executer_28}
	|ID		ASSIGN	expr								{executer_29}
	|ID OPEN_P	expr_list	CLOSE_P						{executer_30}
	|ID													{executer_31}
	|INTEGER_VALUE										{executer_32}
	|STRING_VALUE										{executer_33}
;			

expr_list: 
		expr_list COMMA expr							{executer_34}
		|expr											{executer_35}
;

while_stmt: 
		while_header body								{executer_36}
;

while_header:	
		WHILE OPEN_P expr MORE_THAN expr	CLOSE_P		{executer_37}
		|WHILE OPEN_P expr LESS_THAN expr	CLOSE_P		{executer_38}
;

expression_stmt:	
		expr											{executer_39}
;
%%
void yyerror(char *s) 
{
	;
}

int yylex()
{
	return lexer->yylex();
}
void main(void)
{
	Parser* p = new Parser();
	p->parse();
	
}