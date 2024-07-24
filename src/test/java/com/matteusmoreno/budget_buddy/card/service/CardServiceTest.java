package com.matteusmoreno.budget_buddy.card.service;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.card.CardRepository;
import com.matteusmoreno.budget_buddy.card.constant.CardType;
import com.matteusmoreno.budget_buddy.card.entity.Card;
import com.matteusmoreno.budget_buddy.card.request.CreateCardRequest;
import com.matteusmoreno.budget_buddy.card.request.UpdateCardRequest;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.constant.Country;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import com.matteusmoreno.budget_buddy.utils.AppUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Card Service Unit Tests")
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    AppUtils appUtils;

    @InjectMocks
    private CardService cardService;

    private final Card firstCard = new Card(10L, "FIRST CARD", "8888888888888888", "321", "12/29",
            CardType.CREDIT, LocalDateTime.of(2023, 1, 1, 0, 0, 0), null, null, true);

    private final Card secondCard = new Card(20L, "SECOND CARD", "7777777777777777", "123", "12/29",
            CardType.DEBIT, LocalDateTime.of(2023, 1, 1, 0, 0, 0), null, null, true);

    private final Customer authenticatedCustomer = new Customer(UUID.randomUUID(), "username", "password", "Customer Name",
            "email@email.com", "(99)999999999", "999.999.999-99", new Address(), Country.BRAZIL, new ArrayList<>(List.of(firstCard, secondCard)), BigDecimal.ZERO, LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            null, null, true);

    private final CreateCardRequest createCardRequest = new CreateCardRequest("name", "9999999999999999",
            "123", "12/30", CardType.CREDIT);

    @Test
    @DisplayName("Should create a new card and save in database")
    void shouldCreateNewCardAndSaveInDatabase() {

        when(appUtils.getAuthenticatedUser()).thenReturn(authenticatedCustomer);

        Card result = cardService.createCard(createCardRequest);

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(cardRepository, times(1)).save(result);
        verify(customerRepository, times(1)).save(authenticatedCustomer);
        verify(cardRepository, times(1)).existsByNumber(result.getNumber());
        verify(cardRepository, times(0)).findByNumber(any());

        assertEquals(createCardRequest.name().toUpperCase(), result.getName());
        assertEquals(createCardRequest.number(), result.getNumber());
        assertEquals(createCardRequest.cvv(), result.getCvv());
        assertEquals(createCardRequest.expirationDate(), result.getExpirationDate());
        assertEquals(createCardRequest.cardType(), result.getCardType());
        assertEquals(authenticatedCustomer.getCards().get(2), result);
    }

    @Test
    @DisplayName("Should return an existing card from database")
    void shouldReturnAnExistingCardFromDatabase() {
        CreateCardRequest request = new CreateCardRequest("FIRST CARD", "8888888888888888", "321", "12/29", CardType.CREDIT);
        Customer newCustomer = new Customer();

        when(appUtils.getAuthenticatedUser()).thenReturn(newCustomer);
        when(cardRepository.existsByNumber(request.number())).thenReturn(true);
        when(cardRepository.findByNumber(request.number())).thenReturn(firstCard);

        Card result = cardService.createCard(request);

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(customerRepository, times(1)).save(newCustomer);
        verify(cardRepository, times(1)).existsByNumber(result.getNumber());
        verify(cardRepository, times(1)).findByNumber(request.number());
        verify(cardRepository, times(0)).save(result);

        assertEquals(firstCard, result);
    }

    @Test
    @DisplayName("Should list cards by Customer")
    void shouldListCardsByCustomer() {

        when(appUtils.getAuthenticatedUser()).thenReturn(authenticatedCustomer);

        List<Card> cards = cardService.getCardsByCustomer();

        verify(appUtils, times(1)).getAuthenticatedUser();

        assertEquals(authenticatedCustomer.getCards(), cards);
        assertEquals(authenticatedCustomer.getCards().get(0), cards.get(0));
        assertEquals(authenticatedCustomer.getCards().get(1), cards.get(1));
    }

    @Test
    @DisplayName("Should update card and save in database")
    void shouldUpdateCardAndSaveInDatabase() {
        UpdateCardRequest request = new UpdateCardRequest(10L, "UPDATED CARD", "0000000000000000", "222", "02/29", CardType.DEBIT);

        when(appUtils.getAuthenticatedUser()).thenReturn(authenticatedCustomer);
        when(cardRepository.findById(request.id())).thenReturn(Optional.of(firstCard));
        doNothing().when(appUtils).verifyCustomerHasCard(authenticatedCustomer, firstCard);

        Card result = cardService.updateCard(request);

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(cardRepository, times(1)).findById(request.id());
        verify(cardRepository, times(1)).save(result);

        assertEquals(firstCard.getId(), result.getId());
        assertEquals(request.name().toUpperCase(), result.getName());
        assertEquals(request.number(), result.getNumber());
        assertEquals(request.cvv(), result.getCvv());
        assertEquals(request.expirationDate(), result.getExpirationDate());
        assertEquals(request.cardType(), result.getCardType());
    }

    @Test
    @DisplayName("Should Throw BadCredentialsException When Customer is Not Card Owner")
    void shouldThrowBadCredentialsExceptionWhenCustomerIsNotCardOwner() {
        UpdateCardRequest request = new UpdateCardRequest(10L, "UPDATED CARD", "0000000000000000", "222", "02/29", CardType.DEBIT);
        Customer customer = new Customer();


        when(appUtils.getAuthenticatedUser()).thenReturn(customer);
        when(cardRepository.findById(request.id())).thenReturn(Optional.of(firstCard));
        doThrow(new BadCredentialsException("You don't have permission to access this card"))
                .when(appUtils).verifyCustomerHasCard(customer, firstCard);

        assertThrows(BadCredentialsException.class, () -> cardService.updateCard(request));

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(cardRepository, times(1)).findById(request.id());
        verify(appUtils, times(1)).verifyCustomerHasCard(customer, firstCard);
    }

    @Test
    @DisplayName("Should disable card")
    void shouldDisableCard() {

        when(appUtils.getAuthenticatedUser()).thenReturn(authenticatedCustomer);
        when(cardRepository.findById(10L)).thenReturn(Optional.of(firstCard));
        doNothing().when(appUtils).verifyCustomerHasCard(authenticatedCustomer, firstCard);

        cardService.disableCard(10L);

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(cardRepository, times(1)).findById(10L);
        verify(cardRepository, times(1)).save(firstCard);

        assertNotNull(firstCard.getDeletedAt());
        assertFalse(firstCard.getActive());
    }

    @Test
    @DisplayName("Should enable card")
    void shouldEnableCard() {

        when(appUtils.getAuthenticatedUser()).thenReturn(authenticatedCustomer);
        when(cardRepository.findById(10L)).thenReturn(Optional.of(firstCard));
        doNothing().when(appUtils).verifyCustomerHasCard(authenticatedCustomer, firstCard);

        Card result = cardService.enableCard(10L);

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(cardRepository, times(1)).findById(10L);
        verify(cardRepository, times(1)).save(firstCard);

        assertNull(result.getDeletedAt());
        assertTrue(result.getActive());
    }
}