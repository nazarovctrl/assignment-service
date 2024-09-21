package uz.ccrew.assignmentservice.config;

import uz.ccrew.assignmentservice.entity.User;
import uz.ccrew.assignmentservice.util.AuthUtil;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    private final AuthUtil authUtil;

    public AuditorAwareImpl(AuthUtil authUtil) {
        this.authUtil = authUtil;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        Optional<User> optional = authUtil.takeLoggedUser();
        return optional.map(User::getId);
    }
}