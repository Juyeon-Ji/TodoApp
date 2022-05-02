package com.example.todoserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//데이터베이스로부터 응답과 관련된 클래스
@Data
@NoArgsConstructor //파라미터가 없는 생성자 생성
@AllArgsConstructor // 클래스에 존재하는 모든필에 대한 생성자 생성
public class TodoResponse {
    private Long id;
    private String title;
    private Long order;
    private Boolean completed;
    private String url;

    public TodoResponse(TodoEntity todoEntity) {
        this.id = todoEntity.getId();
        this.title = todoEntity.getTitle();
        this.order = todoEntity.getOrder();
        this.completed = todoEntity.getCompleted();

        this.url = "http://localhost:8080/" + this.id;
    }
}
