package aed;

public class HeapSistema {
        private int[] heap; //Es una representación de un Arbol Heap que cumple con:
                            //El nodo con mayor prioridad se encuentra en el indice 0
                            //Es un arbol perfectamente balanceado
                            //La prioridad de cada nodo es mayor o igual a las de sus hijos, si los tiene
                            //Todo subarbol es a su vez un heap
                            //Es izquierdista
        private int[] indices; //Tiene la misma longitud que heap, todos sus valores son mayores o iguales a 0 y menores a su longitud.
        private int size; //Es mayor o igual a 0
    
        // Utiliza algoritmo de Floyd para pasar de un array a un heap valido
        public HeapSistema(int[] array) {
            size = array.length;
            heap = new int[size];
            for (int i = 0; i < size; i++) {
                heap[i] = array[i];
            }
            indices = new int[size];
            
            for (int i = 0; i < size; i++) {
                indices[i] = i;
            }
    
            for (int i = (size - 1)/ 2; i >= 0; i--) {
                heapifyBajar(i);
            }
        }

        // Tanto padre, izq y der pueden dar valores fuera del rango del array pero no es un problema por como están implementadas las demás funciones
        private int padre(int i) {
            return (i-1) / 2;
        }
    
        private int izq(int i) {
            return 2 * i + 1;
        }
    
        private int der(int i) {
            return 2 * i + 2;
        }
    
        private void cambiar(int i, int j) {
            // Cambio en heap
            int aux1 = heap[i];
            heap[i] = heap[j];
            heap[j] = aux1;
    
            // Cambio en indices
            int aux2 = indices[i];
            indices[i] = indices[j];
            indices[j] = aux2;
        }
    
        private void heapifySubir(int i) {
            int daddy = padre(i);
            // i > 0 porque izq(), padre() y der() a veces salen del rango del array, así acoto
            while (i > 0 && heap[i] > heap[daddy]) {
                cambiar(i, daddy);
                i = daddy;
                // Cuando i llega a 0 padre(i) = 0
                daddy = padre(i);
            }
        }
    
        private void heapifyBajar(int i) {
            int indiceMaximo = i;
            int izquierda = izq(i);
            int derecha = der(i);
    
            if (izquierda < size && heap[izquierda] > heap[indiceMaximo]) {
                indiceMaximo = izquierda;
            }
    
            if (derecha < size && heap[derecha] > heap[indiceMaximo]) { // Es un if y no un else if porque tambien quiero comparar el nodo izq con el der si es el caso
                indiceMaximo = derecha;
            }
    
            if (i != indiceMaximo) { // Entra acá solo si es que cambia de indice, si no se acaba la recursión
                cambiar(i, indiceMaximo);
                heapifyBajar(indiceMaximo); // El elemento va a intentar seguir bajando hasta que no pueda más
            }
        }
        
        public void agregar(int valor, int indice) {
            heap[size] = valor; // Agrega fuera del tamaño del array pero no de la capacidad del array
            indices[size] = indice;
            size ++;
            heapifySubir(size - 1);
        }

        public int indiceMaximo() {
            return indices[0];
        }
        
        // Popea el máximo del heap
        public int extraerMaximo() {
            int valorMaximo = heap[0];
            heap[0] = heap[size - 1];
            indices[0] = indices[size - 1];
            size --;
            heapifyBajar(0);
            return valorMaximo;
        }
    }