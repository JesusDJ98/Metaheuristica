# Algoritmos basados en Entornos y Trayectorias

**Greedy**
<p align="center">
	<img src="https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica1/Imagenes/Greedy.JPG" align="left" />
	Algoritmo voraz, basado en métodos constructivos, que devuelve una solución cercana a la óptima en un tiempo razonable, 
	partiendo de una solución inicial vacía y va añadiendo componentes hasta construir la solución (Heurística constructiva), 
	en este caso cogiendo las ciudades más cercanas a la actual, comenzando por la ciudad de índice 1.
</p>


<br>

**Búsqueda Aleatoria**
<p align="center">
	Algoritmo que elige de manera aleatoria la solución y se queda con la mejor solución 
	encontrada, por lo que su entorno es todo el espacio de búsqueda. Este algoritmo lo 
	estudiamos para ver los beneficios de los siguientes algoritmos, en concreto de las búsquedas 
	locales.
	<br>
	<img src="https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica1/Imagenes/Aleatoria.JPG" />
</p>



**Búsqueda Local**

<p align="center">
Metaheurística basada en trayectorias, partiendo de una solución inicial, van 
iterativamente tratando de reemplazar la solución actual, por una solución vecina que la 
mejore. Consideramos solución vecina, todas aquellas soluciones incluidas en el entorno de la 
solución actual, que están delimitadas por un operador de generación de soluciones, ya sea 
por vecindad o proximidad, por eso a esta metaheurística de búsqueda se le asocia el uso de 
estructuras de entorno.

Estos casos, se ha decidido que consideramos vecinos a aquellas soluciones que se 
diferencien de la actual, con solamente un intercambio, Opt-2.

**_Mejor Vecino_**

En este caso, genera completamente todo su entorno y selecciona la mejor solución 
vecina, y luego tiene dos opciones, ese mejor vecino supera a la solución actual o no, si la 
supera la actualizamos y seguimos y sino, terminamos, pues nos encontramos frente a un 
óptimo local. Al coger el mejor vecino posible de nuestro entorno, nos hace muy dependentes 
de la posición de partida, ya que vamos al óptimo local más cercano a nuestro punto de 
partida, y la probabilidad de que se el óptimo global, es pequeña (1/nº de óptimos).

<br>
<img src="https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica1/Imagenes/MejorVecino.JPG" />


	
**_Primer Mejor Vecino_**
	
Este algoritmo es muy parecido al anterior, lo único que cambia es que no coge el mejor vecino 
de su entorno de búsqueda, sino el primer vecino que mejore al actual, este cambio provoca que 
cambiemos de zona de búsqueda, por lo que no depende tanto de donde cae al inicio, pero sigue con el 
problema de los óptimo locales

<br>
<img src="https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica1/Imagenes/PrimerMejorVecino.JPG" />
</p>

**Enfriamiento Simulado**

<p align="center">
Metaheurística basada en trayectorias. Es un procedimiento de búsqueda global por 
entornos, probabilístico basado en la termodinámica. Este algoritmo permite coger soluciones 
peores que la actuales, para evitar el problema de los óptimos locales, según una variable 
denominada temperatura, que empieza siendo grande para diversificar la búsqueda(explorar) 
y se va reduciendo en cada iteración, para intensificar la búsqueda al final (explotación), esto 
nos aporta que evitemos bloqueos con los óptimos locales, y que controlemos el estudio de 
entornos peores, pues si aceptamos siempre soluciones peores, más que una solución sería
crear otro problema.

Este algoritmo funciona generando una cantidad de soluciones vecinas(L), y coge las 
soluciones que mejoren la solución actual o aquellas que superen el criterio de aceptación, en 
este caso es que un numero aleatorio no supere un valor que depende de la temperatura, que 
ira decreciendo en cada iteración siguiendo el esquema Cauchy. Al final devuelve la mejor 
solución estudiada.

<br>
<img src="https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica1/Imagenes/EnfriamientoSimulado.JPG" />

</p>


**Búsqueda Tabú**

<p align="center">
Metaheurística basada en trayectorias. Es un procedimiento de búsqueda por 
entornos, que además de permitir movimientos de empeoramiento, utiliza memoria 
adaptativa, corto y largo plazo, para restringir o guiar la búsqueda, y estrategias especiales de 
resolución de problemas, como lo son las técnicas de reinicialización.

La memoria a corto plazo, o más conocida como lista tabú, sirve para restrigir las zonas 
de búsqueda de forma inmediata, en cambio, la memoria a largo plazo, guía la búsqueda 
posteriori, estudiando zonas no visitadas. Las técnicas de reinicialización, permiten estudiar 
zonas distantes a la que nos encontramos para tener así un mayor conocimiento del problema.

El algoritmo implementado, estudia en cada iteración una cantidad fija de vecinos 
aleatorios(40), de los cuales nos quedamos con el mejor que no se encuentre en la lista tabú, y 
lo vamos explotando, añadiendo el movimiento a nuestra lista tabú, y también actualizamos 
nuestra memoria a largo plazo (guarda la frecuencia con lo que una ciudad es consecutiva a 
otra). Si esta solucion es mejor que nuestra mejor solución conocida, la actualizamos. Y si 
cumple el criterio designado para que se reinicialice el problema, reinicializamos, para esto 
tenemos 4 opciones, empezar desde una solucion aleatoria, desde la mejor solución conocida, 
o utilizando la memoria a largo plazo, y esta la utilizamos para diversificar (explorar) la 
búsqueda, cogiendo las menos frecuentes.

<br>
<img src="https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica1/Imagenes/Tabu.JPG" />

</p>
