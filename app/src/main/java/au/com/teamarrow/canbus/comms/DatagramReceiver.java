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
import android.os.Handler;
import au.com.teamarrow.canbus.model.CanPacket;
import au.com.teamarrow.canbus.model.UdpPacket;
import au.com.teamarrow.canbus.serial.CanPacketSplitter;
import au.com.teamarrow.canbus.serial.UdpPacketDeserializer;


public class DatagramReceiver extends Thread {
    
	private static final int UDP_SERVER_PORT = 4876;
	private static final int MAX_UDP_DATAGRAM_LEN = 512;
		
	private boolean bKeepRunning = true;
    private int lastSpeed = (int) 0;
    private float lastSOC = (float) 100;
    private float lastLockedSOC = (float) 0;
    private double lastArray1Volts = (double) 0;
    private double lastArray2Volts = (double) 0;
    private double lastArray3Volts = (double) 0;
    private double lastArray1Amps = (double) 0;
    private double lastArray2Amps = (double) 0;
    private double lastArray3Amps = (double) 0;
    private double lastBusPower = (double) 0.0;        
    private double lastBusAmps = (double) 0.0;
    private double lastBusVolts = (double) 0.0;
    private double lastBatteryAmps = (double) 0.0;
    private double lastBatteryVolts = (double) 0.0;
    private double lastMotorTemp = (double) 0.0;
    private double lastControllerTemp = (double) 0.0;
    private int lastMinimumCellV = (int) 0;
    private int lastMaximumCellV = (int) 0;
    private int lastTwelveVBusVolts = (int) 0;
    private int lastMaxCellTemp = (int) 0;
    private int lastMotorPowerSetpoint = 0;
    private boolean cruiseControl = false;
    private int cruiseTargetSpeed = 0;
    private boolean leftBlinker = false;
    private boolean rightBlinker = false;
    private boolean hazard = false;  
    private Runnable updateValuesRunnable = null;
    private Handler handler;
    
    private boolean idle = false;
    private boolean reverse = false;
    private boolean neutral = false;
    private boolean drive = false;
	private boolean run = false;
    private boolean regen = false;
    private boolean brakes = false;     
    private boolean horn = false;
    
    private boolean simulate;
    private int[] SIMULATE_IDS = { 770,771,773,1281 };
    

    public DatagramReceiver(Runnable updateValuesRunnable, Handler handler, boolean simulate) {
    	this.updateValuesRunnable = updateValuesRunnable;
    	this.handler = handler;
    	this.simulate = simulate;
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
            	} catch (SocketTimeoutException ex) { timeout = true; };
                
            	try {
            	
	                if (timeout == false || simulate == true) {
	                                    
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
	                	
	                		CanPacket canPacket = new CanPacket(ByteBuffer.allocate(4).putInt(choice).array(),false, false, (byte)8 , byteArray);                		                		
	                		canPackets.add(canPacket);       
	                		                		
	                		
	                	}
	                    
	                    switch (canPackets.get(0).getIdBase10()) {
	
	                    	// Bus Power, multiply volts by amps to get it (x302)
	                		case 770: 	lastBusPower = smartSimulateDouble((double)((canPackets.get(0).getDataSegmentOneAsFloat() * canPackets.get(0).getDataSegmentTwoAsFloat()) / 1000),lastBusPower,0,5,0.1);
	                					lastBusAmps = smartSimulateDouble((double)((canPackets.get(0).getDataSegmentTwoAsFloat())),lastBusPower,0,5,0.1);
	                					lastBusVolts = smartSimulateDouble((double)((canPackets.get(0).getDataSegmentOneAsFloat())),lastBusPower,150,160,0.1);
	                					// We also us the 302 as a signal to do a refresh
	                    				//handler.post(updateValuesRunnable);
	                    				break;
	                    
	                    
	                    	// Velocity in Meters Per Second (x303)
	                    	case 771: 	lastSpeed = (int) smartSimulateDouble((double)(canPackets.get(0).getDataSegmentTwoAsFloat() * (float)3.6),(double)lastSpeed,0,100,0.1);
	                    				//handler.post(updateValuesRunnable);
		                    			break;
	
		                    // Battery State of Charge (x305) 
	                    	case 773: 	lastSOC = (int) smartSimulateDouble((double)(canPackets.get(0).getDataSegmentTwoAsFloat() * (float)100),(double)lastSOC,0,100,0.1);
	                    				//handler.post(updateValuesRunnable);
	            						break;
	            							            				
	            			// Last Motor Power Setpoint (x501) 
	        	            case 1281: 	lastMotorPowerSetpoint = (int) smartSimulateDouble((double)(canPackets.get(0).getDataSegmentTwoAsFloat() * (float)100),(double)lastMotorPowerSetpoint,0,100,0.1);
	        	            			handler.post(updateValuesRunnable);
	        	            			break;	            						
	        	            	            						
	        		        // Array1 (x311) 
	        	            case 785: 	lastArray1Volts = smartSimulateDouble((double)(canPackets.get(0).getDataSegmentOneAsInt() / (double)1000),(double)lastArray1Volts,80,90,0.1);
	        	            			lastArray1Amps = smartSimulateDouble((double)(canPackets.get(0).getDataSegmentTwoAsInt() / (double)1000),(double)lastArray1Amps,0,5,0.1);
	        	            			//handler.post(updateValuesRunnable);
	        	            			break;	            						
	
	        	            // Array1 (x315) 
	        	            case 789: 	lastArray2Volts = smartSimulateDouble((double)(canPackets.get(0).getDataSegmentOneAsInt() / (double)1000),(double)lastArray2Volts,80,90,0.1);
	            						lastArray2Amps = smartSimulateDouble((double)(canPackets.get(0).getDataSegmentTwoAsInt() / (double)1000),(double)lastArray2Amps,0,5,0.1);
	        	            			//handler.post(updateValuesRunnable);
	        	            			break;	            						
	
	        	            			
	        	            // Array1 (x319) 
	        	            case 793: 	lastArray3Volts = smartSimulateDouble((double)(canPackets.get(0).getDataSegmentOneAsInt() / (double)1000),(double)lastArray3Volts,80,90,0.1);
	            						lastArray3Amps = smartSimulateDouble((double)(canPackets.get(0).getDataSegmentTwoAsInt() / (double)1000),(double)lastArray3Amps,0,5,0.1);
	        	            			//handler.post(updateValuesRunnable);
	        	            			break;	            						
	        	            			

	        	            // Battery (6FA) 
	        	        	case 1786: 	lastBatteryVolts = smartSimulateDouble((double)(canPackets.get(0).getDataSegmentOneAsInt() / (double)1000),(double)lastArray3Volts,80,90,0.1);
	        	            			lastBatteryAmps = smartSimulateDouble((double)(canPackets.get(0).getDataSegmentTwoAsInt() / (double)1000),(double)lastArray3Amps,0,5,0.1);
	        	        	            break;	            						
	        	            			
	        	        	// Driver controls state (x308) 			
	        	        	case 776:   regen = canPackets.get(0).getBit(2,0) !=0;  	        	            			
	        	            			brakes = canPackets.get(0).getBit(0,4) !=0;
	        	            			horn = canPackets.get(0).getBit(2,2) != 0;
	        	            			leftBlinker = canPackets.get(0).getBit(3,1) != 0;
	        	            			rightBlinker = canPackets.get(0).getBit(3,2) != 0;
	        	            			
	        	            			if (canPackets.get(0).getBit(2,7) != 0) {
	        	            				lastLockedSOC = lastSOC;
	        	            			}
	        	            			
	        	            			idle = false;
	        	            			neutral = false;
	        	            			drive = false;
	        	            			reverse = false;
	        	            			
	        	            			switch ( canPackets.get(0).getDataSegmentAsInt(7) ) {
	        	            				case 1: idle = true;  break;
	        	            				case 3: reverse = true; break;
	        	            				case 4: neutral = true; break;
	        	            				case 6: drive = true; break;
	        	            			}
	        	            						        	            				        	            			
	        	            			lastTwelveVBusVolts = canPackets.get(0).getTwoDataSegmentsAsInt(4);
	        	            			break;
	        	            		

	        	        	case 1035: lastMotorTemp = canPackets.get(0).getDataSegmentOneAsFloat();
	        	        			   lastControllerTemp = canPackets.get(0).getDataSegmentTwoAsFloat();
	        	        			   break;
	        	        			   
	        	        	
	        	        	case 1784: lastMinimumCellV = (int)canPackets.get(0).getTwoDataSegmentsAsInt(0);
	        	        	 	       lastMaximumCellV = (int)canPackets.get(0).getTwoDataSegmentsAsInt(2);
	        	        	 	       break;

	        	        	case 1785: lastMaxCellTemp = (int)canPackets.get(0).getTwoDataSegmentsAsInt(2);
 	        	 	       			   break;	        	        	 	      
	        	            			
	        	            			
	        	            			
	            		/*	// Cruise Control Status
	                    	case 1288: if (canPackets.get(0).getDataSegmentOne() == 0) cruiseControl = false; else cruiseControl = true;
									   runOnUiThread(updateValues);
									   break;
									   
							// Cruise Control Speed
	                    	case 1287: cruiseTargetSpeed = formatterNoDecimal.format(canPackets.get(0).getDataSegmentTwo() / (float)KPH_TO_RPM);
	            					   runOnUiThread(updateValues);
	            					   break;
	            					
		                  */  			
	                    
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

    public int getLastSpeed() {
        return lastSpeed;
    }

    public int getCruiseTargetSpeed() {
        return cruiseTargetSpeed;
    }
    
    public double getLastBusPower() {
        return lastBusPower;
    }
    
    public double getLastBusAmps() {
        return lastBusAmps;
    }

    public double getLastBusVolts() {
        return lastBusVolts;
    }
    
    public float getLastSOC() {
        return lastSOC;
    }

    public int getLastMotorPowerSetpoint() {
        return lastMotorPowerSetpoint;
    }

    
    public double getLastArray1Volts() {
        return lastArray1Volts;
    }
    
    public double getLastArray2Volts() {
        return lastArray2Volts;
    }

    public double getLastArray3Volts() {
        return lastArray3Volts;
    }

    public double getLastArray1Amps() {
        return lastArray1Amps;
    }
    
    public double getLastArray2Amps() {
        return lastArray2Amps;
    }

    public double getLastArray3Amps() {
        return lastArray3Amps;
    }
    

    public double getLastArray1Power() {
        return lastArray1Volts * lastArray1Amps;
    }
    
    public double getLastArray2Power() {
        return lastArray2Volts * lastArray2Amps;
    }

    public double getLastArray3Power() {
        return lastArray3Volts * lastArray3Amps;
    }

    public double getLastArrayTotalPower() {
        return getLastArray1Power() + getLastArray3Power() + getLastArray3Power();
    }
    

    
    public double getLastBatteryVolts() {
        return lastBatteryVolts;
    }

    public double getLastBatteryAmps() {
        return lastBatteryAmps;
    }

    public double getLastBatteryPower() {
        return lastBatteryAmps * lastBatteryVolts;
    }

    
    public boolean isCruiseControl() {
    	return cruiseControl;
    }
    
	public void setLastSpeed(int lastSpeed) {
		this.lastSpeed = lastSpeed;
	}

	public void setLastSOC(int lastSOC) {
		this.lastSOC = lastSOC;
	}

	public void setLastBusPower(float lastBusPower) {
		this.lastBusPower = lastBusPower;
	}

	public void setCruiseControl(boolean cruiseControl) {
		this.cruiseControl = cruiseControl;
	}

	public void setCruiseTargetSpeed(int cruiseTargetSpeed) {
		this.cruiseTargetSpeed = cruiseTargetSpeed;
	}
    

	public void setRightBlinker(boolean rightBlinker) {
		this.rightBlinker = rightBlinker;
	}

	
	public void setLeftBlinker(boolean leftBlinker) {
		this.leftBlinker = leftBlinker;
	}


	public boolean isRightBlinker() {
		return(rightBlinker);
	}

	
	public boolean isLeftBlinker() {
		return(leftBlinker);
	}		

	 public boolean isIdle() {
			return idle;
		}


		public boolean isReverse() {
			return reverse;
		}


		public boolean isNeutral() {
			return neutral;
		}


		public boolean isDrive() {
			return drive;
		}


		public boolean isRun() {
			return run;
		}


		public boolean isRegen() {
			return regen;
		}


		public boolean isBrakes() {
			return brakes;
		}	
		
	public boolean isHorn() {
		return horn;
	}
	
	
	
	public double getLastMotorTemp(){
		return lastMotorTemp;
	}

	public double getLastControllerTemp(){
		return lastControllerTemp;
	}
	
	public int getLastMinimumCellV() {
		return lastMinimumCellV;		
	}

	public int getLastMaximumCellV() {
		return lastMaximumCellV;		
	}

	public double getLastTwelveVBusVolts() {
		return lastTwelveVBusVolts;
	}
	
	public int getLastMaxCellTemp() {
		return lastMaxCellTemp;
	}

	public float getLastLockedSOC() {
		return lastLockedSOC;
	}
	
}
