package com.blankj_custom.bus;

class BusExtension {

    boolean abortOnError = true;
    String busUtilsClass = "com.mr235.testbuildsrc.BusUtilsExt";
    String onlyScanLibRegex = ""
    String jumpScanLibRegex = ""

    @Override
    String toString() {
        return "BusExtension { " +
                "abortOnError: " + abortOnError +
                ", busUtilsClass: " + busUtilsClass +
                (onlyScanLibRegex == "" ? "" : ", onlyScanLibRegex: " + onlyScanLibRegex) +
                (jumpScanLibRegex == "" ? "" : ", jumpScanLibRegex: " + jumpScanLibRegex) +
                " }";
    }
}
