package base.utility.module.utilities.httpfilters;

import base.utility.module.utilities.SessionData;
import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.http.context.ServerRequestTracingPublisher;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;



@Filter("/api/**")
public class SessionDataFilter implements HttpServerFilter, Ordered {

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {

        HttpHeaders headers;
        String authorization, payload, workingString, username, companyCode, rolesString, sessionDataString;
        long expireToken;
        String languageCode;
        List<String> rolesList = new LinkedList<>();
        String[] chunks, rolesSplitted;
        SessionData sessionData;
        Base64.Decoder decoder = Base64.getUrlDecoder();

        if (request.getHeaders().contains("authorization")){
            headers = request.getHeaders();
            if (headers.contains("authorization")){
                authorization = headers.get("authorization");
                if (authorization != null) {
                    authorization = authorization.substring(7);
                    authorization = authorization.trim();
                    chunks = authorization.split("\\.");
                    payload = new String(decoder.decode(chunks[1]));
                    if (payload.contains("sessionData")) {
                        sessionDataString = payload.substring(payload.indexOf("sessionData") + 14);
                        sessionDataString = sessionDataString.substring(0, payload.indexOf('}'));
                        if (payload.contains("companyCode")) {
                            workingString = sessionDataString.substring(sessionDataString.indexOf("companyCode") + 14);
                            companyCode = workingString.substring(0, workingString.indexOf("\""));
                            if (payload.contains("languageCode")) {
                                workingString = payload.substring(payload.indexOf("languageCode") + 14);
                                languageCode = (workingString.substring(0, workingString.indexOf(",")));
                                languageCode = languageCode.replaceAll("\"", "");
                                if (payload.contains("expireToken")) {
                                    workingString = payload.substring(payload.indexOf("expireToken") + 13);
                                    expireToken = Long.parseLong(workingString.substring(0, workingString.indexOf(",")));
                                    if (payload.contains("username")) {
                                        workingString = payload.substring(payload.indexOf("username") + 11);
                                        username = workingString.substring(0, workingString.indexOf("\""));
                                        if (payload.contains("roles")) {
                                            rolesString = payload.substring(payload.indexOf("roles\":") + 8);
                                            rolesString = rolesString.substring(0, rolesString.indexOf("]"));
                                            rolesSplitted = rolesString.split(",\"");
                                            if (rolesSplitted[0].contains("R_")) {
                                                rolesList.addAll(Arrays.asList(rolesSplitted));
                                            }
                                            sessionData = new SessionData(username, companyCode, expireToken, languageCode, rolesList);
                                            request.setAttribute("sessionData", sessionData);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else {
            request.setAttribute("sessionData", new SessionData("Anonymous", "<empty>", null, "<empty>"));
        }
        return ServerRequestContext.with(request, (Supplier<Publisher<MutableHttpResponse<?>>>) () ->
                new ServerRequestTracingPublisher(request, chain.proceed(request))
        );

    }

}
