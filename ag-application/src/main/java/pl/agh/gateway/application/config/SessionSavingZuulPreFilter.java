package pl.agh.gateway.application.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.cookie.SM.COOKIE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class SessionSavingZuulPreFilter extends ZuulFilter {

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpSession session = request.getSession(true);

        List<String> cookies = Collections.list(request.getHeaders(COOKIE));
        if (session != null && cookies.stream().noneMatch(c -> c.startsWith("SESSION="))) {
            context.addZuulRequestHeader(COOKIE, "SESSION=" + session.getId());
        }
        cookies = Collections.list(request.getHeaders(AUTHORIZATION));
        if (session != null && !cookies.isEmpty()) {
            context.addZuulRequestHeader(AUTHORIZATION, request.getHeaders(AUTHORIZATION).nextElement());
        }

        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 5;
    }
}
