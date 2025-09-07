package com.pathmonk.crmapi;

import com.pathmonk.crmapi.model.Contact;
import com.pathmonk.crmapi.model.ContactTag;
import com.pathmonk.crmapi.model.Deal;
import com.pathmonk.crmapi.model.DealStage;
import com.pathmonk.crmapi.model.Tag;
import com.pathmonk.crmapi.model.TagType;
import com.pathmonk.crmapi.repo.ContactRepo;
import com.pathmonk.crmapi.repo.TagRepo;
import com.pathmonk.crmapi.repo.ContactTagRepo;
import com.pathmonk.crmapi.repo.DealRepo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ContactRepo contactRepo;
    private final TagRepo tagRepo;
    private final ContactTagRepo contactTagRepo;
    private final DealRepo dealRepo;

    public DataSeeder(ContactRepo contactRepo, TagRepo tagRepo, ContactTagRepo contactTagRepo, DealRepo dealRepo) {
        this.contactRepo = contactRepo;
        this.tagRepo = tagRepo;
        this.contactTagRepo = contactTagRepo;
        this.dealRepo = dealRepo;
    }

    @Override
    public void run(String... args) {

        // --- CREATE CONTACTS ---
        Contact alice = contactRepo.save(new Contact(null, "Alice", "alice@example.com", 0L));
        Contact bob = contactRepo.save(new Contact(null, "Bob", "bob@example.com", 0L));
        Contact charlie = contactRepo.save(new Contact(null, "Charlie", "charlie@example.com", 0L));
        Contact dave = contactRepo.save(new Contact(null, "Dave", "dave@example.com", 0L));
        Contact eve = contactRepo.save(new Contact(null, "Eve", "eve@example.com", 0L));

        // --- CREATE TAGS ---
        Tag vip = tagRepo.save(new Tag(null, "vip", TagType.USER, Instant.now()));
        Tag nl = tagRepo.save(new Tag(null, "nl", TagType.AUTO, Instant.now()));
        Tag premium = tagRepo.save(new Tag(null, "premium", TagType.USER, Instant.now()));
        Tag newsletter = tagRepo.save(new Tag(null, "newsletter", TagType.AUTO, Instant.now()));
        Tag prospect = tagRepo.save(new Tag(null, "prospect", TagType.USER, Instant.now()));
        Tag lead = tagRepo.save(new Tag(null, "lead", TagType.AUTO, Instant.now()));

        dealRepo.save(new Deal(null, "Acme Renewal", 5000.0, DealStage.NEW));
        dealRepo.save(new Deal(null, "Beta Expansion", 3000.0, DealStage.QUALIFIED));
        dealRepo.save(new Deal(null, "Gamma Upgrade", 8000.0, DealStage.WON));
        dealRepo.save(new Deal(null, "Delta Expansion", 4500.0, DealStage.NEW));
        dealRepo.save(new Deal(null, "Epsilon Upgrade", 6000.0, DealStage.QUALIFIED));
        dealRepo.save(new Deal(null, "Zeta Renewal", 7500.0, DealStage.WON));

        // --- ATTACH TAGS TO CONTACTS ---
        contactTagRepo.saveAll(List.of(
                new ContactTag(null, alice, vip, Instant.now()),
                new ContactTag(null, alice, nl, Instant.now()),
                new ContactTag(null, bob, premium, Instant.now()),
                new ContactTag(null, bob, newsletter, Instant.now()),
                new ContactTag(null, charlie, vip, Instant.now()),
                new ContactTag(null, charlie, lead, Instant.now()),
                new ContactTag(null, dave, newsletter, Instant.now()),
                new ContactTag(null, dave, prospect, Instant.now()),
                new ContactTag(null, eve, premium, Instant.now()),
                new ContactTag(null, eve, lead, Instant.now())));

        // --- LOGGING ---
        System.out.println("=== Seed Data Loaded ===");
        System.out.println("Contacts:");
        contactRepo.findAll().forEach(
                c -> System.out.println(" - " + c.getId() + ": " + c.getName() + " (version=" + c.getVersion() + ")"));

        System.out.println("Tags:");
        tagRepo.findAll()
                .forEach(t -> System.out.println(" - " + t.getId() + ": " + t.getName() + " [" + t.getType() + "]"));

        System.out.println("Contact-Tag Relationships:");
        contactTagRepo.findAll().forEach(
                ct -> System.out.println(" - contact=" + ct.getContact().getName() + ", tag=" + ct.getTag().getName()));

        System.out.println("=======================");
    }
}
