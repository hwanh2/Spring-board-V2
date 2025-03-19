package study.spring_board_V2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    private Board board;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    private LocalDateTime createdAt = LocalDateTime.now();
    public Comment(){};
    public Comment(String content,Member member,Board board){
        this.content = content;
        this.member = member;
        this.board = board;
    }
}
