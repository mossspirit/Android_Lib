#define buf0 9
#define buf1 10
#define buf2 11
#define buf3 12
#define con 13
int count = 0;
int data;
void setup() {
  Serial.begin(9600);
  pinMode(buf0, OUTPUT);
  pinMode(buf1, OUTPUT);
  pinMode(buf2, OUTPUT);
  pinMode(buf3, OUTPUT);
}

void loop() {
  if (Serial.available()) {
    int data = Serial.read();
    Serial.println(data, BIN);

    if ((data & 0x08) != 0)
    {
      digitalWrite(buf0, HIGH);
    }
    else if ((data & 0x08) != 1) digitalWrite(buf0, LOW);

    if ((data & 0x04) != 0)
    {
      digitalWrite(buf1, HIGH);
    }
    else if ((data & 0x04) != 1) digitalWrite(buf1, LOW);

    if ((data & 0x02) != 0)
    {
      digitalWrite(buf2, HIGH);
    }
    else if ((data & 0x02) != 1) digitalWrite(buf2, LOW);

    if ((data & 0x01) != 0)
    {
      digitalWrite(buf3, HIGH);
    }
    else if ((data & 0x01) != 1) digitalWrite(buf3, LOW);
  }
}
