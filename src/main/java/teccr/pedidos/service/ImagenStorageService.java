package teccr.pedidos.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImagenStorageService {
    private final Path carpeta=Paths.get("uploads");

    public String guardar(MultipartFile archivo){
        if (archivo==null || archivo.isEmpty()){
            return null;
        }
        try{
            Files.createDirectories(carpeta);
            String nombreFinal = UUID.randomUUID() + "-" + archivo.getOriginalFilename();
            Path destino = carpeta.resolve(nombreFinal);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + nombreFinal;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen", e);
        }
    }

    /** Borra del disco la imagen indicada por su ruta web (ej. /uploads/abc.jpg). */
    public void borrar(String rutaWeb) {
        if (rutaWeb == null || rutaWeb.isBlank()) {
            return; // no habia imagen, nada que borrar
        }
        try {
            // de "/uploads/abc.jpg" sacamos solo el nombre "abc.jpg"
            String nombre = Paths.get(rutaWeb).getFileName().toString();
            Files.deleteIfExists(carpeta.resolve(nombre));
        } catch (IOException e) {
            System.out.println("No se pudo borrar la imagen: " + rutaWeb);
        }
    }
}
