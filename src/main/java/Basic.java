import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.eclipse.jetty.util.component.LifeCycle.start;

public class Basic {
    public static void main(String[] args) {
        // hello();
        //getStaticPage();
        //createPerson();
        //createPersonForm();
        //changePerson();
        //deletePerson();
        inputPersonFormHeader();
    }

    static void hello() {
        var app = Javalin.create()
                .get("/hello", ctx -> ctx.result("Hello World"))
                .get("/hello/{name}",
                        ctx -> {
                            ctx.result("Hello " + ctx.pathParam("name"));
                            ctx.status(202);
                        })
                .get("/hello2/{name}/{surname}", ctx ->
                        ctx.json(new Person(ctx.pathParam("name"), "surname")))
                .get("/sum", ctx -> {
                    int a = Integer.parseInt(ctx.queryParam("a"));
                    int b = Integer.parseInt(ctx.queryParam("b"));
                    ctx.result(String.valueOf(a / b));
                })
                .start(7070);
    }

    static void getStaticPage() {
        var app = Javalin.create(config -> config.addStaticFiles("src/main/resources", Location.EXTERNAL))
                .get("/", ctx -> ctx.html(new String(Files.readAllBytes(Paths.get("src/main/resources/hello.html")))))
                .get("/hello", ctx -> ctx.html("<h1>Hello World<h1>"))
                .start(7070);
    }

    static void createPerson() {
//         /*
//            fetch('http://localhost:7070/person/2', {
//            method: 'PUT',
//            headers: {
//              'Accept': 'application/json',
//              'Content-Type': 'application/json'
//            },
//            body: JSON.stringify({"name": "Ivan", "surname": "Ivanov"})
//          });
//         */
        Set<Person> persons = new HashSet<>();
        persons.add(new Person(1, "name", "surname"));
        persons.add(new Person(2, "name2", "surname2"));
        var app = Javalin.create(config -> config.enableCorsForAllOrigins())
                .get("/", ctx -> ctx.html(new String(Files.readAllBytes(Paths.get("src/main/resources/form.html")))))
                .post("/person", ctx -> {
                    Person person = new Person(ctx.formParam("name"), ctx.formParam("surname "));
                    persons.add(person);
                    persons.add(ctx.bodyAsClass(Person.class));
                    ctx.status(201);


                    // persons.stream().forEach(System.out::println);
                })
                .start(7070);
    }

    static void createPersonForm() {
        Set<Person> persons = new HashSet<>();
        //persons.add(new Person("name","surname"));


        var app = Javalin.create(config -> config.enableCorsForAllOrigins())
                .get("/", ctx -> ctx.html(new String(Files.readAllBytes(Paths.get("src/main/resources/form.html")))))
                .post("/person", ctx -> {
                    Person person = new Person(ctx.formParam("name"), ctx.formParam("surname "));
                    persons.add(person);
                    persons.add(ctx.bodyAsClass(Person.class));
                    ctx.status(201);
                    ctx.redirect("/");
                    persons.stream().forEach(System.out::println);

                    // persons.stream().forEach(System.out::println);
                })
                .start(7070);
    }

    static void changePerson() {

        /*
            fetch('http://localhost:7070/person/3', {
            method: 'PUT',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({"name": "Ivan", "surname": "Ivanov"})
          });
         */
        Set<Person> persons = new HashSet<>();
        persons.add(new Person(-1, "name", "surname"));
        persons.add(new Person(2, "name2", "surname2"));
        var app = Javalin.create(config -> config.enableCorsForAllOrigins())
                .put("/person/{id}", ctx -> {
                    Optional<Person> person =
                            persons.stream
                                            ()
                                    .filter(p -> p.getId() == Long.parseLong(ctx.pathParam("id")))
                                    .findFirst();
                    Person personNew = ctx.bodyAsClass(Person.class);
                    if (person.isPresent()) {
                        person.get().setName(personNew.getName());
                        person.get().setSurname(personNew.getSurname());
                        ctx.status(201);
                    } else ctx.status(404);

                    persons.stream
                            ().forEach(System.out::println);
                })
                .start(7070);
    }
    static void deletePerson(){
        /*
            fetch('http://localhost:7070/person/1', {
            method: 'DELETE'
          });
         */
        Set<Person> persons = new HashSet<>();
        persons.add(new Person(1,"name","surname"));
        persons.add(new Person(2,"name2","surname2"));
        var app = Javalin.create(config -> config.enableCorsForAllOrigins())
                .delete("/person/{id}", ctx -> {
                    Optional<Person> person =
                            persons.stream
                                            ()
                                    .filter(p->p.getId()==Long.parseLong(ctx.pathParam("id")))
                                    .findFirst();
                    persons.remove(person.get());

                    persons.stream
                            ().forEach(System.out::println);
                })
                .start(7070);
    }
    //Передача данных через headers
    static void inputPersonFormHeader() {
     //   fetch('http://localhost:7070/person/2', {
//            method: 'GET',
//            headers: {
//              'Accept': 'application/json',
//              'Content-Type': 'application/json'
        //      'Person': 'name:
//            },
//            body: JSON.stringify({"name": "Ivan", "surname": "Ivanov"})
//          });
        Set<Person> persons = new HashSet();
        persons.add(new Person("default","default"));
        ObjectMapper mapper = new ObjectMapper();

        var app = Javalin.create(config ->
                        config.enableCorsForAllOrigins())
                .get("/form", ctx -> {
                    ctx.html(new String(Files.readAllBytes(Paths.get("src\\main\\resources\\formListsHeader.html"))));
                    ctx.header("persons", mapper.writeValueAsString(persons));
                })
                .start(7070);
    }
}

        class Person{
    public long getId() {
        return id;
    }

    private long id;
    private  String name;

    public Person() {
    }

    public Person(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    private String surname;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }



    public Person(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
