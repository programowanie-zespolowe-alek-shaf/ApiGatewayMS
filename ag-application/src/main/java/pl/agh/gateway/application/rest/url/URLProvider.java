package pl.agh.gateway.application.rest.url;

import pl.agh.gateway.application.rest.MicroService;

public interface URLProvider {

    String getBaseURL(MicroService microService);
}
