

#Import the libraries for the PLY parser generator
import ply.lex as lex
import ply.yacc as yacc
import sys
import math
#Define the Tokens used by the language
tokens =(
         'NUMBER',
   'PLUS',
   'MINUS',
   'TIMES',
   'DIVIDE',
   'LPAREN',
   'RPAREN',
   'LBRACE',
   'RBRACE',
   'OPEN',
   'CLOSE',
   'POWER',
   'FUNCTION',

)

#Define what character(s) define each of these tokens
t_PLUS= r'\+'
t_MINUS= r'\-'
t_TIMES= r'\*'
t_DIVIDE= r'\/'
t_LPAREN= r'\('
t_RPAREN= r'\)'
t_LBRACE= r'\{'
t_RBRACE= r'\}'
t_OPEN=   r'\['
t_CLOSE=  r'\]'
t_POWER  = r'\^'
t_ignore= ' \t' #Ignore whitespace

#A Number is a collection of digits from 0-9
def t_NUMBER( t) :
    r'[0-9]+' #Match any numbers
    t.value = int(t.value) #Convert the number to an int
    return t;

def t_FUNCTION(t):
    r'["sin","cos","tan","log","sqrt"]+'
    return t

#Print an error message on parsing error
def t_error(t):
    print "Illegal character '%s' on line %d" % (t.value[0],t.lexer.lineno)
    return t;
#Define the newline character and count the line number
def t_newline( t ):
    r'\n+'
    t.lexer.lineno += len(t.value)

#All the tokens are now defined.
#Command the parser to build a parse table for the tokens
lex.lex();


precedence = (
#('nonassoc', 'LESSTHAN', 'GREATERTHAN'),  # Nonassociative operators
    ('left', 'PLUS', 'MINUS'),
    ('left', 'TIMES', 'DIVIDE'),
    ('right', 'UMINUS'),            # Unary minus operator
)

def p_expr_uminus(p):
    'expression : MINUS expression %prec UMINUS'
    p[0] = -p[2]


def p_expression_plus(p):
    'expression : expression PLUS term'
    p[0] = p[1] + p[3]

def p_expression_minus(p):
    'expression : expression MINUS term'
    p[0] = p[1] - p[3]

def p_expression_term(p):
    'expression : term'
    p[0] = p[1]

def p_term_times(p):
    'term : term TIMES factor'
    p[0] = p[1] * p[3]

def p_term_div(p):
    'term : term DIVIDE factor'
    p[0] = p[1] / p[3]

def p_term_factor(p):
    'term : factor'
    p[0] = p[1]

def p_factor_num(p):
    'factor : NUMBER'
    p[0] = p[1]

def p_factor_expr(p):
    'factor : LPAREN expression RPAREN'
    p[0] = p[2]

def p_factor_expr_OPEN(p):
    'factor : OPEN expression CLOSE'
    p[0] = p[2]

def p_factor_expr_LBRACE(p):
    'factor : LBRACE expression RBRACE'
    p[0] = p[2]

#functions
def p_factor_b( p ):
    'factor : FUNCTION expression'
    if p[1]=='sin':
       p[0] = math.sin(p[2])
    elif p[1]=='cos':
       p[0]=math.cos(p[2])
    elif p[1]=='tan':
       p[0] = math.tan(p[2])
    elif p[1]=='log':
       p[0]=math.log10(p[2])
    elif p[1]=='sqrt':
       p[0]=math.sqrt(p[2])

def p_term_d( p ):
    'expression : factor POWER term'
    p[0] = math.pow(p[1] , p[3])

#Give the error message for syntax errors
#def p_error( p ):
 #   print "Syntax error in input!", str(p)
  #  sys.exit( 2 )

def p_error(p):
    print "Syntax error at token", p.type
    # Just discard the token and tell the parser it's okay.
    yacc.errok()

#Build the Parser
yacc.yacc()

#Define the main function
#This describes how our parser is called
#Take the standard input and parse it using the parser
if __name__ == '__main__' :
   # print "Hello. Type ctrl-D (End of File) to quit."
    print "expression then enter."
    expression = sys.stdin.readline()
    while expression != "":
        result = yacc.parse(expression)
        print "The Answer is "+str(result)
        expression = sys.stdin.readline()
