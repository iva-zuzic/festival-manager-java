package hr.tvz.festivalmanager.repository.files.json.dto;

import hr.tvz.festivalmanager.entities.Artist;

import java.math.BigDecimal;
import java.util.List;

public class ArtistJson {

    private String stageName;
    private Artist.Genre genre;
    private BigDecimal fee;
    private List<String> memberEmails;

    public ArtistJson() {
    }

    public ArtistJson(
            String stageName,
            Artist.Genre genre,
            BigDecimal fee,
            List<String> memberEmails
    ) {
        this.stageName = stageName;
        this.genre = genre;
        this.fee = fee;
        this.memberEmails = memberEmails;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Artist.Genre getGenre() {
        return genre;
    }

    public void setGenre(Artist.Genre genre) {
        this.genre = genre;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public List<String> getMemberEmails() {
        return memberEmails;
    }

    public void setMemberEmails(List<String> memberEmails) {
        this.memberEmails = memberEmails;
    }
}