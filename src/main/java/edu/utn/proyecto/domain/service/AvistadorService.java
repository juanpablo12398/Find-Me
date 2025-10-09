package edu.utn.proyecto.domain.service;

import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.mappers.AvistadorMapper;
import edu.utn.proyecto.domain.model.concretas.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import edu.utn.proyecto.avistador.exception.DatosNoCoincidenException;
import edu.utn.proyecto.avistador.exception.DniDuplicadoException;
import edu.utn.proyecto.avistador.exception.PersonaNoEncontradaException;
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
        // normalizo entradas para evitar problemas de mayúsculas/acentos/espacios
        String dni      = normalize(dto.getDni());
        String nombre   = normalize(dto.getNombre());
        String apellido = normalize(dto.getApellido());

        // 1) existe en padrón RENAPER?
        RenaperPersonaEntity persona = renaperRepo.findByDni(dni)
                .orElseThrow(() -> new PersonaNoEncontradaException(dni));

        // 2) coinciden datos básicos?
        if (!nombre.equals(normalize(persona.getNombre())) ||
                !apellido.equals(normalize(persona.getApellido()))) {
            throw new DatosNoCoincidenException();
        }

        // 3) ya está registrado en nuestra app?
        if (avistadorRepo.existsByDni(dni)) {
            throw new DniDuplicadoException(dni);
        }

        // 4) guardar
        Avistador domain = mapper.fromRequestToDomain(dto);
        domain.setDni(dni); // guardo normalizado
        Avistador saved = avistadorRepo.save(domain);

        // 5) responder
        return mapper.fromDomainToResponse(saved);
    }

    private String normalize(String s) {
        if (s == null) return null;
        return java.text.Normalizer.normalize(s.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toUpperCase();
    }
}