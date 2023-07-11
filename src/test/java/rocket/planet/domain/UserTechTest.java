package rocket.planet.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import rocket.planet.repository.jpa.*;
import rocket.planet.repository.jpa.ProfileRepository;

@SpringBootTest
public class UserTechTest {

    @Autowired
    private TechRepository techRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PfTechRepository pfTechRepository;

    @Test
    @Rollback(false)
    void saveUserTech() {
        Tech lang1 = techRepository.findByTechName("Java");
        Tech lang2 = techRepository.findByTechName("Python");
        Tech lang3 = techRepository.findByTechName("Kotlin");
        Tech lang4 = techRepository.findByTechName("C");
        Tech lang5 = techRepository.findByTechName("PHP");
        Tech lang6 = techRepository.findByTechName("Ruby");
        Tech lang7 = techRepository.findByTechName("C++");
        Tech lang8 = techRepository.findByTechName("JavaScript");
        Tech frame1 = techRepository.findByTechName("Spring");
        Tech frame2 = techRepository.findByTechName("Spring Boot");
        Tech frame3 = techRepository.findByTechName("Django");
        Tech frame4 = techRepository.findByTechName("Flutter");
        Tech frame5 = techRepository.findByTechName("Vue.js");
        Tech frame6 = techRepository.findByTechName("React.js");
        Tech db1 = techRepository.findByTechName("MySQL");
        Tech db2 = techRepository.findByTechName("Oracle");
        Tech db3 = techRepository.findByTechName("Redis");



        Profile crewProfile = profileRepository.findByUserNickName("crew").get();
        ProfileTech crewPfT1 = ProfileTech.builder()
                .tech(lang1)
                .profile(crewProfile)
                .build();
        pfTechRepository.saveAndFlush(crewPfT1);
        ProfileTech crewPfT2 = ProfileTech.builder()
                .tech(frame1)
                .profile(crewProfile)
                .build();
        pfTechRepository.saveAndFlush(crewPfT2);
        ProfileTech crewPfT3 = ProfileTech.builder()
                .tech(frame2)
                .profile(crewProfile)
                .build();
        pfTechRepository.saveAndFlush(crewPfT3);
        ProfileTech crewPfT4 = ProfileTech.builder()
                .tech(db1)
                .profile(crewProfile)
                .build();
        pfTechRepository.saveAndFlush(crewPfT4);
        ProfileTech crewPfT5 = ProfileTech.builder()
                .tech(db3)
                .profile(crewProfile)
                .build();
        pfTechRepository.saveAndFlush(crewPfT5);



        Profile pilotProfile = profileRepository.findByUserNickName("pilot").get();
        ProfileTech pilotPfT1 = ProfileTech.builder()
                .tech(lang2)
                .profile(pilotProfile)
                .build();
        pfTechRepository.saveAndFlush(pilotPfT1);
        ProfileTech pilotPfT2 = ProfileTech.builder()
                .tech(lang8)
                .profile(pilotProfile)
                .build();
        pfTechRepository.saveAndFlush(pilotPfT2);
        ProfileTech pilotPfT3 = ProfileTech.builder()
                .tech(frame3)
                .profile(pilotProfile)
                .build();
        pfTechRepository.saveAndFlush(pilotPfT3);
        ProfileTech pilotPfT4 = ProfileTech.builder()
                .tech(db1)
                .profile(pilotProfile)
                .build();
        pfTechRepository.saveAndFlush(pilotPfT4);
        ProfileTech pilotPfT5 = ProfileTech.builder()
                .tech(db2)
                .profile(pilotProfile)
                .build();
        pfTechRepository.saveAndFlush(pilotPfT5);



        Profile captainProfile = profileRepository.findByUserNickName("captain").get();
        ProfileTech captainPfT1 = ProfileTech.builder()
                .tech(lang3)
                .profile(captainProfile)
                .build();
        pfTechRepository.saveAndFlush(captainPfT1);
        ProfileTech captainPfT2 = ProfileTech.builder()
                .tech(lang4)
                .profile(captainProfile)
                .build();
        pfTechRepository.saveAndFlush(captainPfT2);
        ProfileTech captainPfT3 = ProfileTech.builder()
                .tech(frame4)
                .profile(captainProfile)
                .build();
        pfTechRepository.saveAndFlush(captainPfT3);
        ProfileTech captainPfT4 = ProfileTech.builder()
                .tech(frame5)
                .profile(captainProfile)
                .build();
        pfTechRepository.saveAndFlush(captainPfT4);
        ProfileTech captainPfT5 = ProfileTech.builder()
                .tech(db1)
                .profile(captainProfile)
                .build();
        pfTechRepository.saveAndFlush(captainPfT5);



        Profile radarProfile = profileRepository.findByUserNickName("radar").get();
        ProfileTech radarPfT1 = ProfileTech.builder()
                .tech(lang5)
                .profile(radarProfile)
                .build();
        pfTechRepository.saveAndFlush(radarPfT1);
        ProfileTech radarPfT2 = ProfileTech.builder()
                .tech(lang6)
                .profile(radarProfile)
                .build();
        pfTechRepository.saveAndFlush(radarPfT2);
        ProfileTech radarPfT3 = ProfileTech.builder()
                .tech(frame6)
                .profile(radarProfile)
                .build();
        pfTechRepository.saveAndFlush(radarPfT3);
        ProfileTech radarPfT4 = ProfileTech.builder()
                .tech(lang7)
                .profile(radarProfile)
                .build();
        pfTechRepository.saveAndFlush(radarPfT4);
        ProfileTech radarPfT5 = ProfileTech.builder()
                .tech(db3)
                .profile(radarProfile)
                .build();
        pfTechRepository.saveAndFlush(radarPfT5);



        Profile plProfile = profileRepository.findByUserNickName("plpl").get();
        ProfileTech plPfT1 = ProfileTech.builder()
                .tech(lang1)
                .profile(plProfile)
                .build();
        pfTechRepository.saveAndFlush(plPfT1);
        ProfileTech plPfT2 = ProfileTech.builder()
                .tech(lang2)
                .profile(plProfile)
                .build();
        pfTechRepository.saveAndFlush(plPfT2);
        ProfileTech plPfT3 = ProfileTech.builder()
                .tech(frame1)
                .profile(plProfile)
                .build();
        pfTechRepository.saveAndFlush(plPfT3);
        ProfileTech plPfT4 = ProfileTech.builder()
                .tech(lang2)
                .profile(plProfile)
                .build();
        pfTechRepository.saveAndFlush(plPfT4);
        ProfileTech plPfT5 = ProfileTech.builder()
                .tech(db1)
                .profile(plProfile)
                .build();
        pfTechRepository.saveAndFlush(plPfT5);


    }

}
