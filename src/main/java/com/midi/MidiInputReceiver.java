package com.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import com.Helpers.SynthHelper;

import static jm.constants.DrumMap.ACOUSTIC_BASS_DRUM;

//TODO: Add new package to map input noteNumber to corresponding output pitch
//TODO: Latency issues! See how to speed up even further!!

public class MidiInputReceiver implements Receiver{
    private final static byte pitch = ACOUSTIC_BASS_DRUM;

    @Override
    public void send(MidiMessage message, long timeStamp) { //In current implementation this gets called every time a message arrives
        byte[] note = message.getMessage();
        byte noteNumber = note[1];
        byte velocity = note[2];
        trigger(noteNumber, velocity);
    }

    public void trigger(byte noteNumber, byte velocity) {
        if (noteNumber == 36) {
            SynthHelper.getInstance().playDrumAction(pitch, velocity);
        }
    }

    @Override
    public void close(){
    }
}
