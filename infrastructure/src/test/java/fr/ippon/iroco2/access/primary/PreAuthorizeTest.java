/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.access.primary;

import fr.ippon.iroco2.common.TestSecurityUtils;
import fr.ippon.iroco2.config.TestContainersPostgresqlConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnabledIf(expression = "#{environment['clerk.secret.key'] != ''}", loadContext = true)
class PreAuthorizeTest extends TestContainersPostgresqlConfig {

    @Autowired
    TestSecurityUtils testSecurityUtils;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity(jwtAuthenticationFilter))
                .build();
    }

    @Test
    void admin_pre_authorize_should_return_200_when_user_has_admin_role() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.ADMIN.name());
        mockMvc
                .perform(get("/api/fakes/admin-api").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void admin_member_pre_authorize_should_return_200_when_user_has_admin_role() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.ADMIN.name());
        mockMvc
                .perform(get("/api/fakes/member-api").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void member_pre_authorize_should_return_200_when_user_has_member_role() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.MEMBER.name());
        mockMvc
                .perform(get("/api/fakes/member-api").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void admin_pre_authorize_should_return_403_when_user_has_member_role() throws Exception {
        String token = testSecurityUtils.buildJWTWithRole(SecurityRole.MEMBER.name());
        mockMvc
                .perform(get("/api/fakes/admin-api").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
