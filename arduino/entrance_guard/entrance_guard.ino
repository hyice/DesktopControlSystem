#include <LCD12864RSPI.h>

#include <SPI.h>

#include <Dhcp.h>
#include <Dns.h>
#include <Ethernet.h>
#include <EthernetClient.h>
#include <EthernetServer.h>
#include <EthernetUdp.h>
#include <util.h>

#include <SoftwareSerial.h>

/*
 * RFID 读写卡测试程序 DFrobot
 *   MFRC522 模块
 *   MRC50-1 白卡
 * 模块资料：http://wiki.dfrobot.com.cn/index.php?title=RFID_Reader_Module%28.NET_Gadgeteer_Compatible%29_%28SKU:TOY0019%29
 */

#include "RC522.h"

/* 特别重要!! 
 * 如果使用的IDE版本小于1.0，请务必将下行注释掉
 */
#define NEWIDE 


/////////////////////////////////////////////////////////////////////
//复位引脚定义
/////////////////////////////////////////////////////////////////////
#define RC522_RST  2//Dreamer MEGA  X14 port PIN6
#define BUZZER_PIN A0
#define RED_LIGHT A4
#define GREEN_LIGHT A5
#define RELAY A3

#define EN485 6

#define AR_SIZE( a ) sizeof( a ) / sizeof( a[0] )

int STATUS = 5;
unsigned long time;

#define MSG_SIZE 100
char MSG[MSG_SIZE];

byte mac[] = {  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xEF };
IPAddress server(10,214,9,210);
EthernetClient client;
IPAddress ip(10,214,9, 220);
#define SET_RC522RST  digitalWrite(RC522_RST,HIGH)
#define CLR_RC522RST  digitalWrite(RC522_RST,LOW)


unsigned char RevBuffer[30];
unsigned char CardID[4] = {
  0 , 0 , 0 , 0};//存储卡片序列号

unsigned char CardID_temp[4] = {
  0 , 0 , 0 , 0};

unsigned char Read_Data[16]={
  0x00};
unsigned char PassWd[6]={
  0xFF,0xFF,0xFF,0xFF,0xFF,0xFF};//密码
unsigned char WriteData[16]={
  0};
unsigned char NewKey[16]={
  0x00};//存储新密码

unsigned char BlockN=0;
unsigned char Command;
unsigned char N;
unsigned char Wdata;


unsigned char show0[]={ 
  0xA1, 0xA1, 
  0xD5, 0xE3,
  0xBD, 0xAD,
  0xC0, 0xED,
  0xB9, 0xA4,
  0xB4, 0xF3,
  0xD1, 0xA7,
  0xA1, 0xA1 };//浙江理工大学
unsigned char show1[]={
  0xA1, 0xA1, 
  0xA1, 0xA1, 
  0xBB, 0xB6,
  0xD3, 0xAD,
  0xC4, 0xFA,
  0xA1, 0xA1,
  0xA1, 0xA1,
  0xA1, 0xA1};//欢迎您
  
/////////////////////////////////////////////////////////////////////
//功    能：初始化RC522
//
/////////////////////////////////////////////////////////////////////
void InitRc522(void)
{
  PcdReset();
  PcdAntennaOff();  
  PcdAntennaOn();
  M500PcdConfigISOType( 'A' );
}

void setup()
{
  //设定端口模式
  pinMode(RC522_RST,OUTPUT);
  pinMode(BUZZER_PIN , OUTPUT);
  pinMode(RED_LIGHT , OUTPUT);
  pinMode(GREEN_LIGHT , OUTPUT);
  pinMode(RELAY , OUTPUT);
  pinMode(EN485 , OUTPUT);
  RELAY_OFF();
  
  LED_ON();
  
  SET_RC522RST;

  //串口初始化
  debugSerial.begin(9600);//串口2：主控板与模块通信
  serial485.begin(9600);
  Serial.begin(9600);//串口：主控板与USB通信
  
  LCDA.Initialise(); // 屏幕初始化
  showWelcome();
  delay(10);
  
  send485("485 OK");
  
  Ethernet.begin(mac, ip);
  delay(100);
  InitRc522();//初始化
  delay(10);
  debugSerial.println("The initialization completes.  ");
  debugSerial.println();
}

void loop()
{
  check485();
  switch(STATUS)
  {
  case 1:
    status1();
    break;
  case 2:
    status2();
    break;
  case 3:
    status3();
    break;
  case 4:
    status4();
    break;
  case 5:
    status5();
    break;
  }
}

void beep()
{
  digitalWrite(BUZZER_PIN, HIGH);
  delay(200);
  digitalWrite(BUZZER_PIN, LOW);
}

void beep2()
{
  digitalWrite(BUZZER_PIN, HIGH);
  delay(200);
  digitalWrite(BUZZER_PIN, LOW);
  delay(200);
  digitalWrite(BUZZER_PIN, HIGH);
  delay(200);
  digitalWrite(BUZZER_PIN, LOW);
}

void beep_long()
{
  digitalWrite(BUZZER_PIN, HIGH);
}

void beep_OFF()
{
  digitalWrite(BUZZER_PIN, LOW);
}

void RELAY_ON()
{
  digitalWrite(RELAY, HIGH);
}

void RELAY_OFF()
{
  digitalWrite(RELAY, LOW);
}

void RED_ON()
{
  digitalWrite(RED_LIGHT, HIGH);
}

void RED_OFF()
{
  digitalWrite(RED_LIGHT, LOW);
}

void RED_FLASH()
{
  RED_ON();
  delay(200);
  RED_OFF();
  delay(200);
}

void GREEN_ON()
{
  digitalWrite(GREEN_LIGHT, HIGH);
}

void GREEN_OFF()
{
  digitalWrite(GREEN_LIGHT, LOW);
}

void IDLE_LED()
{
  RED_ON();
  delay(200);
  RED_OFF();
  GREEN_ON();
  delay(200);
  GREEN_OFF();
  delay(200);
}

void LED_ON()
{
  RED_ON();
  GREEN_ON();
}

void LED_OFF()
{
  RED_OFF();
  GREEN_OFF();
}

int readCard(unsigned char CardID[4])
{
  debugSerial.println("readCard");
  char stat;
  stat=PcdRequest(PICC_REQIDL,&RevBuffer[0]);//寻天线区内未进入休眠状态的卡，返回卡片类型 2字节
  if(stat!=MI_OK)
  {
    return 0;
  }
  debugSerial.print("Card type code; ");
  debugSerial.print(RevBuffer[0],DEC);
  debugSerial.print(',');
  debugSerial.println(RevBuffer[1],DEC);
  debugSerial.println();
  
  stat=PcdAnticoll(&RevBuffer[2]);//防冲撞，返回卡的序列号 4字节
  if(stat!=MI_OK)
  {
    return 0;
  }
  memcpy(CardID,&RevBuffer[2],4); 
  stat=PcdSelect(CardID);//选卡
  if(stat!=MI_OK)
  {
    return 0;
  }
  debugSerial.print("Card series number; ");
  debugSerial.print(CardID[0],DEC);
  debugSerial.print(',');
  debugSerial.print(CardID[1],DEC);
  debugSerial.print(',');
  debugSerial.print(CardID[2],DEC);
  debugSerial.print(',');
  debugSerial.println(CardID[3],DEC);
  debugSerial.println();
  return 1;
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
    debugSerial.println(MSG);
    if(MSG[1] == 'M' && MSG[2] == 'E' && MSG[3] == 'N')
      return 1;
    else
    {
      send485(MSG);
      return 0;
    }
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
    for(int i = 0 ; serial485.available() ; i++)
      MSG[i] = serial485.read();
    if(MSG[0] != 0)
      return 1;
  }
  return 0;
}

void check485()
{
  if(read485(MSG))
  {
    debugSerial.println("read485");
    debugSerial.println(MSG);
    sendMSG(MSG);
  }
}

void showWelcome()
{
   LCDA.CLEAR();//清屏
   LCDA.DisplayString(1,0,show0,16);
   LCDA.DisplayString(2,0,show1,16);
}

void showTXT(unsigned char* TXT)
{
  LCDA.CLEAR();//清屏
  int len = strlen((char*)TXT);
  int s = ceil(len / 16.0)*16 ;
  for(int i = len ; i < s ; i++)
    TXT[i] = 0xA1;
   
  if(len > 0)
    LCDA.DisplayString(0,0,TXT,16);
  len -= 16;
  if(len > 0)
    LCDA.DisplayString(1,0,TXT+16,16);
  len -= 16;
  if(len > 0)
    LCDA.DisplayString(2,0,TXT+32,16);
  len -= 16;
  if(len > 0)
    LCDA.DisplayString(3,0,TXT+48,16);
}

void status1()
{
  //IDLE
  debugSerial.println("1");
  IDLE_LED();

  if(readCard(CardID_temp))
  {
    if(CardID_temp[0] != CardID[0] || CardID_temp[1] != CardID[1] || CardID_temp[2] != CardID[2] || CardID_temp[3] != CardID[3])
    {
      memcpy(CardID, CardID_temp, sizeof(CardID));
    }
    LCDA.CLEAR();//清屏
    beep();
    LED_OFF();
    clearMSG(MSG);
    sprintf(MSG, "$SRVMENA%03d%03d%03d%03d", CardID[0], CardID[1], CardID[2], CardID[3]);
    sendMSG(MSG);
    STATUS = 2;
    time = millis();
  }
  
}

void status2()
{
  debugSerial.println("2");

  if(millis() - time > 5000)
  {
    STATUS = 5;
  }
    
  PcdRequest(PICC_REQIDL,&RevBuffer[0]);

  if(readMSG(MSG))
  { 
    if(MSG[7] == 'C') //connect
    {
      RELAY_ON();
      showTXT((unsigned char*)MSG+8);
      STATUS = 3;
      time = millis();
    }
    else if(MSG[7] == 'D') //disconnect
    {
      beep();
      beep();
      RELAY_OFF();
      showTXT((unsigned char*)MSG+8);
      STATUS = 5;
    }
  }
}

void status3()
{
  debugSerial.println("3");
  RED_OFF();
  GREEN_ON();
  if(millis() - time > 5000)
  {
    RELAY_OFF();
    STATUS = 5;
  }
}

void status4()
{
  debugSerial.println("4");
  RED_FLASH();
  if(millis() - time > 300000)
  {
    RELAY_OFF();
    STATUS = 1;
  }
  
//  PcdRequest(PICC_REQIDL,&RevBuffer[0]);
  if(readCard(CardID_temp))
  {
    if(CardID_temp[0] != CardID[0] || CardID_temp[1] != CardID[1] || CardID_temp[2] != CardID[2] || CardID_temp[3] != CardID[3])
    {
      beep2();
    }
    else
    {
      STATUS = 3;
    }
  }
}

void status5()
{
  client.stop();
  beep_long();
  LED_ON();
  debugSerial.println("connecting...");
  // if you get a connection, report back via serial:
  if (client.connect(server, 4321)) {
    debugSerial.println("connected");
    beep_OFF();
    LED_OFF();
    STATUS = 1;
  } 
  else {
    // kf you didn't get a connection to the server:
    debugSerial.println("connection failed");
  }
  
}

