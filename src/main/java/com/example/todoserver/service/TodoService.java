package com.example.todoserver.service;

import com.example.todoserver.model.TodoEntity;
import com.example.todoserver.model.TodoRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@Service
@AllArgsConstructor
public class TodoService {

    //persistence.xml 에서 unit name과 통일
    EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hello");
//    private final TodoRepository todoRepository;


    // 람다식 인터페이스
    interface Expression {
        TodoEntity run(EntityManager em);
    }

    private TodoEntity transaction(Expression lambda) {
        // FactoryManager를 생성합니다. "JpaExample"은 persistence.xml에 쓰여 있는 이름이다.
        // Manager를 생성한다.
        TodoEntity entity = null;
                
        EntityManager em = this.emf.createEntityManager();
        try {
            // transaction을 가져온다.
            EntityTransaction tx = em.getTransaction();
            try {
                // transaction 실행
                tx.begin();
                // 람다식을 실행한다.
                entity  = lambda.run(em);
                // transaction을 커밋한다.
                tx.commit();
            } catch (Throwable e) {
                // 에러가 발생하면 rollback한다.
                if (tx.isActive()) {
                    tx.rollback();
                }
                // 에러 출력
                e.printStackTrace();
            }
        } finally {
// 각 FactoryManager와 Manager를 닫는다.
            em.close();
            emf.close();
            return entity;
        }
    }

    //   	todo 리스트 목록에 아이템을 추가
    public TodoEntity add(TodoRequest request) {


        return transaction((em) -> {
            TodoEntity todoEntity = new TodoEntity();
            todoEntity.setTitle(request.getTitle());
            todoEntity.setOrder(request.getOrder());
            todoEntity.setCompleted(request.getCompleted());
            em.persist(todoEntity);
            return todoEntity;
        });
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
