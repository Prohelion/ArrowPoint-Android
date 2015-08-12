package au.com.teamarrow.arrowpoint;

import java.text.DecimalFormat;

import au.com.teamarrow.canbus.model.CarData;

/**
 * Created by CTuesley on 9/07/2015.
 */
public class ArrowPointRulesEngine {

    public String checkRules(CarData carData, Boolean simulateMode) {
        String alertString = "";

       // Add warning rules in order of priority
            // 1st priority
        if (simulateMode){
            alertString += "Simulated Data\n";
        }

        if (carData.getMsSinceLastPacket() > 1000){
            alertString += "No Data\n";
        }

        if (carData.getLastMotorTemp() > 80) {
            alertString += "Motor Temp @ "+new DecimalFormat("#0.0").format(carData.getLastMotorTemp())+"\u2103\n";
        }

        if (carData.getLastMaxCellTemp()/ 10 > 40) {
            alertString += "Battery Temp @ "+new DecimalFormat("#0.0").format(carData.getLastMaxCellTemp()/ 10)+"\u2103\n";
        }

        if (carData.getLastControllerTemp() > 50) {
            alertString += "Controller Temp @ "+new DecimalFormat("#0.0").format(carData.getLastControllerTemp())+"\u2103\n";
        }

        if (carData.getLastMinimumCellV() < 3) {
            alertString += "Min Cell Voltage @ "+new DecimalFormat("#0.0").format(carData.getLastMinimumCellV())+"V\n";
        }

        if (carData.getLastArray1Power()/carData.getLastArrayTotalPower() < 0.15) {
            alertString += "Low Array 1 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray1Power())+"W\n";
        }

        if (carData.getLastArray2Power()/carData.getLastArrayTotalPower() < 0.15) {
            alertString += "Low Array 2 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray2Power())+"W\n";
        }

        if (carData.getLastArray3Power()/carData.getLastArrayTotalPower() < 0.15) {
            alertString += "Low Array 3 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray3Power())+"W\n";
        }


        return alertString;
    }


}
