import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SmartDevice(val name: String, val category: String) {

    var deviceStatus = "online"
        protected set

    open val deviceType = "unknown"

    open fun turnOn() {
        deviceStatus = "on"
    }

    open fun turnOff() {
        deviceStatus = "off"
    }
    
    open fun printDeviceInfo() {
        print("Device name: $name, category: $category")
    }
}

class SmartTvDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart TV"

    private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)

    private var channelNumber by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 200)

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased to $speakerVolume.")
    }
    
    fun decreaseSpeakerVolume() {
        speakerVolume--
        println("Speaker volume decreased to $speakerVolume")
    }

    fun nextChannel() {
        channelNumber++
        println("Channel number increased to $channelNumber.")
    }
    
    fun previousChannel() {
        channelNumber--
        println("Channel number decreased to $channelNumber.")
    }

    override fun turnOn() {
        super.turnOn()
        println(
            "$name is turned on. Speaker volume is set to $speakerVolume and channel number is " +
                "set to $channelNumber."
        )
    }

    override fun turnOff() {
        super.turnOff()
        println("$name turned off")
    }
    
    override fun printDeviceInfo() {
        super.printDeviceInfo();
        print(" type: $deviceType")
    }
}

class SmartLightDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart Light"

    private var brightnessLevel by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 100)

    fun increaseBrightness() {
        brightnessLevel++
        println("Brightness increased to $brightnessLevel.")
    }
	
    fun decreaseBrightness() {
        brightnessLevel--
        println("Brightness decreased to $brightnessLevel.")
    }
    
    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }

    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("Smart Light turned off")
    }
    
    override fun printDeviceInfo() {
        super.printDeviceInfo();
        print(" type: $deviceType")
    }
}

class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice
) {

    var deviceTurnOnCount = 0
        private set

    private fun checkDeviceStatus(device: SmartDevice): Boolean {
        return device.deviceStatus == "on"
    }

    private fun incrementDeviceTurnOnCount() {
        deviceTurnOnCount++
    }

    private fun decrementDeviceTurnOnCount() {
        deviceTurnOnCount--
    }

    fun turnOnTv() {
        if (checkDeviceStatus(smartTvDevice)) {
            incrementDeviceTurnOnCount()
            smartTvDevice.turnOn()
        } else {
            println("Cannot turn on TV. Device is not on.")
        }
    }

    fun turnOffTv() {
        if (checkDeviceStatus(smartTvDevice)) {
            decrementDeviceTurnOnCount()
            smartTvDevice.turnOff()
        } else {
            println("Cannot turn off TV. Device is not on.")
        }
    }

    fun increaseTvVolume() {
        if (checkDeviceStatus(smartTvDevice)) {
            smartTvDevice.increaseSpeakerVolume()
        } else {
            println("Cannot increase TV volume. Device is not on.")
        }
    }

    fun decreaseTvVolume() {
        if (checkDeviceStatus(smartTvDevice)) {
            smartTvDevice.decreaseSpeakerVolume()
        } else {
            println("Cannot decrease TV volume. Device is not on.")
        }
    }

    fun changeTvChannelToNext() {
        if (checkDeviceStatus(smartTvDevice)) {
            smartTvDevice.nextChannel()
        } else {
            println("Cannot change TV channel. Device is not on.")
        }
    }

    fun changeTvChannelToPrevious() {
        if (checkDeviceStatus(smartTvDevice)) {
            smartTvDevice.previousChannel()
        } else {
            println("Cannot change TV channel. Device is not on.")
        }
    }

    fun turnOnLight() {
        if (checkDeviceStatus(smartLightDevice)) {
            incrementDeviceTurnOnCount()
            smartLightDevice.turnOn()
        } else {
            println("Cannot turn on light. Device is not on.")
        }
    }

    fun turnOffLight() {
        if (checkDeviceStatus(smartLightDevice)) {
            decrementDeviceTurnOnCount()
            smartLightDevice.turnOff()
        } else {
            println("Cannot turn off light. Device is not on.")
        }
    }

    fun increaseLightBrightness() {
        if (checkDeviceStatus(smartLightDevice)) {
            smartLightDevice.increaseBrightness()
        } else {
            println("Cannot increase light brightness. Device is not on.")
        }
    }

    fun decreaseLightBrightness() {
        if (checkDeviceStatus(smartLightDevice)) {
            smartLightDevice.decreaseBrightness()
        } else {
            println("Cannot decrease light brightness. Device is not on.")
        }
    }

    fun turnOffAllDevices() {
        turnOffTv()
        turnOffLight()
    }
    
    fun printSmartTvInfo() {
        if (checkDeviceStatus(smartTvDevice)) {
            smartTvDevice.printDeviceInfo()
        } else {
            println("Cannot print TV info. Device is not on.")
        }
    }

    fun printSmartLightInfo() {
        if (checkDeviceStatus(smartLightDevice)) {
            smartLightDevice.printDeviceInfo()
        } else {
            println("Cannot print light info. Device is not on.")
        }
    }
}


class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
) : ReadWriteProperty<Any?, Int> {

    var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

fun main() {
    val smartTv = SmartTvDevice("Android TV", "Entertainment")
    val smartLight = SmartLightDevice("Google Light", "Utility")
    val smartHome = SmartHome(smartTv, smartLight)
    
    smartHome.decreaseLightBrightness()
    smartHome.printSmartTvInfo()
}
