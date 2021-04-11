package ru.java.markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Heading {
    private int level = 0;
    private Heading parent = null;
    private String name;
    private String id;
    private final List<Heading> subheadings = new ArrayList<>();

    public StringBuilder getStringRepresentation(int indent, int number) {
        StringBuilder builder = new StringBuilder();
        builder.append("\t".repeat(indent))
                .append(number)
                .append(". [").append(name).append("](#")
                .append(id).append(")\n");
        for (ListIterator<Heading> iter = subheadings.listIterator(); iter.hasNext();) {
            builder.append(iter.next().getStringRepresentation(indent + 1, iter.previousIndex() + 1));
        }
        return builder;
    }

    public Heading getParent() {
        return parent;
    }

    public void setParent(Heading parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSubheading(Heading subheading) {
        subheading.setParent(this);
        subheading.setLevel(level + 1);
        subheadings.add(subheading);
    }
}
