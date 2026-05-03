package ie.stephen.pubquiz;

import ie.stephen.pubquiz.repository.QuestionRepository;
import ie.stephen.pubquiz.service.TriviaFetchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitialiser implements CommandLineRunner {

    private final TriviaFetchService triviaFetchService;
    private final QuestionRepository questionRepository;

    public DataInitialiser(TriviaFetchService triviaFetchService,
                           QuestionRepository questionRepository) {
        this.triviaFetchService = triviaFetchService;
        this.questionRepository = questionRepository;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        int count = questionRepository.count();
        if (count < 200) {
            System.out.println("Fetching initial questions...");
            triviaFetchService.fetchAndStore(50, "easy");
            Thread.sleep(6000);
            triviaFetchService.fetchAndStore(50, "medium");
            Thread.sleep(6000);
            triviaFetchService.fetchAndStore(50, "hard");
            Thread.sleep(6000);
            triviaFetchService.fetchAndStore(50, null);
            System.out.println("Done. Total: " + questionRepository.count());
        } else {
            System.out.println("Database has " + count + " questions, skipping fetch.");
        }
    }
}