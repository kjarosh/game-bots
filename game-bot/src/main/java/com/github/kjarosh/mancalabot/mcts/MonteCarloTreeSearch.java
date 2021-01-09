package com.github.kjarosh.mancalabot.mcts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @param <S> state
 * @param <M> move
 * @author Kamil Jarosz
 */
public class MonteCarloTreeSearch<S, M> {
    private final MonteCarloTreeSearchHandler<S, M> handler;
    private final SelectionStrategy selectionStrategy;

    private final InternalNode root;

    public MonteCarloTreeSearch(
            SelectionStrategy selectionStrategy,
            MonteCarloTreeSearchHandler<S, M> handler,
            S state) {
        this.selectionStrategy = selectionStrategy;
        this.handler = handler;
        this.root = new InternalNode(null, Party.MAIN, state);
    }

    public void nextRound() {
        // selection
        InternalNode leaf = this.root.selectLeaf();

        // expansion
        if (leaf.total.get() > 0) leaf.expandChildren();

        // simulation
        Party winner = handler.simulatePlayout(leaf.state, leaf.party);

        // backpropagation
        leaf.backpropagate(winner);
    }

    public double getWinProbability(Party party) {
        return root.getWinProbability(party);
    }

    public Map<M, Double> getWinProbabilities(Party party) {
        Map<M, Double> prob = new HashMap<>();
        root.children.forEach((move, node) -> prob.put(move, node.getWinProbability(party)));
        return prob;
    }

    public long getTotalSimulations() {
        return root.getTotal();
    }

    public int getMaxDepth() {
        return root.getMaxDepth();
    }

    public double getAverageDepth() {
        return 1d * root.sumDepths() / root.countLeaves();
    }

    private class InternalNode implements Node {
        private final InternalNode parent;
        private final Party party;
        private final Map<M, InternalNode> children = new HashMap<>();
        private final S state;

        private final AtomicLong won = new AtomicLong(0);
        private final AtomicLong total = new AtomicLong(0);

        private volatile boolean expanded = false;

        private InternalNode(InternalNode parent, Party party, S state) {
            this.state = state;
            this.parent = parent;
            this.party = party;
        }

        private void expandChildren() {
            if (expanded) return;
            synchronized (children) {
                if (expanded) return;

                handler.possibleMoves(state, party).forEach(move -> {
                    S newState = handler.applyMove(state, move);
                    InternalNode child = new InternalNode(this, party.opponent(), newState);
                    children.put(move, child);
                });
                expanded = true;
            }
        }

        private InternalNode selectLeaf() {
            if (!expanded) return this;
            if (children.size() == 0) return this;

            InternalNode selectedChild = selectionStrategy.select(
                    Collections.unmodifiableCollection(children.values()));
            return selectedChild.selectLeaf();
        }

        private void backpropagate(Party winner) {
            if (party == winner) {
                won.incrementAndGet();
            }
            total.incrementAndGet();

            if (parent != null) {
                parent.backpropagate(winner);
            }
        }

        @Override
        public Party getParty() {
            return party;
        }

        @Override
        public Optional<Node> getParent() {
            return Optional.ofNullable(parent);
        }

        @Override
        public long getWon() {
            return won.get();
        }

        @Override
        public long getTotal() {
            return total.get();
        }

        private double getWinProbability(Party party) {
            double p = 1d * won.get() / total.get();
            if (this.party != party) {
                return 1 - p;
            } else {
                return p;
            }
        }

        public int getMaxDepth() {
            AtomicInteger maxDepth = new AtomicInteger(0);
            children.forEach((m, n) -> {
                int md = n.getMaxDepth();
                if (md > maxDepth.get()) {
                    maxDepth.set(md);
                }
            });
            return 1 + maxDepth.get();
        }

        public long sumDepths() {
            return children.size() + children.values()
                    .stream()
                    .mapToLong(InternalNode::sumDepths)
                    .sum();
        }

        public long countLeaves() {
            if (children.size() == 0) return 1;

            return children.values()
                    .stream()
                    .mapToLong(InternalNode::countLeaves)
                    .sum();
        }
    }
}
