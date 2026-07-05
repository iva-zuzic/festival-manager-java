package hr.tvz.festivalmanager.service;

import hr.tvz.festivalmanager.entities.Artist;
import hr.tvz.festivalmanager.entities.Member;
import hr.tvz.festivalmanager.repository.ArtistRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servisna klasa za rad s umjetnicima.
 */
public class ArtistService {
    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    /**
     * Pretražuje umjetnike prema scenskom imenu.
     *
     * @param name scensko ime umjetnika
     * @return popis pronađenih umjetnika
     */
    public List<Artist> findByName(String name) {
        List<Artist> allArtists = artistRepository.getAllEntities();
        List<Artist> resultList = new ArrayList<>();

        for(Artist artist : allArtists) {
            if (artist.getStageName().equalsIgnoreCase(name)) {
                resultList.add(artist);
            }
        }
        return resultList;
    }

    /**
     * Grupira umjetnike prema žanru.
     *
     * @return mapa umjetnika grupiranih po žanru
     */
    public Map<Artist.Genre, List<Artist>> groupByGenre() {
        return artistRepository.getAllEntities().stream()
                .collect(Collectors.groupingBy(Artist::getGenre));
    }

    /**
     * Sortira umjetnike prema honoraru.
     *
     * @return sortirani popis umjetnika
     */
    public List<Artist> sortByFee() {
        return artistRepository.getAllEntities().stream()
                .sorted(Comparator.comparing(Artist::getFee))
                .toList();
    }

    /**
     * Pronalazi umjetnika s najvećim honorarom.
     *
     * @return umjetnik s najvećim honorarom
     */
    public Artist getHighestPaidArtist() {
        return artistRepository.getAllEntities().stream()
                .sorted(Comparator.comparing(Artist::getFee))
                .toList()
                .getLast();
    }

    /**
     * Pronalazi umjetnika s najmanjim honorarom.
     *
     * @return umjetnik s najmanjim honorarom
     */
    public Artist getLowestPaidArtist() {
        return artistRepository.getAllEntities().stream()
                .sorted(Comparator.comparing(Artist::getFee))
                .toList()
                .getFirst();
    }

    /**
     * Vraća scenska imena svih umjetnika.
     *
     * @return lista scenskih imena
     */
    public List<String> getArtistStageNames() {
        return artistRepository.getAllEntities().stream()
                .map(Artist::getStageName)
                .toList();
    }

    /**
     * Pronalazi sve bendove kojima zadani član pripada.
     *
     * @param member član za kojeg se traže bendovi
     * @return lista scenskih imena bendova, prazna ako član nije ni u jednom
     */
    public List<String> findBandsForMember(Member member) {
        return artistRepository.getAllEntities().stream()
                .filter(artist -> artist.getMembers().contains(member))
                .map(Artist::getStageName)
                .toList();
    }

    /**
     * Računa ukupni iznos honorara svih umjetnika.
     *
     * @return ukupni honorar umjetnika
     */
    public BigDecimal calculateTotalArtistFees() {
        return artistRepository.getAllEntities().stream()
                .map(Artist::getFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Računa prosječni honorar umjetnika po žanru.
     *
     * @return mapa u kojoj je ključ žanr, a vrijednost prosječni honorar tog žanra
     */
    public Map<Artist.Genre, Double> averageFeeByGenre() {
        return artistRepository.getAllEntities().stream()
                .collect(Collectors.groupingBy(
                        Artist::getGenre,
                        Collectors.averagingDouble(artist -> artist.getFee().doubleValue())
                ));
    }
}
