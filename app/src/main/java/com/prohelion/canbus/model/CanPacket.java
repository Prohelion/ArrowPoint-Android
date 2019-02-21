package com.prohelion.canbus.model;

import java.nio.ByteBuffer;
import java.util.Date;

public class CanPacket {
    
    private byte[] id;
    private Integer idBase10;
    //private DateTime timestamp;
    private Date timestamp;
    private boolean extended;
    private boolean rtr;
    private byte length;
    private byte[] data;
    
    public CanPacket() {
        
    }
    
  
    public CanPacket(byte[] id, boolean extended, boolean rtr, byte length, byte[] data) {
        //this.timestamp = new DateTime();
    	this.timestamp = new Date();
        this.id = id;
        
        this.idBase10 = ByteBuffer.wrap(id).getInt();
        this.extended = extended;
        this.rtr = rtr;
        this.length = length;
        this.data = data;
    }

    public int getIdBase10() {
        return idBase10;
    }
    
    public void setIdBase10(byte[] id) {
        this.idBase10 = ByteBuffer.wrap(id).getInt();
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isRtr() {
        return rtr;
    }

    public void setRtr(boolean rtr) {
        this.rtr = rtr;
    }

    public byte getLength() {
        return length;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
    public Integer getBaseId() {
        return (idBase10 >>> 5) << 5; 
    }

    public Integer getOffset() {
        return idBase10 & 0xff; 
    }

    public float getDataSegmentOneAsFloat() {
        int intbits = (data[0] & 0xFF) 
            | ((data[1] & 0xFF) << 8) 
            | ((data[2] & 0xFF) << 16) 
            | ((data[3] & 0xFF) << 24);
        return Float.intBitsToFloat(intbits);
    }

    public float getDataSegmentTwoAsFloat() {
        int intbits = (data[4] & 0xFF) 
            | ((data[5] & 0xFF) << 8) 
            | ((data[6] & 0xFF) << 16) 
            | ((data[7] & 0xFF) << 24);
        return Float.intBitsToFloat(intbits);
    }

    public int getDataSegmentOneAsInt() {
        return (data[0] & 0xFF)
            | ((data[1] & 0xFF) << 8) 
            | ((data[2] & 0xFF) << 16) 
            | ((data[3] & 0xFF) << 24);
    }

    public int getDataSegmentTwoAsInt() {
        return (data[4] & 0xFF)
            | ((data[5] & 0xFF) << 8) 
            | ((data[6] & 0xFF) << 16) 
            | ((data[7] & 0xFF) << 24);
    }
    
    
    public int getTwoDataSegmentsAsInt(int bytePosition) {
    	return (data[bytePosition] & 0xFF)| ((data[bytePosition + 1] & 0xFF) << 8)  ;
    }
    
    public int getDataSegmentAsInt(int bytePosition) {
    	return (data[bytePosition] & 0xFF);
    }

    public byte getBit(int bytePosition, int bitPosition)
    {
       return (byte) ((data[bytePosition] >> bitPosition) & 1);
    }
    
    @Override
    public String toString() {
    	return null;
    }
}
