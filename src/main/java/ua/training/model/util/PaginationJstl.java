package ua.training.model.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class PaginationJstl extends TagSupport {
    private static final Logger LOG = LogManager.getLogger(PaginationJstl.class);

    private int pageNumber;
    private int currentPage;

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();

            out.write("<form id='paginateForm' method='post'>");
            out.write("<nav style='margin-top: 20px;' aria-label='Page navigation'>");
            out.write("<ul class='pagination justify-content-center'>");

            if (currentPage != 1) {
                out.write("<li class='page-item'>");
                out.write("<a class='page-link' href='#' aria-label='Previous' style='padding:0;'>");
                out.write("<input type='submit' name='page" + (currentPage - 1) + "' value='&laquo;' style='padding: .5rem .75rem; color: #007bff; background-color: #fff; border:0;'/>");
                out.write("</a>");
                out.write("</li>");
            }

            for (int i = 1; i <= pageNumber; i++) {
                if (i == currentPage) {
                    out.write("<li class='page-item active' aria-current='page'>");
                    out.write("<a class='page-link' href='#'>");
                    out.write(Integer.toString(i));
                    out.write("<span class='sr-only'>(current)</span>");
                    out.write("</a>");
                    out.write("</li>");

                    continue;
                }

                out.write("<li class='page-item'>");
                out.write("<a class='page-link' href='#' style='padding:0;'>");
                out.write("<input type='submit' name='page" + i + "' value='" + Integer.toString(i) + "' style='padding: .5rem .75rem; color: #007bff; background-color: #fff; border:0;'/>");
                out.write("</a>");
                out.write("</li>");
            }

            if (currentPage != pageNumber) {
                out.write("<li class='page-item'>");
                out.write("<a class='page-link' href='#' aria-label='Next' style='padding:0;'>");
                out.write("<input type='submit' name='page" + (currentPage+1) + "' value='&raquo;'>");
                out.write("</input>");
                out.write("</a>");
            }

            out.write("</li>");
            out.write("</ul></nav>");
            out.write("</form>");
        } catch (IOException ex) {
            LOG.error("Exception in JSTL {}", ex);
        }
        return SKIP_BODY;
    }
}
