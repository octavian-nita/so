package com.octaviannita.ui;

import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 24, 2017
 */
@Title("Backoffice for SO Question #42927030")
public class MainUI extends UI {

    private Cookie login(URI targetUri, String loginPath, Map<String, String> params) throws IOException {
        requireNonNull(targetUri);
        requireNonNull(loginPath);

        // Keep track of cookies we might receive in an HttpClient:
        final CookieStore cookies = new BasicCookieStore();

        // Build and work with an (AutoCloseable) HttpClient that uses the cookie store:
        try (CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookies).build()) {

            // Prepare (login) request parameters:
            List<NameValuePair> reqParams = new ArrayList<>();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    reqParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }

            // Execute the login (POST) request with the given parameters:
            HttpPost post = new HttpPost(targetUri + loginPath);
            post.setEntity(new UrlEncodedFormEntity(reqParams));

            CloseableHttpResponse response = client.execute(post);
            // Eventually, check the response to see if successful
            response.close();

            // Look for a JSESSIONID-named cookie stored in the HttpClient and return it to be used by calling code:
            for (org.apache.http.cookie.Cookie cookie : cookies.getCookies()) {
                if ("JSESSIONID".equalsIgnoreCase(cookie.getName())) {

                    String domain = targetUri.getHost();
                    if (domain.startsWith("www.")) {
                        domain = domain.substring(4);
                    }

                    Cookie authCookie = new Cookie(cookie.getName(), cookie.getValue());
                    authCookie.setDomain(domain);
                    authCookie.setPath("/");
                    // Eventually, set expiry (to allow longer login) and other things...

                    return authCookie;
                }
            }

            return null; // some sort of error?
        }
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new VerticalLayout(new Button("Log into site...", event -> {

            try {
                URI targetUri = new URI("http://localhost:8080");

                Map<String, String> params = new HashMap<>();
                params.put("username", "Johnny");
                params.put("password", "incorrect :)");
                // Eventual hidden fields, etc.
                // params.put("...", "...");

                Cookie targetAuthCookie = login(targetUri, "/log-me-in", params);

                // We're not ready just yet: we still need to 'transfer' the cookie
                // the HTTP client received to the current browser:
                VaadinService.getCurrentResponse().addCookie(targetAuthCookie);

                // Upon responding to the Vaadin 'click' request, open the target URL (eventually in a new page / tab):
                Page.getCurrent().getJavaScript().execute("window.open('" + targetUri + "');");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        })));
    }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {}
}
