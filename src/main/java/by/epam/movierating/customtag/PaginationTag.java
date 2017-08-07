package by.epam.movierating.customtag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.Writer;

/**
 * @author serge
 *         05.08.2017.
 */
public class PaginationTag extends SimpleTagSupport {
    private String uri;
    private String previous = "Previous";
    private String next = "Next";
    private int pageCount;
    private static final int PAGE_COUNT = 3;
    private int currentPage;

    /**
     * Returns an instance of {@link Writer} object
     *
     * @return {@link Writer} object
     */
    private Writer getWriter() {
        return getJspContext().getOut();
    }

    /**
     * Processes this tag
     *
     * @throws JspException
     */
    @Override
    public void doTag() throws JspException {
        Writer out = getWriter();

        try {
            out.write("<nav style=\"text-align: center;\">");
            out.write("<ul class=\"pagination\">");
            out.write(constructPrevLink());
            for (int i = 0; i < pageCount; i++) {
                out.write(constructLink(i));
            }
            out.write(constructNextLink());
            out.write("</ul>");
            out.write("</nav>");
        } catch (java.io.IOException e) {
            throw new JspException("Error in pagination tag", e);
        }
    }

    /**
     * Builds previous link
     *
     * @return representation of previous link
     */
    private String constructPrevLink() {
        StringBuilder link = new StringBuilder("<li");
        if (currentPage <= 1) {
            link.append(" class=\"disabled\"");
        }
        link.append("><a href=").append(uri).append("&curPageNumber=").
                append(currentPage - 1).append(">").append(previous).append("</a></li>");

        return link.toString();
    }

    /**
     * Builds next link
     *
     * @return representation of next link
     */
    private String constructNextLink() {
        StringBuilder link = new StringBuilder("<li");
        if (currentPage >= pageCount) {
            link.append(" class=\"disabled\"");
        }
        link.append("><a href=").append(uri).append("&curPageNumber=").
                append(currentPage + 1).append(">").append(next).append("</a></li>");

        return link.toString();
    }

    /**
     * Builds link
     *
     * @param page the page of this link
     * @return representation of this link
     */
    private String constructLink(int page) {
        StringBuilder link = new StringBuilder("<li");
        if ((page + 1) == currentPage) {
            link.append(" class=\"active\"");
        }
        link.append("><a href=").append(uri).append("&curPageNumber=").
                append(page + 1).append(">").append(page + 1).append("</a></li>");

        return link.toString();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
