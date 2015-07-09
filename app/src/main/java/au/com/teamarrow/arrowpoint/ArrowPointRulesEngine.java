package au.com.teamarrow.arrowpoint;

import au.com.teamarrow.canbus.model.CarData;

/**
 * Created by CTuesley on 9/07/2015.
 */
public class ArrowPointRulesEngine {

    public String checkRules(CarData carData) {

        String alertString = new String();

        if (carData.getLastMotorTemp() > 80) {
            alertString = "Max Motor Temp Alert, at 80";
        }

        if (alertString.isEmpty()) return null; else return alertString;

    };


}
