@skip whitespace {
    root ::= sum;
    sum ::= product ('+' product)*;
    product ::= primary ('*' primary)*;
    primary ::= number | variable | '(' sum ')';
}
number ::= [0-9]+;
variable ::= [a-zA-Z]+;
whitespace ::= [ \t\r\n]+;
