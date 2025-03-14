package study.spring_board_V2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "board")
public class Board {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne(cascade = CascadeType.PERSIST)  // 필요에 따라 추가
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

}