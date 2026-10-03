package com.runner;

import com.midi.MidiListener;

import java.util.Scanner;

public class Runner {
    public static void main(String[] args) throws Exception {
        MidiListener midiListener = new MidiListener();
        midiListener.getConnectedDevices();
        midiListener.listMidiDevices();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter device name: ");
        String deviceName = scanner.nextLine().trim(); //prompt user input

        midiListener.findMidiDevice(deviceName);
        midiListener.attachReciever();
    }
}
