import java.nio.file.Path;
import java.nio.file.Paths;

class Docgen {

    public static void main() {
        Path localSwaggerFile = Paths.get("/path/to/swagger.yaml");
        Path outputDirectory = Paths.get("build/asciidoc");

        Swagger2MarkupConverter.from(localSwaggerFile)
                .build()
                .toFolder(outputDirectory);
    }


}