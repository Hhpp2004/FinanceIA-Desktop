package financas.bb.com.Service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ShutdownListener implements ApplicationListener<ContextClosedEvent>{
    private final SessionManager sm;

    public ShutdownListener(SessionManager sm) {
        this.sm = sm;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        sm.logout();
    }    
}
