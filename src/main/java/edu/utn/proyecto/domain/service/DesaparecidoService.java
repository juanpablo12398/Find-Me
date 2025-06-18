package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.DesaparecidoMapper;
import edu.utn.proyecto.domain.model.concretas.Desaparecido;
import edu.utn.proyecto.domain.service.abstraccion.IDesaparecidoService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persitence.RepositorioDeDesaparecidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DesaparecidoService implements IDesaparecidoService {

    private final RepositorioDeDesaparecidos repositorioDeDesaparecidos;
    private final DesaparecidoMapper desaparecidoMapper;

    @Autowired
    public DesaparecidoService(RepositorioDeDesaparecidos repositorioDeDesaparecidos, DesaparecidoMapper desaparecidoMapper) {
        this.repositorioDeDesaparecidos = repositorioDeDesaparecidos;
        this.desaparecidoMapper = desaparecidoMapper;
    }

    // Flujo de creacion de desaparecido
    public DesaparecidoResponseDTO crearDesaparecido(DesaparecidoRequestDTO requestDto) {
        // Primero tradusco a una entidad de dominio
        Desaparecido desaparecido = desaparecidoMapper.fromRequestToDomain(requestDto);
        // Luego lo guardo en mi repositorio
        this.repositorioDeDesaparecidos.save(desaparecido);
        // Y devuelvo el dominio convertido a DTO de respuesta
        return desaparecidoMapper.fromDomainToResponse(desaparecido);
    }

    //Flujo de obtener desaparecidos
    public List<DesaparecidoResponseDTO> obtenerDesaparecidos() {
        // Obtengo la lista de desaparecidos del repositorio
        List<Desaparecido> desaparecidos = this.repositorioDeDesaparecidos.getDesaparecidos();
        // Mapeo cada desaparecido a su DTO de respuesta
        return desaparecidoMapper.fromDomainToResponse(desaparecidos);
    }

}
