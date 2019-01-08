package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Announcement implements Serializable {
    private Integer id;
    private String title;
    private String content;

    public String renderContentHtml() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
