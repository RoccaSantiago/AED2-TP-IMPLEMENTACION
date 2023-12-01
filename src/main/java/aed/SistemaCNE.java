package aed;


public class SistemaCNE {

    

    // El invariante de representacion  del modulo  es el conjunto de las siguientes condiciones:
    private String[] nombresPartidos;  // La longitud de nombresPartidos la definimos como P, P es mayor igual a 2, los valores no pueden ser nulos y no hay repetidos.
    
    private String[] nombresDistritos; // La longitud de nombresDartidos la definimos como D, D es mayor igual a 1, los valores no pueden ser nulos y no hay repetidos.
    
    private int[] diputadosDeDistrito; // La longitud es igual a D, todo los elementos son mayores o iguales 0.
    
    private int[] rangoMesasDistritos; // No hay repetidos, estan en orden ascendente y son todos valores mayores a 0.
    
    private int[] votosPresidenciales; // Todos los elementos son mayores o iguales a 0 y su longitud es P. 
    
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
        VotosPartido(int presidente, int diputados)
        {this.presidente = presidente; this.diputados = diputados;} // O(1)
        public int votosPresidente(){return presidente;}// O(1)
        public int votosDiputados(){return diputados;}// O(1)
    }

    // La complejidad de SistemnaCNE es O(P*D) ya que al incializar las variables poseemos operacion u O(P) u O(D)
    // hasta resultadosDhondt que es un matriz que al reservar memoria, su complejidad sera de O(P*D).
    // Luego aplicamos un for con complejidad O(P) y luego uno con O(D). La complejidad mas alta termina siendo O(P*D).
    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
         
        //inicializacion de variables
        this.nombresPartidos = new String[nombresPartidos.length]; // O(P)
        this.nombresDistritos = new String[nombresDistritos.length]; // O(D)
        this.diputadosDeDistrito = new int[diputadosPorDistrito.length]; // O(D)
        this.rangoMesasDistritos = new int[nombresDistritos.length]; // O(P)
        
        this.votosPresidenciales = new int[nombresPartidos.length]; // O(P)
        this.votosDiputados = new int[nombresDistritos.length][nombresPartidos.length]; // O(P*D)

        this.votosTotalesDiputados = new int[nombresDistritos.length]; // O(D)
        this.votosTotalesPresidente = 0;

        this.heapDhondt = new HeapSistema[nombresDistritos.length]; // O(D)
        this.resultadosDhondt = new int[nombresDistritos.length][nombresPartidos.length]; // O(P*D)

        this.hayResultadosDhondt = new boolean[nombresDistritos.length]; // O(D)

        this.hayBallotageIndice = new int[2];

        // input de Partidos
        for (int i = 0; i < nombresPartidos.length; i++) { // O(P)
            this.nombresPartidos[i] = nombresPartidos[i];
        }

        // input de Distritos
        for (int i = 0; i < nombresDistritos.length; i++) { // O(D)
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

    // La complejidad de esta funcion es O(1) sumado a la complejidad de la auxiliar
    // O(1) + O(log(D)) = O(log(D)) ya que O(1) pertenece a O(log(D))
    public String distritoDeMesa(int idMesa) {
        int id = obtenerIdDistrito(idMesa); // O(log(D))
        return nombresDistritos[id]; // O(1)
    }

    // Al hacer la asignacion de Ids usamos la funcion obtenerIdDistrito en la cual, como dijimos anteriormente, posee complejidad O(Log D).
    // Para luego realizar un ciclo que tiene como cota P, ya que registramos los votos para todos los Ids de los partidos.
    // La función maximos tiene complejidad O(P) y la creación del heap a través del arreglo tiene complejidad O(P) por utilizar algoritmo de Floyd.
    // Por lo tanto obtenemos O(Log D + P).
    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        
        // Consigo el Id del districto.
        int idDis = obtenerIdDistrito(idMesa); // O(log(D))
        
        // Registra votos presidenciales 
        for (int idPartido=0; idPartido<actaMesa.length; idPartido++){ // O(P)
            votosDiputados[idDis][idPartido] += actaMesa[idPartido].diputados;
            votosPresidenciales[idPartido] += actaMesa[idPartido].presidente;

            votosTotalesDiputados[idDis] += actaMesa[idPartido].diputados;
            votosTotalesPresidente += actaMesa[idPartido].presidente;
        }

        //Cada vez que se registra una mesa nueva actualizan los valores de hayBallotageIndice y del heapDhondt del distrito
        hayBallotageIndice = maximos(votosPresidenciales); //O(P)

        double umbral = votosTotalesDiputados[idDis] * 0.03;
        
        int[] partidosSobreUmbral = new int[nombresPartidos.length]; //O(P)

        for (int i = 0; i < nombresPartidos.length; i++){ //O(P)
            if (votosDiputados[idDis][i] > umbral) {
                partidosSobreUmbral[i] = votosDiputados[idDis][i];
            }
            else {
                partidosSobreUmbral[i] = -1;
            }
        }

        partidosSobreUmbral[partidosSobreUmbral.length - 1] = -1;

        heapDhondt[idDis] = new HeapSistema(partidosSobreUmbral); //O(P)
    }

    private int[] maximos(int[] array) { // O(n) donde n es el tamaño del array, como solo se usa con array tamaño P entonces es O(P)
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

            int[] res = resultadosDhondt[idDistrito];

            // Entra en un ciclo que entrega las bancas a los partidos según corresponda
            for (int i = 0; i < this.diputadosDeDistrito[idDistrito]; i++) { // Dd veces log(P) entonces complejidad O(Dd*log(P))
                int idGanador = heapDhondt[idDistrito].indiceMaximo();
                int valorGanadorOriginal = votosDiputados[idDistrito][idGanador];
                heapDhondt[idDistrito].extraerMaximo(); //O(log(P))

                res[idGanador] += 1;
                heapDhondt[idDistrito].agregar(valorGanadorOriginal/(res[idGanador] + 1), idGanador); //O(log(P))

            }
            // Guarda el resultado del Dhondt y lo entrega
            hayResultadosDhondt[idDistrito] = true;
            return res;
        }
        // Entrega el resultado directamente desde memoria
        else {
            int[] res = resultadosDhondt[idDistrito];
            return res;
        }

    }

    // Es O(1), ya que solo se realizan accesos a arrays que ya estan en memorias y otras operaciones O(1).
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
}

