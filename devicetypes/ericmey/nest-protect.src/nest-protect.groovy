/**
 *  Nest Protect
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
  definition (name: "Nest Protect", namespace: "ericmey", author: "Eric Mey") {
    capability "Battery"
    capability "Carbon Monoxide Detector"
    capability "Polling"
    capability "Smoke Detector"
    
    command "deviceUpdateStatus"
  }
  
  simulator {
  }
  
  tiles {
    valueTile("smoke", "device.smoke", width: 2, height: 2) {
      state("smoke", label: 'Smoke ${currentValue}', unit: "smoke", backgroundColors: [
        [value: "clear", color: "#44B621"],
        [value: "detected", color: "#e86d13"],
        [value: "tested", color: "#003CEC"]
      ])
    }
    
    valueTile("carbonMonoxide", "device.carbonMonoxide"){
      state("carbonMonoxide", label: 'CO ${currentValue}', unit:"CO", backgroundColors: [
        [value: "clear", color: "#44B621"],
        [value: "detected", color: "#e86d13"],
        [value: "tested", color: "#003CEC"]
      ])
    }

    valueTile("battery", "device.battery"){
      state("battery", label: 'Battery ${currentValue}', unit:"battery", backgroundColors: [
        [value: "OK", color: "#44B621"],
        [value: "Low", color: "#e86d13"]
      ])
    }

    standardTile("refresh", "device.smoke", inactiveLabel: false, decoration: "flat") {
      state "default", action:"polling.poll", icon:"st.secondary.refresh"
    }

    main "smoke"

    details(["smoke", "carbonMonoxide", "battery", "refresh"])
  }
}

def parse(String description) {
}

def poll() {
  log.debug "Executing 'poll'"
  
  deviceUpdateStatus(parent.deviceStatus(this))
}

def deviceUpdateStatus() {
  log.debug "Executing 'deviceUpdateStatus'"
  
  statusSmoke = data.device.smoke_status == 0 ? "clear" : "detected"
  statusCarbonMonoxide = data.device.co_status == 0 ? "clear" : "detected"
  statusBattery = data.device.battery_health_state == 0 ? "OK" : "Low"
  
  sendEvent(name: 'smoke', value: statusSmoke)
  sendEvent(name: 'carbonMonoxide', value: statusCarbonMonoxide)
  sendEvent(name: 'battery', value: statusBattery )
}