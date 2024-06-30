package com.sanfalcon.literalura;

import com.sanfalcon.literalura.principal.Principal;
import com.sanfalcon.literalura.repository.AutorRepository;
import com.sanfalcon.literalura.repository.IdiomaRepository;
import com.sanfalcon.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private IdiomaRepository idiomaRepository;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //? Pruebas con el consumo de api usando los servicios creados
        Principal principal = new Principal(this.libroRepository, this.autorRepository, this.idiomaRepository);
        principal.muestraElMenu();
    }
}
