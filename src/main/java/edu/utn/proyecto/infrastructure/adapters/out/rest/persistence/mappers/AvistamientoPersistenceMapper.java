package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistamientoEntity;
import org.springframework.stereotype.Component;

@Component
public class AvistamientoPersistenceMapper {

    public AvistamientoEntity domainToEntity(Avistamiento d) {
        AvistamientoEntity e = new AvistamientoEntity();
        e.setId(d.getId());
        e.setAvistadorId(d.getAvistadorId());
        e.setDesaparecidoId(d.getDesaparecidoId());
        e.setLatitud(d.getLatitud());
        e.setLongitud(d.getLongitud());
        e.setFechaHora(d.getFechaHora());
        e.setDescripcion(d.getDescripcion());
        e.setFotoUrl(d.getFotoUrl());
        e.setVerificado(d.getVerificado());
        e.setPublico(d.getPublico());
        e.setCreadoEn(d.getCreadoEn());
        return e;
    }

    public Avistamiento entityToDomain(AvistamientoEntity e) {
        Avistamiento d = new Avistamiento();
        d.setId(e.getId());
        d.setAvistadorId(e.getAvistadorId());
        d.setDesaparecidoId(e.getDesaparecidoId());
        d.setLatitud(e.getLatitud());
        d.setLongitud(e.getLongitud());
        d.setFechaHora(e.getFechaHora());
        d.setDescripcion(e.getDescripcion());
        d.setFotoUrl(e.getFotoUrl());
        d.setVerificado(e.getVerificado());
        d.setPublico(e.getPublico());
        d.setCreadoEn(e.getCreadoEn());
        return d;
    }
}
