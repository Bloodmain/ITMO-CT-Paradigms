% node(K, V, L, R, Bal, Height).

max(A, B, A) :- A > B, !.
max(A, B, B).

update(node(K, V, null, null, Bl, H), node(K, V, null, null, 0, 0)).
update(node(K, V, null, node(BK, BV, BL, BR, BBal, BH), Bal, Heg),
				node(K, V, null, node(BK, BV, BL, BR, BBal, BH), Bl, H)) :- H is BH + 1, Bl is -BH.
update(node(K, V, node(AK, AV, AL, AR, ABal, AH), null, Bal, Heg),
				node(K, V, node(AK, AV, AL, AR, ABal, AH), null, AH, H)) :- H is AH + 1.
update(node(K, V, node(AK, AV, AL, AR, ABal, AH), node(BK, BV, BL, BR, BBal, BH), Bal, Heg),
				node(K, V, node(AK, AV, AL, AR, ABal, AH), node(BK, BV, BL, BR, BBal, BH), Bl, H)) :-
				max(AH, BH, H1), H is H1 + 1, Bl is AH - BH.

rotateLeft(node(AK, AV, AL, node(BK, BV, BL, BR, BBal, BH), ABal, AH), Res) :- 
	  update(node(AK, AV, AL, BL, null, null), ResL), update(node(BK, BV, ResL, BR, null,null), Res).

rotateRight(node(AK, AV, node(BK, BV, BL, BR, BBal, BH), AR, ABal, AH), Res) :- 
		update(node(AK, AV, BR, AR, null, null), ResR), update(node(BK, BV, BL, ResR, null, null), Res).
		
bigRotateLeft(node(AK, AV, AL, AR, ABal, AH), Res) :- rotateRight(AR, NewR), update(node(AK, AV, AL, NewR, null, null), Upd), rotateLeft(Upd, Res).
bigRotateRight(node(AK, AV, AL, AR, ABal, AH), Res) :- rotateLeft(AL, NewL), update(node(AK, AV, NewL, AR, null, null), Upd), rotateRight(Upd, Res).

rotate(node(AK, AV, AL, node(BK, BV, BL, BR, BBal, BH), -2, AH), Res) :- (BBal = -1; BBal = 0), rotateLeft(Tree, Res), !.
rotate(node(AK, AV, AL, node(BK, BV, BL, BR, BBal, BH), 2, AH), Res) :- (BBal = 1; BBal = 0), rotateRight(Tree, Res), !.
rotate(node(AK, AV, AL, node(BK, BV, BL, BR, 1, BH), -2, AH), Res) :- bigRotateLeft(Tree, Res), !.
rotate(node(AK, AV, AL, node(BK, BV, BL, BR, -1, BH), 2, AH), Res) :- bigRotateRight(Tree, Res), !.
rotate(Tree, Tree).

map_put(null, K, V, node(K, V, null,null,0,0)) :- !.
map_put(node(K, V1, L, R, Bal, H), K, V, node(K, V, L,R,Bal,H)) :- !.
map_put(node(AK, AV, AL, AR, ABal, AH), K, V, R) :- 
	K < AK, !, map_put(AL, K, V, Res),
	update(node(AK, AV, Res, AR,null,null), Upd), rotate(Upd, R). 
map_put(node(AK, AV, AL, AR, ABal, AH), K, V, R) :- 
	map_put(AR, K, V, Res),
	update(node(AK, AV, AL, Res,null,null), Upd), rotate(Upd, R). 

find_nearest(node(AK, AV, AL, null, AB, AH), node(AK, AV, AL, null, AB, AH)). 
find_nearest(node(AK, AV, null, AR, AB, AH), node(AK, AV, null, AR, null, AB, AH)). 
find_nearest(node(AK, AV, AL, AR, AB, AH), Res) :- find_nearest(AR, Res). 

map_remove(null, K, null) :- !.
map_remove(node(K, AV, null, null, AB, AH), K, null, K, AV) :- !.
map_remove(node(K, AV, AL, AR, AB, AH), K, Res, K, AV) :- map_remove(node(K, NV, NL, NR, NB, NH), K, TmpR), 
		update(node(NewK, NewV, NewR, AR, null,null), Upd), rotate(Upd, Res), !.

map_remove(node(AK, AV, AL, AR, AB, AH), K, Res) :-  K < AK, !, map_remove(AL, K, NewL), update(node(AK, AV, NewL, AR, null,null), Upd), rotate(Upd, Res), !.
map_remove(node(AK, AV, AL, AR, AB, AH), K, Res) :- map_remove(AR, K, NewR), update(node(AK, AV, AL, NewR, null,null), Upd), rotate(Upd, Res).

map_build([], null).
map_build([(K, V) | T], Res) :- map_build(T, Tree), map_put(Tree, K, V, Res).

map_get(node(K1, V1, _,_,_,_), K1, V1) :- !.
map_get(node(K1, V1, L, R, Bal, Height), K, V) :- K1 > K, !, map_get(L, K, V).
map_get(node(K1, V1, L, R, Bal, Height), K, V) :- map_get(R, K, V).
