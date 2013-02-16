CLIENTE GRAFICO LINEA COMANDOS PARA LA PRUEBA TECNICA CALCULO MINIMA DISTANCIA ENTRE DOS CIUDADES 
-------------------------------------------------------------------------------------------------

Este repositorio contiene un cliente muy sencillo para probar la solución implementada en el repositorio distance-calculator.

Se trata de un cliente gráfico con las siguientes características:

   - Permite introducir una ciudad origen y destino
   - Llama a la implementación de distance-calculator y muestra por pantalla los resultados obtenidos.
   - Gestiona la excepción que se produce cuando un usuario introduce una ciudad que no se encuentra en nuestro mapa, mostrando por pantalla las ciudades disponibles actualmente.

La solución se ha implemtado en Java utilizando Swing.

INSTRUCCIONES PARA INSTALAR EL PROYECTO
---------------------------------------

1. Clonar el repositorio en un directorio local
2. Ejecutar los siguientes comandos de maven:
     mvn clean install
     mvn dependency:copy-dependencies
3. Ejecutar el siguiente comando:
     java -classpath classes:target/dependency/*:target/distance-calculator-client-1.0.jar org.ardlema.GUIUtil 

