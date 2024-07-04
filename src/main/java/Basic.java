import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.eclipse.jetty.util.component.LifeCycle.start;

public class Basic {
    public static void main(String[] args) {
       // hello();
        //getStaticPage();
        createPerson();
    }
    static void hello(){
        var app = Javalin.create()
              .get("/hello",ctx->ctx.result("Hello World"))
              .get("/hello/{name}",
                      ctx->{ctx.result("Hello "+ctx.pathParam("name"));
                      ctx.status(202);
              })
              .get("/hello2/{name}",ctx->
                      ctx.json(new Person(ctx.pathParam("name"))))
              .get("/sum",ctx->{
                  int a=Integer.parseInt(ctx.queryParam("a"));
                  int b=Integer.parseInt(ctx.queryParam("b"));
                  ctx.result(String.valueOf(a+b));
              })                     
              .start(7070);
    }
 static void getStaticPage(){
           var app = Javalin.create(config->config.addStaticFiles("src/main/resources", Location.EXTERNAL))
                   .get("/", ctx->ctx.html(new String(Files.readAllBytes(Paths.get("src/main/resources/hello.html")))))
                   .get("/hello",ctx->ctx.html("<h1>Hello World<h1>"))
                   .start(7070);
    }
 static void createPerson(){
        Set<Person> persons=new HashSet<>();
               var app = Javalin.create(config->config.enableCorsForAllOrigins())
                       .post("/person",ctx-> {
                           persons.add(ctx.bodyAsClass(Person.class));
                           ctx.status(201);
                       })
                       .get("/hello",ctx->ctx.html("<h1>Hello World<h1>"))                                                 
                       .start(7070);                                                                                       

    }
 }
class Person{
    private  String name;

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
}
