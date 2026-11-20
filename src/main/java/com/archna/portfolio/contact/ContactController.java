package com.archna.portfolio.contact;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ContactResponse> submit(@Valid @RequestBody ContactRequest request) {
        contactService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ContactResponse("Thank you! Your message has been received."));
    }
}
