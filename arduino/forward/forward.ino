#include <SPI.h>

#include <Dhcp.h>
#include <Dns.h>
#include <Ethernet.h>
#include <EthernetClient.h>
#include <EthernetServer.h>
#include <EthernetUdp.h>
#include <util.h>

#include <SoftwareSerial.h>
#define EN485 14
SoftwareSerial serial485(15 , 16);

byte mac[] = {  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0x1D };
IPAddress server(10,214,9,228);
EthernetClient client;
IPAddress ip(10,214,9,201);

#define MSG_SIZE 100
char MSG[MSG_SIZE];

void setup() {
  // put your setup code here, to run once:
  pinMode(EN485 , OUTPUT);
  Serial.begin(9600);
  serial485.begin(1200);
  Ethernet.begin(mac, ip);
  Serial.println(Ethernet.localIP());
  
  Serial.println("connecting...");
  // if you get a connection, report back via serial:
  if (client.connect(server, 4321)) {
    Serial.println("connected");
  } 
  else {
    // kf you didn't get a connection to the server:
    Serial.println("connection failed");
  }
}

void loop() {
  // put your main code here, to run repeatedly: 
  readMSG(MSG);
  check485();
}

void clearMSG(char* MSG)
{
  memset(MSG, 0, sizeof(MSG[0])*MSG_SIZE);
}

void sendMSG(char* MSG)
{
//  for(int i = 0 ; i < 20 ; i++)
//    client.print(MSG[i]);
   client.println(MSG);
}

int readMSG(char* MSG)
{
  clearMSG(MSG);
  if(client.available())
  {
    for(int i = 0 ; client.available() ; i++)
    {
      MSG[i] = client.read();
    }
    client.flush();
    send485(MSG);
    Serial.println(MSG);
  }
  return 0;
}

void enableRead485()
{
  digitalWrite(EN485, LOW);
}

void enableWrite485()
{
  digitalWrite(EN485, HIGH);
}

void send485(char* MSG)
{
  enableWrite485();
  serial485.println(MSG);
}

int read485(char* MSG)
{
  clearMSG(MSG);
  enableRead485();
  if(serial485.available())
  {
    delay(50);
    for(int i = 0 ; serial485.available() ; i++)
    {
      MSG[i] = serial485.read();
    }

  if(MSG[0] != 0)
      return 1;
  }
  return 0;
}

void check485()
{
  if(read485(MSG))
  {
    Serial.println("read485");
    Serial.println(MSG);
    sendMSG(MSG);
  }
}
