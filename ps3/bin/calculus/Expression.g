/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

@skip whitespace {
    root ::= sum;
    sum ::= product ('+' product)*;
    product ::= exponent ('*' exponent)*;
    exponent ::= primary ('^' number)*; 
    primary ::= number | variable | '(' sum ')';
}

number ::= [0-9]+ ('.')? [0-9]* | ('.') [0-9]+;
variable ::= [a-zA-Z];
whitespace ::= [ \t\r\n]+;