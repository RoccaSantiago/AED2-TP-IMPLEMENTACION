package aed;

public class SistemaCNE {
    // Completar atributos privados

    private String[] nombresPartidos;   
    private String[] nombresDistritos;
    private int[] diputadosDeDistrito; // Cambie el nombre de diputadosEnDisputa a diputadosDeDistrito @Gonza
    private int[] rangoMesasDistritos; // es distritosDeMesa por ahora

    // private AVL    DistrictosDeMesa;
    // private int[] DistrictosDeMesa;

    // private int[] bancasDeDistricto; PUede que sea el mismo que diputadosEnDisputa
    private int[] votosPresidenciales;
    private int[][] votosDiputados;

    // private heap hayBallotage; Despues lo ponemos cuando el heap este listo

    private int[] hayBallotage; // 3 elementos, [0] Maximo, [1] Segundo Maximo, [2] votos totales, tambien pude ser un vector @Gonza

    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }

    //La complejidad de SistemnaCNE es O(P+D) que pertene a O(P*D), debido a que primero incializamos los arrays que cuesta O(1), para luego realizar dos ciclos, no anidados, el cual el primero asigna los nombres de partidos que cuesta O(P) 
    // y despues el ciclo que inserta todo lo relacionado a los diputados y los dristictos; que como cota de ciclo tiene a la longitud D luego cuesta O(D).
    // Por lo tanto la suma de estos resultaria en O(D+P) y este pertenece a O(P*D) ya que este ultimo crece mucho mas rapido que el crecimiento lineal que tiene O(P+D).

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
         



        //inicializacion de variables
        
        this.nombresPartidos = new String[nombresPartidos.length];
        this.nombresDistritos = new String[nombresDistritos.length];
        this.diputadosDeDistrito = new int[diputadosPorDistrito.length];
        this.rangoMesasDistritos = new int[ultimasMesasDistritos.length];
        
        //hecho @RoccaSantiago
        this.votosPresidenciales = new int[nombresPartidos.length];
        this.votosDiputados = new int[nombresDistritos.length][nombresPartidos.length];

        // input de nombresPartidos

        for (int i = 0; i<nombresPartidos.length; i++){
            this.nombresPartidos[i] = nombresPartidos[i];
        }

        // input de nombresDistrictos, diputadosEnDisputa y rangoMesasDistritos

        for (int i = 0; i<nombresDistritos.length; i++){
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

    // hecho @RoccaSantiago

    //Al hacer la asignacion de IdDis usamos la funcion obtenerIdDistrito en la cual, como dijimos anteriormente, posee complejidad O(Log D). Para luego realizar un ciclo que tiene como cota P, ya que registramos los votos para todos los iD de los partidos.
    // Por lo tanto obtenemos O(Log D + P)
    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        
        // Consigo el Id del districto.
        int idDis = obtenerIdDistrito(idMesa);
        
        // Registra votos presidenciales
        
        for (int IdPartido=0; IdPartido<actaMesa.length; IdPartido++){
            votosDiputados[idDis][IdPartido] += actaMesa[IdPartido].diputados;
            votosPresidenciales[IdPartido] += actaMesa[IdPartido].presidente; 
        }

    }


    // hecho @RoccaSantiago
    // Es O(1) ya que votosDiputados es un array el cual tiene reservado en memoria todos los id. Por lo que acceder a cualquier id cuesta O(1).
    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];  
    }

    // hecho @RoccaSantiago
    // Es O(1) ya que votosDiputados es un array de arrays el cual tiene reservado en memoria todos los id de districtos y pasrtidos. Por lo que acceder a cualquier id cuesta O(1).
    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados[idDistrito][idPartido];
    }







    
    //FALTA HACER=========================================================================================================
    private int[] calcularDhont(int[] escrutinio, int bancas){
        int[] res = new int[nombresPartidos.length-1];

        int bancasAsignadas = 0;
        
       
        while (bancasAsignadas<=bancas) {
            
            while(true) {   
                    
                //PARTIDOS LOG(P)
            }


            
        }





        return res;
    }



    public int[] resultadosDiputados(int idDistrito){
        
        return calcularDhont(votosDiputados[idDistrito],diputadosEnDisputa(idDistrito));
    }

    //FALTA HACER=========================================================================================================

    public boolean hayBallotage(){ // Si quedamos con el heap despues se cambian las asignaciones solamente @Gonza
        int votosTotales = hayBallotage[2];
        int max1 = hayBallotage[0];
        int max2 = hayBallotage[1];
        float porcentajeMax1 = max1 / votosTotales * 100;
        float porcentajeMax2 = max2 / votosTotales * 100;

        if (porcentajeMax1 > 45){
            return false;
        }
        else if (porcentajeMax1 > 40 && (porcentajeMax1 - porcentajeMax2) > 10){
            return false;
        }
        else {
            return true;
        }
    }
}

