/**
 *  MyQ Garage Door Opener
 *
 *  Copyright 2016 Eric Mey
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
  definition (name: "MyQ Garage Door Opener", namespace: "ericmey", author: "Eric Mey") {
    capability "Actuator"
    capability "Contact Sensor"
    capability "Door Control"
    capability "Garage Door Control"
    capability "Momentary"
    capability "Polling"
    capability "Refresh"
    capability "Sensor"
    capability "Switch"

    attribute "lastActivity", "string"

    command "deviceUpdateStatus"
    command "deviceUpdateLastActivity"
  }

  simulator {
  }

  tiles {
    standardTile("door", "device.door", width: 2, height: 2) {
      state("unknown", label: '${name}', action: "door control.close", icon: "st.doors.garage.garage-open", backgroundColor: "#ffa81e", nextState: "closing")
      state("closed", label: '${name}', action: "door control.open", icon: "st.doors.garage.garage-closed", backgroundColor: "#79b821", nextState: "opening")
      state("open", label: '${name}', action: "door control.close", icon: "st.doors.garage.garage-open", backgroundColor: "#ffa81e", nextState: "closing")
      state("opening", label: '${name}', icon: "st.doors.garage.garage-opening", backgroundColor: "#ffe71e", nextState: "open")
      state("closing", label: '${name}', icon: "st.doors.garage.garage-closing", backgroundColor: "#ffe71e", nextState: "closed")
      state("stopped", label: 'stopped', action: "door control.close", icon: "st.doors.garage.garage-opening", backgroundColor: "#1ee3ff", nextState: "closing")
    }

    standardTile("refresh", "device.door", inactiveLabel: false, decoration: "flat") {
      state("default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh")
    }

    standardTile("contact", "device.contact") {
      state("open", label: '${name}', icon: "st.contact.contact.open", backgroundColor: "#ffa81e")
      state("closed", label: '${name}', icon: "st.contact.contact.closed", backgroundColor: "#79b821")
    }

    standardTile("button", "device.switch") {
      state("on", label: '${name}', icon: "st.contact.contact.open", backgroundColor: "#ffa81e")
      state("off", label: '${name}', icon: "st.contact.contact.closed", backgroundColor: "#79b821")
    }

    valueTile("lastActivity", "device.lastActivity", inactiveLabel: false, decoration: "flat") {
      state("default", label: 'Last activity: ${currentValue}', action: "refresh.refresh", backgroundColor: "#ffffff")
    }

    main "door"

    details(["door", "lastActivity", "refresh"])
  }
}

def close() {
  log.debug "Executing 'close'"

  parent.sendCommand(this, "desireddoorstate", 0)
  deviceUpdateStatus(5)
  deviceUpdateLastActivity(parent.deviceLastActivity(this))
}

def off() {
  log.debug "Executing 'off'"

  sendEvent(name: "button", value: "off", isStateChange: true, display: false, displayed: false)
}

def on() {
  log.debug "Executing 'on'"

  push()
  sendEvent(name: "button", value: "on", isStateChange: true, display: false, displayed: false)
}

def open() {
  log.debug "Executing 'open'"

  parent.sendCommand(this, "desireddoorstate", 1)
  deviceUpdateStatus(4)
  deviceUpdateLastActivity(parent.deviceLastActivity(this))
}

def parse(String description) {
}

def poll() {
  log.debug "Executing 'poll'"

  refresh()
}

def push() {
  log.debug "Executing 'push'"

  def doorState = device.currentState("door")?.value

  if (doorState == "open" || doorState == "stopped") {
    close()
  } else if (doorState == "closed") {
    open()
  }

  sendEvent(name: "momentary", value: "pushed", display: false, displayed: false)
}

def refresh() {
  log.debug "Executing 'refresh'"

  parent.refresh()
  deviceUpdateLastActivity(parent.deviceLastActivity(this))
}

def deviceUpdateStatus() {
  log.debug "Executing 'deviceUpdateStatus'"

  def currentState = device.currentState("door")?.value

  if (status == "1" || status == "9") {
    sendEvent(name: "door", value: "open", display: true, descriptionText: device.displayName + " is open")
    sendEvent(name: "contact", value: "open", display: false, displayed: false)
  }

  if (status == "2") {
    sendEvent(name: "door", value: "closed", display: true, descriptionText: device.displayName + " is closed")
    sendEvent(name: "contact", value: "closed", display: false, displayed: false)
  }

  if (status == "3") {
    sendEvent(name: "door", value: "stopped", display: true, descriptionText: device.displayName + " has stopped")
    sendEvent(name: "contact", value: "closed", display: false, displayed: false)
  }

  if (status == "4" || (status=="8" && currentState=="closed")) {
    sendEvent(name: "door", value: "opening", display: false, displayed: false)
  }

  if (status == "5" || (status=="8" && currentState=="open")) {
    sendEvent(name: "door", value: "closing", display: false, displayed: false)
  }
}

def deviceUpdateLastActivity() {
  log.debug "Executing 'deviceUpdateLastActivity'"

  def lastActivityValue = ""
  def diffTotal = now() - lastActivity
  def diffDays  = (diffTotal / 86400000) as long
  def diffHours = (diffTotal % 86400000 / 3600000) as long
  def diffMins  = (diffTotal % 86400000 % 3600000 / 60000) as long

  if (diffDays == 1) {
    lastActivityValue += "${diffDays} Day "
  } else if (diffDays > 1) {
    lastActivityValue += "${diffDays} Days "
  }

  if (diffHours == 1) {
    lastActivityValue += "${diffHours} Hour "
  }	else if (diffHours > 1) {
    lastActivityValue += "${diffHours} Hours "
  }

  if (diffMins == 1 || diffMins == 0 ) {
    lastActivityValue += "${diffMins} Min"
  } else if (diffMins > 1) {
    lastActivityValue += "${diffMins} Mins"
  }

  sendEvent(name: "lastActivity", value: lastActivityValue, display: false , displayed: false)
}
