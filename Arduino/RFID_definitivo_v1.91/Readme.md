University project I developed to showcase an RFID operated intelligent fresh food warehouse using an Arduino. Trucks would be scanned through gates at the warehouse entrance 
and it would trigger on the ERP the contents of the truck and if the temperature inside the cargo area of the truck had had any variations (such as doors being opened) in which 
case the care would be taken to an inspection bay to evaluate if the goods had sufered any damages.

Demonstration was performed using an ID-20LA RFID reader for 125 kHz and regular 125 kHZ access RFID cards were used as trucks clearing the gates. Card Ids were hardcoded and
assigned trucks and drivers, some Ids were used as "wrong temperature" for ease of demonstration as the real world application would have thermal sensors and obviously wouldn't
be using highly unsecure 125 kHz cards.

The ERP would register delivery times of the trucks and if the warehouse was full or the space allocated for that product, it would automatically send the truck back without 
the need to unload teh truck and the possible risks of manipulating the goods unnecessarily.
