package kz.shakenov.gitlab.reviewer;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Entry point for the GitLab Reviewer application.
 * <p>
 * This class bootstraps the Quarkus application using {@link Quarkus#run(String...)}.
 * It is annotated with {@code @QuarkusMain} to allow running the application
 * with a custom main method (useful for CLI tools or debugging).
 */
@QuarkusMain
public class Application {

    /**
     * Main method that launches the Quarkus application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String... args) {
        Quarkus.run(args);
    }
}
