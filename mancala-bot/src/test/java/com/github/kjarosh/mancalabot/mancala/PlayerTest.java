package com.github.kjarosh.mancalabot.mancala;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kamil Jarosz
 */
@ExtendWith(MockitoExtension.class)
class PlayerTest {
    @Mock
    Supplier<Object> supplierA;

    @Mock
    Supplier<Object> supplierB;

    @Mock
    Runnable runnableA;

    @Mock
    Runnable runnableB;

    @Test
    void opponent() {
        assertThat(Player.PLAYER_A.opponent())
                .isSameAs(Player.PLAYER_B);
        assertThat(Player.PLAYER_B.opponent())
                .isSameAs(Player.PLAYER_A);
    }

    @Test
    void getFor() {
        Object resultA = new Object();
        Mockito.when(supplierA.get()).thenReturn(resultA);

        Object resultB = new Object();
        Mockito.when(supplierB.get()).thenReturn(resultB);

        assertThat(Player.PLAYER_A.getFor(supplierA, supplierB))
                .isEqualTo(resultA);

        Mockito.verify(supplierA, Mockito.times(1)).get();
        Mockito.verify(supplierB, Mockito.never()).get();

        assertThat(Player.PLAYER_B.getFor(supplierA, supplierB))
                .isEqualTo(resultB);

        Mockito.verify(supplierA, Mockito.times(1)).get();
        Mockito.verify(supplierB, Mockito.times(1)).get();
    }

    @Test
    void runFor() {
        Player.PLAYER_A.runFor(runnableA, runnableB);

        Mockito.verify(runnableA, Mockito.times(1)).run();
        Mockito.verify(runnableB, Mockito.never()).run();

        Player.PLAYER_B.runFor(runnableA, runnableB);

        Mockito.verify(runnableA, Mockito.times(1)).run();
        Mockito.verify(runnableB, Mockito.times(1)).run();
    }
}
