import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

public class Persons {
    public  static Set<Person> persons=new HashSet<>();
    public static void main(String[] args) {
        //создать страницу где выводится список персон
        //имеется форма для добавления нового персон
        // и способ удаления персон
        persons.add(new Person(1,"Ivan", "Ivanov"));
        persons.add(new Person(2,"John", "Doe"));
        generatePage(persons);
        Path target = Paths.get("src\\main\\resources\\temp.html");

        var app = Javalin.create(config -> config.addStaticFiles("src/main/resources", Location.EXTERNAL))
                .get("/", ctx -> ctx.html(new String(Files.readAllBytes(Paths.get("src\\main\\resources\\temp.html")))))
                .get("/api/persons",ctx ->
                        ctx.json(persons))
                .start(7070);
    }
    //Сгенерировать страницу, выполнив подстановку
    static void generatePage(Set<Person> persons) {
        //Сделать подстановку в файле html
        //Скопировать исходный файл во временный файл
        Path sourceFile = Paths.get("src\\main\\resources\\persons.html");
        Path targetFile = Paths.get("src\\main\\resources\\temp.html");
        try {
            Files.copy(sourceFile, targetFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
        }

        //Прочитать временный файл
        Charset charset = StandardCharsets.UTF_8;
        //Заменить в нем строку
        // persons to json
        ObjectMapper mapper = new ObjectMapper();
        String content = null;
        try {
            content = new String(Files.readAllBytes(targetFile), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            content = content.replace("`${persons}`", mapper.writeValueAsString(persons));

//System.out.println(mapper.writeValueAsString(persons));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            Files.write(targetFile, content.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


