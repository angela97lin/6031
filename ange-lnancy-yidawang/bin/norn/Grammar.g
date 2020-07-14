/*Usernames and domain names are nonempty case-insensitive strings of letters, 
*digits, underscores, dashes, and periods. This system does not enforce any other 
*constraints on domain names. For example, we don’t care how many dot-separated 
*parts a domain name has, or whether it ends with a valid top-level 
*domain like .com or .edu.
*note: priority order: ; = , ! *
*note: parenthesis '('')'  
*/

@skip whitespace {
	root ::= sequence | '(' sequence ')';
	sequence ::= (union|mailinglist|'('mailinglist')') (';' (union|mailinglist))*;
	mailinglist ::= name '=' (union*);
	union ::= (difference) (',' (difference))* ;
	difference ::= (intersection) ('!' (intersection))*;
	intersection ::= (email | name | '(' paren ')' ) ('*' (email | name | '(' paren ')'))*;
	paren ::= email | name | union; 
}

whitespace ::= [ \t\r\n]+;
email ::= (whitespace*|username domain);
domain ::= '@' username ('.' username)*;
name ::= (\w | '.' | '-')+;
username ::= (\w | '.' | '-')+;
