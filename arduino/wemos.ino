/*
  ESP8266 Blink by Simon Peter
  Blink the blue LED on the ESP-01 module
  This example code is in the public domain

  The blue LED on the ESP-01 module is connected to GPIO1
  (which is also the TXD pin; so we cannot use Serial.print() at the same time)

  Note that this sketch uses LED_BUILTIN to find the pin with the internal LED
*/

// pinout ref: https://lastminuteengineers.com/wemos-d1-mini-pinout-reference/

#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>
#include <uri/UriBraces.h>

/* Put your SSID & Password */
const char* ssid = "HUAWEI-4w4R";  // Enter SSID here
const char* password = "A2Fv9XTUQU";  //Enter Password here

ESP8266WebServer server(80);

struct Pin {
  String pinName;
  int gpio;
  bool enabled;
};

Pin pins[] = {
  {"D0", D0, false},
  {"D1", D1, false},
  {"D2", D2, false},
  {"D3", D3, false},
  {"D4", D4, false},
  {"D5", D5, false},
  {"D6", D6, false},
  {"D7", D7, false},
  {"D8", D8, false},
};

void handleEnable() {
  handlePinChange(true);
}


void handleDisable() {
  handlePinChange(false);
}

void handlePinChange(bool enable) {
  String pinName = server.pathArg(0);
  Serial.print("Got request for pin:"); Serial.println(pinName);
  
  // Find the pin by name and enable it
  for (auto& pin : pins) {
    if (pin.pinName == pinName) {
      Serial.print("Set pin: ");  Serial.print(pinName); Serial.print(" enable: "); Serial.println(enable);
      digitalWrite(pin.gpio, enable);
      pin.enabled = enable;
      break;
    }
  }
  
  sendPinStateResponse();
}

void sendPinStateResponse() {
  // Construct JSON response
  DynamicJsonDocument doc(1024);

  JsonArray pinArray = doc.to<JsonArray>();
  
  // Add each pin's state to the JSON response
  for (auto& pin : pins) {
    JsonObject pinObj = pinArray.createNestedObject();
    pinObj["pinName"] = pin.pinName;
    pinObj["gpio"] = pin.gpio;
    pinObj["enabled"] = pin.enabled;
  }

  String jsonResponse;
  serializeJson(doc, jsonResponse);

  server.send(200, "application/json", jsonResponse);
}

void setup() {
  Serial.begin(115200);
  delay(100);
  initPins();
  connectToWiFi();
  startHttpServer();
}

void initPins() {
  pinMode(D0, OUTPUT);
  pinMode(D1, OUTPUT);
  pinMode(D2, OUTPUT);
  pinMode(D3, OUTPUT);
  pinMode(D4, OUTPUT);
  pinMode(D5, OUTPUT);
  pinMode(D6, OUTPUT);
  pinMode(D7, OUTPUT);
  pinMode(D8, OUTPUT);
}

void connectToWiFi() {
  Serial.println("Connecting to ");
  Serial.println(ssid);

  //connect to your local wi-fi network
  WiFi.begin(ssid, password);

  //check wi-fi is connected to wi-fi network
  while (WiFi.status() != WL_CONNECTED) {
  delay(1000);

  Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected..!");
  Serial.print("Got IP: ");  Serial.println(WiFi.localIP());
}

void startHttpServer() {
    // Set up routes
  server.on("/pins", HTTP_GET, sendPinStateResponse);
  server.on(UriBraces("/pins/{}/enable"), HTTP_POST, handleEnable);
  server.on(UriBraces("/pins/{}/disable"), HTTP_POST, handleDisable);

  server.begin();
  Serial.println("HTTP server started");
}

// the loop function runs over and over again forever
void loop() {
  server.handleClient();
}
