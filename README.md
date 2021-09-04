# lgs - loan graph simplification

## What is a loan graph?
A loan graph is an ordered pair `G = (V, A)`, where `V` is the set of nodes and `A` is the set of directed arcs over `V`. Also, in LGS we are given a weight function `w` returning (strictly) positive weights for all arcs present in `A`. The idea is that each node represents an entity in a financial setting where a node may lend some number of unit resources to other nodes. For examplel, if `u` lends 11 resource units to `v`, the arc set `A` will contain an arc `(u, v)` with `w(u, v) = 11`. The eqyity `e(u)` of `u` is defined as the sum of lent resources lent to another nodes minus the sum of resources borrowed from another nodes.

When constructing a loan graph by adding directed arcs, for each arc `a = (u, v)` with weight `w = w(u, v)`, the equity of `u` wiil grow by `w` resource untis and the equity of `v` will reduce by `w` resource units. If we start constructing a loan graph from a fully disconnect loan graph, before and after adding each arc, the sum of equities over all nodes will always be zeroe. 

Two loan graphs `G = (V, A)` and `D = (W, B)` are ***equivalent***, if all the following hold
1. `|V| = |W|`,
2. For all `u` in `V` we have `e(u) = e(f(u))`.

Since we exclude self loops, the maximum number of arcs in a loan graph `G` is `n^2 - n`, where `n = |V|`. It is, however, possible to remove/rearrange the arcs such that the number of arcs will be linear in the number of nodes, and that is what this library aims to achieve.
