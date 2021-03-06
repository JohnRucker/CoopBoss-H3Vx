/**
 *
 *  Copyright 2017 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Virtual CoopBoss", namespace: "JohnRucker", author: "John.Rucker@Solar-Current.com") {
		capability "Actuator"
		//capability "Door Control"
        capability "Garage Door Control"
        capability "Switch"
		capability "Contact Sensor"
        capability "Lock"
		capability "Refresh"
		capability "Sensor"
        capability "Illuminance Measurement"
        capability "Temperature Measurement"
        capability "Light"
        
        command "finishOpening"
        command "finishClosing"
        command "setLux"
        command "setTemperature"
        command "setLight"
	}

	simulator {
		
	}

	tiles {
		standardTile("toggle", "device.door", width: 2, height: 2) {
			state("closed", label:'${name}', action:"open", icon:"st.doors.garage.garage-closed", backgroundColor:"#79b821", nextState:"opening")
			state("open", label:'${name}', action:"close", icon:"st.doors.garage.garage-open", backgroundColor:"#ffa81e", nextState:"closing")
			state("opening", label:'${name}', icon:"st.doors.garage.garage-closed", backgroundColor:"#ffe71e")
			state("closing", label:'${name}', icon:"st.doors.garage.garage-open", backgroundColor:"#ffe71e")
			
		}
		standardTile("open", "device.door", inactiveLabel: false, decoration: "flat") {
			state "default", label:'open', action:"open", icon:"st.doors.garage.garage-opening"
		}
		standardTile("close", "device.door", inactiveLabel: false, decoration: "flat") {
			state "default", label:'close', action:"close", icon:"st.doors.garage.garage-closing"
		}

		main "toggle"
		details(["toggle", "open", "close"])
	}
}

def parse(String description) {
	log.trace "parse($description)"
}

def open() {
	log.info "virtual coopboss firing opening event"
	sendEvent(name: "door", value: "opening")
    // runIn(6, finishOpening)
}

def close() {
	log.info "virtual coopboss firing close event"
    sendEvent(name: "door", value: "closing")
	// runIn(6, finishClosing)
}

def on(){
	log.info("turning switch on")
    sendEvent(name: "switch", value: "on")
}

def off(){
	log.info("turning switch off")
    sendEvent(name: "switch", value: "off")
}

def unlock(){
	log.info("unlock calling open")
	open()
}

def lock(){
	log.info("Lock calling close")
	close()
}

def setLux(luxValue){
	log.info("Setting lux value to ${luxValue} ")
	sendEvent(name: "illuminance", value: luxValue)
}

def setTemperature(temperatureValue){
	log.info("Setting temperature value to ${temperatureValue}")
	sendEvent(name: "temperature", value: temperatureValue)
}

def setLight(lightValue){
	log.info("Setting light value to ${lightValue} ")
	sendEvent(name: "switch", value: lightValue)
}

def finishOpening() {
	log.info("Door finished opening")
    sendEvent(name: "door", value: "open")
    sendEvent(name: "contact", value: "open")  
    sendEvent(name: "lock", value: "unlocked")    
}

def finishClosing() {
	log.info("Door finished closing")
    sendEvent(name: "door", value: "closed")
    sendEvent(name: "contact", value: "closed")  
    sendEvent(name: "lock", value: "locked")       
}