package gryphon.web;

import gryphon.*;

/**
 * @author ET
 */

public interface WebApplication extends Application, Cloneable
{
    WebApplication cloneMe() throws Exception;

}