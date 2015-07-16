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
            alertString += "Alert- Motor Temp @ "+new DecimalFormat("#0.0").format(carData.getLastMotorTemp())+"c\n";
        }

        if (carData.getLastMaxCellTemp()/ 10 > 40) {
            alertString += "Alert- Battery Temp @ "+new DecimalFormat("#0.0").format(carData.getLastMaxCellTemp()/ 10)+"c\n";
        }

        if (carData.getLastControllerTemp() > 50) {
            alertString += "Alert- Controller Temp @ "+new DecimalFormat("#0.0").format(carData.getLastControllerTemp())+"c\n";
        }

        if (carData.getLastMinimumCellV() < 3) {
            alertString += "Alert- Min Cell Voltage @ "+new DecimalFormat("#0.0").format(carData.getLastMinimumCellV())+"V\n";
        }

        if (carData.getLastArray1Power() < 50) {
            alertString += "Alert- Low Array 1 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray1Power())+"w\n";
        }

        if (carData.getLastArray2Power() < 50) {
            alertString += "Alert- Low Array 2 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray2Power())+"w\n";
        }

        if (carData.getLastArray3Power() < 50) {
            alertString += "Alert- Low Array 3 Power @ "+new DecimalFormat("#0.0").format(carData.getLastArray3Power())+"w\n";
        }


        return alertString;
    }


}
