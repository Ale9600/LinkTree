package base.foundation.module.authentication;

import base.foundation.module.coreentities.company.Company;
import base.foundation.module.coreentities.company.CompanyRepository;
import base.foundation.module.coreentities.language.Language;
import base.foundation.module.coreentities.language.LanguageRepository;
import base.foundation.module.coreentities.settings.Settings;
import base.foundation.module.coreentities.settings.SettingsRepository;
import base.foundation.module.coreentities.user.UserRepository;
import base.foundation.module.coreentities.user.links.role.UserRoleLinkRepository;
import base.utility.module.utilities.SessionData;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.*;

import static base.foundation.module.FoundationConstants.KEY_DEFAULT_LANGUAGE;
import static java.util.Objects.isNull;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    @Inject
    SettingsRepository settingsRepository;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    UserRoleLinkRepository userRoleLinkRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    CompanyRepository companyRepository;

    @Inject
    LanguageRepository languageRepository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {
        Optional<UsernamePasswordCredentials> usernamePasswordCredentials;
        List<String> roles = new ArrayList<>();
        Map<String, Object> attributes = new HashMap<>();
        usernamePasswordCredentials = Objects.requireNonNull(httpRequest).getBody(UsernamePasswordCredentials.class);

        if (httpRequest.getBody(UsernamePasswordCredentials.class).isPresent()){
            return Mono.create(emitter -> {
                usernamePasswordCredentials.ifPresentOrElse(
                        credentials -> {
                            userRepository.findByUsername(credentials.getUsername()).ifPresentOrElse(
                                    user -> {
                                        String languageCode, companyCode = "";
                                        Optional<Language> optionalLanguage = Optional.empty();
                                        Optional<Company> optionalCompany = Optional.empty();
                                        Optional<Settings> optionalSettings;
                                        userRoleLinkRepository.findByUser(user).forEach( (it)-> {
                                                    roles.add(it.getRole().getCode());
                                                }
                                                );
                                        if (!isNull(user.getLanguage())){
                                            optionalLanguage = languageRepository.findById(user.getLanguage().getId());
                                        }
                                        if (optionalLanguage.isPresent()){
                                            languageCode = optionalLanguage.get().getCode();
                                        } else {
                                            optionalSettings = settingsRepository.findByKey(KEY_DEFAULT_LANGUAGE);
                                            if (optionalSettings.isEmpty()){
                                                languageCode = "ENG";
                                            } else {
                                                languageCode = optionalSettings.get().getValue();
                                            }
                                        }
                                        if (!isNull(user.getCompany())){
                                            optionalCompany = companyRepository.findById(user.getCompany().getId());
                                        }
                                        if (optionalCompany.isPresent()){
                                            companyCode = optionalCompany.get().getCode();
                                        }
                                        SessionData sessionData = new SessionData(user.getUsername(), companyCode, System.currentTimeMillis() + 3600000, languageCode);
                                        attributes.put("sessionData", sessionData);
                                        String password = authenticationService.hashPassword(credentials.getPassword(), credentials.getUsername().toUpperCase());
                                        if (user.getPassword().equals(password)){
                                            emitter.success(AuthenticationResponse.success((String) authenticationRequest.getIdentity(), roles, attributes));
                                        }else {
                                            emitter.success(AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
                                        }
                                    },
                                    () ->{
                                        emitter.success(AuthenticationResponse.failure(AuthenticationFailureReason.USER_NOT_FOUND));
                                    }
                            );
                        },
                        ()->{
                            emitter.success(AuthenticationResponse.failure(AuthenticationFailureReason.CUSTOM));
                        }
                );
            });
        }
        return Mono.error(new AuthenticationNoCredentialsException());
    }
}