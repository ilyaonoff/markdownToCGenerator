package ru.java.markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TableOfContent {
    static final int MIN_LEVEL = 1;
    List<Heading> headings = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ListIterator<Heading> iter = headings.listIterator(); iter.hasNext();) {
            builder.append(iter.next().getStringRepresentation(0, iter.previousIndex() + 1));
        }
        return builder.toString();
    }

    public void addHeading(Heading heading) {
        heading.setLevel(MIN_LEVEL);
        headings.add(heading);
    }

    public boolean isEmpty() {
        return headings.isEmpty();
    }
}
