composite(0).
composite(1).

mark_composite(S, E, D) :- S > E, !	.
mark_composite(S, E, D) :- assert(composite(S)), S2 is S + D, mark_composite(S2, E, D).

eratosthenes_sieve(N, MAX_N) :- composite(N), !, N2 is N + 1, eratosthenes_sieve(N2, MAX_N).
eratosthenes_sieve(N, MAX_N) :- 
		SQ is N * N, SQ =< MAX_N, 
		mark_composite(SQ, MAX_N, N),
		N2 is N + 1,
		eratosthenes_sieve(N2, MAX_N).

init(N) :- eratosthenes_sieve(1, N).
prime(N) :- not(composite(N)).

factorize(1, _, []) :- !.
factorize(N, _, [N]) :- prime(N), !.
factorize(N, D, R) :- 
		D2 is D * D, D2 =< N,
		M is mod(N, D), (composite(D); M \= 0), !, 
		D1 is D + 1, 
		factorize(N, D1, R).
factorize(N, D, [D | T]) :- N1 is N / D, factorize(N1, D, T). 

prime_divisors_sorted(1, _, []).
prime_divisors_sorted(N, Last, [H | T]) :- 
		prime(H), H >= Last, 
		prime_divisors_sorted(N1, H, T),
		N is H * N1.

prime_divisors(N, Divisors) :- number(N), !, factorize(N, 2, Divisors).
prime_divisors(N, Divisors) :- prime_divisors_sorted(N, 2, Divisors).

%%%%%%%%%%%%%%%%%

divisors(N, Now, []) :- SQ is Now * Now, SQ > N, !.
divisors(N, Now, T) :-
		M is mod (N, Now), M \= 0, !,
		Now1 is Now + 1,
		divisors(N, Now1, T).
divisors(N, Now, [Now | [H2 | T]]) :- 
		H2 is N / Now, Now1 is Now + 1, 
		divisors(N, Now1, T).

make_d([], _, []) :- !.
make_d([H | T], H, TR) :- !, make_d(T, H, TR).
make_d([H | T], Last, [HR | TR]) :- prime_divisors(H, HR), make_d(T, H, TR).
divisors_divisors(N, D) :- divisors(N, 1, D1), make_d(D1, 0, D).