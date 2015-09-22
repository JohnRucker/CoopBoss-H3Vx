// Automatically generated. Make future change here.
definition(
    name: "Admin (Set Base Current)",
    namespace: "JohnRucker",
    author: "John.Rucker@Solar-current.com",
    description: "Sets new base current for door's object detection circuit.",
    category: "My Apps",
    iconUrl: "http://coopboss.com/images/SmartThingsIcons/coopbossLogo.png",
    iconX2Url: "http://coopboss.com/images/SmartThingsIcons/coopbossLogo2x.png",
    iconX3Url: "http://coopboss.com/images/SmartThingsIcons/coopbossLogo3x.png")

preferences {
    page(name: "page1", title: "Select CoopBoss", nextPage: "page2", uninstall: true) {
        section {
        	paragraph "CAUTION This SmartApp allows you to set a new base current for your CoopBoss. The base current along with the sensitivity setting are used to detect objects by the door control circuit."
            paragraph "Setting this value incorrectly may cause the door to not close properly or close with too much pressure.  Please consult the CoopBoss online manual for guidelines on setting this value."  
			input(name: "doorSensor", type: "capability.doorControl", title: "Select CoopBoss to change", required: true, multiple: false)
        }
    }

    page(name: "page2", title: "Enter new base door current", install: true, uninstall: true)

}

def page2() {
    dynamicPage(name: "page2") {
        section {
        	def crntBaseCurrent = doorSensor.currentState("baseDoorCurrent")
            def lastClsCurrent = doorSensor.currentState("doorCurrent")
            def millamps = (int)((crntBaseCurrent.value as float) * 1000)
            def lastCloseI = (int)((lastClsCurrent.value as float) * 1000)
            paragraph "The current setting for the base door current is ${millamps}mA, the last time the door closed it required ${lastCloseI}mA."
            paragraph "Enter a new value in milliamps and the value must be greater than 500ma and less than 3,000ma.  If this value is set too high the door may close with too much pressure smashing objects.  If set too low the door will show a Jammed state.  The factory default for this setting is 1,000mA"
        	input(name: "newBaseCurrent", type: "number", title: "Enter new base current and push done, the current value = ${millamps}")
         	}
    	}
	}   


def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	//subscribe(doorSensor, "doorState", coopDoorStateHandler)
    setBaseCurrent()
}

def setBaseCurrent(){
	log.debug "showBaseCurrent called newBaseCurrent = ${newBaseCurrent} "
    doorSensor.setNewBaseCurrent(newBaseCurrent)
}