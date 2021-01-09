package com.github.kjarosh.mancalabot.bot;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mancala.Player;
import com.github.kjarosh.mancalabot.mancala.Result;
import com.github.kjarosh.mancalabot.mcts.MonteCarloTreeSearchHandler;
import com.github.kjarosh.mancalabot.mcts.Party;

import java.util.Collection;
import java.util.Random;

/**
 * @author Kamil Jarosz
 */
public class MancalaMonteCarloTreeSearchHandler implements MonteCarloTreeSearchHandler<MancalaBoard, Move> {
    private final Random random;
    private final Player player;

    public MancalaMonteCarloTreeSearchHandler(Random random, Player player) {
        this.random = random;
        this.player = player;
    }

    @Override
    public MancalaBoard applyMove(MancalaBoard state, Move move) {
        return state.move(move);
    }

    @Override
    public Collection<Move> possibleMoves(MancalaBoard state, Party party) {
        return state.getPossibleMoves(party == Party.MAIN ? player : player.opponent());
    }

    @Override
    public Party simulatePlayout(MancalaBoard state, Party party) {
        MancalaBoard sim = state.copy();
        Player current = party == Party.MAIN ? player : player.opponent();

        while (sim.hasMove(current)) {
            Move move = sim.randomMove(current, random);
            sim.moveInPlace(move);
            current = current.opponent();
        }

        return sim.resultFor(player) == Result.WIN ? Party.MAIN : Party.OPPONENT;
    }
}
