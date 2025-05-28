package com.example.proyecto.infrastructure.persitence;
import com.example.proyecto.domain.avistador.Avistador;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
// Como no puedo implementarla porque MongoDeckRepo es una interfaz de Spring
// que se inyecta, no se implemetna manualmente.
// Para que no me quede la interfaz inyectada en las clases RepositorioDeCardsMongo
// y en RepositorioDeDecksMongo. La defino aca y queda inyectada.

// Aca hay que aclarar que agrego este metodo findByDeckId, sin implementarlo
// desde la interface inyectada, gracias al naming de Spring, que me permite
// usando un nombre descriptivo que este entinda como tiene que hacer la query.

// Esto no es una interface definida, es una interface inyectada.
// Que va a usar solo la parte de mongo
public interface IMongoValidadorRepo extends MongoRepository<Avistador, String>{

}
