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
	rand_int(100000, P),
	split(Tree, K, Less, GrEq),
	K1 is K + 1,
	split(GrEq, K1, Eq, Gr),
	merge(Less, node(K, V, P, null, null), Tree1),
	merge(Tree1, Gr, Res).

map_remove(Tree, K, Res) :-
	split(Tree, K, Less, GrEq),
	K1 is K + 1,
	split(GrEq, K1, Eq, Gr),
	merge(Less, Gr, Res).

map_build([], null).
map_build([(K, V) | T], Res) :- map_build(T, Tree), map_put(Tree, K, V, Res).
