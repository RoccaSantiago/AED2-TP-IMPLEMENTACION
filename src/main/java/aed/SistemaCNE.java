package aed;

public class SistemaCNE {
    // Completar atributos privados

    private String[] nombresPartidos;
    private String[] nombresDistritos;
    private int[] diputadosDeDistrito;
    private int[] rangoMesasDistritos;
    private int[] votosPresidenciales;
    private int[][] votosDiputados;

    private int[] votosTotalesDiputados; // Indice es el distrito
    private int votosTotalesPresidente;

    private HeapSistema[] heapDhondt;
    private int[][] resultadosDhondt;
    private boolean[] hayResultadosDhondt;

    private int[] hayBallotageIndice;

    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }

    // Esto ya no es asi
    // Con el doble for tenemos la complejidad O(P*D)
    //La complejidad de SistemnaCNE es O(P+D) que pertene a O(P*D), debido a que primero incializamos los arrays que cuesta O(1), para luego realizar dos ciclos, no anidados, el cual el primero asigna los nombres de partidos que cuesta O(P) 
    // y despues el ciclo que inserta todo lo relacionado a los diputados y los dristictos; que como cota de ciclo tiene a la longitud D luego cuesta O(D).
    // Por lo tanto la suma de estos resultaria en O(D+P) y este pertenece a O(P*D) ya que este ultimo crece mucho mas rapido que el crecimiento lineal que tiene O(P+D).

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
         
        //inicializacion de variables
        
        this.nombresPartidos = new String[nombresPartidos.length];
        this.nombresDistritos = new String[nombresDistritos.length];
        this.diputadosDeDistrito = new int[diputadosPorDistrito.length];
        this.rangoMesasDistritos = new int[ultimasMesasDistritos.length];
        
        this.votosPresidenciales = new int[nombresPartidos.length];
        this.votosDiputados = new int[nombresDistritos.length][nombresPartidos.length];

        this.votosTotalesDiputados = new int[nombresDistritos.length];
        this.votosTotalesPresidente = 0;

        this.heapDhondt = new HeapSistema[nombresDistritos.length];
        this.resultadosDhondt = new int[nombresDistritos.length][nombresPartidos.length];

        this.hayResultadosDhondt = new boolean[nombresDistritos.length];

        this.hayBallotageIndice = new int[2];

        // input de Partidos

        for (int i = 0; i < nombresPartidos.length; i++) {
            this.nombresPartidos[i] = nombresPartidos[i];
        }

        // input de Distritos

        for (int i = 0; i < nombresDistritos.length; i++) {
            this.nombresDistritos[i] = nombresDistritos[i];
            this.diputadosDeDistrito[i] = diputadosPorDistrito[i];
            this.rangoMesasDistritos[i] = ultimasMesasDistritos[i];
        }
    }

    // Es O(1) ya que nombresPartidos es un array el cual tiene reservado en memoria todos los id. Por lo que acceder a cualquier id cuesta O(1).

    public String nombrePartido(int idPartido) {
        return nombresPartidos[idPartido];
    }


    // Es O(1) ya que nombresDistrito es un array el cual tiene reservado en memoria todos los id. Por lo que acceder a cualquier id cuesta O(1).

    public String nombreDistrito(int idDistrito) {
        return nombresDistritos[idDistrito];
    }

    // Es O(1) ya que diputadosEnDisputa es un array el cual tiene reservado en memoria todos los id. Por lo que acceder a cualquier id cuesta O(1).

    public int diputadosEnDisputa(int idDistrito) {
        return diputadosDeDistrito[idDistrito];
    }
    
    
    //hecho @maximilianofisz
    // La complejidad de esta auxiliar es O(Log(D)) ya que se recorre una secuendai de tamaño D utilizando una busqueda
    // binaria por lo que la cantidad de elementos a revisar disminuye logaritmicamente

    
    public int obtenerIdDistrito(int idMesa) {
        int high = rangoMesasDistritos.length;
        int low = 0;
        int actual = 0;
        while(low != high) {
            actual = (high + low) / 2;

            if(rangoMesasDistritos[actual] > idMesa) {
                if(actual == 0) {
                    return actual;
                }
                else {
                    if(rangoMesasDistritos[actual - 1] <= idMesa) {
                        break;
                    }
                    if(idMesa < rangoMesasDistritos[actual]) {
                        high = actual;
                    }

                }
            }
            else {
                low = actual;
            }
        }
        return actual;
    }

    //hecho @maximilianofisz
    
    // La complejidad de esta funcion es O(1) * complejidad de la auxiliar
    // O(1) * O(log(D)) = O(log(D))

    public String distritoDeMesa(int idMesa) {
       return nombresDistritos[obtenerIdDistrito(idMesa)];
    }

    //Al hacer la asignacion de IdDis usamos la funcion obtenerIdDistrito en la cual, como dijimos anteriormente, posee complejidad O(Log D). Para luego realizar un ciclo que tiene como cota P, ya que registramos los votos para todos los iD de los partidos.
    // Por lo tanto obtenemos O(Log D + P)

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        
        // Consigo el Id del districto.
        int idDis = obtenerIdDistrito(idMesa);
        
        // Registra votos presidenciales
        for (int idPartido=0; idPartido<actaMesa.length; idPartido++){
            votosDiputados[idDis][idPartido] += actaMesa[idPartido].diputados;
            votosPresidenciales[idPartido] += actaMesa[idPartido].presidente;

            votosTotalesDiputados[idDis] += actaMesa[idPartido].diputados;
            votosTotalesPresidente += actaMesa[idPartido].presidente;
        }

        hayBallotageIndice = maximos(votosPresidenciales);
        heapDhondt[idDis] = new HeapSistema(votosDiputados[idDis]);
    }

    private int[] maximos(int[] array) {
        int[] res = new int[2];
        int max1 = 0;
        int max2 = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max1) {
                res[1] = res[0];
                res[0] = i;
                max2 = max1;
                max1 = array[i];
            }
            else if (array[i] > max2 && array[i] < max1) {
                res[1] = i;
                max2 = array[i];
            }
        }
        return res;
    }

    // hecho @RoccaSantiago
    // Es O(1) ya que votosDiputados es un array el cual tiene reservado en memoria todos los id. Por lo que acceder a cualquier id cuesta O(1).

    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];  
    }

    // Es O(1) ya que votosDiputados es un array de arrays el cual tiene reservado en memoria todos los id de districtos y pasrtidos. Por lo que acceder a cualquier id cuesta O(1).
    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados[idDistrito][idPartido];
    }
    
    public int[] resultadosDiputados(int idDistrito) {

        if (!hayResultadosDhondt[idDistrito]){

            int[] res = new int[this.nombresPartidos.length];
            double umbral = votosTotalesDiputados[idDistrito] * 0.03;

            for (int i = 0; i < this.diputadosDeDistrito[idDistrito]; i++) {
                int idGanador = heapDhondt[idDistrito].indiceMaximo();
                int valorGanadorOriginal = heapDhondt[idDistrito].valorOriginalMaximo();
                int valorGanador = heapDhondt[idDistrito].extraerMaximo();

                if (res[idGanador] == 0) {
                    if (valorGanador > umbral && idGanador != nombresPartidos.length - 1) {
                        res[idGanador] += 1;
                        heapDhondt[idDistrito].agregar(valorGanadorOriginal/(res[idGanador] + 1), idGanador, valorGanadorOriginal);
                    }
                    else {
                        i --;
                        heapDhondt[idDistrito].agregar(-1, idGanador, valorGanadorOriginal);
                    }
                }

                else {
                    res[idGanador] += 1;
                    heapDhondt[idDistrito].agregar(valorGanadorOriginal/(res[idGanador] + 1), idGanador, valorGanadorOriginal);
                }
            }
            resultadosDhondt[idDistrito] = res;
            hayResultadosDhondt[idDistrito] = true;
            return res;
        }

        else {
            int[] res = resultadosDhondt[idDistrito];
            return res;
        }

    }

    public boolean hayBallotage() {
        int votosTotales = votosTotalesPresidente;
        int max1 = votosPresidenciales[hayBallotageIndice[0]];
        int max2 = votosPresidenciales[hayBallotageIndice[1]];
        float porcentajeMax1 = (max1 * 100 / votosTotales);
        float porcentajeMax2 = (max2 * 100 / votosTotales);

        if (porcentajeMax1 >= 45){
            return false;
        }
        else if (porcentajeMax1 >= 40 && (porcentajeMax1 - porcentajeMax2) > 10){
            return false;
        }
        else {
            return true;
        }
    }


    private class HeapSistema {
        private int[] heap;
        private int[] indices;
        private int[] valoresOriginales;
        private int size;
    
        public HeapSistema(int[] array) {
            size = array.length;
            heap = new int[size];
            for (int i = 0; i < size; i++) {
                heap[i] = array[i];
            }
            indices = new int[size];
            valoresOriginales = new int[size];
            
            for (int i = 0; i < size; i++) {
                indices[i] = i;
                valoresOriginales[i] = array[i];
            }
    
            for (int i = (size - 1)/ 2; i >= 0; i--) {
                heapifyBajar(i);
            }
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

            // Cambio en valoresOriginales
            int aux3 = valoresOriginales[i];
            valoresOriginales[i] = valoresOriginales[j];
            valoresOriginales[j] = aux3;

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
    
        public void agregar(int valor, int indice, int valorOriginal) {
            heap[size] = valor; // Agrega fuera del tamaño del array pero no de la capacidad del array
            indices[size] = indice; //ASDFasfdfe
            valoresOriginales[size] = valorOriginal;
            size ++;
            heapifySubir(size - 1);
        }

        public int indiceMaximo() {
            return indices[0];
        }

        public int valorOriginalMaximo() {
            return valoresOriginales[0];
        }
    
        public int extraerMaximo() {
            int valorMaximo = heap[0];
            heap[0] = heap[size - 1];
            indices[0] = indices[size - 1];
            valoresOriginales[0] = valoresOriginales[size - 1];
            size --;
            heapifyBajar(0);
            return valorMaximo;
        }
    }
}

