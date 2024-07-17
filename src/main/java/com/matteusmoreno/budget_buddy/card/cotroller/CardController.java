package com.matteusmoreno.budget_buddy.card.cotroller;

import com.matteusmoreno.budget_buddy.card.entity.Card;
import com.matteusmoreno.budget_buddy.card.request.CreateCardRequest;
import com.matteusmoreno.budget_buddy.card.response.CardDetailsResponse;
import com.matteusmoreno.budget_buddy.card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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
}
