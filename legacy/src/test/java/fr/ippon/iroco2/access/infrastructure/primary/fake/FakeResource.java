package fr.ippon.iroco2.access.infrastructure.primary.fake;

import fr.ippon.iroco2.common.presentation.security.IsAdmin;
import fr.ippon.iroco2.common.presentation.security.IsMember;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fakes")
public class FakeResource {

    @IsAdmin
    @GetMapping("/admin-api")
    public void adminApi() {}

    @IsMember
    @GetMapping("/member-api")
    public void memberApi() {}
}
