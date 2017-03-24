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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 24, 2017
 */
@Title("Backoffice for SO Question #42927030")
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new VerticalLayout(new Button("Log into site...", event -> {

            final String site = "http://localhost:8080";

            // Store the cookies we might receive:
            CookieStore cookies = new BasicCookieStore();

            // Build an HTTP client:
            CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookies).build();

            // Prepare a POST request and log in parameters:
            HttpPost post = new HttpPost(site + "/log-me-in");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", "Johnny"));
            params.add(new BasicNameValuePair("password", "passwd"));

            try {

                // Execute the log in (POST) request with the required parameters:
                post.setEntity(new UrlEncodedFormEntity(params));
                CloseableHttpResponse response = client.execute(post);

                // Look for a JSESSIONID-named cookie and add it to the response:
                for (org.apache.http.cookie.Cookie cookie : cookies.getCookies()) {
                    if ("JSESSIONID".equalsIgnoreCase(cookie.getName())) {

                        VaadinService.getCurrentResponse().addCookie(new Cookie(cookie.getName(), cookie.getValue()));
                        Page.getCurrent().getJavaScript().execute("window.open('" + site + "');");

                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        })));
    }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {}
}
