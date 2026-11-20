package com.archna.portfolio.contact;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final ExcelContactWriter excelContactWriter;

    public ContactService(ContactRepository contactRepository,
                          ExcelContactWriter excelContactWriter) {
        this.contactRepository = contactRepository;
        this.excelContactWriter = excelContactWriter;
    }

    @Transactional
    public void save(ContactRequest request) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(request.name().trim());
        contactMessage.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        contactMessage.setMessage(request.message().trim());

        ContactMessage savedMessage = contactRepository.saveAndFlush(contactMessage);
        excelContactWriter.append(savedMessage);
    }
}
