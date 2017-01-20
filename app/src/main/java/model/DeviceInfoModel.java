package model;

import java.io.Serializable;

/**
 * Created by Swati on 1/20/2017.
 */

public class DeviceInfoModel implements Serializable {
    String deviceID, deviceManufacturerName, deviceModel, osVersion, apiLevel;

    public DeviceInfoModel() {

    }

    public DeviceInfoModel(String deviceID, String deviceManufacturerName, String deviceModel, String osVersion, String apiLevel) {

    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceManufacturerName() {
        return deviceManufacturerName;
    }

    public void setDeviceManufacturerName(String deviceManufacturerName) {
        this.deviceManufacturerName = deviceManufacturerName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(String apiLevel) {
        this.apiLevel = apiLevel;
    }

    public String generateCSV() {
        String infoCSVFormat = "deviceID" + deviceID + "," + "deviceManufacturerName" + deviceManufacturerName + "," + "deviceModel" + deviceModel + "," + "osVersion" + osVersion + ",apiLevel" + apiLevel;
        return infoCSVFormat;
    }
}
