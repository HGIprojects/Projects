//Historial de versiones
//
//v1.9 Comentarios explicativos añadidos
//v1.8 Semáforo LED operativo, funciones optimizadas
//v1.7 Duracion entregas de camiones implementada, modificación de sistema registro de camiones
//v1.6 Semaforos añadido, fallo LED amarillo y problema luminosidad
//v1.5 Introduccion de inventario inicial al ejecutar la aplicacion
//v1.4 Programa funcionando correctamente, pendiente de añadir código para LEDs
//v1.3 Código ordenado, problemas con la salida de camiones cuando no se ha registrado ninguna entrada todavía
//v1.2 Cabecera inicial y tablas
//v1.1 Control de tarjetas identificadoras
//v1.0 Boceto previo a llegada hardware

#include <Time.h>
#include <TimeLib.h>
#include <math.h>


char borrar_bufer = ' '; //variable de borrado de buffer de entrada por teclado
int sand; //variables para la entrada por teclado de existencias iniciales
int plat;
int tomat;

const byte verde = 12; //variables para identificar los DIGITAL PIN INPUTS y OUTPUTS
const byte amarillo = 11;
const byte rojo = 10;
const byte pulsador = 9;
byte chekksum; //variable para conversión hexadecimal
long int tiempo_1;  //variables para monitorizar el tiempo entre entrada y salida de los camiones
long int tiempo_2;
long int tiempo_3;
long int tiempo_1_1;
long int tiempo_2_1;
long int tiempo_3_1;
byte minuto_1;
byte hora_1;
byte minuto_2;
byte hora_2;
byte minuto_3;
byte hora_3;
byte cont_1;
byte cont_2;
byte cont_3;
byte n_1; //contadores para la entrada y salida de camiones
byte n_2;
byte n_3;
byte f_sand; //existencias finales de productos
byte f_plat;
byte f_tomat;
byte devol = 0; //devoluciones inicializadas en 0
byte contador = 1; //contador para el tamaño de tabla
byte contador_2 = 0; //contador para comienzo/final de turno
byte boton = 0; //botón pulsador inicializado apagado

void setup() {
n_1 = 0;
n_2 = 0;
n_3 = 0;
test_LEDS(verde,amarillo,rojo);
pinMode(pulsador,INPUT); //DIGITAL PIN del botón pulsador inicializado como receptor de señal
pinMode(verde,OUTPUT); //DIGITAL PIN de los LEDs inicializado como emisor de señal
pinMode(amarillo,OUTPUT);
pinMode(rojo,OUTPUT);            
setTime(1477654200); //fijado de hora inicial
Serial.begin(9600); //velocidad de procesamiento
Serial.println(F("          ::`"));        
Serial.println(F("        `os-"));        
Serial.println(F("   -/:` `::.  .::`"));  
Serial.println(F("  +yyyo`yyyy-:yyyy"));  
Serial.println(F("  .+o+. :oo/``+oo:"));  
Serial.println(F("    .//////////-"));    
Serial.println(F("    .::::::::::-"));    
Serial.println();                    
Serial.println(F(" +dddd+      /ddddo")); 
Serial.println(F(" sMMMMo      +MMMNy")); 
Serial.println(F(" sMMMMo      +MMMNy")); 
Serial.print(F(" sMMMMo      +MMNNy")); 
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("'GESTION DE INVENTARIOS POR RADIOFRECUENCIA'"));
Serial.println(F(" sMMMMo      +MMMMy")); 
Serial.print(F(" sMMMMo      oMMMMy")); 
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F(" oMMMMy     `mMMMMy")); 
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("Autor: HGIProjects"));
Serial.println(F(" -MMMMMh+/+smMMMMMy")); 
Serial.println(F("  /mMMMMMMMNhoMMMMy")); 
Serial.print(F("    -+osoo/` .////-")); 
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("v1.91"));
delay(3000);
Serial.println();
Serial.println();
Serial.println();
Serial.println();
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                       
Serial.println(F("Por favor, introduzca el inventario inicial de sandias:"));
while (Serial.available() == 0) ;  
{
sand = Serial.parseFloat();
f_sand = sand;
Serial.print(F("Existencias iniciales de sandias: ")); 
Serial.println(f_sand,DEC);                                           
while (Serial.available() > 0)  
{ borrar_bufer = Serial.read() ;
}      
}

Serial.println(F("Por favor, introduzca el inventario inicial de platanos:"));
while (Serial.available() == 0) ;
{
plat = Serial.parseFloat();
f_plat = plat;
Serial.print(F("Existencias iniciales de platanos: ")); 
Serial.println(f_plat, DEC);
while (Serial.available() > 0)
{ borrar_bufer = Serial.read() ; 
}
}
Serial.println(F("Por favor, introduzca el inventario inicial de tomates:"));
while (Serial.available() == 0) ;
{
tomat = Serial.parseFloat();
f_tomat = tomat;
Serial.print(F("Existencias iniciales de tomates: ")); 
Serial.println(f_tomat, DEC);
while (Serial.available() > 0)
{ borrar_bufer = Serial.read() ; 
}            
}

Serial.println();
Serial.println();
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                         
Serial.println();
Serial.println();
Serial.println();
Serial.println(F("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////"));
Serial.print(F("////"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Hora"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Producto"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Entradas"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Salidas"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Devoluciones"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Existencias finales"));
Serial.print(F("\t"));
Serial.println(F("////"));                                                                                                                 
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                         
Serial.println();
Serial.println();
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Sandias"));
Serial.print(F("\t"));
Serial.print(F("\t"));                                                 
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_sand);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));  
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Platanos"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_plat);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));   
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Tomates"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_tomat);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));             
}

void camion_1(){//registro de entrada y salida de camión 1 contabilizando el tiempo que pasa dentro del almacén
//variable a empleada como contador para diferenciar entre salida y entrada del camión
//variable b empleada como contador de líneas impresas por pantalla
contador = contador + 1;
if ((n_1 % 2) == 0) {
tiempo_1 = millis();
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Camion con matricula TO-5219-AB realizando entrega"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////")); 
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                                                                                                          
}else {
tiempo_1_1 = millis();
tiempo_1_1 = tiempo_1_1 - tiempo_1;
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Camion con matricula TO-5219-AB entrega finalizada en ")); 
tiempo_1_1 = tiempo_1_1/1000;
if ((tiempo_1_1 / 60) >= 1){
minuto_1 = tiempo_1_1/60;
tiempo_1_1 = tiempo_1_1 % 60;
if ((minuto_1 / 60) >= 1){
hora_1 = minuto_1/60;
minuto_1 = minuto_1 % 60;
}
}
if (minuto_1 > 0){
if (hora_1 > 1) {
Serial.print(hora_1);
Serial.print(F(" horas "));
cont_1 = cont_1 + 1;
}else{
if (hora_1 == 1 ){
Serial.print(hora_1);
Serial.print(F(" hora "));
cont_1 = cont_1 + 1;
}
}
if (minuto_1 > 1){
Serial.print(minuto_1);
Serial.print(F(" minutos "));
cont_1 = cont_1 + 1;
}else{
if (minuto_1 == 1){
Serial.print(minuto_1);
Serial.print(F(" minuto "));
cont_1 = cont_1 + 1;
}
}

}
Serial.print(tiempo_1_1);
Serial.print(F(" segundos.")); 
if (cont_1 == 0){
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));                                               
}
if (cont_1 == 1){
Serial.print(F("\t"));     
Serial.print(F("\t"));
}
if (cont_1 == 2){
Serial.print(F("\t"));
}
Serial.println(F("////")); 
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
minuto_1 = 0;
hora_1 = 0;
cont_1 = 0;
}
}

void camion_2(){//registro de entrada y salida de camión 1 contabilizando el tiempo que pasa dentro del almacén
//variable a empleada como contador para diferenciar entre salida y entrada del camión
//variable b empleada como contador de líneas impresas por pantalla
contador = contador + 1;
if ((n_2 % 2) == 0) {
tiempo_2 = millis();
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Camion con matricula 2532-BXS realizando entrega"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////")); 
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                                                                                                          
}else {
tiempo_2_1 = millis();
tiempo_2_1 = tiempo_2_1 - tiempo_2;
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Camion con matricula 2532-BXS entrega finalizada en ")); 
tiempo_2_1 = tiempo_2_1/1000;
if ((tiempo_2_1 / 60) >= 1){
minuto_2 = tiempo_2_1/60;
tiempo_2_1 = tiempo_2_1 % 60;
if ((minuto_2 / 60) >= 1){
hora_2 = minuto_2/60;
minuto_2 = minuto_2 % 60;
}
}
if (minuto_2 > 0){
if (hora_2 > 1) {
Serial.print(hora_2);
Serial.print(F(" horas "));
cont_2 = cont_2 + 1;
}else{
if (hora_2 == 1 ){
Serial.print(hora_2);
Serial.print(F(" hora "));
cont_2 = cont_2 + 1;
}
}
if (minuto_2 > 1){
Serial.print(minuto_2);
Serial.print(F(" minutos "));
cont_2 = cont_2 + 1;
}else{
if (minuto_2 == 1){
Serial.print(minuto_2);
Serial.print(F(" minuto "));
cont_2 = cont_2 + 1;
}
}

}
Serial.print(tiempo_2_1);
Serial.print(F(" segundos.")); 
if (cont_2 == 0){
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t")); 
Serial.print(F("\t"));                                              
}
if (cont_2 == 1){
Serial.print(F("\t"));     
Serial.print(F("\t"));
}
if (cont_2 == 2){
Serial.print(F("\t"));
}
Serial.println(F("////")); 
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
minuto_2 = 0;
hora_2 = 0;
cont_2 = 0;
}
}


void camion_3(){//registro de entrada y salida de camión 1 contabilizando el tiempo que pasa dentro del almacén
//variable a empleada como contador para diferenciar entre salida y entrada del camión
//variable b empleada como contador de líneas impresas por pantalla
contador = contador + 1;
if ((n_3 % 2) == 0) {
tiempo_3 = millis();
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Camion con matricula M-7463-UD realizando entrega"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////")); 
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                                                                                                          
}else {
tiempo_3_1 = millis();
tiempo_3_1 = tiempo_3_1 - tiempo_3;
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Camion con matricula M-7463-UD entrega finalizada en ")); 
tiempo_3_1 = tiempo_3_1/1000;
if ((tiempo_3_1 / 60) >= 1){
minuto_3 = tiempo_3_1/60;
tiempo_3_1 = tiempo_3_1 % 60;
if ((minuto_3 / 60) >= 1){
hora_3 = minuto_3/60;
minuto_3 = minuto_3 % 60;
}
}
if (minuto_3 > 0){
if (hora_3 > 1) {
Serial.print(hora_3);
Serial.print(F(" horas "));
cont_3 = cont_3 + 1;
}else{
if (hora_3 == 1 ){
Serial.print(hora_3);
Serial.print(F(" hora "));
cont_3 = cont_3 + 1;
}
}
if (minuto_3 > 1){
Serial.print(minuto_3);
Serial.print(F(" minutos "));
cont_3 = cont_3 + 1;
}else{
if (minuto_3 == 1){
Serial.print(minuto_3);
Serial.print(F(" minuto "));
cont_3 = cont_3 + 1;
}
}

}
Serial.print(tiempo_3_1);
Serial.print(F(" segundos.")); 
if (cont_3 == 0){
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));                                               
}
if (cont_3 == 1){
Serial.print(F("\t"));     
Serial.print(F("\t"));
}
if (cont_3 == 2){
Serial.print(F("\t"));
}
Serial.println(F("////")); 
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                                                                                        
minuto_3 = 0;
hora_3 = 0;
cont_3 = 0;
}
}


void sandia_entrada(){//registro de entrada de palets de sandías, en caso de completar el almacén
//con 255 palets las unidades entrantes serán devueltas automáticamente
//variable a contabiliza las existencias de palets
//variable b contabiliza las devoluciones
//variable c empleada como contador de líneas impresas por pantalla
f_sand = f_sand + 1;
contador = contador + 1;
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Sandias"));
Serial.print(F("\t"));
Serial.print(F("\t"));                                                 
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("1"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_sand);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));
if ( f_sand == 255 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Atencion, almacen al limite de capacidad"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));            
}
if ( f_sand == 0 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Atencion, unidad devuelta por sobrecapacidad"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
f_sand = 255;
devol = devol + 1;             
} 
LEDS_stock(f_sand);                      
}

void sandia_salida(){ //registro de salida de palets de sandía
//variable a contabiliza las existencias de palets
//variable b empleada como contador de líneas impresas por pantalla
f_sand = f_sand - 1; 
contador = contador + 1;
if ( f_sand == 255 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Error en el sistema detectado, incongruencia salida/entrada"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
f_sand = 0;
}else{
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("Sandia"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("1"));                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_sand);   
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
}
LEDS_stock(f_sand);             
}

void platano_entrada(){ //registro de entrada de palets de plátanos, en caso de completar el almacén 
//con 255 palets las unidades entrantes serán devueltas automáticamente
//variable a contabiliza las existencias de palets
//variable b contabiliza las devoluciones
//variable c empleada como contador de líneas impresas por pantalla
f_plat = f_plat + 1;
contador = contador + 1;
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Platanos"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("1"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_plat);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));
if ( f_plat == 255 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Atencion, almacen al limite de capacidad"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));            
}
if ( f_plat == 0 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Atencion, unidad devuelta por sobrecapacidad"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
f_plat = 255;
contador = contador + 1;             
} 
LEDS_stock(f_plat);
}

void platano_salida(){//registro de salida de palets de plátanos
//variable a contabiliza las existencias de palets
//variable b empleada como contador de líneas impresas por pantalla
f_plat = f_plat - 1;
contador = contador + 1;
if ( f_plat == 255 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Error en el sistema detectado, incongruencia salida/entrada"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
f_plat = 0;                    
} else {
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Platano"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("1"));                                                                                                    
Serial.print(F("\t"));
Serial.print(F("||"));                                                                                                    
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_plat);                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
}
LEDS_stock(f_plat);                         
}

void tomate_entrada(){//registro de entrada de palets de tomates, en caso de completar el almacén 
//con 255 palets las unidades entrantes serán devueltas automáticamente
//variable a contabiliza las existencias de palets
//variable b contabiliza las devoluciones
//variable c empleada como contador de líneas impresas por pantalla
f_tomat = f_tomat + 1;
contador = contador + 1;
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Tomates"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("1"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_tomat);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));
if ( f_tomat == 255 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Atencion, almacen al limite de capacidad"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));            
}
if ( f_tomat == 0 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Atencion, unidad devuelta por sobrecapacidad"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
f_tomat = 255;
devol = devol + 1;             
}                               
LEDS_stock(f_tomat);
}

void tomate_salida(){ //registro de salida de tomates
//variable a contabiliza las existencias de palets
//variable b empleada como contador de líneas impresas por pantalla
f_tomat = f_tomat - 1;
contador = contador + 1;
if ( f_tomat == 255 ){
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Error en el sistema detectado, incongruencia salida/entrada"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
f_tomat = 0;             
} else {
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("Tomates"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));                                                                                                    
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));                                                                                                    
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("1"));                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(f_tomat);                                                    
Serial.print(F("\t"));
Serial.print(F("\t")); 
Serial.println(F("////"));                                                
}
LEDS_stock(f_tomat);                           
}

void turno(){ //control de inicio/final de turno
//variable a empleada como contador de estado de turno
//variable b empleada como contador de líneas impresas por pantalla
contador = contador + 1;
contador_2 = contador_2 + 1;
if ((contador_2 % 2) == 1 ){
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                         
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Comienzo turno"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                         
} else {
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                         
Serial.print(F("////"));                                                  
Serial.print(F("\t"));
hora();                                                  
Serial.print(F("\t"));
Serial.print(F("||"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Final turno"));                                                  
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.println(F("////"));
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                         
}
}

void devolucion(){//registro de productos a devolver debido a no mantener las condiciones óptimas de transporte
//variable devol contabiliza el número total de devoluciones 
//variable contador empleada como contador de líneas impresas por pantalla
devol = devol + 1;
contador = contador + 1;
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Devolucion"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(devol);
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));                                
}
void hora(){ //procedimiento de impresión de fecha
Serial.print(year());
Serial.print(F("/"));
Serial.print(month());
Serial.print(F("/"));
Serial.print(day());
Serial.print(F(" "));
Serial.print(hour());
Serial.print(F(":"));
Serial.print(minute());
Serial.print(F(":"));
Serial.print(second());
}
void LEDS_stock(byte a){//control de stocks mediante sistema de semáforo, punto de pedido fijado en 3 palets de cada producto,
//en caso de que sea superior un LED verde lucirá, si es igual a 3 palets las unidades en el almacén 
//lucirá un LED amarillo indicando que se debe realizar un pedido y si el stock baja de 3 palets lucirá 
//un LED rojo
//variable a toma el valor de las existencias finales de palets de un producto
digitalWrite(verde,LOW);
digitalWrite(amarillo,LOW);
digitalWrite(rojo,LOW);                    
if (a > 3){
digitalWrite(verde,HIGH);
delay(200);
digitalWrite(verde,LOW);
delay(200);
digitalWrite(verde,HIGH);
delay(200);
digitalWrite(verde,LOW);
delay(200);
digitalWrite(verde,HIGH);
delay(200);
digitalWrite(verde,LOW);
}
if (a == 3) {
digitalWrite(amarillo,HIGH);
delay(200);
digitalWrite(amarillo,LOW);
delay(200);
digitalWrite(amarillo,HIGH);
delay(200);
digitalWrite(amarillo,LOW);
delay(200);
digitalWrite(amarillo,HIGH);
delay(200);
digitalWrite(amarillo,LOW);                 
}
if (a < 3){
digitalWrite(rojo,HIGH);
delay(200);
digitalWrite(rojo,LOW);
delay(200);
digitalWrite(rojo,HIGH);
delay(200);
digitalWrite(rojo,LOW);
delay(200);
digitalWrite(rojo,HIGH);
delay(200);
digitalWrite(rojo,LOW);                       
}
digitalWrite(verde,LOW);
digitalWrite(amarillo,LOW);
digitalWrite(rojo,LOW);                
}

void test_LEDS(byte a, byte b, byte c){ //prueba inicial de los LEDs del semáforo
//variable a identifica el pin del LED verde
//variable b identifica el pin del LED amarillo
//variable c identifica el pin del LED rojo
digitalWrite(a,LOW);
digitalWrite(b,LOW);
digitalWrite(c,LOW); 
digitalWrite(a,HIGH);
delay(750);
digitalWrite(b,HIGH);
delay(750);
digitalWrite(c,HIGH);
delay(750);
digitalWrite(a,LOW);
digitalWrite(b,LOW);
digitalWrite(c,LOW);    
delay(200);  
digitalWrite(a,HIGH);
digitalWrite(b,HIGH);
digitalWrite(c,HIGH);
delay(100);
digitalWrite(a,LOW);
digitalWrite(b,LOW);
digitalWrite(c,LOW);
delay(100);
digitalWrite(a,HIGH);
digitalWrite(b,HIGH);
digitalWrite(c,HIGH);
delay(100);
digitalWrite(a,LOW);
digitalWrite(b,LOW);
digitalWrite(c,LOW);          
}

void resumen(byte a, byte b, byte c, byte d, byte e, byte f, byte g){//procedimiento que muestra por pantalla un resumen de los registros,
//imprimiendo por pantalla existencias totales de palets de sandías, 
//plátanos, tomates y devoluciones
//variable a toma el valor de las existencias finales de sandías
//variable b toma el valor de las existencias finales de plátanos
//variable c toma el valor de las existencias finales de tomates
//variable d toma el valor de las devoluciones totales
//variable e empleada como contador de líneas impresas por pantalla
//variable f toma el valor del pulsador para iniciar el procedimiento
//variable g identifica el pin del pulsador
f = digitalRead(g);
if (f == HIGH) {
e = e + 3;
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Sandias"));
Serial.print(F("\t"));
Serial.print(F("\t"));                                                 
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(a);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Platanos"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(b);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Tomates"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(c);
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));
Serial.print(F("////"));
Serial.print(F("\t"));
hora();
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Devolucion"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(d);
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("\t"));   
Serial.println(F("////"));       
}      
}
void loop () { //código principal del programa
byte i = 0;
byte val = 0;
byte code[6];
byte checksum = 0;
byte bytesread = 0;
byte tempbyte = 0;
resumen(f_sand,f_plat,f_tomat,devol,contador,boton,pulsador);
if ((contador % 10) == 0 ){ 
delay(500);                  
Serial.println();
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                         
Serial.print(F("////"));   
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("Hora"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Producto"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Entradas"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Salidas"));
Serial.print(F("\t"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Devoluciones"));
Serial.print(F("\t"));
Serial.print(F("||"));
Serial.print(F("\t"));
Serial.print(F("Existencias finales"));
Serial.print(F("\t"));
Serial.println(F("////"));                                                                                                                
Serial.println(F("--------------------------------------------------------------------------------------------------------------------------------------------------------------------"));                         
delay(500);
contador = contador + 1;
}
if(Serial.available() > 0) {//operadores lógicos para la lectura de la tarjeta identificadora
//algoritmo obtenido del proyecto de Jonathan Oxer's en http://www.practicalarduino.com/projects/medium/rfid-access-control
if((val = Serial.read()) == 2) {                   
bytesread = 0; 
while (bytesread < 12) {                        
if( Serial.available() > 0) { 
val = Serial.read();
if((val == 0x0D)||(val == 0x0A)||(val == 0x03)||(val == 0x02)) {  
                          break;                                   
                          }
if ((val >= '0') && (val <= '9')) {
val = val - '0';
} else if ((val >= 'A') && (val <= 'F')) {
                                       val = 10 + val - 'A';
                                       }
if (bytesread & 1 == 1) {
code[bytesread >> 1] = (val | (tempbyte << 4));
if (bytesread >> 1 != 5) {              
              checksum ^= code[bytesread >> 1];       
             };
} else {
tempbyte = val;                           
};  

bytesread++;                               
} 
} 
if (bytesread == 12) {//operador lógico para la llamada de los procedimientos
//en función de la tarjeta detectada               
for (i=0; i<5; i++) {
chekksum = code[5];
} 
if (chekksum == 153) {
sandia_entrada();
}  
if (chekksum == 56) {
platano_entrada();
}
if (chekksum == 81) {
tomate_entrada();
}
if (chekksum == 162){
devolucion();
}
if (chekksum == 104) {
sandia_salida();
}
if (chekksum == 67) {
platano_salida();
}
if (chekksum == 215) {
tomate_salida();
}
if (chekksum == 123) {
turno();
}
if (chekksum == 83) {
camion_1();  
n_1 = n_1 + 1;                    
}
}
if (chekksum == 63) {
camion_2();
n_2 = n_2 + 1;
}
if (chekksum == 30) {
camion_3();
n_3 = n_3 + 1;
}
}
}
bytesread = 0;
}
