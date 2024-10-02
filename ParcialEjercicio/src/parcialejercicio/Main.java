package parcialejercicio;

//Importamos las Librerías Necesarias
import java.io.*;
import java.util.concurrent.*;

public class Main {
    
    public static final String DOCUMENTO = "Datos.csv";
    public static final String MUJERES = "Mujeres.csv";
    public static final String MENORES = "Menores.csv";
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        Future<?> tareaMujeres = executor.submit(() -> FuncionMujeres(DOCUMENTO, MUJERES));
        Future<?> tareaHombres = executor.submit(() -> FuncionHombreMenores(DOCUMENTO, MENORES));
        
        tareaHombres.get();
        tareaMujeres.get();
        
        Future<?> leerArchivos = executor.submit(() -> LeerDocumentosCreados(MUJERES,MENORES));
        leerArchivos.get();
        
        executor.shutdown();
    }
    
    public static void FuncionMujeres(String Entrada, String Salida) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Entrada));
            BufferedWriter writer = new BufferedWriter(new FileWriter(Salida))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[1].equalsIgnoreCase("F")) {  
                    writer.write(linea);
                    writer.newLine();
                }
            }
            System.out.println("Procesamiento de las Mujeres ha Sido Terminado...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void FuncionHombreMenores(String Entrada, String Salida) {
        int MinoriaEdad = 18;
        try (BufferedReader reader = new BufferedReader(new FileReader(Entrada));
             BufferedWriter writer = new BufferedWriter(new FileWriter(Salida))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");  
                try {
                    int edad = Integer.parseInt(datos[0]); 
                    if (datos[1].equalsIgnoreCase("M") && edad < MinoriaEdad) {  
                        writer.write(linea);
                        writer.newLine();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Dato de edad inválido: " + datos[0]);
                }
            }
            System.out.println("Procesamientod de los Hombres Menores de Edad ha Sido Terminado...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void LeerDocumentosCreados(String ArchivoUno, String ArchivoDos) {
        System.out.println("\nMujeres en la Base de Datos:");
        LeerArchivo(ArchivoUno);
        System.out.println("\nHombres Menores de Edad:");
        LeerArchivo(ArchivoDos);
    }
    
        public static void LeerArchivo(String Documento) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Documento))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[1].equalsIgnoreCase("F")) {  
                    System.out.println(datos[3] + "(" + datos[1] + ")");
                } else {
                    System.out.println(datos[3] + "(" + datos[0] + ")");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
