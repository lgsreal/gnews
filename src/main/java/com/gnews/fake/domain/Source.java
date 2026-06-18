package com.gnews.fake.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sources")
public class Source {
        @Id
        private String id;
        private String name;
        private String url;
        private String country;

        public Source() {
        }

        public Source(String id, String name, String url, String country) {
                this.id = id;
                this.name = name;
                this.url = url;
                this.country = country;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public String getCountry() {
                return country;
        }

        public void setCountry(String country) {
                this.country = country;
        }

        // Compatibility methods to minimize refactoring pain if possible, but better to
        // fix properly.
        public String id() {
                return id;
        }

        public String name() {
                return name;
        }

        public String url() {
                return url;
        }

        public String country() {
                return country;
        }

}
