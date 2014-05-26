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

int ID = 1;
int STATUS = 1;
unsigned long time;
unsigned long startTime;
unsigned long totalTime;

#define MSG_SIZE 100
char MSG[MSG_SIZE];

byte mac[] = {  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

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
  pinMode(EN485 , OUTPUT);
  pinMode(2 , INPUT_PULLUP);
  RELAY_OFF();
  
  SET_RC522RST;

  //串口初始化
  debugSerial.begin(9600);//串口2：主控板与模块通信
  serial485.begin(1200);
  Serial.begin(9600);//串口：主控板与USB通信
  delay(10);
  
  send485("485 OK");

  
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
  digitalWrite(RELAY, LOW);
}

void RELAY_OFF()
{
  digitalWrite(RELAY, HIGH);
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
  serial485.print(MSG);
}

int read485(char* MSG)
{
  clearMSG(MSG);
  enableRead485();
  if(serial485.available())
  {
    delay(50);
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
    if(getID(MSG) == ID)
    {
      beep();
      if(getCommmand(MSG) == 'C')
        RELAY_ON();
      else if(getCommmand(MSG) == 'D')
        RELAY_OFF();
    }
  }
}

int getID(char* MSG)
{
  int id = 0;
  for(int i = 1 ; i < 4 ; i++)
    id = id * 10 + MSG[i] - '0';
  return id;
}

char getCommmand(char* MSG)
{
  return MSG[7];
}


unsigned long getTotalTime(char* MSG) {

  int i = 8;
  int len = strlen(MSG);
  unsigned long res = 0;
  while(i<len && MSG[i]>='0' && MSG[i]<='9')
  {
    res = res*10 + (MSG[i]-'0');
    i++;
  }

  res = res*60000;

  return res;
}

void status1()
{
  //IDLE
  debugSerial.println("1");
  IDLE_LED();
  
//  delay(1000);
  
  if(readCard(CardID_temp))
  {
    if(CardID_temp[0] != CardID[0] || CardID_temp[1] != CardID[1] || CardID_temp[2] != CardID[2] || CardID_temp[3] != CardID[3])
    {
      memcpy(CardID, CardID_temp, sizeof(CardID));
    }
    beep();
    LED_OFF();
    clearMSG(MSG);
    sprintf(MSG, "$SRV%03dA%03d%03d%03d%03d", ID, CardID[0], CardID[1], CardID[2], CardID[3]);
    send485(MSG);
    time = millis();
    STATUS = 2;
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

  if(!readCard(CardID_temp))
  {
    STATUS = 1;
  }
  else
  {
      if(read485(MSG))
      { 
        if(MSG[7] == 'C') //connect
        {
          if((totalTime = getTotalTime(MSG))!=0) 
          {
            RELAY_ON();
            STATUS = 3;
            startTime = millis();
          }else {
           
           STATUS = 1; 
          }
        }
        else if(MSG[7] == 'D') //disconnect
        {
          beep();
          beep();
          RELAY_OFF();
          STATUS = 1;
        }
      }
  }
}

void status3()
{
  debugSerial.println("3");
  RED_OFF();
  GREEN_ON();    
  if(read485(MSG))
  {
    if(MSG[7] == 'D')
    {
      RELAY_OFF();
      STATUS = 1;
    }
  }

  if(millis()-startTime>totalTime)
  {
      beep_OFF();
      RELAY_OFF();
      clearMSG(MSG);
      sprintf(MSG, "$SRV%03dB%03d%03d%03d%03d", ID, CardID[0], CardID[1], CardID[2], CardID[3]);
      send485(MSG);
      STATUS = 1;
  }
  else if(millis()-startTime+10000>totalTime) 
  {
    beep_long();  
  }
  else if(millis()-startTime+30000>totalTime)
  {
    beep();
  }else {

    RED_FLASH();
  }
  
  PcdRequest(PICC_REQIDL,&RevBuffer[0]);
  if(!readCard(CardID_temp))
  {
    time = millis();
    STATUS = 4;
  }
}

void status4()
{
  debugSerial.println("4");
  beep_OFF();
  GREEN_OFF();
  RED_FLASH();
  if(((millis() - time) > 300000) || (millis()-startTime>totalTime))
  {
    RELAY_OFF();
    clearMSG(MSG);
    sprintf(MSG, "$SRV%03dB%03d%03d%03d%03d", ID, CardID[0], CardID[1], CardID[2], CardID[3]);
    send485(MSG);
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
  beep_long();
  LED_ON();
}

