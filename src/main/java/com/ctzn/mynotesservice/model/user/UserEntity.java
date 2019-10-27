package com.ctzn.mynotesservice.model.user;

import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    Long id;

    @Column(nullable = false, length = 36, unique = true)
    String userId = UserIdFactory.produce();

    @Column(nullable = false, length = 50)
    @NonNull
    private String firstName;

    @Column(length = 50)
    @NonNull
    private String lastName;

    @Column(nullable = false, length = 50, unique = true)
    @NonNull
    private String email;

    @Column(nullable = false, length = 60)
    private String encodedPassword;

    @Column(nullable = false)
    private Integer roles = 0;

    @Column(nullable = false)
    @NonNull
    private Date createdOn = TimeSource.now();

    @Column
    private Date lastSeenOn = null;

    @Column(nullable = false)
    @NonNull
    private Boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<NotebookEntity> notebooks = new ArrayList<>();

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setPassword(String rawPassword) {
        encodedPassword = UserPasswordEncoder.encode(rawPassword);
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = UserRole.rolesToMask(roles);
    }

}
