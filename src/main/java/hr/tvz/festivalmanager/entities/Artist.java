package hr.tvz.festivalmanager.entities;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Predstavlja umjetnika ili izvođača koji nastupa na festivalu.
 * Umjetnik ima žanr, scensko ime, popis članova i honorar.
 * Klasa implementira sučelje {@link Payable}, što znači da za umjetnika
 * postoji podatak o iznosu honorara.
 * Objekti klase {@code Artist} kreiraju se pomoću {@link ArtistBuilder}
 * builder obrasca.
 */
public non-sealed class Artist implements Payable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum Genre {
        ROCK,
        METAL,
        POP,
        ELECTRONIC,
        JAZZ,
        COUNTRY,
        HIP_HOP,
        RNB,
        OSTALO
    }

    private final Genre genre;
    private final String stageName;
    private final List<Member> members;
    private final BigDecimal fee;

    /**
     * Privatni konstruktor koji kreira umjetnika pomoću builder objekta.
     * @param builder builder objekt koji sadrži podatke za kreiranje umjetnika
     */
    private Artist(ArtistBuilder builder) {
        this.genre = builder.genre;
        this.stageName = builder.stageName;
        this.members = builder.members;
        this.fee = builder.fee;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getStageName() {
        return stageName;
    }

    public List<Member> getMembers() {
        return members;
    }

    /**
     * Builder klasa za kreiranje objekata klase {@link Artist}.
     * Obavezni podaci su scensko ime i popis članova, dok se žanr i honorar
     * mogu dodatno postaviti pomoću metoda {@link #genre(Genre)} i {@link #fee(BigDecimal)}.
     */
    public static class ArtistBuilder {
        private final String stageName;
        private final List<Member> members;

        private Genre genre = Genre.OSTALO;
        private BigDecimal fee = BigDecimal.ZERO;

        public ArtistBuilder(String stageName, List<Member> members) {
            this.stageName = stageName;
            this.members = members;
        }

        public ArtistBuilder genre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public ArtistBuilder fee(BigDecimal fee) {
            this.fee = fee;
            return this;
        }

        public Artist build() {
            return new Artist(this);
        }
    }

    @Override
    public BigDecimal getFee() {
        return fee;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return genre == artist.genre && Objects.equals(stageName, artist.stageName) && Objects.equals(members, artist.members) && Objects.equals(fee, artist.fee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genre, stageName, members, fee);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "žanr: " + genre +
                ", ime: " + stageName + '\'' +
                ", članovi: " + members +
                ", honorar: " + fee +
                '}';
    }
}
