package hr.tvz.festivalmanager.repository.files.json;

import hr.tvz.festivalmanager.entities.Artist;
import hr.tvz.festivalmanager.entities.Member;
import hr.tvz.festivalmanager.repository.ArtistRepository;
import hr.tvz.festivalmanager.repository.MemberRepository;
import hr.tvz.festivalmanager.repository.files.json.dto.ArtistJson;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.JsonbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.io.Serial;

public class JsonArtistRepository implements ArtistRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(JsonArtistRepository.class);

    private static final Path ARTISTS_FILE_PATH =
            Path.of("src/main/resources/data/artists.json");

    private final MemberRepository memberRepository;
    private final List<Artist> artists = new ArrayList<>();

    private static final class ArtistJsonList extends ArrayList<ArtistJson> {
        @Serial
        private static final long serialVersionUID = 1L;
    }

    public JsonArtistRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void inputAllEntities() throws IOException {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            String json = Files.readString(ARTISTS_FILE_PATH);

            Type artistListType =
                    ArtistJsonList.class.getGenericSuperclass();

            List<ArtistJson> artistJsonList =
                    jsonb.fromJson(json, artistListType);

            artists.clear();

            for (ArtistJson artistJson : artistJsonList) {
                List<Member> artistMembers = memberRepository.getAllEntities()
                        .stream()
                        .filter(member ->
                                artistJson.getMemberEmails().contains(member.getEmail()))
                        .toList();

                Artist artist = new Artist.ArtistBuilder(
                        artistJson.getStageName(),
                        artistMembers
                )
                        .genre(artistJson.getGenre())
                        .fee(artistJson.getFee())
                        .build();

                artists.add(artist);
            }

            logger.info("Podaci o umjetnicima uspješno su učitani iz JSON datoteke.");

        } catch (JsonbException exception) {
            throw new IOException(
                    "Dogodila se pogreška kod parsiranja umjetnika iz JSON datoteke.",
                    exception
            );
        } catch (Exception exception) {
            throw new IOException(
                    "Dogodila se pogreška kod zatvaranja JSON-B resursa.",
                    exception
            );
        }
    }

    @Override
    public List<Artist> getAllEntities() {
        return artists;
    }

    private void saveAllEntities() throws IOException {
        List<ArtistJson> artistJsonList = new ArrayList<>();

        for (Artist artist : artists) {
            List<String> memberEmails = artist.getMembers()
                    .stream()
                    .map(Member::getEmail)
                    .toList();

            ArtistJson artistJson = new ArtistJson(
                    artist.getStageName(),
                    artist.getGenre(),
                    artist.getFee(),
                    memberEmails
            );
            artistJsonList.add(artistJson);
        }

        JsonbConfig config = new JsonbConfig().withFormatting(true);

        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            String json = jsonb.toJson(artistJsonList);
            Files.writeString(ARTISTS_FILE_PATH, json);
            logger.info("Podaci o umjetnicima uspješno su spremljeni u JSON datoteku.");
        } catch (JsonbException exception) {
            throw new IOException("Dogodila se pogreška kod pretvaranja umjetnika u JSON format.", exception);
        } catch (Exception exception) {
            throw new IOException("Dogodila se pogreška kod zatvaranja JSON-B resursa.", exception);
        }
    }

    public void saveEntity(Artist artist) throws IOException {
        artists.add(artist);
        saveAllEntities();
    }
}