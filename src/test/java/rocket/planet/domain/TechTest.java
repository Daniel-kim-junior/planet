package rocket.planet.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.TechRepository;

import java.util.UUID;

@SpringBootTest
public class TechTest {

    @Autowired
    private TechRepository techRepository;

    @Test
    @Rollback(false)
    public void createTechTestData() {

        Tech lang1 = Tech.builder()
                .techName("Java")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang1);
        Tech lang2 = Tech.builder()
                .techName("Javascript")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang2);
        Tech lang3 = Tech.builder()
                .techName("PHP")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang3);
        Tech lang4 = Tech.builder()
                .techName("Python")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang4);
        Tech lang5 = Tech.builder()
                .techName("SQL")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang5);
        Tech lang6 = Tech.builder()
                .techName(".Net")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang6);
        Tech lang7 = Tech.builder()
                .techName("C#")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang7);
        Tech lang8 = Tech.builder()
                .techName("C++")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang8);
        Tech lang9 = Tech.builder()
                .techName("C")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang9);
        Tech lang10 = Tech.builder()
                .techName("TypeScript")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang10);
        Tech lang11 = Tech.builder()
                .techName("Swift")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang11);
        Tech lang12 = Tech.builder()
                .techName("Kotlin")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang12);
        Tech lang13 = Tech.builder()
                .techName("Go")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang13);
        Tech lang14 = Tech.builder()
                .techName("Dart")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang14);
        Tech lang15 = Tech.builder()
                .techName("Scalr")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang15);
        Tech lang16 = Tech.builder()
                .techName("Ruby")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang16);
        Tech lang17 = Tech.builder()
                .techName("Rust")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang17);
        Tech lang18 = Tech.builder()
                .techName("ObjectiveC")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang18);
        Tech lang19 = Tech.builder()
                .techName("Haskell")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang19);
        Tech lang20 = Tech.builder()
                .techName("Clojure")
                .techCategory("Language")
                .build();
        techRepository.saveAndFlush(lang20);

        Tech frame1 = Tech.builder()
                .techName("Spring Boot")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame1);
        Tech frame2 = Tech.builder()
                .techName("React.js")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame2);
        Tech frame3 = Tech.builder()
                .techName("Spring")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame3);
        Tech frame4 = Tech.builder()
                .techName("Node.js")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame4);
        Tech frame5 = Tech.builder()
                .techName("Vue.js")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame5);
        Tech frame6 = Tech.builder()
                .techName("Django")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame6);


        Tech frame7 = Tech.builder()
                .techName("TensorFlow")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame7);
        Tech frame8 = Tech.builder()
                .techName(".NETCore")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame8);
        Tech frame9 = Tech.builder()
                .techName("Torch")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame9);
        Tech frame10 = Tech.builder()
                .techName("PyTorch")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame10);
        Tech frame11 = Tech.builder()
                .techName("Flask")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame11);
        Tech frame12 = Tech.builder()
                .techName("React Native")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame12);
        Tech frame13 = Tech.builder()
                .techName("Flutter")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame13);
        Tech frame14 = Tech.builder()
                .techName("Hadoop")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame14);
        Tech frame15 = Tech.builder()
                .techName("Angular.js")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame15);
        Tech frame16 = Tech.builder()
                .techName("Apache Spark")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame16);
        Tech frame17 = Tech.builder()
                .techName("ASP")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame17);
        Tech frame18 = Tech.builder()
                .techName("Electron")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame18);
        Tech frame19 = Tech.builder()
                .techName("Ruby on Rails")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame19);
        Tech frame20 = Tech.builder()
                .techName("Struts")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame20);
        Tech frame21 = Tech.builder()
                .techName("Laravel")
                .techCategory("Framework")
                .build();
        techRepository.saveAndFlush(frame21);

        Tech db1 = Tech.builder()
                .techName("MySQL")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db1);
        Tech db2 = Tech.builder()
                .techName("Oracle")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db2);
        Tech db3 = Tech.builder()
                .techName("MariaDB")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db3);
        Tech db4 = Tech.builder()
                .techName("PostgreSQL")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db4);
        Tech db5 = Tech.builder()
                .techName("MongoDB")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db5);
        Tech db6 = Tech.builder()
                .techName("MS SQL")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db6);
        Tech db7 = Tech.builder()
                .techName("Redis")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db7);
        Tech db8 = Tech.builder()
                .techName("SQLite")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db8);
        Tech db9 = Tech.builder()
                .techName("AWS Aurora")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db9);
        Tech db10 = Tech.builder()
                .techName("Elasticsearch")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db10);
        Tech db11 = Tech.builder()
                .techName("DynamoDB")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db11);
        Tech db12 = Tech.builder()
                .techName("Firebase")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db12);
        Tech db13 = Tech.builder()
                .techName("Tibero")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db13);
        Tech db14 = Tech.builder()
                .techName("Hive")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db14);
        Tech db15 = Tech.builder()
                .techName("Cassandra")
                .techCategory("DataBase")
                .build();
        techRepository.saveAndFlush(db15);














    }
}
