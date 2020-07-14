@skip whitespace {
    root ::= sequence;
    sequence ::= listDef (';' listDef)*;
    listDef ::= union | definition;
    union ::= difference (',' difference)*;
    difference ::= intersect ('!' intersect)*;
	intersect ::= primitive ('*' primitive)*; 
    primitive ::= definition | email? | name |'(' sequence ')';
    
    definition ::= name '=' union;
}

name ::= [a-zA-Z0-9_\-\.]+;
email ::= [a-zA-Z0-9_\-\.\+]+'@'[a-zA-Z0-9_\-\.]+;
whitespace ::= [ \t\r\n]+;