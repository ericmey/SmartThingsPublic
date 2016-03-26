/**
 *  MyQ Remote Light
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
  definition (name: "MyQ Remote Light", namespace: "ericmey", author: "Eric Mey") {
    capability "Actuator"
    capability "Polling"
    capability "Refresh"
    capability "Sensor"
    capability "Switch"

    command "deviceUpdateStatus"
  }

  simulator {
  }

  tiles {
    standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
      state("off", label: 'Off', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on")
      state("on", label: 'On', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off")
    }

    standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
      state("default", label: '', action: "refresh.refresh", icon: "st.secondary.refresh")
    }

    main "switch"

    details(["switch", "refresh"])
  }
}

def off() {
  log.debug "Executing 'off'"

  parent.sendCommand(this, "desiredlightstate", 0)
  updateDeviceStatus(0)
}

def on() {
  log.debug "Executing 'on'"

  parent.sendCommand(this, "desiredlightstate", 1)
  updateDeviceStatus(1)
}

def parse(String description) {
}

def poll() {
  log.debug "Executing 'poll'"

  deviceUpdateStatus(parent.deviceStatus(this))
}

def refresh() {
  log.debug "Executing 'refresh'"

  parent.refresh()
}

def deviceUpdateStatus() {
  log.debug "Executing 'deviceUpdateStatus'"

  if (status == "0") {
    sendEvent(name: "switch", value: "off", display: true, descriptionText: device.displayName + " was off")
  }

  if (status == "1") {
    sendEvent(name: "switch", value: "on", display: true, descriptionText: device.displayName + " was on")
  }
}
