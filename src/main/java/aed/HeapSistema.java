package aed;

public class HeapSistema {
    private int[] heap;
    private int[] indices;
    private int size;
    private int capacidad;

    public HeapSistema(int cap) {
        heap = new int[cap];
        indices = new int[cap];
        size = 0;
        capacidad = cap;
    }

    public HeapSistema(int[] array) {
        size = 0;
        capacidad = array.length;
        for (int i = 0; i < size; i++) {
            this.agregar(array[i], i);
        }
    }

    public int capacidad() {
        return capacidad;
    }

    private int padre(int i) { // Tanto padre, izq y der pueden dar valores fuera del rango del array pero no es un problema por como están implementadas las demas funciones
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
        while (i > 0 && heap[i] > heap[daddy]) { // i > 0 porque izq(), padre() y der() a veces salen del rango del array, asi acoto
            cambiar(i, daddy);
            i = daddy;
            daddy = padre(i);   // Cuando i llega a 0 padre(i) = 0
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
        indices[size] = indice; //ASDFasfdfe
        size ++;
        heapifySubir(size - 1);
    }

    public int verMaximo() {
        return heap[0];
    }

    public int indiceMaximo() {
        return indices[0];
    }

    public int extraerMaximo() {
        int valorMaximo = heap[0];
        heap[0] = heap[size - 1];
        size --;
        heapifyBajar(0);
        return valorMaximo;
    }
}