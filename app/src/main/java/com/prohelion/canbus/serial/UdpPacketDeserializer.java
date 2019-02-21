package com.prohelion.canbus.serial;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

import com.prohelion.canbus.model.CanPacket;
import com.prohelion.canbus.model.UdpPacket;
/*import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.serializer.Deserializer;
import org.springframework.stereotype.Component;
*/

//@Component
//public class UdpPacketDeserializer implements Deserializer<UdpPacket> {
public class UdpPacketDeserializer {

    public UdpPacket deserialize(InputStream inputStream) throws IOException {
        byte[] b = new byte[512];
        
        DataInputStream dis = new DataInputStream(inputStream);
        int nread, total = 0;
        while ((nread = dis.read(b)) >= 0) {
            total += nread;
        }
        
        /*
         * Packet structure is:
         * 
         * +-+------------------+-+----------------------+--------------+---------+----------+---------+
         * |8|56 - Bus Identifer|8|56 - Client Identifier|32 - Identifer|8 - Flags|8 - Length|64 - Data|
         * +-+------------------+-+----------------------+--------------+---------+----------+---------+
         * 
         */
        //byte[] datastream = ArrayUtils.subarray(b, 0, total);
        byte[] datastream = Arrays.copyOfRange(b, 0, total);
        
        // First byte is padding
        //byte[] busIdentifier = ArrayUtils.subarray(datastream, 1, 8);
        byte[] busIdentifier = Arrays.copyOfRange(datastream, 1, 8);
        //byte[] clientIdentifier = ArrayUtils.subarray(datastream, 9, 16);
        byte[] clientIdentifier = Arrays.copyOfRange(datastream, 9, 16);
        
        ArrayList<CanPacket> packets = new ArrayList<CanPacket>();
        
        boolean done = false;
        int start = 16;
        boolean isBridgeHeartbeat = false;
        while (!done) {
            //byte[] cpId = ArrayUtils.subarray(datastream, start, start + 4); // 17 - 20
        	byte[] cpId = Arrays.copyOfRange(datastream, start, start + 4); // 17 - 20
            
            //BitSet flags = BitSet.valueOf(ByteBuffer.wrap(ArrayUtils.subarray(datastream, start + 4, start + 5)));
        	BitSet flags = BitSet.valueOf(ByteBuffer.wrap(Arrays.copyOfRange(datastream, start + 4, start + 5)));
            
            isBridgeHeartbeat = flags.get(7); // heartbeat flag
            boolean isSettingsPacket = flags.get(6);
            boolean isRtr = flags.get(1);
            boolean isExtended = flags.get(0);
            
            byte length = datastream[start + 5];
            //byte[] data = ArrayUtils.subarray(datastream, start + 6, start + 6 + length);
            byte[] data = Arrays.copyOfRange(datastream, start + 6, start + 6 + length);

            CanPacket cp = new CanPacket(cpId, isExtended, isRtr, length, data);
            packets.add(cp);
            
            if (start + 6 + length >= total) {
                done = true;
            } else {
                start += 6 + length;
            }
        }
        
        //UdpPacket up = new UdpPacket((byte) total, ArrayUtils.subarray(b, 0, total), busIdentifier, clientIdentifier);
        UdpPacket up = new UdpPacket((byte) total, Arrays.copyOfRange(b, 0, total), busIdentifier, clientIdentifier);
        up.setCanPackets(packets);

        return up;
    }
}
