package com.prohelion.canbus.model;

import java.util.ArrayList;

//import org.apache.commons.codec.binary.Hex;

public class UdpPacket {

    private byte length;
    private byte[] data;
    private byte[] busId;
    private byte[] senderId;
    private ArrayList<CanPacket> canPackets;
    
    public UdpPacket(byte length, byte[] data, byte[] busId, byte[] senderId) {
        this.length = length;
        this.data = data;
        this.busId = busId;
        this.senderId = senderId;
    }

    public ArrayList<CanPacket> getCanPackets() {
        return canPackets;
    }

    public void setCanPackets(ArrayList<CanPacket> canPackets) {
        this.canPackets = canPackets;
    }

    @Override
    public String toString() {
        /*return "UdpPacket [busId=" + Hex.encodeHexString(busId) + ", senderId=" +
            Hex.encodeHexString(senderId) + ", canPackets=" + canPackets + "]"; */
    	return null;
    }
}
