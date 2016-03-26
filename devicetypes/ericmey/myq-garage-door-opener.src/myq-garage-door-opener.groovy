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
		// TODO: define your main and details tiles here
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'contact' attribute
	// TODO: handle 'door' attribute
	// TODO: handle 'door' attribute
	// TODO: handle 'switch' attribute
	// TODO: handle 'lastActivity' attribute

}

def open() {
	log.debug "Executing 'open'"
	// TODO: handle 'open' command
}

def close() {
	log.debug "Executing 'close'"
	// TODO: handle 'close' command
}

def push() {
	log.debug "Executing 'push'"
	// TODO: handle 'push' command
}

def poll() {
	log.debug "Executing 'poll'"
	// TODO: handle 'poll' command
}

def refresh() {
	log.debug "Executing 'refresh'"
	// TODO: handle 'refresh' command
}

def on() {
	log.debug "Executing 'on'"
	// TODO: handle 'on' command
}

def off() {
	log.debug "Executing 'off'"
	// TODO: handle 'off' command
}

def deviceUpdateStatus() {
	log.debug "Executing 'deviceUpdateStatus'"
	// TODO: handle 'deviceUpdateStatus' command
}

def deviceUpdateLastActivity() {
	log.debug "Executing 'deviceUpdateLastActivity'"
	// TODO: handle 'deviceUpdateLastActivity' command
}