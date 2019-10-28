package com.ctzn.mynotesservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserAdminRequest {
    private Integer roles;
    private Boolean enabled;
}
