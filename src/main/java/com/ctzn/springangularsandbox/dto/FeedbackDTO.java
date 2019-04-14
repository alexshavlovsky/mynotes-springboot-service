package com.ctzn.springangularsandbox.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class FeedbackDTO {
    @NotNull
    @Email
    private String from;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Size(min = 10, max = 1000)
    private String text;

    public FeedbackDTO() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FeedbackDTO(String from, String name, String text) {
        this.from = from;
        this.name = name;
        this.text = text;
    }

    @Override
    public String toString() {
        return "FeedbackDTO{" +
                "from='" + from + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackDTO that = (FeedbackDTO) o;
        return from.equals(that.from) &&
                name.equals(that.name) &&
                text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, name, text);
    }
}
