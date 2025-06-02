package com.blankj_custom.bus

open class BusExtension {
    var abortOnError = true;
    var busUtilsClass = "com.blankj.utilcode.util.BusUtils";
    var onlyScanLibRegex = ""
    var jumpScanLibRegex = ""

    override fun toString(): String {
        return "BusExtension { " +
                "abortOnError: " + abortOnError +
                ", busUtilsClass: " + busUtilsClass +
                (if (onlyScanLibRegex == "") "" else ", onlyScanLibRegex: " + onlyScanLibRegex) +
                (if (jumpScanLibRegex == "") "" else ", jumpScanLibRegex: " + jumpScanLibRegex) +
                " }"
    }
}