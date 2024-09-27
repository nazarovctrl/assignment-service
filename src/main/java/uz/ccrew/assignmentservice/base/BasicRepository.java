package uz.ccrew.assignmentservice.base;

import uz.ccrew.assignmentservice.exp.NotFoundException;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.jpa.repository.JpaRepository;

@NoRepositoryBean
public interface BasicRepository<T, D> extends JpaRepository<T, D> {
    default T loadById(D id,String expMessage) {
        return findById(id).orElseThrow(() -> new NotFoundException(expMessage));
    }
}