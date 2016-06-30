package au.com.teamarrow.arrowpoint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import au.com.teamarrow.canbus.model.ArrowMessage;
import au.com.teamarrow.canbus.model.CarData;

/**
 * Created by CTuesley on 9/07/2015.
 */
public class ArrowPointRulesEngine {
    private int MAX_COUNT = 100;
    private double LOW_ARRAY_PERCENT = 0.10;
    private int lowArray1Count = 0;
    private int lowArray2Count = 0;
    private int lowArray3Count = 0;


    private List<String> checkRules(CarData carData, Boolean simulateMode) {
        List<String> alertList = new ArrayList<String>();

       // Add warning rules in order of priority
            // 1st priority

        if (carData.getSecSinceLastPacket() >= 1){
            int time = (carData.getSecSinceLastPacket());
            alertList.add("No Data - "+time+" sec ");
        }

        if(carData.getDriverMessage() != null && carData.getDriverMessage().getSecondsSince() < 30){;
            alertList.add(carData.getDriverMessage().getMessageContent());
        }


        if (simulateMode){
            alertList.add("Simulated Data");
        }

        if (carData.getLastBatteryVolts() < 100 && carData.getLastBatteryVolts() != -1) {
            alertList.add("Check Battery!");
        }

        if (carData.getLastTwelveVBusVolts() < 12 && carData.getLastTwelveVBusVolts() != -1) {
            alertList.add("ESTOP NOW! 12V System @ "+new DecimalFormat("#0.00").format(carData.getLastTwelveVBusVolts())+"V");
        }

        if (carData.getLastMinimumCellV() < carData.getMinThreshMinimumCellV() && carData.getLastMinimumCellV() != -1) {
            alertList.add("Min Cell Voltage @ "+new DecimalFormat("#0.00").format(carData.getLastMinimumCellV())+"V");
        }

        if (carData.getLastMotorTemp() > carData.getMaxThreshMotorTemp()) {
            alertList.add("Motor Temp @ "+new DecimalFormat("#0.0").format(carData.getLastMotorTemp())+"\u2103");
        }

        if ((carData.getLastMaxCellTemp()/ 10) > carData.getMaxThreshMaxCellTemp()) {
            alertList.add("Battery Temp @ "+new DecimalFormat("#0.0").format(carData.getLastMaxCellTemp()/ 10)+"\u2103");
        }

        if (carData.getLastControllerTemp() > 50) {
            alertList.add("Controller Temp @ "+new DecimalFormat("#0.0").format(carData.getLastControllerTemp())+"\u2103");
        }

        if (carData.getLastArray1Power()/carData.getLastArrayTotalPower() > LOW_ARRAY_PERCENT) {
            lowArray1Count = 0;
        } else if (carData.getLastArray1Power()/carData.getLastArrayTotalPower() < LOW_ARRAY_PERCENT && lowArray1Count > MAX_COUNT) {
            alertList.add("Low Array 1 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray1Power())+"W");
        }else if (carData.getLastArray1Power()/carData.getLastArrayTotalPower() < LOW_ARRAY_PERCENT) {
            lowArray1Count++;
        }

        if (carData.getLastArray2Power()/carData.getLastArrayTotalPower() > LOW_ARRAY_PERCENT) {
            lowArray2Count = 0;
        } else if (carData.getLastArray2Power()/carData.getLastArrayTotalPower() < LOW_ARRAY_PERCENT && lowArray2Count > MAX_COUNT) {
            alertList.add("Low Array 2 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray2Power())+"W");
        }else if (carData.getLastArray2Power()/carData.getLastArrayTotalPower() < LOW_ARRAY_PERCENT) {
            lowArray2Count++;
        }

        if (carData.getLastArray3Power()/carData.getLastArrayTotalPower() > LOW_ARRAY_PERCENT) {
            lowArray3Count = 0;
        } else if (carData.getLastArray3Power()/carData.getLastArrayTotalPower() < LOW_ARRAY_PERCENT && lowArray3Count > MAX_COUNT) {
            alertList.add("Low Array 3 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray3Power())+"W");
        }else if (carData.getLastArray3Power()/carData.getLastArrayTotalPower() < LOW_ARRAY_PERCENT) {
            lowArray3Count++;
        }

        return alertList;
    }

    public String getAlerts(CarData carData, Boolean simulateMode){
        List<String> alertList = new ArrayList<String>();
        alertList = checkRules(carData, simulateMode);
        String string = "";
        if (alertList.size() > 0) {
            for (int i = 0; i < alertList.size(); i++) {
                string += alertList.get(i) + "\n";
            }
        }
        return string;
    }


}
