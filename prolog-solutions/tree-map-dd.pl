% node(K, V, Prior, L, R).
% null.

% split(Tree, K, L, R).
split(null, _, null, null).
split(node(K1, V1, Prior1, L1, R1), K, LTree, node(K1, V1, Prior1, NewL, R1)) :-
	K1 >= K, !,
	split(L1, K, LTree, NewL).
split(node(K1, V1, Prior1, L1, R1), K, node(K1, V1, Prior1, L1, NewR), RTree) :-
	split(R1, K, NewR, RTree).

% merge(L, R, Tree).
merge(null, R, R) :- !.
merge(L, null, L) :- !.
merge(node(K1, V1, P1, L1, R1), node(K2, V2, P2, L2, R2), node(K1, V1, P1, L1, Res)) :-
	P1 > P2, !,
	merge(R1, node(K2, V2, P2, L2, R2), Res).
merge(node(K1, V1, P1, L1, R1), node(K2, V2, P2, L2, R2), node(K2, V2, P2, Res, R2)) :-
	merge(node(K1, V1, P1, L1, R1), L2, Res).

map_get(node(K1, V1, P1, L, R), K1, V1) :- !.
map_get(node(K1, V1, P1, L, R), K, V) :- K1 > K, !, map_get(L, K, V).
map_get(node(K1, V1, P1, L, R), K, V) :- map_get(R, K, V).

map_put(Tree, K, V, Res) :-
	split(Tree, K, Less, GrEq),
	K1 is K + 1, % :NOTE: + 1
	split(GrEq, K1, Eq, Gr),

	rand_int(100000, P), % :NOTE: birthday
	merge(Less, node(K, V, P, null, null), Tree1),
	merge(Tree1, Gr, Res).

map_remove(Tree, K, Res) :-
% :NOTE: copy-paste
	split(Tree, K, Less, GrEq),
	K1 is K + 1,
	split(GrEq, K1, Eq, Gr),
	merge(Less, Gr, Res).

map_build([], null).
map_build([(K, V) | T], Res) :- map_build(T, Tree), map_put(Tree, K, V, Res).

%%%%%%%%%%%%%
getCeiling(node(K1, V1, P1, L1, R1), K1, K1, V1) :- !.
getCeiling(node(K1, V1, P1, L1, R1), K, ExactK, V) :- K > K1, !, getCeiling(R1, K, ExactK, V).
getCeiling(node(K1, V1, P1, L1, R1), K, ExactK, V) :- getCeiling(L1, K, ExactK, V), !.
getCeiling(node(K1, V1, P1, L1, R1), K, K1, V1). 

map_getCeiling(Tree, K, V) :- getCeiling(Tree, K, NewK, V).

% :NOTE: single-pass
map_putCeiling(Tree, K, V, R) :- getCeiling(Tree, K, ExactK, OldV), !, map_put(Tree, ExactK, V, R).
map_putCeiling(Tree, K, V, Tree).


% node(Keys, Children).

%find_ch([(K1, V1) | T], [C1 | T1], K, C1, T1) :- K < K1, !.
%find_ch([(K1, V1)], [C1, C2], K, C2, [C1]).
%find_ch([(K1, V1), (K2, V2)], [C1, C2, C3], K, C3, [C1, C2]) :- K > K2, !.
%find_ch([(K1, V1), (K2, V2)], [C1, C2, C3], K, C2, [C1, C3]).

%insert_ch([(K1, V1) | T], [C1 | T1], node([(K, V), Ch]), [node([(K, V), Ch]), C1 | T1]) :- K < K1, !.
%insert_ch([(K1, V1)], [C1], node([(K, V), Ch]), [C1, node([(K, V), Ch])]).
%insert_ch([(K1, V1), (K2, V2)], [C1, C2], node([(K, V), Ch]), [C1, C2, node([(K, V), Ch])]) :- K > K2, !.
%insert_ch([(K1, V1), (K2, V2)], [C1, C2], node([(K, V), Ch]), [C1, node([(K, V), Ch]), C2]).

%put(node([(K, V1), (K2, V2)], Cs), K, V, node([(K, V), (K2, V2)], Cs), false).
%put(node([(K1, V1), (K, V2)], Cs), K, V, node([(K1, V1), (K, V)], Cs), false).
%put(node(Ks, Cs), K, V, node(Ks, NewCh), false) :- find_ch(Ks, Cs, K, CH, Rest), put(CH, K, V, Res, false), insert_ch(Ks, Rest, Res, NewCh).
%put(node(Ks, Cs), K, V, node(Ks, NewCh), false) :- find_ch(Ks, Cs, K, CH, Rest), put(CH, K, V, Res, true),

% put(Tree, K, V, Res, Overflow)
%put(null, K, V, node(K, V, null, null, null, null, null), false).

%put(node(K, V1, K2, V2, L, M, R), K, V, node(K, V, K2, V2, L, M, R), false) :- !.
%put(node(K1, V1, K, V2, L, M, R), K, V, node(K1, V1, K, V, L, M, R), false) :- !.

%put(node(K1, V1, null, null, null,null,null), K, V, node(K1, V1, K, V, null,null,null), false) :- K > K1, !.
%put(node(K1, V1, null, null, null,null,null), K, V, node(K, V, K1, V1, null,null,null), false) :- K < K1, !.
%put(node(K1, V1, K2, V2, null,null,null), K, V, node(K2, V2, null,null, node(K1,V1,null,null,null,null,null),null,node(K,V,null,null,null,null,null)), true) :- K > K2, !.
%put(node(K1, V1, K2, V2, null,null,null), K, V, node(K1, V1, null,null, node(K,V,null,null,null,null,null),null,node(K2,V2,null,null,null,null,null)), true) :- K < K1, !.
%put(node(K1, V1, K2, V2, null,null,null), K, V, node(K, V, null,null, node(K1,V1,null,null,null,null,null),null,node(K2,V2,null,null,null,null,null)), true) :- K < K1, !.

%put(node(K1, V1, null, null, L, null, R), K, V, node(K1, V1, null, null, L, null, Res), false) :- K > K1, put(R, K, V, Res, false), !.
%put(node(K1, V1, null, null, L, null, R), K, V, node(K1, V1, RK, RV, L, RL, RR), false) :- K > K1, put(R, K, V, node(RK, RV, null, null, RL, null, RR), true), !.

%put(node(K1, V1, null, null, L, null, R), K, V, node(K1, V1, null, null, Res, null, R), false) :- K < K1, put(L, K, V, Res, false), !.
%put(node(K1, V1, null, null, L, null, R), K, V, node(RK, RV, K1, V1, RL, RR, R), false) :- K < K1, put(L, K, V, node(RK, RV, null, null, RL, null, RR), true), !.

%put(node(K1, V1, K2, V2, L, M, R), K, V, node(K1, V1, K2, V2, L, M, Res), false) :- K > K2, put(R, K, V, Res, false), !.
%put(node(K1, V1, K2, V2, L, M, R), K, V, node(K2, V2, null, null, node(K1, V1, null,null, L, M), null, Res, true)) :- K > K2, put(R, K, V, Res, true), !.

%put(node(K1, V1, K2, V2, L, M, R), K, V, node(K1, V1, K2, V2, Res, M, R), false) :- K < K1, put(L, K, V, Res, false), !.
%put(node(K1, V1, K2, V2, L, M, R), K, V, node(K1, V1, null,null, Res, null, node(K2, V2, null,null, M, null, R)), true) :- K < K1, put(L, K, V, Res, true), !.

%put(node(K1, V1, K2, V2, L, M, R), K, V, node(K1, V1, K2, V2, L, Res, R), false) :- put(M, K, V, Res, false), !.
%put(node(K1, V1, K2, V2, L, M, R), K, V, node(RK, RV, null,null, node(K1, V1, null,null,L,null,RL), null, node(K2, V2, null,null, RR, null, R)), true) :-
%	put(M, K, V, node(RK, RV, null, null, RL, null, RR), true), !.

%map_put(Tree, K, V, R) :- put(Tree, K, V, R, _).