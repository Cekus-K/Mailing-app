package pl.wsb.massmailing.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wsb.massmailing.dao.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact findByEmailAddress(String emailAddress0);
}
