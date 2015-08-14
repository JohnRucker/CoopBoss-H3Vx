/**
 *  Door Status Notification
 *
 *  Author: SmartThings
 */
definition(
    name: "Door Jammed Notification",
    namespace: "JohnRucker",
    author: "John.Rucker@Solar-current.com",
    description: "Get a push notification and text messages when your CoopBoss detects a door jam.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
    section("When the door state changes") {
        paragraph "Send a SmartThings notification only when the coop's door jammed and did not close."
		input "doorSensor", "capability.doorControl", title: "Select CoopBoss", required: true, multiple: false            
        input("recipients", "contact", title: "Recipients", description: "Send notifications to") {
        	input "phone", "phone", title: "Phone number?", required: true}
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
	subscribe(doorSensor, "doorState", coopDoorStateHandler)
}

def coopDoorStateHandler(evt) {
	if (evt.value == "jammed"){
        def msg = "WARNING ${doorSensor.displayName} door is jammed and did not close!"
        log.debug "WARNING ${doorSensor.displayName} door is jammed and did not close, texting $phone"

        if (location.contactBookEnabled) {
            sendNotificationToContacts(msg, recipients)
        }
        else {
            sendPush(msg)
            if (phone) {
                sendSms(phone, msg)
            }
        }
	}        
}