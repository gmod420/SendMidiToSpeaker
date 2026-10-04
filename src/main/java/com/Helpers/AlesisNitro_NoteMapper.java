package com.Helpers;

import java.util.function.Function;

import static jm.constants.DrumMap.*;

public class AlesisNitro_NoteMapper {
    //Declaration of alesis nitro map
    private final static byte kickDrum = 36;
    private final static byte snare = 38;
    private final static byte snareRim = 40;
    private final static byte tom1 = 48;
    private final static byte tom1Rim = 50;
    private final static byte tom2 = 45;
    private final static byte tom2Rim = 47;
    private final static byte tom3 = 43;
    private final static byte tom3Rim = 58;
    private final static byte tom4 = 41;
    private final static byte tom4Rim = 39;

    private final static byte ride = 51;
    private final static byte crash1 = 49;
    private final static byte crash2 = 57;
    private final static byte hiHatOpen = 46;
    private final static byte hiHatHalfOpen = 23;
    private final static byte hiHatClosed = 42;
    private final static byte hiHatPedal = 44;
    private final static byte splash = 21;

    private static final byte[] MAP = new byte[128];

    //TODO: Open hi hat not working properly, check map
    //TODO: Experiment with other pitch values possible - look for some lib

    static {
        for (int i = 0; i < MAP.length; i++){
            MAP[kickDrum] = ACOUSTIC_BASS_DRUM;
            MAP[snare] = ACOUSTIC_SNARE;
            MAP[snareRim] = ELECTRIC_SNARE;
            MAP[tom1] = HIGH_TOM;
            MAP[tom2] = LOW_MID_TOM;
            MAP[tom3] = LOW_FLOOR_TOM;
            MAP[ride] = RIDE_CYMBAL_1;
            MAP[crash1] = CRASH_CYMBAL_1;
            MAP[hiHatOpen] = OPEN_HI_HAT;
            MAP[hiHatClosed] = CLOSED_HI_HAT;
            MAP[hiHatPedal] = PEDAL_HI_HAT;
            MAP[splash] = SPLASH_CYMBAL;
        }
    }

    public static int getPitch(int noteNumber){
        return MAP[noteNumber];
    }

    public static byte boostDrumVelocity(byte velocity, int noteNumber){
        if(noteNumber == kickDrum && velocity < 100){
            velocity = (byte) (velocity + 20);
        }
        return velocity;
    }
}
