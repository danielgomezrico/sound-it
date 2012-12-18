
// Matrix values
const int ledsWidth = 9, ledsHeight = 3;
int lineFirst[ledsWidth] = { LOW,LOW,LOW, LOW,LOW,LOW, LOW,LOW,LOW };
int lineSec[ledsWidth] = { LOW,LOW,LOW, LOW,LOW,LOW, LOW,LOW,LOW };
int lineThird[ledsWidth] = { LOW,LOW,LOW, LOW,LOW,LOW, LOW,LOW,LOW };
int* leds[ledsHeight];
int* column;

// Position values
int actualLed, actualFloor;

  // Read String from serial
   int inputCounter = 0;
   char inChar = ' ';
   String inData = "";

void setup()  
{
  actualLed = 0, actualFloor = 0;

  // Init Leds matrix
  leds[0] = lineFirst;
  leds[1] = lineSec;
  leds[2] = lineThird;
  
  // Setup Initial state and pinmode for each pin
  int i;
  for (i = 2; i < 14; i = i + 1) {
     pinMode(i, OUTPUT);  
     digitalWrite(i, LOW);  
  }
  
  digitalWrite(2, HIGH);   
  digitalWrite(3, HIGH);
  digitalWrite(4, HIGH);    

 
 // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect.
  }
  
}

void loop() // run over and over
{
 
   while (Serial.available() > 0)
   {
      
      inChar = (char)Serial.read();
      
      if(isDigit(inChar)){
        
        inData += inChar;
                        
        if(inputCounter == 0){
          actualFloor = inData.toInt();  
        }else if(inputCounter == 1){
          actualLed = inData.toInt();
        }
      
        inputCounter+=1;
        inData = "";
      }
    }
    
    if(inChar == '\n'){
      inputCounter = 0;
    }  
    
  updateLedstate();
  
}

void updateLedstate(){

   // Send signals for each led
   
  digitalWrite(2, HIGH);   
  digitalWrite(3, HIGH);
  digitalWrite(4, HIGH);    

  int led, myFloor;
  boolean founded = false;
  for( myFloor = 0 ; myFloor < ledsHeight; myFloor++)
  {
    column = leds[myFloor]; // get the array address
    
    for( led = 0; led < ledsWidth; led++)
    { 
      if( myFloor == actualFloor && led == actualLed){
        *column = HIGH;
        digitalWrite(led + 5,  *column);
        digitalWrite(myFloor + 2, LOW);   

        founded = true;
      }else{
        *column = LOW;
        digitalWrite(led + 5,  *column);
      }
      
      column++;

    }
    
    if(founded){
      break;
    }
  }
}

