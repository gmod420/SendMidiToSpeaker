package com.Helpers;

import javax.sound.midi.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class SynthHelper {
    private Synthesizer synth;
    private Receiver synthReceiver;
    private SourceDataLine line;
    private Map<String, Object> params;
    public float samplingFreq = 44100;
    public int bitStream = 16;
    public int channels = 2; // 16-bit stereo
    public int bufferSize = 2048;
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

    public void setAudioFormat(float samplingFreq, int bitStream, int channels){
        try {
            AudioFormat format = new AudioFormat(samplingFreq, bitStream, channels, true, false);
            line = AudioSystem.getSourceDataLine(format);
            line.open(format, bufferSize);
            line.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException("Unable to set synthesizer format: ", e);
        }
        params = new HashMap<>();
        params.put("latency", 20_000L); //20k microseconds
        params.put("interpolation", "linear"); //Linear interpoler (maybe faster)
        params.put("max polyphony", 32);
    }

    //TODO- Logging: think about it and implement later
    public void initSynthesizer() {
        try {
            if (synth != null && synth.isOpen()) {
                System.out.println("Synthesizer already initialized");
                return;
            }
            synth = MidiSystem.getSynthesizer();
            openSynthesizer(bufferSize);
            if(synth != null) {
                synthReceiver = synth.getReceiver();
            } else {throw new RuntimeException("Unable to init synthesizer");}
            System.out.println("Synthesizer initialized");
        }
        catch (MidiUnavailableException e){
            throw new RuntimeException("Issue with synthesizer init: ", e);
        }
    }

    //Opens synthesizer by reflection to minimize latency and buffer size
    public void openSynthesizer(int bufferSize){
        try {
            setAudioFormat(samplingFreq, bitStream, channels);
            synth.getClass()
                    .getMethod("open", SourceDataLine.class, Map.class)
                    .invoke(synth, line, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException f){
            throw new RuntimeException("Unable to initialize synth with 20ms latency", f);
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
//            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, channel, pitch, (int)velocity);
            synthReceiver.send(noteOn, -1); // -1 = send immediately
//            System.out.println("BD Note on, velocity: " + Integer.valueOf(velocity).toString()); //This line is hit

            // Note OFF
//            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, channel, pitch, 0);
            synthReceiver.send(noteOff, -1);
//            System.out.println("BD note off"); //This line reached
        } catch (InvalidMidiDataException e){
            throw new RuntimeException("Failed to wire signal to synthesizer for aux: ", e);
        }

    }
}
