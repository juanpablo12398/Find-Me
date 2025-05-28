package com.example.proyecto.infrastructure.persitence;

import com.example.proyecto.domain.avistador.Avistador;

public interface iRepoDeAvistadores {
        public Avistador getAvistadorDni(String id);
        public Avistador getAvistadorDni(String id);

        public void addDeck(Deck deck);
        public void updateDeck(Deck deck);
        public void updateDeckById(String id, Deck deck);
        public void deleteDeck(Deck deck);
        public void deleteDeckById(String id);

}
