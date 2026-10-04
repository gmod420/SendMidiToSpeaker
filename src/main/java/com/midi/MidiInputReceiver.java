package com.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import com.Helpers.AlesisNitro_NoteMapper;
import com.Helpers.SynthHelper;

//TODO: Latency issues! See how to speed up even further!!

public class MidiInputReceiver implements Receiver{
//    private final static byte pitch = ACOUSTIC_BASS_DRUM;

    @Override
    public void send(MidiMessage message, long timeStamp) { //In current implementation this gets called every time a message arrives
        byte[] note = message.getMessage();
        byte noteNumber = note[1];
        byte velocity = note[2];
        int pitch = AlesisNitro_NoteMapper.getPitch(noteNumber);
        velocity = AlesisNitro_NoteMapper.boostDrumVelocity(velocity, noteNumber);
        SynthHelper.getInstance().playDrumAction(pitch, velocity);
    }

    @Override
    public void close(){
    }
}
