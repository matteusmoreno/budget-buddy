package com.matteusmoreno.budget_buddy.card.controller;

import com.matteusmoreno.budget_buddy.card.entity.Card;
import com.matteusmoreno.budget_buddy.card.request.CreateCardRequest;
import com.matteusmoreno.budget_buddy.card.request.UpdateCardRequest;
import com.matteusmoreno.budget_buddy.card.response.CardDetailsResponse;
import com.matteusmoreno.budget_buddy.card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create")
    public ResponseEntity<CardDetailsResponse> create(@RequestBody @Valid CreateCardRequest request, UriComponentsBuilder uriBuilder) {
        Card card = cardService.createCard(request);
        URI uri = uriBuilder.path("/cards/create/{id}").buildAndExpand(card.getId()).toUri();

        return ResponseEntity.created(uri).body(new CardDetailsResponse(card));
    }

    @GetMapping
    public ResponseEntity<List<CardDetailsResponse>> getCardById() {
        List<Card> cards = cardService.getCardsByCustomer();

        return ResponseEntity.ok(cards.stream().map(CardDetailsResponse::new).toList());
    }

    @PutMapping("/update")
    public ResponseEntity<CardDetailsResponse> updateCard(@RequestBody @Valid UpdateCardRequest request) {
        Card card = cardService.updateCard(request);

        return ResponseEntity.ok(new CardDetailsResponse(card));
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        cardService.disableCard(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<CardDetailsResponse> enableCard(@PathVariable Long id) {
        Card card = cardService.enableCard(id);

        return ResponseEntity.ok(new CardDetailsResponse(card));
    }
}
