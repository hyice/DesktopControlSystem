#include <LCD12864RSPI.h>

#include <SPI.h>

#include <Dhcp.h>
#include <Dns.h>
#include <Ethernet.h>
#include <EthernetClient.h>
#include <EthernetServer.h>
#include <EthernetUdp.h>
#include <util.h>s

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

#define AR_SIZE( a ) sizeof( a ) / sizeof( a[0] )

#define MSG_SIZE 100
char MSG[MSG_SIZE];

byte mac[] = {  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xEF };
IPAddress server(10,214,9,210);
EthernetClient client;
IPAddress ip(10,214,9, 222);
#define SET_RC522RST  digitalWrite(RC522_RST,HIGH)
#define CLR_RC522RST  digitalWrite(RC522_RST,LOW)


unsigned char RevBuffer[30];
unsigned char CardID[4] = {
  0 , 0 , 0 , 0};//存储卡片序列号

unsigned char CardID_temp[4] = {
  0 , 0 , 0 , 0};
  
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
  RELAY_OFF();
  
  LED_ON();
  
  SET_RC522RST;

  //串口初始化
  debugSerial.begin(9600);//串口2：主控板与模块通信
  Serial.begin(9600);//串口：主控板与USB通信
  
  LCDA.Initialise(); // 屏幕初始化
  delay(10);
  
  Ethernet.begin(mac, ip);
  delay(100);
  InitRc522();//初始化
  delay(10);
  debugSerial.println("The initialization completes.  ");
  debugSerial.println();

  beep_long();
  LED_ON();

  if (client.connect(server, 4321)) {
    debugSerial.println("connected");
    beep_OFF();
    LED_OFF();
  } 
}

void loop()
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
    beep();
    LED_OFF();
    clearMSG(MSG);
    sprintf(MSG, "$SRVADDE%03d%03d%03d%03d", CardID[0], CardID[1], CardID[2], CardID[3]);
    sendMSG(MSG);
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
    debugSerial.println(MSG);
    return 1;
  }
  return 0;
}

