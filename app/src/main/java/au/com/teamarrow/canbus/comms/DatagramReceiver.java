package au.com.teamarrow.canbus.comms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Handler;
import au.com.teamarrow.canbus.model.CanPacket;
import au.com.teamarrow.canbus.model.CarData;
import au.com.teamarrow.canbus.model.UdpPacket;
import au.com.teamarrow.canbus.serial.CanPacketSplitter;
import au.com.teamarrow.canbus.serial.UdpPacketDeserializer;


public class DatagramReceiver extends Thread {

	private static final int UDP_SERVER_PORT = 4876;
	private static final int MAX_UDP_DATAGRAM_LEN = 512;

	private boolean bKeepRunning = true;
	private CarData carData;

	private boolean simulate;
	private int[] SIMULATE_IDS = { 770,771,773,1281,785,789,793, 776, 508 };


	public DatagramReceiver(CarData carData, boolean simulate) {
		this.simulate = simulate;
		this.carData = carData;
	}


	private double smartSimulateDouble(double valueIn, double currentValue, double lowRange, double highRange, double variation) {

		if ( simulate == false) return valueIn;

		double potentialSize = (highRange - lowRange) * variation;
		double randomNum = (float) (Math.random()*potentialSize) - (potentialSize / 2);

		double newValue = currentValue + randomNum;

		if (newValue <= lowRange ) newValue = lowRange;
		if (newValue >= highRange) newValue = highRange;

		return newValue;

	}


	public void run() {
		byte[] lmessage = new byte[MAX_UDP_DATAGRAM_LEN];
		InetAddress addr = null;
		UdpPacketDeserializer udpDeserializer = new UdpPacketDeserializer();
		CanPacketSplitter canPacketSplitter = new CanPacketSplitter();
		List<CanPacket> canPackets = null;

		boolean timeout;

		try {
			addr = InetAddress.getByName("239.255.60.60");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);


		try {

			//Need to use a multicast socket apparently
			MulticastSocket socket = new MulticastSocket(UDP_SERVER_PORT); // must bind receive side
			socket.joinGroup(addr);
			socket.setSoTimeout(100);

			while(bKeepRunning) {

				timeout = false;

				try {
					socket.receive(packet);
				} catch (SocketTimeoutException ex) {
                    carData.setMsSinceLastPacket(carData.getMsSinceLastPacket()+100); // Add 100ms to the LastPacket Timer
                    timeout = true; };

				try {

					if (timeout == false || simulate == true) {

                        carData.setMsSinceLastPacket(0);// reset LastPacket Timer

						// Deserialize the packet
						                            if ( simulate == false) {

                                InputStream inputStream = new ByteArrayInputStream(packet.getData(),0,packet.getLength());

                                UdpPacket udpPacket = udpDeserializer.deserialize(inputStream);
                                canPackets = canPacketSplitter.extractCanPackets(udpPacket);

						} else {

							// Dummy up at list of can packets to keep the logic flowing
							canPackets = new ArrayList<CanPacket>();

							int choice = SIMULATE_IDS[(int)(Math.random() * SIMULATE_IDS.length)];
							byte[] byteArray = new byte[8];
							new Random().nextBytes(byteArray);

							CanPacket canPacket = new CanPacket(ByteBuffer.allocate(4).putInt(choice).array(),false, false, (byte)8 , byteArray);
							canPackets.add(canPacket);
						}

						switch (canPackets.get(0).getIdBase10()) {

							// Bus Power, multiply volts by amps to get it (x302)
							case 770: 	carData.setLastBusPower(smartSimulateDouble((double) ((canPackets.get(0).getDataSegmentOneAsFloat() * canPackets.get(0).getDataSegmentTwoAsFloat()) / 1000), carData.getLastBusPower(), 0, 5, 0.1));
								carData.setLastBusAmps(smartSimulateDouble((double) ((canPackets.get(0).getDataSegmentTwoAsFloat())), carData.getLastBusPower(), 0, 5, 0.1));
								carData.setLastBusVolts(smartSimulateDouble((double)((canPackets.get(0).getDataSegmentOneAsFloat())),carData.getLastBusPower(),150,160,0.1));
								break;


							// Velocity in Meters Per Second (x303)
							case 771: 	carData.setLastSpeed((int) smartSimulateDouble((double) (canPackets.get(0).getDataSegmentTwoAsFloat() * (float) 3.6), carData.getLastSpeed(), 0, 100, 0.1));
								break;

							// Battery State of Charge (x305)
							case 773: 	carData.setLastSOC((int) smartSimulateDouble((double) (canPackets.get(0).getDataSegmentTwoAsFloat() * (float) 100), carData.getLastSOC(), 0, 100, 0.1));
								break;

							// Last Motor Power Setpoint (x501)
							case 1281: 	carData.setLastMotorPowerSetpoint((int) smartSimulateDouble((double) (canPackets.get(0).getDataSegmentTwoAsFloat() * (float) 100), carData.getLastMotorPowerSetpoint(), 0, 100, 0.1));
								break;

							// Array1 (x311)
							case 785: 	carData.setLastArray1Volts(smartSimulateDouble((double)(canPackets.get(0).getDataSegmentOneAsInt() / (double)1000),carData.getLastArray1Volts(),80,90,0.1));
								carData.setLastArray1Amps(smartSimulateDouble((double) (canPackets.get(0).getDataSegmentTwoAsInt() / (double) 1000),carData.getLastArray1Amps(), 0, 5, 0.1));
								break;

							// Array1 (x315)
							case 789: 	carData.setLastArray2Volts(smartSimulateDouble((double) (canPackets.get(0).getDataSegmentOneAsInt() / (double) 1000),carData.getLastArray2Volts(), 80, 90, 0.1));
								carData.setLastArray2Amps(smartSimulateDouble((double) (canPackets.get(0).getDataSegmentTwoAsInt() / (double) 1000),carData.getLastArray2Amps(),0,5,0.1));
								break;

							// Array1 (x319)
							case 793: 	carData.setLastArray3Volts(smartSimulateDouble((double) (canPackets.get(0).getDataSegmentOneAsInt() / (double) 1000),carData.getLastArray3Volts(), 80,90,0.1));
								carData.setLastArray3Amps(smartSimulateDouble((double) (canPackets.get(0).getDataSegmentTwoAsInt() / (double) 1000),carData.getLastArray3Amps(),0,5,0.1));
								break;

							// Battery (6FA)
							case 1786: 	carData.setLastBatteryVolts(smartSimulateDouble((double)(canPackets.get(0).getDataSegmentOneAsInt() / (double)1000),carData.getLastBatteryVolts(),80,90,0.1));
								carData.setLastBatteryAmps(smartSimulateDouble((double) (canPackets.get(0).getDataSegmentTwoAsInt() / (double) 1000), carData.getLastBatteryAmps(), 0,5,0.1));
								break;

							// Driver controls state (x308)
							case 776:   carData.setRegen(canPackets.get(0).getBit(2,0) !=0);
								carData.setBrakes(canPackets.get(0).getBit(0,4) !=0);
								carData.setHorn(canPackets.get(0).getBit(2,2) != 0);
								carData.setLeftBlinker(canPackets.get(0).getBit(3,2) != 0);
								carData.setRightBlinker(canPackets.get(0).getBit(3,1) != 0);

								// todo: Should this be state of charge or throttle position?
								if (canPackets.get(0).getBit(2,7) != 0) {
									carData.setLastLockedSOC(carData.getLastSOC());
								}

								carData.setIdle(false);
								carData.setNeutral(false);
								carData.setDrive(false);
								carData.setReverse(false);

								switch ( canPackets.get(0).getDataSegmentAsInt(7) ) {
									case 1: carData.setIdle(true);  break;
									case 3: carData.setReverse(true); break;
									case 4: carData.setNeutral(true); break;
									case 6: carData.setDrive(true); break;
								}

								carData.setLastTwelveVBusVolts(canPackets.get(0).getTwoDataSegmentsAsInt(4));
								break;


							case 1035: carData.setLastMotorTemp(canPackets.get(0).getDataSegmentOneAsFloat());
								carData.setLastControllerTemp(canPackets.get(0).getDataSegmentTwoAsFloat());
								break;


							case 1784: carData.setLastMinimumCellV((int)canPackets.get(0).getTwoDataSegmentsAsInt(0));
								carData.setLastMaximumCellV((int)canPackets.get(0).getTwoDataSegmentsAsInt(2));
								break;

							case 1785: carData.setLastMaxCellTemp((int)canPackets.get(0).getTwoDataSegmentsAsInt(2));
								break;

                            // Cruise control state (x508)
                            case 1288:

                                switch ( canPackets.get(0).getDataSegmentAsInt(7) ) {
                                    case 0: carData.setSpeedCruiseControl(false);
                                        carData.setSetPointCruiseControl(false);
                                        break;
                                    case 1: carData.setSpeedCruiseControl(true);
                                        carData.setSetPointCruiseControl(false);
                                        break;
                                    case 2: carData.setSetPointCruiseControl(true);
                                        carData.setSpeedCruiseControl(false);
                                        break;
                                }

                                break;
						}

					} else {
						timeout = true;
					}

				} catch (Exception e) {
					// We don't want to stop on exceptions, keep processing for all events
					e.printStackTrace();
				}
			}

			if (socket != null) {
				socket.close();
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public void kill() {
		bKeepRunning = false;
	}



}
