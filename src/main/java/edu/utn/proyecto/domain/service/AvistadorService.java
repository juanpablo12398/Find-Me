package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.mappers.AvistadorMapper;
import edu.utn.proyecto.domain.model.concretas.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvistadorService {

    private final IRepoDeAvistadores avistadorRepo;
    private final IRepoDeRenaper renaperRepo;
    private final AvistadorMapper mapper;

    public AvistadorService(IRepoDeAvistadores avistadorRepo,
                            IRepoDeRenaper renaperRepo,
                            AvistadorMapper mapper) {
        this.avistadorRepo = avistadorRepo;
        this.renaperRepo = renaperRepo;
        this.mapper = mapper;
    }

    @Transactional
    public AvistadorResponseDTO registrar(AvistadorRequestDTO dto) {
        String dni = normalize(dto.getDni());
        String nombre = normalize(dto.getNombre());
        String apellido = normalize(dto.getApellido());

        // 1) existe en padrón?
        RenaperPersonaEntity persona = renaperRepo.findByDni(dni)
                .orElseThrow(() -> new PersonaNoEncontradaException(dni));

        // 2) coinciden datos básicos?
        if (!nombre.equals(normalize(persona.getNombre()))
                || !apellido.equals(normalize(persona.getApellido()))) {
            throw new DatosNoCoincidenException();
        }

        // 3) ya está registrado?
        if (avistadorRepo.existsByDni(dni)) {
            throw new DniDuplicadoException(dni);
        }

        // 4) guardar
        Avistador domain = mapper.fromRequestToDomain(dto);
        domain.setDni(dni); // normalizado
        Avistador saved = avistadorRepo.save(domain);

        return mapper.fromDomainToResponse(saved);
    }

    private String normalize(String s) {
        if (s == null) return null;
        return java.text.Normalizer.normalize(s.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toUpperCase();
    }

    // Excepciones
    public static class PersonaNoEncontradaException extends RuntimeException {
        public PersonaNoEncontradaException(String dni) { super("No existe en padrón: " + dni); }
    }
    public static class DatosNoCoincidenException extends RuntimeException {
        public DatosNoCoincidenException() { super("Los datos no coinciden con el padrón."); }
    }
    public static class DniDuplicadoException extends RuntimeException {
        public DniDuplicadoException(String dni) { super("El DNI ya está registrado: " + dni); }
    }
}