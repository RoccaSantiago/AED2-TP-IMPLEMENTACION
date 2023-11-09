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

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
        
        //inicializacion de variables
        
        this.nombresPartidos = new String[nombresPartidos.length];
        this.nombresDistritos = new String[nombresDistritos.length];
        this.diputadosDeDistrito = new int[diputadosPorDistrito.length];
        this.rangoMesasDistritos = new int[ultimasMesasDistritos.length];

        // input de nombresPartidos

        for (int i = 0; i<nombresPartidos.length; i++){
            this.nombresPartidos[i] = nombresPartidos[i];
        }

        // input de nombresDistrictos, diputadosEnDisputa y rangoMesasDistritos

        for (int i = 0; i<nombresDistritos.length; i++){
            this.nombresDistritos[i] = nombresDistritos[i];
            this.diputadosDeDistrito[i] = diputadosPorDistrito[i];
            this.rangoMesasDistritos[i] = ultimasMesasDistritos[i];


            // Pensado con rangoMesasDistritos como matriz y no como array @Gonza
            //if (i > 0){
            //    this.rangoMesasDistritos[i][0] = ultimasMesasDistritos[i-1];
            //    this.rangoMesasDistritos[i][1] = ultimasMesasDistritos[i];
            //}
            //else {
            //    this.rangoMesasDistritos[0][0] = 0;
            //    this.rangoMesasDistritos[0][1] = ultimasMesasDistritos[0];
            //}
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

/*     public String distritoDeMesa(int idMesa) {
        int n = nombresDistritos.length/2;
        n = this.bbDistritodeMesa(idMesa, n);
        return nombreDistrito(n);

    } */

    /* private int bbDistritodeMesa(int idMesa, int n){ // Mi intento de busqueda binaria, no se si hace division entera el / porfa diganme si no lo hace @Gonza
        if (rangoMesasDistritos[n] > idMesa){

            if (n == 0){
                return n;
            }
            else if (rangoMesasDistritos[n-1] < idMesa){
                return n;
            }
            else {
            n = n - n / 2; // me complique con este facor, lo demas funciona bien
            return bbDistritodeMesa(idMesa, n);
            }
        }
        else if (rangoMesasDistritos[n] < idMesa){

            if (n == nombresDistritos.length - 1){
                return n;
            }
            else if (rangoMesasDistritos[n+1] > idMesa){
                return (n+1);
            }
            else {
            n = n + n/2; // este factor tambien
            return bbDistritodeMesa(idMesa, n);
            }
        }
        return n;
    } */

    public String distritoDeMesa(int idMesa) {
        int capacidadOriginal = rangoMesasDistritos.length;
        int mitadSegmento = capacidadOriginal / 2;
        while(true) {

            // El id esta en el primero
            if(mitadSegmento == 0 ) {
                return nombreDistrito(0);
            }

            // El id esta en el anterior
            if(idMesa >= rangoMesasDistritos[mitadSegmento - 1] && idMesa < rangoMesasDistritos[mitadSegmento]) {
                return nombreDistrito(mitadSegmento);
            }

            // El Id esta en el actual
            if(idMesa >= rangoMesasDistritos[mitadSegmento] && idMesa < rangoMesasDistritos[mitadSegmento + 1]) {
                return nombreDistrito(mitadSegmento + 1);
            }

            // El Id esta en el ultimo
            if(idMesa >= rangoMesasDistritos[mitadSegmento + 1] && mitadSegmento + 1 == capacidadOriginal - 1) {
                return nombreDistrito(capacidadOriginal - 1);
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

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int votosPresidenciales(int idPartido) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int[] resultadosDiputados(int idDistrito){
        throw new UnsupportedOperationException("No implementada aun");
    }

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

