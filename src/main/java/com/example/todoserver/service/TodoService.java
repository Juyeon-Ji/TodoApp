package com.example.todoserver.service;

import com.example.todoserver.model.TodoEntity;
import com.example.todoserver.model.TodoRequest;
import com.example.todoserver.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

@Service
@AllArgsConstructor
public class TodoService {

    //persistence.xml 에서 unit name과 통일
    EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hello");
//    private final TodoRepository todoRepository;

    //   	todo 리스트 목록에 아이템을 추가
    public TodoEntity add(TodoRequest request) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜젝션 시작

        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTitle(request.getTitle());
        todoEntity.setOrder(request.getOrder());
        todoEntity.setCompleted(request.getCompleted());
        // <S extends T> S save(S entity);
        // save는 제네릭으로 받은 타입(T)으로 값을 반환
        try {
            em.persist(todoEntity);
            tx.commit();
//            em.flush();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
        return todoEntity;
//        return this.todoRepository.save(todoEntity);
    }

    //   	todo  리스트 목록 중 특정 아이템을 조회
    public TodoEntity searchById(Long id) {
        EntityManager em = emf.createEntityManager();
        TodoEntity findTodo = em.find(TodoEntity.class, id);
        try {

            if (findTodo.getId() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }

        return findTodo;

//        return this.todoRepository.fin dById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    //   	todo 리스트 목록 중 특정 아이템을 수정
    public TodoEntity updateById(Long id, TodoRequest request) {
        TodoEntity todoEntity = this.searchById(id);

        EntityManager em = emf.createEntityManager();
        TodoEntity findTodo = em.find(TodoEntity.class, id);
        try {

//            if (findTodo.getId().equals(null)) {
//                new ResponseStatusException(HttpStatus.NOT_FOUND);
//            }

            if (request.getTitle() != null) {
                findTodo.setTitle(request.getTitle());
            }
            if (request.getOrder() != null) {
                findTodo.setOrder(request.getOrder());
            }
            if (request.getCompleted() != null) {
                findTodo.setCompleted(request.getCompleted());
            }

        } catch (Exception e) {
            throw e;
//            tx.rollback();
        } finally {
            em.close();
        }

        return findTodo;
        //        return this.todoRepository.save(todoEntity);
    }

    //   	todo 리스트 목록 중 특정 아이템을 삭제
    public void deleteById(Long id) {
        EntityManager em = emf.createEntityManager();
        TodoEntity findTodo = em.find(TodoEntity.class, id);
        em.remove(findTodo);
//        this.todoRepository.deleteById(id);
    }

}