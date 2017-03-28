package com.octaviannita.ui;

import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.time.Year;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 28, 2017
 */
@Title("[SO Question #43026546]")
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final TextField nameField = new TextField("Type the person’s name here:");
        final TextField yearOfBirthField = new TextField("Type the year of birth here:");

        final TextField ageField = new TextField("Approximate age:");
        ageField.setReadOnly(true);

        final Label beanToString = new Label();

        final Binder<Person> binder = new Binder<>();
        binder.forField(nameField).bind(Person::getName, Person::setName);
        binder.forField(yearOfBirthField).withConverter(new StringToIntegerConverter("Input must be Integer"))
              .bind(Person::getYearOfBirth, Person::setYearOfBirth);
        binder.forField(ageField).withConverter(new StringToIntegerConverter("")).bind(Person::getAge, null);

        final Person person = new Person("Jean-Luc", 1955);
        binder.setBean(person);

        final Button button = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                binder.readBean(binder.getBean());
                beanToString.setValue(person.toString());
            } else {
                beanToString.setValue("The Person bean has invalid state.");
            }
        });

        this.setContent(new VerticalLayout(nameField, yearOfBirthField, ageField, button, beanToString));
    }

    static class Person {

        private String name;

        private Integer yearOfBirth;

        Person() {}

        Person(String name, Integer yearOfBirth) {
            this.name = name;
            this.yearOfBirth = yearOfBirth;
        }

        String getName() { return name; }

        void setName(String name) { this.name = name; }

        Integer getYearOfBirth() { return yearOfBirth; }

        void setYearOfBirth(Integer yearOfBirth) { this.yearOfBirth = yearOfBirth; }

        Integer getAge() { return Year.now().getValue() - yearOfBirth; }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", yearOfBirth=" + yearOfBirth + ", age=" + getAge() + '}';
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {}
}
