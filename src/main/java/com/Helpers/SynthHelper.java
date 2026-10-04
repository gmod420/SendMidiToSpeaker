package com.Helpers;

import javax.sound.midi.*;

public final class SynthHelper {
    private Synthesizer synth;
    private Receiver synthReceiver;
    public static ShortMessage noteOn;
    public static ShortMessage noteOff;
    private static final int channel = 9;

    private static final SynthHelper INSTANCE = new SynthHelper();

    private SynthHelper(){
        initSynthesizer();
    }

    public static SynthHelper getInstance(){
        return INSTANCE;
    }

    //TODO- Logging: think about it and implement later
    //TODO: Latency - minimize buffer size and preset latency
    public void initSynthesizer() {
        try {
            if (synth != null && synth.isOpen()) {
                System.out.println("Synthesizer already initialized");
                return;
            }
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synthReceiver = synth.getReceiver();
            System.out.println("Synthesizer initialized");
        }
        catch (MidiUnavailableException e){
            throw new RuntimeException("Issue with synthesizer init: ", e);
        }
    }

    public void closeSynthesizer() {
        if (synthReceiver != null) synthReceiver.close();
        if (synth != null && synth.isOpen()) synth.close();
//        System.out.println("Synthesizer closed");
    }

    public void playDrumAction(int pitch, byte velocity){
//        if (synthReceiver == null) {
//            throw new IllegalStateException("Synthesizer not initialized - call initSynthesizer() first");
//        }
        try {
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, channel, pitch, (int)velocity);
            synthReceiver.send(noteOn, -1); // -1 = send immediately
//            System.out.println("BD Note on, velocity: " + Integer.valueOf(velocity).toString()); //This line is hit

            // Note OFF
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, channel, pitch, 0);
            synthReceiver.send(noteOff, -1);
//            System.out.println("BD note off"); //This line reached
        } catch (InvalidMidiDataException e){
            throw new RuntimeException("Failed to wire signal to synthesizer for aux: ", e);
        }

    }
}
