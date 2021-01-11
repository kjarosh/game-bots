package com.github.kjarosh.mancalabot.gui.history;

import com.github.kjarosh.mancalabot.mancala.MancalaBoard;
import com.github.kjarosh.mancalabot.mancala.Move;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Kamil Jarosz
 */
@Getter
@Builder
public class HistoryEntry {
    private final Move move;
    private final MancalaBoard boardAfterMove;
}
