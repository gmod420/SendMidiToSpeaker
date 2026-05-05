package com.midi;

import javax.sound.midi.*;

public class MidiListener {
    
    public MidiDevice target;
    public MidiDevice.Info[] connectedDevices;

    public void getConnectedDevices(){
        this.connectedDevices = MidiSystem.getMidiDeviceInfo();
    }

    public void listMidiDevices(){
        System.out.println("Available MIDI devices:");
        for (MidiDevice.Info info : connectedDevices) {
            System.out.println("  - " + info.getName() + " | " + info.getDescription());
        }
    }

    public void findMidiDevice(String name) throws MidiUnavailableException{
        for (MidiDevice.Info info : connectedDevices) {
            MidiDevice device = MidiSystem.getMidiDevice(info);
            boolean isInputDevice = device.getMaxTransmitters() != 0;
            boolean isMyDevice = info.getName().toLowerCase().contains(name);

            if (isInputDevice && isMyDevice){
                this.target = device;
                break;
            }
        }
        if (target == null){
            System.out.println("No matching device found with name: " + name);
            throw new MidiUnavailableException();
        } 
    }

    public void attachReciever() throws MidiUnavailableException, InterruptedException{
        try{
            target.open();
            Transmitter transmitter = target.getTransmitter();
            transmitter.setReceiver(new MidiInputReceiver());
            Object lock = new Object();
            synchronized (lock) {
                lock.wait(); // blocks until interrupted
            }
        }
        catch (MidiUnavailableException e){
            System.out.println("Failed in opening target device");
            e.printStackTrace();
        }
        catch (InterruptedException f){
            System.out.println("Failed to set asynchronous listener connection between device and program");
            f.printStackTrace();    
        }

    }
}
