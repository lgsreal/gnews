package com.gnews.fake.config;

import com.gnews.fake.domain.Article;
import com.gnews.fake.domain.Source;
import com.gnews.fake.repository.ArticleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

        private final ArticleRepository articleRepository;
        private final Random random = new Random();

        public DataInitializer(ArticleRepository articleRepository) {
                this.articleRepository = articleRepository;
        }

        @Override
        public void run(String... args) throws Exception {
                List<Article> articles = new ArrayList<>();

                String[] categories = { "technology", "business", "sports", "health", "science", "entertainment",
                                "general",
                                "nation", "world" };
                String[] countries = { "us", "uk", "br", "au", "ca", "in" };
                String[] languages = { "en", "pt" }; // Focus on English for simplicity and consistency

                // Image mapping by category
                String[] techImages = {
                                "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=800&q=80",
                                "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?auto=format&fit=crop&w=800&q=80"
                };
                String[] businessImages = {
                                "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=800&q=80",
                                "https://images.unsplash.com/photo-1507679799987-c73779587ccf?auto=format&fit=crop&w=800&q=80"
                };
                String[] sportsImages = {
                                "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?auto=format&fit=crop&w=800&q=80",
                                "https://images.unsplash.com/photo-1471295253337-3ceaaedca402?auto=format&fit=crop&w=800&q=80"
                };
                String[] healthImages = {
                                "https://images.unsplash.com/photo-1505751172876-fa1923c5c528?auto=format&fit=crop&w=800&q=80",
                                "https://images.unsplash.com/photo-1532938911079-1b06ac7ceec7?auto=format&fit=crop&w=800&q=80"
                };
                String[] generalImages = {
                                "https://images.unsplash.com/photo-1495020689067-958852a7765e?auto=format&fit=crop&w=800&q=80",
                                "https://images.unsplash.com/photo-1504711434969-e33886168f5c?auto=format&fit=crop&w=800&q=80"
                };

                for (int i = 0; i < 500; i++) {
                        String category = categories[random.nextInt(categories.length)];
                        // Increase weight for 'br' to ensuring sufficient PT content for demo
                        String country = random.nextInt(100) < 40 ? "br" : countries[random.nextInt(countries.length)];
                        // Se o país for BR, força idioma PT, caso contrário randomiza ou mantém EN
                        String lang = "br".equals(country) ? "pt" : languages[0];

                        String image = switch (category) {
                                case "technology", "science" -> techImages[random.nextInt(techImages.length)];
                                case "business" -> businessImages[random.nextInt(businessImages.length)];
                                case "sports" -> sportsImages[random.nextInt(sportsImages.length)];
                                case "health" -> healthImages[random.nextInt(healthImages.length)];
                                default -> generalImages[random.nextInt(generalImages.length)];
                        };

                        String sanitizedCategory = category.substring(0, 1).toUpperCase() + category.substring(1);

                        String title;
                        String description;
                        String content;

                        if ("pt".equals(lang)) {
                                String ptCategory = switch (category) {
                                        case "technology" -> "Tecnologia";
                                        case "business" -> "Negócios";
                                        case "sports" -> "Esportes";
                                        case "health" -> "Saúde";
                                        case "science" -> "Ciência";
                                        case "entertainment" -> "Entretenimento";
                                        case "general" -> "Geral";
                                        case "nation" -> "Nacional";
                                        case "world" -> "Mundo";
                                        default -> category;
                                };

                                title = "Notícia de " + ptCategory + ": Grande Evento #" + (i + 1);
                                description = "Esta é uma descrição simulada para o evento de "
                                                + ptCategory.toLowerCase() + " número " + (i + 1)
                                                + ". Trazendo as últimas atualizações do cenário brasileiro e mundial.";
                                content = "Conteúdo completo da notícia simulada em português. Detalhes sobre o evento, repercussão e análises de especialistas.";
                        } else {
                                title = sanitizedCategory + " News Request: Major event #" + (i + 1);
                                description = "This is a simulated description for the " + category
                                                + " news event number " + (i + 1) + ".";
                                content = "Full content of the article goes here. It provides successful simulation of real news content structure.";
                        }

                        articles.add(new Article(
                                        UUID.randomUUID().toString(),
                                        title,
                                        description,
                                        content,
                                        "https://news.example.com/articles/" + (i + 1),
                                        image,
                                        LocalDateTime.now().minusHours(random.nextInt(24 * 30)), // Random time in last
                                                                                                 // 30 days
                                        lang,
                                        category,
                                        new Source(
                                                        "source-" + i,
                                                        "The Mock Times " + country.toUpperCase(),
                                                        "https://mocktimes." + country,
                                                        country)));
                }

                articleRepository.saveAll(articles);
                System.out.println("Initialized " + articles.size() + " fake articles.");
        }
}
