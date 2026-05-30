package hr.tvz.festivalmanager.repository.console;

import hr.tvz.festivalmanager.entities.Artist;
import hr.tvz.festivalmanager.entities.Member;
import hr.tvz.festivalmanager.repository.ArtistRepository;
import hr.tvz.festivalmanager.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static hr.tvz.festivalmanager.util.ConsoleUtils.*;

/**
 * Repozitorij za unos i dohvat umjetnika putem konzole.
 * Klasa omogućuje korisniku unos žanra, scenskog imena, članova benda
 * i honorara umjetnika.
 * Podaci o članovima dohvaćaju se iz {@link MemberRepository} kako bi se
 * umjetnici mogli povezati s postojećim članovima.
 */
public class ConsoleArtistRepository implements ArtistRepository {
    private static final Logger log = LoggerFactory.getLogger(ConsoleArtistRepository.class);
    private final List<Artist> artists = new ArrayList<>();
    private final MemberRepository memberRepository;
    private static final int NUMBER_OF_ENTITIES_TO_INPUT = 3;

    public ConsoleArtistRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Učitava podatke o umjetnicima putem konzole.
     * Za svakog umjetnika korisnik odabire žanr, unosi scensko ime,
     * bira članove iz postojećeg repozitorija članova te unosi honorar.
     * Nakon unosa kreira se objekt klase {@link Artist} pomoću builder obrasca.
     */
    @Override
    public void inputAllEntities() {
        List<Member> allMembers = memberRepository.getAllEntities();
        List<Artist.Genre> genres = List.of(Artist.Genre.values());

        for (int i = 0; i < NUMBER_OF_ENTITIES_TO_INPUT; i++) {
            log.trace("Unos {}. umjetnika", i + 1);

            for (int j = 0; j < genres.size(); j++) {
                log.info("{}. {}", j + 1, genres.get(j));
            }
            int chosenGenreIdx = readIntInRange("Unesite redni broj žanra ovog umjetnika: ", 1, genres.size());
            Artist.Genre chosenGenre = genres.get(chosenGenreIdx - 1);
            log.info("Izabran je: {}", chosenGenre);

            String stageName = readString("Unesite scensko ime umjetnika: ");

            log.info("Dostupni članovi:");
            for (int k = 0; k < allMembers.size(); k++) {
                log.info("{}. {} {}", k + 1, allMembers.get(k).getFirstName(), allMembers.get(k).getLastName());
            }

            int brojClanova = readStrictlyPositiveInt("Koliko članova ima bend? ");
            List<Member> chosenMembers = new ArrayList<>();

            for (int l = 0; l < brojClanova; l++) {
                int idx = readIntInRange("Unesite redni broj " + (l + 1) + ". člana: ", 1, allMembers.size());
                chosenMembers.add(allMembers.get(idx - 1));
            }

            BigDecimal fee = readNonNegativeBigDecimal("Unesite iznos honorara: ");

            artists.add(new Artist.ArtistBuilder
                    (stageName, chosenMembers)
                    .genre(chosenGenre)
                    .fee(fee)
                    .build());

            log.debug("Unesen umjetnik: {}", artists.get(i));
        }
        log.info("Uneseni su svi umjetnici");
    }

    /**
     * Vraća popis svih unesenih umjetnika.
     * @return popis umjetnika
     */
    @Override
    public List<Artist> getAllEntities() {
        return artists;
    }
}
