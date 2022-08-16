# Algoritmos heurísticos no constructivos. Genéticos.

**Algoritmo Genético Básico**

· *Modelo Estacionario*: Durante cada iteración se escogen dos padres de la población 
(diferentes mecanismos de muestreo) y se les aplican los operadores genéticos.
· Cruce: OX alternativo
· Reemplazo: torneo
· Ejecucion: Ejecutamos el algoritmo hasta que se ejecuta mil veces sin mejorar, utilizando un tamaño de 
población de 60 individuos, y un tamaño de sublista (S) de una octava parte de los genes de cada 
individuo, aunque muta con una probabilidad muy baja (5%). El valor de “S”, si es muy grande, genera 
que que haga saltos muy poco pronunciados, y si es pequeña genera saltos muy pronunciados.

![Genes N/5](https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica3/Imagenes/ConvergenciaGB_N5.JPG) ![Genes N/8](https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica3/Imagenes/ConvergenciaGB_N8.JPG)


**CHC**
· Reinicialización: Cogemos el mejor individuo, y luego rellenamos la población con un 35% de 
mutación a este individuo.
· Criterio de parada: 10 reinicializaciones.

![Convergencia](https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica3/Imagenes/CHC.JPG)


**Multimodal**(Clearing)

Los mismos parámetros que el genético básico, más los siguientes:
· Criterio de parada: 1000 clearing.
· Radio: 20% de genes.
· Nº individuos por nicho: 10% de población.

![Convergencia](https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica3/Imagenes/Multimodal.JPG) ![Clearing](https://github.com/JesusDJ98/Metaheuristica/tree/main/Practica3/Imagenes/Clearing.JPG)
