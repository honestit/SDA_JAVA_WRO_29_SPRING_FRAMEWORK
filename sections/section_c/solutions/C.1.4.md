Klasę konfiguracyjną `JpaConfig` umieszczamy w pakiecie `pl.honestit.spring.kb.config`. Klasa oznaczona została adnotacjami:
- `@Configuration`, aby mogła pełnić rolę konfiguracji Spring'owej i udostępniać bean'y,
- `@ComponentScan` ze wskazaniem pakietu `pl.honestit.spring.kb.data`, aby umożliwiała automatyczne wykrywanie komponentów związanych z warstwamy dostępu do danych i modelu danych,
- `@EnableTransactionManagement`, aby aktywować zarządzanie transakcjami i umożliwić wykorzystanie adnotacji `@Transactional` na komponentach typuw `@Repository` czy `@Service`,
- `@EnableJpaRepositories` ze wskazaniem pakietu `pl.honestit.spring.kb.data.repositories`, aby umożliwić wykorzystanie repozytoriów JPA i tworzenie ich implementacji na podstawie naszych interfejsów.

Podstawowy kod klas z adnotacjami konfiguracyjnymi:

```java
package pl.honestit.spring.kb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@ComponentScan(basePackages = "pl.honestit.spring.kb.data")
@EnableJpaRepositories(basePackages = "pl.honestit.spring.kb.data.repositories")
@EnableTransactionManagement
public class JpaConfig {

}
```

W klasie `JpaConfig` poza adnotacjami dodajemy również udostępnienie dwóch bean'ów związanych z zarządzaniem transakcjami oraz konfiguracją fabryki dla klasy `EntityManager`. Pamiętaj, że komponenty udostępniane są poprzez swój typ oraz nazwę, a w przypadku metod z adnotacją `@Bean` nazwa komponentu wynika z nazwy metody. Stąd trzeba zachować metody o podanych nazwach.

Metoda udostępniająca fabrykę encji:

```java
    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        LocalEntityManagerFactoryBean entityManagerFactory = new LocalEntityManagerFactoryBean();
        
        /* Ustawiamy nazwę jednostki trwałej, która potem musi być użyta w pliku konfiguracyjnym
           persistence.xml
         */
        entityManagerFactory.setPersistenceUnitName("knowledgePU");
        return entityManagerFactory;
    }
```

Metoda udostępniająca menedżera transakcji:

```java
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
        return transactionManager;
    }
```

Pełen kod klasy `JpaConfig` z obiema metodami:

```java
package pl.honestit.spring.kb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@ComponentScan(basePackages = "pl.honestit.spring.kb.data")
@EnableJpaRepositories(basePackages = "pl.honestit.spring.kb.data.repositories")
@EnableTransactionManagement
public class JpaConfig {

    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        LocalEntityManagerFactoryBean entityManagerFactory = new LocalEntityManagerFactoryBean();
        entityManagerFactory.setPersistenceUnitName("knowledgePU");
        return entityManagerFactory;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
        return transactionManager;
    }
}
```

---

Drugi element zadania, to poprawny plik `persistence.xml`. Plik ten definiuje zestaw jednostek trwałych (Persistence Unit), z którymi potem powiązany jest obiekty klasy `EntityManager`. Nazwa jednostki trwałej, którą konfigurujemy, musi być zgodna z nazwa, której użyliśmy w bean'ie typu `LocalEntityManagerFactoryBean`.

Ze względu na to, że zostały udostępnione skrypty tworzące struktury na bazie danych, to wybierzemy opcję, aby hibernate walidował poprawność naszych encji względem bazy danych (`hbm2ddl` na wartość `validate`). W ten sposób będziemy mieli pewność, że model obiektowy w 100% jest zgodny z modelem relacyjnym.

Treść pliku `persistence.xml`:

```xml
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd"
             version="2.1">

    <persistence-unit name="knowledgePU">
        <properties>
            <property name="javax.persistence.jdbc.url"
                      value="jdbc:mysql://localhost:3306/knowledge_db?serverTimezone=UTC&amp;useSSL=false&amp;allowPublisKeyRetrieval=true"/>
            <!--Zmienić nazwę użytkownika na swojego-->
            <property name="javax.persistence.jdbc.user" value="root"/>
            <!--Zmienić hasło użytkownika na swoje-->
            <property name="javax.persistence.jdbc.password" value="pass"/>
            <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
            <property name="hibernate.connection.useUnicode" value="true"/>
            <property name="hibernate.connection.characterEncoding" value="utf8"/>
            <property name="hibernate.connection.CharSet" value="utf8"/>
        </properties>
    </persistence-unit>

</persistence>
```

Należy pamietać, że do zadania dołączone są również skrypty inicjalizujące tabele na bazie oraz dodające dane testowe. Oba skrypty zostały zapisane w składni zgodnej z MySQL.