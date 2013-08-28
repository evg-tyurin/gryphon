package gryphon.web;

import gryphon.Application;
import gryphon.common.GryphonNames;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class JspUtil
{
    public JspUtil()
    {
    }

    public static Application getApplication(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return (WebApplication) request.getSession().getAttribute(GryphonNames.APPLICATION_NAME);
    }
}