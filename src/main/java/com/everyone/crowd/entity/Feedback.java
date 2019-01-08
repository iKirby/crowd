package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Feedback implements Serializable {
    private Integer id;
    private Integer userId;
    private String url;
    private String title;
    private Date submitTime;
    private String content;
    private String reply;

    public String renderContentHtml() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public String renderReplyHtml() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
