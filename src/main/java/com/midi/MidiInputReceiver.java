package com.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import com.instrumentBuilder.BassDrum;

public class MidiInputReceiver implements Receiver{
    // TODO: Implement class for MidiInputListener
    // Understand working

    @Override
    public void send(MidiMessage message, long timeStamp) { //In current implementation this gets called every time a message arrives
        //Filter based on note number recieved
        byte[] note = message.getMessage();
        int noteNumber = note[1];
        int velocity = note[2];
        messageRouter(noteNumber, velocity);
    }

    public void messageRouter(int noteNumber, int velocity){
        //TODO: Try switch case or dynamic hashmap to runnable - for now simple if
        BassDrum bassDrum = new BassDrum();
        bassDrum.initSynthesizer();
        if(noteNumber == 36) {
            bassDrum.setVelocity(velocity);
            bassDrum.playBassDrumHit();
            System.out.println("Bass Drum Struck");
        }
    }

    @Override
    public void close(){}
}
