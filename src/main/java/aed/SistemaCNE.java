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

    private int[] mesasRegistradas;
    

    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }

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

    public String nombrePartido(int idPartido) {
        return nombresPartidos[idPartido];
    }

    public String nombreDistrito(int idDistrito) {
        return nombresDistritos[idDistrito];
    }

    public int diputadosEnDisputa(int idDistrito) {
        return diputadosDeDistrito[idDistrito];
    }

    //hecho @maximilianofisz
    public int obtenerIdDistrito(int idMesa) {
        int capacidadOriginal = rangoMesasDistritos.length;
        int mitadSegmento = capacidadOriginal / 2;
        while(true) {

            // El id esta en el primero
            if(mitadSegmento == 0 ) {
                return mitadSegmento;
            }

            // El id esta en el anterior
            if(idMesa >= rangoMesasDistritos[mitadSegmento - 1] && idMesa < rangoMesasDistritos[mitadSegmento]) {
                return mitadSegmento;
            }

            // El Id esta en el actual
            if(idMesa >= rangoMesasDistritos[mitadSegmento] && idMesa < rangoMesasDistritos[mitadSegmento + 1]) {
                return mitadSegmento+1;
            }

            // El Id esta en el ultimo
            if(idMesa >= rangoMesasDistritos[mitadSegmento + 1] && mitadSegmento + 1 == capacidadOriginal - 1) {
                return capacidadOriginal - 1;
            }

            // IdMesa es menor
            if(idMesa < rangoMesasDistritos[mitadSegmento]) {
                mitadSegmento = mitadSegmento / 2;
                continue;
            }

            // IdMesa es mayor
            if(idMesa >= rangoMesasDistritos[mitadSegmento]) {
                mitadSegmento = mitadSegmento + (mitadSegmento/2 + 1);
                continue;
            }
        }
    }

    //hecho @maximilianofisz
    public String distritoDeMesa(int idMesa) {
       return nombresDistritos[obtenerIdDistrito(idMesa)];
    }

    // hecho @RoccaSantiago
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
    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];  
    }

    // hecho @RoccaSantiago
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

