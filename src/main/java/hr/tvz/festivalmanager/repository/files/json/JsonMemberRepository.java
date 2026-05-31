package hr.tvz.festivalmanager.repository.files.json;

import hr.tvz.festivalmanager.entities.Member;
import hr.tvz.festivalmanager.repository.MemberRepository;
import hr.tvz.festivalmanager.repository.files.json.dto.MemberJson;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonMemberRepository implements MemberRepository {
    private static final Logger logger = LoggerFactory.getLogger(JsonMemberRepository.class);
    private static final Path MEMBERS_FILE_PATH = Path.of("src/main/resources/data/members.json");
    private final List<Member> members = new ArrayList<>();
    private final Set<String> existingEmails = new HashSet<>();

    @Override
    public void inputAllEntities() {
        try {
            String json = Files.readString(MEMBERS_FILE_PATH);
            Jsonb jsonb = JsonbBuilder.create();

            Type memberListType = new ArrayList<MemberJson>() {
            }.getClass().getGenericSuperclass();

            List<MemberJson> memberJsonList = jsonb.fromJson(json, memberListType);

            members.clear();
            existingEmails.clear();

            for (MemberJson memberJson : memberJsonList) {
                Member member = new Member(memberJson.getFirstName(),
                        memberJson.getLastName(),
                        memberJson.getEmail(),
                        memberJson.getDateOfBirth(),
                        memberJson.getRole());
                members.add(member);
                existingEmails.add(member.getEmail());
            }

            logger.info("Member(član) podaci uspješno su učitani iz JSON datoteke.");

        } catch (IOException | JsonbException exception) {
            logger.error("Dogodila se pogreška kod čitanja članova iz JSON datoteke.", exception);
        }
    }

    @Override
    public List<Member> getAllEntities() {
        return members;
    }

    private void saveAllEntities() {
        List<MemberJson> memberJsonList = new ArrayList<>();

        for (Member member : members) {
            MemberJson memberJson = new MemberJson(member.getFirstName(),
                    member.getLastName(),
                    member.getEmail(),
                    member.getDateOfBirth(),
                    member.getRole());
            memberJsonList.add(memberJson);
        }

        try {
            JsonbConfig config = new JsonbConfig()
                    .withFormatting(true);

            Jsonb jsonb = JsonbBuilder.create(config);
            String json = jsonb.toJson(memberJsonList);

            Files.writeString(MEMBERS_FILE_PATH, json);

            logger.info("Member(član) podaci uspješno su spremljeni u JSON datoteku.");
        } catch (IOException | JsonbException exception) {
            logger.error("Dogodila se pogreška kod spremanja članova u JSON datoteku.", exception);
        }
    }

    public void saveEntity(Member member) {
        if (existingEmails.contains(member.getEmail())) {
            logger.warn("Član s email adresom {} već postoji i neće biti ponovno spremljen.", member.getEmail());
            return;
        }
        members.add(member);
        existingEmails.add(member.getEmail());
        saveAllEntities();
    }
}