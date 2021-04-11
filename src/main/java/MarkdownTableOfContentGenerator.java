import java.io.File;
import java.util.function.Consumer;

public class MarkdownTableOfContentGenerator {
    public static TableOfContent getTableOfContent(File file) throws ParserException {
        TableOfContent toc = new TableOfContent();

        MarkdownHeadingParser.parseFile(file, new Consumer<>() {
            Heading parentHeading = null;

            @Override
            public void accept(Heading heading) {
                while (parentHeading != null && parentHeading.getLevel() >= heading.getLevel()) {
                    parentHeading = parentHeading.getParent();
                }
                if (parentHeading == null) {
                    toc.addHeading(heading);
                } else {
                    parentHeading.addSubheading(heading);
                }
                parentHeading = heading;
            }
        });

        return toc;
    }
}
