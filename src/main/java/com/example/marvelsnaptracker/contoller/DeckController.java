package com.example.marvelsnaptracker.contoller;

import com.example.marvelsnaptracker.decks.Deck;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DeckController {

    @FXML
    private Label label;

    public void setDeck(Deck deck) {
        label.setText(deck.getName());
    }
}
