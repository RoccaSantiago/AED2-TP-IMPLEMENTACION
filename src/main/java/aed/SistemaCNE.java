package aed;

public class SistemaCNE {

    // D es igual a la cantidad de distritos y P igual a la cantidad de partidos.

    // El invariante de representacion  del modulo  es la sumatoria de las siguientes condiciones:
    private String[] nombresPartidos;  // La longitud de nombresPartidos es P inclusive, los valores no pueden ser nulos y no hay repetidos.
    
    private String[] nombresDistritos; // La longitud de nombresPartidos es D inclusive, los valores no pueden ser nulos y no hay repetidos.
    
    private int[] diputadosDeDistrito; // La longitud es igual a D, todo los elementos son mayores o iguales 0.
    
    private int[] rangoMesasDistritos; // No hay repetidos, estan en orden ascendente y son todos valores mayores a 0.
    
    private int[] votosPresidenciales; // Todos los elementos son mayores o iguales a 0 y su longitud es P. 
                                       // Cada índice es el total actual de votos de un partido a nivel nacional que se modifica con la operación registrar mesas
    
    private int[][] votosDiputados; // La longitud es igual D, cada elemento tiene longitud P y cada subelemento de un elemento es mayor o igual a 0.
                                    // Cada elemento corresponde a un distrito y cada subelemento corresponde a la sumatoria actual de votos de diputados de un partido del elemento.

    private int[] votosTotalesDiputados; // Posee longitud D y cada elemento es la sumatoria de todos los valores en cada elemento de votosDiputados, tiene que ser mayor o igual a 0.
    
    private int votosTotalesPresidente; // Es la sumatoria de todos los elementos de votosPresidenciales. Es mayor o igual a 0.


    private HeapSistema[] heapDhondt; // Cada elemento cumple el invariante de HeapSistema o es un elemento null, tiene longitud D. Implementado al final.
    
    private int[][] resultadosDhondt; // La longitud es igual a D, cada elemento tiene longitud P y cada subelemento es mayor o igual a 0.
    
    private boolean[] hayResultadosDhondt; // La longitud es igual a D
                                           // Para cada 0 <= i < longitud de hayResultadosDhondt se cumple que hayResultadosDhondt[i] == True si y solo si existe algun elemento en resultadosDhondt[i] que sea distinto a 0.
    
    private int[] hayBallotageIndice; // La longitud es igual a 2
                                      // hayBallotageIndice[0] es el indice del elemento maximo en votosPrecidenciales y hayBallotageIndice[1] es el indice del segundo elemento maximo en votosPrecidenciales, o ambos son 0.

    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }


    // La complejidad de SistemnaCNE es O(P*D) ya que al incializar las variables poseemos operacion u O(P) u O(D)
    // hasta resultadosDhondt que es un matriz que al reservar memoria, su complejidad sera de O(P*D).
    // Luego aplicamos un for con complejidad O(P) y luego uno con O(D). La complejidad mas alta termina siendo O(P*D).
    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
         
        //inicializacion de variables
        this.nombresPartidos = new String[nombresPartidos.length];
        this.nombresDistritos = new String[nombresDistritos.length];
        this.diputadosDeDistrito = new int[diputadosPorDistrito.length];
        this.rangoMesasDistritos = new int[nombresDistritos.length];
        
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
    
    
    // La complejidad de esta auxiliar es O(Log(D)) ya que se recorre una secuencia de tamaño D utilizando una busqueda
    // binaria por lo que la cantidad de elementos a revisar disminuye logaritmicamente
    public int obtenerIdDistrito(int idMesa) {
        int high = rangoMesasDistritos.length;
        int low = 0;
        int actual = 0;

        //Realiza busqueda binaria para hallar el id del districto.
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

    // La complejidad de esta funcion es O(1) * complejidad de la auxiliar
    // O(1) * O(log(D)) = O(log(D))
    public String distritoDeMesa(int idMesa) {
       return nombresDistritos[obtenerIdDistrito(idMesa)];
    }

    // Al hacer la asignacion de Ids usamos la funcion obtenerIdDistrito en la cual, como dijimos anteriormente, posee complejidad O(Log D).
    // Para luego realizar un ciclo que tiene como cota P, ya que registramos los votos para todos los Ids de los partidos.
    // La función maximos tiene complejidad O(P) y la creación del heap a través del arreglo tiene complejidad O(P) por utilizar algoritmo de Floyd.
    // Por lo tanto obtenemos O(Log D + P).
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

        //Cada vez que se registra una mesa nueva actualizan los valores de hayBallotageIndice y del healDhondt del distrito
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

    // Es O(1) ya que votosDiputados es un array el cual tiene reservado en memoria todos los id. Por lo que acceder a cualquier id cuesta O(1).
    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];  
    }

    // Es O(1) ya que votosDiputados es un array de arrays el cual tiene reservado en memoria todos los id de distritos y partidos. Por lo que acceder a cualquier id cuesta O(1).
    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados[idDistrito][idPartido];
    }
    
    // La complejidad de esta operación es O(Dd*log(P)) ya que tenemos que iterar Dd, la cantidad de bancas de un distrito y por cada una de 
    // ellas agregar al heap el valor asociado al calculo de Dhont que tiene complejidad O(log(P)). Entonces nos quda complejidad O(Dd*log(P)).
    public int[] resultadosDiputados(int idDistrito) {

        // Verifica si la información del Dhondt ya fue calculada, para entregar la informacion guardada o calcularla.
        if (!hayResultadosDhondt[idDistrito]){

            int[] res = new int[this.nombresPartidos.length];
            double umbral = votosTotalesDiputados[idDistrito] * 0.03;

            // Entra en un ciclo que entrega las bancas a los partidos según corresponda
            for (int i = 0; i < this.diputadosDeDistrito[idDistrito]; i++) {
                int idGanador = heapDhondt[idDistrito].indiceMaximo();
                int valorGanadorOriginal = heapDhondt[idDistrito].valorOriginalMaximo();
                int valorGanador = heapDhondt[idDistrito].extraerMaximo();

                // Si no se le han entregado bancas al partido que ganó comprueba que el partido pase el umbral y no represente los votos en blanco
                if (res[idGanador] == 0) {

                    //Si cumple le asigna una banca
                    if (valorGanador > umbral && idGanador != nombresPartidos.length - 1) {
                        res[idGanador] += 1;
                        heapDhondt[idDistrito].agregar(valorGanadorOriginal/(res[idGanador] + 1), idGanador, valorGanadorOriginal);
                    }
                    //Si no cumple lo elimina de la cuenta y reinicia el ciclo para darle la banca al siguiente
                    else {
                        i --;
                        heapDhondt[idDistrito].agregar(-1, idGanador, valorGanadorOriginal);
                    }
                }
                // Si ya se le asignó bancas anteriormente se sabe que pasa el umbral y no son los votos en blanco por lo que se le entrega una banca directamente
                else {
                    res[idGanador] += 1;
                    heapDhondt[idDistrito].agregar(valorGanadorOriginal/(res[idGanador] + 1), idGanador, valorGanadorOriginal);
                }
            }
            // Guarda el resultado del Dhondt y lo entrega
            resultadosDhondt[idDistrito] = res;
            hayResultadosDhondt[idDistrito] = true;
            return res;
        }
        // Entrega el resultado directamente desde memoria
        else {
            int[] res = resultadosDhondt[idDistrito];
            return res;
        }

    }

    // Es O(1) ya solo se realizan accesos a arrays que ya estan en memorias y otras operaciones O(1).
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

    // Implementacion de Heap utilizado en el SistemaCNE
    private class HeapSistema {
        private int[] heap; //Es una representación de un Arbol Heap que cumple con:
                            //El nodo con mayor prioridad se encuentra en el indice 0
                            //Es un arbol perfectamente balanceado
                            //La prioridad de cada nodo es mayor o igual a las de sus hijos, si los tiene
                            //Todo subarbol es a su vez un heap
                            //Es izquierdista
        private int[] indices; //Tiene la misma longitud que heap, todos sus valores son mayores o iguales a 0 y menores a su longitud.
        private int[] valoresOriginales; //Tiene la misma longitud que heap, todos sus valores son mayores o iguales a 0
        private int size; //Es mayor o igual a 0
    
        // Utiliza algoritmo de Floyd para pasar de un array a un heap valido
        public HeapSistema(int[] array) {
            size = array.length;
            heap = new int[size];
            indices = new int[size];
            valoresOriginales = new int[size];
            for (int i = 0; i < size; i++) {
                heap[i] = array[i];
                indices[i] = i;
                valoresOriginales[i] = array[i];
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

            // Cambio en valoresOriginales
            int aux3 = valoresOriginales[i];
            valoresOriginales[i] = valoresOriginales[j];
            valoresOriginales[j] = aux3;

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
        
        public void agregar(int valor, int indice, int valorOriginal) {
            heap[size] = valor; // Agrega fuera del tamaño del array pero no de la capacidad del array
            indices[size] = indice;
            valoresOriginales[size] = valorOriginal;
            size ++;
            if(valor > valorOriginalMaximo()) {
                heapifySubir(size - 1);
            }
            
        }

        public int indiceMaximo() {
            return indices[0];
        }

        // Entrega el valro del máximo sin eliminar
        public int valorOriginalMaximo() {
            return valoresOriginales[0];
        }
        
        // Popea el máximo del heap
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

