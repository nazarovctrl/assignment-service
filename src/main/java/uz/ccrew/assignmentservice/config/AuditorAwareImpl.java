package uz.ccrew.assignmentservice.config;

import uz.ccrew.assignmentservice.user.User;
import uz.ccrew.assignmentservice.base.AuthUtil;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<User> {
    private final AuthUtil authUtil;

    public AuditorAwareImpl(AuthUtil authUtil) {
        this.authUtil = authUtil;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        return authUtil.takeLoggedUser();
    }
}