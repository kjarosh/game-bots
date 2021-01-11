package com.github.kjarosh.mancalabot.bot;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Move;
import com.github.kjarosh.mancalabot.mancala.Player;
import com.github.kjarosh.mancalabot.mancala.Result;
import com.github.kjarosh.mancalabot.mcts.MonteCarloTreeSearchHandler;
import com.github.kjarosh.mancalabot.mcts.Outcome;
import com.github.kjarosh.mancalabot.mcts.Party;

import java.util.Collection;
import java.util.Random;

/**
 * @author Kamil Jarosz
 */
public class MancalaMonteCarloTreeSearchHandler implements MonteCarloTreeSearchHandler<MancalaBoard, Move> {
    private final Random random;
    private final Player mainParty;

    public MancalaMonteCarloTreeSearchHandler(Random random, Player mainParty) {
        this.random = random;
        this.mainParty = mainParty;
    }

    @Override
    public MancalaBoard applyMove(MancalaBoard state, Move move) {
        return state.move(move);
    }

    @Override
    public Collection<Move> possibleMoves(MancalaBoard state, Party party) {
        return state.getPossibleMoves(partyToPlayer(party));
    }

    @Override
    public Outcome simulatePlayout(MancalaBoard state, Party party) {
        MancalaBoard sim = state.copy();
        Player current = partyToPlayer(party);

        while (!sim.isFinished()) {
            Move move = sim.randomMove(current, random);
            sim.moveInPlace(move);
            current = current.opponent();
        }

        Result result = sim.resultFor(mainParty);
        switch (result) {
            case TIE:
                return Outcome.TIE;
            case WIN:
                return Outcome.MAIN_WON;
            case LOSE:
                return Outcome.OPPONENT_WON;
        }

        throw new AssertionError();
    }

    private Player partyToPlayer(Party party) {
        return party == Party.MAIN ? mainParty : mainParty.opponent();
    }
}
