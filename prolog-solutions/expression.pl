:- load_library('alice.tuprolog.lib.DCGLibrary').

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

variable(Name, variable(Name)).
const(Value, const(Value)).

operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is -A.

and(1, 1, 1.0) :- !.
and(_, _, 0.0).
or(0, 0, 0.0) :- !.
or(_, _, 1.0).
xor(A, A, 0.0) :- !.
xor(_, _, 1.0).
iff(A, A, 1.0) :- !.
iff(_, _, 0.0).
impl(1, 0, 0.0) :- !.
impl(_, _, 1.0).
to_bool(X, 1) :- X > 0, !. 
to_bool(X, 0).
eval_bool(A, B, F, R) :- to_bool(A, AB), to_bool(B, BB), G =.. [F, AB, BB, R], call(G).
operation(op_and, A, B, R) :- eval_bool(A, B, and, R).
operation(op_or, A, B, R) :- eval_bool(A, B, or, R).
operation(op_xor, A, B, R) :- eval_bool(A, B, xor, R).
operation(op_not, A, R) :- to_bool(A, RB), R is 1 - RB. 
operation(op_iff, A, B, R) :- eval_bool(A, B, iff, R).
operation(op_impl, A, B, R) :- eval_bool(A, B, impl, R).

evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- atom_chars(Name, [H | T]), lookup(H, Vars, R).
evaluate(operation(Op, A, B), Vars, R) :- 
    evaluate(A, Vars, AV), 
    evaluate(B, Vars, BV), 
    operation(Op, AV, BV, R).
evaluate(operation(Op, A), Vars, R) :- 
    evaluate(A, Vars, AV),
    operation(Op, AV, R).

nonvar(V, T) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).
digits(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.']).
vars(['x', 'y','z', 'X', 'Y', 'Z']).

list_p([], _) --> [].
list_p([H | T], L) -->
  { member(H, L) },
  [H],
  list_p(T, L).

number_p(['-' | T]) --> {digits(L)}, ['-'], list_p(T, L), {T = [_ | _]}.
number_p(E) --> {digits(L)}, list_p(E, L).

token(S) --> {atom_chars(S, R)}, R.

bin_p(op_add) --> ['+'].
bin_p(op_subtract) --> ['-'].
bin_p(op_multiply) --> ['*'].
bin_p(op_divide) --> ['/'].
unary_p(op_negate) --> token("negate").
bin_p(op_and) --> token("&&").
bin_p(op_or) --> token("||").
bin_p(op_xor) --> token("^^").
bin_p(op_impl) --> token("->").
bin_p(op_iff) --> token("<->").
unary_p(op_not) --> ['!'].

expr_infix(variable(Name), _) --> 
	{ nonvar(Name, atom_chars(Name, Chars)), vars(L) },
  list_p(Chars, L), 
  { Chars = [_ | _], atom_chars(Name, Chars) }.
  
expr_infix(const(Value), _) -->
  { nonvar(Value, number_chars(Value, Chars)) },
  number_p(Chars), 
  { Chars = [_ | _], number_chars(Value, Chars) }.
  
expr_infix(operation(Op, A, B), MultiWs) --> 
		['('], 
		whitespaces(MultiWs, []), 
		expr_infix(A, MultiWs), 
		whitespaces(MultiWs, [' ']), 
		bin_p(Op), 
		whitespaces(MultiWs, [' ']), 
		expr_infix(B, MultiWs),
		whitespaces(MultiWs, []), 
		[')'].
		
expr_infix(operation(Op, A), MultiWs) --> unary_p(Op), whitespaces(MultiWs, [' ']), expr_infix(A, MultiWs).

expr_p(E, MultiWs) --> whitespaces(MultiWs, []), expr_infix(E, MultiWs), whitespaces(MultiWs, []).

whitespaces(0, Ws) --> Ws.
whitespaces(1, Ws) --> [' '], whitespaces(1, Ws).
whitespaces(1, Ws) --> [].

infix_str(E, A) :- ground(E), phrase(expr_p(E, 0), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(expr_p(E, 1), C).
 