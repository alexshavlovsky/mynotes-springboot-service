package com.ctzn.mynotesservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserAdminResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer roles;
    private Date createdOn;
    private Date lastSeenOn;
    private Boolean enabled;
}
