# lgs - loan graph simplification

## What is a loan graph?
A loan graph is an ordered pair `G = (V, A)`, where `V` is the set of nodes and `A` is the set of directed arcs over `V`. Also, in LGS we are given a weight function `w` returning (strictly) positive weights for all arcs present in `A`. The idea is that each node represents an entity in a financial setting where a node may lend some number of unit resources to other nodes. For examplel, if `u` lends 11 resource units to `v`, the arc set `A` will contain an arc `(u, v)` with `w(u, v) = 11`. The equity `e(u)` of `u` is defined as the sum of resources lent to another nodes minus the sum of resources borrowed from the other nodes.

When constructing a loan graph by adding directed arcs, for each arc `a = (u, v)` with weight `w = w(u, v)`, the equity of `u` wiil grow by `w` resource units and the equity of `v` will reduce by `w` resource units. If we start constructing a loan graph from a fully disconnect loan graph, before and after adding each arc, the sum of equities over all nodes will always be zero.

Two loan graphs `G = (V, A)` and `D = (W, B)` are said to be ***equivalent***, if there exists a injection `f` such that the following condition holds

For all `u` in `V` we have `e(u) = e(f(u))`.

Since we exclude self loops, the maximum number of arcs in a loan graph `G` is `n^2 - n`, where `n = |V|`. It is, however, possible to remove/rearrange the arcs such that the number of arcs will be linear in the number of nodes, and that is what this library aims to achieve.

For more thorough discussion, see [this WordPres blog](https://coderodde.wordpress.com/2015/07/20/minimizing-the-amount-of-bank-transactions-in-a-static-loan-graph/).

## Using `lgs`

First, you need a graph:
```
import net.coderodde.loan.model.Graph;
...
Graph graph = new Graph();

Node nodeA = new Node("A");
Node nodeB = new Node("B");
Node nodeC = new Node("C");

graph.add(nodeA);
graph.add(nodeB);
graph.add(nodeC);

// Create "empty" arcs:
nodeA.connectToBorrower(nodeB);
nodeB.connectToBorrower(nodeC);
nodeC.connectToBorrower(nodeA);

// Set arc weights:
nodeA.setWeightTo(nodeB, 3L);
nodeB.setWeightTo(nodeC, 2L);
nodeC.setWeightTo(nodeA, 1L);

// If needed, copy with both nodes and corresponding arcs:
Graph copyGraph  = new Graph(graph);

```
Then, you are almost done:
```
import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.LinearSimplifier;
...
Algorithm alg = new LinearSimplifier();
Graph result = alg.simplicy(graph);
// Should return true:
result.isEquivalent(graph);
```
