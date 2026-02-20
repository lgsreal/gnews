package com.gnews.fake.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
public class Article {

        @Id
        private String id;
        private String title;

        @Column(length = 1000)
        private String description;

        @Column(columnDefinition = "TEXT")
        private String content;

        private String url;
        private String image;
        private LocalDateTime publishedAt;
        private String lang;
        private String category; // Internal use for filtering

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "source_id")
        private Source source;

        public Article() {
        }

        public Article(String id, String title, String description, String content, String url, String image,
                        LocalDateTime publishedAt, String lang, String category, Source source) {
                this.id = id;
                this.title = title;
                this.description = description;
                this.content = content;
                this.url = url;
                this.image = image;
                this.publishedAt = publishedAt;
                this.lang = lang;
                this.category = category;
                this.source = source;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public String getImage() {
                return image;
        }

        public void setImage(String image) {
                this.image = image;
        }

        public LocalDateTime getPublishedAt() {
                return publishedAt;
        }

        public void setPublishedAt(LocalDateTime publishedAt) {
                this.publishedAt = publishedAt;
        }

        public String getLang() {
                return lang;
        }

        public void setLang(String lang) {
                this.lang = lang;
        }

        public String getCategory() {
                return category;
        }

        public void setCategory(String category) {
                this.category = category;
        }

        public Source getSource() {
                return source;
        }

        public void setSource(Source source) {
                this.source = source;
        }

        // Compatibility methods
        public String id() {
                return id;
        }

        public String title() {
                return title;
        }

        public String description() {
                return description;
        }

        public String content() {
                return content;
        }

        public String url() {
                return url;
        }

        public String image() {
                return image;
        }

        public LocalDateTime publishedAt() {
                return publishedAt;
        }

        public String lang() {
                return lang;
        }

        public String category() {
                return category;
        }

        public Source source() {
                return source;
        }
}
